package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.qualcomm.ftcrobotcontroller.R;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

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

    private DcMotor armOut;
    private DcMotor armIn;

    private DcMotor arm;

    boolean evening = false;

    String forward = "forward";
    String backward = "backward";

    private boolean encoderInit = true;

    private int armOutInital;
    private int armOutFinal;

    private int armInInital;
    private int armInFinal;

    private int armOutDelta;
    private int armInDelta;

    private double rotation = 280 * 3;

    private int lastTargetPosition;

    boolean rFast;
    boolean lFast;

    private boolean yPressed = false;
    private boolean aPressed = false;

    private boolean xPressed = false;
    private boolean bPressed = false;

    boolean armRatchet = false;

    public double armSpeed = 0.3;



    public DriverOp() {

    }

    @Override
    public void init() {

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");


        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        armOut = hardwareMap.dcMotor.get("armOut");
        armIn = hardwareMap.dcMotor.get("armIn");

        arm = hardwareMap.dcMotor.get("arm");

        rDrive.setDirection(DcMotor.Direction.FORWARD);
        lDrive.setDirection(DcMotor.Direction.REVERSE);
        rFinger.setDirection(DcMotor.Direction.REVERSE);
        lFinger.setDirection(DcMotor.Direction.FORWARD);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        lGill.setDirection(DcMotor.Direction.FORWARD);
        armIn.setDirection(DcMotor.Direction.FORWARD);
        armOut.setDirection(DcMotor.Direction.FORWARD);

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armIn);
        resetEncoders(armOut);
        resetEncoders(arm);

        armOutInital = armOut.getCurrentPosition() + 100000;
        armInInital = armIn.getCurrentPosition() + 100000;

    }

    @Override
    public void stop() {

    }

    @Override
    public void init_loop() {

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armIn);
        resetEncoders(armOut);
        resetEncoders(arm);

        armOutInital = armOut.getCurrentPosition() + 100000;
        armInInital = armIn.getCurrentPosition() + 100000;
        setMotorToRunToPos(arm);
        setMotorToRunToPos(lDrive);
        setMotorToRunToPos(rDrive);

    }

    @Override
    public void loop() {
        telemetry.addData("ArmSpeed", armSpeed);
        if (gamepad1.b) {
            System.out.println("lDrive : " + lDrive.getCurrentPosition());
            System.out.println("rDrive : " + rDrive.getCurrentPosition());
            System.out.println("Arm : " + arm.getCurrentPosition());
            System.out.println("armIn : " + armIn.getCurrentPosition());
            System.out.println("armOut : " + armOut.getCurrentPosition());
        }
        controllerOne();
        controllerTwo();
        setMotorToRunToPos(arm);
    }

    public void controllerOne() {
        {

            //Drive controls
            if (lDrive.getChannelMode() == DcMotorController.RunMode.RUN_TO_POSITION && rDrive.getChannelMode() == DcMotorController.RunMode.RUN_TO_POSITION) {
                lDrive.setPower(gamepad1.left_stick_y);
                rDrive.setPower(gamepad1.right_stick_y);
            }

            if (gamepad1.left_stick_y < 0.0) {
                lDrive.setTargetPosition(lDrive.getCurrentPosition() - (int) rotation);
            } else if (gamepad1.left_stick_y > 0.0) {
                lDrive.setTargetPosition(lDrive.getCurrentPosition() + (int) rotation);
            }
            if (gamepad1.right_stick_y < 0.0) {
                rDrive.setTargetPosition(rDrive.getCurrentPosition() - (int) rotation);
            } else if (gamepad1.right_stick_y > 0.0) {
                rDrive.setTargetPosition(rDrive.getCurrentPosition() + (int) rotation);
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

            if(gamepad2.x && !xPressed){
                xPressed = true;
                armSpeed+= 0.1;
            }
            else if(!gamepad2.x && xPressed){
                xPressed = false;
            }
            if(gamepad2.x && !bPressed){
                xPressed = true;
                armSpeed-= 0.1;
            }
            else if(!gamepad2.x && bPressed){
                bPressed = false;
            }

            if(gamepad2.right_stick_y != 0.0) {
                armControlls(armSpeed, true);
            }
            else{
                armControlls(armSpeed, false);
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

        double ratio = 1;
        double driveSpeed = speed;
        double slowSpeed = 0.0;
        int lbm = 200;

        double absInEncoder = Math.abs(armIn.getCurrentPosition());
        double absOutEncoder = Math.abs(armOut.getCurrentPosition());

        if (direction == "out") {

            setMotorToRunToPos(armIn);
            setMotorToRunToPos(armOut);

            if (absOutEncoder >= absInEncoder - lbm) {
                armOut.setPower(driveSpeed);
                armIn.setPower(driveSpeed);
            } else if (absInEncoder - lbm >= absOutEncoder) {
                armOut.setPower(driveSpeed);
                armIn.setPower(slowSpeed);
            }

            armOut.setTargetPosition(armOut.getCurrentPosition() + (int) (rotation));
            armIn.setTargetPosition((int) (armIn.getCurrentPosition() + (rotation / ratio)));
            String.valueOf(armIn);

        }
        if (direction == "in") {

            setMotorToRunToPos(armIn);
            setMotorToRunToPos(armOut);

            if (absInEncoder <= absOutEncoder + lbm) {
                armOut.setPower(-driveSpeed);
                armIn.setPower(-driveSpeed);
            } else if (absOutEncoder + lbm <= absInEncoder) {

                armOut.setPower(-slowSpeed);
                armIn.setPower(-driveSpeed);

            }

            armOut.setTargetPosition(armOut.getCurrentPosition() - (int) (rotation));
            armIn.setTargetPosition((int) (armIn.getCurrentPosition() - (rotation / ratio)));

        }
    }

    public void armControlls(double speed, boolean running) {

        if (running) {
            arm.setPower(speed);
        } else if (!running) {
            if (arm.getCurrentPosition() > arm.getTargetPosition()) {
                arm.setPower(-0.1);
            }
            else if(arm.getCurrentPosition() < arm.getTargetPosition()){
                arm.setPower(0.1);
            }
            else {
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

        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


    }

    public void setMotorToRunToPos(DcMotor motor) {

        motor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

    }

}