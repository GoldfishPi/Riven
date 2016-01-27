package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by root on 1/26/16.
 */
public class TheSuperAwesomeServoTest extends OpMode {

    private Servo lovePonny;
    private boolean toggle;


    public void init()
    {

        lovePonny = hardwareMap.servo.get("theDumper");

    }

    public void loop()
    {
        if(toggle){

            lovePonny.setPosition(0.0);

        }else{

            lovePonny.setPosition(1.0);

        }

    }

}
