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
        debugArray[0] = STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL;
        debugArray[1] = STATE_TURN_45_RIGHT;
        debugArray[2] = STATE_STRAIGHT_PARK;
        debugArray[3] = STATE_RAISE_ARM;
        debugArray[4] = STATE_STRAIGHT_POSITION;
        debugArray[5] = STATE_DUMP_GUYS;
        debugArray[6] = STATE_UNDUMP_GUYS;
        debugArray[7] = STATE_STRAIGHT_REPOSITION;
        debugArray[8] = STATE_LOWER_ARM;
        debugArray[9] = STATE_REVERSE_90_DEGREE_RIGHT;
        debugArray[10]= STATE_REVERSE_90_DEGREE_RIGHT;

        debugArray[11] = STATE_STRAIGHT_TO_SIDE;
        debugArray[12] = STATE_TURN_45_LEFT;
        debugArray[13] = STATE_TURN_45_LEFT;
        debugArray[14] = STATE_STRAIGHT_TO_MOUNTAIN;
        debugArray[15] = STATE_TURN_45_LEFT;
        debugArray[16] = STATE_TURN_45_LEFT;
        debugArray[17] = STATE_STRAIGHT_TO_FAR;
        debugArray[18] = STATE_TURN_45_LEFT;
        debugArray[19] = STATE_STRAIGHT_TO_CORNER;
        debugArray[20] = STATE_STOP;
    }
}
