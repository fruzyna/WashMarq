package com.mail929.android.washmarq;

/**
 * Created by mail929 on 1/29/16.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Activity for customizing app settings.
 */
public class SettingsActivity extends PreferenceActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("WASHMARQ", 0);
        String fave = prefs.getString("FAVE", "NONE");
        String faveMach = prefs.getString("FAVE_MACHINE", "NONE");


        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);

        PreferenceCategory gen = new PreferenceCategory(this);
        gen.setTitle("General");
        ps.addPreference(gen);

        Preference sort = new Preference(this);
        sort.setTitle("Auto Open Building");

        if(fave.equals("NONE"))
        {
            sort.setSummary("Automatically opens a chosen building");
        }
        else
        {
            sort.setSummary("Automatically opens " + fave.replace("-", " ").replace(".aspx", " "));
        }
        sort.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                DialogFragment dialog = new ChooseFavoriteDialog();
                dialog.show(getFragmentManager(), "");
                return true;
            }
        });
        gen.addPreference(sort);


        Preference machine = new Preference(this);
        machine.setTitle("Favorite Machine");

        if(faveMach.equals("NONE"))
        {
            machine.setSummary("No favorite set");
        }
        else
        {
            machine.setSummary("Favorite machine is: " + faveMach.replace("-", " ").replace(".aspx", " - ").replace(":", ""));
        }
        gen.addPreference(machine);

        PreferenceCategory about = new PreferenceCategory(this);
        about.setTitle("About");
        ps.addPreference(about);

        //The version number of the app
        Preference version = new Preference(this);
        version.setTitle("App Version");
        try
        {
            version.setSummary(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        about.addPreference(version);

        //About me and a link to my site
        Preference me = new Preference(this);
        me.setTitle("2014-16 Liam Fruzyna");
        me.setSummary("mail929.com");
        me.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mail929.com"));
                startActivity(browserIntent);
                return true;
            }
        });
        about.addPreference(me);

        setPreferenceScreen(ps);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        //sets up the view
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        bar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        bar.setTranslationZ(8);
        root.addView(bar, 0);
        bar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null)
        {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }
}