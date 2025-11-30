package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
@Disabled
public class FiveartifactRed extends LinearOpMode {
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
        blackboard.put(config.AllianceKey, config.RedAlliance);
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(88, 9, 0);
        //here
        timer = new ElapsedTime();
        speed = 0.5;
        AutoState = 0;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        rgb = hardwareMap.get(Servo.class, config.RBGName);
        waitForStart();
        runtime.reset();
        timer.reset();
        while (opModeIsActive()) {
            switch (AutoState) {
                case 0: {
                    timer.startTime();
                    pods.holdPosition(88, 90, 0, 1);
                    pods.update();
                    if (timer.seconds() > 1.6) {
                        AutoState = 1;
                    }
                }
                break;
                case 1: {
                    pods.holdHeading(-48, 1);
                    pods.update();
                    resetArtifacts();
                    if (timer.seconds() > 4.5) {
                        pods.update();
                        shutOff();
                        AutoState = 2;
                        robotSubsystem.setFlywheelVelocity(3500);
                    }
                }
                break;
                case 2: {
                    timer.reset();
                    timer.startTime();
                    ElapsedTime timer2 = new ElapsedTime();
                    /*while (timer.seconds() <= 8) {
                        if (robotSubsystem.getRpm() < 3520 && robotSubsystem.getRpm() > 3480) {
                            timer2.startTime();
                            if (timer.seconds() > 2) {
                                rgb.setPosition(config.Green);
                                robotSubsystem.spinBelt(1);
                            }
                        } else {
                            robotSubsystem.spinBelt(0);
                            rgb.setPosition(0.388);
                        }
                    }*/
                    if(timer.seconds() > 2){
                        shutOff();
                        AutoState = 3;
                    }

                }
                break;
                case 3:{
                    pods.holdHeading(90,1);
                    pods.update();
                    timer.reset();
                    timer.startTime();
                    while(timer.seconds() > 1){
                        pods.holdPosition(88, 110, 90, 1);
                        pods.update();
                    }
                }

            }

                telemetry.addData("state", AutoState);
                telemetry.addData("time", timer.seconds());
                telemetry.addData("x", pods.getX());
                telemetry.addData("y", pods.getY());
                telemetry.addData("h", pods.getHeading());

                telemetry.update();

        }
    }

    public void resetArtifacts() {
        robotSubsystem.setFlywheelVelocity(-300);
        robotSubsystem.spinBelt(-0.2);
        robotSubsystem.spinIntake(1);
    }

    public void shutOff() {
        robotSubsystem.setFlywheelVelocity(0);
        robotSubsystem.spinBelt(0);
        robotSubsystem.spinIntake(0);
    }

    public void spinForTime(double secs, double power) {
        ElapsedTime timer3 = new ElapsedTime();
        timer3.startTime();
        while (timer3.seconds() < secs) {
            robotSubsystem.spinBelt(power);
        }
    }
}

