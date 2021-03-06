package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import android.content.Context;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons.AutonomousConstruction;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons.CollisionHandler;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons.DriveInspector;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Sensors.UltraSonic;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by goldfishpi on 12/12/15.
 */
public class AutonomousMindContainer extends OpMode {

    public static final int
            STATE_STOP = 0,
            DRIVE_FORWARD = 1,
            DRIVE_BACKWARD = 2,
            STATE_WAIT = 3,
            THE_DUMPER = 4,
            ARM_ACTION = 5,

    SET_COLLISION_PROFILE = 6,
            VIBRATOR_ACTION = 7,
            SCREAM_ACTION = 8,
            RELIEVED_ACTION = 9,
            WINCH_ACTION = 10,
            DRIVE_ARM_ACTION = 11,
            DRIVE_WINCH_ACTION = 12,

    LEFT_SHURIKEN = 13,
            RIGHT_SHURIKEN = 14,
            STATE_PING = 15,
            THE_DUMPER_SLOW = 16,
            ENCODER_RESET_ACTION = 17,


    COLLISION_IGNORE = 0,
            COLLISION_CHANGE_DIRECTION = 1,
            COLLISION_WAIT = 2;
    final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    /* Begin variable definitions */
    public CollisionHandler collisionHandler;
    public AutonomousConstruction builder;
    public DriveInspector driveInspector;
    public String currentMachineState;
    public DcMotor lDrive;
    public DcMotor rDrive;
    public DcMotor lFinger;
    public DcMotor rFinger;
    public DcMotor lGill;
    public DcMotor rGill;
    public Servo theDumper;
    public Servo leftShuriken;
    public Servo rightShuriken;
    public DcMotor winch;
    public DcMotor arm;
    public UltrasonicSensor usSensor;
    public Vibrator vibrator;
    public double armSpeed;
    public int armLocation;
    public int leftEncoderTarget;
    public int rightEncoderTarget;
    public double lDrivePower;
    public double rDrivePower;
    public double theDumperPosition;
    public int theDumperTick;
    public int stateMachineIndex = 0;
    public int debugArrayIndex = 0;
    public int actionIndex = 0;
    public double[][] actionArray;
    public int[] stateMachineArray = new int[100];
    public int[] debugArray;
    public boolean lockMachine = false;
    public boolean machineCompleted = false;
    public int currentWaitTicks = 0;
    public int targetWaitTicks = 10;
    public FtcRobotControllerActivity instance;
    public int collisionProfile = COLLISION_IGNORE,
            COLLISION_THRESHOLD = 6,
            tickSinceCollision = 0,
            collisionID = 0,
            accelerometerTicks = 0,
            winchTarget = 0;
    public double winchSpeed = 0.0;
    public boolean collisionLock = false,
            collisionDetected = false,
            needsDrive = false,
            needsDumper = false,
            needsArm = false,
            needsWinch = false,
            needsSensor = false,
            needsShurikens = true;
    public float deadX,
            deadY,
            deadZ,
            oldX,
            oldY,
            oldZ,
            x,
            y,
            z;
    boolean leftDriveDone = false;
    boolean rightDriveDone = false;
    String currentState = "Not set";
    private UltraSonic sonic = new UltraSonic();

    /* End variable definitions */

    public DcMotor getMotor(String string) {
        return hardwareMap.dcMotor.get(string);
    }

    public Servo getServo(String string) {
        return hardwareMap.servo.get(string);
    }

