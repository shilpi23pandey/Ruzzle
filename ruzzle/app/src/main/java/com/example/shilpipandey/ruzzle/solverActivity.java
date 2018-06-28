package com.example.shilpipandey.ruzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class solverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);
        String[] validBoardWords = getIntent().getStringArrayExtra("validBoardWords");
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        for(String word : validBoardWords) {
            Log.i("SolverWord ", word);
            TextView wordText = new TextView(this);
            wordText.setText(word);
            linearLayout.addView(wordText);
        }
    }
    public void goMainActivity(View v) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
