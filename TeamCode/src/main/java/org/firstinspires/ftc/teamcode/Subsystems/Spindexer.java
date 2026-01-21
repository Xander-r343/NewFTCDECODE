package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Configs.Config;

import kotlin.jvm.JvmField;

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
    private double eta;
    Telemetry telemetry;
    int FiringState = 0;

    public enum color{
        PURPLE, GREEN, UNDECTED
    }
    public enum SpindexerRotationalState{
        SLOT_0_PICKUP,
        MOVING_TO_SLOT_0_PICKUP,
        SLOT_1_PICKUP,
        MOVING_TO_SLOT_1_PICKUP,
        SLOT_2_PICKUP,
        MOVING_TO_SLOT_2_PICKUP,
        SLOT_0_FIRE,
        MOVING_TO_SLOT_0_FIRE,
        SLOT_1_FIRE,
        MOVING_TO_SLOT_1_FIRE,
        SLOT_2_FIRE,
        MOVING_TO_SLOT_2_FIRE,
        INIT
    }

    SpindexerRotationalState firstFirePosition = SpindexerRotationalState.SLOT_0_FIRE;
    SpindexerRotationalState secondFirePosition = SpindexerRotationalState.SLOT_1_FIRE;
    SpindexerRotationalState thirdFirePosition = SpindexerRotationalState.SLOT_2_FIRE;

    private double[] servoAngleLookupTable = {
                Config.slot0Pickup, //SLOT0PICKUP,
                0.0, // MOVING_TO_SLOT_0_PICKUP,
                Config.slot1Pickup, //SLOT1PICKUP,
                0.0, //MOVING_TO_SLOT_1_PICKUP,
                Config.slot2Pickup, //SLOT2PICKUP,
                0.0, //MOVING_TO_SLOT_2_PICKUP,
                Config.slot0FiringPosition, //SLOT0FIRE,
                0.0, //MOVING_TO_SLOT_0_FIRE,
                Config.slot1FiringPosition, //SLOT1FIRE,
                0.0, //MOVING_TO_SLOT_1_FIRE,
                Config.slot2FiringPosition, //SLOT2FIRE,
                0.0, //MOVING_TO_SLOT_2_FIRE,
                0.5 //INIT
    };
    public enum FlickerServoState{
        RELOADED, FIRING, RELOADING,INIT
    }
    Config config;
    public double TimestampSpindexer;
    public double TimestampFlicker;
    ///
    ///          |
    ///          |
    ///       1  o    2
    ///        /   \
    ///       / 0   \
    ///     /        \ slots are unmoving
    ///      intake side
    ///        firing side

    public Spindexer(HardwareMap hardwareMap, ElapsedTime Givenruntime, Telemetry givenTelemetry){
        config = new Config();
        //initialize servos
        spindexerServo = hardwareMap.get(Servo.class, config.stbSv);
        spindexerServo.resetDeviceConfigurationForOpMode();
        //initialize color sensors
        slot0 = hardwareMap.get(RevColorSensorV3.class, config.sl0);
        //slot0.enableLed(true);
        firingServo = hardwareMap.get(Servo.class, config.firingServoName);
        currentSpindexerState = SpindexerRotationalState.INIT;
        previousSpindexerState = SpindexerRotationalState.INIT;
        flickerServoState = FlickerServoState.RELOADED;
        firingServo.resetDeviceConfigurationForOpMode();
        spindexerServo.resetDeviceConfigurationForOpMode();
        TimestampSpindexer = 0;
        TimestampFlicker = 0;
        runtime = Givenruntime;
        telemetry = givenTelemetry;
    }

    /**
     * updates the spindexer and flicker states by checking if they have reached their destination
     * run this every time in loop()
     */
    public void updateState()
    {
        // Is the spindexer in a moving state?
        if(     currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_FIRE   ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_FIRE   ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_FIRE   ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_0_PICKUP ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_1_PICKUP ||
                currentSpindexerState == SpindexerRotationalState.MOVING_TO_SLOT_2_PICKUP)
        {
            // If the elapsed time to reach position has been reached, update the state
            //if the time to run to pos is less than the time since start,
            if(runtime.seconds() > TimestampSpindexer + eta)
            {
                previousSpindexerState = currentSpindexerState;
                switch (currentSpindexerState) {
                    case MOVING_TO_SLOT_0_FIRE:
                        currentSpindexerState = SpindexerRotationalState.SLOT_0_FIRE;
                        break;
                    case MOVING_TO_SLOT_1_FIRE:
                        currentSpindexerState = SpindexerRotationalState.SLOT_1_FIRE;
                        break;
                    case MOVING_TO_SLOT_2_FIRE:
                        currentSpindexerState = SpindexerRotationalState.SLOT_2_FIRE;
                        break;
                    case MOVING_TO_SLOT_0_PICKUP:
                        currentSpindexerState = SpindexerRotationalState.SLOT_0_PICKUP;
                        break;
                    case MOVING_TO_SLOT_1_PICKUP:
                        currentSpindexerState = SpindexerRotationalState.SLOT_1_PICKUP;;
                        break;
                    case MOVING_TO_SLOT_2_PICKUP:
                        currentSpindexerState = SpindexerRotationalState.SLOT_2_PICKUP;;
                        break;
                }

            }
        }
        // Check and reset flicker state based on HW

        if(     (runtime.seconds() > (TimestampFlicker + config.timeForFlickInSeconds)) &&
                flickerServoState == FlickerServoState.FIRING){
            reloadFlickerServo();
        }

        else if((runtime.seconds() > (TimestampFlicker + config.timeForFlickInSeconds)) &&
                flickerServoState == FlickerServoState.RELOADING){
            flickerServoState = FlickerServoState.RELOADED;
        }


    }
    public void fireFlickerServo(){
        TimestampFlicker = runtime.seconds();
        if(flickerServoState == FlickerServoState.RELOADED) {
            firingServo.setPosition(config.firingServoFirePose);
            flickerServoState = FlickerServoState.FIRING;
        }
        else {
            float zero = 0.0f;
            float bad_val = 1.0f / zero;
        }
    }
    public void reloadFlickerServo(){
        TimestampFlicker = runtime.seconds();
        firingServo.setPosition(config.firingServoReloadPose);
        flickerServoState = FlickerServoState.RELOADING;
    }
    public FlickerServoState getFlickerState(){
        return flickerServoState;
    }
    public double getPosition(){
        return spindexerServo.getPosition();
    }


    /**
     * @param newPos give the spindexer a new pos to move to
     */
    public void moveSpindexerToPos(SpindexerRotationalState newPos)
    {
        TimestampSpindexer = runtime.seconds();
        eta = getETAtoNewPositionInSeconds(newPos, currentSpindexerState);
        // add in the offset. Maybe call getETAtoNewPositionInSeconds ??
        previousSpindexerState = currentSpindexerState;
        spindexerServo.setPosition(servoAngleLookupTable[newPos.ordinal()]);
        switch (newPos) // assign "moving" to the current state
        {
            case SLOT_0_FIRE: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_0_FIRE; break;
            case SLOT_1_FIRE: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_1_FIRE; break;
            case SLOT_2_FIRE: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_2_FIRE; break;
            case SLOT_0_PICKUP: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_0_PICKUP; break;
            case SLOT_1_PICKUP: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_1_PICKUP; break;
            case SLOT_2_PICKUP: currentSpindexerState = SpindexerRotationalState.MOVING_TO_SLOT_2_PICKUP; break;
            // fill this out
        }

    }
    //delete these below


    /**
     * gets the number of seconds to get to the next position
     * @return seconds assuming spindexer is not jammed
     */
    private double getETAtoNewPositionInSeconds(SpindexerRotationalState currentState, SpindexerRotationalState target){
        //first get the distance or number of slots we are traveling

        // replace these with the table
        return config.timePerDegInSeconds * Math.abs(servoAngleLookupTable[currentState.ordinal()] -
                                                     servoAngleLookupTable[target.ordinal()]);
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
    //commands
    public boolean fire3Balls(){
        boolean b3BallsFired = false;

        // ensure we have the latest spindexer and flicker state positions
        updateState();

        switch (FiringState){
            case 0:
                // Setup the state needed to fire the balls in the right order
                // read motif, program firing order
                firstFirePosition = SpindexerRotationalState.SLOT_0_FIRE;
                secondFirePosition = SpindexerRotationalState.SLOT_1_FIRE;
                thirdFirePosition = SpindexerRotationalState.SLOT_2_FIRE;
                FiringState = 1;
            case 1: // Start spindexer moving
                moveSpindexerToPos(firstFirePosition);
                FiringState = 2;
            case 2: //check to see if spindexer is finished moving
                if(currentSpindexerState == firstFirePosition){
                    FiringState = 3;
                }
                else
                    break; // Haven't reached the right spindexer state yet, wait
            case 3: // Fire first ball
                fireFlickerServo();
                FiringState = 4;
            case 4: // Wait for first fire to finish
                if(getFlickerState() == FlickerServoState.RELOADED){
                    FiringState = 5;
                }
                break;
            case 5: // Start spindexer moving state 2
                moveSpindexerToPos(secondFirePosition);
                FiringState = 6;
            case 6: //check to see if spindexer is finished moving
                if(currentSpindexerState == secondFirePosition){
                    FiringState = 7;
                }
                else
                    break; // Haven't reached the right spindexer state yet, wait
            case 7: // Fire second ball
                fireFlickerServo();
                FiringState = 4;
            case 8: // Wait for first fire to finish
                if(getFlickerState() == FlickerServoState.RELOADED){
                    FiringState = 9;
                }
                break;
            case 9: // Start spindexer moving
                moveSpindexerToPos(thirdFirePosition);
                FiringState = 10;
            case 10: //check to see if spindexer is finished moving
                if(currentSpindexerState == thirdFirePosition){
                    FiringState = 11;
                }
                else
                    break; // Haven't reached the right spindexer state yet, wait
            case 11: // Fire third ball
                fireFlickerServo();
                FiringState = 12;
            case 12: // Wait for last fire to finish
                if(getFlickerState() == FlickerServoState.RELOADED){
                    FiringState = 100;
                }
                break;

            case 100:
                FiringState = 0;
                b3BallsFired = true;
                break;
        }
        telemetry.addData("state", FiringState);
        telemetry.update();
        return b3BallsFired;

    }





}
