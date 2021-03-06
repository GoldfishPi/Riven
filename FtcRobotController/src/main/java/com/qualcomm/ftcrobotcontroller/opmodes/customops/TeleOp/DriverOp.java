package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by goldfishpi on 11/21/15.
 */
public class DriverOp extends OpMode {

    public double armSpeed = 0.3;
    public DcMotor[] motors = new DcMotor[8];
    public String[] motorNames = new String[]{"lDrive", "rDrive", "lFinger", "rFinger", "lGill", "rGill", "armExtender", "arm"};
    public double armExtenderSpeed;
    public boolean armExtended;
    private DcMotor lDrive;
    private DcMotor rDrive;
    private DcMotor lFinger;
    private DcMotor rFinger;
    private DcMotor lGill;
    private DcMotor rGill;
    private DcMotor armExtender;
    private DcMotor arm;
    private Servo lovePonny;
    private Servo rightShuriken;
    private Servo leftShuriken;
    private boolean yPressed = false;
    private boolean aPressed = false;
    private boolean xPressed = false;
    private boolean bPressed = false;
    private int calc = 0;

    public DriverOp() {

    }

    @Override
    public void init() {


        lDrive = hardwareMap.dcMotor.get("lDrive");
        rDrive = hardwareMap.dcMotor.get("rDrive");

        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        armExtender = hardwareMap.dcMotor.get("armExtender");

        lovePonny = hardwareMap.servo.get("theDumper");

        arm = hardwareMap.dcMotor.get("arm");

        rightShuriken = hardwareMap.servo.get("rightShuriken");

        leftShuriken = hardwareMap.servo.get("leftShuriken");

        rDrive.setDirection(DcMotor.Direction.FORWARD);
        lDrive.setDirection(DcMotor.Direction.REVERSE);

        rFinger.setDirection(DcMotor.Direction.REVERSE);
        lFinger.setDirection(DcMotor.Direction.FORWARD);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        lGill.setDirection(DcMotor.Direction.FORWARD);
        armExtender.setDirection(DcMotor.Direction.FORWARD);

        rightShuriken.setDirection(Servo.Direction.REVERSE);

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armExtender);

        resetEncoders(arm);

