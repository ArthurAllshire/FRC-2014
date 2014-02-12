package com.team254.frc2014.subsystems;

import com.team254.frc2014.Constants;
import com.team254.lib.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import java.util.Hashtable;
/**
 *
 * @author Mani Gnanasivam
 */
public class Clapper extends Subsystem{
  public static Solenoid frontSolenoid = new Solenoid(Constants.frontClapperSolenoidPort.getInt());
  public static Solenoid rearSolenoid = new Solenoid(Constants.rearClapperSolenoidPort.getInt());
  public static boolean frontIsUp;
  public static boolean rearIsUp;
  
  public Hashtable serialize() {
    return new Hashtable();
  }
  
  public Clapper() {
    super("Clapper");
  }
  
  public static void clap(){
    setStates(true,true);
  }
  
  public static void antiClap(){
    setStates(false,false);
  }
  
  public static void setStates(boolean frontUp,boolean rearUp){
    frontSolenoid.set(frontUp);
    rearSolenoid.set(rearUp);
    frontIsUp = frontUp;
    rearIsUp = rearUp;
  }
}
