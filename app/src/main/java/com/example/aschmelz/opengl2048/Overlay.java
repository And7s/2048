package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.util.Log;
import android.view.MotionEvent;

import static com.example.aschmelz.opengl2048.ImgConsts.*;
import static com.example.aschmelz.opengl2048.TextManager.*;

/**
 * Created by aschmelz on 15/02/2018.
 */

public class Overlay extends RenderHelper {
    private ClickRegion CR_amount_btn = new ClickRegion(0.7f, 0.15f, 0.9f, 0.5f, 0);

    public Overlay(GLRenderer renderer) {
        this.renderer = renderer;
    }



    public void update(double dt) {


        this.render(dt);
    }

    public void onSurfaceChanged(float width, float height) {

        this.width = width;
        this.height = height / 6;

        offsetX = 0;
        offsetY = 0;
    }
    public void processTouchEvent(MotionEvent event) {
        float x = (event.getX() - offsetX) / width, // relative coords (0-1 if inside)
                y = (event.getY() - offsetY) / height;
        if (x > 0 && x <= 1 && y > 0 && y <= 1) {
            Log.d("overlay", " idx " + x + ", " + y);
        }


    }
    private void render(double dt) {
        coverImage(SPR_SOLID_WHITE, 0, 0.0f, 1, 1, COLOR_GREY_TRANSPARENT);


        drawText("hello world", 0.5f, 0f, COLOR_WHITE, 60, 0.5f, 0f);


        //coverImage(SPR_UNBOUGHT, 0.7f, 0.15f, 0.9f, 0.5f);



        drawText("123123", 0.8f, 0.3f, COLOR_WHITE, 60, 0.5f, 0.5f, true);



        drawText("right aligned", 1, 0, COLOR_RED, 30, 1, 0);
        drawText("left aligned", 0, 0, COLOR_RED, 30, 0, 0);

        drawText("right aligned", 1, 1, COLOR_RED, 30, 1, 1);
        drawText("left aligned", 0, 1, COLOR_RED, 30, 0, 1);

    }
}
