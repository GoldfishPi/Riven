package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/*
* created by c5, GoldfishPi, and Cyberarm
* Contact details: timecrafters8962@gmail.com
* */

public class AutonomousMountainLeftCorner extends AutonomousVariables {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    public AutonomousMountainLeftCorner() {

    }

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 18768, 21108, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 3500, 3500, 1.0, 1.0);
        addArmAction(ARM_ACTION, 2048, 0.5);
        addDriveAction(DRIVE_FORWARD, 475 * 2, 475 * 2, 0.3, 0.3);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_BACKWARD, -475 * 2, -475 * 2, -1.0, -1.0);
        addArmAction(ARM_ACTION, -2048, -0.5);
        addArmAction(ARM_ACTION, 0, 0.0);
//        setCollisionProfile(COLLISION_WAIT);
        addDriveAction(DRIVE_BACKWARD, 0, -6720, 0.0, -1.0);
        addDriveAction(DRIVE_FORWARD, 6040, 3400, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 9120, 9120, 1.0, 1.0);
        addDriveAction(DRIVE_FORWARD, 8962-320, 0, 1.0, 0.0);
        addDriveAction(DRIVE_FORWARD, 13200, 13200, 1.0, 1.0);
        addState(STATE_STOP);

//        addState(STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL);
//        addState(STATE_TURN_45_LEFT);
//        addState(STATE_STRAIGHT_PARK);
////        addState(STATE_RAISE_ARM);
//        addState(STATE_STRAIGHT_POSITION);
////        addState(STATE_DUMP_GUYS);
////        addState(STATE_UNDUMP_GUYS);
//        addState(STATE_STRAIGHT_REPOSITION);
////        addState(STATE_LOWER_ARM);
//        addState(STATE_REVERSE_135_DEGREE_LEFT);
//
//        addState(STATE_STRAIGHT_TO_SIDE);
//        addState(STATE_TURN_45_RIGHT);
//        addState(STATE_STRAIGHT_TO_MOUNTAIN);
//        addState(STATE_TURN_90_DEGREE_RIGHT);
//        addState(STATE_TURN_90_DEGREE_RIGHT);
//        addState(STATE_STRAIGHT_TO_FAR);
//        addState(STATE_STRAIGHT_TO_CORNER);
//        addState(STATE_STOP);
    }
}