package org.firstinspires.ftc.teamcode.OpModes;

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
@Autonomous()
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
    AutonomousState AutoState;
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
    enum  AutonomousState{
        INIT, SHOOT_1, DRIVE_TO_PICKUP_FAR, PICKUP_BALL_FAR_PICKUP, DRIVE_BACK_FROM_FAR_PICKUP,  SHOOT_2, PICKUP_CORNER, SHOOT_3, PARK
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
            //should this be moved out of the while loop? They are init values, right?
            turret.setServoPoseManaul(0.95);
            switch (AutoState){
                case INIT:
                    turret.setFlywheelToRPM((int)values[1]);
                    if(turret.flywheelIsUpToSpeed((int)values[1],100)){
                         AutoState = AutonomousState.SHOOT_1;
                    }
                case SHOOT_1:
                    if(spindexer.fire3Balls()){
                        AutoState = AutonomousState.DRIVE_TO_PICKUP_FAR;
                    }
                case DRIVE_TO_PICKUP_FAR:
                    //strafe to far pickup and turn to orient spike mark before pickuup
                    if(pods.holdPosition(101, 35.5,-90,1)){
                        //set intake speed to prepare for ball
                        turret.setIntakeSpeed(1.0);
                        //go to next state to actually pickup
                        AutoState = AutonomousState.PICKUP_BALL_FAR_PICKUP;
                    }else{
                        pods.holdPosition(101, 35.5,-90,1);
                        break;
                    }
                case PICKUP_BALL_FAR_PICKUP:
                    if(pods.holdPosition(135, 35.5, -90, 0.4)){
                        AutoState = AutonomousState.DRIVE_BACK_FROM_FAR_PICKUP;
                    }else{
                        //auto spin spindexer to next state after detecting a ball
                        AutodetectBall();
                        //make sure odo is driving
                        pods.holdPosition(135, 35.5, -90, 0.4);
                        break;
                    }
                case DRIVE_BACK_FROM_FAR_PICKUP:
                    if(pods.holdPosition(88,9,-90,1)){
                        //go next state
                        AutoState = AutonomousState.SHOOT_2;
                    }
                    else{
                        //update turret position
                        turret.setTurretUsingVelAim();
                        //reset odometry position
                        pods.holdPosition(88,9,-90,1);
                        break;
                    }

            }




        }

    }
    public void AutodetectBall(){
        pickupTimer.reset();
        if (spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED) {
            if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP) {
                pickupTimer.reset();
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
                pods.update();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP && pickupTimer.seconds() > 0.3) {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
                pickupTimer.reset();
                pods.update();
            } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP && pickupTimer.seconds() > 0.3) {
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                turret.setIntakeSpeed(0);
            }
        }
    }
}
