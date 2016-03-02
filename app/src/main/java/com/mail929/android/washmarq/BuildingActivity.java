package com.mail929.android.washmarq;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mail929 on 1/26/16.
 */
public class BuildingActivity extends AppCompatActivity
{
    ArrayList<Machine> machines;
    LinearLayout list;
    CheckBox washers;
    CheckBox dryers;
    CheckBox unavailable;
    String url = "straz-tower.aspx";
    SharedPreferences prefs;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        prefs = getSharedPreferences("WASHMARQ", 0);

        Intent intent = getIntent();
        url = intent.getExtras().getString("URL");

        getSupportActionBar().setTitle(url.replace("-", " ").replace(".aspx", " "));

        machines = DataFetcher.machineList;
        list = (LinearLayout) findViewById(R.id.list);

        washers = ((CheckBox) findViewById(R.id.washers));
        washers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateList();
            }
        });
        unavailable = ((CheckBox) findViewById(R.id.unavailable));
        unavailable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateList();
            }
        });
        dryers = ((CheckBox) findViewById(R.id.dryers));
        dryers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateList();
            }
        });
        DataFetcher.fetchData(url, this);
        machines = DataFetcher.machineList;
        updateList();

        c = this;
        (new Thread()
        {
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("Refreshing");
                    DataFetcher.fetchData(url, c);
                    machines = DataFetcher.machineList;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                        }
                    });
                }
            }
        }).start();
    }

    public void updateList()
    {
        System.out.println("Updating list");
        list.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < machines.size(); i++)
        {
            View view = inflater.inflate(R.layout.listitem_machine, list, false);

            Machine m = machines.get(i);
            final String status = m.status;
            final String name = m.name;
            final String type = m.type;
            final int time = m.time;

            TextView nametv = (TextView) view.findViewById(R.id.top);
            nametv.setText(type + " " + name);

            TextView statustv = (TextView) view.findViewById(R.id.bottom);
            if (time > 0)
            {
                statustv.setText(status + " - " + time + " minutes remaining");
            } else
            {
                statustv.setText(status);
            }

            View side = view.findViewById(R.id.view);
            if (status.equals("Available"))
            {
                side.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else if (status.equals("In use") || status.equals("Not online") || status.equals("Out of order"))
            {
                side.setBackgroundColor(Color.parseColor("#F44336"));
            } else if (status.equals("Almost done") || status.equals("End of cycle") || status.equals("Ready to start") || status.equals("Payment in progress"))
            {
                side.setBackgroundColor(Color.parseColor("#FFEB3B"));
            }
            if(prefs.getString("FAVE_MACHINE", "NONE").equals(url + ":" + name))
            {
                view.setBackgroundColor(Color.parseColor("#FFC107"));
                list.addView(view, 0);
            }
            else if(status.equals("Available") || unavailable.isChecked())
            {
                if(type.equals("Washer"))
                {
                    if(washers.isChecked())
                    {
                        list.addView(view);
                    }
                }
                else
                {
                    if(dryers.isChecked())
                    {
                        list.addView(view);
                    }
                }
            }

            final Context c = this;
            view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    SharedPreferences.Editor editor = prefs.edit();
                    if(!prefs.getString("FAVE_MACHINE", "NONE").equals(url + ":" + name))
                    {
                        editor.putString("FAVE_MACHINE", url + ":" + name);
                        Toast.makeText(c, "Set favorite machine to: " + name, Toast.LENGTH_SHORT).show();
                        scheduleNotification(getNotification(type + " " + name + " is ready!"), time * 60 * 1000);
                    }
                    else
                    {
                        editor.putString("FAVE_MACHINE", "NONE");
                        Toast.makeText(c, "Removed favorite machine", Toast.LENGTH_SHORT).show();
                    }
                    editor.commit();
                    return false;
                }
            });
        }
    }


    private void scheduleNotification(Notification notification, int delay)
    {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content)
    {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Machine Complete");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_notif);
        return builder.build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                DataFetcher.fetchData(url, c);
                machines = DataFetcher.machineList;
                updateList();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the inbuilding; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inbuilding, menu);
        return true;
    }
}

