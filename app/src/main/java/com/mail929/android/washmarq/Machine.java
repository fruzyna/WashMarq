package com.mail929.android.washmarq;

/**
 * Created by mail929 on 1/26/16.
 */
public class Machine
{
    String name;
    String status;
    String type;
    int time;

    public Machine(String name, String status, String type, int time)
    {
        this.name = name;
        this.status = status;
        this.time = time;
        this.type = type;
    }
}
