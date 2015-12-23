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

    public DriverOp driverOp = new DriverOp(); //imports

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
    public int[] autoRightCorner;
    public int[] autoLeftCorner;

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
            STATE_STRAIGHT_TO_NEAR_BLUE         = 14,
            STATE_TURN_45_NEAR_BLUE_LEFT        = 15,
            STATE_REVERSE_90_DEGREE_RIGHT       = 16;


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
}
