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
    DcMotor frontLeft, frontRight, rearLeft, rearRight;
    DcMotorEx shooterMotor;

    Servo kicker1, kicker2, spindexer;

    double KICKER_MAX_POSITION = 0.95;
    double KICKER_MIN_POSITION = 0;
    double SPINDEXER_ONE_ROTATION = 0;
    double SPINDEXER_POSITION = 0;

    // STATES
    double KICKER_LOWER_TIMER = -1;
    boolean KICKER_LOWER_TIMER_SET = false;
    String SHOOTER_STATE = "IDLE"; // IDLE, SPINUP, READY
    String KICKER_STATE = "IDLE"; // IDLE, MOVE_UP, WAITING, MOVE_DOWN, LOAD_TO_SHOOTER

    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        SPINDEXER_POSITION = spindexer.getPosition();
        initMotors();
        timer.reset();
        waitForStart();

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
            SHOOTER_STATE = "SPINUP";
        }
        if (gamepad2.y) {
            // immediately shoot
            KICKER_STATE = "LOAD_TO_SHOOTER";
            SHOOTER_STATE = "READY";
        }

        // KICKER STATE
        if (Objects.equals(KICKER_STATE, "MOVE_DOWN")) {
            kicker1.setPosition(KICKER_MIN_POSITION);
            kicker2.setPosition(KICKER_MIN_POSITION);
        }
        if (Objects.equals(KICKER_STATE, "MOVE_UP")) {
            kicker1.setPosition(0.65);
            kicker2.setPosition(0.65);
        }
        if (Objects.equals(KICKER_STATE, "MOVE_UP") && kicker1.getPosition() > 0.6) {
            KICKER_STATE = "WAITING";
        }
        if (Objects.equals(SHOOTER_STATE, "LOAD_TO_SHOOTER")) {
            kicker1.setPosition(KICKER_MAX_POSITION);
            kicker2.setPosition(KICKER_MAX_POSITION);
        }
        if (Objects.equals(KICKER_STATE, "LOAD_TO_SHOOTER") && kicker1.getPosition() > 0.9) {
            // stop moving, but wait 1 second before moving back down
            if (!KICKER_LOWER_TIMER_SET) {
                KICKER_LOWER_TIMER = timer.milliseconds() + 1000;
                KICKER_LOWER_TIMER_SET = true;
            }
            kicker1.setPosition(kicker1.getPosition());
            kicker2.setPosition(kicker2.getPosition());
        }
        if (Objects.equals(KICKER_STATE, "LOAD_TO_SHOOTER") && timer.milliseconds() > KICKER_LOWER_TIMER) {
            SHOOTER_STATE = "IDLE";
            KICKER_STATE = "MOVE_DOWN";
            KICKER_LOWER_TIMER_SET = false;
            kicker1.setPosition(KICKER_MIN_POSITION);
            kicker2.setPosition(KICKER_MIN_POSITION);
        }

        // SHOOTER STATE
        if (Objects.equals(SHOOTER_STATE, "IDLE")) {
            shooterMotor.setPower(0);
        }
        if (Objects.equals(SHOOTER_STATE, "SPIN_UP")) {
            shooterMotor.setPower(1);
        }
        if (Objects.equals(SHOOTER_STATE, "SPINUP") && shooterMotor.getVelocity() > 100 && Objects.equals(KICKER_STATE, "WAITING")) {
            // ready now, start shooting
            SHOOTER_STATE = "READY";
            KICKER_STATE = "LOAD_TO_SHOOTER";
        }

        // spindexer
        spindexer.setPosition(SPINDEXER_POSITION);

        // move the robot
        moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
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

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter-motor");

        spindexer = hardwareMap.get(Servo.class, "spindexer");
        kicker1 = hardwareMap.get(Servo.class, "kicker-1");
        kicker2 = hardwareMap.get(Servo.class, "kicker-2");
    }
}
