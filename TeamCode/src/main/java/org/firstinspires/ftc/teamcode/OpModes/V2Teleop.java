package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
@TeleOp

public class V2Teleop extends OpMode {
    Servo rbgIndicator;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    Config config;
    Aimbots aimbots;
    Turret turret;
    Spindexer spindexer;
    int vel;
    boolean flywheelActive;
    double targetH;
    boolean continousAim;
    int hoodAngle;
    @Override
    public void init() {
        config = new Config();
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        aimbots = new Aimbots(config.BlueAlliance, pods, hardwareMap);
        //rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, config.BlueAlliance, aimbots);
        //initialize the robotSubsystem class
        //robotSubsystem = new ShooterSubsystem(hardwareMap);
        vel = 0;
        hoodAngle = 0;
        //flywheelActive = false;
        //continousAim = false;
        pods.setPosition(72, 9, 0);
    }

    @Override
    public void loop() {
        //gamepad
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad1);
        currentGamepad2.copy(gamepad1);
        //drivetrain driving
        drivetrain.drive(-gamepad1.left_stick_y*1, 1*gamepad1.left_stick_x, gamepad1.right_stick_x*0.8);

        //turret aiming controller 2
        if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees()- 5,1);
        }
        else if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees()+ 5,1);
        }
        if(currentGamepad1.dpad_left && !previousGamepad1.dpad_left){
            hoodAngle -=0.05;
        }
        else if(currentGamepad1.dpad_right && !previousGamepad1.dpad_right){
            hoodAngle +=0.05;
        }
        if(gamepad1.a){
            turret.turretSetIdealAngleUsingLLandPods();
        }
        turret.setServoPoseManaul(1);
        turret.setFlywheelToRPM(vel);
        turret.updateSystem();
        telemetry.addData("turret pose", turret.getTurretPositionDegrees());
        telemetry.addData("rpm", turret.getRpm());
        telemetry.addData("ideal", turret.getTx());
        telemetry.addData("heading", aimbots.pods.getHeading());
        telemetry.addData("x", aimbots.pods.getX());
        telemetry.addData("y", aimbots.pods.getY());
        telemetry.addData("dist", aimbots.calculateSideLengthUsingPods());
        telemetry.update();





    }
}
