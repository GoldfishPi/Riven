package com.qualcomm.ftcrobotcontroller.opmodes.customops.Atomonous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
* created by c5 and GoldfishPi
* Contact details: timecrafters8962@gmail.com
* */

/* This autonomous does the following steps
* 0) Wait For encoder to reset
* 1) Drive forward to middle
* 2) turn 90 degrees
* 3) Drive forward until the distance sensor reads 5
* 4) Raise Arm using encoder ticks
* 5) Extend Arm using encoder ticks
* 7) Drive straight back
* 8) Turn to become inline with the mountain
* 9) Drive to ramp
* 10) Raise arm to inline with ramp
* 11) lower arm slightly
* 12) pull robot up
* End) Stop all
 */

public class AtonomusOp extends OpMode {

    //Drive forward using encoders (leftMotor, rightMotor, Speed)
    //final PathSeg[] DriveForward = {
    //   new PathSeg(1.0, 1.0, 1.0),
    //};



    final double RANGE = 10;


    //-------------------------------
    // motor declearations
    //-------------------------------

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    //private DcMotor lFinger;
    //private DcMotor rFinger;

    private DcMotor lWinch;
    private DcMotor rWinch;

    private DcMotor lArm;
    private DcMotor rArm;
    private DcMotor arm;

    private DcMotor lCoffin;
    private DcMotor rCoffin;

    private int leftEncoderTarget;
    private int rightEncoderTarget;

    public LightSensor frontLightSensor;
    public LightSensor backLightSensor;

    double wheelBase = 15.75;
    double wheelCircumfrance;
    double dInsideWheelDistance;
    double dOutsideWheelDistance;
    double[] adRotatingRobotDrive = new double[3];


    public OpticalDistanceSensor distanceSonsor;
    public LightSensor lightSensor;

    public ElapsedTime runTime = new ElapsedTime();  // time into round

    public ElapsedTime stateTime = new ElapsedTime();  // time into state


    public AtonomusOp() {

    }



    @Override
    public void init() {

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        //lFinger = hardwareMap.dcMotor.get("lFinger");
        //rFinger = hardwareMap.dcMotor.get("rFinger");

        lWinch = hardwareMap.dcMotor.get("armOut");
        rWinch = hardwareMap.dcMotor.get("armIn");
        rWinch.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.dcMotor.get("arm");

        setDrivePower(0, 0);


    }

    @Override
    public void init_loop() {



    }

    public final int
            STATE_TURN_45_RIGHT = 0,
            STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL = 1,
            STATE_STOP = 2,
            STATE_TURN_45_LEFT = 3;

    public int
            stateTurn45Right = 0,
            stateDriveStraightConerToGoal = 0,
            stateStop = 0,
            stateTurn45Left = 0;





    public int stateMachineIndex = 0;
    public int[] stateMachineArray;
    public int[] debugArray;
    public int[] autoRightCorner;
    public int[] autoLeftCorner;


    @Override
    public void start() {

        debugArray = new int[2];
        debugArray[0] = STATE_TURN_45_RIGHT;
        debugArray[1] = STATE_STOP;

        for(int i=0; i<debugArray.length; i++){
            stateMachineArray[i]=debugArray[i];
        }



    }

    @Override
    public void loop() {
                /*STATE_INITIAL,
                STATE_DRIVE_TO_MIDDLE ,
                STATE_TURN,
                STATE_DRIVE_TO_WALL,
                STATE_RAISE_ARM,
                STATE_EXTEND_ARM,
                STATE_DRIVE_BACK,
                STATE_TURN_TO_RAMP,
                STATE_RAISE_ARM_MOUNTAIN,
                STATE_LOWER_ARM_SLIGHTLY,
                PULL_ROBOT,
                STATE_STOP,*/

        switch (stateMachineArray[0]) {
            case STATE_TURN_45_RIGHT:
                if (stateTurn45Right == 0){

                    stateTurn45Right = 1;
                    resetEncodersAuto(rightDrive);
                    resetEncodersAuto(leftDrive);
                    turnRobotCalculation(24, 45);// 24 inches, 45 degrees
                    driveTurn45Left((int) adRotatingRobotDrive[0], (int) adRotatingRobotDrive[1]);
                }
                else if (moveComplete() == true) {
                    stateMachineIndex ++;
                }



                break;
            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                resetEncodersAuto(rightDrive);
                resetEncodersAuto(leftDrive);

                   driveStraight(straightRobotCalculation((double)(12.5)));
                break;


            case STATE_STOP:
                break;
        }
    }

    @Override
    public void stop() {
        useConstantPower();
        setDrivePower(0.0, 0.0);
    }

    //-------------------------------------//
    //functions here...                    //
    //-------------------------------------//


    void setEncoderTarget(int leftEncoder, int rightEncoder) {

        leftDrive.setTargetPosition(leftEncoderTarget = leftEncoder);
        rightDrive.setTargetPosition(rightEncoderTarget = rightEncoder);
    }

    void addEncoderTarget(int leftEncoder, int rightEncoder) {

        leftDrive.setTargetPosition(leftEncoderTarget += leftEncoder);
        rightDrive.setTargetPosition(rightEncoderTarget += rightEncoder);
    }

