package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Arrays;


public class Spindexer {
    private Artifact[] pattern;
    private Servo spindexer;

    //stores the artifacts in clockwise order starting from the artifact nearest to intake
    private ArrayList<Artifact> artifacts;
    private int motifIndex;
    private Servo kicker1;
    private Servo kicker2;
    private final double spindexIncrement = 0.2;
    private final double kicker1Launch = 1.0;
    private final double kicker1Idle = 0;
    private final double kicker2Launch = 0;
    private final double kicker2Idle = 1.0;
    public Spindexer(HardwareMap hardwareMap){
        spindexer = hardwareMap.get(Servo.class, "spindexer");
        kicker1 = hardwareMap.get(Servo.class, "kicker-1");
        kicker2 = hardwareMap.get(Servo.class, "kicker-2");
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
}
