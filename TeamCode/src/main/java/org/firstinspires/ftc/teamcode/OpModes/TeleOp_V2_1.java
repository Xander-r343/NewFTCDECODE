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

@TeleOp(name = "TeleOp V2.1.0", group = "teleop")
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
    boolean justFired = false;
    boolean isIntaking;
    double [] values;
    boolean isParking = false;
    @Override
    public void init() {
        config = new Config();
        //initialize subsystems
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
       // pods.setPosition(72,9,0);
        aimbots = new Aimbots((int)blackboard.get(config.AllianceKey), pods, hardwareMap);
        //rbgIndicator = hardwareMap.get(Servo.class, config.RBGName);
        turret = new Turret(hardwareMap, (int)blackboard.get(config.AllianceKey), aimbots, telemetry);
        //initialize the robotSubsystem class
        //robotSubsystem = new ShooterSubsystem(hardwareMap);
        vel = 0;
        hoodAngle = 0.55;
        //flywheelActive = false;
        //continousAim = false;
        runtimer = new ElapsedTime();
        runtimer.startTime();
        spindexer = new Spindexer(hardwareMap, runtimer, telemetry);
        firingTimer = new ElapsedTime();
        timer = new ElapsedTime();
        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
        spindexer.reloadFlickerServo();
        turret.setTurretOffset((int)blackboard.put(config.turretOffsetKey, turret.getTurretPositionDegreesNotOffField()));

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
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE)
            {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE)
            {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE) {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
            }
        }
        //retreat
        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE)
            {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE)
            {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE) {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
            }
        }

        //goto slot 0 firing position
        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
        }
        //goto slot 2 firing position
        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
        }
        //fire ball controls (flicking servo)
        //fire a single ball and rotate to the next slot afterwards
        if (gamepad2.left_trigger > 0.8) {
            spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                 Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
        }
        //flicker manual controls
        if(currentGamepad2.left_bumper && !previousGamepad2.dpad_left && !isFlicking &&
                       (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE ||
                        spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE ||
                        spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE)){
            spindexer.fireFlickerServo();
            justFired = true;
            firingTimer.reset();
        }
        if(firingTimer.seconds() > 1.0 && justFired){

            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE)
            {
                justFired = false;
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE && firingTimer.seconds() > 0.41)
            {
                justFired = false;
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE && firingTimer.seconds() > 0.5){
                justFired = false;
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
            }
        }
        if(gamepad2.rightBumperWasReleased()){
            if(autoSpindexIntake) {
                autoSpindexIntake = false;
            }else{
                autoSpindexIntake = true;
            }
        }


        //spindexer controls INTAKE:
        //intake hold for on

        if(gamepad2.a){
            turret.setIntakeSpeed(1.0);
            isIntaking = true;
        }
        else if(gamepad2.right_trigger > 0){
            turret.setIntakeSpeed(Math.pow(-gamepad2.right_trigger, 1.2));
        }
        else{
            turret.setIntakeSpeed(0);
        }

        if(autoSpindexIntake && spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED && isIntaking){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP){
                timer.reset();
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP && timer.seconds() > 0.3){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
                timer.reset();
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP && timer.seconds() > 0.33){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                turret.setIntakeSpeed(0);
            }
        }
        //set 0 pose
        if(currentGamepad2.y && !previousGamepad2.y){
            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
        }
        //advance
        if(currentGamepad2.x && !previousGamepad2.x){
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
            }
        }
        //reverse
        if(currentGamepad2.b && !previousGamepad2.b){

            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
            }
            else if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
            }
        }
        if (currentGamepad2.right_stick_x > 0.8 && previousGamepad2.right_stick_x < 0.8 && gamepad2.right_stick_x > 0){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegreesNotOffField() + 5,1);
        }
        if (currentGamepad2.right_stick_x < -0.8 && previousGamepad2.right_stick_x > -0.8 && gamepad2.right_stick_x < 0){
            turret.setTurretPositionDegrees(turret.getTurretPositionDegreesNotOffField() - 5,1);
        }
        if(currentGamepad2.right_stick_button && !previousGamepad2.right_stick_button){
            if(!manualFlywheel){
                manualFlywheel = true;
            }
            else{
                manualFlywheel = false;
            }
        }
        if (currentGamepad2.right_stick_y > 0.8 && previousGamepad2.right_stick_y < 0.8 && manualFlywheel && gamepad2.right_stick_y > 0){
            vel -= 80;
        }
        else if (currentGamepad2.right_stick_y < -0.8 && previousGamepad2.right_stick_y > -0.8 && manualFlywheel  && gamepad2.right_stick_y < 0){
            vel +=80;
        }
        if (currentGamepad2.left_stick_y > 0.8 && previousGamepad2.left_stick_y < 0.8   && gamepad2.left_stick_y > 0){
            hoodAngle += 2.5;
        }
        if (currentGamepad2.left_stick_y < -0.8 && previousGamepad2.left_stick_y > -0.8 && gamepad2.left_stick_y < 0){
            hoodAngle -=2.5;
        }
        //hold park position
        if(gamepad1.left_trigger > 0){
            if((int)blackboard.get(config.AllianceKey) == config.BlueAlliance){
                pods.holdPosition(105.5,33, 0,1);

            }
            else if((int)blackboard.get(config.AllianceKey) == config.RedAlliance){
                pods.holdPosition(38.5,33, 0,1);
            }
            isParking = true;
        }else{
            isParking = false;
        }

        if(!isParking) {
            drivetrain.drive(-gamepad1.left_stick_y * 1, 1 * gamepad1.left_stick_x, gamepad1.right_stick_x * 0.8);
        }
        //update
        values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
        telemetry.addData("e", turret.getRpm());
        telemetry.addData("target vel", values[1]);
        telemetry.addData("heading", aimbots.pods.getHeading());
        telemetry.addData("x", aimbots.pods.getX());
        telemetry.addData("y", aimbots.pods.getY());
        telemetry.addData("spin", spindexer.getState());
        telemetry.addData("flik", spindexer.getFlickerState());
        if(!manualFlywheel) {
            vel = (int)(values[1]);
            hoodAngle = values[0] + 0.1;
            turret.setTurretUsingVelAim();
        }
        turret.setFlywheelToRPM(vel);
        spindexer.updateState();
        turret.setHoodLaunchAngle(hoodAngle);
        telemetry.update();
        aimbots.update();
        turret.update();


    }
}
