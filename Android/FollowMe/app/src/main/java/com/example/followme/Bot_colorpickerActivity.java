package com.example.followme;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

public class Bot_colorpickerActivity extends AppCompatActivity implements View.OnClickListener {
    private int mSelectedColor = -1;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private double mRatioX;
    private double mRatioY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botcolorpicker);
//        Intent intent = getIntent();
        Intents intents = Intents.getInstance(getApplicationContext());
        mImageView = (ImageView) findViewById(R.id.imageView);
        final ImageButton colorPickButton = findViewById(R.id.botcolorPickButton);
        colorPickButton.setOnClickListener(this);
        updateBitmap();
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) (event.getX() * mRatioX);
                    int y = (int) (event.getY() * mRatioY);
                    //Matrix inverse = new Matrix();
                    //v.getMatrix().invert(inverse);
                    //float[] touchPoint = new float[] {event.getX(), event.getY()};
                    //inverse.mapPoints(touchPoint);
                    //int x=Integer.valueOf((int)touchPoint[0]);
                    //int y=Integer.valueOf((int)touchPoint[1]);
                    Log.i("ColorPicker", "x: " + event.getX());
                    Log.i("ColorPicker", "y: " + event.getY());
                    Log.i("ColorPicker", "rx: " + x);
                    Log.i("ColorPicker", "ry: " + y);
                    mSelectedColor = mBitmap.getPixel(x, y);
                    colorPickButton.setBackgroundColor(mSelectedColor);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBitmap();
    }

    private void updateBitmap()
    {
        Intents intents = Intents.getInstance(getApplicationContext());
        Uri selectedImageUri = intents.bot_imageUri;
        if (mImageView != null)
        {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                mImageView.setImageBitmap(mBitmap);
                int baseScreenResolutionX;
                int baseScreenResolutionY;

                //해상도 가져오기
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point theScreenResolution = new Point();
                display.getSize(theScreenResolution);
//              mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//              mImageView.setImageBitmap(mBitmap);
                baseScreenResolutionX = mBitmap.getWidth();
                baseScreenResolutionY = mBitmap.getHeight();
                Log.i("ColorPicker", "bitmap.getWidth: " + baseScreenResolutionX);
                Log.i("ColorPicker", "bitmap.getHeight: " + baseScreenResolutionY);
                // 보정값 구하기(기본 해상도에 대한 배율 구하기)
                mRatioX = baseScreenResolutionX / (double) theScreenResolution.x;
                mRatioY = baseScreenResolutionY / (double) theScreenResolution.y;
                Log.i("ColorPicker", "bitmap.getWidth: " + baseScreenResolutionX);
                Log.i("ColorPicker", "bitmap.getHeight: " + baseScreenResolutionY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intents intents = Intents.getInstance(getApplicationContext());

        int red = Color.red(mSelectedColor);
        int green = Color.green(mSelectedColor);
        int blue = Color.blue(mSelectedColor);

        String redStr = String.format(Locale.KOREA, "%03d", red);
        String greenStr = String.format(Locale.KOREA, "%03d", green);
        String blueStr = String.format(Locale.KOREA, "%03d", blue);
        intents.switchIntent.putExtra("botcolorRGB", redStr + greenStr + blueStr);
        startActivity(intents.switchIntent);
    }
}