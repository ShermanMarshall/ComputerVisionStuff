#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <iostream>

using namespace std;
using namespace cv;

int main(int argc, char** argv) {

	VideoCapture cap(0); // open the default camera

	if (!cap.isOpened()) { // check if they succeeded
		return -1;
	}
	
	Mat edges, frame;

	namedWindow("Canny", 1);

	for(;;) {
		cap >> frame; // get a new frame from camera
		cvtColor(frame, edges, COLOR_BGR2GRAY);
		GaussianBlur(edges, edges, Size(7,7), 1.5, 1.5);
		Canny(edges, edges, 0, 30, 3);
		imshow("Canny", edges);

		if (waitKey(1) == 27) {
			break;
		}
	}
	//at this point the camera will be deinitialized 
	//automatically in VideoCapture destructor

	return 0;
}
