package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import android.content.Context;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;

/**
 * Created by goldfishpi on 12/12/15.
 */
public class AutonomousVariables extends OpMode {

    public DriverOp driverOp = new DriverOp();

    public String currentMachineState;

    public DcMotor lDrive;
    public DcMotor rDrive;

    public DcMotor lFinger;
    public DcMotor rFinger;

    public DcMotor lGill;
    public DcMotor rGill;

    public Servo theDumper;

    public DcMotor armExtender;

    public DcMotor arm;

    public Vibrator vibrator;
    final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    public double armSpeed;
    public int armLocation;
    public int leftEncoderTarget;
    public int rightEncoderTarget;
    public double lDrivePower;
    public double rDrivePower;
    public double theDumperPosition;
    public int theDumperTick;

    public double wheelBase = 15.75;
    public double wheelCircumfrance;
    public double dInsideWheelDistance;
    public double dOutsideWheelDistance;
    public double[] adRotatingRobotDrive = new double[5];

    public int stateMachineIndex = 0;
    public int debugArrayIndex   = 0;
    public int actionIndex       = 0;
    public double[][] actionArray;
    public int[] stateMachineArray = new int[100];
    public int[] debugArray;

    public boolean lockMachine = false;
    public int currentWaitTicks = 0;
    public int targetWaitTicks  = 10;

    public FtcRobotControllerActivity instance;

    String currentState;

    public final int
            STATE_TURN_45_RIGHT                 = 0,
            STATE_STOP                          = 1,
            STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL = 2,
            STATE_TURN_45_LEFT                  = 3,
            STATE_RAISE_ARM                     = 4,
            STATE_LOWER_ARM                     = 5,
            STATE_EXTEND_ARM                    = 6,
            STATE_RETRACT_ARM                   = 7,
            STATE_STRAIGHT_PARK                 = 8,
            STATE_STRAIGHT_POSITION             = 9,
            STATE_DUMP_GUYS                     = 10,
            STATE_UNDUMP_GUYS                   = 11,
            STATE_STRAIGHT_REPOSITION           = 12,
            STATE_REVERSE_90_DEGREE_LEFT        = 13,
            STATE_STRAIGHT_TO_SIDE              = 14,
            STATE_STRAIGHT_TO_MOUNTAIN          = 15,
            STATE_REVERSE_90_DEGREE_RIGHT       = 16,
            STATE_STRAIGHT_TO_FAR               = 17,
            STATE_STRAIGHT_TO_CORNER            = 18,
            STATE_REVERSE_135_DEGREE_RIGHT      = 19,
            STATE_REVERSE_135_DEGREE_LEFT       = 20,
            STATE_135_DEGREE_RIGHT              = 21,
            STATE_135_DEGREE_LEFT               = 22,
            STATE_TURN_90_DEGREE_RIGHT          = 23,
            STATE_TURN_90_DEGREE_LEFT           = 24,

            // States for testing new approach to autonomous construction
            FORWARD_TURN  = 25, // use for dual-motor turns, with +/- angle as only action input
            REVERSE_TURN  = 26,
            DRIVE_FORWARD = 27,
            DRIVE_BACKWARD= 28,
            STATE_WAIT    = 29,
            THE_DUMPER    = 30,
            ARM_ACTION    = 31,

            SET_COLLISION_PROFILE      = 32,
            COLLISION_IGNORE           = 33,
            COLLISION_CHANGE_DIRECTION = 34,
            VIBRATOR_ACTION            = 35;


    public int
            stateWait,
            stateDriveStraightConerToGoal = 0,
            stateStop = 0,
            stateTurn45Left = 0,
            stateRaiseArm = 0,
            stateExtnedArm = 0,
            stateRetractArm = 0;

    public int collisionProfile   = COLLISION_IGNORE,
               COLLISION_THRESHOLD= 12,
               tickSinceCollision = 0,
               accelerometerTicks = 0;
    public boolean collisionLock  = false;

