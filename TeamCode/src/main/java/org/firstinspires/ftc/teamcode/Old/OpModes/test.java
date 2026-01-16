package org.firstinspires.ftc.teamcode.Old.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Old.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Old.Subsystems.ShooterSubsystem;

@Disabled
public class test extends OpMode {
    Aimbots aimbots;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    int targetRPM;
    ShooterSubsystem robot;
    @Override
    public void init() {
        robot = new ShooterSubsystem(hardwareMap);
        targetRPM = 0;

    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        if(gamepad1.right_trigger > 0){
            robot.spinBelt(gamepad1.right_trigger);
        }
        else if(gamepad1.left_trigger > 0){
            robot.spinBelt(-gamepad1.left_trigger);
        }
        else{
            robot.spinBelt(0);
        }
        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            targetRPM+=150;
        }
        else if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            targetRPM-=150;
        }
        if(gamepad1.right_stick_y > 0 || gamepad1.right_stick_y < 0){
            robot.spinIntake(gamepad1.right_stick_y);
        }
        robot.setFlywheelVelocity(targetRPM);
        telemetry.addData("target", targetRPM);
        telemetry.addData("rpm", (robot.flywheel.getVelocity()/28)*60);
    }
}
