package com.example.aschmelz.opengl2048;

import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE;

/**
 * Created by aschmelz on 31/03/2018.
 */

public class Tile extends RenderHelper {
    int destX, destY;
    float curX, curY, curScale, destScale,
    startX, startY, startScale;
    public boolean isDead = false;

    float animDuration = .5f, animProgress = 0;

    public int state = 0;
    private int curAnim = 0;
    private static int ANIM_IN = 0, ANIM_MERGE = 1, ANIM_MOVE = 2;

    public Tile(GLRenderer renderer) {
        this.renderer = renderer;

    }

    public boolean update(double dt) {
        animProgress += dt;
        if (isDead && animProgress > animDuration) {
            return false;
        }
        this.render(dt);
        return true;     // still alive
    }


    private float[][] bgColor = {
            {.929f, .890f, .852f, 1},   // 2
            {.926f, .875f, .781f, 1},   // 4
            {.945f, .691f, .473f, 1},   // 8
            {.957f, .582f, .387f, 1},   // 16
            {.861f, .484f, .371f, 1},   // 32
            {.960f, .367f, .230f, 1},   // 64
            {.926f, .809f, .445f, 1},   // 128
            {237 / 256f, 204 / 256f, 97 / 256f, 1}, // 256
            {237 / 256f, 200 / 256f, 80 / 256f, 1}, // 512
            {237 / 256f, 197 / 256f, 63 / 256f, 1}, // 1024
            {237 / 256f, 194 / 256f, 46 / 256f, 1}, // 2048
            {60 / 256f, 58 / 256f, 50 / 256f, 1}, // 4096 and up
    };

    private int[] fontSizes = {
            120, // 2
            120, // 4
            120, // 8
            100, // 16
            100, // 32
            100, // 64
            90, // 128
            90, // 256
    };

    private float[][] textColors = {
            {119 / 256f, 110 / 256f, 101 / 256f, 1},    // 2
            {119 / 256f, 110 / 256f, 101 / 256f, 1},    // 4
            {249 / 256f, 246 / 256f, 242 / 256f, 1}     // 8 and up
    };

    private void render(double dt) {
        int scale = 30;
        float[] color;
        float[] textColor;
        // animate

        float progress = animProgress / animDuration;
        if (progress > 1) progress = 1; // linear progress


        float animProgress;
        if (curAnim == ANIM_IN) {
            // delay by 2/3
            if (progress < .5) {
                animProgress = 0;
            } else {
                progress = (progress - .5f) * 2;
                animProgress = easeOutCubic(progress); // eased progress
            }
        } else if (curAnim == ANIM_MERGE) {
            if (progress < 0.333) {
                animProgress = 0;
            } else {
                progress = (progress - 0.333f) / 0.666f;
                animProgress = easeOutBack(progress);
            }

        } else { // ANIM_MOVE
            if (progress < 0.333) {
                animProgress = easeInOutQuad(progress * 3); // eased progress
            } else {
                animProgress = 1;
            }

        }
        curX = (1 - animProgress) * startX + animProgress * destX;
        curY = (1 - animProgress) * startY + animProgress * destY;
        curScale = (1 - animProgress) * startScale + animProgress * destScale;

        if (state >= 1) {
            color = bgColor[Math.min(state, bgColor.length) - 1];
            scale = fontSizes[Math.min(fontSizes.length, state) - 1];
            textColor = textColors[Math.min(textColors.length, state) - 1];
            RenderConfig rc = new RenderConfig(0.25f * curX, 0.25f * curY, 0.25f * (curX + 1), 0.25f * (curY + 1));
            rc.setScale(curScale * 0.9f);
            rc.setColor(color);
            coverImage(SPR_SQUARE, rc);
            float baseLine = 0.9f;
            drawText(  (int) Math.pow(2, state) + "", 0.25f * (curX + 0.5f), 0.25f * (curY + 0.5f), textColor, scale * curScale, .5f, 0.5f * baseLine);
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
        startX = x;
        startY = y;
        startScale = 0;
        destScale = 1;
        animProgress = 0;
        curAnim = ANIM_IN;
    }

    public void mergeSpawn(int state, int dX, int dY) {
        this.state = state;
        destX = dX;
        destY = dY;
        startX = dX;
        startY = dY;
        startScale = 0;
        destScale = 1;
        animProgress = 0;
        curAnim = ANIM_MERGE;
    }

    public void die() {
        startX = curX;
        startY = curY;
        startScale = curScale;  // so the animation wont run again
        isDead = true;
        animProgress = 0;
        curAnim = ANIM_MOVE;
    }

    public void move(int x, int y) {
        if (x == destX && y == destY) return;   // dont interrupt current running animation if destination is the same
        startX = curX;
        startY = curY;
        startScale = curScale;
        destX = x;
        destY = y;
        animProgress = 0;
        curAnim = ANIM_MOVE;
    }

    // helper functions for timing
    // t: current time, b: begInnIng value, c: change In value, d: duration
    private float easeInCubic (float x) {
        return x * x * x;
    }

    private float easeOutCubic (float x) {
        x -= 1;
        return x * x * x + 1;
    }

    private float easeOutBack(float x) {
        x -= 1;
        float s = 1.7f;
        return x * x * ((s + 1) * x + s) + 1;
    }

    private float easeInOutQuad(float x) {
        x *= 2;
        if (x < 1) return .5f * x * x;
        return -.5f * ((--x)*(x-2) - 1);
    }

}
