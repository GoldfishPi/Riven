package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.MindTests;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 2/4/16.
 */
public class AutonomouseMountainWinchTesting extends AutonomousMindContainer {

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 475 * 2, 475 * 2, 0.3, 0.3);
        addArmAction(ARM_ACTION, 2140, 0.5);
        addDriveAction(DRIVE_FORWARD, 20158, 17818, 1.0, 1.0); // TODO: Check distance is correct
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 475 * 2, 475 * 2, 0.3, 0.3);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, (-475 * 2 * 6), (-475 * 2 * 6), -1.0, -1.0);
        addDriveAction(DRIVE_BACKWARD, 0, -50*45, 0.0, -1.0);
        addWinchAction(40000, 0.9);
        addWaitAction(6);
        addWinchAction(-10000, -0.9);
        addState(STATE_STOP);
    }
}
