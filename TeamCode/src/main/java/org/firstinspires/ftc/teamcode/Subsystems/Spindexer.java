package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.GREEN;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.PURPLE;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.UNDECTED;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Retired.SpindexerState;

import java.util.Arrays;
import java.util.HashMap;

public class Spindexer {
    private Servo starboardServo;
    private Servo firingServo;
    private RevColorSensorV3 slot0;
    private ColorSensor slot1;
    private ColorSensor slot2;
    public SpindexerState spindexerState;
    private SpindexerRotationalState RealSpindexerState;
    private FlickerServoState flickerServoState;
    public enum color{
        PURPLE, GREEN, UNDECTED
    }
    Config config;
    ///          |
    ///          |
    ///       1  o    2
    ///        /   \
    ///       / 0   \
    ///     /        \ slots are unmoving
    ///      intake side
    ///        firing side

    public Spindexer(HardwareMap hardwareMap){
        config = new Config();
        //initialize servos
        starboardServo = hardwareMap.get(Servo.class, config.stbSv);
        starboardServo.resetDeviceConfigurationForOpMode();
        //initialize color sensors
        slot0 = hardwareMap.get(RevColorSensorV3.class, config.sl0);
        slot1 = hardwareMap.get(ColorSensor.class, config.sl1);
        slot2 = hardwareMap.get(ColorSensor.class, config.sl2);
        //slot0.enableLed(true);
        slot1.enableLed(true);
        slot2.enableLed(true);
        spindexerState = new SpindexerState();
        firingServo = hardwareMap.get(Servo.class, config.firingServoName);
        RealSpindexerState = SpindexerRotationalState.INIT;
        flickerServoState = FlickerServoState.INIT;
        firingServo.resetDeviceConfigurationForOpMode();
        starboardServo.resetDeviceConfigurationForOpMode();
    }



    public enum FlickerServoState{
        FIRE,RELOAD,INIT
    }
    public  void fireFlickerServo(){
        firingServo.setPosition(config.firingServoFirePose);
        flickerServoState = FlickerServoState.FIRE;
    }
    public void reloadFlickerServo(){
        firingServo.setPosition(config.firingServoReloadPose);
        flickerServoState = FlickerServoState.RELOAD;
    }

    public FlickerServoState getFlickerState(){
        return flickerServoState;
    }
    public enum SpindexerRotationalState{
        SLOT0PICKUP, SLOT1PICKUP, SLOT2PICKUP, SLOT0FIRE, SLOT1FIRE,SLOT2FIRE, INIT
    }
    public double getPosition(){
        return starboardServo.getPosition();
    }
    public void setPosition(double pose){
        starboardServo.setPosition(pose);
    }
    public void PickupPoseSlot0(){
        starboardServo.setPosition(config.slot0Pickup);
        setState(SpindexerRotationalState.SLOT0PICKUP);
    }
    public void PickupPoseSlot1(){
        starboardServo.setPosition(config.slot1Pickup);
        setState(SpindexerRotationalState.SLOT1PICKUP);
    }
    public void PickupPoseSlot2(){
        starboardServo.setPosition(config.slot2Pickup);
        setState(SpindexerRotationalState.SLOT2PICKUP);
    }
    public void FirePoseSlot0(){
        starboardServo.setPosition(config.slot0FiringPosition);
        setState(SpindexerRotationalState.SLOT0FIRE);
    }
    public void FirePoseSlot1(){
        starboardServo.setPosition(config.slot1FiringPosition);
        setState(SpindexerRotationalState.SLOT1FIRE);
    }
    public void FirePoseSlot2(){
        starboardServo.setPosition(config.slot2FiringPosition);
        setState(SpindexerRotationalState.SLOT2FIRE);
    }
    public void setState(SpindexerRotationalState givenState){
        RealSpindexerState = givenState;
    }
    public SpindexerRotationalState getState(){
        return RealSpindexerState;
    }
    public color getBallColorImmediately(){
            NormalizedRGBA c = slot0.getNormalizedColors();
            double d = slot0.getDistance(DistanceUnit.MM);

            if (d > 30.0) {
                return color.UNDECTED;
            }

            // Prevents division by zero or negative alpha values
            float alphaDivisor = Math.max(c.alpha, 1.0f);
            float r = c.red / alphaDivisor;
            float g = c.green / alphaDivisor;
            float b = c.blue / alphaDivisor;

            if ((g / r) > 2.0 && g > b) {
                return color.GREEN;
            } else if ((b / g) > 1.3 && b > r) {
                return color.PURPLE;
            }

            return color.UNDECTED;

    }
    public double getFlickerServoPose(){
        return firingServo.getPosition();
    }
    public void rapidFire3(){
        ElapsedTime timer =  new ElapsedTime();
        timer.startTime();

    }

}
