package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.example.aschmelz.opengl2048.ImgConsts.*;
import static com.example.aschmelz.opengl2048.TextManager.*;

/**
 * Created by aschmelz on 15/02/2018.
 */

public class Overlay extends RenderHelper {
    private ClickRegion CR_amount_btn = new ClickRegion(0.7f, 0.15f, 0.9f, 0.5f, 0);



    public Overlay(GLRenderer renderer) {
        this.renderer = renderer;

        addNewTile();
    }

    public void update(double dt) {
        this.render(dt);
        for(Tile tile : tiles) {
            tile.update(dt);
        }
    }

    private List<Tile> tiles = new ArrayList<>();

    private int[][] state = new int[4][4];

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


    public void onSurfaceChanged(float width, float height) {
        offsetX = 0;
        offsetY = 0;

        if (width > height) {   // landscape
            this.width = height;
            this.height = height;
            offsetX = (width - this.width) / 2;
        } else {    // portrait
            this.width = width;
            this.height = width;
            offsetY = (height - this.height) / 2;
        }

        for (Tile tile : tiles) {
            tile.onSurfaceChanged(this.width, this.height, offsetX, offsetY);
        }
    }

    private float mLastTouchX, mLastTouchY, mTouchDistanceTravel = 0;

    int mTouchState = 0; // 0 = waiting, 1 = in progress, 2 = executed
    private static int TOUCH_WAITING = 0, TOUCH_PROGRESS = 1, TOUCH_EXECUTED = 2;
    float scrollX, scrollY = 0;
    private int mActivePointerId;
    float moveTreshhold = 0.1f;
    public void processTouchEvent(MotionEvent event) {
        float x = (event.getX() - offsetX) / width, // relative coords (0-1 if inside)
                y = (event.getY() - offsetY) / height;

        if (x > 0 && x <= 1 && y > 0 && y <= 1) {
            Log.d("overlay", " idx " + x + ", " + y);
        }
        // TODO support multi touch (here index has to always be 0)
        // mActivePointerId = event.getPointerId(0);
        // https://developer.android.com/training/gestures/multi.html


        int action = event.getAction();
        if (action == ACTION_DOWN && mTouchState == TOUCH_WAITING) {

            mTouchState = TOUCH_PROGRESS;
            mTouchDistanceTravel = 0;
            mLastTouchX = x;
            mLastTouchY = y;
            //(int idx, float left, float top, float right, float bottom, float progress, float[] color)
        }
        if (action == ACTION_MOVE && mTouchState == TOUCH_PROGRESS) {
           scrollX = x - mLastTouchX;
           scrollY = y - mLastTouchY;
Log.d("MOVE", scrollX +   " " + scrollY);
            if (Math.abs(scrollX) > moveTreshhold || Math.abs(scrollY) > moveTreshhold) {
                handleMove(scrollX, scrollY);
                mTouchState = TOUCH_EXECUTED;
            }
        }

        if (action == ACTION_UP) {
            mTouchState = TOUCH_WAITING;
            Log.d("ACTION_UP", "travel dis" );
            //mTouchActive = false;
        }

    }

    private void handleMove(float deltaX, float deltaY) {
        if (deltaX > moveTreshhold) {
            move(1, 0);
        } else if (deltaX < -moveTreshhold) {
            move(-1, 0);
        } else if (deltaY > moveTreshhold) {
            move(0, 1);
        } else if (deltaY < -moveTreshhold) {
            move(0, -1);
        }
    }

    int[] emptyList = new int[16];
    int nEmpty = 0;

