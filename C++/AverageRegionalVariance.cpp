#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <pthread.h>
#include <stdlib.h>
#include <math.h>

using namespace std;
using namespace cv;

const int  numThreads = 16, byteSize = 256, iterations = 30;
int partitionWidth, partitionHeight, iterate = 0;

int redSet[numThreads][iterations];
int greenSet[numThreads][iterations];
int blueSet[numThreads][iterations];

Mat frame, current, last;
typedef struct _data {
	pthread_mutex_t* mutex;
	int num, startX, startY;
	int currentRed[byteSize], currentBlue[byteSize], currentGreen[byteSize], lastRed[byteSize], lastBlue[byteSize], lastGreen[byteSize];
} data;

void*thread_func(void*);

void* thread_func(void* arg) {
	data* partition = (data*) arg;

	//printf("In thread %i\n", partition->num);
	partition->startY = partitionHeight * (partition->num / (sqrt(numThreads)));
	partition->startX = partitionWidth * (partition->num % ((int) sqrt(numThreads)));

	int status;
	while (iterate < iterations) {
		status = pthread_mutex_lock(partition->mutex);
		if (status > 0) {
			printf("Error: %i locking mutex in thread: %i. aborting\n",
				status, partition->num);
			return arg;
		}
		
		Vec3b values;
		//add up all new values for this partition;
		for (int y = partition->startY, maxY = (partition->startY + partitionHeight); y < maxY; y++) {
			for (int x = partition->startX, maxX = (partition->startX + partitionWidth); x < maxX; x++) {
				values = frame.at<Vec3b>(y,x);
				partition->currentBlue[values[0]]++;
				partition->currentGreen[values[1]]++;
				partition->currentRed[values[2]]++;
			}
		}
	
		redSet[partition->num][iterate] = 0;
		blueSet[partition->num][iterate] = 0;
		greenSet[partition->num][iterate] = 0;

		//Store the differences between images in the set of all of the data
		//reset the current array of values to the last
		for (int x = 0; x < byteSize; x++) {
			redSet[partition->num][iterate] += abs(partition->currentRed[x] - partition->lastRed[x]);
			blueSet[partition->num][iterate] += abs(partition->currentBlue[x] - partition->lastBlue[x]);
			greenSet[partition->num][iterate] += abs(partition->currentGreen[x] - partition->lastGreen[x]);

			partition->lastRed[x] = partition->currentRed[x];
			partition->lastGreen[x] = partition->currentGreen[x];
			partition->lastBlue[x] = partition->currentBlue[x];

			partition->currentRed[x] = partition->currentGreen[x] = partition->currentBlue[x] = 0;
			
			//Terminate threads if main thread exited
			if (!(iterations > iterate)) {
				break;
			}
			//printf("Diffs %i\tRed: %i, Green: %i, Blue: %i\n", iterate, redSet[partition->num][iterate], [partition->num][iterate], blueSet[partition->num][iterate]);
		}

		printf("Diffs %i\tRed: %i, Green: %i, Blue: %i\n", iterate, redSet[partition->num][iterate], greenSet[partition->num][iterate], blueSet[partition->num][iterate]);
	}
}

