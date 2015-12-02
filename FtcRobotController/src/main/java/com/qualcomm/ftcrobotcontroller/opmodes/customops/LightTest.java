package com.qualcomm.ftcrobotcontroller.opmodes.customops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by goldfishpi on 12/1/15.
 */
public class LightTest extends OpMode {

    LightSensor sensor;
    @Override
    public void init(){

        sensor = hardwareMap.lightSensor.get("light");
        sensor.enableLed(true);

    }

    @Override
    public void loop(){

        telemetry.addData("light",sensor.getLightDetected());

    }

}