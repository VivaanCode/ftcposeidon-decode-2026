package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name = "Color Sensor Test")
public class ColorSensor extends LinearOpMode {
    NormalizedColorSensor colorSensor;
    private float colorSensorGain = 5;
    @Override
    public void runOpMode() throws InterruptedException {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color-sensor");
        colorSensor.setGain(colorSensorGain);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up){
                colorSensorGain += 0.05;
            }
            else if (gamepad1.dpad_down){
                colorSensorGain -= 0.05;
            }

            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            telemetry.addData("red", colors.red/colors.alpha);
            telemetry.addData("blue", colors.blue/colors.alpha);
            telemetry.addData("green", colors.green/colors.alpha);
            telemetry.addData("alpha", colors.alpha);
            telemetry.addData("gain", colorSensorGain);

            telemetry.update();
            colorSensor.setGain(colorSensorGain);
        }
    }
}
