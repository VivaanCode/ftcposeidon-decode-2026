package org.firstinspires.ftc.teamcode;
/*
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;*/
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



@TeleOp(name = "Gamepad 1 Testing")
public class Gamepad1Testing extends OpMode {
    @Override
    public void init() {/*
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
        telemetryManager.addData("Stage", "Initialized");*/
        telemetry.addData("Initialized","The program has been initialized");
    }

    @Override
    public void loop() {/*
        telemetryManager.addData("left stick x",gamepad1.left_stick_x);
        telemetryManager.addData("left stick y",gamepad1.left_stick_y);

        telemetryManager.addData("right stick x",gamepad1.right_stick_x);
        telemetryManager.addData("right stick y",gamepad1.right_stick_y);

        telemetryManager.addData("a button",(boolean)gamepad1.a);
        telemetryManager.addData("b button",(boolean)gamepad1.b);
        telemetryManager.addData("x button",(boolean)gamepad1.x);
        telemetryManager.addData("y button",(boolean)gamepad1.y);

        telemetryManager.addData("dpad down",gamepad1.dpad_down);
        telemetryManager.addData("dpad up",gamepad1.dpad_up);
        telemetryManager.addData("dpad right",gamepad1.dpad_right);
        telemetryManager.addData("dpad left",gamepad1.dpad_left);

        telemetryManager.addData("right trigger",gamepad1.right_trigger);
        telemetryManager.addData("right bumper",gamepad1.right_bumper);

        telemetryManager.addData("left trigger",gamepad1.left_trigger);
        telemetryManager.addData("left bumper",gamepad1.left_bumper);

        telemetryManager.update();*/


        telemetry.addData("left stick x",gamepad1.left_stick_x);
        telemetry.addData("left stick y",gamepad1.left_stick_y);

        telemetry.addData("right stick x",gamepad1.right_stick_x);
        telemetry.addData("right stick y",gamepad1.right_stick_y);

        telemetry.addData("a button",(boolean)gamepad1.a);
        telemetry.addData("b button",(boolean)gamepad1.b);
        telemetry.addData("x button",(boolean)gamepad1.x);
        telemetry.addData("y button",(boolean)gamepad1.y);

        telemetry.addData("dpad down",gamepad1.dpad_down);
        telemetry.addData("dpad up",gamepad1.dpad_up);
        telemetry.addData("dpad right",gamepad1.dpad_right);
        telemetry.addData("dpad left",gamepad1.dpad_left);

        telemetry.addData("right trigger",gamepad1.right_trigger);
        telemetry.addData("right bumper",gamepad1.right_bumper);

        telemetry.addData("left trigger",gamepad1.left_trigger);
        telemetry.addData("left bumper",gamepad1.left_bumper);

        telemetry.update();
    }
}
