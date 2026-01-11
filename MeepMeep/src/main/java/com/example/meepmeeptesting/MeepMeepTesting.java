package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(64, 22, Math.toRadians(180)))
                .lineToX(30)
                //wait for april tag reading to read motif
                .waitSeconds(2)
                .splineTo(new Vector2d(60, 10), Math.toRadians(210))
                //shooting preloaded balls
                .waitSeconds(5)
                //intake balls on field
                .splineTo(new Vector2d(35, 50), Math.toRadians(90))
                //spindexer organizes balls by motif
                .waitSeconds(1)
                .splineTo(new Vector2d(60, 10), Math.toRadians(210))
                //shooting the balls
                .waitSeconds(5)
                //get balls from human player
                .strafeTo(new Vector2d(60, 60))
                .strafeTo(new Vector2d(60, 10))
                //shoot human player's balls (pause)
                .waitSeconds(5)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}