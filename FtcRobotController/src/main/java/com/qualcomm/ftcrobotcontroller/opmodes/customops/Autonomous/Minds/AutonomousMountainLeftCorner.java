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
        addDriveAction(DRIVE_FORWARD, 250, 250, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2140, 0.5);
        addDriveAction(DRIVE_FORWARD, 4354, 4939, 1.0, 1.0);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_FORWARD, 875, 875, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 237, 237, 0.5, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, 237, 237, -1.0, -1.0);
        addWinchAction(15000, 0.9);
        addDriveAction(DRIVE_BACKWARD, -12 * 205, -12 * 90, -1.0, -0.1);
        addWaitAction((1800));
        addWinchAction(-5000, -0.9);
        addWaitAction((850));
        addState(STATE_STOP);
    }
}