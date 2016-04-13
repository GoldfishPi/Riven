package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by cyberarm on 3/10/16.
 *
 * Autonomous construction, heavy lifting, and sky diving; this does it all.
 *
 * This class adds instructions for: encoder distances, motor power, servo positions, and collision mode,
 * to a list for AutonomousMindContainer to work through.
 */
public class AutonomousConstruction extends Neuron {
    public AutonomousConstruction(AutonomousMindContainer container) {
        instance = container;
    }

        // Use a generic state (largely unused)
    public void addState(int state) {
        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;
    }

    // Have drive train move.
    public void addDriveAction(int state, int leftDrive, int rightDrive, double leftPower, double rightPower) {
        // Set needsDrive to true if the "Mind" uses the drive train
        instance.needsDrive = true;

        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;

        ensureMatchingGoal(leftDrive, leftPower);
        ensureMatchingGoal(rightDrive, rightPower);

        double[] array = {leftDrive, rightDrive, leftPower, rightPower, 0};
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }
    // Have drive train move.
    public void addDriveAction(int state, int leftDrive, int rightDrive, double leftPower, double rightPower, boolean stopMotors) {
        // Set needsDrive to true if the "Mind" uses the drive train
        instance.needsDrive = true;

        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;

        ensureMatchingGoal(leftDrive, leftPower);
        ensureMatchingGoal(rightDrive, rightPower);

        if (stopMotors) {
            double[] array = {leftDrive, rightDrive, leftPower, rightPower, 1};
            instance.actionArray[instance.actionIndex] = array;
        } else {
            double[] array = {leftDrive, rightDrive, leftPower, rightPower, 0};
            instance.actionArray[instance.actionIndex] = array;
        }
        instance.actionIndex++;
    }

    // Have servo set position
    public void addServoAction(int state, double position) {
        instance.needsDumper = true;
        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;

        double[] array = new double[1];
        array[0] = position;
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Have the robot wait for a period of time.
    public void addWaitAction(double ticks) {
        instance.debugArray[instance.debugArrayIndex] = STATE_WAIT;
        instance.debugArrayIndex++;

        instance.actionArray[instance.actionIndex][0] = ticks;
        instance.actionIndex++;
    }

    // Use the arm (Actuator) in parallel of state machine
    public void addArmAction(int state, int armPosition, double armPower) {
        instance.needsArm = true;
        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;

        ensureMatchingGoal(armPosition/3, armPower);

        double[] array = {armPosition/3, armPower, 0}; // Arm encoder is reporting negative when power is positive...
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Lock state machine, then use the arm (Actuator)
    public void addBlockingArmAction(int state, int armPosition, double armPower) {
        instance.needsArm = true;
        instance.debugArray[instance.debugArrayIndex] = state;
        instance.debugArrayIndex++;

        ensureMatchingGoal(armPosition/3, armPower);

        double[] array = {armPosition/3, armPower, 1}; // Arm encoder is reporting negative when power is positive...

        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Have the phones vibrator run.
    public void addVibratorAction(int duration) {
        instance.debugArray[instance.debugArrayIndex] = VIBRATOR_ACTION;
        instance.debugArrayIndex++;

        double[] array = {duration};
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Have the phone beep in distress.
    public void addScreamAction() {
        instance.debugArray[instance.debugArrayIndex] = SCREAM_ACTION;
        instance.debugArrayIndex++;
        instance.actionIndex++;
    }

    // Have the phone beep, relaxed-ly.
    public void addRelievedAction() {
        instance.debugArray[instance.debugArrayIndex] = RELIEVED_ACTION;
        instance.debugArrayIndex++;
        instance.actionIndex++;
    }

    // Use the winch in parallel with the state machine.
    public void addWinchAction(int winchPosition, double winchPower) {
        instance.needsWinch = true;
        instance.debugArray[instance.debugArrayIndex] = WINCH_ACTION;
        instance.debugArrayIndex++;

        ensureMatchingGoal(winchPosition, winchPower);

        double[] array = {winchPosition, winchPower, 0};
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Lock the state machine then use the winch.
    public void addBlockingWinchAction(int winchPosition, double winchPower) {
        instance.needsWinch = true;
        instance.debugArray[instance.debugArrayIndex] = WINCH_ACTION;
        instance.debugArrayIndex++;

        ensureMatchingGoal(winchPosition, winchPower);

        double[] array = {winchPosition, winchPower, 1};
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    public void addCompleteEncoderResetAction() {
        instance.debugArray[instance.debugArrayIndex] = ENCODER_RESET_ACTION;
        instance.debugArrayIndex++;


        instance.actionIndex++;
    }

    public void setCollisionProfile(int profile) {
        instance.debugArray[instance.debugArrayIndex] = SET_COLLISION_PROFILE;
        instance.debugArrayIndex++;

        double[] array = {profile};
        instance.actionArray[instance.actionIndex] = array;
        instance.actionIndex++;
    }

    // Method that checks for mismatch drive power and encoder target.
    // e.g. 100 target, -0.2 power, would trigger this.
    public void ensureMatchingGoal(int encoderTarget, double motorPower) {
        if ((double)encoderTarget > 0 && motorPower > 0.0) {
        } else if ((double)encoderTarget < 0 && motorPower < 0.0) {
        } else if ((double)encoderTarget == 0 && motorPower == 0.0) {

        } else {
            instance.scream();
            throw new RuntimeException("HANG DETECTED!\nA motor power and encoder target are not both greater than 0 or less than 0.");
        }
    }
}
