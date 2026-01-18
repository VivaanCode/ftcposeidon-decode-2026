package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Objects;

// spindexer

@TeleOp(name = "Teleop Main")
public class TeleopMain extends LinearOpMode {

    // HARDWARE MAP INITIALIZE ------------------------------
    // MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
    DcMotor frontLeft, frontRight, rearLeft, rearRight;
    DcMotorEx shooterMotor, shooterMotor2;

    Servo kicker1, kicker2, spindexer;

    double KICKER_MAX_POSITION = 0.6;
    double KICKER_MIN_POSITION = 0;
    double SPINDEXER_ONE_ROTATION = 0.22;
    double SPINDEXER_POSITION = 0;
    double KICKER_SERVO_POSITION = 0;
    // Turret turret = new Turret(hardwareMap, drive.localizer, Alliance.BLUE_ALLIANCE);

    // STATES
    double KICKER_LOWER_TIMER = -1;
    double SHOOTER_STOPSHOOTING_TIMER = -1;
    boolean SHOOTER_STOPSHOOTING_SET = false;
    boolean KICKER_LOWER_TIMER_SET = false;

    String SHOOTER_STATE = "IDLE"; // IDLE, SPINUP, READY
    String KICKER_STATE = "IDLE"; // IDLE, MOVE_UP, WAITING, MOVE_DOWN, LOAD_TO_SHOOTER
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        SPINDEXER_POSITION = spindexer.getPosition();
        KICKER_SERVO_POSITION = kicker1.getPosition();
        timer.reset();
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            // DEBUG - SET KICKER DOWN MAX
            if (gamepad2.dpadDownWasPressed()) {
                kicker1.setPosition(KICKER_MIN_POSITION);
                kicker2.setPosition(KICKER_MIN_POSITION);
            }
            // DEBUG - SET KICKER UP MAX
            if (gamepad2.dpadUpWasPressed()) {
                kicker1.setPosition(KICKER_MAX_POSITION);
                kicker2.setPosition(KICKER_MAX_POSITION);
            }

            if (gamepad2.dpadLeftWasPressed()) {
                SPINDEXER_POSITION -= SPINDEXER_ONE_ROTATION / 3;
            }
            if (gamepad2.dpadRightWasPressed()) {
                SPINDEXER_POSITION += SPINDEXER_ONE_ROTATION / 3;
            }

            if (gamepad2.x) {
                // turn everything off
            }
            if (gamepad2.a) {
                // spin up everything - works
                KICKER_STATE = "MOVE_UP";
                SHOOTER_STATE = "PREPARE_BALL";
            }
            if (gamepad2.y) {
                // immediately shoot - not done yet
                KICKER_STATE = "LOAD_TO_SHOOTER";
                SHOOTER_STATE = "READY";
            }

            // KICKER STATE
            if (Objects.equals(KICKER_STATE, "MOVE_DOWN")) {
                kicker1.setPosition(KICKER_MIN_POSITION);
                kicker2.setPosition(KICKER_MIN_POSITION);
            }
            if (Objects.equals(KICKER_STATE, "PREPARE_BALL")) {
                kicker1.setPosition(0.45);
                kicker2.setPosition(0.45);
            }
            if (Objects.equals(KICKER_STATE, "LOAD_TO_SHOOTER") ) {
                // stop moving, but wait 1 second before moving back down
                if (!KICKER_LOWER_TIMER_SET) {
                    KICKER_LOWER_TIMER_SET = true;
                    KICKER_LOWER_TIMER = timer.milliseconds() + 1500;
                }
                kicker1.setPosition(KICKER_MAX_POSITION);
                kicker2.setPosition(KICKER_MAX_POSITION);
            }
            if (Objects.equals(KICKER_STATE, "LOAD_TO_SHOOTER") && timer.milliseconds() > KICKER_LOWER_TIMER) {
                SHOOTER_STATE = "IDLE";
                KICKER_STATE = "MOVE_DOWN";
                KICKER_LOWER_TIMER_SET = false;
            }

            // SHOOTER STATE
            if (Objects.equals(SHOOTER_STATE, "IDLE")) {
                shooterMotor.setPower(0);
            }
            if (Objects.equals(SHOOTER_STATE, "SPIN_UP")) {
                shooterMotor.setPower(0.65);
            }
            if (Objects.equals(SHOOTER_STATE, "SPINUP") && shooterMotor.getVelocity() > 100) {
                // ready now, start shooting
                SHOOTER_STATE = "READY";
                KICKER_STATE = "LOAD_TO_SHOOTER";
                if (!SHOOTER_STOPSHOOTING_SET) {
                    SHOOTER_STOPSHOOTING_SET = true;
                    SHOOTER_STOPSHOOTING_TIMER = timer.milliseconds() + 1000;
                }
            }
            if (Objects.equals(SHOOTER_STATE, "READY") && SHOOTER_STOPSHOOTING_TIMER > timer.milliseconds()) {
                SHOOTER_STATE = "IDLE";
                SHOOTER_STOPSHOOTING_SET = false;
            }

            // spindexer
            spindexer.setPosition(SPINDEXER_POSITION);
            // Actions.runBlocking(turret.alignShooter());
            // move the robot
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            telemetry.addData("Shooter time", shooterMotor.getVelocity());
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
    }
}
