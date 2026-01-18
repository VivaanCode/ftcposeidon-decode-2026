package org.firstinspires.ftc.teamcode.teleop;

import static android.text.TextUtils.isEmpty;

import android.graphics.Color;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.subsystems.Artifact;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// spindexer

@TeleOp(name = "Teleop Main")
public class TeleopMain extends LinearOpMode {

    // HARDWARE MAP INITIALIZE ------------------------------
    // MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
    DcMotor frontLeft, frontRight, rearLeft, rearRight, shooter1, shooter2;
    DcMotorEx shooterMotor, shooterMotor2, intakeMotor, rotator;
    Artifact[] pattern;
    NormalizedColorSensor colorSensor;
    MecanumDrive drive;
    int colorSensorGain = 11;
    Alliance alliance = Alliance.BLUE_ALLIANCE;

    private ArrayList<Artifact> artifacts;

    Servo kicker1, kicker2, spindexer;

    double KICKER_MAX_POSITION = 0.65;
    double KICKER_MIN_POSITION = 0;
    double SPINDEXER_ONE_ROTATION = 0.66;
    double SPINDEXER_POSITION = 0;
    double KICKER_SERVO_POSITION = 0;
    double NEAR_SIDE_SHOOT_POWER = 0.75;
    double FAR_SIDE_SHOOT_POWER = 0.95;
    double encoderTicksToDegrees = 672/90.0;
    int encoderInitialPosition;
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    boolean SPINDEXER_AUTO = false;
    boolean TURRET_AUTO = true;
    int motifIndex = 0;

    String SHOOTER_STATE = "IDLE"; // IDLE, SPINUP, READY
    String KICKER_STATE = "IDLE"; // IDLE, MOVE_UP, WAITING, MOVE_DOWN, LOAD_TO_SHOOTER

