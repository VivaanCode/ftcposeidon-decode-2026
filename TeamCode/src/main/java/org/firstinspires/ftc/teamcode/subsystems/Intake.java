package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intakeMotor;
    private double intakeEnabledPower = 0.6;
    private double intakeIdlePower = 0;
    public Intake(HardwareMap hardwareMap){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake-motor");
    }
    public Action toggleIntakeOn(){
        return new ToggleIntakeOn();
    }
    public Action toggleIntakeOff(){
        return new ToggleIntakeOff();
    }
    public class ToggleIntakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeMotor.setPower(intakeEnabledPower);
            return true;
        }
    }
    public class ToggleIntakeOff implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeMotor.setPower(intakeIdlePower);
            return true;
        }
    }
}
