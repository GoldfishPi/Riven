package com.qualcomm.ftcrobotcontroller.opmodes.customops.MotorTest;

/**
 * Created by cyberarm on 12/19/15.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ServoTest extends OpMode {
    public Boolean mode = true;
    public double lDrivePower = 0.0;
    public Servo theDumper;

    //-------------------------------
    // motor declearations come from atonomous veriables class
    //------------------------------//

    public ServoTest() {
    }

    @Override
    public void init() {
        autonomousInit();
    }

    public void autonomousInit() {
        theDumper = hardwareMap.servo.get("theDumper");

        lDrivePower = 0.00;

    }

    @Override
    public void init_loop() {
    }

    public Boolean changeMode() {
        if (lDrivePower >= 1.0) {
            mode = false;
        }

        if (lDrivePower <= 0.0) {
            mode = true;
        }

        return mode;
    }

    @Override
    public void loop() {
        telemetry.addData("!WARNING! ", "!DANGEROUS SERVO TESTING! - " + mode);
        changeMode();
        theDumper.setPosition(Range.clip(lDrivePower, 0.0, 1.0));
        if (!mode) {
            lDrivePower-=0.01;
        }

        if (mode) {
            lDrivePower+=0.01;
        }
    }
}