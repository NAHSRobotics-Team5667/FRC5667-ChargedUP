// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SlideSubsystem extends SubsystemBase {
  private WPI_TalonFX m_rightSlide;
  private WPI_TalonFX m_leftSlide;
  private WPI_TalonFX m_tilt;
  
  /** Creates a new SlideSubsystem. */
  public SlideSubsystem() {
    m_leftSlide = new WPI_TalonFX(Constants.SlideConstants.kLSlideID);
    m_leftSlide.setNeutralMode(NeutralMode.Brake);
    m_rightSlide = new WPI_TalonFX(Constants.SlideConstants.kRSlideID);
    m_rightSlide.setNeutralMode(NeutralMode.Brake);
    m_tilt = new WPI_TalonFX(Constants.SlideConstants.kTiltID);
    m_tilt.setNeutralMode(NeutralMode.Brake);
  }

  public void setSlide(double percentOutput){
    m_leftSlide.set(ControlMode.PercentOutput, percentOutput);
    m_rightSlide.set(ControlMode.PercentOutput, -percentOutput);
  }

  public void setTilt(double percentOutput){
    m_tilt.set(ControlMode.PercentOutput, percentOutput);
  }

  //make a function that gets the number of ticks
  //make a functon that set motor to number of ticks
  //make a function that designates levels for game 
  //try to optimize
  //
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}