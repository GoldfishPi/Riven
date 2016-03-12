package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 3/11/16.
 *
 * This following is intended to be used for detecting if the wheels are moving when they're
 * supposed to, and decide the best way to recover, if needed.
 */
public class DriveInspector extends Neuron {
    public DriveInspector(AutonomousMindContainer container) {
        instance = container;
    }

    public int leftDriveLastEncoder  = 0,
               rightDriveLastEncoder = 0,
               activateAfterTicks  = 50,
               ticks               = 0,
               ticksSinceFault     = 15,
               differenceThreshold = 5;

    public boolean faultDetected = false;


    public void ensureDriveMoving() {
        int left, right;

        if (activateAfterTicks >= ticks && ticksSinceFault >= 15) {
            if (Math.abs(instance.lDrivePower) > 0.005) {
                left = Math.abs(instance.getEncoderValue(instance.lDrive));

                if (left >= leftDriveLastEncoder + differenceThreshold) {
                } else {
                    faultDetected = true;
                }
            }

            if (Math.abs(instance.rDrivePower) > 0.005) {
                right = Math.abs(instance.getEncoderValue(instance.rDrive));

                if (right >= rightDriveLastEncoder + differenceThreshold) {
                } else {
                    faultDetected = true;
                }
            }

            if (faultDetected) {
                ticksSinceFault = 0;
                instance.scream();
            }
        }

        leftDriveLastEncoder  = Math.abs(instance.getEncoderValue(instance.lDrive));
        rightDriveLastEncoder = Math.abs(instance.getEncoderValue(instance.rDrive));
        faultDetected = false;
        ticksSinceFault++;
        ticks++;
    }

    public void resetSystem() {
        ticks                 = 0;
        ticksSinceFault       = 15;
        leftDriveLastEncoder  = 0;
        rightDriveLastEncoder = 0;
    }
}
