package org.firstinspires.ftc.teamcode.Configs;

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
    public final String stbSv = "starboardServo";
    public final String ptSv = "portServo";
    public final String sl0 = "slot0";
    public final String sl1 = "slot1";

    public final String sl2 = "slot2";

    public final double slot0ServoPosition = 0.33;
    public final double slot1ServoPosition = 0.67;
    public final double slot2ServoPosition = 1.0;


}
