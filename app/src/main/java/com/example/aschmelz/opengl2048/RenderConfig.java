package com.example.aschmelz.opengl2048;

import static com.example.aschmelz.opengl2048.TextManager.COLOR_WHITE;

/**
 * Created by aschmelz on 23/03/2018.
 */


public class RenderConfig {
    public float[] color = COLOR_WHITE;
    public float progress = 1;
    public float rotation = 0;
    public float left, top, right, bottom;


    RenderConfig(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
    RenderConfig(float left, float top, float right, float bottom, float margin) {
        this.left = left + margin;
        this.top = top + margin;
        this.right = right - margin;
        this.bottom = bottom - margin;
    }

    public RenderConfig setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public RenderConfig setScale(float scale) {
        float midX = (left + right) / 2,
                midY = (top + bottom) / 2,
                halfWidth = (right - left) / 2,
                halfHeight = (bottom - top) / 2;
        left = midX - halfWidth * scale;
        right = midX + halfWidth * scale;
        top = midY - halfHeight * scale;
        bottom = midY + halfHeight * scale;
        return this;
    }

    public RenderConfig setColor(float[] color) {
        this.color = color;
        return this;
    }


}
