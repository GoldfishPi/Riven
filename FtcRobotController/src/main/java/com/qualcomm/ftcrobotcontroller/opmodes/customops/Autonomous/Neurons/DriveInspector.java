package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 3/11/16.
 */
public class DriveInspector extends Neuron {
    public DriveInspector(AutonomousMindContainer container) {
        instance = container;
    }

    public int leftDriveLastEncoder  = 0,
               rightDriveLastEncoder = 0,
               activateAfterTicks  = 50,
               ticks               = 0,
               differenceThreshold = 10;

    public boolean faultDetected = false;


    public void ensureDriveMoving() {
        int left, right;

        if (activateAfterTicks >= ticks) {
            if (Math.abs(instance.lDrivePower) > 0.005) {
                left = Math.abs(instance.getEncoderValue(instance.lDrive));

                if (left > leftDriveLastEncoder + differenceThreshold) {
                } else {
                    faultDetected = true;
                }
            }

            if (Math.abs(instance.rDrivePower) > 0.005) {
                right = Math.abs(instance.getEncoderValue(instance.rDrive));

                if (right > rightDriveLastEncoder + differenceThreshold) {
                } else {
                    faultDetected = true;
                }
            }

            if (faultDetected) {
                instance.scream();
            }
        }

        leftDriveLastEncoder  = Math.abs(instance.getEncoderValue(instance.lDrive));
        rightDriveLastEncoder = Math.abs(instance.getEncoderValue(instance.rDrive));
        faultDetected = false;
        ticks++;
    }

    public void resetSystem() {
        ticks                 = 0;
        leftDriveLastEncoder  = 0;
        rightDriveLastEncoder = 0;
    }
}