int main(int argc, char** argv) {
	int imgHeight, imgWidth;

	pthread_t threads[numThreads];
	pthread_mutex_t mutexes[numThreads];
	data* partitions[numThreads];

	VideoCapture cap(0); // open the default camera

	if (!cap.isOpened()) { // check if we succeeded
		return -1;
	}
	
	cap >> frame;
	GaussianBlur(frame, frame, Size(7,7), 1.5, 1.5);
	GaussianBlur(frame, frame, Size(7,7), 1.5, 1.5);
	
	imgHeight = frame.size().height;
	imgWidth = frame.size().width;
	
	namedWindow("Picture", 1);
	
	partitionWidth = imgWidth / numThreads;
	partitionHeight = imgHeight / numThreads;

	int keyValue;
	//initialize the partitions;
	for (int x = 0, err; x < numThreads; x++) {
		partitions[x] = (data*) malloc(sizeof(data));
		partitions[x]->num = x;

		partitions[x]->mutex = &mutexes[x];
		err = pthread_mutex_init(partitions[x]->mutex, NULL);
		if (err != 0) {
			printf("Error: %i initialing mutex %i\n", err, x);
			return 0;
		} 

		for (int idx = 0; idx < byteSize; idx++) {
			partitions[x]->currentRed[idx] = partitions[x]->lastRed[idx] = partitions[x]->currentBlue[idx] = 
			partitions[x]->lastBlue[idx] = partitions[x]->currentGreen[idx] = partitions[x]->lastGreen[idx] = 0;
		}
	
		if (x > 0) {
			pthread_mutex_lock(partitions[x]->mutex);
			pthread_create(&threads[x], NULL, &thread_func, partitions[x]);
		} else {
			partitions[x]->startX = partitionWidth * (partitions[x]->num % ((int)sqrt(numThreads)));
			partitions[x]->startY = partitionHeight * (partitions[x]->num / (sqrt(numThreads)));
		}
	}

	do {
		imshow("Picture", frame);
		keyValue = waitKey(1);

		for (int x = 1; x < numThreads; x++) {
			pthread_mutex_unlock(partitions[x]->mutex);
		}

		Vec3b values;
		//add up all new values for this partition
		for (int y = partitions[0]->startY, maxY = (partitions[0]->startY + partitionHeight); y < maxY; y++) {
			for (int x = partitions[0]->startX, maxX = (partitions[0]->startX + partitionWidth); x < maxX; x++) {
				values = frame.at<Vec3b>(y, x);
				partitions[0]->currentBlue[values[0]]++;
				partitions[0]->currentGreen[values[1]]++;
				partitions[0]->currentRed[values[2]]++;
			}	
		}

		//Store the differences between images in the set of all of the data
		//reset the current array of values to the last
		for (int x = 0; x < byteSize; x++) {
			redSet[0][iterate] += abs(partitions[0]->currentRed[x] - partitions[0]->lastRed[x]);
			greenSet[0][iterate] += abs(partitions[0]->currentGreen[x] - partitions[0]->currentGreen[x]);
			blueSet[0][iterate] += abs(partitions[0]->currentBlue[x] - partitions[0]->currentBlue[x]);
			
			partitions[0]->currentRed[x] = partitions[0]->currentGreen[x] = partitions[0]->currentBlue[x] = 0;
			//printf("Diffs %i\tRed: %i, Green: %i, Blue: %i\n", iterate, redSet[0][iterate], greenSet[0][iterate], blueSet[0][iterate]);
		}
		cap >> frame;
		
	} while (keyValue != 27 && iterate++ <= iterations);

	iterate = iterations;

	double red, green, blue, avgRed, avgGreen, avgBlue;
	
	for (int x = 20; x < iterations; x++) {
		for (int y = 0; y < 16; y++) {
			red += ((double) redSet[y][x]) / 16.0;
			blue += ((double) blueSet[y][x]) / 16.0;
			green += ((double) greenSet[y][x]) / 16.0;
		}
		
		if (x < 22) {
			avgRed += red;
			avgGreen += green;
			avgBlue += blue;
		} else {
			avgRed *= x - 1;
			avgRed += red;
			avgRed /= x;

			avgGreen *= x - 1;
			avgGreen += green;
			avgGreen /= x;
		
			avgBlue *= x - 1;
			avgBlue += blue;
			avgBlue /= x;

			//printf("Diff #: %i:\tRed: %i, Green: %i, Blue: %i\n", x, red, green, blue);
		}
		red = blue = green = 0;
	}
	printf("Averages\n--------\nRed: %.3f, Green: %.3f, Blue: %.3f\n", avgRed, avgGreen, avgBlue);
	return 0;
}
