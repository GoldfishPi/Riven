package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by root on 1/23/16.
 */
public class SingleMotorTest extends OpMode {

    private DcMotor motor;

    @Override
    public void init(){

        motor = hardwareMap.dcMotor.get("motor");

    }

    @Override
    public void loop(){

        motor.setPower(0.5);

    }

    @Override
    public void stop(){
        motor.setPower(0.0);
    }
}
