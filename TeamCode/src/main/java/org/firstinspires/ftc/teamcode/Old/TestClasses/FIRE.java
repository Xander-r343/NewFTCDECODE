package org.firstinspires.ftc.teamcode.Old.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Utilities.RpmLookupTable;

@TeleOp
public class FIRE extends OpMode {
    RpmLookupTable table;
    DcMotorEx shooter;
    public double ticksPerRev;
    public ElapsedTime timer;
    public double rpm;
    public double lastPosition;
    public double difference;
    public double time;
    public double targetRpm;
    public double tps;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    OdoPods pods;
    MecanumDrivetrain drivetrain;
    Aimbots aimBots;
    int flywheelVel;
    @Override
    public void init() {
        drivetrain = new MecanumDrivetrain(1,hardwareMap);
        pods = new OdoPods(hardwareMap,drivetrain);
        pods.setPosition(72, 15.5, 90);
        //aimBots = new Aimbots(true,pods);
        ticksPerRev = 103.8;
        timer = new ElapsedTime();
        timer.milliseconds();
        shooter = hardwareMap.get(DcMotorEx.class,"shooter");
        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheelVel = 0;
        table = new RpmLookupTable();

    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        shooter.getVelocity();
        if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            flywheelVel -= 50;
        }
        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            flywheelVel += 50;
        }
        telemetry.addData("target", flywheelVel*3);
        telemetry.addData("rpm", (180*shooter.getVelocity())/311.4);
        telemetry.addData("power",shooter.getPower());
        //telemetry.addData("distance", aimBots.getHypotnuseLength(pods.getX(), pods.getY()));
        //telemetry.addData("table says:", table.getRpm((int)aimBots.getHypotnuseLength(pods.getX(), pods.getY())));
        //telemetry.addData("ideal",180-aimBots.getIdealRobotAngle(pods.getX(),pods.getY()));
        telemetry.addData("real", pods.getHeading());
        pods.update();
        telemetry.update();
        if(gamepad1.b){
            flywheelVel = 0;
        }
        else if(currentGamepad1.a && !previousGamepad1.a){
            //flywheelVel = (int)(-311.4*(table.getRpm((int)aimBots.getHypotnuseLength(pods.getX(), pods.getY()))/180));
        }
        /*if(gamepad1.x){
            if(pods.holdPosition(pods.getX(),pods.getY(),180-aimBots.getIdealRobotAngle(pods.getX(),pods.getY()),1)){
                pods.update();
            }
        }*/
        shooter.setVelocity(flywheelVel);
    }
    public void stop(){

    }

}