    private void move(int x, int y) {
        Log.d("move", "direction " + x + " " + y);
        boolean moved = false;

        int sX = 0, sY = 0, dX = 0, dY = 0, prevX = 0, prevY = 0;
        // left
        for (int i = 0; i < 4; i++) {
            int idx = 0;
            int lastState = 0;
            for (int j = 0; j< 4; j++) {
                if (x == -1) {
                    sX = j;
                    sY = i;
                    dX = idx;
                    dY = i;
                    prevX = idx - 1;
                    prevY = i;
                }

                if (x == 1) {
                    sX = 3 - j;
                    sY = i;
                    dX = 3 - idx;
                    dY = i;
                    prevX = 3 - (idx - 1);
                    prevY = i;
                }

                if (y == -1) {
                    sX = i;
                    sY = j;
                    dX = i;
                    dY = idx;
                    prevX = i;
                    prevY = idx - 1;
                }

                if (y == 1) {
                    sX = i;
                    sY = 3 - j;
                    dX = i;
                    dY = 3 - idx;
                    prevX = i;
                    prevY = 3 - (idx - 1);
                }

                int curState = state[sY][sX];
                state[sY][sX] = 0;    // reset
                if (curState != 0) {
                    if (lastState == curState) {    // merge two
                        state[prevY][prevX]++;
                        moved = true;
                        lastState = 0; // merge only two
                        mergeTile(sX, sY, prevX, prevY);
                    } else {
                        if (dX != sX || dY != sY) moved = true;
                        state[dY][dX] = curState;
                        moveTile(sX, sY, dX, dY);
                        idx++;
                        lastState = curState;
                    }
                }
            }
        }

        if (moved) {
            addNewTile();
        }
        calculateScore();
    }

    private int score = 0;
    private void calculateScore() {
        score = 0;
        for (int i = 0; i < 16; i++) {
            int x = i % 4;
            int y = i / 4;

            if (state[y][x] != 0) {
                score += Math.pow(2, state[y][x]);
            }
        }
    }

    private void moveTile(int sX, int sY, int dX, int dY) {
        for(Tile tile : tiles) {
            if (tile.destX == sX && tile.destY == sY) {
                tile.move(dX, dY);
                return;
            }
        }
        Log.e("could not find tile", "at " + sX + " " + sY);
    }

    private void mergeTile(int sX, int sY, int dX, int dY) {

        Iterator<Tile> i = tiles.iterator();
        while (i.hasNext()) {
            Tile tile = i.next();

            if (tile.destX == sX && tile.destY == sY) {
                tile.move(dX, dY);
                i.remove(); // remove the current tile

            } else if (tile.destX == dX && tile.destY == dY) {
                //tile.move(dX, dY);
                tile.state++;

            }
        }
        Log.e("could not find tile", "at " + sX + " " + sY);
    }

    private void addNewTile() {
        nEmpty = 0;
        for (int i = 0; i < 16; i++) {
            int x = i % 4;
            int y = i / 4;
            if (state[y][x] == 0) {
                emptyList[nEmpty] = i;
                nEmpty++;
            }
        }
        int position = (int) (Math.random() * nEmpty);
        int x = emptyList[position] % 4;
        int y = emptyList[position] / 4;
        int newSpawn = Math.random() < .9 ? 1 : 2;

        Tile tile = new Tile(renderer, width, height, offsetX, offsetY);
        tile.spawn(newSpawn, x, y);
        tiles.add(tile);
        state[y][x] = newSpawn;
    }


    private void render(double dt) {
        coverImage(SPR_SOLID_WHITE, 0, 0.0f, 1, 1, COLOR_GREY_TRANSPARENT);


        drawText("hello world", 0.5f, 0f, COLOR_WHITE, 60, 0.5f, 0f);


        //coverImage(SPR_UNBOUGHT, 0.7f, 0.15f, 0.9f, 0.5f);

        renderGrid();

        drawText("123123", 0.8f, 0.3f, COLOR_WHITE, 60, 0.5f, 0.5f, true);



        drawText("right aligned", 1, 0, COLOR_RED, 30, 1, 0);
        drawText("left aligned", 0, 0, COLOR_RED, 30, 0, 0);

        drawText("right aligned", 1, 1, COLOR_RED, 30, 1, 1);
        drawText("left aligned", 0, 1, COLOR_RED, 30, 0, 1);

    }


    private void renderGrid() {
        drawText("score " + score, 0.1f, 0.1f, COLOR_WHITE, 50, false);
        float[] color = COLOR_RED;
        int scale = 30;


        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int curState = state[y][x];

                if (curState >= 1 && curState <= bgColor.length) {
                    color = bgColor[curState - 1];
                    scale = fontSizes[curState - 1];
                    coverImage(SPR_SOLID_WHITE, 0.25f * x, 0.25f * y, 0.25f * (x + 0.5f), 0.25f * (y + 0.5f), 0.01f, 1, color);
                    drawText(  (int) Math.pow(2, curState) + "", 0.25f * (x + 0.5f), 0.25f * (y + 0.5f), COLOR_WHITE, scale, .5f, 0.5f);

                }
            }
        }
    }
}
