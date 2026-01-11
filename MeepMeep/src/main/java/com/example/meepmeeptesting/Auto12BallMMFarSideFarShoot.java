/*

MeepMeep
AUTONOMOUS INFORMATION
Balls shot: 12
Starting side: FarSide
Shooting spot: All FarSide


 */



package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Auto12BallMMFarSideFarShoot {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(62, 14, Math.toRadians(180)))

                // detect april tags and keep in field of view
                .splineTo(new Vector2d(56, 14), Math.toRadians(180))
                .turnTo(Math.toRadians(162))

                .waitSeconds(0.8) // replace with shooting 3 preloaded balls in motif
                // start running the intake

                .splineToLinearHeading(new Pose2d(36, 30, Math.toRadians(90)), Math.toRadians(90))
                .lineToY(54)

                // stop running the intake
                // start running the flywheel/shooter

                .strafeTo(new Vector2d(37, 54))

                // optionally run the flywheel/shooter here to save 0.01% battery


                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0)
                .waitSeconds(0.8) // replace with shooting 3 intaked balls in motif
                // shoot 3 more balls (est. 5-6s)
                // start running the intake


                .splineToLinearHeading(new Pose2d(12, 30, Math.toRadians(90)), Math.toRadians(90))
                .lineToY(54)

                // stop running intake
                // start running shooter either here or after the next line

                .strafeTo(new Vector2d(13, 54))

                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0)
                .waitSeconds(0.8) // replace with shooting 3 intaked balls in motif
                // shoot 3 more balls (est. 5-6s) 36 54

                // start running the intake
                .splineToLinearHeading(new Pose2d(40, 58, Math.toRadians(0)), Math.toRadians(90))


                .strafeTo(new Vector2d(60, 58))
                //.splineToLinearHeading(new Pose2d(64, 58, Math.toRadians(0)), Math.toRadians(90))
                .strafeTo(new Vector2d(50, 58))
                .splineToLinearHeading(new Pose2d(56, 14, Math.toRadians(162)), 0)


                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}