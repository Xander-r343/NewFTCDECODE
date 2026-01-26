package org.firstinspires.ftc.teamcode.Retired;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

@TeleOp
@Configurable
public class Test extends OpMode {
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    DcMotorEx f1M, f2M;

    @Override
    public void init() {
        f1M = (DcMotorEx) hardwareMap.dcMotor.get("leftFlywheel");
        f2M = (DcMotorEx) hardwareMap.dcMotor.get("rightFlywheel");
        f1M.setDirection(DcMotorSimple.Direction.REVERSE);
        f2M.setDirection(DcMotorSimple.Direction.FORWARD);
        f1M.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f2M.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    /*
    public static PIDCoefficients pidC = new PIDCoefficients(0.0145, 0.0, 0.0);
    public static BasicFeedforwardParameters ffCoefs = new BasicFeedforwardParameters(0.00000637755, 0.0, 0.0075);
*/
    public static PIDCoefficients pidCoefficients = new PIDCoefficients(0.0, 0.0, 0.0);
    public static BasicFeedforwardParameters ff = new BasicFeedforwardParameters(0.0,0.0,0.0);
    public static double target = 0.0;

    private ControlSystem controlSystem = ControlSystem.builder()
            .velPid(pidCoefficients)
            .basicFF(ff)
            .build();

    private boolean pid = false;

    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);
        controlSystem.setGoal(new KineticState(0.0, target));

        if(pid) {
            f1M.setPower(controlSystem.calculate(new KineticState(0.0, f1M.getVelocity())));
            f2M.setPower(f1M.getPower());
        }

        if(currentGamepad1.b && !previousGamepad1.b) {
            pid = !pid;
        }
        if(currentGamepad1.a && !previousGamepad1.a){
            controlSystem.setGoal(new KineticState(0.0, 3200));
        }
        telemetry.addData("power", f1M.getPower());
        telemetry.addData("velocity", f1M.getVelocity());
        telemetry.addData("pid", pid);
        telemetry.addData("target", target);
        telemetry.update();
    }
}