    public float deadX,
                 deadY,
                 deadZ,
                 oldX,
                 oldY,
                 oldZ,
                 x,
                 y,
                 z;


    public DcMotor getMotor(String string) {
        return hardwareMap.dcMotor.get(string);
    }
    public Servo getServo(String string) { return hardwareMap.servo.get(string); }

    public int getEncoderValue(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    public double getEncoderValue(Servo servo) {
        return servo.getPosition();
    }

    void setEncoderTarget(int leftEncoder, int rightEncoder) {
        leftEncoderTarget  = getEncoderValue(lDrive) + leftEncoder;
        rightEncoderTarget = getEncoderValue(rDrive) + rightEncoder;

        lDrive.setTargetPosition(leftEncoderTarget);
        rDrive.setTargetPosition(rightEncoderTarget);
    }

    void setDrivePower(double leftPower, double rightPower) {
        lDrivePower = Range.clip(leftPower, -1.0, 1.0);
        rDrivePower = Range.clip(rightPower, -1.0, 1.0);
        lDrive.setPower(lDrivePower);
        rDrive.setPower(rDrivePower);
    }

    public void resetEncodersAuto(DcMotor motor){
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    // Clean up state machine (a lot, hopefully)

    // Checks that the wheels are where they should be, tells the state machine to proceed
    public boolean positiveDriveCheck() {
        boolean checkFinal = false;

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

            stateWait = 0;
            lockMachine = false;
            stateMachineIndex++;
        }

        return checkFinal;
    }

    // Checks that the wheels are where they should be, tells the state machine to proceed
    public boolean negativeDriveCheck() {
        boolean checkFinal = false;
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

            stateWait = 0;
            lockMachine = false;
            stateMachineIndex++;
        }

        return checkFinal;
    }

    public void setTelemetry() {
        telemetry.addData("Accelerometer", "X: "+x+" Y: "+y+" Z: "+Math.round(z));
        telemetry.addData("State", currentMachineState);
//        telemetry.addData("Left Position", getEncoderValue(lDrive));
//        telemetry.addData("right Position", getEncoderValue(rDrive));

//        telemetry.addData("left Target", leftEncoderTarget);
//        telemetry.addData("right Target", rightEncoderTarget);

//        telemetry.addData("theDumper Position", theDumperPosition);
//        telemetry.addData("Arm Position", getEncoderValue(arm));
    }

    public void setupAutonomous() {
    }

    public void addState(int state) {
        debugArray[debugArrayIndex] = state;
        debugArrayIndex++;
    }

    public void addDriveAction(int state, int leftDrive, int rightDrive, double leftPower, double rightPower) {
        debugArray[debugArrayIndex] = state;
        debugArrayIndex++;

        double[] array = {leftDrive, rightDrive, leftPower, rightPower};
        actionArray[actionIndex] = array;
        actionIndex++;
    }

    public void addServoAction(int state, double position) {
        debugArray[debugArrayIndex] = state;
        debugArrayIndex++;

        double[] array = new double[1];
        array[0] = position;
        actionArray[actionIndex] = array;
        actionIndex++;
    }

    public void addWaitAction(double ticks) {
        debugArray[debugArrayIndex] = STATE_WAIT;
        debugArrayIndex++;

        actionArray[actionIndex][0] = ticks;
        actionIndex++;
    }

    public void addArmAction(int state, int armPosition, double armPower) {
        debugArray[debugArrayIndex] = state;
        debugArrayIndex++;

        double[] array = {armPosition, armPower};
        actionArray[actionIndex] = array;
        actionIndex++;
    }

    public void addVibratorAction(int duration) {
        debugArray[debugArrayIndex] = VIBRATOR_ACTION;
        debugArrayIndex++;

        double[] array = {duration};
        actionArray[actionIndex] = array;
        actionIndex++;
    }

    public void setCollisionProfile(int profile) {
        debugArray[debugArrayIndex] = SET_COLLISION_PROFILE;
        debugArrayIndex++;

        double[] array = {profile};
        actionArray[actionIndex] = array;
        actionIndex++;
    }

