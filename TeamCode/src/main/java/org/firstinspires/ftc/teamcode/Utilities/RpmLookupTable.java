package org.firstinspires.ftc.teamcode.Utilities;

import androidx.annotation.NonNull;

import java.util.NavigableMap;
import java.util.TreeMap;

public class RpmLookupTable {
    NavigableMap<Integer,Integer> distanceAndRpm = new TreeMap<>();
    public RpmLookupTable(){

        //k is distance and v is rpm
        distanceAndRpm.put(45,2550);
        distanceAndRpm.put( 51, 2600);
        distanceAndRpm.put(56, 2650);
        distanceAndRpm.put(61, 2650);
        distanceAndRpm.put(66,2750);
        distanceAndRpm.put(71, 2800);
        distanceAndRpm.put(76, 2850);
        distanceAndRpm.put(81, 2950);
        distanceAndRpm.put(86, 2950);
        distanceAndRpm.put(92,3000);
        distanceAndRpm.put(98,3150);
        distanceAndRpm.put(105, 3250);
        distanceAndRpm.put(110, 3300);
        distanceAndRpm.put(117, 3550);
        distanceAndRpm.put(123, 3700);
        distanceAndRpm.put(130, 3800);
        distanceAndRpm.put(135, 3850);
        distanceAndRpm.put(140, 3900);
        distanceAndRpm.put(145, 3950);
        distanceAndRpm.put(150, 4000);
        //single wheel
        /*distanceAndRpm.put(144, 4350);
        distanceAndRpm.put(136, 4250);
        distanceAndRpm.put(128, 4100);
        distanceAndRpm.put(118, 4000);
        distanceAndRpm.put(107, 3850);
        distanceAndRpm.put(97, 3700);
        distanceAndRpm.put(85, 3650);
        distanceAndRpm.put(64, 3450);*/


    }
    public int getRpm(@NonNull int target){
        Integer floorKey = distanceAndRpm.floorKey(target);
        Integer ceilingKey = distanceAndRpm.ceilingKey(target);

        if(ceilingKey == null){
            return distanceAndRpm.get(floorKey);
        }
        else if(floorKey == null){
            return distanceAndRpm.get(ceilingKey);
        }
        if(Math.abs(target - floorKey) <= Math.abs(target - ceilingKey)){
            return distanceAndRpm.get(floorKey);
        }
        else{
            return distanceAndRpm.get(ceilingKey);
        }




    }
}
