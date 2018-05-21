package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.util.Log;


import static com.example.aschmelz.opengl2048.ImgConsts.*;
import static com.example.aschmelz.opengl2048.FontConstants.*;

public class TextManager extends RenderHelper {

    private static final float
            RI_TEXT_MARGIN = -0.12f,
            RI_TEXT_SPACESIZE = .3f;

    public TextManager(GLRenderer renderer) {
        this.renderer = renderer;
    }

    public void onSurfaceChanged(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static final float[] COLOR_RED = {1f, 0, 0, 1f},
            COLOR_WHITE = {1,1,1,1},
            COLOR_BLACK = {0, 0, 0, 1};


    public float getHeight(float scale) {
        return scale;
    }

    public float getWidth(String text, float scale) {
        float width = 0;
        int offset = -33;
        for(int j = 0; j < text.length(); j++) {
            char c = text.charAt(j);
            int c_val = (int)c;
            c_val += offset;
            if (c == ' ') {
                // space
                width += (RI_TEXT_SPACESIZE + RI_TEXT_MARGIN) * scale;

            } else if (c_val < 0 || c_val > FONT_CHAR.length - 1) {
                Log.e("NO font", "error no img eith idx" + c_val + " text" + text);
                continue;
            } else {
                int img_idx = FONT_CHAR[c_val];

                float font_width = scale * dimensions[img_idx][2] / dimensions[img_idx][3];
                width += font_width + RI_TEXT_MARGIN * scale;// TODO: use uniform scale
            }
        }
        return width;
    }


    public void addText(String text, float x, float y, float[] color, float scale) {
        float dx = x + scale * RI_TEXT_MARGIN / 2, // start at half margin (perfect centered)
                dy = y,
                fw,
                fh = scale;
        int offset = -33;

        // Create
        for (int j = 0; j < text.length(); j++) {
            char c = text.charAt(j);
            int c_val = (int) c;
            c_val += offset;
            if (c == ' ') {
                // space
                fw = RI_TEXT_SPACESIZE * scale;

            } else if (c_val < 0 || c_val > FONT_CHAR.length - 1) {
                Log.e("NO font", "error no img eith idx" + c_val + " text" + text);
                continue;
            } else {
                int img_idx = FONT_CHAR[c_val];
                fw = scale * dimensions[img_idx][2] / dimensions[img_idx][3];   // keep aspect ration

                renderer.drawImagePx(img_idx, dx, dy, dx + fw, dy + fh, color);
            }
            dx += fw + RI_TEXT_MARGIN * scale;
        }
    }
}
