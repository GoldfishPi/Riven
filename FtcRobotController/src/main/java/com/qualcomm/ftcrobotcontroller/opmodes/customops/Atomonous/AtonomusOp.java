package com.qualcomm.ftcrobotcontroller.opmodes.customops.Atomonous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
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







    //-------------------------------
    // motor declearations
    //-------------------------------

    private DcMotor lDrive;
    private DcMotor rDrive;

    private DcMotor lFinger;
    private DcMotor rFinger;

    private DcMotor armOut;
    private DcMotor armIn;
    
    private DcMotor arm;

    private int leftEncoderTarget;
    private int rightEncoderTarget;
    double lDrivePower;
    double rDrivePower;

    double wheelBase = 15.75;
    double wheelCircumfrance;
    double dInsideWheelDistance;
    double dOutsideWheelDistance;
    double[] adRotatingRobotDrive = new double[5];

    public int stateMachineIndex = 0;
    public int[] stateMachineArray = new int[100];
    public int[] debugArray;
    public int[] autoRightCorner;
    public int[] autoLeftCorner;





    final int
            STATE_TURN_45_RIGHT = 0,
            STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL = 2,
            STATE_STOP = 1,
            STATE_TURN_45_LEFT = 3,
            STATE_RAISE_ARM = 4,
            STATE_EXTEND_ARM = 5,
            STATE_RETRACT_ARM = 6;


    public int
            stateWait,
            stateDriveStraightConerToGoal = 0,
            stateStop = 0,
            stateTurn45Left = 0,
            stateRaiseArm = 0,
            stateExtnedArm = 0,
            stateRetractArm = 0;






    public DriverOp driverOp = new DriverOp(); //imports


    public AtonomusOp() {

    }



    @Override
    public void init() {

        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");
        lDrive.setDirection(DcMotor.Direction.REVERSE);
        rDrive.setDirection(DcMotor.Direction.FORWARD);

        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        armOut = hardwareMap.dcMotor.get("armOut");
        armIn = hardwareMap.dcMotor.get("armIn");
        armIn.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.dcMotor.get("arm");
    }



     @Override
    public void init_loop() {
         stateWait = 0;
         stateMachineIndex = 0;
         debugArray = new int[100];
         debugArray[0] = STATE_TURN_45_RIGHT;
         debugArray[1] = STATE_TURN_45_RIGHT;
         debugArray[2] = STATE_TURN_45_RIGHT;
         debugArray[3] = STATE_TURN_45_RIGHT;

         debugArray[4] = STATE_STOP;

         for (int i = 0; i < 100; i++) {
             stateMachineArray[i] = debugArray[i];
         }

         leftEncoderTarget = lDrive.getCurrentPosition();
         rightEncoderTarget= rDrive.getCurrentPosition();

         lDrivePower = 0.0;
         rDrivePower = 0.0;

         lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
         rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));

         lDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
         rDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

         lDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
         rDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
     }


    @Override
    public void loop() {
        telemetry.addData("Left position", lDrive.getCurrentPosition());
        telemetry.addData("right position", rDrive.getCurrentPosition());

        telemetry.addData("left target", leftEncoderTarget);
        telemetry.addData("right target", rightEncoderTarget);

        switch (stateMachineArray[stateMachineIndex]) {
            case STATE_TURN_45_RIGHT:
                if (stateWait == 0){
                    System.out.print("Start TURN_45_RIGHT\n");

                    stateWait = 1;

                    turnRobotCalculation(12, 45);// 24 inches, 45 degrees
                    leftEncoderTarget  = (int) adRotatingRobotDrive[1] + lDrive.getCurrentPosition();
                    rightEncoderTarget = (int) adRotatingRobotDrive[0] + rDrive.getCurrentPosition();

                    System.out.print(String.valueOf(adRotatingRobotDrive[1]));
                    System.out.print(String.valueOf(adRotatingRobotDrive[0]));

                    lDrivePower = 1.0;
                    rDrivePower = (1.0 * adRotatingRobotDrive[2]);

                    lDrive.setTargetPosition(leftEncoderTarget);
                    rDrive.setTargetPosition(rightEncoderTarget);

                    lDrive.setPower(Range.clip(lDrivePower, -1.0, 1.0));
                    rDrive.setPower(Range.clip(rDrivePower, -1.0, 1.0));
                }

                if ((lDrive.getCurrentPosition() >= leftEncoderTarget - 15)) {
                    lDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                }

                if ((rDrive.getCurrentPosition() >= rightEncoderTarget - 15)) {
                    rDrivePower = 0.0;
                    rDrive.setPower(rDrivePower);
                }

                if ((lDrivePower == 0.0) && (rDrivePower == 0.0)) {
                    System.out.print("45 Complete\n");
                    lDrivePower = 0.0;
                    rDrivePower = 0.0;
                    lDrive.setPower(lDrivePower);
                    rDrive.setPower(rDrivePower);

                    stateWait = 0;
                    stateMachineIndex ++;
                }

                System.out.print("Waiting...\n");
                break;

            case STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL:
                if ( stateDriveStraightConerToGoal == 0) {
                    resetEncodersAuto(rDrive);
                    resetEncodersAuto(lDrive);
                    driveStraight(straightRobotCalculation((double) (12.5)));

                }
                else if(moveComplete()){
                    stateMachineIndex ++;
                }
                break;

            case STATE_RAISE_ARM:
                if (stateRaiseArm == 0){
                    resetEncodersAuto(arm);
                    arm.setTargetPosition(280);
                    arm.setPower(0.3);
                }
                else if(moveComplete()){
                    arm.setPower(0.0);
                    stateMachineIndex ++;
                }
                break;

            case STATE_EXTEND_ARM:
                if (stateExtnedArm == 0){
                    resetEncodersAuto(armOut);
                    resetEncodersAuto(armIn);
                    extendArm();
                }
                else if (extendFinished()){
                    stateMachineIndex ++;
                }
                break;

            case STATE_RETRACT_ARM:
                if(stateRetractArm == 0){
                    resetEncodersAuto(armOut);
                    resetEncodersAuto(armIn);
                    retractArm();
                }
                else if(retractDone()){
                    stateMachineIndex ++;
                }

            case STATE_STOP:
                System.out.println("STOP STATE");
                break;

            default:
        }
    }

    @Override
    public void stop() {
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
        return ((Math.abs(lDrive.getCurrentPosition() - leftEncoderTarget) < 10) &&
                (Math.abs(rDrive.getCurrentPosition() - rightEncoderTarget) < 10));
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


    public void driveStraight(int encoderValue){
        resetEncodersAuto(lDrive);
        resetEncodersAuto(rDrive);
        setEncoderTarget(encoderValue, encoderValue);
        setDriveSpeed(0.5, 0.5);
    }

    public void resetEncodersAuto(DcMotor motor){
        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
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

    public void driveEqual(String Direction) {
        if ("forward".equals(Direction)) {
            if (rDrive.getCurrentPosition() > lDrive.getCurrentPosition()) {
                rDrive.setPower(0.1);
                lDrive.setPower(1.0);
            } else if (lDrive.getCurrentPosition() > rDrive.getCurrentPosition()) {
                rDrive.setPower(1.0);
                lDrive.setPower(0.1);
            } else {
                rDrive.setPower(0.1);
                lDrive.setPower(0.1);
            }
        }
        if (Direction .equals("backwards")) {
            if (rDrive.getCurrentPosition() > lDrive.getCurrentPosition()) {
                rDrive.setPower(-1.0);
                lDrive.setPower(-0.1);
            } else if (lDrive.getCurrentPosition() > rDrive.getCurrentPosition()) {
                rDrive.setPower(-0.1);
                lDrive.setPower(-1.0);
            } else {
                rDrive.setPower(-0.1);
                lDrive.setPower(-0.1);
            }
        }
    }
}