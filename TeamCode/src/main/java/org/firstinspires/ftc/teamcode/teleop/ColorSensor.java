
package org.firstinspires.ftc.teamcode.teleop;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name = "HSV Color Sensor Test", group = "Sensor")
public class ColorSensor extends LinearOpMode {

    private NormalizedColorSensor colorSensor;

    float colorSensorGain = 11;
    String lastDetectedBallColor = "NONE";

    @Override
    public void runOpMode() throws InterruptedException {

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color-sensor");
        colorSensor.setGain(colorSensorGain);

        waitForStart();

        while (opModeIsActive()) {

            // Adjust gain
            if (gamepad1.dpad_up) {
                colorSensorGain += 0.05;
            } else if (gamepad1.dpad_down) {
                colorSensorGain -= 0.05;
            }
            colorSensor.setGain(colorSensorGain);

            // Read colors
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            // Convert normalized RGB (0–1) to HSV
            int r = (int) (colors.red * 255);
            int g = (int) (colors.green * 255);
            int b = (int) (colors.blue * 255);

            float[] hsv = new float[3];
            Color.RGBToHSV(r, g, b, hsv);

            float hue = hsv[0];        // 0–360
            float saturation = hsv[1]; // 0–1
            float value = hsv[2];      // 0–1

            // Detect color
            String detectedColor = colorDetectHSV(hue, saturation, value);

            if (!detectedColor.equals("NONE")) {
                lastDetectedBallColor = detectedColor;
            }

            // Telemetry
            telemetry.addData("Gain", colorSensorGain);
            telemetry.addData("R", r);
            telemetry.addData("G", g);
            telemetry.addData("B", b);
            telemetry.addData("Hue", hue);
            telemetry.addData("Sat", saturation);
            telemetry.addData("Val", value);
            telemetry.addData("Detected", detectedColor);
            telemetry.addData("Last Detected", lastDetectedBallColor);
            telemetry.update();
        }
    }

    private String colorDetectHSV(float hue, float saturation, float value) {
        String colors = "NONE";
        if (hue > 150) {
            colors = "GREEN";
        }
        if (hue > 175) {
            colors = "NONE";
        }
        if (hue > 200) {
            colors = "PURPLE";
        }
        return colors;
    }
}
