package org.firstinspires.ftc.teamcode.Old.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Old.Utilities.RpmLookupTable;

@TeleOp
public class AimbotsTester extends OpMode {
    RpmLookupTable table;
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    public Aimbots aimBots;
    double nextX;
    double nextY;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    int i = 0;
    @Override
    public void init() {
        drivetrain = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drivetrain);
        pods.setPosition(106,15,-90);
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        telemetry.addData("x", pods.getX());
        telemetry.addData("y", pods.getY());
        telemetry.addData("h", pods.getHeading());
        //telemetry.addData("ideal h", -aimBots.getIdealRobotAngle(pods.getX(), pods.getY()));
        telemetry.update();
        pods.update();

        if(gamepad1.a){
            lockOn();
        }


        drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
    void lockOn(){
        nextY = pods.getY();
        nextX = pods.getX();
        nextX += gamepad1.left_stick_x;
        nextY += gamepad1.left_stick_y;
       /* if (pods.holdPosition(nextX, nextY, -aimBots.getIdealRobotAngle(nextX, nextY), 1)) {
            pods.update();
        }*/
    }
    void setCalculatedRpm(){
        //table.getRpm((int)aimBots.getHypotnuseLength(pods.getX(), pods.getY()));
    }
}