    void setDrivePower(double leftPower, double rightPower) {
        leftDrive.setPower(Range.clip(leftPower, -1, 1));
        rightDrive.setPower(Range.clip(rightPower, -1, 1));
    }

    void setDriveSpeed(double leftSpeed, double rightSpeed) {

        setDrivePower(leftSpeed, rightSpeed);
    }

    void setCoffinPower(double leftCoffinPower, double rightCoffinPower) {

        lCoffin.setPower(Range.clip(leftCoffinPower, -1, 1));
        rCoffin.setPower(Range.clip(rightCoffinPower, -1, 1));
    }

    void setCoffinSpeed(double leftCoffinSpeed, double rightCoffinSpeed) {

        setCoffinPower(leftCoffinSpeed, rightCoffinSpeed);
    }


    void setArmPower(double powerArm) {
        arm.setPower(Range.clip(powerArm, -1, 1));

    }

    void setArmSpeed(double armSpeed) {
        setArmPower(armSpeed);
    }

    void setWinchPower(double leftWinchPower, double rightWinchPower) {

        lWinch.setPower(Range.clip(leftWinchPower, -1, 1));
        rWinch.setPower(Range.clip(rightWinchPower, -1, 1));
    }

    void setWinchSpeed(double leftWinchSpeed, double rightWinchSpeed) {

        setWinchPower(leftWinchSpeed, rightWinchSpeed);
    }

    public void runToPosition() {

        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void useConstantSpeed() {
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void useConstantPower() {

        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

    }
    void synchEncoders() {

        leftEncoderTarget = leftDrive.getCurrentPosition();
        rightEncoderTarget = rightDrive.getCurrentPosition();
    }

    public void setDriveMode(DcMotorController.RunMode mode) {
        if (leftDrive.getChannelMode() != mode)
            leftDrive.setChannelMode(mode);

        if (rightDrive.getChannelMode() != mode)
            rightDrive.setChannelMode(mode);
    }

    public void driveTurn45Left(int left, int right) {

        resetEncodersAuto(leftDrive);
        resetEncodersAuto(rightDrive);
        setEncoderTarget(left, right);
        setDriveSpeed(0.5, 5.0);
    }

    public void driveTurn45LeftCheck(int left, int right) {

        setEncoderTarget(left, right);
        setDriveSpeed(0.5, 5.0);
    }

    public void driveStraight(int encoderValue){
        resetEncodersAuto(leftDrive);
        resetEncodersAuto(rightDrive);
        setEncoderTarget(encoderValue, encoderValue);
        setDriveSpeed(0.5, 0.5);

    }
    public void resetEncodersAuto(DcMotor motor){
        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);



    }

    public void turnRobotCalculation( double dRadius, double dDegreeTurn ){

        dOutsideWheelDistance = dDegreeTurn * Math.PI * dRadius / 180;
        wheelCircumfrance = 4 * Math.PI;

        adRotatingRobotDrive[0] = wheelCircumfrance * (80 / 40) * 280;

        dInsideWheelDistance = dDegreeTurn * Math.PI * (dRadius + wheelBase);

        adRotatingRobotDrive[1] = wheelCircumfrance * (80 / 40) * 280;

        adRotatingRobotDrive[2] =   adRotatingRobotDrive[0] /   adRotatingRobotDrive[1];

    }

    public int straightRobotCalculation(Double dDistance){


        wheelCircumfrance = 4 * Math.PI;

        return (int) (dDistance * (80 / 40) * 280);


    }
    public void driveEqual(String Direction) {
        if (Direction == "forward") {
            if (rightDrive.getCurrentPosition() > leftDrive.getCurrentPosition()) {
                rightDrive.setPower(0.1);
                leftDrive.setPower(1.0);
            } else if (leftDrive.getCurrentPosition() > rightDrive.getCurrentPosition()) {
                rightDrive.setPower(1.0);
                leftDrive.setPower(0.1);
            } else {
                rightDrive.setPower(0.1);
                leftDrive.setPower(0.1);
            }
        }
        if (Direction == "backward") {
            if (rightDrive.getCurrentPosition() > leftDrive.getCurrentPosition()) {
                rightDrive.setPower(-1.0);
                leftDrive.setPower(-0.1);
            } else if (leftDrive.getCurrentPosition() > rightDrive.getCurrentPosition()) {
                rightDrive.setPower(-0.1);
                leftDrive.setPower(-1.0);
            } else {
                rightDrive.setPower(-0.1);
                leftDrive.setPower(-0.1);
            }
        }}


    int getLeftPosition() {
        return leftDrive.getCurrentPosition();
    }

    int getRightPosition() {
        return rightDrive.getCurrentPosition();

    }


    boolean moveComplete() {
        return ((Math.abs(getLeftPosition() - leftEncoderTarget) < 10) &&
                (Math.abs(getRightPosition() - rightEncoderTarget) < 10));
    }

    boolean encodersAtZero() {

        return ((Math.abs(getLeftPosition()) < 5) && (Math.abs(getRightPosition()) < 5));
    }








    }
//sup


