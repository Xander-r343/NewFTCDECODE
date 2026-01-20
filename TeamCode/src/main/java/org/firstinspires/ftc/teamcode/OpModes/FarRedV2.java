package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
    int tagID;
    Turret turret;
    Spindexer spindexer;
    double [] values;
    boolean stopAiming = false;
    enum  AutonomousState{
        INIT, SHOOT_1, SHOOT_1_CHECK_IF_DONE, PICKUP_FAR, SHOOT_2, PICKUP_CORNER, SHOOT_3, PARK
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        //initialize odopods by using the config class
        pods.setPosition(88, 9, -90);
        timer = new ElapsedTime();
        AutoState = AutonomousState.INIT;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap, runtime, telemetry);

        spindexer.reloadFlickerServo();
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

            //should this be moved out of the while loop? They are init values, right?
            turret.setServoPoseManaul(0.95);

            turret.setFlywheelToRPM((int)((values[1])*0.93));

            turret.update();
            aimbots.update();
            if(!stopAiming) {
                turret.turretSetIdealAngleUsingLLandPods();
            }
            spindexer.updateState();
            pods.update();
            telemetry.addData("AutoState Case:",AutoState);
            telemetry.addData("flick", spindexer.getFlickerState());
            telemetry.addData("rpm", turret.getRpm());
            telemetry.update();
            switch (AutoState){
                case INIT:
                    if(turret.flywheelIsUpToSpeed((int)(values[1]*0.93), 100))
                        {
                        timer.reset();
                        AutoState = AutonomousState.SHOOT_1;
                    }
                    else {
                        break;
                    }
                case SHOOT_1:
                    spindexer.fireFlickerServo();
                    AutoState = AutonomousState.SHOOT_1_CHECK_IF_DONE;
                case SHOOT_1_CHECK_IF_DONE:
                    // check here if firing is complete
                    if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED){
                        //0,2,1
                        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
                        AutoState = AutonomousState.SHOOT_2;
                    } else {
                        break;
                    }

            }
        }

    }
    public void shoot_1(){
        spindexer.fireFlickerServo();



    }
}
