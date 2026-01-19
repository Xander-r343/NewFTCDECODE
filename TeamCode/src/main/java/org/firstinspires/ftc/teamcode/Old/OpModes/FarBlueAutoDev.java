package org.firstinspires.ftc.teamcode.Old.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Old.Configs.Config;
import org.firstinspires.ftc.teamcode.Old.Configs.RedAutoPaths;
import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Subsystems.retired.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Old.Subsystems.retired.Turret;
import org.firstinspires.ftc.teamcode.Old.Utilities.AimbotV2;

@Autonomous(name = "BLUE AUTO DEV ONLY")
public class FarBlueAutoDev extends LinearOpMode {

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
    int AutoState;
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
    ElapsedTime timer2 = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(56, 9, 90);
        timer = new ElapsedTime();
        AutoState = 0;
        aimbots = new Aimbots(config.BlueAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.BlueAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap, runtime);
        spindexer.reloadFlickerServo();
        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
        waitForStart();
        runtime.reset();
        timer.reset();
        timer2.reset();
        blackboard.put(config.AllianceKey,config.BlueAlliance);
        while (opModeIsActive()) {
            values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
            turret.setServoPoseManaul(1);
            turret.setFlywheelToRPM((int)((values[1])*0.96));
            turret.update();
            aimbots.update();
            if(!stopAiming) {
                turret.turretSetIdealAngleUsingLLandPods();
            }
            spindexer.updateState();
            pods.update();
            switch (AutoState) {
                case 0:
                    timer.startTime();
                    //aim and fire 3 balls here
                    fire3Balls();
                    while(timer.seconds() < 7 && timer.seconds() > 4.9 && opModeIsActive()){
                        pods.holdPosition(40, 37, 90, 1);
                        pods.update();
                        if(pods.holdPosition(40, 37, 90,1)){
                            AutoState = 1;
                        }else{
                            pods.update();
                            turret.update();
                            aimbots.update();
                            spindexer.updateState();
                            turret.setIntakeSpeed(1);
                        }
                    }

                    break;
                case 1:
                    timer.reset();
                    spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                    pods.update();
                    while (timer.seconds() < 5 && opModeIsActive() && AutoState ==1) {
                        pods.holdPosition(18, 37, 90, 0.25);
                        turret.update();
                        turret.setIntakeSpeed(1);
                        pods.update();
                        spindexer.updateState();
                        aimbots.update();
                        //detect balls
                        detectBalls();
                        if(timer.seconds() > 3.5){
                            AutoState = 2;
                            turret.setIntakeSpeed(0);
                        }
                    }



                    break;
                case 2:
                    timer.reset();
                    spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                    pods.holdPosition(56,9,90,1);
                    turret.update();
                    pods.update();
                    aimbots.update();
                    spindexer.updateState();
                    if(pods.holdPosition(56, 9, 90, 1)){
                            AutoState = 3;
                        }

                    break;
                case 3:
                    timer.reset();
                    fire3Balls();
                    turret.update();
                    spindexer.updateState();
                    aimbots.update();

                    break;
                case 4:
                    stopAiming = true;
                    turret.setTurretPositionDegrees(90, 1);
                    turret.setFlywheelToRPM(0);
                    spindexer.reloadFlickerServo();
                    spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                    break;

            }
            telemetry.addData("state", spindexer.getFlickerState());
            telemetry.update();
        }

    }



    public void fire3Balls(){
            //flick the ball
        if(turret.flywheelIsSpedUp((int)(values[1]*0.96), 50)) {
            spindexer.fireFlickerServo();
        }
            spindexer.updateState();
            //move to next slot
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
                spindexer.updateState();
            }
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE){
                //fire
                spindexer.fireFlickerServo();
                spindexer.updateState();
            }
            spindexer.updateState();
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
                spindexer.updateState();
            }
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE && timer.seconds() > 4){
                spindexer.fireFlickerServo();
               spindexer.updateState();
            }
    }
   public void detectBalls(){
       if (spindexer.getBallColorImmediately() != Spindexer.color.UNDECTED) {
           if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_PICKUP) {
               timer2.reset();
               spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_PICKUP);
               pods.update();
           } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_PICKUP && timer2.seconds() > 0.3) {
               spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_PICKUP);
               timer2.reset();
               pods.update();
           } else if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_PICKUP && timer2.seconds() > 0.3) {
               spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
               turret.setIntakeSpeed(0);
               AutoState = 2;
           }
       }
   }
}
