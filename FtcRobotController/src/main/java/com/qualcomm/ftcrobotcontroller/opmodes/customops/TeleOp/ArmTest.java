package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by goldfishpi on 12/1/15.
 */
public class ArmTest extends OpMode {

    private DcMotor lDrive;
    private DcMotor rDrive;

    private DcMotor arm;

    public ArmTest(){}

    @Override
    public void init(){

        arm = hardwareMap.dcMotor.get("arm");
        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");

        rDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop(){

        arm.setPower(gamepad2.right_stick_y);
        lDrive.setPower(gamepad1.right_stick_y);
        rDrive.setPower(gamepad1.left_stick_y);

    }
}
