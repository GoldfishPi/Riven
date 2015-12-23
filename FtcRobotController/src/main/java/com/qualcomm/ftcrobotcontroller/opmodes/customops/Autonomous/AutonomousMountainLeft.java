package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/*
* created by c5 and GoldfishPi
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

public class AutonomousMountainLeft extends AutonomousVariables {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    public AutonomousMountainLeft() {

    }

    @Override
    public void init() {
        autonomousInit();
    }

    public void autonomousInit() {
        lDrive = getMotor("lDrive");
        rDrive = getMotor("rDrive");
        lDrive.setDirection(DcMotor.Direction.REVERSE);
        rDrive.setDirection(DcMotor.Direction.FORWARD);

        theDumper = getServo("theDumper");
        arm       = getMotor("arm");

        stateWait = 0;
        stateMachineIndex = 0;
        debugArray = new int[100];
        // Field Debug Array:
        debugArray[0] = STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL;
        debugArray[1] = STATE_TURN_45_LEFT;
        debugArray[2] = STATE_STRAIGHT_PARK;
//        debugArray[3] = STATE_RAISE_ARM;
        debugArray[3] = STATE_STRAIGHT_POSITION;
        debugArray[4] = STATE_DUMP_GUYS;
        debugArray[5] = STATE_UNDUMP_GUYS;
        debugArray[6] = STATE_STRAIGHT_REPOSITION;
//        debugArray[8] = STATE_LOWER_ARM;
        debugArray[7] = STATE_REVERSE_90_DEGREE_LEFT;
//        debugArray[10]= STATE_STOP;

        // Testing Debug Array:
//        debugArray[8] = STATE_STRAIGHT_REPOSITION;
//        debugArray[9] = STATE_REVERSE_90_DEGREE_LEFT;
        debugArray[8] = STATE_STRAIGHT_TO_NEAR_BLUE;
        debugArray[9] = STATE_TURN_45_RIGHT;
        debugArray[10] = STATE_TURN_45_RIGHT;
        debugArray[11] = STATE_STRAIGHT_TO_NEAR_BLUE;
        debugArray[12] = STATE_STOP;

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }

        leftEncoderTarget = getEncoderValue(lDrive);
        rightEncoderTarget= getEncoderValue(rDrive);

        lDrivePower = 0.0;
        rDrivePower = 0.0;

        lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
        rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));

        lDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        lDrive.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rDrive.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void init_loop(){
        autonomousInitLoop();
    }

    public void autonomousInitLoop() {
        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
        theDumperTick = 0;
        theDumperPosition = theDumper.MIN_POSITION;
    }

    @Override
    public void loop() {
        autonomousloop();
    }

    public void autonomousloop() {
        setTelemetry();
        theDumper.setPosition(theDumperPosition);

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_LEFT:
                if (stateWait == 0) {

                    currentMachineState = "(L) Turn 45 Left";
                    stateWait = 1;

                    leftEncoderTarget = getEncoderValue(lDrive);                                    // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 2240;

                    lDrivePower = 0.0;
                    rDrivePower = 1.0;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) 45 Left Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }

                break;

            case STATE_TURN_45_RIGHT:
                if (stateWait == 0) {
                    currentMachineState = "(L) Turn 45 Right";

                    System.out.println("(L) Start TURN 45 Right");

                    stateWait = 1;

                    leftEncoderTarget = getEncoderValue(lDrive) + 2340;                                    // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive);

                    lDrivePower = 1.0;
                    rDrivePower = 0.0;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) 45 Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }

                break;

            case STATE_STRAIGHT_PARK:
                if (stateWait == 0) {
                    currentMachineState = "(L) Straight park";

                    System.out.println("(L) STATE STRAIGHT PARK start");

                    stateWait = 1;

                    leftEncoderTarget  = getEncoderValue(lDrive) + 3500;        // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 3500;        // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) Drive straight park Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if (stateWait == 0) {

                    stateWait = 1;

                    currentMachineState = "(L) Drive Straight";

                    System.out.println("(L) STATE STRAIGHT start");
                    leftEncoderTarget  = getEncoderValue(lDrive) + 18768;                                     // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 18768;                                     // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) Drive straight Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_STRAIGHT_POSITION:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "(L) Straight Position";

                    lDrivePower = 0.3;
                    rDrivePower = 0.3;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    leftEncoderTarget  = getEncoderValue(lDrive) + 475;
                    rightEncoderTarget = getEncoderValue(rDrive) + 475;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);
                }

                if (getEncoderValue(lDrive) >= leftEncoderTarget - 15) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if (getEncoderValue(rDrive) >= rightEncoderTarget - 15) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) Straight Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_RAISE_ARM:

                if (stateWait == 0) {
                    stateWait = 1;
                    resetEncodersAuto(arm);
                    currentMachineState = "(L) Raise arm";
                    armSpeed = 0.3;
                    armLocation = getEncoderValue(arm);
                    arm.setTargetPosition(armLocation + 300);
                    arm.setPower(armSpeed);
                }

                if (getEncoderValue(arm) >= getEncoderValue(arm) - 15) {
                    stateWait = 0;
                    stateMachineIndex++;
                }

                break;

            case STATE_DUMP_GUYS:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "(L) Dump Guys";
                    theDumperPosition = theDumper.MAX_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;
                    System.out.print("(L) Dump Guys Complete\n");

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;


            case STATE_UNDUMP_GUYS:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "(L) Undump Guys";
                    theDumperPosition = theDumper.MIN_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;
                    stateWait = 0;
                    System.out.print("(L) Undump Guys Complete\n");

                    stateMachineIndex++;
                }
                break;

            case STATE_LOWER_ARM:

                if (stateWait == 0) {
                    stateWait = 1;
                    resetEncodersAuto(arm);
                    currentMachineState = "(L) Lower arm";
                    armSpeed = -0.3;
                    armLocation = getEncoderValue(arm);
                    arm.setTargetPosition(armLocation - 200);
                    arm.setPower(armSpeed);
                }

                if (getEncoderValue(arm) <= getEncoderValue(arm) + 15) {
                    stateWait = 0;
                    arm.setPower(0.0);
                    stateMachineIndex++;
                }

                break;

            case STATE_STRAIGHT_REPOSITION:
                if (stateWait == 0) {

                    stateWait = 1;
                    leftEncoderTarget  = getEncoderValue(lDrive) - 475;
                    rightEncoderTarget = getEncoderValue(rDrive) - 475;

                    lDrivePower = -0.3;
                    rDrivePower = -0.3;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "(L) Straight Reposition";
                }

                if (getEncoderValue(lDrive) <= leftEncoderTarget + 15) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if (getEncoderValue(rDrive) <= rightEncoderTarget + 15) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower >= -0.1) && (rDrivePower >= -0.1)) {
                    System.out.print("(L) Straight Reposition Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_REVERSE_90_DEGREE_LEFT:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive);
                    rightEncoderTarget= getEncoderValue(rDrive) - (2240 * 2);
                    lDrivePower = 0.0;
                    rDrivePower = -1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "(L) Reverse 90 Degree Left";
                }

                if (getEncoderValue(rDrive) <= rightEncoderTarget + 15) {
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_STRAIGHT_TO_NEAR_BLUE:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) + 3500;
                    rightEncoderTarget= getEncoderValue(rDrive) + 3500;

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "(L) Straight to Near Blue";
                }

                if (getEncoderValue(lDrive) >= leftEncoderTarget - 15) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }


                if (getEncoderValue(rDrive) >= rightEncoderTarget - 15) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }


                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_STOP:

                currentMachineState = "(L) STOP";
                break;

            default:
                currentMachineState = "!DEFAULT!";
                break;
        }
    }

    @Override
    public void stop() {
    }
}