package com.qualcomm.ftcrobotcontroller.opmodes.customops.TeleOp;

import com.qualcomm.ftcrobotcontroller.opmodes.Junk.MatrixK9TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

    boolean rFast;
    boolean lFast;





    public DriverOp() {

    }

    @Override
    public void init() {



        lDrive = hardwareMap.dcMotor.get("leftDrive");
        rDrive = hardwareMap.dcMotor.get("rightDrive");


        lFinger = hardwareMap.dcMotor.get("lFinger");
        rFinger = hardwareMap.dcMotor.get("rFinger");

        lGill = hardwareMap.dcMotor.get("lGill");
        rGill = hardwareMap.dcMotor.get("rGill");

        armOut = hardwareMap.dcMotor.get("armOut");
        armIn = hardwareMap.dcMotor.get("armIn");

        arm = hardwareMap.dcMotor.get("arm");

        rDrive.setDirection(DcMotor.Direction.REVERSE);
        rFinger.setDirection(DcMotor.Direction.REVERSE);
        rGill.setDirection(DcMotor.Direction.REVERSE);
        armIn.setDirection(DcMotor.Direction.REVERSE);


    }

    @Override
    public void stop(){

    }

    @Override
    public void init_loop(){


    }

    @Override
    public void loop(){
        telemetry.addData("Right Motor", rDrive.getCurrentPosition());
        telemetry.addData("Left Motor", lDrive.getCurrentPosition());
        controllerOne();
        controllerTwo();
    }

    public void controllerOne(){

        //Drive controls
        lDrive.setPower(gamepad1.left_stick_y);
        rDrive.setPower(gamepad1.right_stick_y);

        //Finger controls
        if(!evening) {
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

        if(gamepad1.x){
            evening = true;
            evenDriveMotors();
        }
        else{evening = false;}
        if(gamepad1.a){
            driveEqual(forward);
        }
        if(gamepad1.y){
            driveEqual(backward);
        }

        driveEncoderCheck(rDrive, lDrive);
    }



    public  void controllerTwo(){


        //extending arm controls.
        if (gamepad2.left_stick_y > 0.0) {
            if(armIn.getCurrentPosition() > armOut.getCurrentPosition()/2){

                armOut.setPower(-1.0);
                armIn.setPower(0.1);

            }else if( armOut.getCurrentPosition()/2 > armIn.getCurrentPosition() ){
                armOut.setPower(-0.1);
                armIn.setPower(0.5);

            }else{
                armOut.setPower(-1.0);
                armIn.setPower(0.5);

            }
        }
        //retracting arm controls
        else if (gamepad2.left_stick_y < 0.0){
            armOut.setPower(1.0);
            armIn.setPower(-0.5);
        }else if(gamepad2.y){
            armOut.setPower(0.1);
        }else if(gamepad2.a){
            armIn.setPower(-0.1);
        }
        else {
            armOut.setPower(0.0);
            armIn.setPower(0.0);
        }



        //raising arm controlls
        arm.setPower(gamepad2.right_stick_y);
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
        telemetry.addData("right to fast", rFast );
        telemetry.addData("left to fast", lFast);
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

    public void driveEqual(String direction){
        if(direction == "forward") {
            if (rDrive.getCurrentPosition() > lDrive.getCurrentPosition()) {
                rDrive.setPower(0.1);
                lDrive.setPower(1.0);
            } else if (lDrive.getCurrentPosition() > rDrive.getCurrentPosition()) {
                rDrive.setPower(1.0);
                lDrive.setPower(0.1);
            } else {
                rDrive.setPower(0.1);
                lDrive.setPower(0.1);
            }
        }
        if(direction == "backward"){
            if (rDrive.getCurrentPosition() > lDrive.getCurrentPosition()){
                rDrive.setPower(-1.0);
                lDrive.setPower(-0.1);
            }
            else if(lDrive.getCurrentPosition() > rDrive.getCurrentPosition()){
                rDrive.setPower(-0.1);
                lDrive.setPower(-1.0);
            }else {
                rDrive.setPower(-0.1);
                lDrive.setPower(-0.1);
            }
        }
    }
}

