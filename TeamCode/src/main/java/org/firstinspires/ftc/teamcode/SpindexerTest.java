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
        }
    }
}
