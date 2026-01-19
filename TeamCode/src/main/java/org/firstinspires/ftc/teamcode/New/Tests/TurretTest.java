package org.firstinspires.ftc.teamcode.New.Tests;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.New.Constants.PedroConstants;
import org.firstinspires.ftc.teamcode.Old.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.New.Util.Alliance;

@TeleOp(name="Turret Test")
public class TurretTest extends OpMode {
    public TurretSubsystem turret;
    public Follower follower;

    @Override
    public void init() {
        follower = PedroConstants.createFollower(hardwareMap);
        turret = new TurretSubsystem(hardwareMap, Alliance.BLUE, follower);
        follower.startTeleOpDrive();
    }

    @Override
    public void loop() {
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        turret.update();
    }
}
