package org.firstinspires.ftc.teamcode.Old.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Old.Configs.Config;
import org.firstinspires.ftc.teamcode.Old.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Subsystems.retired.ShooterSubsystem;

@Disabled
public class FarLaunchZoneRed extends LinearOpMode {
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
    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        //intialize blackboard objects
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(81, 9, 0);
        timer = new ElapsedTime();
        AutoState = 0;
        blackboard.put(config.AllianceKey, config.RedAlliance);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        rgb = hardwareMap.get(Servo.class, config.RBGName);
        flywheelSpeed = 3600;
        robotSubsystem.setServoPosition(0.5);
        driveSpeed = 0.4;
        waitForStart();
        headingSpeed = 0.5;
        runtime.reset();
        timer.reset();
        waitForStart();
        resetRuntime();
        while(opModeIsActive()){
            switch (AutoState) {
                case 0: {
                    robotSubsystem.setFlywheelVelocity(flywheelSpeed);
                    pods.update();
                    if(pods.holdPosition(81, 9, 0, 1)){
                        aimbots.update();
                        pods.holdHeading(aimbots.getIdealAngle(), 1);
                        AutoState = 1;
                    }
                }
                case 1:{
                    timer.startTime();
                    while(timer.seconds() < 4) {
                        robotSubsystem.spinIntake(speed);
                        robotSubsystem.spinBelt(0.4);
                        if(timer.seconds() > 4.8){
                            robotSubsystem.setServoPosition(0);
                            AutoState = 3;
                            robotSubsystem.setFlywheelVelocity(flywheelSpeed);
                            robotSubsystem.spinIntake(1);
                        }
                        else if(timer.seconds() > 0.4 && timer.seconds() < 2){
                            robotSubsystem.setServoPosition(0);
                        }
                        if(timer.seconds() > 2 && timer.seconds() < 4){
                            robotSubsystem.setServoPosition(1);
                        }
                        speed -= 0.0003;
                    }
                    robotSubsystem.setServoPosition(0);
                    AutoState = 2;
                }
            }

        }
    }
}
