package ru.samsung.itschool.dbgame;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HoFActivity extends Activity {

    DBManager dbManager;
    final ArrayList<Result> results = new ArrayList<Result>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_f);
        dbManager = new DBManager(this);
        publish();

    }

    public void gen5games(View v)
    {
        String[] userNames = new String[]{"Peter", "Alex", "Vadim", "Grisha", "Vova"};
        Random rand = new Random();
        for (int i = 0; i < 5; i++)
        {
            dbManager.addResult(userNames[rand.nextInt(userNames.length)], rand.nextInt(1000));
        }
        publish();
    }

    public void publish(){
        TextView restv = (TextView)this.findViewById(R.id.textView);
        ArrayList<Result> results = dbManager.getAllResults();
        String resStr = "";
        for (Result res : results)
        {
            resStr += res.name + ": " + res.score + "\n";
        }
        restv.setText(resStr);
    }
}
