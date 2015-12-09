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

    private enum State {

        STATE_INITIAL,
        STATE_DRIVE_TO_MIDDLE ,
        STATE_TURN,
        STATE_DRIVE_TO_WALL,
        STATE_RAISE_ARM,
        STATE_EXTEND_ARM,
        STATE_DRIVE_BACK,
        STATE_TURN_TO_RAMP,
        STATE_RAISE_ARM_MOUNTAIN,
        STATE_LOWER_ARM_SLIGHTLY,
        STATE_PULL_ROBOT,

        STATE_STOP,

    }


    //Drive forward using encoders (leftMotor, rightMotor, Speed)
    //final PathSeg[] DriveForward = {
    //   new PathSeg(1.0, 1.0, 1.0),
    //};
    final PathSeg[] driveBack = {

            new PathSeg(10.0, 10.0, 1.0)
    };

    final PathSeg[] driveForward = {

            new PathSeg(48.0, 48.0, 1.0)
    };

    final PathSeg[] turn90 = {

            new PathSeg(6.0, -6.0, 0.5)
    };

    final PathSeg[] turn45 = {

            new PathSeg(3.0, -3.0, 0.3)

    };

    final double COUNT_PER_INCH_DRIVE = 120;

    final double RANGE = 10;


    //-------------------------------
    // motor declearations
    //-------------------------------

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    //private DcMotor lFinger;
    //private DcMotor rFinger;

    private DcMotor armOut;
    private DcMotor armIn;

    private double distanceBetweenWheels = 15.75;

    private DcMotor lArm;
    private DcMotor rArm;
    private DcMotor arm;

    private DcMotor lCoffin;
    private DcMotor rCoffin;

    private int leftEncoderTarget;
    private int rightEncoderTarget;

    public LightSensor frontLightSensor;
    public LightSensor backLightSensor;

    public OpticalDistanceSensor distanceSonsor;
    public LightSensor lightSensor;

    public ElapsedTime runTime = new ElapsedTime();  // time into round

    public ElapsedTime stateTime = new ElapsedTime();  // time into state


    private State currentState;

    private PathSeg[] currentPath;

    private int currentSeg;

    public boolean debug = true;


    public void stateMachine() {
    }

    public AtonomusOp() {

    }

    @Override
    public void init() {

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        //lFinger = hardwareMap.dcMotor.get("lFinger");
        //rFinger = hardwareMap.dcMotor.get("rFinger");

        armOut = hardwareMap.dcMotor.get("armOut");
        armIn = hardwareMap.dcMotor.get("armIn");
        armIn.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.dcMotor.get("arm");

        lCoffin = hardwareMap.dcMotor.get("lCoffin");
        rCoffin = hardwareMap.dcMotor.get("rCoffin");
        rCoffin.setDirection(DcMotor.Direction.REVERSE);

        setDrivePower(0, 0);
        resetDriveEncoders();

    }

    @Override
    public void init_loop() {

        resetDriveEncoders();

    }

    @Override
    public void start() {


        setDriveSpeed(0, 0);
        runToPosition(); // run to the position set by the encoders
        runTime.reset();
        newState(State.STATE_INITIAL);
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

        switch (currentState) {
            case STATE_INITIAL:
                if (encodersAtZero()) {
                    setDriveSpeed(1.0, 1.0);
                    startPath(driveForward);
                    newState(State.STATE_DRIVE_TO_MIDDLE);
                }
                break;

            case STATE_DRIVE_TO_MIDDLE:

                if (pathComplete()) {

                    startPath(turn90);
                    newState(State.STATE_TURN);
                }
                break;

            case STATE_TURN:

                if (pathComplete()) {

                    setDriveSpeed(0.3, 0.3);
                    newState(State.STATE_DRIVE_TO_WALL);
                }

                break;

            case STATE_DRIVE_TO_WALL:

                if (distanceSonsor.getLightDetected() > RANGE) {

                    setDriveSpeed(0.0, 0.0);
                    setArmSpeed(0.3);
                    newState(State.STATE_RAISE_ARM);
                }
                break;

            case STATE_RAISE_ARM:

                if (stateTime.time() > 3.0) {
                    setArmSpeed(0.0);
                    setWinchSpeed(0.5, 0.5);
                    newState(State.STATE_EXTEND_ARM);
                }
                break;

            case STATE_EXTEND_ARM:
                if (stateTime.time() > 3.0) {
                    setWinchSpeed(0.0, 0.0);
                    setCoffinSpeed(0.1, 0.1);
                    startPath(driveBack);
                    newState(State.STATE_DRIVE_BACK);
                }
                break;

            case STATE_DRIVE_BACK:
                if (pathComplete()) {
                    setDriveSpeed(0.0, 0.0);
                    newState(State.STATE_TURN_TO_RAMP);
                    break;
                }

            case STATE_TURN_TO_RAMP:
                resetDriveEncoders();
                startPath(turn45);
                newState(State.STATE_RAISE_ARM_MOUNTAIN);
                break;
            case STATE_RAISE_ARM_MOUNTAIN:
                if(pathComplete()){
                    setArmSpeed(0.3);

                    if( lArm.getTargetPosition() > 1000 ){
                        newState(State.STATE_LOWER_ARM_SLIGHTLY);
                        break;
                    }
                }
                break;
            case STATE_LOWER_ARM_SLIGHTLY:

                setArmSpeed(-0.3);
                if(lArm.getTargetPosition() < 750)

                break;
            case STATE_PULL_ROBOT:
                setWinchSpeed(-0.3, -0.6);
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

    void newState(State newState) {
        stateTime.reset();
        currentState = newState;

    }

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

        armOut.setPower(Range.clip(leftWinchPower, -1, 1));
        armIn.setPower(Range.clip(rightWinchPower, -1, 1));
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

    public void resetDriveEncoders() {

        setEncoderTarget(0, 0);
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);

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

    private void startPath(PathSeg[] path) {
        currentPath = path;
        currentSeg = 0;
        synchEncoders();
        runToPosition();
        startSeg();
    }

    private void startSeg() {
        int left;
        int right;
        if (currentPath != null) {
            left = (int) (currentPath[currentSeg].LEFT * COUNT_PER_INCH_DRIVE);
            right = (int) (currentPath[currentSeg].RIGHT * COUNT_PER_INCH_DRIVE);
            addEncoderTarget(left, right);
            setDriveSpeed(currentPath[currentSeg].speed, currentPath[currentSeg].speed);

            currentSeg++;
        }
    }

    private boolean pathComplete() {
        if (moveComplete()) {
            if (currentSeg < currentPath.length) {
                startSeg();
            } else {
                currentPath = null;
                currentSeg = 0;
                setDriveSpeed(0, 0);
                useConstantSpeed();
                return true;
            }
        }
        return false;
    }

    public static void debug(DebugLevel level, String message) {
        String name = "Siver";
        switch (level) {
            default:
            case INFO:
                System.out.println("[" + name + "]" + message);
                break;
            case WARNING:
                System.out.println("[" + name + "] [WARNING] " + message);
                break;
            case SEVERE:
                System.out.println("[" + name + "] [SEVERE] " + message);
                break;
        }



    }

    public static enum DebugLevel {
        INFO, WARNING, SEVERE;
    }

    public void turn45(){}
}

class PathSeg {
    public double LEFT;
    public double RIGHT;
    public double speed;

    public PathSeg(double Left, double Right, double Speed) {

        LEFT = Left;
        RIGHT = Right;
        speed = Speed;




    }
}}


class turn

    public void turn45(float fRadius, float fDegreeTurn) {

        float wheelBase = 15.75;
        float fWheelCircumfrance;
        float fDistanceInsideTurn;
        float fDistanceOutsideTurn;
        float fEncoder2;
        float fEncoder1;
        float ret = [3];


        fDistanceInsideTurn = fDegreeTurn * Math.PI / 180 * fRadius;

        fWheelCircumfrance = 4 * Math.PI;

        ret[0] = fWheelCircumfrance * 2 * 1044;

        fDistanceOutsideTurn = fDegreeTurn * Math.PI / 180 * (fRadius + 15.75);

        ret[1] = fWheelCircumfrance * 2 * 1044;
    }





    }


//sup sup