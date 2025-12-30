package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.GREEN;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.PURPLE;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.UNDECTED;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Retired.SpindexerState;

import java.util.Arrays;
import java.util.HashMap;

public class Spindexer {
    private Servo starboardServo;
    private Servo firingServo;
    private ColorSensor slot0;
    private ColorSensor slot1;
    private ColorSensor slot2;
    public SpindexerState spindexerState;
    private SpindexerRotationalState RealSpindexerState;
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
        slot0 = hardwareMap.get(ColorSensor.class, config.sl0);
        slot1 = hardwareMap.get(ColorSensor.class, config.sl1);
        slot2 = hardwareMap.get(ColorSensor.class, config.sl2);
        slot0.enableLed(true);
        slot1.enableLed(true);
        slot2.enableLed(true);
        spindexerState = new SpindexerState();
        firingServo = hardwareMap.get(Servo.class, config.firingServoName);

        RealSpindexerState = SpindexerRotationalState.INIT;
    }
    /**
     * spins the spindexer to a slot
     * @param givenSlot is a number between 0-2 and is the wanted spindexer slot
     */
    /**
     * returns the color value
     * @param givenSlot specified slot to read
     * @return the color enum
     */
    public color readSlotColor(int givenSlot) {
        color toReturn = color.UNDECTED;
        if (givenSlot == 0) {
            double redValue = slot0.red();
            double greenValue = slot0.green();
            double blueValue = slot0.blue();
            if (blueValue > redValue && blueValue > greenValue) {
                // Detected purple (adjust thresholds as needed)
                toReturn = color.PURPLE;
            } else if (greenValue > redValue && greenValue > blueValue) {
                // Detected green (adjust thresholds as needed)
                toReturn  = GREEN;
            }
            else{
                toReturn  = color.UNDECTED;
            }
        }
        else if (givenSlot == 1) {
            double redValue = slot1.red();
            double greenValue = slot1.green();
            double blueValue = slot1.blue();
            if (blueValue > redValue && blueValue > greenValue) {
                // Detected purple (adjust thresholds as needed)
                toReturn = color.PURPLE;
            } else if (greenValue > redValue && greenValue > blueValue) {
                // Detected green (adjust thresholds as needed)
                toReturn  = GREEN;
            }
            else{
                toReturn  = color.UNDECTED;
            }
        }
        else if (givenSlot == 2) {
            double redValue = slot2.red();
            double greenValue = slot2.green();
            double blueValue = slot2.blue();
            if (blueValue > redValue && blueValue > greenValue) {
                // Detected purple (adjust thresholds as needed)
                toReturn = color.PURPLE;
            } else if (greenValue > redValue && greenValue > blueValue) {
                // Detected green (adjust thresholds as needed)
                toReturn  = GREEN;
            }
            else{
                toReturn  = color.UNDECTED;
            }
        }
        return toReturn;
    }
    public boolean slotIsFull(int givenSlot){
        return readSlotColor(givenSlot) != color.UNDECTED;
    }
    /**
     * returns the ideal routing of slots to get pattern/motif
     * @param givenFirstBall first ball color in the motif
     * @param givenSecondBall second ball color in motif
     * @param givenThirdBall etc.
     * @return int [] of the slots
     */
    public int[] determineBestSpinRoute(@NonNull color givenFirstBall, @NonNull color givenSecondBall, @NonNull color givenThirdBall){
        int[] order = new int[2];
        spindexerState.setSlotState(0, readSlotColor(0));
        spindexerState.setSlotState(1, readSlotColor(1));
        spindexerState.setSlotState(2, readSlotColor(2));
        //check if the sorter contains the wanted color
            if(spindexerState.containsColorInSpindexer(givenFirstBall)) {
                //check the sorter for color
                order[0] = spindexerState.getSlotFromColor(givenFirstBall);
                spindexerState.setSlotState(order[0], UNDECTED);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(spindexerState.containsColorInSpindexer(oppositeBall(givenFirstBall))){
                //check the sorter for color
                order[0] = spindexerState.getSlotFromColor(oppositeBall(givenFirstBall));
                spindexerState.setSlotState(order[0], UNDECTED);
            }
            //add safe for if color is undetected
            else{
                order[1] = Integer.parseInt(null);
            }
            //second ball
            if(spindexerState.containsColorInSpindexer(givenSecondBall)) {
                //check the sorter for color
                order[1] = spindexerState.getSlotFromColor(givenSecondBall);
                spindexerState.setSlotState(order[1], UNDECTED);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(spindexerState.containsColorInSpindexer(oppositeBall(givenFirstBall))){
                //check the sorter for color
                order[1] = spindexerState.getSlotFromColor(oppositeBall(givenFirstBall));
                spindexerState.setSlotState(order[1], UNDECTED);
            }
            //add safe for if color is undetected
            else{
                order[1] = Integer.parseInt(null);
            }
            //last ball
            if(spindexerState.containsColorInSpindexer(givenThirdBall)) {
             //check the sorter for color
                 order[2] = spindexerState.getSlotFromColor(givenThirdBall);
                spindexerState.setSlotState(order[2], UNDECTED);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(spindexerState.containsColorInSpindexer(oppositeBall(givenThirdBall))){
                 //check the sorter for color
                order[2] = spindexerState.getSlotFromColor(oppositeBall(givenThirdBall));
                 spindexerState.setSlotState(order[2], UNDECTED);
            }
            //add safe for if color is undetected
            else{
                order[2] = Integer.parseInt(null);
            }
            //return oder in slot #'s with int []
        return order;
    }
    private color oppositeBall(color currentBall){
        if(currentBall == GREEN){
            return PURPLE;
        }
        else if(currentBall == PURPLE){
            return GREEN;
        }
        else{
            return UNDECTED;
        }

    }

    /**
     * fires a ball
     * @param givenTimeInMilliseconds is the time in between fire and reload
     */
    public void FireBall(long givenTimeInMilliseconds){
        ElapsedTime timer = new ElapsedTime();
        timer.startTime();
        firingServo.setPosition(config.firingServoFirePose);
        while(timer.milliseconds() < givenTimeInMilliseconds+11){
            if(timer.milliseconds() > givenTimeInMilliseconds){
                firingServo.setPosition(config.firingServoReloadPose);
            }

        }
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
}
