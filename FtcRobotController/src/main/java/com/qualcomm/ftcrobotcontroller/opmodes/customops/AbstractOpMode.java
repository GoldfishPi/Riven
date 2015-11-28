package com.qualcomm.ftcrobotcontroller.opmodes.customops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by goldfishpi on 11/24/15.
 */
public abstract class AbstractOpMode extends OpMode {

    public abstract void init();

    public abstract void init_loop();

    public abstract void loop();
}
