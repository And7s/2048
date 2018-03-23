package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

public class ClickRegion {
    float left, top, right, bottom, margin = 0;
    public ClickRegion(float left, float top, float right, float bottom, float margin) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.margin = margin;
    }
}
