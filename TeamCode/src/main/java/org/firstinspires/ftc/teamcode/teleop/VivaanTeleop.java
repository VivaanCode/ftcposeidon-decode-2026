package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.acmerobotics.roadrunner.Actions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.Turret;


@TeleOp(name = "Vivaans tleeop caljfoierwhjg")
public class VivaanTeleop extends OpMode {
    private DcMotor frontLeft, frontRight, rearLeft, rearRight;
    Servo kicker1, kicker2;

    Turret turret = new Turret(hardwareMap, drive.localizer, Alliance.BLUE_ALLIANCE);

    @Override
    public void init() {}

    /** This initializes the PoseUpdater, the mecanum drive motors, and the Panels telemetry. */
    @Override
    public void init_loop() {
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        rearLeft = hardwareMap.get(DcMotor.class, "rear-left");
        rearRight = hardwareMap.get(DcMotor.class, "rear-right");

        kicker1 = hardwareMap.get(Servo.class, "servo-kicker-1");
        kicker2 = hardwareMap.get(Servo.class, "servo-kicker-2");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {

    }

    /**
     * This updates the robot's pose estimate, the simple mecanum drive, and updates the
     * Panels telemetry with the robot's position as well as draws the robot's position.
     */
    @Override
    public void loop() {
        moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        if (gamepad1.x){
            kicker1.setPosition(0.65);
            sleep(300);
            kicker1.setPosition(0.1);
        }
        if (gamepad1.y){
            Actions.runBlocking(
                    turret.alignShooter()
            );
        }
    }
    public void moveRobot(double leftStickX, double leftStickY, double rightStickX) {
        double speed = leftStickY;   // Forward/Backward movement
        double strafe = -leftStickX;  // Left/Right movement (strafe)
        double turn = -rightStickX;   // Rotation


        // Calculate each motor's power
        double frontLeftPower = (speed + turn + strafe);
        double frontRightPower = (speed - turn - strafe);
        double backLeftPower = speed + turn - strafe;
        double backRightPower = speed - turn + strafe;

        // Set motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        rearLeft.setPower(backLeftPower);
        rearRight.setPower(backRightPower);
    }

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

