package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousConcept14 extends AutonomousVariables {

    @Override
    public void setupAutonomous() {
//        addDriveAction(DRIVE_FORWARD, 21108, 18768, 1.0, 1.0);
//        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
//        addArmAction(ARM_ACTION, 200, 0.5);
//        addDriveAction(DRIVE_FORWARD, 475, 475, 0.3, 0.3);
//        addServoAction(THE_DUMPER, 1.0);
//        addWaitAction(120); // time_in_ticks
//        addServoAction(THE_DUMPER, 0.0);
//        addDriveAction(DRIVE_BACKWARD, 475, 475, -0.3, -0.3);
//        addArmAction(ARM_ACTION, 130, -0.5);
//        addArmAction(ARM_ACTION, 0, 0.0);
//        addDriveAction(DRIVE_BACKWARD, -6720, 0, -1.0, 0.0);
//        addDriveAction(DRIVE_FORWARD, 3400, 5740, 1.0, 1.0);
//        addDriveAction(DRIVE_FORWARD, 9120, 9120, 1.0, 1.0);
//        addDriveAction(DRIVE_FORWARD, 0, 8962+840, 0.0, 1.0);
//        addDriveAction(DRIVE_FORWARD, 13200, 13200, 1.0, 1.0);
        addVibratorAction(250);
        addWaitAction(120);
        addVibratorAction(1000);
        addState(STATE_STOP);
    }
}
