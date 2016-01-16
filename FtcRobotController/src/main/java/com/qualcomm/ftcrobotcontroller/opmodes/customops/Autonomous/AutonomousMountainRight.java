package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Array;

/*
* created by C5, Cyberarm and GoldfishPi
* Contact details: timecrafters8962@gmail.com
* */

/* This autonomous does the following steps
* 0) Wait For encoder to reset
* 1) Drive forward to middle
* 2) turn 90 degrees
* 3) Drive forward until the distance sensor reads 5
* 4) Raise Arm using encoder ticks
* 5) Extend Arm using encoder ticks
* 7) Drive straight back
* 8) Turn to become inline with the mountain
* 9) Drive to ramp
* 10) Raise arm to inline with ramp
* 11) lower arm slightly
* 12) pull robot up
* End) Stop all
 */

public class AutonomousMountainRight extends AutonomousVariables {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    public AutonomousMountainRight() {

    }

    @Override
    public void setupAutonomous() {
        addState(STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL);
        addState(STATE_TURN_45_RIGHT);
        addState(STATE_STRAIGHT_PARK);
//        addState(STATE_RAISE_ARM);
        addState(STATE_STRAIGHT_POSITION);
//        addState(STATE_DUMP_GUYS);
//        addState(STATE_UNDUMP_GUYS);
        addState(STATE_STRAIGHT_REPOSITION);
//        addState(STATE_LOWER_ARM);
        addState(STATE_REVERSE_135_DEGREE_RIGHT);

        addState(STATE_STRAIGHT_TO_SIDE);
        addState(STATE_TURN_45_LEFT);
        addState(STATE_STRAIGHT_TO_MOUNTAIN);
        addState(STATE_TURN_90_DEGREE_LEFT);
        addState(STATE_TURN_90_DEGREE_LEFT);
        addState(STATE_STRAIGHT_TO_FAR);
        addState(STATE_STRAIGHT_TO_CORNER);
        addState(STATE_STOP);
    }
}