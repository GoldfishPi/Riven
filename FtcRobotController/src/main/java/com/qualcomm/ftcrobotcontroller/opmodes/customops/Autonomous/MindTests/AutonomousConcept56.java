package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.MindTests;

/*
* created by C5, Cyberarm and GoldfishPi
* Contact details: timecrafters8962@gmail.com
* */

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/*
    OpMode for Blue alliance that starts in the < corner by the driver box. TESTING.
 */

public class AutonomousConcept56 extends AutonomousMindContainer {
    //-------------------------------
    // motor declarations come from autonomous variables class
    //------------------------------//

    @Override
    public void setupAutonomous() {
        // normal init

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2);
        builder.addBlockingWinchAction(2400, 0.9);
        builder.addBlockingArmAction(ARM_ACTION, 2140 * 4, 0.5);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addBlockingWinchAction(-2400, -0.9);
        builder.addArmAction(ARM_ACTION, 0, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 4354, 4354, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.6);
        builder.addDriveAction(DRIVE_FORWARD, 12 * 120, 0, 0.3, 0.0);
        builder.addDriveAction(DRIVE_FORWARD, 500, 500, 0.2, 0.2);
        builder.addDriveAction(DRIVE_BACKWARD, -350, -350, -0.2, -0.2);

        builder.addDriveAction(DRIVE_BACKWARD, -12 * 70, 0, -0.2, 0.0);
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);

        builder.addDriveAction(DRIVE_FORWARD, 240, 240, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(180);

        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(120);

        builder.addDriveAction(DRIVE_FORWARD, 340, 340, 0.2, 0.2);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -237, -237, -0.2, -0.2);
//        builder.addWinchAction(15000, 0.9);
        builder.addDriveAction(DRIVE_BACKWARD, -12 * 90, -12 * 205, -0.2, -0.4);
//        builder.addWaitAction((1800));
//        builder.addWinchAction(-5000, -0.9);
//        builder.addWaitAction((850));
        builder.addState(STATE_STOP);
    }
}