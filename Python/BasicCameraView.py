from cv2 import *
cam = VideoCapture(0)
a, b = cam.read()
num = 0;

numESC = 27

while num != numESC:
	imshow('Your Camera', b)
	num = waitKey(1)
	a, b = cam.read();

destroyAllWindows();
cam.close();
