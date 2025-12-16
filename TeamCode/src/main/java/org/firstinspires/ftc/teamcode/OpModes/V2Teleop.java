package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

public class V2Teleop extends OpMode {
    Servo rbgIndicator;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    ShooterSubsystem robotSubsystem;
    Config config;
    Aimbots aimbots;
    Turret turret;
    Spindexer spindexer;
    int vel;
    boolean flywheelActive;
    double targetH;
    double LLheadingError;
    double PodsIdealAngle;
    boolean continousAim;
    @Override
    public void init() {
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        aimbots = new Aimbots((int)blackboard.get(config.AllianceKey), pods, hardwareMap);
        config = new Config();
        rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, (int)blackboard.get(config.AllianceKey), aimbots);
        //initialize the robotSubsystem class
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        vel = 3500;
        flywheelActive = false;
        continousAim = false;
    }

    @Override
    public void loop() {
        //gamepad
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad1);
        currentGamepad2.copy(gamepad1);
        //drivetrain driving
        drivetrain.drive(gamepad1.left_stick_y*1, -1*gamepad1.left_stick_x, gamepad1.right_stick_x*0.8);
        //turret aiming controller 2
        if(currentGamepad2.a && !previousGamepad1.a){
            if(continousAim == false) {
                turret.continuouslyAim(true);
            }
            else{
                turret.continuouslyAim(false);
            }
        }





    }
}
