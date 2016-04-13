package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
* created by c5, GoldfishPi, and Cyberarm
* Contact details: timecrafters8962@gmail.com
 *
 * cheese puffs
* */

public class AutonomousMountainLeftCorner extends AutonomousMindContainer {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    @Override
    public void setupAutonomous() {
        // normal init
        builder.addCompleteEncoderResetAction();

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2);
        builder.addBlockingWinchAction(2400 * 2, 1.0);
        builder.addBlockingArmAction(ARM_ACTION, 2240 * 4, 0.5);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);

        builder.addArmAction(ARM_ACTION, 2240 * 5, 0.7);
        builder.addWinchAction(-2300, -0.9);

        builder.addDriveAction(DRIVE_FORWARD, 4254, 4254, 0.2, 0.2, true);
        builder.addServoAction(LEFT_SHURIKEN, 0.6);
        builder.addDriveAction(DRIVE_FORWARD, 0, (-12 * 120), 0.0, -0.2);

        builder.addDriveAction(DRIVE_FORWARD, 700, 700, 0.2, 0.2);
        builder.addDriveAction(DRIVE_BACKWARD, -250, -250, -0.2, -0.2);
        builder.addArmAction(ARM_ACTION, 0, 0.0);

        builder.addCompleteEncoderResetAction();
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, 0, (12 * 55), 0.0, 0.2);

        builder.addDriveAction(DRIVE_FORWARD, 340, 340, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(120);

        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(60);

        builder.addDriveAction(DRIVE_FORWARD, 440, 440, 0.2, 0.2);
        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -200, -200, -0.2, -0.2);
        builder.addCompleteEncoderResetAction();
        builder.addDriveAction(DRIVE_BACKWARD, (-12 * 150), (-12 * 60), -0.4, -0.13);
        builder.addBlockingWinchAction(25000, 1.0);
        builder.addState(STATE_STOP);
    }
}