package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.MindTests;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous.AutonomousMindContainer;

/**
 * Created by cyberarm on 2/1/16.
 */
public class AutonomousConcept51 extends AutonomousMindContainer {
    @Override
    public void setupAutonomous() {
        addArmAction(ARM_ACTION, -1024, -0.2);
        addState(STATE_STOP);
    }
}
