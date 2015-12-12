package com.qualcomm.ftcrobotcontroller.opmodes.customops.AutonomousMountainLeft;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousVariables;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousOp;
import com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp.DriverOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by cyberarm on 12/12/15.
 */

public class AutonomousMountainLeft extends AutonomousVariables {
    public AutonomousOp autonomousOp = new AutonomousOp();

    public AutonomousMountainLeft(){
    }

    @Override
    public void init() {
        autonomousOp.autonomousInit();
    }

    @Override
    public void init_loop(){
        autonomousOp.autonomousInitLoop();
    }

    @Override
    public void loop() {
        autonomousOp.autonomousloop();
    }
}
