// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.util.PoleFinder;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
public class Align extends CommandBase {
  public LimelightSubsystem m_limelight;
  /** Creates a new Align. */
  public Align(LimelightSubsystem m_Limelight) {
   
    this.m_limelight = m_Limelight;
    PoleFinder poleFinder = new PoleFinder();
    // Use addRequirements() here to declare subsystem dependencies.
   

  }

  //Plan for align: first find if there are targets, pick the lowest one and center the robot to it. All heights for the robot will be based off of that. Afterwards we can add the ability to change height based off of distance, however we should initially just have code to do it from a set position

  //Might link this to a button press, like y or something. When its pressed if it sees a target within range it makes that the set point of the swerve drive MAKE SURE POSE IS FACING THE RIGHT WAY

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute(){
    PathPlannerTrajectory poleLocation = PathPlanner.generatePath(new PathConstraints( 5, 5), new PathPoint(new Translation2d(PoleFinder.getNearestPole().getX(), PoleFinder.getNearestPole().getY())), PoleFinder.getNearestPole().getRotation())


    // if (RobotContainer.m_controller.getYButtonPressed() == true) {
    //   if (m_limelight.hasValidTarget()){
    //     if (m_limelight.getArea())
    //   }

    // }
 }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
