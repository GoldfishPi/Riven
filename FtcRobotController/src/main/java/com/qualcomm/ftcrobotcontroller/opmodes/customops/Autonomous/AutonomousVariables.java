package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Array;

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

    @Override
    public void init() {
        autonomousInit();
    }

    public void setTelemetry() {
        telemetry.addData("State", currentMachineState);
        telemetry.addData("Left position", getEncoderValue(lDrive));
        telemetry.addData("right position", getEncoderValue(rDrive));

        telemetry.addData("left target", leftEncoderTarget);
        telemetry.addData("right target", rightEncoderTarget);

        telemetry.addData("theDumper position", theDumperPosition);
        telemetry.addData("Arm", getEncoderValue(arm));

//        System.out.println("left pos: " + getEncoderValue(lDrive));
//        System.out.println("Right pos: " + getEncoderValue(rDrive));
//        System.out.println();
//        System.out.println("left  target: " + rightEncoderTarget);
//        System.out.println("Right target: " + rightEncoderTarget);
//        System.out.println();


    }

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
    //-------------------------------------//
    //functions here...                    //
    //-------------------------------------//

    void setEncoderTarget(int leftEncoder, int rightEncoder) {
        lDrive.setTargetPosition(leftEncoderTarget = leftEncoder);
        rDrive.setTargetPosition(rightEncoderTarget = rightEncoder);
    }

    void setDrivePower(double leftPower, double rightPower) {
        lDrive.setPower(Range.clip(leftPower, -1, 1));
        rDrive.setPower(Range.clip(rightPower, -1, 1));
    }

    void setDriveSpeed(double leftSpeed, double rightSpeed) {
        setDrivePower(leftSpeed, rightSpeed);
    }

    public void useConstantPower() {
        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    public void setDriveMode(DcMotorController.RunMode mode) {
        if (lDrive.getChannelMode() != mode) {
            lDrive.setChannelMode(mode);
        }

        if (rDrive.getChannelMode() != mode) {
            rDrive.setChannelMode(mode);
        }
    }


    boolean moveComplete() {
        return ((Math.abs(getEncoderValue(lDrive) - leftEncoderTarget) < 10) &&
                (Math.abs(getEncoderValue(rDrive) - rightEncoderTarget) < 10));
    }

    public void driveTurn45Left(int left, int right) {

        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
        setEncoderTarget(left, right);
        setDriveSpeed(0.5, 5.0);
    }

    private int count = 0;

    public void extendArm(){
        count = 0;
        while (count < 20){
            driverOp.extendEqual("out", 0.5);
            count++;
        }
    }

    boolean extendFinished(){
        return count >= 20;
    }
    private int countIn = 0;

    public void retractArm(){
        countIn = 0;
        while(countIn < 20) {
            driverOp.extendEqual("in", -0.5);
            countIn ++;
        }
    }

    boolean retractDone(){
        return countIn >= 20;
    }

    public void resetEncodersAuto(DcMotor motor){
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void turnRobotCalculation( double dRadius, double dDegreeTurn ){

        dOutsideWheelDistance = dDegreeTurn * Math.PI * dRadius / 180;
        dInsideWheelDistance = dDegreeTurn * Math.PI * (dRadius + wheelBase);

        wheelCircumfrance = 4 * Math.PI;

        adRotatingRobotDrive[0] = 3.0 * dDegreeTurn * dRadius * (1.0); // dInsideWheelDistance * (80 / 40 * wheelCircumfrance) * 280;

        adRotatingRobotDrive[1] = 3.0 * dDegreeTurn * (dRadius + wheelBase) * (1.0); // dOutsideWheelDistance * (80 / 40 * wheelCircumfrance) * 280;

        adRotatingRobotDrive[2] =   adRotatingRobotDrive[0] /  adRotatingRobotDrive[1];
    }

    public int straightRobotCalculation(Double dDistance){
        wheelCircumfrance = 4 * Math.PI;
        return (int) (dDistance * (80 / 40) * 280);
    }

    public void setupAutonomous() {
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

        setupAutonomous();

        for (int i = 0; i < 100; i++) {
            stateMachineArray[i] = debugArray[i];
        }

        leftEncoderTarget = getEncoderValue(lDrive);
        rightEncoderTarget= getEncoderValue(rDrive);

        lDrivePower = 0.0;
        rDrivePower = 0.0;

        lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
        rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));

        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);

        resetEncodersAuto(arm);

