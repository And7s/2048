package com.example.aschmelz.opengl2048;

import android.util.Log;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_UP;
import static com.example.aschmelz.opengl2048.ImgConsts.COLOR_BOARD;
import static com.example.aschmelz.opengl2048.ImgConsts.COLOR_TEXT_DARK;
import static com.example.aschmelz.opengl2048.ImgConsts.COLOR_TEXT_LIGHT;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SOLID_WHITE;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_RED;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_WHITE;

public class Overlay extends RenderHelper {

    private Board board;
    private ClickRegion CR_new_game = new ClickRegion(0.65f, 0.17f, 0.95f, .23f, 0);

    private int curScore = 0;
    boolean menuActive = false;
    double timeInMenu = 0;

    public Overlay(GLRenderer renderer) {
        this.renderer = renderer;
        board = new Board(renderer, this);
    }

    public void onSurfaceChanged(float width, float height) {
        offsetX = 0;
        offsetY = 0;

        this.width = width;
        this.height = height;
        board.onSurfaceChanged(width, height);
    }

    public void processTouchEvent(MotionEvent event) {
        board.processTouchEvent(event);

        float x = (event.getX() - offsetX) / width, // relative coords (0-1 if inside)
                y = (event.getY() - offsetY) / height;

        if (event.getAction() == ACTION_UP && contain(x, y, CR_new_game)) {
            //
            Log.d("new game", "clicked");
            menuActive = true;
        } else if (event.getAction() == ACTION_UP && menuActive) {
           double distSq = Math.pow(x - 0.7f, 2) + Math.pow(y - 0.5f, 2);
           Log.d("dist", distSq + "");
            if (menuActive && distSq < 0.02) {    // needs to be refined
                board.newGame();
                menuActive = false;
            } else {
                menuActive = false;
            }
        }


    }

    public void update(double dt) {
        board.update(dt);

        drawText("2048", 0.1f, 0.1f, COLOR_TEXT_DARK, 200);
        renderRoundCorner(0.65f, 0.05f, 0.95f, 0.15f, COLOR_BOARD, 20f);


        drawText("SCORE", 0.8f, 0.06f, COLOR_TEXT_LIGHT, 75, 0.5f , 0);
        drawText(curScore + "", 0.8f, 0.1f, COLOR_WHITE, 100, 0.5f, 0);

        renderRoundCorner(CR_new_game.left, CR_new_game.top, CR_new_game.right, CR_new_game.bottom, COLOR_BOARD, 20f);

        drawText("NEW GAME", 0.8f, 0.185f, COLOR_TEXT_LIGHT, 75, 0.5f , 0);

        if (menuActive) {
            timeInMenu += dt;

            float alpha = (float) (Math.sin(timeInMenu) * .1 + 0.8);

            coverImage(SPR_SOLID_WHITE, 0,0,1,1, mixColor(COLOR_BOARD, alpha));
            drawText("Start new game", 0.5f, 0.3f, COLOR_TEXT_LIGHT, 90, 0.5f , 0);

            drawText("No", 0.3f, 0.5f, COLOR_TEXT_LIGHT, 75, 0.5f , 0.5f);

            drawText("yes", 0.7f, 0.5f, COLOR_TEXT_LIGHT, 75, 0.5f , 0.5f);
        }

    }

    private float[] mixColor(float[] color, float alpha) {
        float[] ret = {color[0] * alpha, color[1] * alpha, color[2] * alpha, color[3] * alpha};
        return ret;
    }

    public void updateScore(int add, int total) {
        curScore = total;
    }
}
