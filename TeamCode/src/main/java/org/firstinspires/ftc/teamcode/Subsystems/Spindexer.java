package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.GREEN;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs.Config;

import java.util.Arrays;

public class Spindexer {
    private Servo starboardServo;
    private Servo portServo;
    private ColorSensor slot0;
    private ColorSensor slot1;
    private ColorSensor slot2;
    public enum color{
        PURPLE, GREEN, UNDECTED
    }
    public enum motif{
        PPG, PGP, GPP
    }
    public color slot0status;
    public color slot1status;

    public color slot2status;


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
        portServo = hardwareMap.get(Servo.class, config.ptSv);
        //initialize color sensors
        slot0 = hardwareMap.get(ColorSensor.class, config.sl0);
        slot1 = hardwareMap.get(ColorSensor.class, config.sl1);
        slot2 = hardwareMap.get(ColorSensor.class, config.sl2);
        slot0.enableLed(true);
        slot1.enableLed(true);
        slot2.enableLed(true);

    }

    /**
     * spins the spindexer to a slot
     * @param givenSlot is a number between 0-2 and is the wanted spindexer slot
     */
    public void spinToPose(int givenSlot){
        if(givenSlot == 0){
            starboardServo.setPosition(config.slot0ServoPosition);
        }
        else if(givenSlot == 1){
            starboardServo.setPosition(config.slot1ServoPosition);
        }
        else if(givenSlot == 2){
            starboardServo.setPosition(config.slot2ServoPosition);
        }
        else{

        }
    }
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
     * @param currentMotif is the current motif using enum
     * @return int [] of the slots
     */
    public int[] determineBestSpinRoute(motif currentMotif){
        int[] order = new int[2];
        color[] slotColors = new color[2];
        for(int i = 0; i < 2; i++){
            slotColors [i] = readSlotColor(i);
        }
        //check for each motif
        //TODO fix this control logic, it won't work
        /*if(currentMotif == motif.GPP){
            if(slotColors[0] == color.GREEN){
                order[0] = 0;
            }
            else if(slotColors[1] == color.GREEN){
                order[0] = 1;
            }
            else if(slotColors[2] == color.GREEN){
                order[0] = 2;

            }
            else{
                order[0] = 2;
            }
            if(slotColors[0] == color.PURPLE){
                order[1] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[1] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }
            //purple again
            if(slotColors[0] == color.PURPLE){
                order[1] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[1] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }
        }
        else if(currentMotif == motif.PGP){
            if(slotColors[0] == color.PURPLE){
                order[0] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[0] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[0] = 2;

            }
            else{
                order[0] = 2;
            }
            if(slotColors[0] == color.GREEN){
                order[1] = 0;
            }
            else if(slotColors[1] == color.GREEN){
                order[1] = 1;
            }
            else if(slotColors[2] == color.GREEN){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }
            if(slotColors[0] == color.PURPLE){
                order[1] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[1] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }
        }
        else if(currentMotif == motif.PPG){
            if(slotColors[0] == color.PURPLE){
                order[0] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[0] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[0] = 2;

            }
            else{
                order[0] = 2;
            }
            if(slotColors[0] == color.PURPLE){
                order[1] = 0;
            }
            else if(slotColors[1] == color.PURPLE){
                order[1] = 1;
            }
            else if(slotColors[2] == color.PURPLE){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }
            if(slotColors[0] == color.GREEN){
                order[1] = 0;
            }
            else if(slotColors[1] == color.GREEN){
                order[1] = 1;
            }
            else if(slotColors[2] == color.GREEN){
                order[1] = 2;

            }
            else{
                order[1] = 2;
            }

        }


         */
        if(currentMotif == motif.GPP){
        }
        return order;
    }
}
