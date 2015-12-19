package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

/**
 * Created by cyberarm on 12/19/15.
 */

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousVariables;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorTest extends AutonomousVariables {
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
        lDrive = getHardware("lDrive");
        rDrive = getHardware("rDrive");

        lFinger = getHardware("lFinger");
        rFinger = getHardware("rFinger");

        armOut = getHardware("armOut");
        armIn  = getHardware("armIn");

        arm = getHardware("arm");

        lGill = getHardware("lGill");
        rGill = getHardware("rGill");

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
        armIn.setPower(lDrivePower);
        armOut.setPower(lDrivePower);
        lGill.setPower(lDrivePower);
        rGill.setPower(lDrivePower);
    }
}