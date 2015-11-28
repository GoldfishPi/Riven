package com.qualcomm.ftcrobotcontroller.opmodes.customops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Constructor;

/* This autonomous does the following steps
* 0) Wait For encoder to reset
* 1) Drive forward until the light sensor senses white line
* 2) turn until back sensor senses white line
* 3) Drive forward until the distance sensor reads 5
* 4) Raise Arm using encoder ticks
* 5) Extend Arm using encoder ticks
* 6) release climbers
* 7) Drive straight back
* 8) Stop all
 */

public class AtonomusOp extends OpMode {

    private enum State {

        STATE_INITIAL,
        STATE_DRIVE_TO_WHITE_LINE,
        STATE_TURN,
        STATE_DRIVE_TO_WALL,
        STATE_RAISE_ARM,
        STATE_EXTEND_ARM,
        STATE_RELEASE_CLIMBERS,
        STATE_DRIVE_BACK,
        STATE_STOP,

    }


        //Drive forward using encoders (leftMotor, rightMotor, Speed)
    //final PathSeg[] DriveForward = {
     //   new PathSeg(1.0, 1.0, 1.0),
    //};
    final PathSeg[] driveBack = {

            new PathSeg(10.0, 10.0, 1.0)
    };

    final double count_per_inch_drive = 140;
    final double white = 0.5;
    final double range = 0.5;


    //-------------------------------
    // motor declearations
    //-------------------------------

    private DcMotor lDrive;
    private DcMotor rDrive;

   //private DcMotor lFinger;
   //private DcMotor rFinger;

    private DcMotor lWinch;
    private DcMotor rWinch;

    private DcMotor lArm;
    private DcMotor rArm;

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


    private State   currentState;

    private PathSeg[] currentPath;

    private int currentSeg;



    public void stateMachine() {
    }

    @Override
    public void init() {

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");
        rDrive.setDirection(DcMotor.Direction.REVERSE);

        //lFinger = hardwareMap.dcMotor.get("lFinger");
        //rFinger = hardwareMap.dcMotor.get("rFinger");

        lWinch = hardwareMap.dcMotor.get("lWinch");
        rWinch = hardwareMap.dcMotor.get("rWinch");
        rWinch.setDirection(DcMotor.Direction.REVERSE);

        lArm = hardwareMap.dcMotor.get("lArm");
        rArm = hardwareMap.dcMotor.get("rArm");
        rArm.setDirection(DcMotor.Direction.REVERSE);

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

        switch (currentState)
        {
            case STATE_INITIAL:
                if (encodersAtZero()) {
                    setDriveSpeed(1.0, 1.0);
                    lightSensor.enableLed(true);
                    newState(State.STATE_DRIVE_TO_WHITE_LINE);
                }
                break;

            case STATE_DRIVE_TO_WHITE_LINE:

                if (frontLightSensor.getLightDetectedRaw() > white) {

                    setDriveSpeed(1.0, -1.0);
                    newState(State.STATE_TURN);
                }
                    break;


            case STATE_TURN:

                if (backLightSensor.getLightDetectedRaw() > white) {

                    setDriveSpeed(0.3, 0.3);
                    newState(State.STATE_DRIVE_TO_WALL);
                }

                    break;



            case STATE_DRIVE_TO_WALL:

                if (distanceSonsor.getLightDetected() > range)
                {

                    setDriveSpeed(0.0, 0.0);
                    setArmSpeed(0.3, 0.3);
                    newState(State.STATE_RAISE_ARM);
                }

                break;
            case STATE_RAISE_ARM:

                if(stateTime.time() > 3.0 ) {
                    setArmSpeed(0.0, 0.0);
                    setWinchSpeed(0.5, 0.5);
                    newState(State.STATE_EXTEND_ARM);
                }
            case STATE_EXTEND_ARM:
                if (stateTime.time() > 3.0) {
                    setWinchSpeed(0.0, 0.0);
                    setCoffinSpeed(0.1, 0.1);
                    startPath(driveBack);
                    newState(State.STATE_DRIVE_BACK);
                }

            case STATE_DRIVE_BACK:
                if(pathComplete()){
                    setDriveSpeed(0.0, 0.0);
                    newState(State.STATE_STOP);
                }

            case STATE_STOP:
                break;
        }
    }
    @Override
    public void stop(){

        useConstantPower();
        setDrivePower(0.0, 0.0);
    }

