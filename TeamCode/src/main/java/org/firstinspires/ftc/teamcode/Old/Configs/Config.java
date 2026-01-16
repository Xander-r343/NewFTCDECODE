package org.firstinspires.ftc.teamcode.Old.Configs;

public class Config {
//
    // Constants

    public static final int NoAlliance = 0;
    public static final int BlueAlliance = 2;
    public static final int RedAlliance = 3;


    //drivetrain
    public static final String backLeft = "backLeft";
    public static final String frontLeft = "frontLeft";
    public static final String backRight = "backRight";
    public static final String frontRight = "frontRight";
    //-------------------------------------------------------------------------
    //Shotter class
    //-------------------------------------------------------------------------
    //flywheel stuff
    public static final String MainFlywheelMotorName = "shooter";
    public static final int ticksPerRevFlywheel = 28;
    public static final String sbBeltName = "sb";
    public static final String portBeltName = "port";
    public static final double beltmotorTicksPerRev = 537.7;

    //feedingServo values and names
    public static final String FeedingServoName = "feedingServo";
    public static final double openValue = 1;
    public static final double closedValue = 0;
    //intake values
    public static final String IntakeMotorName = "intake";
    //servo
    public static final String GateServoName = "gateServo";



    //pathing values
    public static final double RedAllianceTargetX = 144;
    public static final double RedAllianceTargetY = 144;
    public static final double BlueAllianceTargetY = 144;
    public static final double BlueAllianceTargetX = 0;

    //blackboard keys
    public static final String AllianceKey = "allianceKey";
    public static final String HeadingKey = "headingKey";
    public static final String Xkey = "xKey";
    public static final String Ykey = "ykey";

    // rgb
    public static final String RBGName = "rgb";
    public static final double Green = 0.5;
    public static final double Red = 0.277;

    //pindexer
    //time for axon spindexer with MAX mk2 to move 1 slot position
    public static final double timePerDegInSeconds =1.2/360;
    //flicker servo time same thing
    public static final double timeForFlickInSeconds = 0.3;


    public static final String firingServoName = "firingServo";
    public static final double firingServoFirePose = 1;
    public static final double firingServoReloadPose = 0;


    public static final String stbSv = "starboardServo";
    public static final String ptSv = "portServo";
    public static final String sl0 = "slot0";
    public static  String sl1 = "slot1";

    public static final String sl2 = "slot2";

    //TODO if the ratio of the gears is 40:30 the servo needs to be 270degress of rotation
    public static final double slot0FiringPosition = 0.725;
    public static final double slot1FiringPosition = 0;
    public static final double slot2FiringPosition = 0.35;

    public static final double slot0Pickup = 0.15;
    public static final double slot1Pickup = 0.5;
    public static final double slot2Pickup = 0.9;
    //degrees
    public static final double slot0FiringPositionDegrees = 257.375;
    public static final double slot1FiringPositionDegrees = 0.0;
    public static final double slot2FiringPositionDegrees = 124.25;
    public static  double slot0PickupDegrees = 53.25;
    public static final double slot1PickupDegrees = 177.5;
    public static final double slot2PickupDegrees = 319.5;

    //turret
    //Todo fill out
    public static final String turretRotationName = "turretRotater";
    public static final String leftHoodServo = "leftHood";
    public static final String rightHoodServo = "rightHood";
    public static final double hoodMinimumLaunchAngle = 30;
    public static final double hoodMaximumLaunchAngle = 70;
    public static  int HoodservoRange = 120;//degrees

    public static final int teethOnTurretGear = 100;
    public static final int teethOnMotorGear = 15;
    public static  double ticksPerRevTurretMotor = 360;
    public static final double turretTicksLowerLimit = -346;
    public static final double turretTicksUpperLimit = 346;
    public static final double ticksPerDegree = 3584.6/360;


    //flywheel names
    public static final String newRobotLeftFlywheel = "leftFlywheel";
    public static final String newRobotRightFlywheel = "rightFlywheel";
    public static final double velScalar = 1.0;
    public static final double angularVelScalar = 1.0;
}
