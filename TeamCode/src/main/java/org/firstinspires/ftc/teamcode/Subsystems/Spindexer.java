package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configs.Config;

public class Spindexer {
    public ElapsedTime runtime;
    private Servo spindexerServo;
    private Servo firingServo;
    private RevColorSensorV3 slot0;
    private ColorSensor slot1;
    private ColorSensor slot2;
    private SpindexerRotationalState previousSpindexerState;
    private SpindexerRotationalState currentSpindexerState;
    private FlickerServoState flickerServoState;
    public enum color{
        PURPLE, GREEN, UNDECTED
    }
    public enum SpindexerRotationalState{
        SLOT0PICKUP, MOVING_TO_SLOT_0_PICKUP,
        SLOT1PICKUP, MOVING_TO_SLOT_1_PICKUP,
        SLOT2PICKUP, MOVING_TO_SLOT_2_PICKUP,
        SLOT0FIRE,MOVING_TO_SLOT_0_FIRE,
        SLOT1FIRE,MOVING_TO_SLOT_1_FIRE,
        SLOT2FIRE,MOVING_TO_SLOT_2_FIRE, INIT
    }
    public enum FlickerServoState{
        FIRE,MOVING,RELOAD,INIT
    }
    Config config;
    public double TimestampSpindexer;
    public double TimestampFlicker;
    ///          |
    ///          |
    ///       1  o    2
    ///        /   \
    ///       / 0   \
    ///     /        \ slots are unmoving
    ///      intake side
    ///        firing side

    public Spindexer(HardwareMap hardwareMap, ElapsedTime Givenruntime){
        config = new Config();
        //initialize servos
        spindexerServo = hardwareMap.get(Servo.class, config.stbSv);
        spindexerServo.resetDeviceConfigurationForOpMode();
        //initialize color sensors
        slot0 = hardwareMap.get(RevColorSensorV3.class, config.sl0);
        //slot0.enableLed(true);
        slot1.enableLed(true);
        firingServo = hardwareMap.get(Servo.class, config.firingServoName);
        currentSpindexerState = SpindexerRotationalState.INIT;
        previousSpindexerState = SpindexerRotationalState.INIT;
        flickerServoState = FlickerServoState.INIT;
        firingServo.resetDeviceConfigurationForOpMode();
        spindexerServo.resetDeviceConfigurationForOpMode();
        TimestampSpindexer = 0;
        TimestampFlicker = 0;
        runtime = Givenruntime;
    }
    public void update(){
        if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_FIRE ||currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_FIRE ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_FIRE || currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_PICKUP ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_PICKUP || currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_PICKUP
        ){
            //if the time to run to pos is less than the time since start,
            if(runtime.seconds() < TimestampSpindexer){
                previousSpindexerState = currentSpindexerState;
                if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_FIRE){
                    currentSpindexerState = SpindexerRotationalState.SLOT0FIRE;
                }
                else if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_FIRE){
                    currentSpindexerState = SpindexerRotationalState.SLOT1FIRE;
                }
                else if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_FIRE){
                    currentSpindexerState = SpindexerRotationalState.SLOT2FIRE;
                }
                if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_PICKUP){
                    currentSpindexerState = SpindexerRotationalState.SLOT0PICKUP;
                }
                else if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_PICKUP){
                    currentSpindexerState = SpindexerRotationalState.SLOT1PICKUP;
                }
                else if(currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_PICKUP){
                    currentSpindexerState = SpindexerRotationalState.SLOT2PICKUP;
                }
            }
        }
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
    public double getPosition(){
        return spindexerServo.getPosition();
    }
    public void setPosition(double pose){
        spindexerServo.setPosition(pose);
    }
    public void PickupPoseSlot0(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot0Pickup);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_0_PICKUP;
    }
    public void PickupPoseSlot1(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot1Pickup);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_1_PICKUP;
    }
    public void PickupPoseSlot2(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot2Pickup);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_2_PICKUP;
    }
    public void FirePoseSlot0(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot0FiringPosition);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_0_FIRE;
    }
    public void FirePoseSlot1(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot1FiringPosition);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_1_FIRE;
    }
    public void FirePoseSlot2(){
        TimestampSpindexer = runtime.seconds();
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(config.slot2FiringPosition);
        currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_2_FIRE;
    }

    /**
     * gets the number of seconds to get to the next position
     * @return seconds assuming spindexer is not jammed
     */
    private double getETAtoNewPositionInSeconds(){
        //first get the distance or number of slots we are traveling
        double targetDegrees =  getDegreesFromEnum(currentSpindexerState);
        double currentDegrees =  getDegreesFromEnum(previousSpindexerState);
        return config.timePerDegInSeconds * Math.abs(targetDegrees-currentDegrees);
    }
    public SpindexerRotationalState getState(){
        return currentSpindexerState;
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

    public double getDegreesFromEnum(SpindexerRotationalState givenState){
        if(givenState == SpindexerRotationalState.SLOT0FIRE){
            return config.slot0FiringPositionDegrees;
        }
        else if(givenState == SpindexerRotationalState.SLOT1FIRE){
            return config.slot1FiringPositionDegrees;
        }
        else if(givenState == SpindexerRotationalState.SLOT2FIRE){
            return config.slot2FiringPositionDegrees;
        }
        if(givenState == SpindexerRotationalState.SLOT0PICKUP){
            return config.slot0PickupDegrees;
        }
        else if(givenState == SpindexerRotationalState.SLOT1PICKUP){
            return config.slot1PickupDegrees;
        }
        else if(givenState == SpindexerRotationalState.SLOT2PICKUP){
            return config.slot2PickupDegrees;
        }
        else{
            return 0.0;
        }
    }


}
