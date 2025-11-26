package org.firstinspires.ftc.teamcode.TestClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
public class useRPM extends OpMode {
    public double x;
    DcMotorEx shooter;
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    @Override
    public void init() {
        x = 0;
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");

    }

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        telemetry.addData("x",x);
        telemetry.addData("rpm",((shooter.getVelocity()*60)/28));
        telemetry.addData("pow", shooter.getPower());
        telemetry.update();
        shooter.setVelocity((x/60)*28);
        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up){
            x+=300;
        }
        else if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down){
            x-= 300;
        }


    }
}
