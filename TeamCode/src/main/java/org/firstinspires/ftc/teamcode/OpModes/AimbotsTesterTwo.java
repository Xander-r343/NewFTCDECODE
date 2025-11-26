package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;
@TeleOp
public class AimbotsTesterTwo extends OpMode {
    MecanumDrivetrain drivetrain;
    OdoPods pods;
    Aimbots aimbots;
    Config config;
    double targetH;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();

    @Override
    public void init() {
        config = new Config();
        drivetrain = new MecanumDrivetrain(1,hardwareMap);
        pods = new OdoPods(hardwareMap,drivetrain);
        aimbots = new Aimbots(config.RedAlliance, pods, hardwareMap);
        pods.setPosition(72,9,0);
        aimbots.startLL();
        //red is 0, blue is 1, obelisk is 2
        aimbots.switchPipeline(0);
        targetH = 0;


    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        aimbots.update();
        telemetry.addData("x", pods.getX());
        telemetry.addData("y", pods.getY());
        telemetry.addData("h", pods.getHeading());
        telemetry.addData("distance", aimbots.calculateSideLengthUsingPods());
        telemetry.addData("angle", pods.getHeading() - aimbots.getHeadingErrorLL());
        telemetry.addData("LL", aimbots.LLstatusIsValid());
        telemetry.addData("angle", aimbots.getIdealAngle());
        telemetry.addData("targetH", targetH);
        telemetry.update();
        drivetrain.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if(currentGamepad1.a && !previousGamepad1.a) {
            if(aimbots.LLstatusIsValid()){
                targetH = pods.getHeading()-aimbots.getHeadingErrorLL();
                //pods.holdHeading(targetH,1);
            }
            else {
                targetH = aimbots.getIdealAngle();
                //pods.holdHeading(targetH, 1);
            }
        }
        if(gamepad1.a){
            pods.holdHeading(targetH,1);
        }


    }
}
