package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 2/1/16.
 */
public class AutonomousConcept51 extends AutonomousVariables {
    @Override
    public void setupAutonomous() {
        addWaitAction(4000);
        setCollisionProfile(COLLISION_CHANGE_DIRECTION);
        addWaitAction(2000);
        setCollisionProfile(COLLISION_WAIT);
        addWaitAction(1000);
        addState(STATE_STOP);
    }
}
