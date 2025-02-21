package qual1teleop;//package org.firstinspires.ftc.teamcode.qual1archive.qual1teleop;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.controller.PIDController;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//@Disabled
//@Config
//@TeleOp
//public class rsliderPID extends OpMode{
//    private PIDController controller;
//    public static double p = 0, i = 0, d = 0;ee
//    public static double f = 0;
//
//    public static int target = 104;
//
//    private final double ticks_in_degree = 1993.6 / 180.0;
//    private DcMotorEx rslider;
//    @Override
//    public void init(){
//        controller = new PIDController(p, i, d);
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        rslider = hardwareMap.get(DcMotorEx.class, "rslider");
//
//    }
//    @Override
//    public void loop(){
//        controller.setPID(p, i, d);
//        int armPos = rslider.getCurrentPosition();
//        double pid = controller.calculate(armPos, target);
//        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;
//        double power = pid + ff;
//        rslider.setPower(power);
//        telemetry.addData("pos",armPos);
//        telemetry.addData("target", target);
//        telemetry.update();
//
//    }
//}