    public void scream() {
//        toneGenerator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL, 250);
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

    public void checkCollision() { // FIXME: Reduntant collision check method, will trigger again on recovery.
        if (collisionLock) {
            tickSinceCollision++;
            if (tickSinceCollision >= 45) {
                tickSinceCollision = 0;
                collisionLock = false;
            }
        }

        if (accelerometerTicks >= 4 && !collisionLock) {
            System.out.println("x: " + Math.abs(x) + " y: " + Math.abs(y) + " z: " + Math.abs(z));

            if (Math.abs(x) >= COLLISION_THRESHOLD && !collisionLock) {
                scream();
                collisionLock = true;
            }

            if (Math.abs(y) >= COLLISION_THRESHOLD && !collisionLock) {
                scream();
                collisionLock = true;
            }
        }
    }

    @Override
    public void init() {
        autonomousInit();
    }

    @Override
    public void init_loop(){
        autonomousInitLoop();
    }

    @Override
    public void loop() {
        autonomousloop();
    }

    @Override
    public void stop() {
        instance.getSensorAccelerometer().unregisterActiveBrain();
    }

    public void autonomousInit() {
        instance = FtcRobotControllerActivity.getInstance();
        instance.getSensorAccelerometer().registerActiveBrain(this);
        vibrator = (Vibrator) instance.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

//        lDrive = getMotor("lDrive");
//        rDrive = getMotor("rDrive");
//        lDrive.setDirection(DcMotor.Direction.REVERSE);
//        rDrive.setDirection(DcMotor.Direction.FORWARD);

//        theDumper = getServo("theDumper");
//        arm       = getMotor("arm");

        stateWait = 0;
        currentMachineState = "In-Active";
        lockMachine = false;
        stateMachineIndex = 0;
        actionIndex       = 0;
        debugArrayIndex   = 0;
        debugArray = new int[100];
        actionArray= new double[100][4];

        setupAutonomous(); // Add states to debugArray before setting stateMachineArray
        actionIndex = 0; // Reset to 0 after setup

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }
//        FtcRobotControllerActivity.getSensorAccelermeter().registerActiveBrain(this);

//        setEncoderTarget(0, 0);
//        setDrivePower(0.0, 0.0);

//        resetEncodersAuto(lDrive);
//        resetEncodersAuto(rDrive);

//        resetEncodersAuto(arm);
    }

    public void autonomousInitLoop() {
//        resetEncodersAuto(lDrive);
//        resetEncodersAuto(rDrive);
//        resetEncodersAuto(arm);
        theDumperTick = 0;
//        theDumperPosition = Servo.MIN_POSITION;
//        theDumper.setPosition(theDumperPosition);
    }

