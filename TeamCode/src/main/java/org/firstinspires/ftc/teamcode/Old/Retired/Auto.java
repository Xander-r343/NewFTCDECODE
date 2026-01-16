package org.firstinspires.ftc.teamcode.Old.Retired;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Old.Sensors.OdoPods;
import org.firstinspires.ftc.teamcode.Old.Subsystems.MecanumDrivetrain;

@Configurable
@Disabled
public class Auto extends OpMode {
    private MecanumDrivetrain drive;
    private OdoPods pods;
    public static double nextx = 0;
    public static double nexty = 0;
    public static double nextheading = 0;
    public static double nextspeed = 1;
    @Override
    public void init() {
        drive = new MecanumDrivetrain(1, hardwareMap);
        pods = new OdoPods(hardwareMap, drive);
        pods.setPosition(0,0,0);
    }


    @Override
    public void loop() {
        if(pods.holdPosition(nextx,nexty,nextheading,nextspeed)){
            pods.update();
        }
        telemetry.addData("x:", pods.getX());
        telemetry.addData("y:", pods.getY());
        telemetry.addData("heading:", pods.getHeading());
        telemetry.update();
        pods.update();
        }
            }


