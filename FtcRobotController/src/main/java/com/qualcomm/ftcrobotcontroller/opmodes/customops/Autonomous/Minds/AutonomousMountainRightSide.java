package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousMountainRightSide extends AutonomousMindContainer {

    @Override
    public void setupAutonomous() {
        builder.addDriveAction(DRIVE_FORWARD, 750, 750, 1.0, 1.0);
        builder.addArmAction(ARM_ACTION, 2140, 0.5);
        builder.addDriveAction(DRIVE_FORWARD, 950, 950, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 12 * 45, 0, 1.0, 0.0);
        builder.addArmAction(ARM_ACTION, 0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 1925, 1925, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 12 * 45, 0, 1.0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 875, 875, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 237, 237, 0.5, 0.5);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -237, -237, -1.0, -1.0);
        builder.addWinchAction(15000, 0.9);
        builder.addDriveAction(DRIVE_BACKWARD, (-12 * 45), (-12 * 212), -0.13, -1.0);
        builder.addWaitAction((1800));
        builder.addWinchAction(-5000, -0.9);
        builder.addWaitAction((850));
        builder.addState(STATE_STOP);
    }
}