    public void autonomousloop() {
        setTelemetry();
        checkCollision();

        switch (stateMachineArray[stateMachineIndex]) {
            // BEGIN - CONCEPT 14
            case DRIVE_FORWARD:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "Drive Forward";
                    System.out.println("LeftTarget: " + (int) actionArray[actionIndex][0] + " RightTarget: " + (int) actionArray[actionIndex][1]);
                    System.out.println("LeftPower: " + actionArray[actionIndex][2] + " RightPower: " + actionArray[actionIndex][3]);

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
                    System.out.println("LeftTarget: " + (int) actionArray[actionIndex][0] + " RightTarget: " + (int) actionArray[actionIndex][1]);
                    System.out.println("LeftPower: " + actionArray[actionIndex][2] + " RightPower: " + actionArray[actionIndex][3]);

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
                    resetEncodersAuto(arm);
                    currentMachineState = "Arm Action";
                    armSpeed = actionArray[actionIndex][1];
                    armLocation = getEncoderValue(arm);
                    arm.setTargetPosition(armLocation - (int) actionArray[actionIndex][0]);
                    arm.setPower(armSpeed);
                }

                if (armSpeed <= 0.0) {
                    if (getEncoderValue(arm) <= getEncoderValue(arm) + 15) {
                        lockMachine = false;
                        arm.setPower(0.0);
                        stateMachineIndex++;
                        actionIndex++;
                    }
                }

                if (armSpeed >= 0.0) {
                    if (getEncoderValue(arm) >= getEncoderValue(arm) - 15) {
                        lockMachine = false;
                        arm.setPower(0.0);
                        stateMachineIndex++;
                        actionIndex++;
                    }
                }

                break;

            case THE_DUMPER:
                if (!lockMachine) {
                    lockMachine = true;
                    currentMachineState = "TheDumper";

                    theDumperPosition = actionArray[actionIndex][0];
                    System.out.println("DumperPosition: "+theDumperPosition);
                    theDumper.setPosition(theDumperPosition);
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case SET_COLLISION_PROFILE:
                if (!lockMachine) {
                    lockMachine = true;

                    int profile = (int)actionArray[actionIndex][0];
                    System.out.println("CollisionProfile: "+profile);
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

                    int duration = (int)actionArray[actionIndex][0];
                    vibrator.vibrate(duration);
                }

                lockMachine = false;
                stateMachineIndex++;
                actionIndex++;
                break;

            case STATE_WAIT:
                if (!lockMachine) {
                    lockMachine = true;
                    targetWaitTicks = (int)actionArray[actionIndex][0];
                }

                if (currentWaitTicks >= targetWaitTicks) {
                    currentWaitTicks = 0;
                    lockMachine = false;
                    stateMachineIndex++;
                    actionIndex++;
                }
                else { currentMachineState = "Waiting... " + currentWaitTicks + "/" + targetWaitTicks; }

                currentWaitTicks++;
                break;
            // END - CONCEPT 14

            case STATE_TURN_45_LEFT:
                if (stateWait == 0) {

                    currentMachineState = "Turn 45 Left";
                    stateWait = 1;

                    setEncoderTarget(0, 2340);                                    // Drive distance
                    setDrivePower(0.0, 1.0);
                }

                positiveDriveCheck();

                break;

            case STATE_TURN_45_RIGHT:
                if (stateWait == 0) {
                    currentMachineState = "Turn 45 Right";


                    stateWait = 1;

                    setEncoderTarget(2340, 0);
                    setDrivePower(1.0, 0.0);
                }

                positiveDriveCheck();

                break;

            case STATE_STRAIGHT_PARK:
                if (stateWait == 0) {

                    currentMachineState = "Straight park";
                    stateWait = 1;

                    setEncoderTarget(3500, 3500);
                    setDrivePower(1.0, 1.0);
                }

                positiveDriveCheck();
                break;

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "Drive Straight";

                    setEncoderTarget(18768, 18768);
                    setDrivePower(1.0, 1.0);
                }

                positiveDriveCheck();
                break;

            case STATE_STRAIGHT_POSITION:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "Straight Position";

                    setEncoderTarget(475, 475);
                    setDrivePower(0.3, 0.3);
                }

                positiveDriveCheck();
                break;

            case STATE_RAISE_ARM:

                if (stateWait == 0) {
                    stateWait = 1;
                    resetEncodersAuto(arm);
                    currentMachineState = "Raise arm";
                    armSpeed = 0.3;
                    armLocation = getEncoderValue(arm);
                    arm.setTargetPosition(armLocation + 300);
                    arm.setPower(armSpeed);
                }

                if (getEncoderValue(arm) >= getEncoderValue(arm) - 15) {
                    stateWait = 0;
                    stateMachineIndex++;
                }

                break;

            case STATE_DUMP_GUYS:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "Dump Guys";
                    theDumperPosition = Servo.MAX_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;


