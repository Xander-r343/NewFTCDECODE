package org.firstinspires.ftc.teamcode.New.Subsystems;

import org.firstinspires.ftc.teamcode.Old.Configs.Config;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class TurretSubsystem implements Subsystem {
    public static final TurretSubsystem INSTANCE = new TurretSubsystem();
    private TurretSubsystem() {  }

    public final MotorEx turretMotor = new MotorEx(Config.turretRotationName);

    public static PIDCoefficients turretPIDCoeffs = new PIDCoefficients(0.0115, 0.0, 0.0);
    public static BasicFeedforwardParameters turretFFCoefs = new BasicFeedforwardParameters(0.0001851, 0.0, 0.006);
    public final ControlSystem turretControlSystem = ControlSystem.builder()
            .posPid(turretPIDCoeffs)
            .basicFF(turretFFCoefs)
            .build();

    @Override
    public void periodic() {
        turretMotor.setPower(turretControlSystem.calculate(turretMotor.getState()));
    }
}
