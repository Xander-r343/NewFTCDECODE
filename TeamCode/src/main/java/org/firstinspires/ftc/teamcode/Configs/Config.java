package org.firstinspires.ftc.teamcode.Configs;

import org.firstinspires.ftc.teamcode.Subsystems.Turret;

public class Config {
//
    // Constants

    public final int NoAlliance = 0;
    public final int BlueAlliance = 2;
    public final int RedAlliance = 3;


    //drivetrain
    public final String backLeft = "backLeft";
    public final String frontLeft = "frontLeft";
    public final String backRight = "backRight";
    public final String frontRight = "frontRight";
    //-------------------------------------------------------------------------
    //Shotter class
    //-------------------------------------------------------------------------
    //flywheel stuff
    public final String MainFlywheelMotorName = "shooter";
    public final int ticksPerRevFlywheel = 28;
    public final String sbBeltName = "sb";
    public final String portBeltName = "port";
    public final double beltmotorTicksPerRev = 537.7;

    //feedingServo values and names
    public final String FeedingServoName = "feedingServo";
    public final double openValue = 1;
    public final double closedValue = 0;
    //intake values
    public final String IntakeMotorName = "intake";
    //servo
    public final String GateServoName = "gateServo";



    //pathing values
    public final double RedAllianceTargetX = 144;
    public final double RedAllianceTargetY = 144;
    public final double BlueAllianceTargetY = 144;
    public final double BlueAllianceTargetX = 0;

    //blackboard keys
    public final String AllianceKey = "allianceKey";
    public final String HeadingKey = "headingKey";
    public final String Xkey = "xKey";
    public final String Ykey = "ykey";

    // rgb
    public final String RBGName = "rgb";
    public final double Green = 0.5;
    public final double Red = 0.277;

    //pindexer
    //time for axon spindexer with MAX mk2 to move 1 slot position
    public final double timePerDegInSeconds =5/360;
    //flicker servo time same thing
    public final double timeForFlickInSeconds = 0.6;


    public final String firingServoName = "firingServo";
    public final double firingServoFirePose = 1;
    public final double firingServoReloadPose = 0;


    public final String stbSv = "starboardServo";
    public final String ptSv = "portServo";
    public final String sl0 = "slot0";
    public final String sl1 = "slot1";

    public final String sl2 = "slot2";

    //TODO if the ratio of the gears is 40:30 the servo needs to be 270degress of rotation
    public static final double slot0FiringPosition = 0.725;
    public static final double slot1FiringPosition = 0;
    public static final double slot2FiringPosition = 0.35;

    public static final double slot0Pickup = 0.15;
    public static final double slot1Pickup = 0.5;
    public static final double slot2Pickup = 0.9;
    //degrees
    public final double slot0FiringPositionDegrees = 257.375;
    public final double slot1FiringPositionDegrees = 0.0;
    public final double slot2FiringPositionDegrees = 124.25;
    public final double slot0PickupDegrees = 53.25;
    public final double slot1PickupDegrees = 177.5;
    public final double slot2PickupDegrees = 319.5;

    //turret
    //Todo fill out
    public final String turretRotationName = "turretRotater";
    public final String leftHoodServo = "leftHood";
    public final String rightHoodServo = "rightHood";
    public final double hoodMinimumLaunchAngle = 30;
    public final double hoodMaximumLaunchAngle = 70;
    public final int HoodservoRange = 120;//degrees

    public final int teethOnTurretGear = 100;
    public final int teethOnMotorGear = 15;
    public final double ticksPerRevTurretMotor = 360;
    public final double turretTicksLowerLimit = -346;
    public final double turretTicksUpperLimit = 346;
    public final double ticksPerDegree = 3584.6/360;


    //flywheel names
    public final String newRobotLeftFlywheel = "leftFlywheel";
    public final String newRobotRightFlywheel = "rightFlywheel";
    public final double velScalar = 1.0;
    public final double angularVelScalar = 1.0;
    public final String turretKey = "turret";
}