    @Override
    public void runOpMode() throws InterruptedException {
        //Spindexer spindexerMech = new Spindexer(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(62, 14, Math.toRadians(180)));
        aprilTagWebcam.init(hardwareMap, telemetry);
        //Intake intakeMech = new Intake(hardwareMap);
        initMotors();
        SPINDEXER_POSITION = spindexer.getPosition();
        KICKER_SERVO_POSITION = kicker1.getPosition();
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            // DEBUG - SET KICKER DOWN MAX
            handleManualInputs();
            if (gamepad2.a) {
                kicker1.setPosition(KICKER_MAX_POSITION);
                kicker2.setPosition(KICKER_MAX_POSITION);
            }
            if (gamepad2.b){
                kicker1.setPosition(KICKER_MIN_POSITION);
                kicker2.setPosition(KICKER_MIN_POSITION);
            }
            if (gamepad2.y) {
                shooter1.setPower(NEAR_SIDE_SHOOT_POWER);
                if (SPINDEXER_AUTO) setCorrectSpindex();
                sleep(1000);
                kickCurrentBall();
            }
            if (gamepad2.x){
                shooter1.setPower(FAR_SIDE_SHOOT_POWER);
                if (SPINDEXER_AUTO) setCorrectSpindex();
                sleep(1200);
                kickCurrentBall();
            }
            if (gamepad2.start){
                TURRET_AUTO = true;
            }
            if (gamepad2.back) {
                TURRET_AUTO = false;
            }
            if (gamepad1.start){
                SPINDEXER_AUTO = true;
            }
            if(gamepad1.back){
                SPINDEXER_AUTO = false;
            }
            if (gamepad2.left_bumper){
                if (SPINDEXER_AUTO) setToEmptyPosition();
                intakeMotor.setPower(0.8);
            }
            if (gamepad2.right_bumper){
                intakeMotor.setPower(0);
            }

            if (intakeMotor.getPower() > 0 && SPINDEXER_AUTO){
                Artifact detected = colorDetectHSV();
                if (detected != Artifact.EMPTY) {
                    artifacts.set(0, detected);
                    intakeMotor.setPower(0);
                }
            }


            // Actions.runBlocking(turret.alignShooter());
            // move the robot
            updateCamera();
            if (TURRET_AUTO) updateTurret();

            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            telemetry.addData("Shooter time", shooterMotor.getVelocity());
            telemetry.update();
        }

    }
    private void setCorrectSpindex(){
        if (pattern[motifIndex] != artifacts.get(0)){
            if (artifacts.get(1) == pattern[motifIndex]){
                spindexer.setPosition(SPINDEXER_POSITION + SPINDEXER_ONE_ROTATION / 3);
                artifacts.add(artifacts.get(0));
                artifacts.remove(0);
            }
            else if (artifacts.get(2) == pattern[motifIndex]){
                spindexer.setPosition(SPINDEXER_POSITION - SPINDEXER_ONE_ROTATION / 3);
                artifacts.add(0, artifacts.get(2));
                artifacts.remove(2);
            }
        }
    }
    private void updateCamera() {
        aprilTagWebcam.update();
        /*localizer.update();
        pose = localizer.getPose();*/
        List<AprilTagDetection> detections = aprilTagWebcam.getDetectedTags();
        if (!detections.isEmpty()) {
            SPINDEXER_AUTO = true;
            for (AprilTagDetection detection : detections){
                if (detection.id == 20 || detection.id == 24)continue;
                else{
                    switch(detection.id){
                        case 20:
                            pattern = new Artifact[]{Artifact.PURPLE, Artifact.PURPLE, Artifact.GREEN};
                            break;
                        case 21:
                            pattern = new Artifact[]{Artifact.GREEN, Artifact.PURPLE, Artifact.PURPLE};
                            break;
                        case 22:
                            pattern = new Artifact[]{Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE};
                            break;
                    }
                }
            }
        }
    }

    private boolean setToEmptyPosition(){
        if (artifacts.get(0) != Artifact.EMPTY){
            if (artifacts.get(1) == Artifact.EMPTY){
                artifacts.add(artifacts.get(0));
                artifacts.remove(0);
                SPINDEXER_POSITION -= SPINDEXER_ONE_ROTATION / 3;
                return true;
            }
            else if (artifacts.get(2) == Artifact.EMPTY){
                artifacts.add(0, artifacts.get(2));
                artifacts.remove(2);
                SPINDEXER_POSITION += SPINDEXER_ONE_ROTATION / 3;
                return true;
            }else {
                return false;
            }
        }
        return true;
    }
    private void handleManualInputs() {
        if (gamepad1.left_trigger > 0.4) {
            kicker1.setPosition(KICKER_MIN_POSITION);
            kicker2.setPosition(KICKER_MIN_POSITION);
        }
        // DEBUG - SET KICKER UP MAX
        if (gamepad1.right_trigger > 0.4) {
            kicker1.setPosition(KICKER_MAX_POSITION);
            kicker2.setPosition(KICKER_MAX_POSITION);
        }

        if (gamepad1.leftBumperWasPressed()) {
            SPINDEXER_POSITION -= SPINDEXER_ONE_ROTATION / 3;
            SPINDEXER_AUTO = false;
        }
        if (gamepad1.leftBumperWasPressed()) {
            SPINDEXER_POSITION += SPINDEXER_ONE_ROTATION / 3;
            SPINDEXER_AUTO = false;
        }
        shooter1.setPower(gamepad2.left_stick_y);
        shooter2.setPower(gamepad2.left_stick_y);
        intakeMotor.setPower(gamepad2.right_stick_x);
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
        sleep(800);
        kicker1.setPosition(KICKER_MIN_POSITION);
        kicker2.setPosition(KICKER_MIN_POSITION);
    }

    private void updateTurret(){
        Pose2d pose = drive.localizer.getPose();
        Vector2d goalPose;
        if (alliance == Alliance.RED_ALLIANCE){
            goalPose = new Vector2d(-63, 59);
        }
        else{
            goalPose = new Vector2d(-63, -59);
        }
        double degrees = Math.toDegrees(Math.atan2(goalPose.x - pose.position.x,goalPose.y -  pose.position.y) - pose.heading.toDouble());
        if (degrees > 180){
            degrees -= 360;
        }
        double targetPosition = degrees* encoderTicksToDegrees - encoderInitialPosition;
        rotator.setTargetPosition((int)Math.round(targetPosition));
        rotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotator.setPower(1);
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
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake-motor");
        rotator = hardwareMap.get(DcMotorEx.class, "turret-rotation");
        encoderInitialPosition = rotator.getCurrentPosition();

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color-sensor");
        colorSensor.setGain(colorSensorGain);

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter-motor-1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter-motor-2");
        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        artifacts = new ArrayList<>(Arrays.asList(Artifact.EMPTY, Artifact.EMPTY, Artifact.EMPTY));
    }
    private Artifact colorDetectHSV() {// Convert normalized RGB (0–1) to HSV
        // Read colors
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        int r = (int) (colors.red * 255);
        int g = (int) (colors.green * 255);
        int b = (int) (colors.blue * 255);

        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);

        float hue = hsv[0];        // 0–360
        float saturation = hsv[1]; // 0–1
        float value = hsv[2];      // 0–1

        // Detect color
        Artifact color = Artifact.EMPTY;
        if (hue > 150) {
            color = Artifact.GREEN;;
        }
        if (hue > 175) {
            color = Artifact.EMPTY;
        }
        if (hue > 200) {
            color = Artifact.PURPLE;
        }
        return color;
    }

}
