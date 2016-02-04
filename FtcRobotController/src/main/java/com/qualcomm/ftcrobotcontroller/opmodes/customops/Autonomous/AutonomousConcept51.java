package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 2/1/16.
 */
public class AutonomousConcept51 extends AutonomousVariables {
    @Override
    public void setupAutonomous() {
        addArmAction(ARM_ACTION, -1024, -0.2);
        addState(STATE_STOP);
    }
}
