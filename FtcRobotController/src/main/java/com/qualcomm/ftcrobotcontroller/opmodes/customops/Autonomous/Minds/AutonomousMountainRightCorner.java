package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

/*
* created by C5, Cyberarm and GoldfishPi
* Contact details: timecrafters8962@gmail.com
* */

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/*
    OpMode for Blue alliance that starts in the < corner by the driver box.
 */

public class AutonomousMountainRightCorner extends AutonomousMindContainer {
    //-------------------------------
    // motor declarations come from autonomous variables class
    //------------------------------//

    @Override
    public void setupAutonomous() {
//        builder.addBlockingWinchAction(2400, 0.9);
//        builder.addBlockingWinchAction(2400, 0.9);
//        builder.addBlockingWinchAction(-2400, -0.9);
//        builder.addBlockingWinchAction(-2400, -0.9);
//        builder.addArmAction(ARM_ACTION, 1300, 0.5);
//        builder.addBlockingArmAction(ARM_ACTION, -1300, -0.5);
//        builder.addBlockingArmAction(ARM_ACTION, 1300, 0.5);
//        builder.addBlockingArmAction(ARM_ACTION, -1300, -0.5);
//        builder.addBlockingArmAction(ARM_ACTION, 1300, 0.5);
//        builder.addBlockingArmAction(ARM_ACTION, -1300, -0.5);
//        builder.addBlockingWinchAction(-2300*2, -0.5);
//        builder.addServoAction(LEFT_SHURIKEN, 0.8);
//        builder.addServoAction(RIGHT_SHURIKEN, 0.5);

        // normal init

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2);
//        builder.addBlockingWinchAction(2400, 0.9);
        builder.addBlockingArmAction(ARM_ACTION, 2140 * 2, 0.5);
//        builder.addBlockingWinchAction(-2400 * 2, -0.9);
        builder.addDriveAction(DRIVE_FORWARD, 4354, 4354, 0.2, 0.2);
        builder.addArmAction(ARM_ACTION, 0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 585, 0, 0.3, 0.0);
//        builder.addScreamAction();
        builder.addDriveAction(DRIVE_FORWARD, 875, 875, 0.2, 0.2);
        builder.addDriveAction(DRIVE_FORWARD, 237, 237, 0.2, 0.2);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -237, -237, -0.2, -0.2);
//        builder.addWinchAction(15000, 0.9);
        builder.addDriveAction(DRIVE_BACKWARD, -12 * 90, -12 * 205, -0.1, -0.2);
//        builder.addWaitAction((1800));
//        builder.addWinchAction(-5000, -0.9);
//        builder.addWaitAction((850));
        builder.addState(STATE_STOP);
    }
}