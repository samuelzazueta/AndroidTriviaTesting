package com.vicentearmenta.androidtriviatesting.activities;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.vicentearmenta.androidtriviatesting.R;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityResultBinding;
import com.vicentearmenta.androidtriviatesting.database.DatabaseOperations;
import com.vicentearmenta.androidtriviatesting.models.Answer;
import com.vicentearmenta.androidtriviatesting.models.Score;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    DatabaseOperations mDBOperations;


    boolean playerAdded = false;

    String userId;

    List<Score> results;


    // Inital declarations for everything table related
    TableLayout tl;

    TableRow tr;

    TextView user;

    TextView score;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("USERID");

        mDBOperations = new DatabaseOperations(ResultActivity.this);

        results = mDBOperations.getResults(userId);



        //String query = "SELECT * FROM result";
        //Cursor cursor = mDatabase.rawQuery(query, null);
        //List<String> User = new ArrayList<String>();

        //while (cursor.moveToNext()) {
            //String user = cursor.getString(cursor.getColumnIndexOrThrow("RSUserName"));
            //int score = cursor.getInt(cursor.getColumnIndexOrThrow("RSScore"));
            //User.add(user);
            //hacer una lista List<String> y guardar la info de la base de datos
        //}

        //asignar informacion del listado a las vistas
        //binding.textView2.setText(User.get(0));
        //binding.textView4.setText(User.get(1));
        //binding.textView6.setText(User.get(2));
        //binding.textView8.setText(User.get(4));

        int currentPlayerScore = Integer.parseInt(results.get(0).getScore());

        tl = (TableLayout) binding.tablelayout;

    // Rows creation and parameters

        for(int i=1; i < results.size(); i++){

            int scoreValue = Integer.parseInt(results.get(i).getScore());
            String userValue = results.get(i).getUser();

            tr = new TableRow(ResultActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            user = new TextView(ResultActivity.this);
            user.setGravity(Gravity.CENTER);
            score = new TextView(ResultActivity.this);
            score.setGravity(Gravity.CENTER);

            if ( scoreValue > currentPlayerScore || playerAdded){
                setParametersRow(R.drawable.border2, userValue, scoreValue);
            } else {
            // Add current player to the scoreboard
                setParametersRow(R.drawable.border, results.get(0).getUser(), currentPlayerScore);

                playerAdded = true;
                i--;
            }

        // concatenate textview to table row
            tr.addView(user);
            tr.addView(score);

        // concatenate row to table
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        if(!playerAdded){
            tr = new TableRow(ResultActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            user = new TextView(ResultActivity.this);
            user.setGravity(Gravity.CENTER);
            score = new TextView(ResultActivity.this);
            score.setGravity(Gravity.CENTER);

            setParametersRow(R.drawable.border, results.get(0).getUser(), currentPlayerScore);
            tr.addView(user);
            tr.addView(score);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); }

        binding.nuevoJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setParametersRow(int border, String value, int scoreValue){
        user.setText(value);
        score.setText(String.valueOf(scoreValue));

        tr.setBackground(getResources().getDrawable(border));
        user.setBackground(getResources().getDrawable(border));
        user.setTextSize(13);
        score.setTextSize(13);
        user.setHeight(60);
        score.setHeight(60);
        user.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
    }

}