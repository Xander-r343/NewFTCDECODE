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
import org.firstinspires.ftc.teamcode.Old.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Old.Utilities.AimbotV2;

@Autonomous(name = "far zone red V1")
public class FarRedAuto extends LinearOpMode {

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


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pathDatabase = new RedAutoPaths();
        config = new Config();//intialize blackboard objects
        AutoState = 0;
        //initialize odopods by using the config class
        pods.setPosition(88, 9, -90);
        timer = new ElapsedTime();
        AutoState = 0;
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        turret = new Turret(hardwareMap, config.RedAlliance, aimbots,telemetry);
        spindexer = new Spindexer(hardwareMap, runtime);
        spindexer.reloadFlickerServo();
        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
        waitForStart();
        runtime.reset();
        timer.reset();
        blackboard.put(config.AllianceKey,config.RedAlliance);
        while (opModeIsActive()) {
            values = AimbotV2.getValues(aimbots.calculateSideLengthUsingPods());
            turret.setServoPoseManaul(0.95);
            turret.setFlywheelToRPM((int)((values[1])*0.94));
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
                        pods.holdPosition(102, 37, -90, 1);
                        pods.update();
                        if(pods.holdPosition(102, 37, -90,1)){
                            AutoState = 1;
                        }else{
                            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                            pods.update();
                            turret.update();
                            aimbots.update();
                            spindexer.updateState();
                            turret.setIntakeSpeed(1);
                        }
                    }

                    break;
                case 1:
                    ElapsedTime timer2 = new ElapsedTime();
                    timer.reset();
                    pods.update();
                    while (timer.seconds() < 5 && opModeIsActive()) {
                        pods.holdPosition(139, 37, -90, 0.45);
                        turret.update();
                        turret.setIntakeSpeed(1);
                        pods.update();
                        spindexer.updateState();
                        aimbots.update();
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
                        if(timer.seconds() > 3.5){
                            AutoState = 2;
                            turret.setIntakeSpeed(0);
                        }
                    }



                    break;
                case 2:
                    timer.reset();
                    while (timer.seconds() < 2.6 && timer.seconds() > 0 && opModeIsActive())
                    {
                        pods.holdPosition(88, 9, -90, 1);
                        turret.update();
                        pods.update();
                        aimbots.update();
                        spindexer.updateState();
                        spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_FIRE);
                    }
                    if(timer.seconds() > 2.5){
                        AutoState = 3;
                    }
                    break;
                case 3:
                    timer.reset();
                    spindexer.fireFlickerServo();
                    while(timer.seconds() < 5 & opModeIsActive()) {
                        pods.update();
                        //flick the ball
                        spindexer.updateState();
                        //move to next slot
                        if (spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && timer.seconds() > 0.7) {
                            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
                            spindexer.updateState();
                        }
                        if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE && timer.seconds() > 3.05) {
                            //fire
                            spindexer.fireFlickerServo();
                            spindexer.updateState();
                        }
                        if (timer.seconds() > 3.6) {
                            spindexer.reloadFlickerServo();
                        }
                        spindexer.updateState();
                        if (spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE
                                || timer.seconds() > 4) {
                            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
                            spindexer.updateState();
                        }
                        if (spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE && timer.seconds() > 4.3) {
                            spindexer.fireFlickerServo();
                            spindexer.updateState();
                            if (timer.seconds() > 4.7) {
                                spindexer.reloadFlickerServo();
                                spindexer.updateState();
                                AutoState = 4;

                            }
                        }
                        turret.update();
                        spindexer.updateState();
                        aimbots.update();
                    }
                    break;
                case 4:
                    pods.holdPosition(88, 35, -90,1);
                    stopAiming = true;
                    turret.setFlywheelToRPM(0);
                    spindexer.reloadFlickerServo();
                    spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_0_PICKUP);
                    pods.update();
                    if(timer.seconds() < 6.5){
                        turret.setTurretPositionDegrees(-90, 1);
                    }
                    break;


            }
            telemetry.addData("state", spindexer.getFlickerState());
            telemetry.update();
        }

    }



    public void fire3Balls(){
        if(timer.seconds() > 2) {
            //flick the ball
            if(timer.seconds() > 2 && timer.seconds() < 2.3){
                spindexer.fireFlickerServo();
            }
            spindexer.updateState();
            //move to next slot
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && timer.seconds() > 3){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
                spindexer.updateState();
            }
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE && timer.seconds() > 3.35){
                //fire
                spindexer.fireFlickerServo();
                spindexer.updateState();
                if(timer.seconds() > 4){
                    spindexer.reloadFlickerServo();
                }
            }
            spindexer.updateState();
            if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE
                    || timer.seconds() > 4.5){
                spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
                spindexer.updateState();
            }
            if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE && timer.seconds() > 4.75){
                spindexer.fireFlickerServo();
                if(timer.seconds()> 5.3){
                    spindexer.updateState();
                    spindexer.reloadFlickerServo();
                    spindexer.updateState();
                }
            }

        }
    }
    public void launch3second(){
        timer.reset();
        spindexer.fireFlickerServo();
        spindexer.updateState();
        if(timer.seconds() > 0.3 && timer.seconds() < 0.5){
            spindexer.reloadFlickerServo();
            spindexer.updateState();
        }
        //move to next slot
        if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED || timer.seconds() > 0.5){
            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_2_FIRE);
            spindexer.updateState();
        }
        if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_2_FIRE || timer.seconds() > 0.7){
            //fire
            spindexer.fireFlickerServo();
            spindexer.updateState();
            if(timer.seconds() > 0.9){
                spindexer.reloadFlickerServo();
            }
        }
        spindexer.updateState();
        if(spindexer.getFlickerState() == Spindexer.FlickerServoState.RELOADED && spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_0_FIRE
                || timer.seconds() > 1.15){
            spindexer.moveSpindexerToPos(Spindexer.SpindexerRotationalState.SLOT_1_FIRE);
            spindexer.updateState();
        }
        if(spindexer.getState() == Spindexer.SpindexerRotationalState.SLOT_1_FIRE || timer.seconds() > 1.4){
            spindexer.fireFlickerServo();
            spindexer.updateState();
            if(timer.seconds()> 1.65){
                spindexer.reloadFlickerServo();
                spindexer.updateState();
            }
        }



    }
}
