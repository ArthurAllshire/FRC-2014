package com.team254.frc2014.controllers;

import com.team254.lib.Controller;
import edu.missdaisy.utilities.DaisyMath;
import edu.missdaisy.utilities.Trajectory;
import edu.missdaisy.utilities.TrajectoryFollower;

/**
 * DriveController.java
 *
 * @author tombot
 */
public class DriveController extends Controller {

  public DriveController() {
    init();
  }
  Trajectory trajectory;
  TrajectoryFollower followerLeft = new TrajectoryFollower();
  TrajectoryFollower followerRight = new TrajectoryFollower();
  double direction;
  double heading;
  double kTurn = 1.0/23.0;

  public boolean onTarget() {
    return followerLeft.isFinishedTrajectory();// && mFollower.onTarget(distanceThreshold);
  }

  private void init() {
    followerLeft.configure(.65, 0, 0, 0.06666666666667, 1.0/45.0);
    followerRight.configure(.65, 0, 0, 0.06666666666667, 1.0/45.0);
  }

  public void loadProfile(Trajectory leftProfile, Trajectory rightProfile, double direction, double heading) {
    reset();
    followerLeft.setTrajectory(leftProfile);
    followerRight.setTrajectory(rightProfile);
    this.direction = direction;
    this.heading = heading;
  }

  public void reset() {
    followerLeft.reset();
    followerRight.reset();
    drivebase.resetEncoders();
  }

  public void update() {
    if (!enabled) {
      return;
    }
    //System.out.println(this.onTarget() + " " + mFollower.isFinishedTrajectory() + " " + mFollower.onTarget(1.0));
    if (onTarget()) {
      drivebase.setLeftRightPower(0.0, 0.0);
    } else {
      double distanceL = direction * drivebase.getLeftEncoderDistance();
      double distanceR = direction * drivebase.getRightEncoderDistance();
      

      double speedLeft = direction * followerLeft.calculate(distanceL);
      double speedRight = direction * followerRight.calculate(distanceR);
      
      double angleDiff = DaisyMath.boundAngleNeg180to180Degrees(followerLeft.getHeading() - drivebase.getGyroAngle());
      double turn = kTurn * angleDiff;
      drivebase.setLeftRightPower(speedLeft + turn, speedRight - turn);
    }
  }

  public void setTrajectory(Trajectory t) {
    this.trajectory = t;
  }
}
