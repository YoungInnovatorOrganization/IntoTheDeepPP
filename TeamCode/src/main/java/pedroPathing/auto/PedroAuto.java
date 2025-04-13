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

@Autonomous(name = "Specimen Auto", group = "Competition")
public class PedroAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;

    // Key Positions
    private final Pose startPose = new Pose(8, 68, Math.toRadians(0));
    private final Pose scorePose1 = new Pose(42, 68, Math.toRadians(0));
    //    private final Pose backupPose = new Pose(30, 68, Math.toRadians(0));
    private final Pose backUpA = new Pose(32, 68, Math.toRadians(0));
    private final Pose strafeRightPose = new Pose(55, 34, Math.toRadians(0));
    private final Pose strafeRightPoseControl = new Pose(30, 34, Math.toRadians(0));
    Pose strafeRightPoseControl2 = new Pose(40, 35, Math.toRadians(0));

    // Sample Pushing Positions
    private final Pose pushSample1 = new Pose(57, 24, Math.toRadians(0));
    private final Pose pushSample1Control = new Pose(57, 30, Math.toRadians(0));
    private final Pose pushSample1Control2 = new Pose(60, 30, Math.toRadians(0));
    private final Pose pushSample2 = new Pose(20, 23, Math.toRadians(0));
    //
    private final Pose pushSample3 = new Pose(60, 15, Math.toRadians(0));
    private final Pose pushSample3Control = new Pose(70, 28, Math.toRadians(0));
    private final Pose backUp = new Pose(25, 23, Math.toRadians(0));
    private final Pose goIn = new Pose(8, 23, Math.toRadians(0));

    //
//    private final Pose pushSample4 = new Pose(18, 15, Math.toRadians(0));
//    private final Pose controlPoint = new Pose(37, 25, Math.toRadians(0));
//    private final Pose pushSample5 = new Pose(8, 27, Math.toRadians(0));

    // Pick Up and Scoring Positions
    private final Pose score2Pose = new Pose(25, 73, Math.toRadians(0));
    private final Pose score2PoseControl = new Pose(40.8, 73, Math.toRadians(0));
    private final Pose pickup2Pose1 = new Pose(20, 27, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(8, 27, Math.toRadians(0));
    private final Pose score3Pose = new Pose(25, 75, Math.toRadians(0));
    private final Pose score3PoseControl = new Pose(40.8, 75, Math.toRadians(0));
    private final Pose pickup3Pose = new Pose(7, 27, Math.toRadians(0));
    //
    private final Pose score4Pose = new Pose(42, 67, Math.toRadians(0));
    private final Pose score4PoseControl = new Pose(36, 67, Math.toRadians(0));
    private final Pose pickup4Pose = new Pose(15, 27, Math.toRadians(0));

    // Motors & Servos
    private DcMotorEx slider;
    private Servo shortArm, claw;

    private PathChain pushSample1Path;
    private PathChain pushSample2Path;
    private PathChain pushSample3Path;
    private PathChain pushSample4Path;
    private PathChain pushSample6Path;
    private Path backUpa, scorePreload, backup, goin, strafeRight, pushSample5Path,score1stSpeciman, score1stSpeciman1, score2ndSpeciman,score2ndSpeciman1;
    // Individual paths instead of the chain
    private Path pushSample7Path;
    private Path pushSample8Path;
    private Path getScore1stSpeciman2;

    private Path score2ndSpeciman2;
    private Path score3rdSpeciman;
    private Path score3rdSpeciman2;
    private Path pickUp2, pickUp21;
    private Path pickUp3, pickUp31;
    private Path pickUp4;

    private ElapsedTime runtime = new ElapsedTime();

    private boolean wait = true;

    @Override
    public void init() {
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        // Initialize Hardware
        claw = hardwareMap.get(Servo.class, "claw");
        shortArm = hardwareMap.get(Servo.class, "shortArm");
        shortArm.setDirection(Servo.Direction.REVERSE);
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
        slider.setTargetPosition(758);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slider.setPower(1.0);
    }

    public void sliderDown() {
        slider.setTargetPosition(0);
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
        shortArm.setPosition(0.1306);
    }

    public void shortArmPickup() {
        shortArm.setPosition(0.9856);
    }

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose1)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(),scorePose1.getHeading()); // Maintain heading
        scorePreload.setPathEndTimeoutConstraint(100);

        backUpa = new Path(new BezierLine(new Point(scorePose1), new Point(backUpA)));
        backUpa.setLinearHeadingInterpolation(scorePose1.getHeading(),backUpA.getHeading());
        backUpa.setPathEndTimeoutConstraint(100);

        strafeRight = new Path(new BezierCurve(new Point(backUpA), new Point(strafeRightPoseControl), new Point(strafeRightPoseControl2), new Point(strafeRightPose)));
        strafeRight.setLinearHeadingInterpolation(backUpA.getHeading(),strafeRightPose.getHeading());
