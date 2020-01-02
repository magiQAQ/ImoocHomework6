package com.magi.imoochomework6;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.magi.imoochomework6.view.Cube;

public class MainActivity extends AppCompatActivity {

    private FrameLayout gameLayout;
    private ImageView leftButton;
    private ImageView bottomButton;
    private ImageView rightButton;

    private Bitmap red;
    private Bitmap blue;
    //当前的方格
    private Cube currentCube;
    //FrameLayout的宽度
    private int innerWidth;
    //FrameLayout的高度
    private int innerHeight;
    //方格宽高
    private int cubeWidth;
    //方格能下落的高度
    private int downHeight;
    //方格动画的高度
    private int animationHeight;
    //用于存放方格的数组，大小为6
    private Cube[] cubes = new Cube[6];
    private FrameLayout.LayoutParams layoutParams;


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
            cubeWidth = innerWidth / 6;
            downHeight = innerHeight - cubeWidth;
            startGame();
        }
    }

    //开始游戏时创建方格
    private void startGame() {
        if (currentCube == null) {
            currentCube = new Cube(this, red);
            layoutParams = new FrameLayout.LayoutParams(innerWidth / 6, innerWidth / 6);
            currentCube.setLayoutParams(layoutParams);
            gameLayout.addView(currentCube);
        }
    }

    private void initView() {
        gameLayout = findViewById(R.id.gameLayout);
        leftButton = findViewById(R.id.left_button);
        bottomButton = findViewById(R.id.bottom_button);
        rightButton = findViewById(R.id.right_button);
    }

    private void initEvent() {
        View.OnClickListener onClickListener = view -> {
            float x = currentCube.getX();
            float y = currentCube.getY();

            switch (view.getId()){
                case R.id.left_button:
                    Log.e("MainActivity", "left_button OnClick");
                    if (x > cubeWidth) {
                        Log.e("MainActivity", "cube X:" + x);
                        ObjectAnimator leftAnimator = ObjectAnimator.ofFloat(currentCube, "x", x, x - cubeWidth - 1);
                        leftAnimator.setDuration(100);
                        leftAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                buttonsCanNotClick();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                buttonsCanClick();
                            }
                        });
                        leftAnimator.start();
                    }
                    break;
                case R.id.bottom_button:
                    Log.e("MainActivity", "left_button OnClick y:" + y);
                    animationHeight = downHeight;
                    if (y == 0) {
                        Log.e("MainActivity", "cube Y:" + y);
                        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(currentCube, "y", 0, animationHeight);
                        bottomAnimator.setDuration(300);
                        bottomAnimator.setRepeatCount(6);
                        bottomAnimator.setRepeatMode(ObjectAnimator.RESTART);
                        bottomAnimator.setInterpolator(new AccelerateInterpolator());
                        bottomAnimator.addListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                buttonsCanNotClick();
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                                super.onAnimationRepeat(animation);
                                ObjectAnimator objectAnimator = (ObjectAnimator) animation;
                                //设置回弹高度为上一次下落的0.3倍
                                if (currentCube.isDown()) {
                                    animationHeight = (int) (animationHeight * 0.3);
                                }
                                currentCube.setDown(!currentCube.isDown());
                                //如果是下落，就设置加速度，否则设置减速度
                                if (currentCube.isDown()) {
                                    objectAnimator.setFloatValues(downHeight - animationHeight, downHeight);
                                    objectAnimator.setInterpolator(new AccelerateInterpolator());
                                } else {
                                    objectAnimator.setFloatValues(downHeight, downHeight - animationHeight);
                                    objectAnimator.setInterpolator(new DecelerateInterpolator());
                                }
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                buttonsCanClick();

                                //先让原先的方格变蓝
                                currentCube.setBitmap(blue);
                                //获取方格的位置
                                int pos = Math.round(currentCube.getX() / (cubeWidth + 1));
                                cubes[pos] = currentCube;
                                currentCube = new Cube(MainActivity.this, red);
                                currentCube.setLayoutParams(layoutParams);
                                gameLayout.addView(currentCube);

                                boolean isFull = true;
                                for (Cube cube : cubes) {
                                    if (cube == null) {
                                        isFull = false;
                                        break;
                                    }
                                }
                                if (isFull) {
                                    ObjectAnimator[] animators = new ObjectAnimator[6];
                                    for (int i = 0; i < animators.length; i++) {
                                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(cubes[i], "alpha", 1f, 0f);
                                        animators[i] = objectAnimator;
                                    }
                                    //准备一起执行
                                    AnimatorSet animatorSet = new AnimatorSet().setDuration(300);
                                    animatorSet.playTogether(animators);
                                    animatorSet.addListener(new AnimatorListenerAdapter() {

                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            super.onAnimationStart(animation);
                                            buttonsCanNotClick();
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);

                                            //全部移除布局
                                            for (Cube cube : cubes) {
                                                gameLayout.removeView(cube);
                                            }
                                            //将方格列表清空
                                            cubes = new Cube[6];

                                            buttonsCanClick();
                                        }
                                    });
                                    animatorSet.start();
                                }
                            }
                        });
                        bottomAnimator.start();
                    }
                    break;
                case R.id.right_button:
                    Log.e("MainActivity", "right_button OnClick");
                    if (x < innerWidth - cubeWidth) {
                        Log.e("MainActivity", "cube X:" + x);
                        ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(currentCube, "x", x, x + cubeWidth + 1);
                        rightAnimator.setDuration(100);
                        rightAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                buttonsCanNotClick();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                buttonsCanClick();
                            }
                        });
                        rightAnimator.start();
                    }
                    break;
            }
        };
        leftButton.setOnClickListener(onClickListener);
        bottomButton.setOnClickListener(onClickListener);
        rightButton.setOnClickListener(onClickListener);
    }

    private void buttonsCanClick() {
        leftButton.setClickable(true);
        rightButton.setClickable(true);
        bottomButton.setClickable(true);
    }

    private void buttonsCanNotClick() {
        leftButton.setClickable(false);
        rightButton.setClickable(false);
        bottomButton.setClickable(false);
    }


}
