package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.ControlOutput;
import com.team254.lib.ControlSource;
import com.team254.lib.Loopable;
import com.team254.lib.Subsystem;
import com.team254.lib.util.ThrottledPrinter;
import com.team254.lib.util.Util;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import java.util.Hashtable;

/**
 * This represents the electrical components of the shooter.
 * @author bg
 */
public class Shooter extends Subsystem implements Loopable, ControlOutput, ControlSource {

  private Solenoid hood = new Solenoid(Constants.hoodSolenoidPort.getInt());
  private Solenoid catcher = new Solenoid(Constants.catcherSolenoidPort.getInt());
  private Talon shooterA = new Talon(Constants.leftShooterWheelPort.getInt());
  private Talon shooterB = new Talon(Constants.rightShooterWheelPort.getInt());
  public Counter counter = new Counter(Constants.shooterReflectorPort.getInt());
  private ThrottledPrinter p = new ThrottledPrinter(.1);
  
  private void setShooterPower(double power) {
    power = Util.limit(power, 1.0);
    shooterA.set(-power);
    shooterB.set(power);
  }

  public Shooter() {
    super("Shooter");
    if (controller != null) {
      controller.enable();
    }
    counter.start();
  }

  public Hashtable serialize() {
    return new Hashtable();
  }

  public void set(double value) {
    setShooterPower(value);
  }
  
  public void setHood(boolean up) {
    hood.set(up);
  }
  
  public double lastRpm = 0;

  public void setCatcher(boolean down) {
    catcher.set(down);
  }
  public double get() {
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
    return ret;
  }
  
  public void update() {
    updateVelocity();
    super.update();
  }

  public double updateVelocity() {
    int kCountsPerRev = 1;
    double period = counter.getPeriod();
    double rpm = 60.0 / (period * (double) kCountsPerRev);
    if (rpm < 20000) {
      lastRpm = rpm;
    }
    double ret = (lastRpm * Math.PI * 2.0) / 60.0;
    return ret;
  }

  public void updateFilter() {
  }

  public boolean getLowerLimit() {
    return false;
  }

  public boolean getUpperLimit() {
    return false;
  }
  
  public double getLastRpm() {
    return lastRpm;
  }
}
