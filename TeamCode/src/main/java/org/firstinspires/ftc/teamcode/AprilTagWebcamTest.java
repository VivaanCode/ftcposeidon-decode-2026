package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
// import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@Autonomous(name = "April tag test")
public class AprilTagWebcamTest extends OpMode {

    public final double xToleranceFactor = 0.7/200;
    // The tolerance in horizontal centimeters that the camera will accept as the
    // april tag being in the desired location (9.6 inches right from center).


    private final int targetedAprilTag = 20; // blue alliance
    // The april tag to be targeted (20 or 24, depending on red [24] or blue alliance [20])

    private double maximumTurretPower = 0.4; // -1 to 1

    public double skewedValue = 0; // in centimeters

    private DcMotor turretRotation;

    public double projectedPower;

    private TwoDeadWheelLocalizer localizer;

    public Pose2d pose;

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void init() {
        initMotors();
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        // update the vision portal
        aprilTagWebcam.update();
        localizer.update();
        pose = localizer.getPose();
        AprilTagDetection aprilTagId = aprilTagWebcam.getTagBySpecificId(targetedAprilTag);
        if (aprilTagId != null) {
            AprilTagDetection detectedAprilTag = aprilTagWebcam.getDetectionTelemetry(aprilTagId);

            telemetry.addData("aprilTagId String", aprilTagId.toString());

            projectedPower = Math.min(0.02*(Math.abs(detectedAprilTag.ftcPose.x+skewedValue)), maximumTurretPower);
            double xTolerance = xToleranceFactor*detectedAprilTag.ftcPose.y;
            if ((detectedAprilTag.ftcPose.x+skewedValue) > xTolerance) {
                telemetry.addData("x movement", "right");
                // move turret right
                turretRotation.setPower(projectedPower);
            } else if ((detectedAprilTag.ftcPose.x+skewedValue) < (-1*xTolerance)) {
                telemetry.addData("x movement", "left");
                // move turret left
                turretRotation.setPower(-1*(projectedPower));
            } else {
                telemetry.addData("x movement", "none");
                turretRotation.setPower(0);
            }

            telemetry.addData("position", pose);
            telemetry.addData("distance from tag", detectedAprilTag.ftcPose.y);

            telemetry.addData("x position", detectedAprilTag.ftcPose.x);
            // telemetry.addData("y position", detectedAprilTag.ftcPose.y);
            telemetry.addData("z position", detectedAprilTag.ftcPose.z);
            telemetry.addData("max turret power", maximumTurretPower);


/*
            if (gamepad1.a) {
                maximumTurretPower = (maximumTurretPower + 0.05);
            }

            if (gamepad1.b) {
                maximumTurretPower = (maximumTurretPower - 0.05);
            }
*/
        } else {
            telemetry.addData("aprilTagId String", "none detected");
            turretRotation.setPower(0);


        }
        telemetry.update();

    }

    public void initMotors() {
        turretRotation = hardwareMap.get(DcMotor.class, "turret-rotation");
        turretRotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //
        // turretRotation.setDirection(DcMotorSimple.Direction.REVERSE);


        /*
        Drivetrain stuff, not needed for this


        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "front-right");
        DcMotor rearLeft = hardwareMap.get(DcMotor.class, "rear-left");
        DcMotor rearRight = hardwareMap.get(DcMotor.class, "rear-right");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);*/
    }

}
