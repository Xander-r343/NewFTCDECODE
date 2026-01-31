package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
@Disabled
public class LLtestertwo extends OpMode {
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    ShooterSubsystem robotSubsystem;
    Config config;
    Aimbots aimbots;
    @Override
    public void init() {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        //initialize OdoPods
        pods = new OdoPods(hardwareMap, drivetrain);
        //initialize Config
        config = new Config();
        pods.setPosition(72, 8 ,0);
        //initialize the robotSubsystem class
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        aimbots.startLL();
        aimbots.switchPipeline(2);
    }

    @Override
    public void loop() {
        aimbots.update();
        telemetry.addData("valid", aimbots.LLstatusIsValid());
        telemetry.update();
    }
}
