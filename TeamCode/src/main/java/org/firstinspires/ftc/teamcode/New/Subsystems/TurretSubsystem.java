package org.firstinspires.ftc.teamcode.New.Subsystems;

import org.firstinspires.ftc.teamcode.New.Util.Alliance;
import org.firstinspires.ftc.teamcode.Old.Configs.Config;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class TurretSubsystem {
    private DcMotorEx turretMotor;
    public Pose goalPose;
    public Follower follower;
    public TurretSubsystem(HardwareMap hwMap, Alliance currAlliance, Follower f) {
        turretMotor = hwMap.get(DcMotorEx.class, Config.turretRotationName);
        follower = f;
        goalPose = currAlliance.goalPose;
    }

    public static PIDCoefficients turretPIDCoeffs = new PIDCoefficients(0.0115, 0.0, 0.0);
    public static BasicFeedforwardParameters turretFFCoefs = new BasicFeedforwardParameters(0.0001851, 0.0, 0.006);
    public final ControlSystem turretControlSystem = ControlSystem.builder()
            .posPid(turretPIDCoeffs)
            .basicFF(turretFFCoefs)
            .build();

    public double calculateTurretAngle() {
        Pose currPose = follower.getPose();
        double rawAngle = Math.atan2(currPose.getY() - goalPose.getY(), currPose.getX()-goalPose.getX());
        double turretAngle = Range.clip(normalizeAngle(rawAngle - follower.getHeading() * (100.0/15.0)), -135.0, 135.0);
        return turretAngle;
    }

    public double normalizeAngle(double angDeg) {
        double finalAngle = angDeg;
        if (finalAngle < 0.0) {
            finalAngle += 360.0;
        }
        if (finalAngle > 180.0) {
            finalAngle -= 360.0;
        }
        return finalAngle;
    }

    public void update() {
        turretControlSystem.setGoal(new KineticState(calculateTurretAngle()));
        turretMotor.setPower(turretControlSystem.calculate(new KineticState(turretMotor.getCurrentPosition())));
    }
}
