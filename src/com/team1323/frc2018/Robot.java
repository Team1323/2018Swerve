/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team1323.frc2018;

import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.subsystems.Swerve;
import com.team1323.io.Xbox;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private Swerve swerve;
	private Looper swerveLooper = new Looper();
	private Xbox driver;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		swerve = Swerve.getInstance();
		driver = new Xbox(0);
		driver.setDeadband(0.0);
		swerve.registerEnabledLoops(swerveLooper);
		swerve.zeroSensors();
	}
	
	private void allPeriodic(){
		swerve.outputToSmartDashboard();
	}
	
	@Override
	public void autonomousInit() {
		swerve.zeroSensors();
		swerveLooper.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		allPeriodic();
	}
	
	@Override
	public void teleopInit(){
		swerveLooper.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		driver.update();
		
		swerve.sendInput(driver.getX(Hand.kLeft), -driver.getY(Hand.kLeft), driver.getX(Hand.kRight), false, driver.leftTrigger.isBeingPressed());
		if(driver.yButton.wasPressed())
			swerve.rotate(0);
		else if(driver.bButton.wasPressed())
			swerve.rotate(90);
		else if(driver.aButton.wasPressed())
			swerve.rotate(180);
		else if(driver.xButton.wasPressed())
			swerve.rotate(270);
		if(driver.backButton.isBeingPressed()){
			swerve.temporarilyDisableHeadingController();
			swerve.zeroSensors(new RigidTransform2d(new Translation2d(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH), Rotation2d.fromDegrees(0)));
		}
		
		allPeriodic();
	}

	@Override
	public void disabledInit(){
		swerveLooper.stop();
		swerveLooper.start();
	}
	
	@Override
	public void disabledPeriodic(){
		allPeriodic();
	}
}
