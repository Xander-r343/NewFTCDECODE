package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.opencv.features2d.MSER;

@TeleOp

public class V2Teleop extends OpMode {
    Servo rbgIndicator;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    Config config;
    Aimbots aimbots;
    Turret turret;
    Spindexer spindexer;
    int vel;
    boolean flywheelActive;
    double targetH;
    boolean continousAim;
    double hoodAngle;
    boolean intakingMode;
    boolean slot1;

    @Override
    public void init() {
        config = new Config();
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pods.setPosition(72, 9, 0);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        //rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots, telemetry);
        //initialize the robotSubsystem class
        //robotSubsystem = new ShooterSubsystem(hardwareMap);
        vel = 0;
        hoodAngle = 0.55;
        //flywheelActive = false;
        //continousAim = false;
        spindexer = new Spindexer(hardwareMap);
        intakingMode = false;
        slot1 = false;
    }

    @Override
    public void loop() {
        if(gamepad1.left_stick_button){
            pods.setPosition(72,9,0);
        }
        //gamepad
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad2);
         currentGamepad2.copy(gamepad2);
        //drivetrain driving
        drivetrain.drive(-gamepad1.left_stick_y*1, 1*gamepad1.left_stick_x, gamepad1.right_stick_x*0.8);

        //fire ball, NOW WORKS WITH RESTRICTIONS
        if(currentGamepad2.right_bumper && !previousGamepad2.right_bumper){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0FIRE ||spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT1FIRE ||spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT2FIRE){
                spindexer.FireBall(200);
            }
        }
        if(gamepad2.b){
            spindexer.FirePoseSlot0();
            turret.setIntakeSpeed(0);
            intakingMode = false;
        }
        if(currentGamepad2.dpad_up && !previousGamepad2.dpad_up){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0FIRE){
                spindexer.FirePoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT1FIRE){
                spindexer.FirePoseSlot0();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT2FIRE){
                spindexer.FirePoseSlot1();
            }
        }
        if(gamepad2.a){
            intakingMode = true;
            spindexer.PickupPoseSlot0();
            turret.setIntakeSpeed(0.75);
        }
        if(currentGamepad2.dpad_right && !previousGamepad2.dpad_right){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0PICKUP){
                spindexer.PickupPoseSlot1();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT1PICKUP){
                spindexer.PickupPoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT2PICKUP){
                spindexer.PickupPoseSlot0();
            }
        }
        if(intakingMode && spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0PICKUP){
                spindexer.PickupPoseSlot1();
            }
            else if(spindexer.getPosition() == config.slot1Pickup){
                spindexer.PickupPoseSlot2();

            }
        }
        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            vel += 100;
        }
        else if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            vel -= 100;
        }
        if(currentGamepad1.dpad_right && !previousGamepad1.dpad_right){
            hoodAngle+= 0.025;
        }
        else if(currentGamepad1.dpad_left && !previousGamepad1.dpad_left){
            hoodAngle -= 0.025;
        }
        if(gamepad1.x){
            turret.turretSetIdealAngleUsingLLandPods();
        }
        pods.update();
        aimbots.update();
        turret.setServoPoseManaul(hoodAngle);
        turret.setFlywheelToRPM(vel);
        turret.update();
        telemetry.addData("current R", turret.getRightCurrent());
        telemetry.addData("current L", turret.getLeftCurrent());
        telemetry.addData("servo pose", spindexer.getState());
        telemetry.addData("turret pose", turret.getTurretPositionDegrees());
        telemetry.addData("rpm", turret.getRpm());
        telemetry.addData("targetrpm", vel);
        telemetry.addData("ideal", turret.getTx());
        telemetry.addData("heading", aimbots.pods.getHeading());
        telemetry.addData("x", aimbots.pods.getX());
        telemetry.addData("y", aimbots.pods.getY());
        telemetry.addData("dist", aimbots.calculateSideLengthUsingPods());
        telemetry.addData("hood", hoodAngle);
        telemetry.addData("color", spindexer.getBallColorImmediately());
        telemetry.update();





    }
}
