package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Utilities.AimbotV2;
@Autonomous(name = "Far Zone Red V2.2.1")
public class FarRedV2 extends LinearOpMode {
    Servo rgb;
    ShooterSubsystem robotSubsystem;
    OdoPods pods;
    MecanumDrivetrain drivetrain;
    RedAutoPaths pathDatabase;
    Object Xpos;
    Object Ypos;
    Object Headingpos;
    Object Alliance;
    Config config;
    private AutonomousState AutoState;
    ElapsedTime timer;
    double speed;
    double time;
    Aimbots aimbots;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime pickupTimer = new ElapsedTime();

    int tagID;
    Turret turret;
    Spindexer spindexer;
    double [] values;
    boolean stopAiming = false;
    boolean preparedForPickup = false;
    boolean wentToTheWall = false;
    double pickupBallSpeed = 0.24;
    double regularPathSpeed = 1.0;
    public int motifNumber = Config.PPG;
    boolean ball1pickedUp = false;
    boolean ball2pickedUp = false;

    private enum  AutonomousState{
        SCAN_APRILTAG, INIT, SHOOT_1, DRIVE_TO_PICKUP_FAR, PICKUP_BALL_FAR_PICKUP, DRIVE_BACK_FROM_FAR_PICKUP,
        SHOOT_2, DRIVE_TO_MIDDLE_PICKUP, PICKUP_BALL_MIDDLE_PICKUP, DRIVE_BACK_FROM_MIDDLE_PICKUP,
        PICKUP_CORNER, SHOOT_3, PARK
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        //initialize odopods by using the config class
        pods.setPosition(88, 9, 0);
        timer = new ElapsedTime();
        AutoState = AutonomousState.INIT;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap, runtime, telemetry);
        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
        // store alliance and position info for Teleop
        blackboard.put(config.AllianceKey,config.RedAlliance);
        blackboard.put(config.turretOffsetKey, turret.getTurretPositionDegreesNotOffField());

        spindexer.reloadFlickerServo();
        while(opModeInInit() && motifNumber == 0){
            if(turret.getFidicualResults() != Config.GPP || turret.getFidicualResults() != Config.PGP||
                    turret.getFidicualResults() != Config.PPG || runtime.seconds() > 0.5){
                motifNumber = turret.getFidicualResults();
            }
        }
        waitForStart();
        runtime.reset();
        runtime.startTime();
        timer.reset();

