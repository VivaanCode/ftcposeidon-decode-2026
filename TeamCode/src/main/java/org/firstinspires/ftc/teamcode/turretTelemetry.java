package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Turret Telemetry")

public class turretTelemetry extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor turretRotation = hardwareMap.get(DcMotor.class, "turret-rotation");
        DcMotor rearRight = hardwareMap.get(DcMotor.class, "rear-right");
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("position", turretRotation.getCurrentPosition());
            telemetry.addData("rear-right position", rearRight.getCurrentPosition());
            telemetry.update();
        }
    }
}