    //-------------------------------------
    //functions here...
    //-------------------------------------

    void newState(State newState){
        stateTime.reset();
        currentState = newState;
    }

    void setEncoderTarget(int leftEncoder, int rightEncoder){

        lDrive.setTargetPosition(leftEncoderTarget = leftEncoder);
        rDrive.setTargetPosition(rightEncoderTarget = rightEncoder);

    }

    void addEncoderTarget(int leftEncoder, int rightEncoder){

        lDrive.setTargetPosition(leftEncoderTarget += leftEncoder);
        rDrive.setTargetPosition(rightEncoderTarget += rightEncoder);
    }

     void setDrivePower(double leftPower, double rightPower){
        lDrive.setPower(Range.clip(leftPower, -1, 1));
        rDrive.setPower(Range.clip(rightPower, -1, 1));
    }

    void setDriveSpeed( double leftSpeed, double rightSpeed){

        setDrivePower(leftSpeed, rightSpeed);
    }

    void setCoffinPower(double leftCoffinPower, double rightCoffinPower){

        lCoffin.setPower(Range.clip(leftCoffinPower, -1, 1));
        rCoffin.setPower(Range.clip(rightCoffinPower, -1, 1));
    }

    void setCoffinSpeed(double leftCoffinSpeed, double rightCoffinSpeed){

        setCoffinPower(leftCoffinSpeed, rightCoffinSpeed);
    }


    void setArmPower(double leftPowerArm, double rightPowerArm){
        lArm.setPower(Range.clip(leftPowerArm, -1, 1));
        rArm.setPower(Range.clip(rightPowerArm, -1, 1));
    }

    void setArmSpeed(double leftArmSpeed, double rightArmSpeed){
        setArmPower(leftArmSpeed, rightArmSpeed);
    }

    void setWinchPower(double leftWinchPower, double rightWinchPower){

        lWinch.setPower(Range.clip(leftWinchPower, -1, 1));
        rWinch.setPower(Range.clip(rightWinchPower, -1, 1));
    }

    void setWinchSpeed(double leftWinchSpeed, double rightWinchSpeed){

        setWinchPower(leftWinchSpeed, rightWinchSpeed);
    }

    public void runToPosition(){

        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void useConstantSpeed(){
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void useConstantPower(){

        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

    }
    public void resetDriveEncoders(){

        setEncoderTarget(0, 0);
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);

    }

    void synchEncoders(){

        leftEncoderTarget = lDrive.getCurrentPosition();
        rightEncoderTarget = rDrive.getCurrentPosition();
    }

    public void setDriveMode(DcMotorController.RunMode mode){
        if(lDrive.getChannelMode() != mode)
            lDrive.setChannelMode(mode);

        if(rDrive.getChannelMode() != mode)
            rDrive.setChannelMode(mode);
    }

    int getLeftPosition(){
        return lDrive.getCurrentPosition();
    }

    int getRightPosition(){
        return rDrive.getCurrentPosition();

    }


    boolean moveComplete(){
       return ((Math.abs(getLeftPosition() - leftEncoderTarget) < 10) &&
               (Math.abs(getRightPosition() - rightEncoderTarget) < 10));
    }

    boolean encodersAtZero(){

        return ((Math.abs(getLeftPosition()) < 5) && (Math.abs(getRightPosition()) < 5));
    }
    private void startPath(PathSeg[] path){
        currentPath = path;
        currentSeg = 0;
        synchEncoders();
        runToPosition();
        startSeg();
    }

    private void startSeg(){
        int left;
        int right;
        if (currentPath != null){
            left = (int)(currentPath[currentSeg].LEFT * count_per_inch_drive);
            right =  (int)(currentPath[currentSeg].RIGHT * count_per_inch_drive);
            addEncoderTarget(left, right);
            setDriveSpeed(currentPath[currentSeg].speed, currentPath[currentSeg].speed);

            currentSeg++;
        }
    }

    private boolean pathComplete(){
        if (moveComplete()){
            if (currentSeg < currentPath.length){
                startSeg();
            }
            else {
                currentPath = null;
                currentSeg = 0;
                setDriveSpeed(0, 0);
                useConstantSpeed();
                return true;
            }
        }
        return false;
    }
}

class PathSeg{
    public double LEFT;
    public double RIGHT;
    public double speed;

    public PathSeg(double Left, double Right, double Speed){

        LEFT = Left ;
        RIGHT = Right;
        speed = Speed;


    }
}