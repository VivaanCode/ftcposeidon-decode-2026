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

@Autonomous(name = "Far side test")
public class FarSideTest extends LinearOpMode {
    MecanumDrive drive;
    AprilTagWebcam webcam;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
        /* action 1 */ TrajectoryActionBuilder aprilTagReadTrajectory = drive.actionBuilder(new Pose2d(64, 22, Math.toRadians(180)))
                .splineTo(new Vector2d(56, 14), Math.toRadians(180))
                .turnTo(Math.toRadians(162));

        /* action 2 */ TrajectoryActionBuilder firstThreeBallsTrajectory = aprilTagReadTrajectory.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(36, 30, Math.toRadians(90)), Math.toRadians(90))
                .lineToY(54)

                // stop running the intake
                // start running the flywheel/shooter

                .strafeTo(new Vector2d(37, 54))

                // optionally run the flywheel/shooter here to save 0.01% battery


                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0);
                // shoot 3 more balls (est. 5-6s)

        /* action 3 */ TrajectoryActionBuilder secondThreeBallsTrajectory = firstThreeBallsTrajectory.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(12, 30, Math.toRadians(90)), Math.toRadians(90))
                // start running the intake
                .lineToY(54)

                // stop running intake
                // start running shooter either here or after the next line

                .strafeTo(new Vector2d(13, 54))

                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0);
                // shoot 3 intake balls in motif


        /* action 4 */ TrajectoryActionBuilder lastThreeBallsTrajectory = secondThreeBallsTrajectory.endTrajectory().fresh()

                .splineToLinearHeading(new Pose2d(40, 58, Math.toRadians(0)), Math.toRadians(90))
                // start running the intake

                .strafeTo(new Vector2d(60, 58))
                //.splineToLinearHeading(new Pose2d(64, 58, Math.toRadians(0)), Math.toRadians(90))
                .strafeTo(new Vector2d(50, 58))
                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0);




        //webcam = new AprilTagWebcam();
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

    }
}
