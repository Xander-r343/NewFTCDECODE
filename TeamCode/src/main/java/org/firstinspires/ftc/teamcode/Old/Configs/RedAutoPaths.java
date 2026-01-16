package org.firstinspires.ftc.teamcode.Old.Configs;

public class RedAutoPaths {
    ///pickup for red auto with 2 artifacts
    public final double[] closePickupPose = {29,36,-180};
    ///starting position for red auto
    public final double[] startingPose = {9, 56, 90};
    ///between start and pickup
    public final double [] transitionBtwnPark_Pickup1 = {56,36,-180};
    ///launch position 1
    public final double[] launchPose1 = {84, 56, 90};//used to be 37
    /// rpm for launch position 1
    public final int rpmLaunchPose1 = 3850;
    ///pickup spot 2, doesn't need a transition path if used right after launch pose 1
    //recommended to turn before pickup
    public final double [] farPickupPose = {29,84,-180};

    ///coordinates for the middle pickup position
    public final double [] middlePickupPose = {29,60,-180};
    ///transition btwn park and stuff
    public final double [] transitionBtwnpickupAndLine2 = {56,60,-180};
    ///

    public RedAutoPaths(){

    }
}
