package com.example.nastyagurskaya.lab4_app1;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{
    TextView tvInfo;
    TextView tvAmount;
    EditText etInput;
    Button bControl;
    int guess;
    int amount;
    boolean gameFinished;
    private static final int UNKNOWN = -1;
    private static final int RESET = -2;
    private static final int ACCEPT = -3;
    private GestureLibrary gLib;
    private GestureOverlayView gestures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView)findViewById(R.id.textView1);
        tvAmount = (TextView)findViewById(R.id.textView2);
        etInput = (EditText)findViewById(R.id.editText);
        bControl = (Button)findViewById(R.id.button1);
        guess = (int)(Math.random()*100);
        gameFinished = false;

        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            //Если жесты не загружены, то выход из приложения
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
        gestures.addOnGesturePerformedListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onClick(View v){
        if (!gameFinished){
            if (etInput.getText().toString().equals("") || etInput.getText() == null){
                showToast(R.string.wrong_input);
            }
            else {
                int inp = Integer.parseInt(etInput.getText().toString());
                if (inp < 0 || inp > 100) {
                    showToast(R.string.out_of_bounds_input);
                } else {
                    amount++;

                    tvAmount.setText(getResources().getString(R.string.try_amount) + amount);
                    if (inp < guess) {
                        showToast(R.string.ahead);
                    }
                    if (inp > guess) {
                        showToast(R.string.behind);
                    }
                    if (inp == guess) {
                        showToast(getResources().getText(R.string.hit).toString() + " " +  guess);
                        bControl.setText(getResources().getString(R.string.play_more));
                        gameFinished = true;
                    }
                }
            }
        }
        else
        {
            guess = (int)(Math.random()*100);
            amount = 0;
            bControl.setText(getResources().getString(R.string.input_value));
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            tvAmount.setText(getResources().getString(R.string.try_amount) + amount);
            gameFinished = false;
        }
        etInput.setText("");
    }
    public int getNumber(String text) {
        int res;
        switch (text) {
            case "zero": {
                res = 0;
                break;
            }
            case "one": {
                res = 1;
                break;
            }
            case "two": {
                res = 2;
                break;
            }
            case "three": {
                res = 3;
                break;
            }
            case "four": {
                res = 4;
                break;
            }
            case "five": {
                res = 5;
                break;
            }
            case "six": {
                res = 6;
                break;
            }
            case "seven": {
                res = 7;
                break;
            }
            case "eight": {
                res = 8;
                break;
            }
            case "nine": {
                res = 9;
                break;
            }
            case "reset": {
                res = RESET;
                break;
            }
            case "accept": {
                res = ACCEPT;
                break;
            }
            default: {
                res = UNKNOWN;
            }
        }
        return res;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        List<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {

            Prediction prediction = predictions.get(0);
            System.out.println(predictions.size() + " " + prediction.score + " " + prediction.name);
            if (prediction.score > 1.0) {
                int res = getNumber(prediction.name);
                if (res >= 0 && res <= 9) {
                    etInput.append(String.valueOf(res));
                } else if (res == ACCEPT) {
                    //acceptBtn();
                } else if (res == RESET) {
                    //resetBtn();
                } else {
                    showToast(R.string.unknown);
                }
            } else {
                showToast(R.string.unknown);
            }
        }
    }
    private void showToast(final String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int id) {
        showToast(getString(id));
    }

}