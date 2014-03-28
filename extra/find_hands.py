#!/usr/bin/env python
import numpy as np
import cv2 as cv
import socket
import sys
import datetime, time

HOST, PORT = "10.2.54.2", 1180

WIDTH_PX = 1000
WEBCAM_WIDTH_PX = 640
WEBCAM_HEIGHT_PX = 360
X_OFFSET = (WIDTH_PX - WEBCAM_WIDTH_PX)/2

LEFT_UL = (240 + X_OFFSET, 200)
LEFT_LR = (310 + X_OFFSET, 250)

RIGHT_UL = (WEBCAM_WIDTH_PX - 310 + X_OFFSET, 200)
RIGHT_LR = (WEBCAM_WIDTH_PX - 240 + X_OFFSET, 250)

MAX_COLOR_DISTANCE = 20

UPDATE_RATE_HZ = 40.0
PERIOD = (1.0 / UPDATE_RATE_HZ) * 1000.0

def getTimeMillis():
    return int(round(time.time() * 1000))

def color_distance(c1, c2):
    diff = (c1[0]-c2[0])
    while diff < -180:
        diff += 360
    while diff > 180:
        diff -= 360
    return abs(diff)

def color_if_far(img, distance, ul, lr):
    if distance < MAX_COLOR_DISTANCE:
        cv.rectangle(img, ul, lr, (0, 255, 255), -1)
    return distance < MAX_COLOR_DISTANCE

def draw_static(img):
    bg = np.zeros((img.shape[0], WIDTH_PX, 3), dtype=np.uint8)
    bg[:,X_OFFSET:X_OFFSET+WEBCAM_WIDTH_PX,:] = img
    cv.rectangle(bg, LEFT_UL, LEFT_LR, (0, 255, 0), 3)
    cv.rectangle(bg, RIGHT_UL, RIGHT_LR, (0, 255, 0), 3)
    return bg

def detect_color(img, box):
    h = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 0])
    s = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 1])
    v = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 2])
    return (h,s,v)

def detect_colors(img):
    left = detect_color(img, (LEFT_UL, LEFT_LR))
    right = detect_color(img, (RIGHT_UL, RIGHT_LR))

    # print "(%f, %f, %f) (%f, %f, %f)" % (left[0],left[1],left[2],right[0],right[1],right[2])
    return left, right

if __name__ == '__main__':
    cv.namedWindow("img",1)
    capture = cv.VideoCapture(0)

    nom_left = None
    nom_right = None
    
    last_t = getTimeMillis()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((HOST, PORT))
    while 1:
        _, img = capture.read()
        small_img = cv.flip(cv.resize(img, (WEBCAM_WIDTH_PX, WEBCAM_HEIGHT_PX)), 1)
        bg = draw_static(small_img)
        left, right = detect_colors(cv.cvtColor(bg, cv.COLOR_BGR2HSV))
        if nom_left is not None:
            left_dist = color_distance(left, nom_left)
            right_dist = color_distance(right, nom_right)
            
            left_on = color_if_far(bg, left_dist, (0, 0), ((WIDTH_PX-WEBCAM_WIDTH_PX)/2, WEBCAM_HEIGHT_PX))
            right_on = color_if_far(bg, right_dist, ((WIDTH_PX+WEBCAM_WIDTH_PX)/2, 0), (WIDTH_PX, WEBCAM_HEIGHT_PX))
            cur_time = getTimeMillis()
            # Throttle the output
            if last_t + PERIOD <= cur_time: 
                
                try:
                    
                    bytes = bytearray()
                    v = (left_on << 1) | (right_on << 0)
                    bytes.append(v)
                    s.send(bytes)
                    print "dleft: %f | %d, dright: %f | %d" % (left_dist, left_on, right_dist, right_on)
                except:
                    print "Could not connect"
                    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    s.connect((HOST, PORT))
                    
                    #time.sleep(1.0)


                last_t = cur_time

        cv.imshow("img", bg)
        key = cv.waitKey(10) & 255
        if key == 27:
            break
        elif key == ord('c'):
            nom_left = left
            nom_right = right
            print "(%f, %f, %f) (%f, %f, %f)" % (left[0],left[1],left[2],right[0],right[1],right[2])
    s.close()
