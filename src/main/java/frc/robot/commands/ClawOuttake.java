// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.RobotContainer.GamePiece.*;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotContainer.GamePiece;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Lights;

public class ClawOuttake extends CommandBase {
    double stopClock;
    IntakeSubsystem claw;
    RobotContainer robotContainer;
    Lights lightstrip;
    GamePiece gamePiece;

    /** Creates a new IntakeOuttakeProcessClaw. */
    public ClawOuttake(GamePiece gamePiece, IntakeSubsystem claw, RobotContainer robotContainer) {
        this.gamePiece = gamePiece;
        this.claw = claw;
        this.robotContainer = robotContainer;
        this.lightstrip = robotContainer.lightstrip;
        addRequirements(claw);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        stopClock = 0;
        lightstrip.scheduler.setLightEffect(() -> {
            lightstrip.setSolidRGB(0, 255, 0);
        }, .6, 25, .14);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // runs for set time
        double intakeSpeed = 0;

        if (gamePiece == CUBE) {
            intakeSpeed = 1;
        } else if (gamePiece == CONE) {
            intakeSpeed = -0.45;
        }

        claw.setIntake(intakeSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        robotContainer.outtakeFinish = false;
        claw.setIntake(0);
        robotContainer.setPositionLevel(0);
        robotContainer.setCurrentElement(GamePiece.NONE);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
