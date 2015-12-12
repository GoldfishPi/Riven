package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by goldfishpi on 12/7/15.
 */
public class AutoTest extends OpMode {

    private DcMotor winch;

    public void init(){
        winch = hardwareMap.dcMotor.get("lWinch");
    }

    public void loop(){
        telemetry.addData("this worked", null);
    }
}
