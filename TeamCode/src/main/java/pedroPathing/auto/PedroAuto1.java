package pedroPathing.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.BezierPoint;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
@Disabled
@Autonomous
public class PedroAuto1 extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;

    // Key Positions
    private final Pose startPose = new Pose(8, 68, Math.toRadians(0));
    private final Pose scorePose1 = new Pose(40.5, 68, Math.toRadians(0));
//    private final Pose backupPose = new Pose(30, 68, Math.toRadians(0));
    private final Pose strafeRightPose = new Pose(35, 35, Math.toRadians(0));
    private final Pose strafeRightPoseControl = new Pose(25, 54, Math.toRadians(0));

    // Sample Pushing Positions
    private final Pose pushSample1 = new Pose(57, 24, Math.toRadians(0));
    private final Pose pushSample1Control = new Pose(70, 30, Math.toRadians(0));
    private final Pose pushSample2 = new Pose(20, 23, Math.toRadians(0));
    //
    private final Pose pushSample3 = new Pose(60, 15, Math.toRadians(0));
    private final Pose pushSample3Control = new Pose(70, 28, Math.toRadians(0));
    //
    private final Pose pushSample4 = new Pose(18, 15, Math.toRadians(0));
    private final Pose controlPoint = new Pose(37, 25, Math.toRadians(0));
    private final Pose pushSample5 = new Pose(8.5, 27, Math.toRadians(0));

    // Pick Up and Scoring Positions
    private final Pose score2Pose = new Pose(40.5, 68, Math.toRadians(0));
    private final Pose score2PoseControl = new Pose(39, 71, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(8.5, 27, Math.toRadians(0));
    private final Pose score3Pose = new Pose(40.5, 67, Math.toRadians(0));
    private final Pose score3PoseControl = new Pose(20, 72, Math.toRadians(0));
    private final Pose pickup3Pose = new Pose(8.5, 27, Math.toRadians(0));
    private final Pose score4Pose = new Pose(40.5, 66, Math.toRadians(0));
    private final Pose score4PoseControl = new Pose(20, 75, Math.toRadians(0));
    private final Pose pickup4Pose = new Pose(10, 27, Math.toRadians(0));

    // Motors & Servos
    private DcMotorEx slider;
    private Servo shortArm, claw;

    private PathChain pushSample1Path, pushSample2Path, pushSample3Path, pushSample4Path, pushSample6Path,score1stSpeciman;
    private Path scorePreload, backup, strafeRight, pushSample5Path;
    // Individual paths instead of the chain
    private Path pushSample7Path, pushSample8Path, getScore1stSpeciman2, score2ndSpeciman,score2ndSpeciman2, score3rdSpeciman, score3rdSpeciman2,pickUp2, pickUp3, pickUp4;

    private ElapsedTime runtime = new ElapsedTime();

    private boolean wait = true;

    @Override
    public void init() {
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        // Initialize Hardware
        claw = hardwareMap.get(Servo.class, "claw");
        shortArm = hardwareMap.get(Servo.class, "shortArm");
        claw.setDirection(Servo.Direction.REVERSE);

        slider = hardwareMap.get(DcMotorEx.class, "slider");
        slider.setDirection(DcMotorSimple.Direction.REVERSE);
        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        claw.setPosition(0.66);
        buildPaths();
    }

    public void sliderHang() {
        slider.setTargetPosition(1722);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slider.setPower(1.0);
    }

    public void sliderPickUp() {
        slider.setTargetPosition(656);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slider.setPower(1.0);
    }

    public void clawClose() {
        claw.setPosition(0.66);
    }

    public void clawOpen() {
        claw.setPosition(0.37);
    }

    public void shortArmHang() {
        shortArm.setPosition(0.1544);
    }

    public void shortArmPickup() {
        shortArm.setPosition(0.9783);
    }

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose1)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(),scorePose1.getHeading()); // Maintain heading


        strafeRight = new Path(new BezierCurve(new Point(scorePose1), new Point(strafeRightPoseControl), new Point(strafeRightPose)));
        strafeRight.setLinearHeadingInterpolation(scorePose1.getHeading(),strafeRightPose.getHeading());
