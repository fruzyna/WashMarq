package com.mail929.android.washmarq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class WashActivity extends AppCompatActivity
{
    ArrayList<Machine> machines;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash);

        final String[] urls = {"Abbottsford-Hall.aspx", "Campus-Town-East-Basement.aspx", "Campus-Town-East-2nd-Floor.aspx", "Campus-Town-East-3rd-Floor.aspx", "Campus-Town-East-4th-Floor.aspx", "Campus-Town-West-2nd-Floor.aspx", "Campus-Town-West-3rd-Floor.aspx", "Campus-Town-West-4th-Floor.aspx", "Carpenter-Tower.aspx", "Cobeen-Hall.aspx",
                "Gilman-Building.aspx", "Humphrey-Hall-1st-Floor.aspx", "Humphrey-Hall-2nd-Floor.aspx", "Humphrey-Hall-3rd-Floor.aspx",  "Humphrey-Hall-4th-Floor.aspx", "Humphrey-Hall-5th-Floor.aspx", "Humphrey-Hall-6th-Floor.aspx", "Mashuda-Hall.aspx", "McCabe-Hall.aspx", "McCormick-Hall.aspx",
                "O'Donnell-Hall.aspx", "Schroeder-Hall.aspx", "Straz-Tower.aspx"};
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, urls)
        {
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                View view;
                if (convertView == null)
                {
                    LayoutInflater infl = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = infl.inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                view = super.getView(position, convertView, parent);

                TextView name = (TextView) view.findViewById(android.R.id.text1);
                String hall = urls[position].replace("-", " ").replace(".aspx", " ");
                name.setText(hall);
                return view;
            }
        });
        c = this;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                Intent intent = new Intent(c, BuildingActivity.class);
                intent.putExtra("URL", urls[position]);
                startActivity(intent);
            }
        });
    }
}