        // Main loop
        while (opModeIsActive()){
            values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
            spindexer.updateState();
            pods.update();
            turret.update();
            blackboard.put(config.turretOffsetKey, turret.getTurretPositionDegreesNotOffField());
            //should this be moved out of the while loop? They are init values, right?
            turret.setServoPoseManaul(0.95);
            if(!stopAiming)
            {
                turret.setFlywheelToRPM(3000);
                turret.turretSetIdealAngleUsingLLandPods();
            }
            switch (AutoState) {

                case INIT:
                    if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && runtime.seconds() > 2){
                        AutoState = AutonomousState.SHOOT_1;
                        break;
                    }
                    else{
                        break;
                    }
                case SHOOT_1:
                    //fire 3 balls
                    //load green in 0 and 2 purples in the other slots TODO
                    if(motifNumber == Config.GPP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_PICKUP_FAR;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PGP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_1_FIRE, Spindexer.SpindexerRotationalState.SLOT_0_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_PICKUP_FAR;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PPG) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_PICKUP_FAR;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    case DRIVE_TO_PICKUP_FAR:
                    //strafe to far pickup and turn to orient spike mark before pickuup
                    if(pods.holdPosition(103, 37.5,-90, regularPathSpeed)){
                        //set intake speed to prepare for ball
                        turret.setIntakeSpeed(1.0);
                        //go to next state to actually pickup
                        //
                         AutoState = AutonomousState.PICKUP_BALL_FAR_PICKUP;
                        break;
                    }else{
                        break;
                    }
                case PICKUP_BALL_FAR_PICKUP:
                    //drive robot into the 3 balls and pick them up
                    if(pods.holdPosition(128, 35.5, -90, pickupBallSpeed)){
                        AutoState = AutonomousState.DRIVE_BACK_FROM_FAR_PICKUP;
                    }else{
                        //auto spin spindexer to next state after detecting a ball
                        if (spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED && pickupTimer.seconds() > 0.3) {
                            //if the spindexer state is 0 and we see a ball move to 1
                            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP ) {
                                pickupTimer.reset();
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                                pods.update();
                                break;
                            }
                            //if the spindexer state is 1 and we see a ball move to 2
                            else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP&& pickupTimer.seconds() > 0.21) {
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
                                pickupTimer.reset();
                                pods.update();
                                break;
                            }
                            //if the spindexer state is 2 and we see a then end this pickup
                            else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP&& pickupTimer.seconds() > 0.3) {
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                                AutoState = AutonomousState.DRIVE_BACK_FROM_FAR_PICKUP;
                                turret.setIntakeSpeed(0);
                                break;
                            }
                            if(pickupTimer.seconds() > 2){
                                AutoState = AutonomousState.DRIVE_BACK_FROM_FAR_PICKUP;
                            }
                        }
                        //make sure odo is driving
                        pods.holdPosition(135, 35.5, -90, pickupBallSpeed);
                        break;
                    }
                case DRIVE_BACK_FROM_FAR_PICKUP:
                    //drive back to the far launch zone triangle after pickup
                    if(pods.holdPosition(90,14,-90,regularPathSpeed)){
                        //go next state after reaching the far triangle
                        AutoState = AutonomousState.SHOOT_2;
                    }
                    else{
                        //update turret position
                        turret.turretSetIdealAngleUsingLLandPods();
                        //reset odometry position
                        pods.holdPosition(90,14,-90,regularPathSpeed);
                        break;
                    }
                case SHOOT_2:
                    //fire 3 balls code
                    if(motifNumber == Config.GPP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_0_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_1_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_MIDDLE_PICKUP;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PGP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_1_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_MIDDLE_PICKUP;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PPG) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.DRIVE_TO_MIDDLE_PICKUP;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    case DRIVE_TO_MIDDLE_PICKUP:
                    //prepare for the pickup by aligning robot with the spike mark
                    if(pods.holdPosition(103,60,-90,regularPathSpeed)){

                        AutoState = AutonomousState.PICKUP_BALL_MIDDLE_PICKUP;
                    }
                    else{
                        pods.holdPosition(102,60,-90,regularPathSpeed);
                        break;
                    }
                case PICKUP_BALL_MIDDLE_PICKUP:
                    //run through ball to pick it up
                    turret.setIntakeSpeed(1);
                    if(pods.holdPosition(128,60,-90,pickupBallSpeed)){
                        //stop intake when position is reached
                        turret.setIntakeSpeed(0);
                        AutoState = AutonomousState.DRIVE_BACK_FROM_MIDDLE_PICKUP;
                    }
                    else{
                        //if we haven't reached the end of the path yet, auto rotate spindexer and hold odometry
                        if (spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED && pickupTimer.seconds() > 0.3) {
                            //if the spindexer state is 0 and we see a ball move to 1
                            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP ) {
                                pickupTimer.reset();
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                                pods.update();
                                break;
                            }
                            //if the spindexer state is 1 and we see a ball move to 2
                            else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP&& pickupTimer.seconds() > 0.3) {
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
                                pickupTimer.reset();
                                pods.update();
                                break;
                            }
                            //if the spindexer state is 2 and we see a then end this pickup
                            else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP&& pickupTimer.seconds() > 0.4) {
                                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                                AutoState = AutonomousState.DRIVE_BACK_FROM_MIDDLE_PICKUP;
                                turret.setIntakeSpeed(0);
                                break;
                            }
                        }
                        //make sure odo is driving
                        break;
                    }
                case DRIVE_BACK_FROM_MIDDLE_PICKUP:
                    //drive back to fire ball
                    if(pods.holdPosition(90,14,-90,regularPathSpeed)){
                        AutoState = AutonomousState.SHOOT_3;
                    }
                    else{
                        pods.holdPosition(90,14,-90,regularPathSpeed);
                        break;
                    }
                case SHOOT_3:
                    //add shooting code here
                    if(motifNumber == Config.GPP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.PARK;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PGP) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_1_FIRE, Spindexer.SpindexerRotationalState.SLOT_0_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.PARK;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                    else if(motifNumber == Config.PPG) {
                        if (spindexer.fire3Balls(Spindexer.SpindexerRotationalState.SLOT_2_FIRE, Spindexer.SpindexerRotationalState.SLOT_1_FIRE,
                                Spindexer.SpindexerRotationalState.SLOT_0_FIRE, Spindexer.SpindexerRotationalState.SLOT_2_PICKUP)) {
                            AutoState = AutonomousState.PARK;
                            break;
                        }
                        else{
                            //if we haven't fired 3 balls yet, break; and come back in later
                            break;
                        }
                    }
                case PARK:
                    //park outside the far triangle to prepare to open the gate
                    pods.holdPosition(90,30,0,regularPathSpeed);
                    stopAiming = true;
                    turret.setTurretPositionDegrees(0,regularPathSpeed);
                        //shut off flywheel
                        turret.setFlywheelToRPM(0);


            }
        }

    }

}
