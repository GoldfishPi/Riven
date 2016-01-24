package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousVariables;

/**
 * Created by cyberarm on 1/24/16.
 */
public class Accelerometer implements SensorEventListener {
    protected AutonomousVariables activeBrain = null;
    protected SensorManager sensorManager;
    protected Sensor accelerometer;
    protected float x = 0,
                    y = 0,
                    z = 0,
                    last_x,
                    last_y,
                    last_z;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            sensorEvent(event);
        }
    }

    public void sensorEvent(SensorEvent event) {
        last_x = x;
        last_y = y;
        last_z = z;
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if (activeBrain != null) {
            activeBrain.sensorAccelerometer(event);
        }
    }

    public void registerActiveBrain(AutonomousVariables brain) {
        activeBrain = brain;
    }

    public void unregisterActiveBrain() {
        activeBrain = null;
    }

    public void registerListener(FtcRobotControllerActivity ftcRobotControllerActivity) {
        sensorManager = (SensorManager) ftcRobotControllerActivity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener(FtcRobotControllerActivity ftcRobotControllerActivity) {
        sensorManager.unregisterListener(this);
    }
}
