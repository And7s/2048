package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */



import static com.example.aschmelz.opengl2048.GLRenderer.tm;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_H;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_LB;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_LT;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_RB;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_RT;
import static com.example.aschmelz.opengl2048.ImgConsts.SPR_SQUARE_V;
import static com.example.aschmelz.opengl2048.TextManager.COLOR_WHITE;

/**
 * Created by aschmelz on 15/02/2018.
 */

public abstract class RenderHelper {
    protected GLRenderer renderer;
    protected float width = 1, height = 1, offsetX = 0, offsetY = 0;


    protected void drawText(String text, float left, float top, float[] color, float scale, float alignX, float alignY) {
        float txt_height = tm.getHeight(scale);
        float txt_width = tm.getWidth(text, scale);

        renderer.drawText(text, offsetX + left * width - txt_width * alignX, offsetY + top * height - txt_height * alignY, color, scale);
    }

    protected void drawText(String text, float left, float top, float[] color, float scale) {
        renderer.drawText(text, offsetX + left * width, offsetY + top * height, color, scale);
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

    protected void renderRoundCorner(float left, float top, float right, float bottom, float[] color, float radius) {
        float radius_x = radius / width,
                radius_y = radius / height;
        coverImage(SPR_SQUARE_H, left + radius_x, top, right - radius_x, bottom, color);
        coverImage(SPR_SQUARE_V, left, top + radius_y, right, bottom - radius_y, color);
        radius_x *= 1.1; // to ensure overlap
        radius_y *= 1.1;
        coverImage(SPR_SQUARE_LT, left, top, left + radius_x, top + radius_y, color);
        coverImage(SPR_SQUARE_LB, left, bottom - radius_y, left + radius_x, bottom, color);
        coverImage(SPR_SQUARE_RT, right - radius_x, top, right, top + radius_y, color);
        coverImage(SPR_SQUARE_RB, right - radius_x, bottom - radius_y, right, bottom, color);
    }

}
