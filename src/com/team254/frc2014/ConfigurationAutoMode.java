package com.team254.frc2014;

import com.team254.frc2014.AutoModeSelector.Configuration;

/**
 * LanedAutoMode.java
 * LanedAutoMode defines all autonomi which utilize lanes.
 * @author tombot
 */
public abstract class ConfigurationAutoMode extends AutoMode {

  public ConfigurationAutoMode(String d) {
    super(d);
  }

  public static final int MIDDLE_LANE = 0;
  public static final int WALL_LANE = 1;
  
  protected Configuration config;

  public void setConfiguration(Configuration config) {
    this.config = config;
  }
}