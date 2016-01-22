package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by goldfishpi on 11/21/15.
 */
public class DriverOp extends OpMode {

    private DcMotor lDrive;
    private DcMotor rDrive;

    private DcMotor lFinger;
    private DcMotor rFinger;

    private DcMotor lGill;
    private DcMotor rGill;

    private DcMotor armExtender;

    private DcMotor arm;

    private BatteryChecker batteryChecker;

    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    private Servo lovePonny;

    private boolean yPressed = false;
    private boolean aPressed = false;

    private boolean xPressed = false;
    private boolean bPressed = false;

    public double armSpeed = 0.3;

    public double[] armPositions = {0.0, 150};

    public DriverOp() {

    }

    @Override
    public void init() {
//        batteryChecker.startBatteryMonitoring();

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");

        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        armExtender = hardwareMap.dcMotor.get("armExtender");

        lovePonny = hardwareMap.servo.get("theDumper");

        arm = hardwareMap.dcMotor.get("arm");

        rDrive.setDirection(DcMotor.Direction.FORWARD);
        lDrive.setDirection(DcMotor.Direction.REVERSE);

        rFinger.setDirection(DcMotor.Direction.REVERSE);
        lFinger.setDirection(DcMotor.Direction.FORWARD);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        lGill.setDirection(DcMotor.Direction.FORWARD);
        armExtender.setDirection(DcMotor.Direction.FORWARD);


        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armExtender);

        resetEncoders(arm);

    }

    @Override
    public void stop() {
    }

    @Override
    public void init_loop() {

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armExtender);
        resetEncoders(arm);

        setMotorToRunToPos(arm);
        lDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    @Override
    public void loop() {

        telemetry.addData("ArmSpeed", armSpeed);
        telemetry.addData("Arm Encoders", arm.getCurrentPosition());
        if (gamepad1.b) {
            System.out.println("lDrive : " + lDrive.getCurrentPosition());
            System.out.println("rDrive : " + rDrive.getCurrentPosition());
            System.out.println("Arm : " + arm.getCurrentPosition());

        }

//        if(batteryChecker.getBatteryLevel() < 100.0){
//            tg.startTone(ToneGenerator.TONE_SUP_ERROR);
//        }

        controllerOne();
        controllerTwo();
        setMotorToRunToPos(arm);
    }

    public void controllerOne()//Living organism velocity extender and phasing oscillation neutralizing neon elliptical yielder
    {
        {

            if(gamepad1.a){
                lovePonny.setPosition(Servo.MIN_POSITION);
            }
            if(gamepad1.y){
                lovePonny.setPosition(Servo.MAX_POSITION);
            }

            //Drive controls
            if (lDrive.getMode() == DcMotorController.RunMode.RUN_TO_POSITION && rDrive.getMode() ==

            DcMotorController.RunMode.RUN_TO_POSITION) {
                lDrive.setPower(gamepad1.left_stick_y);
                rDrive.setPower(gamepad1.right_stick_y);
            }

            if(gamepad1.right_stick_y != 0){
                rDrive.setPower(-gamepad1.right_stick_y);
            } else {
                rDrive.setPower(0.0);
            }
            if(gamepad1.left_stick_y != 0){
                lDrive.setPower(-gamepad1.left_stick_y);
            } else{
                lDrive.setPower(0.0);
            }

            //Finger controls
            if (gamepad1.left_trigger != 0.0) {
                lFinger.setPower(0.3);
            } else if (gamepad1.left_bumper) {
                lFinger.setPower(-0.3);
            } else {
                lFinger.setPower(0.0);
            }
            if (gamepad1.right_trigger != 0.0) {
                rFinger.setPower(0.3);
            } else if (gamepad1.right_bumper) {
                rFinger.setPower(-0.3);
            } else {
                rFinger.setPower(0.0);
            }

        }
    }


    public void controllerTwo() {
        {

            if(!gamepad2.dpad_right){
                arm.setPower(0.0);
                arm.setTargetPosition(0);
            }

            if(gamepad2.x && !xPressed && armSpeed < 1.100){
                xPressed = true;
                armSpeed += 0.100;
            }
            else if(!gamepad2.x && xPressed){
                xPressed = false;
            }
            if(gamepad2.b && !bPressed && armSpeed > -1.100){
                bPressed = true;
                armSpeed -= 0.100;
            }
            else if(!gamepad2.b && bPressed){
                bPressed = false;
            }


            if(gamepad2.right_stick_y != 0.0) {
                armControlls(true, 0);
            }
            else{

                armControlls(false, 0);
            }

            //Arm In and Out
            if (gamepad2.y) {

                yPressed = true;
                extendEqual("out", Math.abs(gamepad2.left_stick_y));
            } else if (!gamepad2.y && yPressed) {
                yPressed = false;
            }

            if (gamepad2.a) {
                aPressed = true;
                extendEqual("in", Math.abs(gamepad2.left_stick_y));
            } else if (!gamepad2.a && aPressed) {
                aPressed = false;
            }

            if(!aPressed && !yPressed){
                extendEqual("STOP", 0.0);
            }
            if(gamepad2.dpad_right){
                extendEqual("STOP", 0.0);
            }


            //gill controls
            if (gamepad2.left_trigger != 0.0) {
                lGill.setPower(0.5);
            } else if (gamepad2.left_bumper) {
                lGill.setPower(-0.5);
            } else {
                lGill.setPower(0.0);
            }

            if (gamepad2.right_trigger != 0) {
                rGill.setPower(0.5);
            } else if (gamepad2.right_bumper) {
                rGill.setPower(-0.5);
            } else {
                rGill.setPower(0.0);
            }
        }
    }

    public void extendEqual(String direction, double speed) {

        if (direction == "out") {
            armExtender.setTargetPosition(armExtender.getCurrentPosition()+50);

        }
        if (direction == "in") {
            armExtender.setTargetPosition(armExtender.getCurrentPosition()-50);
        }

        if(direction == "STOP"){
            armExtender.setPower(0.0);
        }

        armExtender.setPower(gamepad2.left_stick_y);
    }

    public void armControlls(boolean running, int speed) {
        double armPower = 0.1;

        if (running) {
            arm.setPower(Range.clip(speed, -1.0, 1.0));
            } else if (!running) {
            if (arm.getCurrentPosition() > arm.getTargetPosition()) {
                arm.setPower(-armPower);
            } else if (arm.getCurrentPosition() < arm.getTargetPosition()) {
                arm.setPower(armPower);
            } else {
                arm.setPower(0.0);
            }
        }




        //Arm up and down
        if (gamepad2.right_stick_y > 0.0) {
            arm.setTargetPosition(arm.getCurrentPosition() + 112);
        } else if (gamepad2.right_stick_y < 0.0) {

            arm.setTargetPosition(arm.getCurrentPosition() - 112);
        }

    }

    public void resetEncoders(DcMotor motor) {

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    public void setMotorToRunToPos(DcMotor motor) {

        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

    }

}