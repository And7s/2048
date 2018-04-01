package com.example.aschmelz.opengl2048;

import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SOLID_WHITE;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_RED;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_WHITE;

/**
 * Created by aschmelz on 31/03/2018.
 */

public class Tile extends RenderHelper {
    int destX, destY;
    float curX, curY, curScale, destScale,
    startX, startY, startScale;

    public int state = 0;

    public Tile(GLRenderer renderer, float width, float height, float offsetX, float offsetY) {
        this.renderer = renderer;
        onSurfaceChanged(width, height, offsetX, offsetY);
    }

    public void update(double dt) {
        this.render(dt);
    }


    private float[][] bgColor = {
            {.929f, .890f, .852f, 1},   // 2
            {.926f, .875f, .781f, 1},   // 4
            {.945f, .691f, .473f, 1},   // 8
            {.957f, .582f, .387f, 1},   // 16
            {.861f, .484f, .371f, 1},   // 32
            {.960f, .367f, .230f, 1},   // 64
            {.926f, .809f, .445f, 1},   // 128
    };

    private int[] fontSizes = {
            60, // 2
            60, // 4
            60, // 8
            50, // 16
            50, // 32
            50, // 64
            40, // 128
            40, // 256
    };


    private void render(double dt) {
        drawText("hello world" + state, 0.1f, 0.1f, COLOR_RED, 30, false);
        int scale = 30;
        float[] color = COLOR_RED;
        curX = destX;
        curY = destY;
        if (state >= 1 && state <= bgColor.length) {
            color = bgColor[state - 1];
            scale = fontSizes[state - 1];
            coverImage(SPR_SOLID_WHITE, 0.25f * curX, 0.25f * curY, 0.25f * (curX + 1), 0.25f * (curY + 1), 0.01f, 1, color);
            drawText(  (int) Math.pow(2, state) + "", 0.25f * (curX + 0.5f), 0.25f * (curY + 0.5f), COLOR_WHITE, scale, .5f, 0.5f);

        }
    }

    public void onSurfaceChanged(float width, float height, float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        this.width = width;
        this.height = height;
    }

    public void spawn(int state, int x, int y) {
        this.state = state;
        destX = x;
        destY = y;
        curX = x;
        curY = y;
    }

    public void move(int x, int y) {
        destX = x;
        destY = y;
    }



}
