from cv2 import *
cam = VideoCapture(0)
a, b = cam.read()
num = 0;

while num != 27:
	imshow('Your Camera', b)
	num = waitKey(1)
	a, b = cam.read();

destroyAllWindows();
cam.close();
