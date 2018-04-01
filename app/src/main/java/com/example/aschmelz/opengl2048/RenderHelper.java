package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */



import static com.example.aschmelz.opengl2048.GLRenderer.tm;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_WHITE;

/**
 * Created by aschmelz on 15/02/2018.
 */

public class RenderHelper {
    protected GLRenderer renderer;
    protected float width = 1, height = 1, offsetX = 0, offsetY = 0;

    protected void drawText(String text, float left, float top, float[] color, float scale, float alignX, float alignY) {
        drawText(text, left, top, color, scale, alignX, alignY, false);
    }

    protected void drawText(String text, float left, float top, float[] color, float scale, float alignX, float alignY, boolean special) {
        float txt_height = tm.getHeight(scale);
        float txt_width = tm.getWidth(text, scale, special);

        renderer.drawText(text, offsetX + left * width - txt_width * alignX, offsetY + top * height - txt_height * alignY, color, scale, special);
    }

    protected void drawText(String text, float left, float top, float[] color, float scale, boolean special) {
        renderer.drawText(text, offsetX + left * width, offsetY + top * height, COLOR_WHITE, scale, special);
    }
    private void fitImageX(int idx, float left, float top, float right, float margin) {
        margin = margin * (right - left);
        fitImageX(idx, left + margin, top + margin, right - margin);
    }
    /*private void fitImageX(int idx, float left, float top, float right, float marginX, float marginY) {
        fitImageX(idx, left + marginX, top + marginY, right - marginX);
    }*/
    protected void fitImageX(int idx, float left, float top, float right) {
        renderer.fitImageX(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width);
    }

    protected void coverImage(int idx, float left, float top, float right, float bottom, float margin, float progress) {
        renderer.coverImage(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width, offsetY + bottom * height, progress, COLOR_WHITE);
    }
    protected void coverImage(int idx, float left, float top, float right, float bottom, float margin, float progress, float[] color) {
        renderer.coverImage(idx, offsetX + (left + margin) * width , offsetY + (top + margin) * height, offsetX + (right - margin) * width, offsetY + (bottom - margin) * height, progress, color);
    }
    protected void coverImage(int idx, float left, float top, float right, float bottom) {
        renderer.coverImage(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width, offsetY + bottom * height);
    }
    protected void coverImage(int idx, ClickRegion cr, float[] color) {
        renderer.coverImage(idx, offsetX + cr.left * width , offsetY + cr.top * height, offsetX + cr.right * width, offsetY + cr.bottom * height, 1, color);
    }
    protected void coverImage(int idx, float left, float top, float right, float bottom, float[] color) {
        renderer.coverImage(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width, offsetY + bottom * height, 1, color);
    }
    protected void containImage(int idx, ClickRegion cr) {
        containImage(idx, cr.left, cr.top, cr.right, cr.bottom, cr.margin);
    }
    protected void containImage(int idx, float left, float top, float right, float bottom, float margin, float progress) {
        containImage(idx, left + margin, top + margin, right - margin, bottom - margin, progress);
    }
    protected void containImage(int idx, float left, float top, float right, float bottom, float margin) {
        containImage(idx, left + margin, top + margin, right - margin, bottom - margin);
    }
    protected void containImage(int idx, float left, float top, float right, float bottom) {
        renderer.containImage(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width, offsetY + bottom * height);
    }
    protected void containImage(int idx, float left, float top, float right, float bottom, float[] color) {
        renderer.containImage(idx, offsetX + left * width , offsetY + top * height, offsetX + right * width, offsetY + bottom * height,  1, color);
    }

    protected void coverImage(int idx, RenderConfig rc) {
        renderer.coverImage(idx,
                offsetX + rc.left * width , offsetY + rc.top * height, offsetX + rc.right * width, offsetY + rc.bottom * height,
                rc.progress, rc.color, rc.rotation);
    }

    public boolean contain(float x, float y, ClickRegion cr) {
        return (x > cr.left + cr.margin && x < cr.right - cr.margin &&
                y > cr.top + cr.margin && y < cr.bottom - cr.margin);
    }

}