//        backup.setConstantHeadingInterpolation(startPose.getHeading());

        // Create individual paths instead of a chain
        pushSample1Path = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(strafeRightPose), new Point(pushSample1Control), new Point(pushSample1Control2), new Point(pushSample1)))
                .setLinearHeadingInterpolation(strafeRightPose.getHeading(),pushSample1.getHeading())
                .addPath(new BezierLine(new Point(pushSample1), new Point(pushSample2)))
                .setLinearHeadingInterpolation(pushSample1.getHeading(),pushSample2.getHeading())
                .build();

        backup = new Path(new BezierLine(new Point(pushSample2), new Point(backUp)));
        backup.setLinearHeadingInterpolation(pushSample2.getHeading(),backUp.getHeading());

        goin = new Path(new BezierLine(new Point(backUp), new Point(goIn)));
        goin.setLinearHeadingInterpolation(backUp.getHeading(),goIn.getHeading());
        goin.setPathEndTimeoutConstraint(500);

//        pushSample2Path = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(pushSample2), new Point(pushSample3Control), new Point(pushSample3)))
//                .setLinearHeadingInterpolation(pushSample2.getHeading(),pushSample3.getHeading())
//                .addPath(new BezierLine(new Point(pushSample3), new Point(pushSample4)))
//                .setLinearHeadingInterpolation(pushSample3.getHeading(),pushSample4.getHeading())
//                .build();
//
//        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
//        pushSample5Path = new Path(new BezierCurve(new Point(pushSample4), /* Control Point */ new Point(controlPoint), new Point(pushSample5)));
//        pushSample5Path.setLinearHeadingInterpolation(pushSample4.getHeading(),pushSample5.getHeading());

        score1stSpeciman = new Path(new BezierLine(new Point(goIn), new Point(score2Pose)));
        score1stSpeciman.setLinearHeadingInterpolation(goIn.getHeading(),score2Pose.getHeading());

        score1stSpeciman1 = new Path (new BezierLine(new Point(score2Pose), new Point(score2PoseControl)));
        score1stSpeciman1.setLinearHeadingInterpolation(score2Pose.getHeading(),score2PoseControl.getHeading());
        score1stSpeciman1.setPathEndTimeoutConstraint(100);

        pickUp21 = new Path(new BezierLine(new Point(score2Pose), new Point(pickup2Pose1)));
        pickUp21.setLinearHeadingInterpolation(score2Pose.getHeading(),pickup2Pose1.getHeading());

        pickUp2 = new Path(new BezierLine(new Point(pickup2Pose1), new Point(pickup2Pose)));
        pickUp2.setLinearHeadingInterpolation(pickup2Pose1.getHeading(),pickup2Pose.getHeading());
        pickUp2.setPathEndTimeoutConstraint(500);

        score2ndSpeciman = new Path (new BezierLine(new Point(pickup2Pose), new Point(score3Pose)));
        score2ndSpeciman.setLinearHeadingInterpolation(pickup2Pose.getHeading(),score2PoseControl.getHeading());
        score2ndSpeciman.setPathEndTimeoutConstraint(100);

        score2ndSpeciman1 = new Path (new BezierLine(new Point(score3Pose), new Point(score3PoseControl)));
        score2ndSpeciman1.setLinearHeadingInterpolation(score2PoseControl.getHeading(),score2Pose.getHeading());

//
//        pickUp3 = new Path(new BezierLine(new Point(score3Pose), new Point(pickup3Pose)));
//        pickUp3.setLinearHeadingInterpolation(score3Pose.getHeading(),pickup3Pose.getHeading());
//
//        score3rdSpeciman = new Path(new BezierLine(new Point(pickup3Pose), new Point(score4Pose)));
//        score3rdSpeciman.setLinearHeadingInterpolation(pickup3Pose.getHeading(),score4Pose.getHeading());

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
                        follower.followPath(backUpa);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    setPathState(2);
                }
                break;

            case 2: // Back up after scoring
                if (!follower.isBusy()) {
                    runtime.reset();
                    while(wait){
                        shortArmPickup();
                        sliderPickUp();
                        if(runtime.seconds() > 1.0){
                            break;
                        }
                    }
                    follower.followPath(strafeRight);
                    setPathState(3);
                }
                break;

            case 3: // Strafe right before pushing samples
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.7);
                    shortArmPickup();
                    sliderPickUp();
                    follower.followPath(pushSample1Path);
                    setPathState(4);
                }
                break;

            case 4: // Follow first push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    follower.followPath(backup);
                    setPathState(5);
                }
                break;


            case 5: // Follow fourth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.7);
                    follower.followPath(goin,true);
                    setPathState(6);
                }
                break;

            case 6: // Follow fifth push sample path
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

                    setPathState(7);

                }
                break;
            case 7: // Follow sixth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.5);
                    follower.followPath(score1stSpeciman1,true);
                    setPathState(8);
                }
                break;
            case 8: // Follow sixth push sample path
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.9);
                    clawOpen();
                    runtime.reset();
                    while(wait){
                        follower.followPath(pickUp21,true);
                        if(runtime.seconds() > 0.5){
                            break;
                        }
                    }
                    sliderPickUp();
                    shortArmPickup();
                    setPathState(9);
                }
                break;

            case 9: // Follow sixth push sample path
                if (!follower.isBusy()) {
                    follower.followPath(pickUp2,true);
                    setPathState(10);
                }
                break;

            case 10: // Follow seventh push sample path
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
                    setPathState(11);
                }
                break;

            case 11: // Follow sixth push sample path
                if (!follower.isBusy()) {
                    follower.followPath(score2ndSpeciman1,true);
                    setPathState(12);
                }
                break;

            case 12: // score 1st
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
                    sliderDown();
                    shortArm.setPosition(0.9494);
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