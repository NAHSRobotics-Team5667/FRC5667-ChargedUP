// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.SlideConstants;
import static frc.robot.RobotContainer.GamePiece.*;
import frc.robot.RobotContainer.GamePiece;
import frc.robot.subsystems.SlideSubsystem;

public class SlideCommand extends CommandBase {
    private SlideSubsystem slide;
    private RobotContainer robotContainer;
    private boolean autoOverride = false, isCubeAuto = false, isIntake, hasTension, hasZeroed;
    private int positionAuto = 0, positionLevel = 0;
    private double timeout = 0, clock = 0, startTime;
    private double prevPosition = 0;
    private double delay;

    /**
     * Create a slide command. Use .withTimeout to end command in auto.
     * 
     * @param slide          slide object.
     * @param robotContainer robot container
     * @param autoOverride   whether auto is running
     * @param positionAuto   scoring level
     * @param isCubeAuto     cube or cone
     * @param isIntake       intaking or outtaking
     * @param delay          delay before command starts
     * @param timeout        time wrist runs after delay before going back to
     *                       position level 0
     */
    public SlideCommand(SlideSubsystem slide, RobotContainer robotContainer, boolean autoOverride, int positionAuto,
            boolean isCubeAuto, boolean isIntake, double delay, double timeout) {
        this.robotContainer = robotContainer;
        this.isCubeAuto = isCubeAuto;
        this.isIntake = isIntake;
        this.slide = slide;
        this.autoOverride = autoOverride;
        this.positionAuto = positionAuto;
        this.timeout = timeout;
        this.delay = delay;
        hasTension = false;
        hasZeroed = false;

        addRequirements(slide);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();

        hasTension = false; // initialize boolean checkers just in case
        hasZeroed = false;
        slide.setSlide(0);

        if (autoOverride) {
            if (!isIntake) {
                robotContainer.setCurrentElement(isCubeAuto ? CUBE : CONE);
                robotContainer.setTargetElement(NONE);
            } else {
                robotContainer.setCurrentElement(NONE);
                robotContainer.setTargetElement(isCubeAuto ? CUBE : CONE);
            }
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Uncomment the following to control slide with stick
        // slide.setSlide(robotContainer.slideController.getLeftY() / 3);

        double position = 0; // initialize variable to hold position of slide
        boolean bottomLimitSwitch = slide.getBottomLimitSwitch();

        // this is what nightmares are made of
        if (!hasZeroed) { // slide has not zeroed
            if (!hasTension) { // string may not have tension
                if (bottomLimitSwitch) { // bottom limit switch is hit
                    slide.setSlide(0.1); // go up slowly
                } else { // bottom limit switch is not hit
                    hasTension = true; // string has tension
                }
            } else { // string has tension
                if (!bottomLimitSwitch) { // bottom limit switch is not hit
                    slide.setSlide(-0.1); // go down slowly
                } else { // bottom limit switch is hit
                    hasZeroed = true; // slide has completed zeroing procedure
                    slide.setSlide(0); // stop slide in case motors have not updated
                }
            }
        } else {
            clock = Timer.getFPGATimestamp() - startTime;

            if (autoOverride) {
                if (clock > delay && (clock - delay) < timeout) {
                    positionLevel = positionAuto;
                } else {
                    positionLevel = 0;
                }
            } else {
                positionLevel = robotContainer.getPositionLevel();
            }

            if (positionLevel == 0) {
                position = -0.5; // set slide to go to -1 inches - eliminates any error
            } else { // position level is not equal to 0
                GamePiece targetElement = robotContainer.getTargetElement(),
                        currentElement = robotContainer.getCurrentElement();

                if (targetElement.equals(CONE)) {
                    position = SlideConstants.coneIntakeSetpoints[positionLevel - 1]; // length =
                    // 3
                } else if (targetElement.equals(CUBE)) {
                    position = SlideConstants.cubeIntakeSetpoints[positionLevel - 1]; // length =
                    // 1
                } else if (currentElement.equals(CONE)) {
                    position = SlideConstants.coneOuttakeSetpoints[positionLevel - 1]; // length
                    // = 3
                } else if (currentElement.equals(CUBE)) {
                    position = SlideConstants.cubeOuttakeSetpoints[positionLevel - 1]; // length
                    // = 3
                }

                // else { // current element is NONE and target element is NONE
                // position = -1; // reset position
                // }
            }

            if (prevPosition != position) {
                slide.resetPID();
            }

            slide.setSlidePIDInches(position); // set slide to go to position
        }

        if (RobotContainer.slideController.getLeftStickButtonPressed()) {
            hasZeroed = false;
            hasTension = false;
        }

        SmartDashboard.putBoolean("Has Zeroed", hasZeroed);
        SmartDashboard.putBoolean("Has Spool", hasTension);

        prevPosition = position;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        slide.setSlide(0); // stop slide
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
