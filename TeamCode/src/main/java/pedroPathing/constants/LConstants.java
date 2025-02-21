package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        ThreeWheelIMUConstants.forwardTicksToInches = 0.0006;
        ThreeWheelIMUConstants.strafeTicksToInches = 0.0006;
        ThreeWheelIMUConstants.turnTicksToInches = 0.002;
        ThreeWheelIMUConstants.leftY = 6.5;
        ThreeWheelIMUConstants.rightY = -6.5;
        ThreeWheelIMUConstants.strafeX = 7.7;
        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = "rightBack";
        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = "rightFront";
        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = "leftBack";
        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
    }
}




