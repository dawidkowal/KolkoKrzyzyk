package com.example.dkkow.kolkokrzyzyk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartGames extends Activity {

    private Button bStartGames;
    private Button bStatistics;
    private Button bExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_games);

        bStartGames = (Button) findViewById(R.id.startGames);
        bStatistics = (Button) findViewById(R.id.statistics);
        bExit = (Button) findViewById(R.id.exit);

        bStartGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGames.this, MyWiFiActivity.class);
                startActivity(intent);
            }
        });

        bStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
