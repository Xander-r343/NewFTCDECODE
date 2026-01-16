package org.firstinspires.ftc.teamcode.New.Subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class OdometrySubsystem implements Subsystem {
    public static OdometrySubsystem INSTANCE = new OdometrySubsystem();
    public OdometrySubsystem() {  }

    public GoBildaPinpointDriver pinpoint;

    public Pose getPose() { return new Pose(pinpoint.getPosX(DistanceUnit.INCH), pinpoint.getPosY(DistanceUnit.INCH), pinpoint.getHeading(AngleUnit.DEGREES)); }
    public Pose getvelocity() { return new Pose(pinpoint.getVelX(DistanceUnit.INCH), pinpoint.getVelY(DistanceUnit.INCH), pinpoint.getHeadingVelocity(AngleUnit.DEGREES.getUnnormalized())); }


    @Override
    public void initialize() {
        pinpoint = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(0,-190, DistanceUnit.INCH);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.recalibrateIMU();
    }
}
