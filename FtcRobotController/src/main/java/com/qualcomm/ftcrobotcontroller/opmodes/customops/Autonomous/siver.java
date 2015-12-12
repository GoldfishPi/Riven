package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

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

    private DcMotor armIn;
    private DcMotor armOut;

    private DcMotor arm;

    private int[] states = {1,2,3,4};

    List<String> robotStates = new ArrayList<String>();

    private int currentState;

    private boolean running = true;

    private int rotation = 720;

    //powers
    private double lowPower = 0.3;

    private AutonomousOp crap = new AutonomousOp();

    @Override
    public void init() {
        
    }

    @Override
    public void init_loop() {
        resetEncoder(lDrive);
        resetEncoder(rDrive);


    }

    @Override
    public void loop() {

        switch (states[currentState]){

            // turn 45 degrees
            case 0:

                crap.turnRobotCalculation(24, 45);

                break;
            default:
                break;


        }

    }

    public void resetEncoder(DcMotor motor){
        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
}