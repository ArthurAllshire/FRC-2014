package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class TestBumperGather extends ConfigurationAutoMode {

  public TestBumperGather() {
    super("Test Bumper Gather");
  }

  protected void routine() {
    hotGoalDetector.startSampling();
    boolean goLeft = false;
    Timer t = new Timer();
    t.start();
    

    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = true;
    rearIntake.wantBumperGather = true;
    waitTime(.5);
    goLeft = !hotGoalDetector.goLeft();
    System.out.println("Going Left? " + goLeft);
    
    

  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}
