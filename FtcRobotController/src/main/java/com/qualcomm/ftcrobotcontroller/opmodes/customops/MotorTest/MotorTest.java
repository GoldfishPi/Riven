package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

/**
 * Created by cyberarm on 12/19/15.
 */

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

public class MotorTest extends AutonomousMindContainer {
    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    public MotorTest() {
    }

    @Override
    public void init() {
        autonomousInit();
    }

    public void autonomousInit() {
        lDrive = getMotor("lDrive");
        rDrive = getMotor("rDrive");

        lFinger = getMotor("lFinger");
        rFinger = getMotor("rFinger");

        winch = getMotor("armExtender");

        arm = getMotor("arm");

        lGill = getMotor("lGill");
        rGill = getMotor("rGill");

        theDumper = getServo("theDumper");

        lDrivePower = 0.05;
        rDrivePower = 0.05;

    }

    @Override
    public void init_loop(){
    }

    @Override
    public void loop() {
        setTelemetry();
        currentMachineState = "!DANGEROUS MOTOR TESTING!";

        lDrive.setPower(lDrivePower);
        rDrive.setPower(lDrivePower);
        lFinger.setPower(lDrivePower);
        rFinger.setPower(lDrivePower);
        arm.setPower(lDrivePower);
        winch.setPower(lDrivePower);
        lGill.setPower(lDrivePower);
        rGill.setPower(lDrivePower);
        theDumper.setPosition(lDrivePower);
    }
}