package teleop;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StateMachine{
    public static final double CLAW_CLOSE = 0.65;
    public static final double CLAW_OPEN = 0.37;
    public static final double UP = 0.5167;
    public static final double RDOWN = 0.0289;
    public static final double RUP = 0.7044;
    public static final double HRMIDDLE = 0.695;
    public static final double HRLEFT = 0.3539;
    public static final boolean wait = true;
    private ElapsedTime runtime = new ElapsedTime();
    public static Telemetry telemetry;
    protected HardwareMap hwMap;

    public StateMachine(HardwareMap hwMap) {
        this.hwMap = hwMap;
    }

    public enum LiftState {
        DELIVERY,
        BACK,
        WALL,
        MIDDLE,
        SUB,
        BASKET,
        EXTEND,
        HOME,
        PICKUP,
        HANGING,
        HANGRESET,
        EXCHANGE,
        EXTRACT,
    }

    public void setState(LiftState state) {
        this.liftState = state;
    }

    public LiftState getState() {
        return this.liftState;
    }

    private LiftState liftState;
    private Servo claw, shortArm, extender, rotator, outClaw;
    private DcMotor frontLeft, frontRight, backLeft, backRight, slider, hslider, aslider;

    public void init() {
        frontLeft = hwMap.get(DcMotorEx.class, "leftFront");
        frontRight = hwMap.get(DcMotorEx.class, "rightFront");
        backLeft = hwMap.get(DcMotorEx.class, "leftBack");
        backRight = hwMap.get(DcMotorEx.class, "rightBack");
        slider = hwMap.get(DcMotorEx.class, "slider");
        hslider = hwMap.get(DcMotorEx.class, "hslider");
        aslider = hwMap.get(DcMotorEx.class, "aslider");
        claw = hwMap.get(Servo.class, "claw");
        shortArm = hwMap.get(Servo.class, "shortArm");
        extender = hwMap.get(Servo.class, "extender");
        rotator = hwMap.get(Servo.class, "rotator");
        outClaw = hwMap.get(Servo.class, "outClaw");
        claw.setDirection(Servo.Direction.REVERSE);
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
//        shortArm.setDirection(Servo.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hslider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hslider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        aslider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aslider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.startTime();


    }

    public void runState(LiftState previousState) {
        switch (liftState) {
            case BACK:
                back();
                break;
            case WALL:
                wall();
                break;
            case MIDDLE:
                middle();
                break;
            case SUB:
                sub();
                break;
            case EXTEND:
                extend();
                break;
            case BASKET:
                basket();
                break;
            case PICKUP:
                pickup();
                break;
            case HANGING:
                hanging();
                break;
            case HANGRESET:
                hangingreset();
                break;
            case EXCHANGE:
                exchange();
                break;
            case EXTRACT:
                extract();
                break;
            default:
                break;
        }
    }

    public void extract(){
        extender.setPosition(0.2844);
        shortArm.setPosition(0.0322);
        moveToTargetStage(0,1.0);
        moveToTargethStage(0,1.0);
    }

    public void hanging(){
        moveToTargetaStage(10197,1.0);
    }

    public void hangingreset(){
        moveToTargetaStage(6916,1.0);
    }

    public void pickup(){
        runtime.reset();
        while (wait) {
            extender.setPosition(0.0667);
            if (runtime.seconds() > 0.5)   {
                break;
            }
        }
        runtime.reset();
        while (wait) {
            outClaw.setPosition(0.3578);
            if (runtime.seconds() > 0.5)   {
                break;
            }
        }
        moveToTargethStage(0,1.0);
        outClaw.setPosition(0.3578);
        extender.setPosition(0.2844);

    }
    public void basket(){
        shortArm.setPosition(0.7361);
        moveToTargetStage(3548,1.0);
        moveToTargethStage(0,1.0);
    }
    public void back(){
        moveToTargethStage(0,1.0);
//        moveToTargethStage(-130,1.0);
        outClaw.setPosition(0.3578);
        extender.setPosition(0.2844);
    }

    public void wall(){
        moveToTargetStage(758, 1.0);
        shortArm.setPosition(0.9856);
        moveToTargethStage(0, 1.0);
        extender.setPosition(0.73883);
        claw.setPosition(CLAW_OPEN);
        //0.8583
        //1437
    }
    public void middle(){
        moveToTargethStage(-103,1.0);
    }
    public void sub(){
        moveToTargetStage(1722, 1.0);
        shortArm.setPosition(0.1306);
        hslider.setPower(0.0);
    }

    public void exchange(){
        outClaw.setPosition(0.3578);
        extender.setPosition(0.7122);
        rotator.setPosition(0.0);
        shortArm.setPosition(0.95);
        moveToTargethStage(-1776,1.0);
        moveToTargetStage(0,1.0);
        claw.setPosition(CLAW_OPEN);
    }
    public void extend() {
        shortArm.setPosition(0.0322);
        moveToTargetStage(0,1.0);
        extender.setPosition(0.1411);
        rotator.setPosition(0.0);
        runtime.reset();
        while (wait) {
            moveToTargethStage(-2961,1.0);
            if (runtime.seconds() > 0.5)   {
                break;
            }
        }
        outClaw.setPosition(0.0);
    }

    public void moveToTargetStage(int target, double power) {
        slider.setPower(power);
        slider.setTargetPosition(target);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void moveToTargethStage(int target, double power) {
        hslider.setPower(power);
        hslider.setTargetPosition(target);
        hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void moveToTargetaStage(int target, double power) {
        aslider.setPower(power);
        aslider.setTargetPosition(target);
        aslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
