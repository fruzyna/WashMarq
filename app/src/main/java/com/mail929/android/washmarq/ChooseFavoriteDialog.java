package com.mail929.android.washmarq;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

/**
 * Created by mail929 on 1/29/16.
 */
public class ChooseFavoriteDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.dialog_choosefave, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ListView list = (ListView) v.findViewById(R.id.listView);

        //setup list of lists to share
        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, WashActivity.urls) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    LayoutInflater infl = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                    convertView = infl.inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setText(WashActivity.urls[position].replace("-", " ").replace(".aspx", " "));
                return view;
            }
        });
        final ChooseFavoriteDialog c = this;

        //listen for list chosen
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                SharedPreferences prefs = getActivity().getSharedPreferences("WASHMARQ", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("FAVE", WashActivity.urls[position]);
                editor.commit();
                c.dismiss();
            }
        });

        //setup dialog
        builder.setMessage("Choose your favorite building")
                .setTitle("Choose building")
                .setView(v)
                .setNegativeButton("NONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        SharedPreferences prefs = getActivity().getSharedPreferences("WASHMARQ", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("FAVE", "NONE");
                        editor.commit();
                    }
                });
        return builder.create();
    }
}