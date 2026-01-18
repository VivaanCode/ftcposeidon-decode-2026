package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Spindexer;
@TeleOp(name = "Spindexer test")
public class SpindexerTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo spindexer = hardwareMap.get(Servo.class, "spindexer");
        Servo kicker1 = hardwareMap.get(Servo.class, "servo-kicker-1");
        Servo kicker2 = hardwareMap.get(Servo.class, "servo-kicker-2");
        kicker1.setDirection(Servo.Direction.REVERSE);
        final double spindexerIndexIncrement = 0.22;
        final double servoLauncherIdle = 0.95;
        final double servoLauncherActive = 0.55;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up){
                kicker1.setPosition(kicker1.getPosition() + 0.001);
                kicker2.setPosition(kicker2.getPosition() + 0.001);
            }
            if (gamepad1.dpad_down){
                kicker1.setPosition(kicker1.getPosition() - 0.001);
                kicker2.setPosition(kicker2.getPosition() - 0.001);
            }
            if (gamepad1.dpad_left) {
                spindexer.setPosition(spindexer.getPosition() - 0.001);
            }
            if (gamepad1.dpad_right) {
                spindexer.setPosition(spindexer.getPosition() + 0.001);
            }
            if(gamepad1.a){
                kicker1.setPosition(servoLauncherActive);
                kicker2.setPosition(servoLauncherActive);
            }
            if (gamepad1.b){
                kicker2.setPosition(servoLauncherIdle);
                kicker1.setPosition(servoLauncherIdle);
            }
            telemetry.addData("kicker1: ", kicker1.getPosition());
            telemetry.addData("kicker2: ", kicker2.getPosition());
            telemetry.addData("spindexer", spindexer.getPosition());
            telemetry.update();
        }
    }
}
