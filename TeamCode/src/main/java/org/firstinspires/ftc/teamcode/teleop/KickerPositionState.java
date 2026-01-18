package org.firstinspires.ftc.teamcode.teleop;

public class KickerPositionState {
    public boolean isSet;
    public double targetPos;
    public double stopThresholdHigh;
    public double stopThresholdLow;
    public KickerPositionState(double position) {
        isSet = false;
        this.targetPos = position;
    }
    public void setKickerPosition(double targetPos) {
        isSet = true;
        this.targetPos = targetPos;
        double one = targetPos + 0.05;
        double two = targetPos - 0.05;
        stopThresholdHigh = Math.max(one, two);
        stopThresholdLow = Math.min(one, two);
    }
    public boolean withinThreshold(double currentPosition) {
        return currentPosition > stopThresholdLow && currentPosition < stopThresholdHigh;
    }

    public void clearState() {
        isSet = false;
    }
}
