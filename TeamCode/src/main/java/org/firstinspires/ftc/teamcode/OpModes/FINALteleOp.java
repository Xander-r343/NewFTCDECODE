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
@TeleOp
public class FINALteleOp extends OpMode {
    Servo rbgIndicator;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    ShooterSubsystem robotSubsystem;
    Config config;
    Aimbots aimbots;
    boolean engage;
    int dist;
    int vel;
    boolean flywheelActive;
    double targetH;
    double LLheadingError;
    double PodsIdealAngle;
    @Override
    public void init() {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        //initialize OdoPods
        pods = new OdoPods(hardwareMap, drivetrain);
        //initialize Config
        config = new Config();
        rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        pods.setPosition(72, 8 ,-90);
        //initialize the robotSubsystem class
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        engage = false;
        dist = 0;
        vel = 3500;
        flywheelActive = false;
        aimbots.startLL();
        aimbots.switchPipeline(0);
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        if(flywheelActive) {
            robotSubsystem.setFlywheelVelocity(vel);
        }
        else{
            robotSubsystem.setFlywheelVelocity(0);
        }
        aimbots.update();
        //intake & belt controls
        LLheadingError = aimbots.getHeadingErrorLL();
        PodsIdealAngle = aimbots.getIdealAngle();

        if(currentGamepad1.a && !previousGamepad1.a) {
            if(aimbots.LLstatusIsValid()){
                targetH = pods.getHeading()-aimbots.getHeadingErrorLL();
                //pods.holdHeading(targetH,1);
            }
            else {
                targetH = aimbots.getIdealAngle();
                //pods.holdHeading(targetH, 1);
            }
            vel = robotSubsystem.flywheelGetRpmFromTable((int)aimbots.calculateSideLengthUsingPods());
        }
        if(gamepad1.a){
            pods.holdHeading(targetH,1);

        }
        else if(!gamepad1.a) {
            drivetrain.drive(gamepad1.left_stick_y * -1, 1 * gamepad1.left_stick_x, gamepad1.right_stick_x * 1);
        }
        telemetry.addData("ll", aimbots.LLstatusIsValid());
        telemetry.update();



    }
}
