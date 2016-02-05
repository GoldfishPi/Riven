package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousMountainLeftSide extends AutonomousVariables {

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 3000, 3000, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2140, 0.5);
        addDriveAction(DRIVE_FORWARD, 3800, 3800, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 0, 50 * 45, 0.0, 1.0);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_FORWARD, 7700, 7700, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 0, 50 * 45, 0.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 950, 950, 0.5, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, 950, 950, -1.0, -1.0);
        addWinchAction(30000, 0.9);
        addDriveAction(DRIVE_BACKWARD, (-50 * 212), (-50 * 45), -1.0, -0.13);
        addWaitAction((1800));
        addWinchAction(-10000, -0.9);
        addWaitAction((850));
    }
}
