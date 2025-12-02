package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.GREEN;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.PURPLE;
import static org.firstinspires.ftc.teamcode.Subsystems.Spindexer.color.UNDECTED;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs.Config;

import java.util.Arrays;
import java.util.HashMap;

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
            portServo.setPosition(1-config.slot0ServoPosition);
        }
        else if(givenSlot == 1){
            starboardServo.setPosition(config.slot1ServoPosition);
            portServo.setPosition(1-config.slot1ServoPosition);
        }
        else if(givenSlot == 2){
            starboardServo.setPosition(config.slot2ServoPosition);
            portServo.setPosition(1-config.slot2ServoPosition);
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
     * @param givenFirstBall first ball color in the motif
     * @param givenSecondBall second ball color in motif
     * @param givenThirdBall etc.
     * @return int [] of the slots
     *
     */
    public int[] determineBestSpinRoute(color givenFirstBall, color givenSecondBall, color givenThirdBall){
        int[] order = new int[2];
        HashMap <color,Integer> slotColors = new HashMap();

        slotColors.put(readSlotColor(0), 0);
        slotColors.put(readSlotColor(1), 1);
        slotColors.put(readSlotColor(2), 2);
       //check if the sorter contains the wanted color
            if(slotColors.containsKey(givenFirstBall)) {
                //check the hashmap for color
                order[0] = slotColors.get(givenFirstBall);
                //delete that color from the hasmap
                slotColors.remove(order[0], givenFirstBall);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(slotColors.containsKey(oppositeBall(givenFirstBall))){
                //check the hashmap for color
                order[0] = slotColors.get(oppositeBall(givenFirstBall));
                //delete that color from the hasmap
                slotColors.remove(order[0], oppositeBall(givenFirstBall));
            }
            //second ball
            if(slotColors.containsKey(givenSecondBall)) {
                order[1] = slotColors.get(givenSecondBall);
                slotColors.remove(order[1], givenSecondBall);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(slotColors.containsKey(oppositeBall(givenSecondBall))) {
                order[1] = slotColors.get(oppositeBall(givenSecondBall));
                slotColors.remove(order[1], oppositeBall(givenSecondBall));
            }
            //add safe for if color is undetected
            else{
                order[1] = Integer.parseInt(null);
            }
            //last ball
            if(slotColors.containsKey(givenThirdBall)) {
                order[1] = slotColors.get(givenThirdBall);
                slotColors.remove(order[1], givenSecondBall);
            }
            //if the sorter doesn't contain the wanted color then use opposite color ball
            else if(slotColors.containsKey(oppositeBall(givenSecondBall))) {
                order[1] = slotColors.get(oppositeBall(givenThirdBall));
                slotColors.remove(order[1], oppositeBall(givenThirdBall));
            }
            //add safe for if color is undetected
            else{
                order[1] = Integer.parseInt(null);
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
}