    public int getEncoderValue(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    public double getEncoderValue(Servo servo) {
        return servo.getPosition();
    }

    public void setEncoderTarget(int leftEncoder, int rightEncoder) {
        leftEncoderTarget = getEncoderValue(lDrive) + leftEncoder;
        rightEncoderTarget = getEncoderValue(rDrive) + rightEncoder;

        lDrive.setTargetPosition(leftEncoderTarget);
        rDrive.setTargetPosition(rightEncoderTarget);
    }

    public void setDrivePower(double leftPower, double rightPower) {
        lDrivePower = Range.clip(leftPower, -1.0, 1.0);
        rDrivePower = Range.clip(rightPower, -1.0, 1.0);
        lDrive.setPower(lDrivePower);
        rDrive.setPower(rDrivePower);
    }

    public void resetEncodersAuto(DcMotor motor) {
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    // Clean up state machine (a lot, hopefully)

    // Checks that the wheels are where they should be, tells the state machine to proceed
    public boolean positiveDriveCheck() {
        boolean checkFinal = false;
        boolean stopMotors = true;

        if (actionArray[actionIndex][4] == 0) {
            stopMotors = true;
        } else {
            stopMotors = false;
        }

        if (stopMotors) {
            if (getEncoderValue(lDrive) >= leftEncoderTarget - 15) {
                lDrivePower = 0.0;
                lDrive.setPower(lDrivePower);
            }

            if (getEncoderValue(rDrive) >= rightEncoderTarget - 15) {
                rDrivePower = 0.0;
                rDrive.setPower(rDrivePower);
            }

            if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
                checkFinal = true;

                lDrivePower = 0.0;
                rDrivePower = 0.0;
                lDrive.setPower(lDrivePower);
                rDrive.setPower(rDrivePower);

                resetEncodersAuto(lDrive);
                resetEncodersAuto(rDrive);
                driveInspector.resetSystem();


                lockMachine = false;
                stateMachineIndex++;
            }
        } else {

            if (getEncoderValue(lDrive) >= leftEncoderTarget - 15) {
                leftDriveDone = true;
            }

            if (getEncoderValue(rDrive) >= rightEncoderTarget - 15) {
                rightDriveDone = true;
            }

            if (leftDriveDone && rightDriveDone) {
                checkFinal = true;

                leftDriveDone = false;
                rightDriveDone = false;

                resetEncodersAuto(lDrive);
                resetEncodersAuto(rDrive);
                driveInspector.resetSystem();


                lockMachine = false;
                stateMachineIndex++;
            }

        }

        return checkFinal;
    }


    // Checks that the wheels are where they should be, tells the state machine to proceed
    public boolean negativeDriveCheck() {
        boolean checkFinal = false;
        boolean stopMotors = true;

        if (actionArray[actionIndex][4] == 0) {
            stopMotors = true;
        } else {
            stopMotors = false;
        }

        if (stopMotors) {
            if (getEncoderValue(lDrive) <= leftEncoderTarget + 15) {
                lDrivePower = 0.0;
                lDrive.setPower(lDrivePower);
            }

            if (getEncoderValue(rDrive) <= rightEncoderTarget + 15) {
                rDrivePower = 0.0;
                rDrive.setPower(rDrivePower);
            }

            if ((lDrivePower >= -0.1) && (rDrivePower >= -0.1)) {
                lDrivePower = 0.0;
                rDrivePower = 0.0;
                lDrive.setPower(lDrivePower);
                rDrive.setPower(rDrivePower);

                resetEncodersAuto(lDrive);
                resetEncodersAuto(rDrive);
                checkFinal = true;
                driveInspector.resetSystem();

                lockMachine = false;
                stateMachineIndex++;
            }
        } else {
            if (getEncoderValue(lDrive) <= leftEncoderTarget + 15) {
                leftDriveDone = true;
            }

            if (getEncoderValue(rDrive) <= rightEncoderTarget + 15) {
                rightDriveDone = true;
            }

            if (leftDriveDone && rightDriveDone) {
                leftDriveDone = false;
                rightDriveDone = false;

                resetEncodersAuto(lDrive);
                resetEncodersAuto(rDrive);
                checkFinal = true;
                driveInspector.resetSystem();

                lockMachine = false;
                stateMachineIndex++;
            }
        }

        return checkFinal;
    }

    public void setTelemetry() {
        telemetry.addData("State", currentMachineState + " (index: " + actionIndex + ")");
        telemetry.addData("Accelerometer", "X: " + x + " Y: " + y + " Z: " + Math.round(z));
        if (needsDrive) {
            telemetry.addData("Left Drive Position", getEncoderValue(lDrive));
            telemetry.addData("right Drive Position", getEncoderValue(rDrive));

            telemetry.addData("left Drive Target", leftEncoderTarget);
            telemetry.addData("right Drive Target", rightEncoderTarget);
        }

        if (needsDumper) {
            telemetry.addData("theDumper Position", theDumperPosition);
        }
        if (needsArm) {
            telemetry.addData("Arm Position", getEncoderValue(arm));
        }
        if (needsArm) {
            telemetry.addData("[RAW] Arm Position", arm.getCurrentPosition());
        }

        if (needsWinch) {
            telemetry.addData("Winch Position", getEncoderValue(winch));
        }
        if (needsShurikens) {
            telemetry.addData("leftShuriken Position", leftShuriken.getPosition());
            telemetry.addData("rightShuriken Position", rightShuriken.getPosition());
        }

    }

    public void setupAutonomous() {
    }

    public void puts(String string) {
        System.out.println(string);
    }

    public void scream() {
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 1000);
    }

