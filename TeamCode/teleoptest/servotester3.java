package teleoptest;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class servotester3 extends OpMode {
    public static final double CLAW_CLOSE = 0.65;
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
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
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
    //extend:
    //-4530, 0.1606
    //pick up:
    //0.0739
    //0.2844 <-- to go over safely with sample

    //extender: 0.6767 hslider: -2045 shortArm: 0.0361

    @Override
    public void loop() {
        //short arm back: 0.7394

        if(gamepad2.dpad_up){
            shortArm.setPosition(shortArm.getPosition() + 0.0005);
        }

        if(gamepad2.dpad_down){
            shortArm.setPosition(shortArm.getPosition() - 0.0005);
        }

        if(gamepad2.x){
            shortArm.setPosition(0.9656);
            slider.setPower(1);
            slider.setTargetPosition(2160);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
//        //0.0222 630
        if(gamepad2.dpad_right){
            slider.setPower(1);
            slider.setTargetPosition(slider.getCurrentPosition() + 50);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad2.dpad_left){
            slider.setPower(1);
            slider.setTargetPosition(slider.getCurrentPosition() - 50);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if(gamepad2.x){
            claw.setPosition(CLAW_CLOSE);
        }

        if(gamepad2.y){
            claw.setPosition(CLAW_OPEN);
        }
        //shortArm.setPosition(0.3128);
        //-5475
        //hai
//        if(gamepad2.dpad_left){
//            extender.setPosition(extender.getPosition() + 0.0005);
//        }
//        //
//        if(gamepad2.dpad_right){
//            extender.setPosition(extender.getPosition() - 0.0005);
//        }
//        if(gamepad2.a){
//            shortArm.setPosition(1.0);
//            extender.setPosition(0.2844);
//        }
//        if(gamepad2.b){
//            shortArm.setPosition(shortArm.getPosition() - 0.0005);
//        }

        telemetry.addData("slider",slider.getCurrentPosition());
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