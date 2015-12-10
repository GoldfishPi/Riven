package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by goldfishpi on 11/21/15.
 */
public class DriverOp extends OpMode {

    private DcMotor lDrive;
    private DcMotor rDrive;

    private DcMotor lFinger;
    private DcMotor rFinger;

    private DcMotor lGill;
    private DcMotor rGill;

    private DcMotor armOut;
    private DcMotor armIn;

    private DcMotor arm;

    boolean evening = false;

    String forward = "forward";
    String backward = "backward";

    private boolean encoderInit = true;

    private int armOutInital;
    private int armOutFinal;

    private int armInInital;
    private int armInFinal;

    private int armOutDelta;
    private int armInDelta;

    private double rotation = 280*3;

    private int lastTargetPosition;

    boolean rFast;
    boolean lFast;

    private boolean yPressed = false;
    private boolean aPressed = false;

    boolean armRatchet = false;

    public DriverOp() {

    }

    @Override
    public void init() {

        lDrive  = hardwareMap.dcMotor.get("lDrive");
        rDrive  = hardwareMap.dcMotor.get("rDrive");


        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill   = hardwareMap.dcMotor.get("lGill");
        rGill   = hardwareMap.dcMotor.get("rGill");

        armOut  = hardwareMap.dcMotor.get("armOut");
        armIn   = hardwareMap.dcMotor.get("armIn");

        arm     = hardwareMap.dcMotor.get("arm");

        rDrive.setDirection(DcMotor.Direction.REVERSE);
        rFinger.setDirection(DcMotor.Direction.REVERSE);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        armIn.setDirection(DcMotor.Direction.FORWARD);
        armOut.setDirection(DcMotor.Direction.FORWARD);
        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armIn);
        resetEncoders(armOut);
        resetEncoders(arm);
        arm.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);


        armOutInital = armOut.getCurrentPosition() + 100000;
        armInInital = armIn.getCurrentPosition() + 100000;

    }

    @Override
    public void stop(){

    }

    @Override
    public void init_loop(){

        resetEncoders(lDrive);
        resetEncoders(rDrive);
        resetEncoders(armIn);
        resetEncoders(armOut);
        resetEncoders(arm);

        armOutInital = armOut.getCurrentPosition() + 100000;
        armInInital = armIn.getCurrentPosition() + 100000;

    }

    @Override
    public void loop(){
        telemetry.addData("Winch out current position", armOut.getCurrentPosition());
        telemetry.addData("winch in current position", armIn.getCurrentPosition());
        controllerOne();
        controllerTwo();
        setMotorToRunToPos(arm);
    }

    public void controllerOne(){

        //Drive controls
        if(!evening) {
        lDrive.setPower(gamepad1.left_stick_y);
        rDrive.setPower(gamepad1.right_stick_y);

        //Finger controls

            if (gamepad1.left_trigger != 0.0) {
                lFinger.setPower(0.3);
            } else if (gamepad1.left_bumper) {
                lFinger.setPower(-0.3);
            } else {
                 lFinger.setPower(0.0);
            }
            if (gamepad1.right_trigger != 0.0) {
                rFinger.setPower(0.3);
            } else if (gamepad1.right_bumper) {
                rFinger.setPower(-0.3);
            } else {
                rFinger.setPower(0.0);
            }
        }



        driveEncoderCheck(rDrive, lDrive);
    }



    public  void controllerTwo() {


//toggles arm out with gamepad.2's y button arm in with gampad.2's a button
        if (gamepad2.y) {

            yPressed = true;
            extendEqual("out", Math.abs(gamepad2.left_stick_y));
        } else if (!gamepad2.y && yPressed) {
            yPressed = false;
        }

        if (gamepad2.a) {
            aPressed = true;
            extendEqual("in", Math.abs(gamepad2.left_stick_y));
        } else if (!gamepad2.a && aPressed) {
            aPressed = false;
        }

        //Arm up and down


        if (gamepad2.right_stick_y < 0.0 && !armRatchet) {
            arm.setTargetPosition((int)(arm.getCurrentPosition() + rotation));
            arm.setPower(0.4);
            armRatchet = true;
        }
        else if(gamepad2.right_stick_y == 0 && armRatchet){
            arm.setPower(0.0);
            armRatchet = false;
        }



        if (gamepad2.right_stick_y > 0.0 && !armRatchet) {
            arm.setTargetPosition((int)(arm.getCurrentPosition() - rotation));
            arm.setPower(-0.4);
            armRatchet = true;
        }
        else if(gamepad2.right_stick_y == 0 && armRatchet){
            arm.setPower(0.0);
            armRatchet = false;
        }


        //gill controls
        if(gamepad2.left_trigger != 0.0){
            lGill.setPower(0.5);
        }
        else if(gamepad2.left_bumper){
            lGill.setPower(-0.5);
        }
        else{
            lGill.setPower(0.0);
        }




        if(gamepad2.right_trigger !=0){
            rGill.setPower(0.5);
        }
        else if(gamepad2.right_bumper){
            rGill.setPower(-0.5);
        }
        else{
            rGill.setPower(0.0);
        }


    }

    public void driveEncoderCheck(DcMotor motor1, DcMotor motor2){
        if(motor1.getCurrentPosition() > motor2.getCurrentPosition() +100){
            rFast = true;
        }else{rFast = false;}

        if(motor2.getCurrentPosition() > motor1.getCurrentPosition() + 100){
            lFast = true;
        }else{lFast = false;}

    }



    public void evenDriveMotors(){
        if(rFast){
            rDrive.setPower(1.0);
            lDrive.setPower(0.0);
        }
        else if(lFast){
            rDrive.setPower(0.0);
            lDrive.setPower(1.0);
        }
        else{
            rDrive.setPower(0.0);
            lDrive.setPower(0.0);
        }
    }

    public void extendEqual(String direction, double speed){

        double ratio = 1;
        double driveSpeed = speed;
        double slowSpeed = 0.0;
        int lbm = 200;

        double absInEncoder = Math.abs(armIn.getCurrentPosition());
        double absOutEncoder = Math.abs(armOut.getCurrentPosition());

        if(direction == "out") {
                /*armOutFinal = armOut.getCurrentPosition() + 100000;
                armInFinal = armIn.getCurrentPosition () + 100000;
                armOutDelta = Math.abs(armOutFinal - armOutInital);
                armInDelta = Math.abs(armInFinal - armInInital);
                armOutDelta /= 2;
                if (armInDelta <= armOutDelta) {

                    armOut.setPower(1.0);
                    armIn.setPower(0.8);


                }else if(armInDelta > armOutDelta){
                        armOut.setPower(1.0);
                        armIn.setPower(armOut.getPower()/4);
                } else {

                }*/

            setMotorToRunToPos(armIn);
            setMotorToRunToPos(armOut);

            if(absOutEncoder >= absInEncoder - lbm) {
                armOut.setPower(driveSpeed);
                armIn.setPower(driveSpeed);
            }
            else if(absInEncoder - lbm >= absOutEncoder){
                armOut.setPower(driveSpeed);
                armIn.setPower(slowSpeed);
            }

            armOut.setTargetPosition(armOut.getCurrentPosition() + (int) (rotation));
            armIn.setTargetPosition((int) (armIn.getCurrentPosition() + (rotation / ratio)));





        }
        if(direction == "in") {

            /*armOutFinal = armOut.getCurrentPosition() + 100000;
            armInFinal = armIn.getCurrentPosition() + 100000;
            armOutDelta = Math.abs(armOutFinal - armOutInital);
            armInDelta = Math.abs(armInFinal - armInInital);
            armOutDelta /= 2;*/
            /*if (armOutDelta <= armInDelta) {

                armOut.setPower(-1.0);
                armIn.setPower(-0.5);


            } else if (armOutDelta > armInDelta) {
                armOut.setPower(0.0);
                armIn.setPower(-0.5);
            }*/

            setMotorToRunToPos(armIn);
            setMotorToRunToPos(armOut);

            if(absInEncoder <= absOutEncoder - lbm) {
                armOut.setPower(-driveSpeed);
                armIn.setPower(-driveSpeed);
            }
            else if (absOutEncoder - lbm <= absInEncoder) {

                armOut.setPower(-slowSpeed);
                armIn.setPower(-driveSpeed);

            }

            armOut.setTargetPosition(armOut.getCurrentPosition() - (int) (rotation));
            armIn.setTargetPosition((int) (armIn.getCurrentPosition() - (rotation / ratio)));

        }
    }


    public void resetEncoders(DcMotor motor){
        motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void setMotorToRunToPos(DcMotor motor) {
        motor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
}

