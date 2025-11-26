package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp
public class LimelighTester extends OpMode {
    private Limelight3A limelight;
    double xError;
    LLStatus status;
    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
        limelight.pipelineSwitch(3);
        telemetry.setMsTransmissionInterval(11);
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();
        status = limelight.getStatus();

            Pose3D botpose = result.getBotpose();
            double captureLatency = result.getCaptureLatency();
            double targetingLatency = result.getTargetingLatency();
            double parseLatency = result.getParseLatency();
            telemetry.addData("LL Latency", captureLatency + targetingLatency);
            telemetry.addData("Parse Latency", parseLatency);
            telemetry.addData("target area", result.getTa());
            telemetry.addData("tx", result.getTx());
            telemetry.addData("distance? inches",(10.75-2.5)/Math.tan(Math.toRadians(result.getTy())));
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(),(int)status.getFps());
            telemetry.addData("result", result.isValid());
            telemetry.update();
            xError = result.getTx();
        if(gamepad1.a){
            telemetry.speak("angle is" + result.getTx() + " degrees off");
        }
    }
}
