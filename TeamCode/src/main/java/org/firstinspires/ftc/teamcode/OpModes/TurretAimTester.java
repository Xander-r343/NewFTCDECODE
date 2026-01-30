package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Utilities.AimbotV2;

@TeleOp(name = "testerAim", group = "teleop")
public class TurretAimTester extends OpMode {
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
    ElapsedTime runtimer;
    double hoodAngle;
    ElapsedTime firingTimer;
    boolean manualFlywheel = false;
    ElapsedTime timer;
    boolean isFlicking;
    boolean autoSpindexIntake = true;
    boolean justFired = false;
    boolean isIntaking;
    double [] values;
    boolean isParking = false;
    boolean enableFiring = false;
    @Override
    public void init() {
        config = new Config();
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pods.setPosition(88,9,0);
        // pods.setPosition(72,9,0);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        //rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots, telemetry);
        //initialize the robotSubsystem class
        //robotSubsystem = new ShooterSubsystem(hardwareMap);
        vel = 0;
        hoodAngle = 0.55;
        //flywheelActive = false;
        //continousAim = false;
        runtimer = new ElapsedTime();
        runtimer.startTime();
        spindexer = new Spindexer(hardwareMap, runtimer, telemetry);
        firingTimer = new ElapsedTime();
        timer = new ElapsedTime();
        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
        spindexer.reloadFlickerServo();
    }

    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);

        //spindexer controls for FIRING:
        //attack (avdacnce)
        if(gamepad1.aWasReleased()){
            enableFiring = !enableFiring;
        }
        //update
        values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
        telemetry.addData("heading", aimbots.pods.getHeading());
        telemetry.addData("x", aimbots.pods.getX());
        telemetry.addData("y", aimbots.pods.getY());

        if(enableFiring) {
            hoodAngle = values[0] + 0.1;
            turret.setTurretUsingVelAim();
        }
        drivetrain.drive(-gamepad1.left_stick_y * 1, 1 * gamepad1.left_stick_x, gamepad1.right_stick_x * 0.8);
        spindexer.updateState();
        turret.setHoodLaunchAngle(hoodAngle);
        telemetry.update();
        aimbots.update();
        turret.update();


    }
}
