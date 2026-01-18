/*

MeepMeep
AUTONOMOUS INFORMATION
Balls shot: 12
Starting side: Nearside
Shooting spot: All Nearside


 */



package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Auto12BallMMNearSideNearShoot {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-62, 40, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(180)), Math.PI/2)
                .waitSeconds(1)  // shoot the preloaded balls
                .splineTo(new Vector2d(-11.5, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(-11.5, 56))
                // stop running intake
                .strafeTo(new Vector2d(-11.5, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2)
                .waitSeconds(1) // shoot the picked up balls

                .splineTo(new Vector2d(12, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(12, 56))
                // stop running intake
                .strafeTo(new Vector2d(12, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2)
                .waitSeconds(1) // shoot 3 picked up balls

                .splineTo(new Vector2d(35, 36), Math.toRadians(90))
                // start running intake
                .strafeTo(new Vector2d(35, 56))
                // stop running intake
                .strafeTo(new Vector2d(35, 36))
                // start running flywheel
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.PI/2)
                .waitSeconds(1) // shoot 3 picked up balls

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}