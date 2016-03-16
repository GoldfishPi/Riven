package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Sensors;

import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by goldfishpi on 3/12/16.
 */
public class UltraSonic {

    public double getPings(int pingAmount, UltrasonicSensor sensor)
    {

        double[] pings = new double[100];
        double addedPings = 0;
        double adveragePings = 0;

        for(int i = 0; i < pingAmount;i++){
            pings[i] = sensor.getUltrasonicLevel();
            addedPings += pings[i];
        }

        adveragePings = addedPings/pingAmount;

        return adveragePings;
    }
}
