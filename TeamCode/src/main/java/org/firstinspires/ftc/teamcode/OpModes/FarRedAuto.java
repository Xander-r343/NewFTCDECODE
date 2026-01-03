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
import org.firstinspires.ftc.teamcode.Utilities.AimbotV2;

@Autonomous(name = "far zone red V1")
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
    double [] values;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(88, 9, -90);
        timer = new ElapsedTime();
        AutoState = 0;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap, runtime);
        spindexer.reloadFlickerServo();
        waitForStart();
        runtime.reset();
        timer.reset();
        blackboard.put(config.AllianceKey,config.RedAlliance);
        while (opModeIsActive()) {
            values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
            turret.setServoPoseManaul(1);
            turret.setFlywheelToRPM((int)values[1] - 100);
            turret.update();
            aimbots.update();
            turret.turretSetIdealAngleUsingLLandPods();
            switch (AutoState) {
                case 0: {
                    timer.startTime();
                    fire3();
                    if(timer.seconds() > 8){
                            AutoState = 1;

                    }
                }
                break;
                case 1:{
                    telemetry.speak("Gamepad 2:\n" +
                            "Spindexer firing:\n" +
                            "Gamepad2.right Dpad: spindexer go back\n" +
                            "Gamepad2.left Dpad, spindexer advance\n" +
                            "Gamepad2.up Dpad go pose 2\n" +
                            "Gamepad2.down Dpad go pose 0\n" +
                            "Fire:\n" +
                            "Gamepad2.left trigger: fire 1 ball\n" +
                            "Gamepad 2.Left bumper fire 3 balls in pattern IN DEV\n" +
                            "Spindexer intaking: a\n" +
                            "Gamepad2.a runs the intake\n" +
                            "Gamepad2.y spindexer go to slot 0\n" +
                            "Gamepad2.x spindexer go to next intaking slot\n" +
                            "Gamepad2.b spindexer go back to last intaking slot\n" +
                            "Turret Manual override \n" +
                            "Gamepad2.left_stick_x: turret rotate left and right 5 degrees per time you move the stick all the way right or left\n" +
                            "Gamepad2.left_stick_y: turret move hood up or down 0.07 servo per second\n" +
                            "Gamepad2.right_stick_y: rpm increment + or - 100 per time you move the stick to the edge \n" +
                            "Gamepad2 guide button toggle between auto turret and manual turret\n" +
                            "Map the left and right sticks to turret controls, hood and rpm and \n" +
                            "Auto turret mode\n" +
                            "No control\n" +
                            "Manual turret mode\n" +
                            "Gamepad2: joysticks/triggers: >0.8 then do something \n" +
                            "Gamepad 1:\n" +
                            "Gamepad1.left_stick_y: drive chassis forward/back\n" +
                            "Gamepad1.left_stick_x:chassis strafe left and right\n" +
                            "Gamepad1.right_stick_x: rotate chassis left and right\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n");
                }

        }
    }

    }
    public void fire3(){
        while(timer.seconds() < 2){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE && timer.seconds() > 1.2){
                spindexer.fireFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
        }
        while(timer.seconds() < 2){
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.FIRE && timer.seconds() > 0.45){
                spindexer.reloadFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
        }
        timer.reset();
        while(timer.seconds() < 1){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE && timer.seconds() > 0.5){
                spindexer.fireFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
        }
        while(timer.seconds() < 1.75){
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.FIRE && timer.seconds() > 0.8){
                spindexer.reloadFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
        }
        timer.reset();
        while(timer.seconds() < 1){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE && timer.seconds() > 0.5){
                spindexer.fireFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
        }
        while(timer.seconds() < 1.75){
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.FIRE && timer.seconds() > 0.35){
                spindexer.reloadFlickerServo();
            }
            turret.setFlywheelToRPM((int)values[1] - 300);
            turret.update();
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
