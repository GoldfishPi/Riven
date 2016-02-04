package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousMountainLeftSide extends AutonomousVariables {

    @Override
    public void setupAutonomous() {
        addScreamAction();
        addWaitAction(900);
        addRelievedAction();
        addDriveAction(DRIVE_FORWARD, 6500, 6500 + (50 * 45), 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 8000, 8000, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 0, 50 * 45, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2048, 0.5);
        addDriveAction(DRIVE_FORWARD, 4500, 4500, 0.5, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, -475 * 2, -475 * 2, -1.0, -1.0);
//        addArmAction(ARM_ACTION, -2048, -0.5);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_BACKWARD, 0, -6720, 0.0, -1.0);
        addDriveAction(DRIVE_FORWARD, 6040, 3400, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 9120, 9120, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 8962+320, 0, 1.0, 0.0); // 30 second limit reached here.
        addDriveAction(DRIVE_FORWARD, 13500, 13500, 1.0, 1.0);
        addState(STATE_STOP);
    }
}
