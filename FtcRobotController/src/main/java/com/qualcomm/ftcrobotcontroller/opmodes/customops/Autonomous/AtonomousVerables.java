package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by goldfishpi on 12/12/15.
 */
public class AtonomousVerables extends OpMode {


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
    public void init(){

    }

    public void loop(){

    }
}
