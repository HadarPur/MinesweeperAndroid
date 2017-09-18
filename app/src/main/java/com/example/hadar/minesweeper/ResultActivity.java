package com.example.hadar.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG =ResultActivity.class.getSimpleName();
    private ImageView imageView;
    private TextView tv;
    private TextView nametxt;
    private EditText info;
    private Button save;
    private int result, points, level, isMute;
    private String name;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findView();
        getResult();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //set a view for the buttons
    public void findView(){
        findViewById(R.id.homepage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        findViewById(R.id.newgame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResultActivity.this, GameActivity.class);
                intent.putExtra("Difficulty", level);
                intent.putExtra("Volume", isMute);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResultActivity.this, RecordTableActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    //get the result from the GameActivity
    public void getResult() {
        imageView=(ImageView) findViewById(R.id.res);
        tv = (TextView) findViewById(R.id.points);
        nametxt = (TextView) findViewById(R.id.enterName);
        info = (EditText) findViewById(R.id.recordName);
        save = (Button) findViewById(R.id.sendName);
        nametxt.setVisibility(View.INVISIBLE);
        info.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        Bundle ex=intent.getExtras();
        result=ex.getInt("Result");
        points=ex.getInt("Points");
        level=ex.getInt("Difficulty");
        isMute=ex.getInt("Volume");
        latitude=ex.getDouble("locationlat");
        longitude=ex.getDouble("locationlong");
        Log.d(TAG,"location: ( "+latitude+" , "+longitude+" )");

        if (result==0)
            lose();
        else
            win();

    }

    //img view for the loser
    public void lose() {
        imageView.setBackgroundResource(R.drawable.lose);
    }

    //img view for the winner with time winning
    public void win() {
        imageView.setBackgroundResource(R.drawable.winning);
        tv.setBackgroundResource(R.drawable.table);
        tv.setText("TIME: " +points+ " SEC");
        tv.bringToFront();
        nametxt.setVisibility(View.VISIBLE);
        nametxt.setText("enter yout name:");
        info.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=info.getText().toString();
                UserInfo user = new UserInfo(name,latitude,longitude,points);
                Log.d(TAG,"new user: \n");
                Log.d(TAG,"name:" +user.getName()+"\n");
                Log.d(TAG,"location: ("+user.getLatitude()+" , " +user.getLongitude()+ " )\n");
                Log.d(TAG,"points:"+user.getPoints()+ "\n");
                JsonData firebaseData = new JsonData();
                firebaseData.writeUserToDataBase(user);
                nametxt.setVisibility(View.INVISIBLE);
                info.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });
    }
}
