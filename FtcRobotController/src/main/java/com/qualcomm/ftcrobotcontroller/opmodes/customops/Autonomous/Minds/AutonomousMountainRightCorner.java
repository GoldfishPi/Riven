package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

/*
* created by C5, Cyberarm and GoldfishPi
* Contact details: timecrafters8962@gmail.com
* */

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

public class AutonomousMountainRightCorner extends AutonomousMindContainer {
    //-------------------------------
    // motor declarations come from autonomous variables class
    //------------------------------//

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 1000, 1000, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2140, 0.5);
        addDriveAction(DRIVE_FORWARD, 19758, 17418, 1.0, 1.0);
        addArmAction(ARM_ACTION, 0, 0.0);
        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 950, 950, 0.5, 0.5);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, 950, 950, -1.0, -1.0);
        addWinchAction(30000, 0.9);
        addDriveAction(DRIVE_BACKWARD, -50 * 90, -50 * 205, -0.1, -1.0);
        addWaitAction((1800));
        addWinchAction(-10000, -0.9);
        addWaitAction((850));
        addState(STATE_STOP);
    }
}