    public void relieved() {
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_A, 250);
    }

    public void sensorAccelerometer(SensorEvent event) {
        oldX = x;
        oldY = y;
        oldZ = z;

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if (accelerometerTicks >= 2) {
            deadX = x;
            deadY = y;
            deadZ = z;
        }

        accelerometerTicks++;
    }

    // Setup autonomous
    @Override
    public void init() {
        autonomousInit();
    }

    // Get everything ready for loop
    @Override
    public void init_loop() {
        autonomousInitLoop();
    }

    // Run autonomous
    @Override
    public void loop() {
        autonomousloop();
    }

    // What to do when the robot is stopped from the driver station (or time runs out)
    @Override
    public void stop() {
        instance.getSensorAccelerometer().unregisterActiveBrain();
    }

    public void autonomousInit() {
        // Get instance of class FtcRobotControllerActivity
        instance = FtcRobotControllerActivity.getInstance();
        // Register listener for accelerometer updates
        instance.getSensorAccelerometer().registerActiveBrain(this);
        // Get access to the phones vibrator
        vibrator = (Vibrator) instance.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Handle collisions
        collisionHandler = new CollisionHandler(this);
        // Construct autonomous modes
        builder = new AutonomousConstruction(this);
        // Drive fault detection
        driveInspector = new DriveInspector(this);

        currentMachineState = "In-Active";
        lockMachine = false;
        stateMachineIndex = 0;
        actionIndex = 0;
        debugArrayIndex = 0;
        debugArray = new int[100];
        actionArray = new double[100][5];

        setupAutonomous(); // Add states to debugArray before setting stateMachineArray
        actionIndex = 0; // Reset to 0 after setup

        // Variable initialization of Motors and servos as needs by the "Minds"
        if (needsDrive) {
            lDrive = getMotor("lDrive");
            rDrive = getMotor("rDrive");
            lDrive.setDirection(DcMotor.Direction.REVERSE);
            rDrive.setDirection(DcMotor.Direction.FORWARD);

            setEncoderTarget(0, 0);
            setDrivePower(0.0, 0.0);

            resetEncodersAuto(lDrive);
            resetEncodersAuto(rDrive);
        }

        if (needsDumper) {
            theDumper = getServo("theDumper");
        }

        if (needsArm) {
            arm = getMotor("arm");
            arm.setDirection(DcMotor.Direction.FORWARD);
            resetEncodersAuto(arm);
        }

        if (needsWinch) {
            winch = getMotor("armExtender");
            winch.setDirection(DcMotor.Direction.FORWARD);
        }

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }

        if (needsShurikens) {
            leftShuriken = getServo("leftShuriken");
            rightShuriken = getServo("rightShuriken");

            rightShuriken.setDirection(Servo.Direction.REVERSE);
        }

        if (needsSensor) {

            usSensor = hardwareMap.ultrasonicSensor.get("usSensor");
        }
    }

    public void autonomousInitLoop() {
        if (needsDrive) {
            resetEncodersAuto(lDrive);
            resetEncodersAuto(rDrive);
        }

        if (needsDumper) {
            theDumperTick = 0;
            theDumperPosition = Servo.MIN_POSITION;
            theDumper.setPosition(theDumperPosition);
        }

        if (needsShurikens) {
            leftShuriken.setPosition(Servo.MIN_POSITION);
            rightShuriken.setPosition(Servo.MIN_POSITION);
        }

        if (needsArm) {
            resetEncodersAuto(arm);
        }
        if (needsWinch) {
            resetEncodersAuto(winch);
        }
    }

    public void autonomousloop() {
        setTelemetry();
        /* Collision Detection is off due to lack of testing.*/
//        if (!machineCompleted && needsDrive) { collisionHandler.checkCollision(); }
        if (needsDrive) {
            driveInspector.ensureDriveMoving();
        }

        switch (stateMachineArray[stateMachineIndex]) {
            // BEGIN - CONCEPT 14
            case DRIVE_FORWARD:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Drive Forward";

                    setEncoderTarget((int) actionArray[actionIndex][0], (int) actionArray[actionIndex][1]);
                    setDrivePower(actionArray[actionIndex][2], actionArray[actionIndex][3]);
                }

                if (positiveDriveCheck()) {
                    actionIndex++;
                }
                break;

            case DRIVE_BACKWARD:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Drive Backward";

                    setEncoderTarget((int) actionArray[actionIndex][0], (int) actionArray[actionIndex][1]);
                    setDrivePower(actionArray[actionIndex][2], actionArray[actionIndex][3]);
                }

                if (negativeDriveCheck()) {
                    actionIndex++;
                }
                break;

            case ARM_ACTION:

                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Arm Action";
                    armSpeed = actionArray[actionIndex][1];
                    armLocation = (getEncoderValue(arm) + (int) actionArray[actionIndex][0]);
                    int block = (int) actionArray[actionIndex][2];
                    arm.setTargetPosition(armLocation);
                    arm.setPower(armSpeed);

                    if (block == 0) {
                        lockMachine = false;
                        stateMachineIndex++;
                        actionIndex++;

                        resetEncodersAuto(arm);

                    }

                } else {
                    puts("[ABS] ARM Encoder: " + Math.abs(getEncoderValue(arm)) + " [ABS] Location: " + Math.abs(armLocation));
                    puts("[RAW] ARM Encoder: " + getEncoderValue(arm) + " [RAW] Location: " + armLocation);

                    if ((actionArray[actionIndex][0] > 0) && (Math.abs(getEncoderValue(arm)) >= Math.abs(armLocation - 15)) ||
                            (actionArray[actionIndex][0] < 0) && (getEncoderValue(arm) <= armLocation + 15)) {

                        armSpeed = 0.0;
                        arm.setPower(armSpeed);
                    }

                    if (Math.abs(armSpeed) <= 0.01) {
                        lockMachine = false;
                        stateMachineIndex++;
                        actionIndex++;

                        resetEncodersAuto(arm);
                    }
                }

                break;

            case DRIVE_ARM_ACTION:
                break;

            case WINCH_ACTION:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Winch Action";
                    winchTarget = getEncoderValue(winch) + (int) actionArray[actionIndex][0];
                    winchSpeed = actionArray[actionIndex][1];
                    int block = (int) actionArray[actionIndex][2];
                    puts("Winch TARGET: " + winchTarget + " SPEED: " + winchSpeed);
                    winch.setTargetPosition(winchTarget);
                    winch.setPower(winchSpeed);

                    if (block == 0) {
                        lockMachine = false;
                        stateMachineIndex++;
                        actionIndex++;

                        resetEncodersAuto(winch);
                    }

                } else {
                    puts("WINCH Encoder: " + getEncoderValue(winch) + " Location: " + winchTarget);

                    if ((actionArray[actionIndex][0] > 0) && (Math.abs(getEncoderValue(winch)) >= Math.abs(winchTarget - 15)) ||
                            (actionArray[actionIndex][0] < 0) && (getEncoderValue(winch) <= winchTarget + 15)) {

                        winchSpeed = 0.0;
                        winch.setPower(winchSpeed);
                    }

                    if (Math.abs(winchSpeed) <= 0.01) {
                        lockMachine = false;
                        stateMachineIndex++;
                        actionIndex++;

                        resetEncodersAuto(winch);
                    }
                }

                break;

            case DRIVE_WINCH_ACTION:
                break;

            case THE_DUMPER:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "TheDumper";

                    theDumperPosition = actionArray[actionIndex][0];
                    System.out.println("DumperPosition: " + theDumperPosition);
                    theDumper.setPosition(theDumperPosition);

                    if (collisionDetected) {
                        scream();
                    } // TODO: Maybe do not allow use of dumper if collision detected to prevent putting the climbers out of field
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case THE_DUMPER_SLOW:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "TheDumperSlow";
                } else {
                    theDumperPosition = Range.clip(theDumperPosition + 0.02, 0.0, 1.0);
                    System.out.println("DumperPosition: " + theDumperPosition);
                    // theDumper.getPosition()+0.03
                    theDumper.setPosition(theDumperPosition);

                    if (collisionDetected) {
                        scream();
                    } // TODO: Maybe do not allow use of dumper if collision detected to prevent putting the climbers out of field

                    if (theDumper.getPosition() >= actionArray[actionIndex][0] - 0.05) {
                        lockMachine = false;
                        stateMachineIndex++;
                        actionIndex++;
                    }
                }

                break;

            case LEFT_SHURIKEN:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "leftShuriken";

                    double position = actionArray[actionIndex][0];
                    leftShuriken.setPosition(position);
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case RIGHT_SHURIKEN:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "rightShuriken";

                    double position = actionArray[actionIndex][0];
                    rightShuriken.setPosition(position);
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case SET_COLLISION_PROFILE:
                if (!lockMachine) {
                    lockMachine = true;
                    int profile = (int) actionArray[actionIndex][0];
                    System.out.println("CollisionProfile: " + profile);
                    currentMachineState = "Updating Collision Profile";
                    collisionProfile = profile;
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case VIBRATOR_ACTION:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Vibrator";

                    int duration = (int) actionArray[actionIndex][0];
                    vibrator.vibrate(duration);
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case SCREAM_ACTION:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Scream";

                    scream();
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case RELIEVED_ACTION:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Relieved";

                    relieved();
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case ENCODER_RESET_ACTION:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Resetting ALL encoders, one moment...";

                    if (needsDrive) {
                        lDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                        rDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                    }
                    if (needsWinch) {
                        winch.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                    }
                    if (needsArm) {
                        arm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                    }
                } else {
                    if (needsDrive) {
                        lDrive.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                        rDrive.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                    }
                    if (needsWinch) {
                        winch.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                    }
                    if (needsArm) {
                        arm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                    }

                    lockMachine = false;
                    stateMachineIndex++;
                    actionIndex++;
                }
                break;

            case STATE_WAIT:
                if (!lockMachine) {
                    lockMachine = true;
                    targetWaitTicks = (int) actionArray[actionIndex][0];
                }

                if (currentWaitTicks >= targetWaitTicks) {
                    currentWaitTicks = 0;
                    lockMachine = false;
                    stateMachineIndex++;
                    actionIndex++;
                } else {
                    currentMachineState = "Waiting... " + currentWaitTicks + "/" + targetWaitTicks;
                }

                currentWaitTicks++;
                break;
            // END - CONCEPT 14

            case STATE_STOP:
                if (collisionID == 0) {
                    // Cheer, we didn't hit anything!
                }
                if (needsArm) {
                    arm.setPower(0.0);
                }
                if (needsWinch) {
                    winch.setPower(0.0);
                }
                if (needsDrive) {
                    lDrive.setPower(0.0);
                    rDrive.setPower(0.0);
                }
                if (needsDumper) {
                    theDumper.setPosition(Servo.MIN_POSITION);
                }
                lockMachine = true;
                machineCompleted = true;

                currentMachineState = "STOP";
                break;
            case STATE_PING:

                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "STATE_PING";

                    setEncoderTarget((int) actionArray[actionIndex][0], (int) actionArray[actionIndex][1]);
                        setDrivePower(actionArray[actionIndex][2], actionArray[actionIndex][3]);

                    if (sonic.getPings(3, usSensor) <= 3) {

                        setDrivePower(actionArray[actionIndex][0], actionArray[actionIndex][0]);

                    }
                }
                if (positiveDriveCheck()) {
                    actionIndex++;
                }

                break;


            default:
                currentMachineState = "!NO ACTIVE STATE!";
                break;
        }
    }


}
