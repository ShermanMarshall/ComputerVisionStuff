from cv2 import *
import numpy as np

cam = VideoCapture(0)
a, b = cam.read()
num = 0;

#args: 	image - OpenCV Image
#	sigma - Normalized value determining gradient sharpness
#	      - lower sigma corresponds with tighter threshold (more black)
#	      - higher sigma corresponds with lower threshold (more grey)
#	med   - median pixel intensity (color) for the image
def toCanny(image, sigma, med):
	lower = int(min(0, (1.0 - sigma) * med)[0])
	upper = int(max(255, (1.0 + sigma) * med)[0])
	return Canny(image, lower, upper)

print("Press esc (on window) to exit")
while num != 27:
	cannyImg = toCanny(b, 0.33, np.median(b))
	imshow("Canny Detector", cannyImg)
	num = waitKey(1)
	a, b = cam.read()

destroyAllWindows()
cam.close()

