package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
@TeleOp
@Configurable
public class TurretFixer extends OpMode {
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    Config config;
    Aimbots aimbots;
    Turret turret;
    public static double p = 0, i = 0, d = 0, f = 0;
    public static double angle;
    public static double speed;

    @Override
    public void init() {
        config = new Config();
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        // pods.setPosition(72,9,0);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        //rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots, telemetry);
    }

    @Override
    public void loop() {
        turret.setPIDF_constants(p,i,d,f);
        turret.setTurretPositionDegrees(angle, speed);
        turret.update();
    }
}
