package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
@TeleOp(name = "TeleOp V2.1.0")
public class TeleOp_V2_1 extends OpMode {
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
    boolean continousAim;
    boolean turretMoving = false;
    double hoodAngle;
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
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);
        //spindexer controls FIRING:
        //forward
        if(currentGamepad2.dpad_right && !previousGamepad2.dpad_right){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0FIRE){
                spindexer.FirePoseSlot1();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT1FIRE){
                spindexer.FirePoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT2FIRE){
                spindexer.FirePoseSlot0();
            }
        }
        //advance
        if(currentGamepad2.dpad_left && !previousGamepad2.dpad_left){
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
        //pose 0
        if(currentGamepad2.dpad_up && !previousGamepad2.dpad_up){
            spindexer.FirePoseSlot0();
        }
        //pose 2
        if(currentGamepad2.dpad_down && !previousGamepad2.dpad_down){
            spindexer.FirePoseSlot2();
        }
        //fire ball controls
        if(currentGamepad2.left_bumper && !previousGamepad2.left_bumper){
            spindexer.FireBall(220);
        }
        //spindexer controls INTAKE:
        //intake hold for on
        if(gamepad2.a){
            turret.setIntakeSpeed(0.8);
        }
        else{
            turret.setIntakeSpeed(0);
        }
        //set 0 pose
        if(currentGamepad2.y && !previousGamepad2.y){
            spindexer.PickupPoseSlot0();
        }
        //advance
        if(currentGamepad2.x && !previousGamepad2.x){
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
        //reverse
        if(currentGamepad2.b && !previousGamepad2.b){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT0PICKUP){
                spindexer.PickupPoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT1PICKUP){
                spindexer.PickupPoseSlot0();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT2PICKUP){
                spindexer.PickupPoseSlot1();
            }

        }
        if (gamepad2.right_stick_x > 0.4){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees() + 5,1);
        }
        if (gamepad2.right_stick_x < -0.4){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees() - 5,1);
        }
        if (gamepad2.right_stick_x > 0.4){
        }
        if (gamepad2.right_stick_x < -0.4){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees() - 5,1);
        }

        telemetry.addData("g",gamepad2.right_stick_x);
    }
}
