package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by goldfishpi on 12/12/15.
 */
public class AutonomousVariables extends OpMode {

    public DriverOp driverOp = new DriverOp(); //imports

    public String currentMachineState;

    public DcMotor lDrive;
    public DcMotor rDrive;

    public DcMotor lFinger;
    public DcMotor rFinger;

    public DcMotor armOut;
    public DcMotor armIn;

    public DcMotor arm;

    public int leftEncoderTarget;
    public int rightEncoderTarget;
    public double lDrivePower;
    public double rDrivePower;

    public double wheelBase = 15.75;
    public double wheelCircumfrance;
    public double dInsideWheelDistance;
    public double dOutsideWheelDistance;
    public double[] adRotatingRobotDrive = new double[5];

    public int stateMachineIndex = 0;
    public int[] stateMachineArray = new int[100];
    public int[] debugArray;
    public int[] autoRightCorner;
    public int[] autoLeftCorner;

    String currentState;

    public final int
            STATE_TURN_45_RIGHT                = 0,
            STATE_STOP = 1,
            STATE_DRIVE_STRAIGHT_CORNER_TO_GOAL = 2,
            STATE_TURN_45_LEFT = 3,
            STATE_RAISE_ARM = 4,
            STATE_EXTEND_ARM = 5,
            STATE_RETRACT_ARM = 6,
            STATE_STRAIGHT_PARK = 7;


    public int
            stateWait,
            stateDriveStraightConerToGoal = 0,
            stateStop = 0,
            stateTurn45Left = 0,
            stateRaiseArm = 0,
            stateExtnedArm = 0,
            stateRetractArm = 0;

    public void init(){

    }

    public void loop(){

    }

    public void setTelemetry() {
        telemetry.addData("State", currentMachineState);
        telemetry.addData("Left position", lDrive.getCurrentPosition());
        telemetry.addData("right position", rDrive.getCurrentPosition());

        telemetry.addData("left target", leftEncoderTarget);
        telemetry.addData("right target", rightEncoderTarget);

        System.out.println("left pos: " + lDrive.getCurrentPosition());
        System.out.println("Right pos: " + rDrive.getCurrentPosition());
        System.out.println();
        System.out.println("left  target: " + rightEncoderTarget);
        System.out.println("Right target: " + rightEncoderTarget);
        System.out.println();


    }

    public DcMotor getHardware(String string) {
        return hardwareMap.dcMotor.get(string);
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
