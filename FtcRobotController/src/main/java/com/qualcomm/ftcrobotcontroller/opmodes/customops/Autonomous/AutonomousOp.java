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

public class AutonomousOp extends AutonomousVariables {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //-------------------------------

    public AutonomousOp() {

    }

    @Override
    public void init() {
        autonomousInit();
    }

    public void autonomousInit() {
        lDrive = getMotor("lDrive");
        rDrive = getMotor("rDrive");
        lDrive.setDirection(DcMotor.Direction.FORWARD);
        rDrive.setDirection(DcMotor.Direction.REVERSE);

        lFinger = getMotor("lFinger");
        rFinger = getMotor("rFinger");

        armExtender = getMotor("armExtender");
        armExtender.setDirection(DcMotor.Direction.REVERSE);

        arm = getMotor("arm");

        stateWait = 0;
        stateMachineIndex = 0;
        debugArray = new int[100];
        debugArray[0] = STATE_TURN_45_RIGHT;
        debugArray[1] = STATE_TURN_45_RIGHT;
        debugArray[2] = STATE_TURN_45_RIGHT;
        debugArray[3] = STATE_TURN_45_RIGHT;

        debugArray[4] = STATE_STOP;

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
     }

    @Override
    public void loop() {
        autonomousloop();
    }

    public void autonomousloop() {
        setTelemetry();

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_RIGHT:
                if (stateWait == 0){
                    System.out.print("Start TURN_45_RIGHT\n");

                    stateWait = 1;

                    turnRobotCalculation(12, 45);// 24 inches, 45 degrees
                    leftEncoderTarget  = (int) adRotatingRobotDrive[1] + lDrive.getCurrentPosition();
                    rightEncoderTarget = (int) adRotatingRobotDrive[0] + rDrive.getCurrentPosition();

                    System.out.print(String.valueOf(adRotatingRobotDrive[1]));
                    System.out.print(String.valueOf(adRotatingRobotDrive[0]));

                    lDrivePower = 1.0;
                    rDrivePower = (1.0 * adRotatingRobotDrive[2]);

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

                if ((lDrivePower == 0.0) && (rDrivePower == 0.0)) {
                    System.out.print("45 Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    stateWait = 0;
                    stateMachineIndex ++;
                }

                break;

            case STATE_TURN_45_LEFT:
            if (stateWait == 0){
                System.out.print("Start TURN_45_LEFT\n");

                stateWait = 1;

                leftEncoderTarget  = (int) lDrive.getCurrentPosition();
                rightEncoderTarget = (int) rDrive.getCurrentPosition();

                System.out.print(String.valueOf(adRotatingRobotDrive[1]));
                System.out.print(String.valueOf(adRotatingRobotDrive[0]));

                lDrivePower = -1.0;
                rDrivePower = (-1.0 * adRotatingRobotDrive[2]);

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

            if ((lDrivePower == 0.0) && (rDrivePower == 0.0)) {
                System.out.print("45 LEFT Complete\n");
                lDrivePower = 0.0;
                rDrivePower = 0.0;
                lDrive.setPower(lDrivePower);
                rDrive.setPower(rDrivePower);

                stateWait = 0;
                stateMachineIndex ++;
            }

            System.out.print("Waiting...\n");

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if ( stateDriveStraightConerToGoal == 0) {
                    resetEncodersAuto(rDrive);
                    resetEncodersAuto(lDrive);
//                    driveStraight(straightRobotCalculation((double) (12.5)));

                }
                else if(moveComplete()){
                    stateMachineIndex ++;
                }
                break;

            case STATE_RAISE_ARM:
                if (stateRaiseArm == 0){
                    resetEncodersAuto(arm);
                    arm.setTargetPosition(280);
                    arm.setPower(0.3);
                }
                else if(moveComplete()){
                    arm.setPower(0.0);
                    stateMachineIndex ++;
                }
                break;

            case STATE_EXTEND_ARM:
                if (stateExtnedArm == 0){
                    resetEncodersAuto(armExtender);
                    extendArm();
                }
                else if (extendFinished()){
                    stateMachineIndex ++;
                }
                break;

            case STATE_RETRACT_ARM:
                if(stateRetractArm == 0){
                    resetEncodersAuto(armExtender);
                    retractArm();
                }
                else if(retractDone()){
                    stateMachineIndex ++;
                }

            case STATE_STOP:
                break;

            default:
        }
    }

    @Override
    public void stop() {
    }
}