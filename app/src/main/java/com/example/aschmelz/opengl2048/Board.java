package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.example.aschmelz.opengl2048.ImgConsts.*;
import static com.example.aschmelz.opengl2048.MainActivity.dataStorage;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_RED;

/**
 * Created by aschmelz on 15/02/2018.
 */

public class Board extends RenderHelper {
    private ClickRegion CR_amount_btn = new ClickRegion(0.7f, 0.15f, 0.9f, 0.5f, 0);


    float INNER_MARGIN = 0.97f;
    private final List<Tile> tiles = Collections.synchronizedList(new ArrayList<Tile>());
    private int[][] state = new int[4][4];
    private int score = 0;
    private Overlay overlay;

    public Board(GLRenderer renderer, Overlay overlay) {
        this.renderer = renderer;
        this.overlay = overlay;
        load();
        if (tiles.size() == 0) {
            addNewTile();
        }
        calculateScore();

        overlay.updateScore(0, score);
    }

    public void newGame() {
        synchronized (tiles) {
            tiles.clear();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = 0;
            }
        }
        addNewTile();
        calculateScore();

        overlay.updateScore(0, score);

    }

    public void load() {
        SharedPreferences sharedPref = dataStorage.getSharedPref();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                state[y][x] = sharedPref.getInt(y + "-" + x, 0);
                if (state[y][x] != 0) {

                    Tile tile = new Tile(renderer);
                    tile.spawn(state[y][x], x, y);
                    tiles.add(tile);
                }
            }
        }
        onSurfaceChangedUpdateTiles();
    }

    public void save() {
        SharedPreferences sharedPref = dataStorage.getSharedPref();
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                editor.putInt(y + "-" + x, state[y][x]);
            }
        }
        editor.apply();
    }


    public void update(double dt) {
        this.render(dt);
        synchronized (tiles) {
            Iterator<Tile> i = tiles.iterator();
            while (i.hasNext()) {
                Tile tile = i.next();
                if (!tile.update(dt)) {
                    Log.d("iterator", "remove a tile");
                    i.remove();
                }
            }
        }
    }

    public void onSurfaceChanged(float width, float height) {
        float factor = 0.9f;
        offsetX = 0;
        offsetY = 0;

        if (width > height) {   // landscape
            this.width = height * factor;
            this.height = height * factor;
        } else {    // portrait
            this.width = width * factor;
            this.height = width * factor;
        }
        offsetX = (width - this.width) / 2;
        offsetY = (height - this.height) * 2 / 3;

        onSurfaceChangedUpdateTiles();

    }

    private void onSurfaceChangedUpdateTiles() {
        for (Tile tile : tiles) {
            tile.onSurfaceChanged(width * INNER_MARGIN, height * INNER_MARGIN,
                    offsetX + 0.5f * width * (1 - INNER_MARGIN), offsetY + 0.5f * height * (1 - INNER_MARGIN));
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

        // new move => delete all dead tiles
        synchronized (tiles) {
            Iterator<Tile> it = tiles.iterator();
            while (it.hasNext()) {
                Tile tile = it.next();
                if (tile.isDead) {
                    Log.d("remove", "tile in move");
                    it.remove();
                }
            }
        }


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
        save();
    }


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
            if (!tile.isDead && tile.destX == sX && tile.destY == sY) {
                tile.move(dX, dY);
                return;
            }
        }
        Log.e("could not find tile", "at " + sX + " " + sY);
    }

    private void mergeTile(int sX, int sY, int dX, int dY) {
        int state = 0;
        for (Tile tile: tiles) {
            if (!tile.isDead && tile.destX == sX && tile.destY == sY) { // outer tile (merge from)
                tile.move(dX, dY);
                tile.die();
            } else if (!tile.isDead && tile.destX == dX && tile.destY == dY) {  // inner tile (merge to)
                state = tile.state;
                tile.die();
            }
        }

        Tile newTile = new Tile(renderer);
        newTile.mergeSpawn(state + 1, dX, dY);
        tiles.add(newTile);
        onSurfaceChangedUpdateTiles();
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

        Tile tile = new Tile(renderer);
        tile.spawn(newSpawn, x, y);
        synchronized (tiles) {
            tiles.add(tile);
        }
        state[y][x] = newSpawn;
        onSurfaceChangedUpdateTiles();

        calculateScore();
        overlay.updateScore(newSpawn, score);
    }



    private void render(double dt) {

        renderRoundCorner(0,0,1,1, COLOR_BOARD, 50f);

        renderGrid();

        /*
        drawText("right aligned", 1, 0, COLOR_RED, 30, 1, 0);
        drawText("left aligned", 0, 0, COLOR_RED, 30, 0, 0);

        drawText("right aligned", 1, 1, COLOR_RED, 30, 1, 1);
        drawText("left aligned", 0, 1, COLOR_RED, 30, 0, 1);
        */

    }


    private void renderGrid() {

        float offset = (1 - INNER_MARGIN) / 2;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                RenderConfig rc = new RenderConfig(
                        0.25f * x * INNER_MARGIN + offset, 0.25f * y * INNER_MARGIN + offset,
                        0.25f * (x + 1) * INNER_MARGIN + offset, 0.25f * (y + 1) * INNER_MARGIN + offset
                );
                rc.setScale(0.9f);
                rc.setColor(COLOR_TILE_BG);
                coverImage(SPR_SQUARE, rc);
            }
        }
    }
}
