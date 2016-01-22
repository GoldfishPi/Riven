package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Storage.Saver;

/**
 * Created by root on 1/21/16.
 */
public class AutoSaveTest extends AutonomousVariables {

    Saver saver = new Saver();

    @Override
    public void setupAutonomous()
    {

        addDriveAction(DRIVE_FORWARD, 21108, 18768, 1.0, 1.0);
        addState(STATE_STOP);

    }

    @Override
    public void loop()
    {

        saver.save(555, "kill");

    }

    @Override
    public void stop()
    {

        saver.spitAll();

    }

}