//        backup.setConstantHeadingInterpolation(startPose.getHeading());

        // Create individual paths instead of a chain
        pushSample1Path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(strafeRightPose), new Point(pushSample1Control), new Point(pushSample1)))
                .setLinearHeadingInterpolation(strafeRightPose.getHeading(),pushSample1.getHeading())
                .addPath(new BezierLine(new Point(pushSample1), new Point(pushSample2)))
                .setLinearHeadingInterpolation(pushSample1.getHeading(),pushSample2.getHeading())
                .build();



        pushSample2Path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(pushSample2), new Point(pushSample3Control), new Point(pushSample3)))
                .setLinearHeadingInterpolation(pushSample2.getHeading(),pushSample3.getHeading())
                .addPath(new BezierLine(new Point(pushSample3), new Point(pushSample4)))
                .setLinearHeadingInterpolation(pushSample3.getHeading(),pushSample4.getHeading())
                .build();

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
        pushSample5Path = new Path(new BezierCurve(new Point(pushSample4), /* Control Point */ new Point(controlPoint), new Point(pushSample5)));
        pushSample5Path.setLinearHeadingInterpolation(pushSample4.getHeading(),pushSample5.getHeading());

        score1stSpeciman = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pushSample5), new Point(score2Pose)))
                .setLinearHeadingInterpolation(pushSample5.getHeading(),score2Pose.getHeading())
                .addPath(new BezierLine(new Point(score2Pose), new Point(score2PoseControl)))
                .setLinearHeadingInterpolation(score2Pose.getHeading(),score2PoseControl.getHeading())
                .build();

        pickUp2 = new Path(new BezierLine(new Point(score2Pose), new Point(pickup2Pose)));
        pickUp2.setLinearHeadingInterpolation(score2Pose.getHeading(),pickup2Pose.getHeading());


        score2ndSpeciman = new Path(new BezierLine(new Point(pickup2Pose), new Point(score3Pose)));
        score2ndSpeciman.setLinearHeadingInterpolation(pickup2Pose.getHeading(),score3Pose.getHeading());


        pickUp3 = new Path(new BezierLine(new Point(score3Pose), new Point(pickup3Pose)));
        pickUp3.setLinearHeadingInterpolation(score3Pose.getHeading(),pickup3Pose.getHeading());

        score3rdSpeciman = new Path(new BezierLine(new Point(pickup3Pose), new Point(score4Pose)));
        score3rdSpeciman.setLinearHeadingInterpolation(pickup3Pose.getHeading(),score4Pose.getHeading());

        pickUp4 = new Path(new BezierLine(new Point(score4Pose), new Point(pickup4Pose)));
        pickUp4.setLinearHeadingInterpolation(score4Pose.getHeading(),pickup4Pose.getHeading());
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
        telemetry.addData("Path State", pathState);
        telemetry.addData("Position", follower.getPose().toString());
        telemetry.update();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Move from start to scoring position
                runtime.reset();
                while(wait) {
                    shortArmHang();
                    sliderHang();
                    if(runtime.seconds() > 0.5){
                        break;
                    }
                }
                follower.followPath(scorePreload, true);
                setPathState(1);
                break;

            case 1: // Back up after scoring
                if (!follower.isBusy()) {
                    runtime.reset();
                    while(wait){
                        clawOpen();
                        follower.followPath(strafeRight);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmPickup();
                    sliderPickUp();
                    setPathState(2);
                }
                break;

            case 2: // Strafe right before pushing samples
                if (!follower.isBusy()) {
                    shortArmPickup();
                    sliderPickUp();
                    follower.followPath(pushSample1Path);
                    setPathState(3);
                }
                break;

            case 3: // Follow first push sample path
                if (!follower.isBusy()) {
                    follower.followPath(pushSample2Path);
                    setPathState(4);
                }
                break;


            case 4: // Follow fourth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.7);
                    follower.followPath(pushSample5Path, true);
                    setPathState(5);
                }
                break;

            case 5: // Follow fifth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    runtime.reset();
                    while(wait){
                        clawClose();
                        if(runtime.seconds() > 0.3){
                            break;
                        }
                    }
                    runtime.reset();
                    while(wait){
                        sliderHang();
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmHang();
                    follower.followPath(score1stSpeciman,true); // End of sequence

                    setPathState(6);

                }
                break;

            case 6: // Follow sixth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(1.0);
                    clawOpen();
                    runtime.reset();
                    while(wait){
                        follower.followPath(pickUp2,true);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    sliderPickUp();
                    shortArmPickup();
                    setPathState(7);
                }
                break;

            case 7: // Follow seventh push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    runtime.reset();
                    while(wait){
                        clawClose();
                        if(runtime.seconds() > 0.3){
                            break;
                        }
                    }
                    runtime.reset();
                    while(wait){
                        sliderHang();
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmHang();
                    follower.followPath(score2ndSpeciman,true); // End of sequence
                    setPathState(8);
                }
                break;

            case 8: // score 1st
                if (!follower.isBusy()) {
                    follower.setMaxPower(1.0);
                    clawOpen();
                    runtime.reset();
                    while(wait){
                        follower.followPath(pickUp3,true);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    sliderPickUp();
                    shortArmPickup();
                    setPathState(9); // End of sequence
                }
                break;
            case 9: // Follow final curve path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    runtime.reset();
                    while(wait){
                        clawClose();
                        if(runtime.seconds() > 0.3){
                            break;
                        }
                    }
                    runtime.reset();
                    while(wait){
                        sliderHang();
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmHang();
                    follower.followPath(score3rdSpeciman,true); // End of sequence
                    setPathState(10); // End of sequence
                }
                break;
            case 10: // Follow final curve path
                if (!follower.isBusy()) {
                    follower.setMaxPower(1.0);
                    clawOpen();
                    runtime.reset();
                    while(wait){
                        follower.followPath(pickUp4,true);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    sliderPickUp();
                    shortArm.setPosition(0.9494);
                    setPathState(-1); // End of sequence
                }
                break;
            case 13: // Follow final curve path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    runtime.reset();
                    while(wait){
                        clawClose();
                        if(runtime.seconds() > 0.3){
                            break;
                        }
                    }
                    runtime.reset();
                    while(wait){
                        sliderHang();
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmHang();
                    follower.followPath(score3rdSpeciman,true); // End of sequence
                    setPathState(14); // End of sequence
                }
                break;
            case 14: // Follow final curve path
                if (!follower.isBusy()) {
                    runtime.reset();
                    while(wait){
                        clawClose();
                        if(runtime.seconds() > 0.3){
                            break;
                        }
                    }
                    runtime.reset();
                    while(wait){
                        sliderHang();
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    shortArmHang();
                    follower.followPath(score3rdSpeciman,true);
                    setPathState(-1); // End of sequence
                }
                break;


        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}