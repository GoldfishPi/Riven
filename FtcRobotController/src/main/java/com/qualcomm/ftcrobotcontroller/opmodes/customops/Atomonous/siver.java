package com.qualcomm.ftcrobotcontroller.opmodes.customops.Atomonous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goldfishpi on 11/21/15.
 */
public class siver extends OpMode {

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

    private DcMotor[] motors = {lDrive, rDrive, lFinger, rFinger, lGill, rGill, lWinch, rWinch, lArm, rArm};

    private int[] states = {1,2,3,4};

    List<String> robotStates = new ArrayList<String>();

    private int currentState;

    private boolean running = true;

    private int rotation = 720;

    //powers
    private double lowPower = 0.3;


    @Override
    public void init() {

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");
        /*lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        lWinch = hardwareMap.dcMotor.get("lWinch");
        rWinch = hardwareMap.dcMotor.get("rWinch");

        lArm = hardwareMap.dcMotor.get("lArm");
        rArm = hardwareMap.dcMotor.get("rArm");*/

        lDrive.setDirection(DcMotor.Direction.REVERSE);

        lDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);



    }

    @Override
    public void init_loop() {
        resetEncoder(lDrive);
        resetEncoder(rDrive);
    }

    @Override
    public void loop() {

       /*
        *
        * State 1:
        *   Drive untill red colout of tape is read
        * State 2:
        *   Turn 90°
        * State 3:
        *   Drive until wall is found by Ultra Sonic Sensor.
        * State 4:
        *   Elevate armature to aprox of 45°
        * State 5:
        *   Extend arm until climbers fall into shelter
        * State 6:
        *   Retract arm
        * State 7:
        *   Return armature to restposition
        * State 8:
        *   Drive backwords to become inline with the Mountain
        * State 9:
        *   Trun 90° to become parallel
        *
        */

        //(1:40)/2 = 720 ticks in a rotation
        //13 inches to a rotation

        telemetry.addData("ENCODER RIGHT", rDrive.getCurrentPosition());
        telemetry.addData("ENCODER LEFT", lDrive.getCurrentPosition());
        telemetry.addData("running",running);
        switch (states[currentState]){
            case 1:

                if(rDrive.getCurrentPosition() == -rotation * 8)
                {

                    rDrive.setPower(lowPower);
                }
                else {
                    rDrive.setPower(0.0);
                }
                if(lDrive.getCurrentPosition() == -rotation *8){
                    lDrive.setPower(lowPower);
                }
                else {
                    lDrive.setPower(0.0);
                }
            case 2:

        }

    }

    public void resetEncoder(DcMotor motor){
        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
}