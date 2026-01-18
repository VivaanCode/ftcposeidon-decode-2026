package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Arrays;


public class Spindexer {
    private Artifact[] pattern;
    private Servo spindexer;
    private NormalizedColorSensor colorSensor;

    //stores the artifacts in clockwise order starting from the artifact nearest to intake
    private ArrayList<Artifact> artifacts;
    private int motifIndex;
    private Servo kicker1;
    private Servo kicker2;
    private final double spindexIncrement = 0.22;
    private final double kicker1Launch = 0;
    private final double kicker1Idle = 0.65;
    private final double kicker2Launch = 0;
    private final double kicker2Idle = 0.65;
    private final float colorSensorGain = 11;
    public Spindexer(HardwareMap hardwareMap){
        spindexer = hardwareMap.get(Servo.class, "spindexer");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color-sensor");
        colorSensor.setGain(colorSensorGain);
        kicker1 = hardwareMap.get(Servo.class, "servo-kicker-1");
        kicker2 = hardwareMap.get(Servo.class, "servo-kicker-2");
        kicker1.setDirection(Servo.Direction.REVERSE);
        artifacts = new ArrayList<>(Arrays.asList(Artifact.EMPTY, Artifact.EMPTY, Artifact.EMPTY));
    }

    public Action incrementSpindexer(){
        return new IncrementSpindexer();
    }
    public Action decrementSpindexer(){
        return new DecrementSpindexer();
    }
    public Action sendToShooter(){
        return new SequentialAction(
                new KickArtifact(),
                ((artifacts.get(2) == Artifact.EMPTY)?(new IncrementSpindexer()):(new DecrementSpindexer()))
        );
    }
    public Artifact addColor(Artifact color){
        artifacts.add(0, color);
        return color;
    }
    public boolean setToEmptySpot(){
        if (artifacts.get(0) != Artifact.EMPTY){
            if (artifacts.get(1) == Artifact.EMPTY){
                artifacts.add(artifacts.get(0));
                artifacts.remove(0);
                Actions.runBlocking(
                        new DecrementSpindexer()
                );
                return true;
            }
            else if (artifacts.get(2) == Artifact.EMPTY){
                artifacts.add(0, artifacts.get(2));
                artifacts.remove(2);
                Actions.runBlocking(
                        new IncrementSpindexer()
                );
                return true;
            }else {
                return false;
            }
        }
        return true;
    }

    public NormalizedRGBA getColorRaw(){
        return colorSensor.getNormalizedColors();
    }

    public class IncrementSpindexer implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            artifacts.add(artifacts.get(0));
            artifacts.remove(0);
            artifacts.add(0, artifacts.get(artifacts.size()-1));
            artifacts.remove(artifacts.size()-1);
            if (spindexer.getPosition() <= 1 - spindexIncrement) {
                spindexer.setPosition(spindexer.getPosition() + spindexIncrement);
            }
            else{
                spindexer.setPosition(spindexer.getPosition() - 2*spindexIncrement);
            }
            return true;
        }

    }
    public class DecrementSpindexer implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            artifacts.add(artifacts.get(0));
            artifacts.remove(0);
            if (spindexer.getPosition() <= 1 - spindexIncrement) {
                spindexer.setPosition(spindexer.getPosition() - spindexIncrement);
            } else {
                spindexer.setPosition(spindexer.getPosition() + 2 * spindexIncrement);
            }
            return true;
        }
    }

    public class KickArtifact implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            Artifact nextArtifact = pattern[motifIndex];
            if (nextArtifact != artifacts.get(0)){
                if (artifacts.get(1) == nextArtifact){
                    Actions.runBlocking(new IncrementSpindexer());
                }
                else if (artifacts.get(2) == nextArtifact){
                    Actions.runBlocking(new DecrementSpindexer());
                }
            }
            kicker1.setPosition(kicker1Launch);
            kicker2.setPosition(kicker2Launch);
            artifacts.set(0, Artifact.EMPTY);
            Actions.runBlocking(new SleepAction(1));
            kicker1.setPosition(kicker1Idle);
            kicker2.setPosition(kicker2Idle);
            if (motifIndex < 3){
                motifIndex++;
            }
            else{
                motifIndex = 0;
            }
            return true;
        }
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
