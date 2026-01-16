package org.firstinspires.ftc.teamcode.New.Util;

import com.pedropathing.geometry.Pose;

public class Alliance {
    public static Alliance BLUE = new Alliance(new Pose(0.0,144.0));
    public static Alliance RED = new Alliance(new Pose(144.0,144.0));
    public Pose goalPose;
    private Alliance(Pose gPose) {
        goalPose = gPose;
    }
}
