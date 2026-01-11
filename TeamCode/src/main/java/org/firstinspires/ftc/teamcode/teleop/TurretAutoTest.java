package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

public class TurretAutoTest extends OpMode {
    Turret turret;
    MecanumDrive drive;
    double counter;
    @Override
    public void init() {
        turret = new Turret(hardwareMap, drive.localizer, Alliance.BLUE_ALLIANCE);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        Pose2d pose = drive.localizer.getPose();
        telemetry.addData("odo pos x", pose.position.x);
        telemetry.addData("odo pos y", pose.position.y);
        telemetry.addData("robot heading", pose.heading);
        telemetry.addData("turret pos", turret.rotator.getCurrentPosition());
        telemetry.addData("turret status", (turret.rotator.isBusy())?("rotating..."):("idle"));
        super.updateTelemetry(telemetry);
    }

    @Override
    public void loop() {
        if (counter % 200 == 0) {
            Action alignTurret = turret.alignShooter();
            Actions.runBlocking(
                    alignTurret
            );
        }
        counter++;
    }
}
