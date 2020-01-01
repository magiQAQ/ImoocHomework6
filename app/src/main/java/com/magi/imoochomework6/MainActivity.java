package com.magi.imoochomework6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.magi.imoochomework6.view.Cube;

public class MainActivity extends AppCompatActivity {

    private FrameLayout gameLayout;
    private ImageView leftButton;
    private ImageView bottomButton;
    private ImageView rightButton;

    private Bitmap red;
    private Bitmap blue;
    private Cube currentCube;
    private int innerWidth;
    private int innerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

        red = BitmapFactory.decodeResource(getResources(),R.drawable.red);
        blue = BitmapFactory.decodeResource(getResources(),R.drawable.blue);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            innerWidth = gameLayout.getMeasuredWidth();
            innerHeight = gameLayout.getMeasuredHeight();
            Log.e("innerWidth",String.valueOf(innerWidth));
            startGame();
        }
    }

    private void startGame() {
        Cube cube = new Cube(this,red);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(innerWidth/6,innerWidth/6);
        cube.setLayoutParams(layoutParams);
        gameLayout.addView(cube);
        currentCube = cube;
    }

    private void initView() {
        gameLayout = findViewById(R.id.gameLayout);
        leftButton = findViewById(R.id.left_button);
        bottomButton = findViewById(R.id.bottom_button);
        rightButton = findViewById(R.id.right_button);
    }

    private void initEvent() {
        View.OnClickListener onClickListener = view -> {
            switch (view.getId()){
                case R.id.left_button:
                    break;
                case R.id.bottom_button:
                    break;
                case R.id.right_button:
                    break;
            }
        };
        leftButton.setOnClickListener(onClickListener);
        bottomButton.setOnClickListener(onClickListener);
        rightButton.setOnClickListener(onClickListener);
    }
}
