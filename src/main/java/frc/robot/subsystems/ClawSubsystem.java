// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.CurrentSpikeCounter;

public class ClawSubsystem extends SubsystemBase {
    private static WPI_TalonFX m_claw; // intake motor
    private static CurrentSpikeCounter spikeCounter = new CurrentSpikeCounter(Constants.SlideConstants.CurrentThreshold,Constants.SlideConstants.CurrentDeadband);

    /** Creates a new IntakeSubsystem. */
    public ClawSubsystem() {
        m_claw = new WPI_TalonFX(Constants.ClawConstants.kClawID);
        m_claw.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Purpose: To rotate motor
     *  
     * @param percentOutput: Percent of output using Motor Controller
     */
    public void setIntake(double percentOutput) {
        m_claw.set(ControlMode.PercentOutput, percentOutput);
    }
    /**
     * Purpose: To return current motor output voltage to use as de facto sensor
     * @return: Current motor voltage 
     */
    public double getMotorOutputVoltage(){
        return m_claw.getMotorOutputVoltage();
    }
    public boolean isPieceIntaken(){
        return spikeCounter.update(m_claw.getStatorCurrent(), false); 

    }

    

    @Override
    public void periodic() {
        SmartDashboard.putNumber("ClawVoltage", m_claw.getMotorOutputVoltage());
        SmartDashboard.putNumber("ClawTempurature", m_claw.getTemperature());

        // This method will be called once per scheduler run
    }
}
