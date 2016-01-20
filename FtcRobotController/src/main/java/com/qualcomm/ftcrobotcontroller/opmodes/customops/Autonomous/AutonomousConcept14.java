package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

/**
 * Created by cyberarm on 1/20/16.
 */
public class AutonomousConcept14 extends AutonomousVariables {

    @Override
    public void setupAutonomous() {
        addDriveAction(DRIVE_FORWARD, 350, 250, 1.0, 1.0);
        addServoAction(THE_DUMPER, 1.0);
        addWaitAction(120); // time_in_ticks
        addServoAction(THE_DUMPER, 0.0);
        addDriveAction(DRIVE_FORWARD, 350, 1250, 1.0, 1.0);
        addState(STATE_STOP);
    }
}
