package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TwoDeadWheelLocalizer;

public class Turret{
    public DcMotor rotator;
    private DcMotor shooter1;
    private DcMotor shooter2;
    private double distanceFactor = 0.1;
    private double initialPower = 0.2;
    private Localizer localizer;
    private final double encoderTicksToDegrees = 672/90.0;
    private final double encoderInitialPosition;
    private final Alliance alliance;
    public Turret(HardwareMap hardwareMap, Localizer localizer, Alliance color){
        rotator = hardwareMap.get(DcMotorEx.class, "turret-rotation");
        encoderInitialPosition = rotator.getCurrentPosition();
        alliance = color;
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter-motor-1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter-motor-2");
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        this.localizer = localizer;
    }
    public Action warmUpShooter(){
        return new WarmUpShooter();
    }
    public Action alignShooter(){
        return new AlignShooter();
    }
    public class WarmUpShooter implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            Pose2d pose = localizer.getPose();
            double distance = Math.sqrt(Math.pow(pose.position.x,2) + Math.pow(pose.position.y,2));
            shooter1.setPower(distanceFactor * distance + initialPower);
            shooter2.setPower(1);
            Actions.runBlocking(
                    new SleepAction(0.2)
            );
            return true;
        }
    }
    public class AlignShooter implements Action{
        boolean initialized = false;
        Pose2d pose;
        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            if (!initialized){
                pose = localizer.getPose();
                Vector2d goalPose;
                if (alliance == Alliance.RED_ALLIANCE){
                    goalPose = new Vector2d(-63, 59);
                }
                else{
                    goalPose = new Vector2d(-63, -59);
                }
                double degrees = Math.toDegrees(Math.atan2(goalPose.x - pose.position.x,goalPose.y -  pose.position.y) - pose.heading.toDouble());
                if (degrees > 180){
                    degrees -= 360;
                }
                if (Math.abs(degrees) < 10){
                    return false;
                }
                double targetPosition = degrees*encoderTicksToDegrees - encoderInitialPosition;
                rotator.setTargetPosition((int)Math.round(targetPosition));
                rotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                initialized = true;
            }
            if (rotator.isBusy()){
                rotator.setPower(1);
                return true;
            }
            rotator.setPower(0);
            return false;
        }
    }
}
