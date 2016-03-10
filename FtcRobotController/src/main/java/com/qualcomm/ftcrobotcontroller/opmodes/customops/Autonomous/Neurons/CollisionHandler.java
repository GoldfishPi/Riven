package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 3/10/16.
 */
public class CollisionHandler extends Neuron {
    public AutonomousMindContainer instance;

    public CollisionHandler(AutonomousMindContainer container) {
        instance = container;
    }
    public void checkCollision() {
        if (instance.collisionLock) {
            instance.tickSinceCollision++;
            if (instance.tickSinceCollision >= 250) {
                instance.tickSinceCollision = 0;
                instance.collisionLock = false;
            }
        }

        if (instance.accelerometerTicks >= 4 && !instance.collisionLock) {

            if (Math.abs(instance.x) >= instance.COLLISION_THRESHOLD && !instance.collisionLock) {
                instance.scream();
                resolveCollision();
                instance.collisionLock = true;
            }

            if (Math.abs(instance.y) >= instance.COLLISION_THRESHOLD && !instance.collisionLock) {
                instance.scream();
                resolveCollision();
                instance.collisionLock = true;
            }
        }
    }

    public void resolveCollision() {
        instance.collisionDetected = true;
        instance.collisionID++;
        boolean leftDrivePowered = false;
        boolean rightDrivePowered = false;

        // Only process collision resolution if not already processed and not in a wait state
        if (!instance.collisionLock && (instance.stateMachineArray[instance.stateMachineIndex] != STATE_WAIT)) {

            switch (instance.collisionProfile) {
                case COLLISION_IGNORE:
                instance.relieved();
                    break;
                case COLLISION_WAIT:
                    injectWait(120);
                    break;
                case COLLISION_CHANGE_DIRECTION:
                    instance.telemetry.addData("<DATA>", "Collision Change Direction");
                    if (Math.abs(instance.lDrivePower) >= 0.1){ leftDrivePowered = true; }
                    if (Math.abs(instance.rDrivePower) >= 0.1){ rightDrivePowered = true; }

                    if (leftDrivePowered && rightDrivePowered) {
                        // Can safely change direction due to not being in a turn
                        injectDriveDirectionChange();
                    } else {
                        // Drive might be in a turn or not moving, would mal-align robot if direction changed to straight back
                        // Rusty gives up and waits about a second to resume.
                        injectWait(100);
                        instance.relieved();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // FIXME: Compensate for distance already travelled
    public void injectWait(int ticks) {
        // Inject wait state into current actionArray index and stateMachine index, then reorder array and adjust currently running state to compensate for distance already traveled
        int[] stateArrayCopied  = new int[100];
        double[][] actionArrayCopied = new double[100][4];
        double[] newActionArray = new double[4];
        double newLeftTarget,
                newRightTarget;

        for (int i = 0; i < instance.stateMachineArray.length; i++) {
            if (i >= 99) {break;} // can't set 100!
            instance.puts("StateMachineArray index:" + i);
            if (i < instance.stateMachineIndex) {
                stateArrayCopied[i] = instance.stateMachineArray[i];
            } else if (i == instance.stateMachineIndex) {
                stateArrayCopied[i] = STATE_WAIT;
                stateArrayCopied[i+1] = instance.stateMachineArray[instance.stateMachineIndex];
            } else if (i > instance.stateMachineIndex) {
                stateArrayCopied[i+1] = instance.stateMachineArray[i];
            } else { instance.puts("How is this possible?"); }
        }

        for (int i = 0; i < instance.actionArray.length; i++) {
            if (i >= 99) {break;} // can't set 100!
            instance.puts("ActionArray index:" + i);
            if (i < instance.actionIndex) {
                actionArrayCopied[i] = instance.actionArray[i];
            } else if (i == instance.actionIndex) { // TODO: Test that this works as expected and works with negative targets
                newLeftTarget =  ((instance.leftEncoderTarget-instance.getEncoderValue(instance.lDrive)) -instance.actionArray[instance.actionIndex][0]);
                newRightTarget=  ((instance.rightEncoderTarget-instance.getEncoderValue(instance.rDrive))-instance.actionArray[instance.actionIndex][1]);
                newActionArray[0] = newLeftTarget;
                newActionArray[1] = newRightTarget;
                newActionArray[2] = instance.actionArray[instance.actionIndex][2];
                newActionArray[3] = instance.actionArray[instance.actionIndex][3];
                actionArrayCopied[i][0] = ticks;
                actionArrayCopied[i+1]  = newActionArray;
            } else if (i > instance.actionIndex) {
                actionArrayCopied[i+1] = instance.actionArray[i];
            } else { instance.puts("How is this possible?"); }
        }

        instance.stateMachineArray = stateArrayCopied;
        instance.actionArray       = actionArrayCopied;

        if (instance.needsDrive) { instance.setDrivePower(0.0, 0.0);}
        instance.lockMachine = false;
    }

    // FIXME: Compensate for distance already travelled
    public void injectDriveDirectionChange() {
        int[] stateArrayCopied  = new int[100];
        double[][] actionArrayCopied = new double[100][4];
        int direction = 1;
        int recoveryDistance = 800;
        int state = DRIVE_FORWARD;
        double power = 0.3;
        double[] newActionArray = new double[4];
        double newLeftTarget,
                newRightTarget;

        if (instance.lDrivePower > 0.0) { direction = -1; state = DRIVE_BACKWARD; }


        for (int i = 0; i < instance.stateMachineArray.length; i++) {
            if (i >= 99) {break;} // can't set 100!
            instance.puts("StateMachineArray index:" + i);
            if (i < instance.stateMachineIndex) {
                stateArrayCopied[i] = instance.stateMachineArray[i];
            } else if (i == instance.stateMachineIndex) {
                stateArrayCopied[i] = state;
                stateArrayCopied[i+1] = instance.stateMachineArray[instance.stateMachineIndex];
            } else if (i > instance.stateMachineIndex) {
                stateArrayCopied[i+1] = instance.stateMachineArray[i];
            } else { instance.puts("How is this possible?"); }
        }

        for (int i = 0; i < instance.actionArray.length; i++) {
            if (i >= 99) {break;} // can't set 100!
            instance.puts("ActionArray index:" + i);
            if (i < instance.actionIndex) {
                actionArrayCopied[i] = instance.actionArray[i];
            } else if (i == instance.actionIndex) {
                actionArrayCopied[i][0] = recoveryDistance * direction;
                actionArrayCopied[i][1] = recoveryDistance * direction;
                actionArrayCopied[i][2] = power * direction;
                actionArrayCopied[i][3] = power * direction;

                newLeftTarget =  ((instance.leftEncoderTarget-instance.getEncoderValue(instance.lDrive)) -instance.actionArray[instance.actionIndex][0]);
                newRightTarget=  ((instance.rightEncoderTarget-instance.getEncoderValue(instance.rDrive))-instance.actionArray[instance.actionIndex][1]);
                newActionArray[0] = newLeftTarget;
                newActionArray[1] = newRightTarget;
                newActionArray[2] = instance.actionArray[instance.actionIndex][2];
                newActionArray[3] = instance.actionArray[instance.actionIndex][3];
                actionArrayCopied[i+1]  = newActionArray;
            } else if (i > instance.actionIndex) {
                actionArrayCopied[i+1] = instance.actionArray[i];
            } else { instance.puts("How is this possible?"); }
        }

        instance.stateMachineArray = stateArrayCopied;
        instance.actionArray       = actionArrayCopied;

        if (instance.needsDrive) { instance.setDrivePower(0.0, 0.0); }
        instance.lockMachine = false;
    }
}
