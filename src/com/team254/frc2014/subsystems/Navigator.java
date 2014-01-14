package com.team254.frc2014.subsystems;

import com.team254.lib.util.ThrottledPrinter;

/**
 * Navigator.java
 *
 * @author tombot
 */
public class Navigator {

  double x = 0, y = 0, heading = 0;
  double lastL, lastR;
  double gyroReference = 0;
  ThrottledPrinter p = new ThrottledPrinter(.125);

  public void resetWithReferenceHeading(double reference) {
    x = y = heading = 0;
    gyroReference = reference;
  }
  
  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getHeading() {
    return heading;
  }

  private double degreesToRadians(double deg) {
    return (deg * Math.PI) / 180.0;
  }

  public void update(double left, double right, double gyroAngle) {
    double diffL = left - lastL;
    double diffR = right - lastR;
    lastL = left;
    lastR = right;
    heading = gyroAngle - gyroReference;
    double magnitude = (diffL + diffR) / 2.0;
    //p.println("x: " + x + " | y:" + y + " h: " + heading);
    x += magnitude * Math.sin(degreesToRadians(heading));
    y += magnitude * Math.cos(degreesToRadians(heading));
  }

  public String toString() {
    return "X: " + x + " Y: " + y + " Heading: " + heading;
  }
}
