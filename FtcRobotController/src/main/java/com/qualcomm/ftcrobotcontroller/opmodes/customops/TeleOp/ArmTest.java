package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by goldfishpi on 12/1/15.
 */
public class ArmTest extends OpMode {

    private DcMotor arm;

    public ArmTest(){}

    @Override
    public void init(){
        arm = hardwareMap.dcMotor.get("arm");
    }

    @Override
    public void loop(){

        arm.setPower(gamepad1.right_stick_y);

    }
}
