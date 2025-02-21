package teleoptest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class servotester extends OpMode {
    public static final double CLAW_CLOSE = 0.95;
    public static final double CLAW_OPEN = 0.37;
    //diff version for diff chassis
    private Servo outClaw, extender, rotator, shortArm, claw;
    private DcMotor hslider, slider;
    //    private Servo claw, secondStage, shortArm;
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        hslider = hardwareMap.get(DcMotorEx.class, "hslider");
        slider = hardwareMap.get(DcMotorEx.class, "slider");
        outClaw = hardwareMap.get(Servo.class, "outClaw");
        claw = hardwareMap.get(Servo.class, "claw");
        extender = hardwareMap.get(Servo.class, "extender");
        rotator = hardwareMap.get(Servo.class, "rotator");
        shortArm = hardwareMap.get(Servo.class, "shortArm");
        claw.setDirection(Servo.Direction.REVERSE);
        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hslider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hslider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        claw.setDirection(Servo.Direction.REVERSE);
//        rclaw.setDirection(Servo.Direction.REVERSE);
//        claw.setPosition(0);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
//        if(gamepad1.dpad_up){
//            claw.setPosition(claw.getPosition() + 0.0005);
//        }
//        if(gamepad1.dpad_down){
//            claw.setPosition(claw.getPosition() - 0.0005);
//        } 0.9817
//        if(gamepad2.a){
//            claw.setPosition(0.37);
//            shortArm.setPosition(0.0811);
//        }
//        if(gamepad2.b){
//            claw.setPosition(1.0);
//        }
//        1
//        0.37

        // 0.1378
        //0.0811
        //outclaw open = 0 close = 0.3517
        //extender straiht = 0.0472
        //rotator 0.0 0.3439

        if(gamepad2.y){
            outClaw.setPosition(0.3517);
            extender.setPosition(0.6917);
            rotator.setPosition(0.0);
            shortArm.setPosition(0.025);
            claw.setPosition(CLAW_OPEN);
            //extender = 0.0217
        }
        if(gamepad2.a) {
            //, 1.0);
            //        shortArm.setPosition(0.08
            extender.setPosition(0.0672);
            rotator.setPosition(0.0);
            hslider.setPower(1);
            hslider.setTargetPosition(-2923);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            shortArm.setPosition(0.95);
            outClaw.setPosition(0.0);
            //-1737
        }
        if(gamepad2.right_bumper){
            extender.setPosition(0.0217);
            outClaw.setPosition(0.3517);
        }
        if(gamepad2.left_bumper){
            slider.setPower(1);
            slider.setTargetPosition(-10);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hslider.setPower(1);
            hslider.setTargetPosition(-10);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if(gamepad2.dpad_right){
            claw.setPosition(CLAW_CLOSE);
            outClaw.setPosition(0.0);
        }
        if(gamepad2.dpad_left) {
            shortArm.setPosition(shortArm.getPosition() - 0.0005);
        }

//        if(gamepad2.dpad_right){
//            rotator.setPosition(rotator.getPosition() + 0.0005);
//        }
//        if(gamepad2.dpad_left){
//            rotator.setPosition(rotator.getPosition() - 0.0005);
//        }
        if(gamepad2.dpad_up){
            outClaw.setPosition(0.0);
        }
        //-2923 pickup
        //swap outclaw = 0.3489 extender = 0.6917 rotator = 0/0 hslider = -1736
        if(gamepad2.dpad_down){
            outClaw.setPosition(0.3517);
        }
        if(gamepad2.b){
            hslider.setPower(1);
            hslider.setTargetPosition(hslider.getCurrentPosition() + 50);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad2.x){
            hslider.setPower(1);
            hslider.setTargetPosition(hslider.getCurrentPosition() - 50);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        //0.2283
//        if(gamepad2.dpad_left){
//            airplane.setPosition(airplane.getPosition() + 0.0005);
//        }
//        if(gamepad2.dpad_right){
//            airplane.setPosition(airplane.getPosition() - 0.0005);
//        }
//        if(gamepad2.dpad_up){
////            dropBox.setPosition(dropBox.getPosition() + 0.0005);
//            intakeArm.setPosition(intakeArm.getPosition() + 0.0005);
//        }
//        if(gamepad2.dpad_down){
////            dropBox.setPosition(dropBox.getPosition() - 0.0005);
//            intakeArm.setPosition(intakeArm.getPosition() - 0.0005);
//        }
        telemetry.addData("claw",claw.getPosition());
        telemetry.addData("outClaw",outClaw.getPosition());
        telemetry.addData("extender",extender.getPosition());
        telemetry.addData("rotator",rotator.getPosition());
        telemetry.addData("hslider",hslider.getCurrentPosition());
        telemetry.addData("as;dlfkjas;lfjak;sldjkfa;sdzlkjfl", shortArm.getPosition());

//        telemetry.addData("rotating horizontal claw",hrclaw.getPosition());
        telemetry.update();
    }

}