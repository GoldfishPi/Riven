package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.Neurons;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 3/10/16.
 */
public class Neuron {
    public AutonomousMindContainer autonomousMindContainer;

    String currentState = "Not set";

    public static final int
            STATE_STOP    = 0,
            DRIVE_FORWARD = 1,
            DRIVE_BACKWARD= 2,
            STATE_WAIT    = 3,
            THE_DUMPER    = 4,
            ARM_ACTION    = 5,

    SET_COLLISION_PROFILE = 6,
            VIBRATOR_ACTION       = 7,
            SCREAM_ACTION         = 8,
            RELIEVED_ACTION       = 9,
            WINCH_ACTION          = 10,
            DRIVE_ARM_ACTION      = 11,
            DRIVE_WINCH_ACTION    = 12,

    LEFT_SHURIKEN  = 13,
            RIGHT_SHURIKEN = 14,

    COLLISION_IGNORE           = 0,
            COLLISION_CHANGE_DIRECTION = 1,
            COLLISION_WAIT             = 2;

    public int collisionProfile   = COLLISION_IGNORE,
            COLLISION_THRESHOLD= 6,
            tickSinceCollision = 0,
            collisionID        = 0,
            accelerometerTicks = 0,
            winchTarget        = 0;

    public double winchSpeed = 0.0;

    public boolean collisionLock     = false,
            collisionDetected = false,
            needsDrive        = false,
            needsDumper       = false,
            needsArm          = false,
            needsWinch        = false,
            needsShurikens    = true;
}
