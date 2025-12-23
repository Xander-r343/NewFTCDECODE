package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class SpindexertTest extends OpMode {
    CRServo right;
    CRServo left;
    double pose;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    double maxVoltage = 4.8;

    @Override
    public void init() {
        right = hardwareMap.get(CRServo.class,"starboardServo");
        left = hardwareMap.get(CRServo.class,"portServo");
    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        right.setPower(gamepad1.left_stick_y);
        double voltage = hardwareMap.get(AnalogInput.class, "e").getVoltage();
        telemetry.addData("voltage", (voltage));
        telemetry.update();
    }
}
