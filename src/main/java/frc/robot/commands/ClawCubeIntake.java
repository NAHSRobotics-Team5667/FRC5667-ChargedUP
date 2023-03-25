// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotContainer.GamePiece;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.WristSubsystem;

public class ClawCubeIntake extends CommandBase {
    // this is a copy of ClawConeIntake, with minor adjustments. It should be
    // refactored to a single command with a piece type parameter.
    public IntakeSubsystem clawSubsystem;

    public WristSubsystem wrist;
    public boolean isCube;

    public RobotContainer robotContainer;
    // these will be the heights of the slide at different points. The height will
    // be set as ClawConstants.ClawSetpoints[bumperPos]

    /** Creates a new SlideIntakeAndOuttakeCommand. */
    public ClawCubeIntake(IntakeSubsystem clawSubsystem, WristSubsystem wrist, boolean isCube,
            RobotContainer robotContainer) {
        this.clawSubsystem = clawSubsystem;
        this.wrist = wrist;
        this.robotContainer = robotContainer;

        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        robotContainer.setCurrentElement(GamePiece.NONE);
        robotContainer.setTargetElement(GamePiece.CUBE);
        Lights lightstrip = robotContainer.lightstrip;
        lightstrip.scheduler.setLightEffect(() -> {
            lightstrip.flashingRGB(194, 3, 252);
        }, 2, 15, .1);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // runs until current spikes
        // NOTE: Change this to use time of flight sensor
        if (clawSubsystem.intake.getStatorCurrent() < 30) {
            clawSubsystem.setIntake(-.45);
        } else {
            robotContainer.setCurrentElement(GamePiece.CUBE);
            robotContainer.setTargetElement(GamePiece.NONE);
            robotContainer.intakeFinish = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        robotContainer.intakeFinish = false;
        clawSubsystem.setIntake(0);
        robotContainer.setCurrentElement(GamePiece.CUBE);
        robotContainer.setTargetElement(GamePiece.NONE);

        robotContainer.setPositionLevel(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
