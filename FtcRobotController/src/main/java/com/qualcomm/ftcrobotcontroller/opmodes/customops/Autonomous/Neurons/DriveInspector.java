package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by cyberarm on 3/11/16.
 *
 * This following is intended to be used for detecting if the wheels are moving when they're
 * supposed to, and decide the best way to recover, if needed.
 */
public class    DriveInspector extends Neuron {
    public DriveInspector(AutonomousMindContainer container) {
        instance = container;
    }

    public int  leftDriveLastEncoder  = 0,
                rightDriveLastEncoder = 0,
                activateAfterTicks  = 250,
                ticks               = 0,
                ticksSinceFault     = 0,
                waitForCheck        = 15,
                differenceThreshold = 3,
                faultID             = 0,
                currentFaultID      = 1,
                repeatFailures      = 0,
                failureThreshold    = 4;

    public double maxDrivePower    = 0.7;

    public boolean faultDetected = false;


    public void ensureDriveMoving() {
        if (ticks >= waitForCheck) {
            checker();

            leftDriveLastEncoder  = instance.getEncoderValue(instance.lDrive);
            rightDriveLastEncoder = instance.getEncoderValue(instance.rDrive);
        }

        faultDetected = false;
        ticksSinceFault++;
        ticks++;
    }
    public void checker() {
        int left, right;

        if (ticks >= activateAfterTicks && ticksSinceFault >= activateAfterTicks) {
            if (instance.lDrivePower >= 0.05 && !faultDetected) {
                left = Math.abs(instance.getEncoderValue(instance.lDrive));

                if (left >= leftDriveLastEncoder + differenceThreshold) {
                } else {
                    instance.puts("leftDrive fault: " + (leftDriveLastEncoder + differenceThreshold) + " encoder: "+left);
                    faultDetected = true;
                }
            }
            if (instance.lDrivePower <= -0.05 && !faultDetected) {
                left = instance.getEncoderValue(instance.lDrive);

                if (left <= leftDriveLastEncoder + differenceThreshold*-1) {
                } else {
                    instance.puts("leftDrive (reverse) fault: " + (leftDriveLastEncoder + differenceThreshold*-1) + " encoder: "+left);
                    faultDetected = true;
                }
            }

            if (instance.rDrivePower >= 0.05 && !faultDetected) {
                right = instance.getEncoderValue(instance.rDrive);

                if (right >= rightDriveLastEncoder + differenceThreshold) {
                } else {
                    instance.puts("rightDrive fault: " + (rightDriveLastEncoder + differenceThreshold) + " encoder: "+right);
                    faultDetected = true;
                }
            }
            if (instance.rDrivePower <= -0.05 && !faultDetected) {
                right = instance.getEncoderValue(instance.rDrive);

                if (right <= rightDriveLastEncoder + differenceThreshold*-1) {
                } else {
                    instance.puts("rightDrive (reverse) fault: " + (rightDriveLastEncoder + differenceThreshold*-1) + " encoder: "+right);
                    faultDetected = true;
                }
            }

            if (faultDetected) {
                ticksSinceFault = 0;

                if (repeatFailures >= failureThreshold) {
                    instance.puts("[REPEAT FAULT] FaultID: "+faultID+ " Failures on a row: "+repeatFailures);
                    instance.relieved();
                } else {
                    repeatFailures++;

                    instance.scream();
                    instance.puts("[FAULT] leftDrive Power: " + instance.lDrivePower);
                    instance.puts("[FAULT] rightDrive Power: " + instance.rDrivePower);

                    instance.lDrivePower = Range.clip(instance.lDrivePower * 1.5, 0.0, maxDrivePower);
                    instance.rDrivePower = Range.clip(instance.rDrivePower * 1.5, 0.0, maxDrivePower);

                    instance.lDrive.setPower(instance.lDrivePower);
                    instance.rDrive.setPower(instance.rDrivePower);
                }
            } else {
                repeatFailures = 0;
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
