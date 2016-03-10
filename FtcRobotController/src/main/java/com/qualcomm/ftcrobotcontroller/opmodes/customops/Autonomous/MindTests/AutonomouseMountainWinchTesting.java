package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.MindTests;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 2/4/16.
 */
public class AutonomouseMountainWinchTesting extends AutonomousMindContainer {

    @Override
    public void setupAutonomous() {
        builder.addDriveAction(DRIVE_FORWARD, 475 * 2, 475 * 2, 0.3, 0.3);
        builder.addArmAction(ARM_ACTION, 2140, 0.5);
        builder.addDriveAction(DRIVE_FORWARD, 20158, 17818, 1.0, 1.0); // TODO: Check distance is correct
        builder.addArmAction(ARM_ACTION, 0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 475 * 2, 475 * 2, 0.3, 0.3);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, (-475 * 2 * 6), (-475 * 2 * 6), -1.0, -1.0);
        builder.addDriveAction(DRIVE_BACKWARD, 0, -50 * 45, 0.0, -1.0);
        builder.addWinchAction(40000, 0.9);
        builder.addWaitAction(6);
        builder.addWinchAction(-10000, -0.9);
        builder.addState(STATE_STOP);
    }
}
