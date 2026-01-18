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
        /* action 1 */ TrajectoryActionBuilder aprilTagReadTrajectory = drive.actionBuilder(new Pose2d(-62, 37, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(180)), Math.PI/2);
                // read the april tag, align turret, shoot the preloaded balls

        /* action 2 */ TrajectoryActionBuilder firstThreeBallsTrajectory = aprilTagReadTrajectory.endTrajectory().fresh()
                .splineTo(new Vector2d(-11.5, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(-11.5, 56))
                // stop running intake
                .strafeTo(new Vector2d(-11.5, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2);
                // shoot the picked up balls

        /* action 3 */ TrajectoryActionBuilder secondThreeBallsTrajectory = firstThreeBallsTrajectory.endTrajectory().fresh()
                .splineTo(new Vector2d(12, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(12, 56))
                // stop running intake
                .strafeTo(new Vector2d(12, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2);
                // shoot 3 picked up balls

        /* action 3 */ TrajectoryActionBuilder lastThreeBallsTrajectory = firstThreeBallsTrajectory.endTrajectory().fresh()
                .splineTo(new Vector2d(35, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(35, 56))
                // stop running intake
                .strafeTo(new Vector2d(35, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2);
                // shoot 3 picked up balls


        //webcam = new AprilTagWebcam();
<<<<<<< Updated upstream
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
=======
        Actions.runBlocking(
                new SequentialAction(
                        aprilTagReadTrajectory.build(),
                        //webcam.waitForAprilTag(),

                        new SleepAction(1.0),
                        firstThreeBallsTrajectory.build(),
                        new SleepAction(1.0),
                        secondThreeBallsTrajectory.build(),
                        new SleepAction(1.0),
                        lastThreeBallsTrajectory.build()

                )
        );
>>>>>>> Stashed changes

    }
}
