package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
@Disabled
public class RedAutoTriangle extends OpMode {
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
    @Override
    public void init() {
        //initialize subsystems
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
        pods.setPosition(56, 9, 0);
        timer = new ElapsedTime();
        speed = 0.5;
        AutoState = 0;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
    }

    @Override
    public void init_loop() {
        super.init_loop();
        AutoState = 0;
        resetRuntime();
    }

    @Override
    public void loop() {
        switch (AutoState){
            case 0:{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                timer.startTime();
                pods.holdPosition(56, 90, 0, 1);
                pods.update();
                if(timer.seconds() > 2){
                    pods.holdPosition(60,90, 0 ,1);
                }
                if(timer.seconds() > 4){
                    AutoState = 1;
                }
            }
            break;
            case 1:{
                pods.holdHeading(140, 0.4);
                pods.update();
                resetArtifacts();
                if(timer.seconds() > 6){
                    shutOff();
                    robotSubsystem.setFlywheelVelocity(3550);
                    AutoState = 2;
                }
            }
            break;
            case 2:{
                timer.reset();
                timer.startTime();
                ElapsedTime timer2 = new ElapsedTime();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int i = 0;
                while(timer.seconds() < 13) {
                    if (robotSubsystem.getRpm() < 3600 && robotSubsystem.getRpm() > 3500) {
                        timer2.startTime();
                        while(timer2.seconds() < 1.5) {
                            robotSubsystem.spinBelt(0.7);
                        }
                        i++;
                    }
                    else{
                        robotSubsystem.spinBelt(0);
                    }
                }
                shutOff();
                AutoState = 3;
            }
            break;
            case 3:{
                /*timer.reset();
                timer.startTime();
                pods.holdHeading(90,0.3);
                pods.update();*/

            }
            break;
            case 4:{

            }
            break;
        }

        telemetry.addData("state",AutoState);
        telemetry.addData("time", timer.seconds());
        telemetry.addData("x", pods.getX());
        telemetry.addData("y", pods.getY());
        telemetry.addData("h", pods.getHeading());

        telemetry.update();



    }
    public void resetArtifacts(){
        robotSubsystem.setFlywheelVelocity(-300);
        robotSubsystem.spinBelt(-0.2);
        robotSubsystem.spinIntake(1);
    }
    public void shutOff(){
        robotSubsystem.setFlywheelVelocity(0);
        robotSubsystem.spinBelt(0);
        robotSubsystem.spinIntake(0);
    }
    public void stop(){
    }
}