        for (int i = 0; i < motors.length; i++) {
            motors[i] = hardwareMap.dcMotor.get(motorNames[i]);
        }

    }

    @Override
    public void stop() {

        for (int i = 0; i < motors.length; i++) {
            motors[i].setPower(0.0);
        }

    }

    @Override
    public void init_loop() {

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armExtender);
        resetEncoders(arm);

        lDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        arm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


    }

    @Override
    public void loop() {

        telemetry.addData("ArmSpeed     ", armSpeed);
        telemetry.addData("Arm Encoders ", arm.getCurrentPosition());

        if (gamepad1.b) {
            System.out.println("lDrive  : " + lDrive.getCurrentPosition());
            System.out.println("rDrive  : " + rDrive.getCurrentPosition());
            System.out.println("Arm     : " + arm.getCurrentPosition());

        }

        controllerOne();
        controllerTwo();

    }


    public void controllerOne()//Living organism velocitys extender and phasing oscillation neutralizingsu   elliptical yielder
    {

        if (gamepad1.a) {
            lovePonny.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad1.y) {
            lovePonny.setPosition(1.0);
        }

        //Drive controls[
        lDrive.setPower(gamepad1.left_stick_y);
        rDrive.setPower(-gamepad1.right_stick_y);

        //Finger controls
        if (gamepad1.left_trigger != 0.0) {
            lFinger.setPower(0.3);
        } else if (gamepad1.left_bumper) {
            lFinger.setPower(-0.3);
        } else {
            lFinger.setPower(0.0);
        }
        lDrive.setPower(0.0);


        if (gamepad1.right_trigger != 0.0) {
            rFinger.setPower(0.3);
        } else {
            if (gamepad1.right_bumper) {
                rFinger.setPower(-0.3);
            } else {
                rFinger.setPower(0.0);
            }
        }


        if (gamepad1.x) {
            leftShuriken.setPosition(.8);
            rightShuriken.setPosition(.5);
        }

        if (gamepad1.b) {
            leftShuriken.setPosition(0);
            rightShuriken.setPosition(0);
        }

        //Drive controls
        if (lDrive.getMode() == DcMotorController.RunMode.RUN_TO_POSITION && rDrive.getMode() ==

                DcMotorController.RunMode.RUN_TO_POSITION) {
            lDrive.setPower(gamepad1.left_stick_y);
            rDrive.setPower(gamepad1.right_stick_y);
        }

        if (gamepad1.right_stick_y != 0) {
            rDrive.setPower(-gamepad1.right_stick_y);
        } else {
            rDrive.setPower(0.0);
        }

        if (gamepad1.left_stick_y != 0) {
            lDrive.setPower(-gamepad1.left_stick_y);
        } else {
            //Finger controls
            if (gamepad1.left_trigger != 0.0) {
                lFinger.setPower(0.3);
            } else if (gamepad1.left_bumper) {
                lFinger.setPower(-0.3);
            } else {
                lFinger.setPower(0.0);
            }
            lDrive.setPower(0.0);
        }

        if (gamepad1.right_trigger != 0.0) {
            rFinger.setPower(0.3);
        } else if (gamepad1.right_bumper) {
            rFinger.setPower(-0.3);

        } else {
            rFinger.setPower(0.0);
        }


        if (gamepad1.a) {
            lovePonny.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad1.y) {
            lovePonny.setPosition(Servo.MAX_POSITION);
        }

        //Drive controls
        if (lDrive.getMode() == DcMotorController.RunMode.RUN_TO_POSITION && rDrive.getMode() ==

                DcMotorController.RunMode.RUN_TO_POSITION) {
            lDrive.setPower(gamepad1.left_stick_y);
            rDrive.setPower(gamepad1.right_stick_y);
        }

        if (gamepad1.right_stick_y != 0) {
            rDrive.setPower(-gamepad1.right_stick_y);
        } else {
            rDrive.setPower(0.0);
        }

        if (gamepad1.left_stick_y != 0) {
            lDrive.setPower(-gamepad1.left_stick_y);
        } else {
            //Finger controls
            if (gamepad1.left_trigger != 0.0) {
                lFinger.setPower(0.3);
            } else if (gamepad1.left_bumper) {
                lFinger.setPower(-0.3);
            } else {
                lFinger.setPower(0.0);
            }
            lDrive.setPower(0.0);
        }


        if (gamepad1.right_trigger != 0.0) {
            rFinger.setPower(0.3);
        } else if (gamepad1.right_bumper) {
            rFinger.setPower(-0.3);

        } else {
            rFinger.setPower(0.0);
        }


        if (gamepad2.right_trigger != 0) {
            rGill.setPower(0.5);
        } else if (gamepad2.right_bumper) {
            rGill.setPower(-0.5);
        } else {
            rGill.setPower(0.0);
        }
    }

    public void controllerTwo() {

        if (!gamepad2.dpad_right) {
            arm.setPower(0.0);
            arm.setTargetPosition(0);
        }

        if (gamepad2.x && !xPressed && armSpeed < 1.100) {
            xPressed = true;
            armSpeed += 0.100;
        } else if (!gamepad2.x && xPressed) {
            xPressed = false;
        }

        if (gamepad2.b && !bPressed && armSpeed > -1.100) {
            bPressed = true;
            armSpeed -= 0.100;
        } else if (!gamepad2.b && bPressed) {
            bPressed = false;
        }

        //Arm In and Out
        if (gamepad2.y) {

            if (!armExtended) {
                armExtended = true;
                armExtenderSpeed = 0.0;
            }
            if (armExtenderSpeed < 0.9) {
                armExtenderSpeed += 0.1;
            }
            armExtender.setPower(armExtenderSpeed);

        } else if (gamepad2.a) {
            if (armExtended) {
                armExtended = false;
                armExtenderSpeed = 0.0;
            }
            if (armExtenderSpeed > -0.9) {
                armExtenderSpeed -= 0.1;
            }
            armExtender.setPower(armExtenderSpeed);
        } else {
            armExtender.setPower(0.0);
        }

        //Arm up and down

        armControlls(gamepad2.left_stick_y);

        //gill controls
        if (gamepad2.left_trigger != 0.0) {
            lGill.setPower(0.5);
        } else if (gamepad2.left_bumper) {
            lGill.setPower(-0.5);
        } else {
            lGill.setPower(0.0);
        }

        //dumper controlls
        if (gamepad1.a) {
            lovePonny.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad1.y) {
            lovePonny.setPosition(Servo.MAX_POSITION);
        }


    }

    public void extendEqual(double speed) {

        armExtender.setPower(speed);
    }

    public void armControlls(double speed) {

        arm.setPower(speed);

    }

    public void resetEncoders(DcMotor motor) {

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    public void setMotorToRunToPos(DcMotor motor) {

        motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

    }

}
