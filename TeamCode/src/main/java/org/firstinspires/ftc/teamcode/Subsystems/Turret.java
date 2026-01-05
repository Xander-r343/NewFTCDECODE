package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs.Config;
import org.firstinspires.ftc.teamcode.Sensors.OdoPods;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
@Configurable
public class    Turret {
    public static PIDCoefficients pidC = new PIDCoefficients(0.0115, 0.0, 0.0);
    public static BasicFeedforwardParameters ffCoefs = new BasicFeedforwardParameters(0.0001851, 0.0, 0.006);


    private Config config;
    private DcMotorEx turretRotater;
    private Servo leftHoodServo;
    private Servo rightHoodServo;
    private DcMotorEx leftFlywheelMotor;
    private DcMotorEx rightFlywheelMotor;
    private DcMotor intake;
    private Limelight3A limelight;
    private LLResult result;
    Aimbots aimbots;
    boolean aimContinuously;
    Telemetry telemetry;
    ControlSystem controlSystem;
    public Turret(@NonNull HardwareMap hardwareMap, int alliance, Aimbots givenAimbots, Telemetry tel){
        //initalize turret servos and motor
        config = new Config();
        leftHoodServo = hardwareMap.get(Servo.class, config.leftHoodServo);
        rightHoodServo = hardwareMap.get(Servo.class, config.rightHoodServo);
        leftHoodServo.resetDeviceConfigurationForOpMode();
        leftHoodServo.resetDeviceConfigurationForOpMode();
        //set motor runMode
        turretRotater = hardwareMap.get(DcMotorEx.class, config.turretRotationName);
        turretRotater.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRotater.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretRotater.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //initalize left motor
        leftFlywheelMotor = hardwareMap.get(DcMotorEx.class, config.newRobotLeftFlywheel);
        leftFlywheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFlywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFlywheelMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //initalize right motor
        rightFlywheelMotor = hardwareMap.get(DcMotorEx.class, config.newRobotRightFlywheel);
        rightFlywheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFlywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFlywheelMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //intake
        intake = hardwareMap.get(DcMotor.class, config.IntakeMotorName);
        //initalize limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
        if(alliance == config.RedAlliance){
            limelight.pipelineSwitch(0);
        }
        else if(alliance == config.BlueAlliance){
            limelight.pipelineSwitch(1);
        }
        //aiming:
        aimbots = givenAimbots;
        aimContinuously = false;
        telemetry = tel;
        controlSystem = ControlSystem.builder()
                .velPid(pidC)
                .basicFF(ffCoefs)
        .build();
    }

    /**
     * sets the turret to be a specific angle
     * @param degrees the wanted angle from the perspective of the field
     * @param power the power to move turret at
     */

    public void setTurretPositionDegrees(double degrees, double power){
        int targetPose = 0;
        if(degrees <=180 && degrees >= -180) {
            targetPose = (int)degrees;
        }
        else if(degrees < -135){
            targetPose = -135;
        }
        else if(degrees > 135){
            targetPose = 135;
        }
        //this line sets the turret to aim based on field position rather than aiming off of the robot
        turretRotater.setTargetPosition(((int)Math.round((targetPose - aimbots.pods.getHeading()) *config.ticksPerDegree)));
        //sets the motor to runnnn
        turretRotater.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //sets power to given power
        turretRotater.setPower(power);
    }
    public double getTurretPositionDegrees(){
        return aimbots.pods.getHeading() + ((turretRotater.getCurrentPosition()/config.ticksPerDegree));
    }
    public void turretSetIdealAngleUsingLLandPods(){
        setTurretPositionDegrees(aimbots.getIdealAngle(),1);
    }
    public void continuouslyAim(boolean trueOrFalse){
        aimContinuously = trueOrFalse;
    }

    /**
     * sets the hood to a specific launch angle
     * @param givenLaunchAngle is the desired launch angle
     */
    public void setHoodLaunchAngle(double givenLaunchAngle){
        double servoRange = config.hoodMaximumLaunchAngle - config.hoodMinimumLaunchAngle;
        double givenPosition = givenLaunchAngle/config.hoodMaximumLaunchAngle;
        rightHoodServo.setPosition(givenPosition);
        leftHoodServo.setPosition(1-givenPosition);
    }
    public void setServoPoseManaul(double pose){
        rightHoodServo.setPosition(pose);
        leftHoodServo.setPosition(pose);
    }
    /**
     *
     * @param rpm is the desiredRpm
     */
    public void setFlywheelToRPM(int rpm){
        controlSystem.setGoal(new KineticState(0.0, ((rpm*28)/60)));
    }
    public void setFlywheelToTPS(int tps){
        controlSystem.setGoal(new KineticState(0.0, tps));
    }
    public void update(){
        result = limelight.getLatestResult();
        aimbots.update();
        if(aimContinuously){
            turretSetIdealAngleUsingLLandPods();
        }
        double power;
               power =  controlSystem.calculate(
                        new KineticState(0, rightFlywheelMotor.getVelocity())
                );

        leftFlywheelMotor.setPower(power);
        rightFlywheelMotor.setPower(power);
    }
    /**
     * returns a boolean telling whether the flywheel is at speed or not
     * @param speed the speed to check against the flywheel in rpm
     * @param error the amount of error of margin of rpm to still return true
     */
    public boolean flywheelIsSpedUp(int speed, double error){
        if(rightFlywheelMotor.getVelocity() < speed + error && rightFlywheelMotor.getVelocity() > speed){
            return true;
        }
        else{
            return false;
        }
    }
    public double getRpm (){
        return rightFlywheelMotor.getVelocity()/config.ticksPerRevFlywheel*60;
    }
    public double getTx(){
        if(result != null) {
            return result.getTx();
        }
        else{
            return 0;
        }
    }
    public void setIntakeSpeed(double speed){
        intake.setPower(speed);
    }
    public double getRightCurrent(){
        return rightFlywheelMotor.getCurrent(CurrentUnit.MILLIAMPS);
    }
    public double getLeftCurrent(){
        return leftFlywheelMotor.getCurrent(CurrentUnit.MILLIAMPS);
    }
    public int getMotif(){
        return 0;
    }
    public double getHoodDouble(){
        return rightHoodServo.getPosition();
    }




}
