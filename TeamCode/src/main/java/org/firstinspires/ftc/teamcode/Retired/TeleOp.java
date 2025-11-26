package org.firstinspires.ftc.teamcode.Retired;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrivetrain;

// This is our main Teleop Class. It uses x, y ,z classes and calls into a, b, c functions, etc.
// It inherits capabilites from dfg
// It depends on Pedropath, etc.
@Disabled
public class TeleOp extends OpMode {
    MecanumDrivetrain chassis;

    public int state;

    @Override
    public void init() {
        //TODO check motor direction
        state = 1;

        chassis = new MecanumDrivetrain(0.5, hardwareMap);
    }

    @Override
    public void loop() {
        chassis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        switch (state) {
            case 1:
                telemetry.addData("state 1", "");
                telemetry.update();
                if (gamepad1.a) {
                    state = 2;
                    break;
                }

            case 2:
                telemetry.addData("state 2", "");
                telemetry.update();
                if (gamepad1.a) {
                    state = 3;
                    break;
                } else if (gamepad1.b) {
                    state = 1;
                    break;
                }
            case 3:
                telemetry.addData("state 3", "");
                telemetry.update();
                if (gamepad1.b) {
                    state = 2;
                    break;

                }
        }

    }
}
