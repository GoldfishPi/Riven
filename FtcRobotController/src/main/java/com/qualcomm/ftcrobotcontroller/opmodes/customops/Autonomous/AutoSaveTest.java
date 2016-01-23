package com.qualcomm.ftcrobotcontroller.opmodes.customops.Autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.customops.Storage.Saver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by root on 1/21/16.
 */
public class AutoSaveTest extends OpMode {

    Saver saver = new Saver();
    public int x;

    @Override
    public void init()
    {



    }

    @Override
    public void loop()
    {

        saver.save(x, "hello");
        saver.save(00, "poopy pants");
        x++;

    }

    @Override
    public void stop()
    {

        saver.spitAll();
    }



}
