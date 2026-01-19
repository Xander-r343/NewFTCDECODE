package org.firstinspires.ftc.teamcode.Old.Retired;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Old.Configs.Config;
import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Subsystems.retired.ShooterSubsystem;
@Disabled
public class TeleOpRed extends OpMode {
    ShooterSubsystem robotSubsystem;
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    Aimbots aimbots;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Config config;
    public final double[] RedLaunchPose1 = {56, 80, 30, 1};//x,y,heading,speed
    public final double[] BlueLaunchPose1 = {88, 80, 150, 1};//x,y,heading,speed
    @Override
    public void init() {
        config = new Config();
        //subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        robotSubsystem = new ShooterSubsystem(hardwareMap);
        //TODO set this for where auto ends and implement Blackboard thing
        pods.setPosition((double)blackboard.get(config.Xkey),(double) blackboard.get(config.Ykey),(double) blackboard.get(config.HeadingKey));
        //initialize the aiming sysytem
    }
    @Override
    public void loop() {
        //default stuff to run ever loop
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        pods.update();
        drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if(gamepad1.a){
            if(!aimbots.isBlue) {
                pods.holdPosition(RedLaunchPose1[0], RedLaunchPose1[1], RedLaunchPose1[2], RedLaunchPose1[3]);
                pods.update();
            }
            else{
                pods.holdPosition(BlueLaunchPose1[0], BlueLaunchPose1[1], BlueLaunchPose1[2], BlueLaunchPose1[3]);
                pods.update();
            }
            //TODO fix this value
            robotSubsystem.setFlywheelVelocity(3500);
        }
        //spin belts with
        if(gamepad1.right_trigger > 0){
            robotSubsystem.spinBelt(gamepad1.right_trigger);
            robotSubsystem.spinIntake(gamepad1.right_trigger);
            //put intake in here
        }

        if(gamepad1.left_trigger > 0){
            robotSubsystem.spinBelt(-gamepad1.left_trigger);
            robotSubsystem.spinIntake(-gamepad1.left_trigger);
            //put the intake control active in here
        }

    }
}
