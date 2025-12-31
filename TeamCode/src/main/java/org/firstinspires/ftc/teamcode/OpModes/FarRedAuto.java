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
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;

@Autonomous(name = "9 ball auto red")
public class FarRedAuto extends LinearOpMode {

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
    Turret turret;
    Spindexer spindexer;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(88, 9, 0);
        timer = new ElapsedTime();
        AutoState = 0;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap);
        waitForStart();
        runtime.reset();
        timer.reset();
        blackboard.put(config.AllianceKey,config.RedAlliance);
        while (opModeIsActive()) {
            aimbots.update();
            switch (AutoState) {
                case 0: {
                    telemetry.speak(String.valueOf(turret.getMotif()));
                }
                break;


        }

        aimbots.update();
        telemetry.addData("x", pods.getX());
        telemetry.addData("y", pods.getY());
        telemetry.addData("h", pods.getHeading());
        telemetry.addData("I see apriltag: ", aimbots.LLstatusIsValid());
        telemetry.update();
    }

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
