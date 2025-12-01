package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
@Autonomous(name = "9 ball auto blue")
public class FarBlueAuto extends LinearOpMode {

    Servo rgb;
    ShooterSubsystem robotSubsystem;
    OdoPods pods;
    MecanumDrivetrain drivetrain;
    RedAutoPaths pathDatabase;
    Object Xpos;
    Object Ypos;
    Object Headingpos;
    Object Alliance;
    Config config;
    int AutoState;
    ElapsedTime timer;
    double speed;
    double time;
    Aimbots aimbots;
    private ElapsedTime runtime = new ElapsedTime();
    int tagID;
    int flywheelSpeed;
    double driveSpeed;
    double headingSpeed;
    double targetH;

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        //intialize blackboard objects
        Xpos = blackboard.getOrDefault(config.Xkey, 0);
        Ypos = blackboard.getOrDefault(config.Ykey, 0);
        Headingpos = blackboard.getOrDefault(config.HeadingKey, 0);
        Alliance = blackboard.getOrDefault(config.AllianceKey, 0);//isRedValue
        blackboard.put(config.AllianceKey, config.BlueAlliance);
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(56, 9, 0);
        timer = new ElapsedTime();
        AutoState = 0;
        aimbots = new Aimbots(config.BlueAlliance, pods, hardwareMap);
        rgb = hardwareMap.get(Servo.class, config.RBGName);
        flywheelSpeed = 2650;
        robotSubsystem.setServoPosition(0.5);
        driveSpeed = 0.4;
        waitForStart();
        headingSpeed = 0.5;
        runtime.reset();
        timer.reset();
        while (opModeIsActive()) {
            aimbots.update();
            switch (AutoState) {
                case 0: {
                    while(!pods.holdPosition(56, 84, 0, 1)) {
                        pods.update();
                        telemetry.addData("x", pods.getX());
                        telemetry.addData("y", pods.getY());
                        telemetry.addData("h", pods.getHeading());
                        telemetry.update();
                        if (pods.holdPosition(56, 84, 0, 1)) {
                            rgb.setPosition(config.Green);
                            AutoState = 1;
                        }
                    }
                }
                break;
                case 1: {
                    aimbots.update();
                    if (aimbots.LLstatusIsValid()) {
                        targetH = pods.getHeading() - aimbots.getHeadingErrorLL();
                        //pods.holdHeading(targetH,1);
                    } else {
                        targetH = aimbots.getIdealAngle();
                        //pods.holdHeading(targetH, 1);
                    }
                    robotSubsystem.setFlywheelVelocity(flywheelSpeed);
                    pods.holdHeading(targetH, 1);
                }
                break;
            }
        }
        blackboard.put(config.Xkey, pods.getX());
        blackboard.put(config.Ykey, pods.getY());
        blackboard.put(config.HeadingKey, pods.getHeading());
        telemetry.addData("x", pods.getX());
        telemetry.addData("y", pods.getY());
        telemetry.addData("h", pods.getHeading());
        telemetry.addData("I see apriltag: ", aimbots.LLstatusIsValid());
        telemetry.update();
    }





    public void resetArtifacts(){
        robotSubsystem.setFlywheelVelocity(-300);
        robotSubsystem.spinBelt(-0.2);
        robotSubsystem.spinIntake(1);
    }
    public void shutOff(){
        robotSubsystem.spinBelt(0);
    }
}
