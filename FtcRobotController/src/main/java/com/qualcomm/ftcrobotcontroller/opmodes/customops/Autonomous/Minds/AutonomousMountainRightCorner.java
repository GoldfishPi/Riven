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
        // normal init
        builder.addCompleteEncoderResetAction();

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2);
        builder.addBlockingWinchAction(2400 * 2, 1.0);
        builder.addBlockingArmAction(ARM_ACTION, 2240 * 4, 0.5);
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);

        builder.addArmAction(ARM_ACTION, 2240 * 5, 0.7);
        builder.addWinchAction(-2300, -0.9);

        builder.addDriveAction(DRIVE_FORWARD, 4254, 4254, 0.2, 0.2, true);

        builder.addDriveAction(DRIVE_BACKWARD, -250, -250, -0.2, -0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(100);
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(80);

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2, true);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(100);
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(40);

        builder.addDriveAction(DRIVE_FORWARD, (12 * 45), 0, 0.2, 0.0);


        builder.addDriveAction(DRIVE_FORWARD, 350, 350, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(120);
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(60);
        builder.addDriveAction(DRIVE_FORWARD, 500, 500, 0.2, 0.2);

        builder.addCompleteEncoderResetAction();


        builder.addServoAction(THE_DUMPER, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -200, -200, -0.2, -0.2);
        builder.addCompleteEncoderResetAction();
        builder.addDriveAction(DRIVE_BACKWARD, (-12 * 60), (-12 * 150), -0.13, -0.4);
        builder.addBlockingWinchAction(25000, 1.0);
        builder.addState(STATE_STOP);
    }
}