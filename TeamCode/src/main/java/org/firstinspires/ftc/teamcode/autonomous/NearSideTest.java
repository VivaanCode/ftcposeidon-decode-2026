package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;

/*
servo locations
0 - very bottom
0.6 - ready time
1 -
 */
@Autonomous(name = "near side test")
public class NearSideTest extends LinearOpMode {
    MecanumDrive drive;
    AprilTagWebcam webcam;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
        /* action 1 */ TrajectoryActionBuilder aprilTagReadTrajectory = drive.actionBuilder(new Pose2d(64, 22, Math.toRadians(180)))
                .splineTo(new Vector2d(56, 14), Math.toRadians(180))
                .turnTo(Math.toRadians(162));

        //webcam = new AprilTagWebcam();
//        Actions.runBlocking(
//                new SequentialAction(
//                        aprilTagReadTrajectory.build(),
//                        //webcam.waitForAprilTag(),
//                        new SleepAction(1.0)
//                        //firstThreeBallsTrajectory
//
//                        .build()
//
//                )
//        );

    }
}
