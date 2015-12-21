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

        lFinger = getMotor("lFinger");
        rFinger = getMotor("rFinger");

        armExtender  = getMotor("armExtender");
        armExtender.setDirection(DcMotor.Direction.REVERSE);

        arm = getMotor("arm");

        stateWait = 0;
        stateMachineIndex = 0;
        debugArray = new int[100];
        debugArray[0] = STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL;
        debugArray[1] = STATE_TURN_45_LEFT;
        debugArray[2] = STATE_STRAIGHT_PARK;

        debugArray[3] = STATE_STOP;

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }

        leftEncoderTarget = lDrive.getCurrentPosition();
        rightEncoderTarget= rDrive.getCurrentPosition();

        lDrivePower = 0.0;
        rDrivePower = 0.0;

        lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
        rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));

        lDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        lDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void init_loop(){
        autonomousInitLoop();
    }

    public void autonomousInitLoop() {
        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
    }

    @Override
    public void loop() {
        autonomousloop();
    }

    public void autonomousloop() {
        setTelemetry();

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_LEFT:
                if (stateWait == 0){
                    currentMachineState = "(L) Turn 45";

                    System.out.println("(L) Start TURN_45");

                    stateWait = 1;

                    leftEncoderTarget  = lDrive.getCurrentPosition();                                    // Drive distance
                    rightEncoderTarget = rDrive.getCurrentPosition() + 2240;

                    lDrivePower = 0.0;
                    rDrivePower = 1.0;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((lDrive.getCurrentPosition() >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((rDrive.getCurrentPosition() >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) 45 Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    stateWait = 0;
                    stateMachineIndex ++;
                }

                break;

            case STATE_STRAIGHT_PARK:
                if ( stateWait == 0) {
                    currentMachineState = "(L) Straight park";

                    System.out.println("(L) STATE STRAIGHT PARK start");

                    stateWait = 1;

                    leftEncoderTarget = lDrive.getCurrentPosition() + 3500;        // Drive distance
                    rightEncoderTarget= rDrive.getCurrentPosition() + 3500;        // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrive.getCurrentPosition() >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((rDrive.getCurrentPosition() >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) Drive straight park Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    stateWait = 0;
                    stateMachineIndex ++;
                }
                break;

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if ( stateWait == 0) {

                    stateWait = 1;

                    currentMachineState = "(L) Drive Straight";

                    System.out.println("(L) STATE STRAIGHT start");
                    leftEncoderTarget = lDrive.getCurrentPosition() + 18768;                                     // Drive distance
                    rightEncoderTarget= rDrive.getCurrentPosition() + 18768;                                     // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrive.getCurrentPosition() >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((rDrive.getCurrentPosition() >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                    System.out.print("(L) Drive straight Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    stateWait = 0;
                    stateMachineIndex ++;
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