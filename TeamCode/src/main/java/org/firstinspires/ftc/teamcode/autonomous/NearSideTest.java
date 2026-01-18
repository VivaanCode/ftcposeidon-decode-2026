package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.subsystems.Artifact;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import java.util.ArrayList;
import java.util.Arrays;

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
    Intake intake = new Intake(hardwareMap);
    Turret turret = new Turret(hardwareMap, drive.localizer, Alliance.BLUE_ALLIANCE);
    int encoderInitialPosition;

    private ArrayList<Artifact> artifacts;


    int motifIndex = 0;

    double KICKER_MAX_POSITION = 0.65;
    double KICKER_MIN_POSITION = 0.1;


    DcMotor frontLeft, frontRight, rearLeft, rearRight, shooter1, shooter2;

    DcMotorEx shooterMotor, shooterMotor2, intakeMotor, rotator;

    Servo kicker1, kicker2, spindexer;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();

        waitForStart();
        drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
        /* action 1 */ TrajectoryActionBuilder aprilTagReadTrajectory = drive.actionBuilder(new Pose2d(-62, 37, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(180)), Math.PI/2);
                // read the april tag, align turret, shoot the preloaded balls

        /* action 2 */ TrajectoryActionBuilder firstThreeBallsTrajectory = aprilTagReadTrajectory.endTrajectory().fresh()
                .splineTo(new Vector2d(-11.5, 36), Math.toRadians(90));
                Actions.runBlocking(
                        intake.toggleIntakeOn() // start running intake
                );
                firstThreeBallsTrajectory.strafeTo(new Vector2d(-11.5, 56));
                Actions.runBlocking(
                        new ParallelAction(
                                intake.toggleIntakeOff(), // stop running intake
                                turret.warmUpShooter() // warm up the flywheel
                        )
                );
                firstThreeBallsTrajectory.strafeTo(new Vector2d(-11.5, 36));
                firstThreeBallsTrajectory.splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2);
                Actions.runBlocking(
                        turret.alignShooter() // align shooter
                );
                kickCurrentBall(); // kick ball onto flywheel

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
    private void kickCurrentBall(){
        kicker1.setPosition(KICKER_MAX_POSITION);
        kicker2.setPosition(KICKER_MAX_POSITION);
        if (motifIndex < 2){
            motifIndex++;
        }
        else{
            motifIndex = 0;
        }
        artifacts.set(0, Artifact.EMPTY);
        sleep(300);
        kicker1.setPosition(KICKER_MIN_POSITION);
        kicker2.setPosition(KICKER_MIN_POSITION);
    }

    public void initMotors() {
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        rearLeft = hardwareMap.get(DcMotor.class, "rear-left");
        rearRight = hardwareMap.get(DcMotor.class, "rear-right");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter-motor-1");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter-motor-2");

        spindexer = hardwareMap.get(Servo.class, "spindexer");
        kicker1 = hardwareMap.get(Servo.class, "servo-kicker-1");
        kicker2 = hardwareMap.get(Servo.class, "servo-kicker-2");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake-motor");
        rotator = hardwareMap.get(DcMotorEx.class, "turret-rotation");
        encoderInitialPosition = rotator.getCurrentPosition();

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter-motor-1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter-motor-2");
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
