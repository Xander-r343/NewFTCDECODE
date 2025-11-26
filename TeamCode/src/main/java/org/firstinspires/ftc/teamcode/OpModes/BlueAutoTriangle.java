package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@Disabled
public class BlueAutoTriangle extends OpMode {
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    int autostate;
    ElapsedTime timer;
    double secondsWaitedBeforeStart = 0;
    ShooterSubsystem robotSubsystem;
    Object Xpos;
    Object Ypos;
    Object Headingpos;
    Object Alliance;
    Config config;
    @Override
    public void init() {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pods.setPosition(56, 9, -90);
        autostate = 0;
        timer = new ElapsedTime();
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        config = new Config();
        Xpos = blackboard.getOrDefault(config.Xkey, 0);
        Ypos = blackboard.getOrDefault(config.Ykey, 0);
        Headingpos = blackboard.getOrDefault(config.HeadingKey, 0);
        Alliance = blackboard.getOrDefault(config.AllianceKey, config.NoAlliance);//isRedValue
        blackboard.put(config.AllianceKey, config.BlueAlliance);
    }

    @Override
    public void loop() {
        blackboard.put(config.Xkey, pods.getX());
        blackboard.put(config.Ykey, pods.getY());
        blackboard.put(config.HeadingKey, pods.getHeading());

        switch(autostate){
            case 1:{
                timer.startTime();
                //first path
                if(timer.seconds() >= secondsWaitedBeforeStart) {
                    pods.holdPosition(56, 90, 90, 0.8);
                    pods.update();
                }
            }
            case 2:{
                if(timer.seconds() >= 4){
                    pods.holdHeading(-140,1);
                }
                if(timer.seconds() >= 5) {
                    robotSubsystem.spinIntake(1);
                    robotSubsystem.spinBelt(0.5);
                    pods.holdPosition(125, 35.5, 0, 0.8);
                    pods.update();
                    robotSubsystem.spinBelt(0);
                    robotSubsystem.spinIntake(0);
                }
            }
            case 3:{
                if(timer.seconds() >= 7){
                    pods.holdPosition(56,36,90,0.8);
                    pods.update();
                }
            }
            case 4:{
                if(timer.seconds() >= 8) {
                    pods.holdPosition(88, 80, 147, 0.8);
                    pods.update();
                    //for(int i = 0; i <3; i++){

                    //launch
                    //}
                    //TODO check this velocity
                }
                if(timer.seconds() >= 10){
                    robotSubsystem.setFlywheelVelocity(3000);
                    robotSubsystem.spinBelt(1);
                }
            }


        }
    }
}
