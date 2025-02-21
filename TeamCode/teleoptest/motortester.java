package teleoptest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class motortester extends OpMode {
    //diff version for diff chassis
    private DcMotor slider;
    private Servo claw, shortArm;
    //rslider;
    //    private Servo claw, secondStage, intakeArm;
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        claw = hardwareMap.get(Servo.class, "claw");
        shortArm = hardwareMap.get(Servo.class, "shortArm");
        slider = hardwareMap.get(DcMotorEx.class, "slider");
//        rslider = hardwareMap.get(DcMotorEx.class, "rslider");
        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rslider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rslider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        claw.setPosition(0);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
        if(gamepad2.y){
            shortArm.setPosition(1.0);
        }
        if(gamepad2.x){
            shortArm.setPosition(0.1378);
        }
        if(gamepad2.a){
            shortArm.setPosition(0.0811);
        }
        if(gamepad2.b){
            claw.setPosition(1.0);
        }
        //-3815
        //-1390


        if(gamepad2.dpad_right){
            claw.setPosition(0.37);
        }
//        if(gamepad2.dpad_up){
//            slider.setPower(1);
//            slider.setTargetPosition(-4100);
//            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            shortArm.setPosition(0.1378);
//        }
        if(gamepad2.dpad_down){
            slider.setPower(1);
            slider.setTargetPosition(slider.getCurrentPosition() + 50);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad2.dpad_up){
            slider.setPower(1);
            slider.setTargetPosition(slider.getCurrentPosition() - 50);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

//        telemetry.addData("rotating slider", rslider.getCurrentPosition());
        telemetry.addData("slider", slider.getCurrentPosition());
        telemetry.update();
    }

}