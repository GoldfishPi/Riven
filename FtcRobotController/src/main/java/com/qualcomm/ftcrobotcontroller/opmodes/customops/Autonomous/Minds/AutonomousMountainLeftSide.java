package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousMountainLeftSide extends AutonomousMindContainer {

    @Override
    public void setupAutonomous() {
        builder.addDriveAction(DRIVE_FORWARD, 750, 750, 1.0, 1.0);
        builder.addArmAction(ARM_ACTION, 2140, 0.5);
        builder.addDriveAction(DRIVE_FORWARD, 950, 950, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 0, 12 * 45, 0.0, 1.0);
        builder.addArmAction(ARM_ACTION, 0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 1925, 1925, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 0, 12 * 45, 0.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 875, 875, 1.0, 1.0);
        builder.addDriveAction(DRIVE_FORWARD, 237, 237, 0.5, 0.5);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, 237, 237, -1.0, -1.0);
        builder.addWinchAction(15000, 0.9);
        builder.addDriveAction(DRIVE_BACKWARD, (-12 * 212), (-12 * 45), -1.0, -0.13);
        builder.addWaitAction((1800));
        builder.addWinchAction(-5000, -0.9);
        builder.addWaitAction((850));
    }
}
