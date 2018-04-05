package com.example.aschmelz.opengl2048;

import android.view.MotionEvent;

public class Overlay extends RenderHelper {

    private Board board;

    public Overlay(GLRenderer renderer) {
        this.renderer = renderer;
        board = new Board(renderer);
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
    }

    public void update(double dt) {
        board.update(dt);
    }
}
