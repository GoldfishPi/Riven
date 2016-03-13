package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;
import com.qualcomm.robotcore.util.Range;

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
               activateAfterTicks  = 150,
               ticks               = 0,
               ticksSinceFault     = 0,
               waitForCheck        = 15,
               differenceThreshold = 3;

    public boolean faultDetected = false;


    public void ensureDriveMoving() {
        if (ticks >= waitForCheck) {
            checker();
        }

        leftDriveLastEncoder  = Math.abs(instance.getEncoderValue(instance.lDrive));
        rightDriveLastEncoder = Math.abs(instance.getEncoderValue(instance.rDrive));
        faultDetected = false;
        ticksSinceFault++;
        ticks++;
    }
    public void checker() {
        int left, right;

        if (ticks >= activateAfterTicks && ticksSinceFault >= activateAfterTicks) {
            if (Math.abs(instance.lDrivePower) >= 0.05) {
                left = Math.abs(instance.getEncoderValue(instance.lDrive));

                if (left >= leftDriveLastEncoder + differenceThreshold) {
                    instance.puts("[OK] leftDrive");
                } else {
                    faultDetected = true;
                }
            }

            if (Math.abs(instance.rDrivePower) >= 0.05) {
                right = Math.abs(instance.getEncoderValue(instance.rDrive));

                if (right >= rightDriveLastEncoder + differenceThreshold) {
                    instance.puts("[OK] rightDrive");
                } else {
                    faultDetected = true;
                }
            }

            if (faultDetected) {
                ticksSinceFault = 0;
                instance.scream();
                instance.puts("[FAULT] leftDrive Power: " + instance.lDrivePower);
                instance.puts("[FAULT] rightDrive Power: " + instance.rDrivePower);

                instance.lDrivePower = Range.clip(instance.lDrivePower*1.5, 0.0, 0.5);
                instance.rDrivePower = Range.clip(instance.rDrivePower*1.5, 0.0, 0.5);

                instance.lDrive.setPower(instance.lDrivePower);
                instance.rDrive.setPower(instance.rDrivePower);
            } else {
                instance.puts("[OKAY] leftDrive Power: " + instance.lDrivePower);
                instance.puts("[OKAY] rightDrive Power: " + instance.rDrivePower);
            }
        }
    }

    public void resetSystem() {
        ticks                 = 0;
        ticksSinceFault       = 0;
        leftDriveLastEncoder  = 0;
        rightDriveLastEncoder = 0;
    }
}
