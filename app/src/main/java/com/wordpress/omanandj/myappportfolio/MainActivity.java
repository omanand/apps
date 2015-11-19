package com.wordpress.omanandj.myappportfolio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); fab.setOnClickListener(new
         * View.OnClickListener() {
         * 
         * @Override public void onClick(View view) { Snackbar.make(view, "Replace with your own action",
         * Snackbar.LENGTH_LONG) .setAction("Action", null).show(); } });
         */
        Button spotifySteamerBtn = (Button) findViewById(R.id.spotify_streamer_btn);
        spotifySteamerBtn.setOnClickListener(new BtnClickListener(R.string.spotify_streamer));

        Button scoresAppBtn = (Button) findViewById(R.id.scores_app_btn);
        scoresAppBtn.setOnClickListener(new BtnClickListener(R.string.scores_app));

        Button libraryAppBtn = (Button) findViewById(R.id.library_app_btn);
        libraryAppBtn.setOnClickListener(new BtnClickListener(R.string.library_app));

        Button buildItBigBtn = (Button) findViewById(R.id.build_it_bigger_btn);
        buildItBigBtn.setOnClickListener(new BtnClickListener(R.string.build_it_bigger));

        Button xyzReaderBtn = (Button) findViewById(R.id.xyz_reader_btn);
        xyzReaderBtn.setOnClickListener(new BtnClickListener(R.string.xyz_reader));

        Button capstonAppBtn = (Button) findViewById(R.id.capstone_my_own_app_btn);
        capstonAppBtn.setOnClickListener(new BtnClickListener(R.string.capstone_my_own_app));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class BtnClickListener implements View.OnClickListener
    {
        private int appNameId;

        public BtnClickListener(int appNameId)
        {
            this.appNameId = appNameId;
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(getApplicationContext(),
                    "This button will launch my app " + getString(appNameId).toLowerCase(), Toast.LENGTH_SHORT).show();

        }

    }

}
