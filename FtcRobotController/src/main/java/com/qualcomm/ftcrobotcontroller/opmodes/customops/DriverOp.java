package com.qualcomm.ftcrobotcontroller.opmodes.customops;

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

    private DcMotor lWinch;
    private DcMotor rWinch;

    private DcMotor lArm;
    private DcMotor rArm;


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

        lWinch = hardwareMap.dcMotor.get("lWinch");
        rWinch = hardwareMap.dcMotor.get("rWinch");

        lArm = hardwareMap.dcMotor.get("lArm");
        rArm = hardwareMap.dcMotor.get("rArm");

        rDrive.setDirection(DcMotor.Direction.REVERSE);
        rFinger.setDirection(DcMotor.Direction.REVERSE);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        rWinch.setDirection(DcMotor.Direction.REVERSE);
        rArm.setDirection(DcMotor.Direction.REVERSE);

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
        if(gamepad1.left_trigger > 1){
            lFinger.setPower(0.3);
        }
        else if(gamepad1.left_bumper == true){
            lFinger.setPower(-0.3);
        }
        else{
            lFinger.setPower(0.0);
        }
        if(gamepad1.right_trigger > 1){
            rFinger.setPower(0.3);
        }
        else if(gamepad1.right_bumper == true){
            rFinger.setPower(-0.3);
        }
        else{
            rFinger.setPower(0.0);
        }
        //gill controll
        if(gamepad1.x){
            lGill.setPower(0.3);
        }
        else{
            lGill.setPower(-0.1);
        }
        if(gamepad1.b){
            rGill.setPower(0.3);
        }
        else{
            rGill.setPower(0.1);
        }
    }

    public  void controllerTwo(){



    }
}
//Bye.
// sup


//yo yo yo
