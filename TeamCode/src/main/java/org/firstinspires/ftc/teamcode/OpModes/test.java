package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Aimbots;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Spindexer;

@TeleOp(name = "spindexerTest")
public class test extends OpMode {
    Aimbots aimbots;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    double targetPosition;
    Spindexer spindexer;
    ElapsedTime timer;
    @Override

    public void init() {
        timer = new ElapsedTime();
        timer.startTime();
        targetPosition = 0;
        spindexer = new Spindexer(hardwareMap, timer, telemetry);
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            targetPosition += 0.05;
        }
        else if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            targetPosition -= 0.05;

        }
        spindexer.setPositionManualOverride(targetPosition);
        telemetry.addData("target", targetPosition);
    }
}
