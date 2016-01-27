package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by root on 1/23/16.
 */
public class AllMotorTest extends OpMode {

    public DcMotor[] dcMotors = new DcMotor[8];
    public String[] motorNames = new String[]{"lDrive", "rDrive", "lFinger", "rFinger", "lGill", "rGill", "armExtender", "arm"};

    public void init(){

        for(int i = 0; i < dcMotors.length; i ++){
            dcMotors[i] = hardwareMap.dcMotor.get(motorNames[i]);
        }

        for(int i = 0; i < dcMotors.length; i ++){
            dcMotors[i].setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }

    }

    public void loop(){

        for(int i = 0; i < dcMotors.length; i ++){
            dcMotors[i].setPower(0.1);
            dcMotors[i].setTargetPosition(20);
            if(dcMotors[i].getCurrentPosition() != 0){
                dcMotors[i].setTargetPosition(0);
            }
            else{
                stop();
            }
        }

    }

    @Override
    public void stop(){
        for(int i = 0; i < dcMotors.length; i ++){
            dcMotors[i].setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }


}
