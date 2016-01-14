package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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
    public int[] stateMachineArray = new int[100];
    public int[] debugArray;

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
            STATE_STRAIGHT_TO_CORNER            = 18;


    public int
            stateWait,
            stateDriveStraightConerToGoal = 0,
            stateStop = 0,
            stateTurn45Left = 0,
            stateRaiseArm = 0,
            stateExtnedArm = 0,
            stateRetractArm = 0;

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
    public void positiveDriveCheck() {
        if (getEncoderValue(lDrive) >= leftEncoderTarget - 15) {
            lDrivePower = 0.0;
            lDrive.setPower(lDrivePower);
        }


        if (getEncoderValue(rDrive) >= rightEncoderTarget - 15) {
            rDrivePower = 0.0;
            rDrive.setPower(rDrivePower);
        }


        if ((lDrivePower <= 0.1) && (rDrivePower <= 0.1)) {
            lDrivePower = 0.0;
            rDrivePower = 0.0;
            lDrive.setPower(lDrivePower);
            rDrive.setPower(rDrivePower);

            resetEncodersAuto(lDrive);
            resetEncodersAuto(rDrive);

            stateWait = 0;
            stateMachineIndex++;
        }
    }

    // Checks that the wheels are where they should be, tells the state machine to proceed
    public void negativeDriveCheck() {
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

            stateWait = 0;
            stateMachineIndex++;
        }
    }

    public void setTelemetry() {
        telemetry.addData("State", currentMachineState);
        telemetry.addData("Left Position", getEncoderValue(lDrive));
        telemetry.addData("right Position", getEncoderValue(rDrive));

        telemetry.addData("left Target", leftEncoderTarget);
        telemetry.addData("right Target", rightEncoderTarget);

        telemetry.addData("theDumper Position", theDumperPosition);
        telemetry.addData("Arm Position", getEncoderValue(arm));


    }

    public void setupAutonomous() {
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
    }

    public void autonomousInit() {
        lDrive = getMotor("lDrive");
        rDrive = getMotor("rDrive");
        lDrive.setDirection(DcMotor.Direction.REVERSE);
        rDrive.setDirection(DcMotor.Direction.FORWARD);

        theDumper = getServo("theDumper");
        arm       = getMotor("arm");

        stateWait = 0;
        stateMachineIndex = 0;
        debugArray = new int[100];

        setupAutonomous(); // Add states to debugArray before setting stateMachineArray

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }

        setEncoderTarget(0, 0);
        setDrivePower(0.0, 0.0);

        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);

        resetEncodersAuto(arm);
    }

    public void autonomousInitLoop() {
        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
        resetEncodersAuto(arm);
        theDumperTick = 0;
        theDumperPosition = Servo.MIN_POSITION;
        theDumper.setPosition(theDumperPosition);
    }

    public void autonomousloop() {
        setTelemetry();

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_LEFT:
                if (stateWait == 0) {

                    currentMachineState = "Turn 45 Left";
                    stateWait = 1;

                    setEncoderTarget(0, 2340);                                    // Drive distance
                    setDrivePower(0.0, 1.0);
//                    rightEncoderTarget = getEncoderValue(rDrive) + 2340;
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

                negativeDriveCheck(); // MAY CAUSE BUG?
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
