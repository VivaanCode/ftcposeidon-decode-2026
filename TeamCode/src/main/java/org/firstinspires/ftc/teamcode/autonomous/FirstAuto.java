package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;

public class FirstAuto extends LinearOpMode {
    MecanumDrive drive;
    AprilTagWebcam webcam;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        drive = new MecanumDrive(hardwareMap, new Pose2d(60, 14, Math.toRadians(180)));
        TrajectoryActionBuilder aprilTagReadTrajectory = drive.actionBuilder(new Pose2d(64, 22, Math.toRadians(180)))
                .lineToX(30);
        webcam = new AprilTagWebcam();
        Actions.runBlocking(
                new SequentialAction(
                    aprilTagReadTrajectory.build(),
                    webcam.waitForAprilTag()
                )
        );

    }
}