//        stateMachineIndex = 1;
    }

    public void autonomousInitLoop() {
        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
        resetEncodersAuto(arm);
        theDumperTick = 0;
        theDumperPosition = Servo.MIN_POSITION;
    }

    public void autonomousloop() {
        setTelemetry();
        theDumper.setPosition(theDumperPosition);

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_LEFT:
                if (stateWait == 0) {

                    currentMachineState = "Turn 45 Left";
                    stateWait = 1;

                    leftEncoderTarget = getEncoderValue(lDrive);                                    // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 2340;

                    lDrivePower = 0.0;
                    rDrivePower = 1.0;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
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

                break;

            case STATE_TURN_45_RIGHT:
                if (stateWait == 0) {
                    currentMachineState = "Turn 45 Right";


                    stateWait = 1;

                    leftEncoderTarget = getEncoderValue(lDrive) + 2340;                                    // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive);

                    lDrivePower = 1.0;
                    rDrivePower = 0.0;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
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

                break;

            case STATE_STRAIGHT_PARK:
                if (stateWait == 0) {
                    currentMachineState = "Straight park";


                    stateWait = 1;

                    leftEncoderTarget  = getEncoderValue(lDrive) + 3500;        // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 3500;        // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
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
                break;

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if (stateWait == 0) {

                    stateWait = 1;

                    currentMachineState = "Drive Straight";

                    leftEncoderTarget  = getEncoderValue(lDrive) + 18768;                                     // Drive distance
                    rightEncoderTarget = getEncoderValue(rDrive) + 18768;                                     // Drive distance

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);
                }

                if ((getEncoderValue(lDrive) >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((getEncoderValue(rDrive) >= rightEncoderTarget - 15)) {
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
                break;

            case STATE_STRAIGHT_POSITION:
                if (stateWait == 0) {

                    stateWait = 1;
                    currentMachineState = "Straight Position";

                    lDrivePower = 0.3;
                    rDrivePower = 0.3;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    leftEncoderTarget  = getEncoderValue(lDrive) + 475;
                    rightEncoderTarget = getEncoderValue(rDrive) + 475;

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);
                }

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
                    leftEncoderTarget  = getEncoderValue(lDrive) - 475;
                    rightEncoderTarget = getEncoderValue(rDrive) - 475;

                    lDrivePower = -0.3;
                    rDrivePower = -0.3;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Straight Reposition";
                }

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
                break;

            case STATE_REVERSE_90_DEGREE_RIGHT:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) - (2240 * 2);
                    rightEncoderTarget= getEncoderValue(rDrive);
                    lDrivePower = -1.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Reverse 90 Degree Right";
                }

                if (getEncoderValue(lDrive) <= leftEncoderTarget + 15) {
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;

                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    resetEncodersAuto(lDrive);
                    resetEncodersAuto(rDrive);

                    stateWait = 0;
                    stateMachineIndex++;
                }
                break;

            case STATE_STRAIGHT_TO_SIDE:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) + 3400;
                    rightEncoderTarget= getEncoderValue(rDrive) + 3400;

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Straight to Side";
                }

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
                break;

            case STATE_STRAIGHT_TO_MOUNTAIN:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) + 9120;
                    rightEncoderTarget= getEncoderValue(rDrive) + 9120;

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Straight to Mountain";
                }

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
                break;

            case STATE_STRAIGHT_TO_FAR:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) + 8000;
                    rightEncoderTarget= getEncoderValue(rDrive) + 8000;

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Straight to Far";
                }

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
                break;

            case STATE_STRAIGHT_TO_CORNER:

                if (stateWait == 0) {
                    stateWait = 1;
                    leftEncoderTarget = getEncoderValue(lDrive) + 5200;
                    rightEncoderTarget= getEncoderValue(rDrive) + 5200;

                    lDrivePower = 1.0;
                    rDrivePower = 1.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    currentMachineState = "Straight to Corner";
                }

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
                break;

            case STATE_STOP:

                currentMachineState = "STOP";
                break;

            default:
                currentMachineState = "!DEFAULT!";
                break;
        }
    }
}
