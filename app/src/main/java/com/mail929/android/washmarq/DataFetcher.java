package com.mail929.android.washmarq;

import android.os.NetworkOnMainThreadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mail929 on 1/26/16.
 */
public class DataFetcher
{
    public static ArrayList<Machine> machineList;

    public static void fetchData(final String link)
    {
        Thread t = (new Thread() {
        public void run() {

        machineList = new ArrayList<>();
        try
        {
            URL url = new URL("https://wash.mu.edu/washalertweb/" + link);
            System.out.println("Getting data from: " + url.toString());
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();

            String[] machines = sb.toString().split("<tr class=\"");


            for (int i = 0; i < machines.length; i++)
            {
                String mode = machines[i].substring(0, machines[i].indexOf("\""));

                String[] datas = machines[i].split("<td class=\"");
                String name = "";
                String type = "";
                String status = "";
                int timeLeft = 0;
                for (int j = 0; j < datas.length; j++)
                {
                    if (datas[j].contains("name\">"))
                    {
                        String[] split = datas[j].split("<br />");
                        name = split[0].substring(split[0].length() - 2);
                        if (split[1].contains("Dryer"))
                        {
                            type = "Dryer";
                        } else if (split[1].contains("Washer"))
                        {
                            type = "Washer";
                        }
                    } else if (datas[j].contains("status\">"))
                    {
                        status = datas[j].substring(datas[j].indexOf(">") + 1, datas[j].indexOf("<"));
                    } else if (datas[j].contains("time\">") && datas[j].contains("</div></div>"))
                    {
                        timeLeft = Integer.parseInt(datas[j].substring(datas[j].lastIndexOf("</div>") + 6, datas[j].lastIndexOf(" minutes")));
                    }
                }
                System.out.println("Name: " + name + "; Type: " + type + "; Status: " + status + "; Time Left: " + timeLeft);
                if (type.equals("Dryer"))
                {
                    machineList.add(new Dryer(name, status, timeLeft));
                } else if (type.equals("Washer"))
                {
                    machineList.add(new Washer(name, status, timeLeft));
                }
            }
            System.out.println("Finished reading data");
        }
        catch(IOException e)
        {

        }
        }
        });
        t.start();
        try
        {
            t.join();
            System.out.println("Joined");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("DataFetcher Finished");
    }
}