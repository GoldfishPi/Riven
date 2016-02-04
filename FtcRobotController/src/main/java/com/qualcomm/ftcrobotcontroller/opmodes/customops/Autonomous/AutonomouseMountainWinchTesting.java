package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 2/4/16.
 */
public class AutonomouseMountainWinchTesting extends AutonomousVariables {

    @Override
    public void setupAutonomous() {
        addArmAction(ARM_ACTION, 2140, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, -475 * 2, -475 * 2, -1.0, -1.0);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_BACKWARD, -6720, 0, -1.0, 0.0);
        addWinchAction(60*8, 1.0);
        addWinchAction(120, -1.0);
    }
}
