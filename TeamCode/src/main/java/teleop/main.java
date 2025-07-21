package teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "teleopEXTREME")
//my name is zoey
public class main extends OpMode {
    //diff version for diff chassis
    //rs = 113
    //S = IDK
    public static final double CLAW_CLOSE = 0.66;
    public static final double CLAW_OPEN = 0.37;
    public static final double UP = 0.5167;
    public static final double RDOWN = 0.0289;
    public static final double RUP = 0.7044;
    public static final double HRMIDDLE = 0.695;
    public static final double HRLEFT = 0.3539;
    boolean running;
    //    public static final double HRRIGHT = 1.0;
    boolean call = true;
    boolean call1 = true;
    boolean call2 = true;
    boolean call3 = true;
    boolean LPushed = false;
    boolean RPushed = false;
    boolean hanging = false;
    boolean extended = false;
    StateMachine stateMachine;
    private Servo claw, shortArm, extender, rotator, outClaw;
    private DcMotor frontLeft, frontRight, backLeft, backRight, slider, hslider, aslider;
    boolean aButtonPreviousState = false;
    boolean slowModeActive = false;
    static final double MOTOR_TICKS_COUNT = 537.7;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        stateMachine = new StateMachine(hardwareMap);
        stateMachine.init();
        frontLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "rightFront");
        backLeft = hardwareMap.get(DcMotorEx.class, "leftBack");
        backRight = hardwareMap.get(DcMotorEx.class, "rightBack");
        slider = hardwareMap.get(DcMotorEx.class, "slider");
        hslider = hardwareMap.get(DcMotorEx.class, "hslider");
        aslider = hardwareMap.get(DcMotorEx.class, "aslider");
        claw = hardwareMap.get(Servo.class, "claw");
        shortArm = hardwareMap.get(Servo.class, "shortArm");
        extender = hardwareMap.get(Servo.class, "extender");
        rotator = hardwareMap.get(Servo.class, "rotator");
        outClaw = hardwareMap.get(Servo.class, "outClaw");

        claw.setDirection(Servo.Direction.REVERSE);
//        shortArm.setDirection(Servo.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
//        rclaw.setPosition(UP);
//        claw.setPosition(CLAW_CLOSE);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        StateMachine.LiftState liftState = stateMachine.getState();
//        if (running){
////            rclaw.setPosition(RUP);
//            running = false;
//
//        }

        if (gamepad2.dpad_right) {
            hslider.setPower(1);
            hslider.setTargetPosition(hslider.getCurrentPosition() + 50);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }


        if (gamepad2.dpad_left) {
            hslider.setPower(1);
            hslider.setTargetPosition(hslider.getCurrentPosition() - 50);
            hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad2.b) {
            shortArm.setPosition(shortArm.getPosition() + 0.0005);
        }
        if (gamepad2.x) {
            shortArm.setPosition(shortArm.getPosition() - 0.0005);

        }
//
//            if (gamepad2.y) {
//                hslider.setPower(1);
//                hslider.setTargetPosition(hslider.getCurrentPosition() - 10);
//                hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }
//            if (gamepad2.a) {
//                hslider.setPower(1);
//                hslider.setTargetPosition(hslider.getCurrentPosition() + 10);
//                hslider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }

            //extender: 0.0 shortarm: 0.0406
            if (gamepad2.dpad_up) {
                extender.setPosition(extender.getPosition() + 0.0005);
            }

            if (gamepad2.dpad_down) {
                extender.setPosition(extender.getPosition() - 0.0005);
            }


//        if(hslider.getCurrentPosition() == 0.0){
//            hslider.setPower(0.0);
//        }
            double drive = -gamepad1.left_stick_y * 0.8;
            double strafe = -gamepad1.left_stick_x * 0.8;
            double turn = -gamepad1.right_stick_x * 0.8;

            double frontLeftPower = Range.clip(drive - strafe - turn, -1, 1);
            double frontRightPower = Range.clip(drive + strafe + turn, -1, 1);
            double backLeftPower = Range.clip(-drive - strafe + turn, -1, 1);
            double backRightPower = Range.clip(drive - strafe + turn, -1, 1);

            //0.37
            //1650

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

//            if (gamepad1.y) {
//                stateMachine.setState(StateMachine.LiftState.SUB);
//                stateMachine.runState(liftState);
//                //speciman hang
//
//            }
//            if (gamepad1.a) {
//                stateMachine.setState(StateMachine.LiftState.WALL);
//                stateMachine.runState(liftState);
//                //speciman pickup
//            }
            if (gamepad1.dpad_left) {
                stateMachine.setState(StateMachine.LiftState.EXCHANGE);
                stateMachine.runState(liftState);
                //switches between claws
            }
            if (gamepad1.b) {
                stateMachine.setState(StateMachine.LiftState.PICKUP);
                stateMachine.runState(liftState);
                //move extender down so it can reach the pixel
            }
            if (gamepad1.dpad_right) {
                outClaw.setPosition(0.0);
                claw.setPosition(CLAW_CLOSE);
                //delivery between claws fin
            }
            if (gamepad1.dpad_up) {
                stateMachine.setState(StateMachine.LiftState.BASKET);
                stateMachine.runState(liftState);
                //go to high basket
            }
            if (gamepad1.dpad_down) {
                //nothing
            }

            if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                LPushed = true;
            }
            if (LPushed && !gamepad1.left_bumper) {
                LPushed = false;
                if (call) {
                    claw.setPosition(CLAW_OPEN);
                    call = false;
                    return;
                } else if (!call) {
                    claw.setPosition(CLAW_CLOSE);
                    call = true;
                    return;
                }
            }


            if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                RPushed = true;
            }
            if (RPushed && !gamepad1.right_bumper) {
                RPushed = false;
                if (call1) {
                    rotator.setPosition(0.0);
                    call1 = false;
                    return;
                } else if (!call1) {
                    rotator.setPosition(0.33);
                    call1 = true;
                    return;
                }
            }


//        if (gamepad1.dpad_down) {
//            hanging = true;
//        }
//        if (hanging && !gamepad1.dpad_down) {
//            hanging = false;
//            if (call2) {
//                stateMachine.setState(StateMachine.LiftState.HANGING);
//                stateMachine.runState(liftState);
//                call2 = false;
//                return;
//            } else if (!call2) {
//                stateMachine.setState(StateMachine.LiftState.HANGRESET);
//                stateMachine.runState(liftState);
//                call2 = true;
//                return;
//            }
//        }

        if (gamepad1.x) {
            extended = true;
        }
        if (extended && !gamepad1.x) {
            extended = false;
            if (call3) {
                stateMachine.setState(StateMachine.LiftState.EXTEND);
                stateMachine.runState(liftState);
                rotator.setPosition(0.0);
                call3 = false;
                return;
            } else if (!call3) {
                stateMachine.setState(StateMachine.LiftState.EXTRACT);
                stateMachine.runState(liftState);
                call3 = true;
                return;
            }
        }

            //servos
        telemetry.addData("shortArm", shortArm.getPosition());
            telemetry.addData("rotator", rotator.getPosition());
            telemetry.addData("outClaw", outClaw.getPosition());
            telemetry.addData("claw", claw.getPosition());
            telemetry.addData("extender", extender.getPosition());
            //sliders
            telemetry.addData("hanger", aslider.getCurrentPosition());
            telemetry.addData("slider", slider.getCurrentPosition());
            telemetry.addData("hslider", hslider.getCurrentPosition());
            telemetry.update();
        }
    }
