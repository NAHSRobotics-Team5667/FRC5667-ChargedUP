// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClawSubsystem;

public class ClawCubeOuttake extends CommandBase {
  double stopClock;
  ClawSubsystem claw;
  RobotContainer robotContainer;
  /** Creates a new IntakeOuttakeProcessClaw. */
  public ClawCubeOuttake(ClawSubsystem claw, RobotContainer robotContainer ) {
    this.claw = claw;
    this.robotContainer = robotContainer;
    addRequirements(claw);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    stopClock = 0;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (stopClock < 4){
      claw.setIntake(-.45);
      stopClock += .02;

    }else{
      robotContainer.intakeFinish = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    robotContainer.intakeFinish = false;
        claw.setIntake(0);
        robotContainer.inOrOut += 1;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}