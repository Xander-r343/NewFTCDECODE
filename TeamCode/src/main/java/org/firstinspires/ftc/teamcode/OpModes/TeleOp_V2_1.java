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
import org.firstinspires.ftc.teamcode.Utilities.AimbotV2;

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
    ElapsedTime runtimer;
    double hoodAngle;
    ElapsedTime firingTimer;
    boolean manualFlywheel = false;
    ElapsedTime timer;
    boolean isFlicking;
    boolean autoSpindexIntake = true;
    private enum ShooterState {
        IDLE,
        SLOT0_POSE,
        SLOT0_FIRE_WAIT,
        SLOT0_RELOAD_WAIT,
        SLOT1_POSE,
        SLOT1_FIRE_WAIT,
        SLOT1_RELOAD_WAIT,
        SLOT2_POSE,
        SLOT2_FIRE_WAIT,
        SLOT2_RELOAD_WAIT,
        COMPLETE
    }
    private ShooterState currentState;
    double [] values;
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
        runtimer = new ElapsedTime();
        runtimer.startTime();
        spindexer = new Spindexer(hardwareMap, runtimer);
        firingTimer = new ElapsedTime();
        timer = new ElapsedTime();
        currentState = ShooterState.IDLE;
        //spindexer.PickupPoseSlot0();
        spindexer.reloadFlickerServo();
    }

    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);

        //spindexer controls for FIRING:
        //attack (avdacnce)
        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE) {
              //  spindexer.FirePoseSlot1();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE) {
               // spindexer.FirePoseSlot2();
            }
            else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE) {
                //reach the end
                //spindexer.FirePoseSlot0();
            }
        }
        //retreat
        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE) {
                //reached the end
                //spindexer.FirePoseSlot2();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE) {
              //  spindexer.FirePoseSlot0();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE) {
              //  spindexer.FirePoseSlot1();
            }
        }

        //goto slot 0 firing position
        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
          //  spindexer.FirePoseSlot0();
        }
        //goto slot 2 firing position
        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
            //spindexer.FirePoseSlot2();
        }
        //fire ball controls (flicking servo)
        //fire a single ball and rotate to the next slot afterwards
        if (currentGamepad2.left_bumper && previousGamepad2.left_bumper && !isFlicking && (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE ||spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE ||spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE)) {
            isFlicking = true;
            firingTimer.startTime();
            firingTimer.reset();
            spindexer.fireFlickerServo();
        }
        if (spindexer.getFlickerState() == Spindexer.FlickerServoState.FIRE && firingTimer.seconds() > 0.2 && isFlicking) {
            spindexer.reloadFlickerServo();
        }
        if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOAD && firingTimer.seconds() > 0.4 && isFlicking){
            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE) {
               // spindexer.FirePoseSlot1();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE) {
               // spindexer.FirePoseSlot2();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE) {
               // spindexer.FirePoseSlot0();
            }
            isFlicking = false;
        }
        if(gamepad2.right_bumper){
            autoSpindexIntake = false;
        }


        //spindexer controls INTAKE:
        //intake hold for on
        if(gamepad2.a){
            turret.setIntakeSpeed(0.8);
        }
        else{
            turret.setIntakeSpeed(0);
        }
        if(autoSpindexIntake && spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP){
                timer.reset();
                //spindexer.PickupPoseSlot1();
            }
            else if(spindexer.getPosition() == config.slot1Pickup && timer.seconds() > 0.3){
                //spindexer.PickupPoseSlot2();
                timer.reset();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP && timer.seconds() > 0.3){
                //spindexer.FirePoseSlot0();
                turret.setIntakeSpeed(0);
            }
        }
        //set 0 pose
        if(currentGamepad2.y && !previousGamepad2.y){
            //spindexer.PickupPoseSlot0();
        }
        //advance
        if(currentGamepad2.x && !previousGamepad2.x){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP){
                //spindexer.PickupPoseSlot1();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP){
                //spindexer.PickupPoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP){
                //spindexer.PickupPoseSlot0();
            }

        }
        //reverse
        if(currentGamepad2.b && !previousGamepad2.b){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP){
                //spindexer.PickupPoseSlot2();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP){
                //spindexer.PickupPoseSlot0();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP){
                //spindexer.PickupPoseSlot1();
            }

        }
        if (gamepad2.right_stick_x > 0.4){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees() + 5,1);
        }
        if (gamepad2.right_stick_x < -0.4){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegrees() - 5,1);
        }
        if(currentGamepad2.right_stick_button && !previousGamepad2.right_stick_button){
            if(!manualFlywheel){
                manualFlywheel = true;
            }
            else{
                manualFlywheel = false;
            }
        }
        if (gamepad2.right_stick_y > 0.4 && manualFlywheel){
            vel += 1;
        }
        else if (gamepad2.right_stick_y < -0.4 && manualFlywheel){
            vel -=1;
        }
        drivetrain.drive(gamepad1.left_stick_y*1, 1*gamepad1.left_stick_x, gamepad1.right_stick_x*0.8);

        //update
        values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
        telemetry.addData("e", turret.getRpm());
        telemetry.addData("heading", aimbots.pods.getHeading());
        telemetry.addData("x", aimbots.pods.getX());
        telemetry.addData("y", aimbots.pods.getY());
        turret.setFlywheelToRPM((int)(values[1]*0.91));
        turret.setHoodLaunchAngle(values[0] + 0.1);
        turret.turretSetIdealAngleUsingLLandPods();
        telemetry.update();
        aimbots.update();
        turret.update();


    }
}
