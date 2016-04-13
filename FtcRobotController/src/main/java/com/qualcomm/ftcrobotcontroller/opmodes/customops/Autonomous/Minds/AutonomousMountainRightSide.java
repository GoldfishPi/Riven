package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Minds;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 1/20/16.
 *
 * OpMode for Blue alliance that starts along the wall, next to the mountain.
 */

public class AutonomousMountainRightSide extends AutonomousMindContainer {

    @Override
    public void setupAutonomous() {
        // normal init
        builder.addCompleteEncoderResetAction();

        builder.addDriveAction(DRIVE_FORWARD, 250, 250, 0.2, 0.2);
        builder.addBlockingWinchAction(2400 * 2, 1.0);
        builder.addBlockingArmAction(ARM_ACTION, 2240 * 4, 0.5);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);

        builder.addArmAction(ARM_ACTION, 2240 * 4, 0.5);
        builder.addWinchAction(-2300, -0.9);

        builder.addDriveAction(DRIVE_FORWARD, 1754, 1754, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.6);
        builder.addDriveAction(DRIVE_FORWARD, (12 * 45), 0, 0.2, 0.0);

        builder.addDriveAction(DRIVE_FORWARD, 1000, 1000, 0.2, 0.2);
        builder.addDriveAction(DRIVE_FORWARD, (-12 * 90), 0, -0.2, 0.0);

        builder.addDriveAction(DRIVE_FORWARD, 600, 600, 0.2, 0.2);
        builder.addDriveAction(DRIVE_BACKWARD, -350, -350, -0.2, -0.2);
        builder.addArmAction(ARM_ACTION, 0, 0.0);

        builder.addCompleteEncoderResetAction();
        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, (12 * 65), 0, 0.2, 0.0);

        builder.addDriveAction(DRIVE_FORWARD, 240, 240, 0.2, 0.2);
        builder.addServoAction(LEFT_SHURIKEN, 0.8);
        builder.addServoAction(RIGHT_SHURIKEN, 0.5);
        builder.addWaitAction(120);

        builder.addServoAction(LEFT_SHURIKEN, 0.0);
        builder.addServoAction(RIGHT_SHURIKEN, 0.0);
        builder.addWaitAction(60);

        builder.addDriveAction(DRIVE_FORWARD, 400, 400, 0.2, 0.2);
        builder.addServoAction(THE_DUMPER_SLOW, 1.0);
        builder.addWaitAction(120); // time_in_ticks
        builder.addServoAction(THE_DUMPER, 0.0);
        builder.addDriveAction(DRIVE_BACKWARD, -200, -200, -0.2, -0.2);
        builder.addCompleteEncoderResetAction();
        builder.addDriveAction(DRIVE_BACKWARD, (-12 * 90), (-12 * 180), -0.13, -0.4);
        builder.addBlockingWinchAction(25000, 1.0);
        builder.addState(STATE_STOP);
    }
}
