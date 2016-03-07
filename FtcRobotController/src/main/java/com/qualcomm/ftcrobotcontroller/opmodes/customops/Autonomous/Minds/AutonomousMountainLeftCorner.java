package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/*
* created by c5, GoldfishPi, and Cyberarm
* Contact details: timecrafters8962@gmail.com
* */

public class AutonomousMountainLeftCorner extends AutonomousMindContainer {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 1000, 1000, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2140, 0.5);
        addDriveAction(DRIVE_FORWARD, 17418, 19758, 1.0, 1.0);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 950, 950, 0.5, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, 950, 950, -1.0, -1.0);
        addWinchAction(30000, 0.9);
        addDriveAction(DRIVE_BACKWARD, -50 * 205, -50 * 90, -1.0, -0.1);
        addWaitAction((1800));
        addWinchAction(-10000, -0.9);
        addWaitAction((850));
        addState(STATE_STOP);
    }
}