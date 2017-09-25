package com.drunkapp.app.drunk;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jonas on 03.09.2017.
 */

public class Tick{

    // tm represents the period of time in which the timer executes the abstract function execute()
    private int tm;
    // every item gets an id to delete items from ArrayList (not finished)
    private static short idOfItem = 0;
    private ArrayList<onTick> data = new ArrayList<onTick>();

    public Tick(int time)
    {
        tm = time;
    }

    // add a onTick-instance in the ArrayList and update id-counter
    public void setTick(onTick o)
    {
        data.add(o);
        o.id = idOfItem;
        idOfItem++;
        iterateData();
    }

    // set timer and iterate over the ArrayList every x seconds/milliseconds
    private void iterateData()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Object ot: data) {
                    ((onTick) ot).execute();
                }
            }
        }, 0, tm);
    }
}
