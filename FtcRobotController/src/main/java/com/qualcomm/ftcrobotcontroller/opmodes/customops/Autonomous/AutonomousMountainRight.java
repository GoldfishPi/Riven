package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

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
//        debugArray[0] = STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL;
//        debugArray[1] = STATE_TURN_45_RIGHT;
//        debugArray[2] = STATE_STRAIGHT_PARK;
//        debugArray[3] = STATE_RAISE_ARM;
//        debugArray[3] = STATE_STRAIGHT_POSITION;
//        debugArray[4] = STATE_DUMP_GUYS;
//        debugArray[5] = STATE_UNDUMP_GUYS;
//        debugArray[6] = STATE_STRAIGHT_REPOSITION;
//        debugArray[8] = STATE_LOWER_ARM;
//        debugArray[7] = STATE_REVERSE_90_DEGREE_RIGHT;
//        debugArray[8] = STATE_REVERSE_90_DEGREE_RIGHT;
//        debugArray[10]= STATE_STOP;

        // Testing Debug Array:
        debugArray[0] = STATE_STRAIGHT_REPOSITION;
        debugArray[1] = STATE_REVERSE_90_DEGREE_RIGHT;
        debugArray[2] = STATE_REVERSE_90_DEGREE_RIGHT;
        debugArray[3] = STATE_STRAIGHT_TO_NEAR_BLUE;
        debugArray[4] = STATE_TURN_45_LEFT;
        debugArray[5] = STATE_TURN_45_LEFT;
        debugArray[6] = STATE_STRAIGHT_TO_NEAR_BLUE;
        debugArray[7] = STATE_STOP;

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

                    currentMachineState = "(R) Turn 45 Left";
                    stateWait = 1;

                    leftEncoderTarget = getEncoderValue(lDrive);                                    // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 2340;

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
                    System.out.print("(R) 45 Left Complete\n");
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
                    currentMachineState = "(R) Turn 45 Right";

                    System.out.println("(R) Start TURN 45 Right");

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
                    System.out.print("(R) 45 Complete\n");
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
                    currentMachineState = "(R) Straight park";

                    System.out.println("(R) STATE STRAIGHT PARK start");

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
                    System.out.print("(R) Drive straight park Complete\n");
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

                    currentMachineState = "(R) Drive Straight";

                    System.out.println("(R) STATE STRAIGHT start");
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
                    System.out.print("(R) Drive straight Complete\n");
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
                    currentMachineState = "(R) Straight Position";

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
                    System.out.print("(R) Straight Complete\n");
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
                    currentMachineState = "(R) Raise arm";
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
                    currentMachineState = "(R) Dump Guys";
                    theDumperPosition = theDumper.MAX_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;
                    System.out.print("(R) Dump Guys Complete\n");

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;


            case STATE_UNDUMP_GUYS:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "(R) Undump Guys";
                    theDumperPosition = theDumper.MIN_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;
                    stateWait = 0;
                    System.out.print("(R) Undump Guys Complete\n");

                    stateMachineIndex++;
                }
                break;

            case STATE_LOWER_ARM:

                if (stateWait == 0) {
                    stateWait = 1;
                    resetEncodersAuto(arm);
                    currentMachineState = "(R) Lower arm";
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

                    currentMachineState = "(R) Straight Reposition";
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
                    System.out.print("(R) Straight Reposition Complete\n");
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

            case STATE_REVERSE_90_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) - (2240 * 2);
                    rightEncoderTarget= getEncoderValue(rDrive);
                    lDrivePower = -1.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "(R) Reverse 90 Degree Right";
                }

                if (getEncoderValue(lDrive) <= leftEncoderTarget + 15) {
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

                    currentMachineState = "(R) Straight to Near Blue";
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

                currentMachineState = "(R) STOP";
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