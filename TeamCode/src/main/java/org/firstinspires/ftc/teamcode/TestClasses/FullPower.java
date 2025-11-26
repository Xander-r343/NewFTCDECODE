package org.firstinspires.ftc.teamcode.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class FullPower extends OpMode {
    DcMotor shooter;

    @Override
    public void init() {
        shooter = hardwareMap.get( DcMotor.class,"shooter");
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        shooter.setPower(1);
    }
}