            case STATE_UNDUMP_GUYS:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "(R) Undump Guys";
                    theDumperPosition = Servo.MIN_POSITION;
                    theDumper.setPosition(theDumperPosition);
                }

                theDumperTick += 1;

                if (theDumperTick >= 120) {
                    theDumperTick = 0;
                    stateWait = 0;

                    stateMachineIndex++;
                }
                break;

            case STATE_LOWER_ARM:

                if (stateWait == 0) {
                    stateWait = 1;
                    resetEncodersAuto(arm);
                    currentMachineState = "Lower arm";
                    armSpeed = -0.3;
                    armLocation = getEncoderValue(arm);
                    arm.setTargetPosition(armLocation - 200);
                    arm.setPower(armSpeed);
                }

                if (getEncoderValue(arm) <= getEncoderValue(arm) + 15) {
                    stateWait = 0;
                    arm.setPower(0.0);
                    stateMachineIndex++;
                }

                break;

            case STATE_STRAIGHT_REPOSITION:
                if (stateWait == 0) {

                    stateWait = 1;

                    setEncoderTarget(-475, -475);
                    setDrivePower(-0.3, -0.3);

                    currentMachineState = "Straight Reposition";
                }

                negativeDriveCheck();
                break;

            case STATE_REVERSE_90_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(-4480, 0);
                    setDrivePower(-1.0, 0.0);

                    currentMachineState = "Reverse 90 Degree Right";
                }

                negativeDriveCheck();
                break;

            case STATE_REVERSE_90_DEGREE_LEFT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(0, -4480);
                    setDrivePower(0.0, -1.0);

                    currentMachineState = "Reverse 90 Degree Left";
                }

                negativeDriveCheck();
                break;

            case STATE_REVERSE_135_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(-6720, 0);
                    setDrivePower(-1.0, 0.0);

                    currentMachineState = "Reverse 135 Degree Right";
                }

                negativeDriveCheck(); // MAY CAUSE BUG?
                break;

            case STATE_REVERSE_135_DEGREE_LEFT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(0, -6720);
                    setDrivePower(0.0, -1.0);

                    currentMachineState = "Reverse 135 Degree Left";
                }

                negativeDriveCheck();
                break;

            case STATE_TURN_90_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(4480, 0);
                    setDrivePower(1.0, 0.0);

                    currentMachineState = "90 Degree Right";
                }

                positiveDriveCheck(); // MAY CAUSE BUG?
                break;

            case STATE_TURN_90_DEGREE_LEFT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(0, 4480);
                    setDrivePower(0.0, 1.0);

                    currentMachineState = "90 Degree Left";
                }

                positiveDriveCheck();
                break;

            case STATE_135_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(6720, 0);
                    setDrivePower(1.0, 0.0);

                    currentMachineState = "135 Degree Right";
                }

                positiveDriveCheck(); // MAY CAUSE BUG?
                break;

            case STATE_135_DEGREE_LEFT:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(0, 6720);
                    setDrivePower(0.0, 1.0);

                    currentMachineState = "135 Degree Left";
                }

                positiveDriveCheck();
                break;

            case STATE_STRAIGHT_TO_SIDE:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(3400, 3400);
                    setDrivePower(1.0, 1.0);

                    currentMachineState = "Straight to Side";
                }

                positiveDriveCheck();
                break;

            case STATE_STRAIGHT_TO_MOUNTAIN:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(9120, 9120);
                    setDrivePower(1.0, 1.0);

                    currentMachineState = "Straight to Mountain";
                }

                positiveDriveCheck();
                break;

            case STATE_STRAIGHT_TO_FAR:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(8000, 8000);
                    setDrivePower(1.0, 1.0);

                    currentMachineState = "Straight to Far";
                }

                positiveDriveCheck();
                break;

            case STATE_STRAIGHT_TO_CORNER:

                if (stateWait == 0) {
                    stateWait = 1;

                    setEncoderTarget(5200, 5200);
                    setDrivePower(1.0, 1.0);

                    currentMachineState = "Straight to Corner";
                }

                positiveDriveCheck();
                break;

            case STATE_STOP:

                currentMachineState = "STOP";
                break;

            default:
                currentMachineState = "!NO ACTIVE STATE!";
                break;
        }
    }
}
