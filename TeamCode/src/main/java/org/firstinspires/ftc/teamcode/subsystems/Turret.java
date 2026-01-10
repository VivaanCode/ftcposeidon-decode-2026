package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TwoDeadWheelLocalizer;

public class Turret{
    private DcMotor rotator;
    private DcMotor shooter1;
    private DcMotor shooter2;
    private double distanceFactor = 0.1;
    private double initialPower = 0.2;
    private TwoDeadWheelLocalizer localizer;
    public Turret(HardwareMap hardwareMap, TwoDeadWheelLocalizer localizer){
        rotator = hardwareMap.get(DcMotorEx.class, "turret-rotation");
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter-motor-1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter-motor-2");
        this.localizer = localizer;
    }
    public class WarmUpShooter implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            Pose2d pose = localizer.getPose();
            double distance = Math.sqrt(Math.pow(pose.position.x,2) + Math.pow(pose.position.y,2));
            shooter1.setPower(distanceFactor * distance + initialPower);
            shooter2.setPower(1);
            return true;
        }
    }
    public class AlignShooter implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            Pose2d pose = localizer.getPose();
            return true;
        }
    }
}
