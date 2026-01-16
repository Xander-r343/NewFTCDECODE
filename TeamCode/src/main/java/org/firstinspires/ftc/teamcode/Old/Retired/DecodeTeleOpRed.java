package org.firstinspires.ftc.teamcode.Old.Retired;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Old.Configs.Config;
import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Subsystems.ShooterSubsystem;
@Disabled
public class DecodeTeleOpRed extends OpMode {
    public final double[] LaunchPose1 = {0, 18, 40, 1};//x,y,heading,speed
    public final double[] LaunchPose2 = {16, 48, 25, 1};//x,y,heading,speed
    MecanumDrivetrain drivetrain;
    DcMotorEx shooter;
    OdoPods pods;
    ShooterSubsystem robotSubsystem;
    Config config;
    private double targetX = 50, targetY = 0;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();

    public int state;

    //this function sets the chassis to the designated launch position
    void setLaunchPose(@NonNull double[] LaunchPose) {
        if (pods.holdPosition(LaunchPose[0], LaunchPose[1], LaunchPose[2], LaunchPose[3])) {
            pods.update();
        }
    }

    @Override
    public void init() {
        //TODO set this to the position after autonomous ends
        //initialize drivetrain to pass to OdoPods
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        //initialize OdoPods
        pods = new OdoPods(hardwareMap, drivetrain);
        pods.setPosition(30, 30, 90);
        //initialize Config
        config = new Config();
        shooter = hardwareMap.get(DcMotorEx.class, config.MainFlywheelMotorName);
        //initialize the robotSubsystem class
        robotSubsystem = new ShooterSubsystem(hardwareMap);
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        pods.update();


        //test this
        drivetrain.drive(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

    }
        public double calculateDistanceFromGoal () {
            //0 is x
            // 1 is y
            double x = targetX - pods.getX();
            double y = targetY - pods.getY();
            double hypotnuse = Math.sqrt(Math.pow(x, 2) - Math.pow(y, 2));
            return hypotnuse;
        }

        public double lockOn () {
            double adjacent = targetX - pods.getX();
            double opposite = targetY - pods.getY();
            double hypotnuse = Math.sqrt(Math.pow(adjacent, 2) + Math.pow(opposite, 2));
            double robotIdealAngle = Math.acos(Math.toRadians(adjacent / hypotnuse));
            return Math.toDegrees(robotIdealAngle);
        }

}
