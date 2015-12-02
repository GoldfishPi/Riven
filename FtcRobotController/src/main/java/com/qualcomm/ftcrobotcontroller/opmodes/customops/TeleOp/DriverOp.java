package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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


    private boolean aPressed;
    private boolean xPressed;


    public DriverOp() {

    }

    @Override
    public void init() {

        aPressed = false;
        xPressed = false;

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");


        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        armOut = hardwareMap.dcMotor.get("lWinch");
        armIn = hardwareMap.dcMotor.get("rWinch");

        arm = hardwareMap.dcMotor.get("arm");


        rDrive.setDirection(DcMotor.Direction.REVERSE);
        rFinger.setDirection(DcMotor.Direction.REVERSE);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        armIn.setDirection(DcMotor.Direction.REVERSE);


    }

    @Override
    public void stop(){

    }

    @Override
    public void init_loop(){


    }

    @Override
    public void loop(){
        controllerOne();
        controllerTwo();
    }

    public void controllerOne(){

        //Drive controls
        lDrive.setPower(gamepad1.left_stick_y);
        rDrive.setPower(gamepad1.right_stick_y);

        //Finger controls
        if(gamepad1.left_trigger != 0.0){
            lFinger.setPower(0.3);
        }
        else if(gamepad1.left_bumper){
            lFinger.setPower(-0.3);
        }
        else{
            lFinger.setPower(0.0);
        }
        if(gamepad1.right_trigger != 0.0){
            rFinger.setPower(0.3);
        }
        else if(gamepad1.right_bumper){
            rFinger.setPower(-0.3);
        }
        else{
            rFinger.setPower(0.0);
        }
        //gill controll go here
    }

    public  void controllerTwo(){


        //extending arm controlls.
        if (gamepad2.left_stick_y > 0.0){
            armOut.setPower(-1.0);
            armIn.setPower(0.5);
        }
        else if (gamepad2.left_stick_y < 0.0){
            armOut.setPower(1.0);
            armIn.setPower(-0.5);
        }
        else {
            armOut.setPower(0.0);
            armIn.setPower(0.0);
        }

        //raising arm controlls
        arm.setPower(gamepad2.right_stick_y);

    }
}//blag

