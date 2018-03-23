package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.util.Log;

import android.util.Log;

import static com.example.aschmelz.opengl2048.ImgConsts.*;
import static com.example.aschmelz.opengl2048.FontConstants.*;

public class TextManager extends RenderHelper {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    private static final float RI_TEXT_LEFTTER_SPACING = 1.0f;


    private static final float
            RI_TEXT_MARGIN = -4f,
            RI_TEXT_MARGIN_SPECIAL = -14,    // pixels around the image
            RI_TEXT_WIDTH = 22;     // monotext width
    private static final float RI_TEXT_SPACESIZE = 20f;

    private float uniformscale;

    public TextManager(GLRenderer renderer) {
        this.renderer = renderer;
    }

    public void onSurfaceChanged(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static final float[] COLOR_RED = {1f, 0, 0, 1f},
            COLOR_WHITE = {1,1,1,1},
            COLOR_GREY = {.2f, .2f, .2f, 1},
            COLOR_BLACK = {0, 0, 0, 1},
            COLOR_GREY_TRANSPARENT = {.2f, .2f, .2f, .8f},
            COLOR_ORANGE = {.875f, .531f, .285f, 1f},
            COLOR_GREEN = {.508f, .684f , .273f, 1},
            COLOR_BROWN = {.230f, .211f, 0.188f, 1},
            COLOR_LIGHT_BROWN = {.543f, .504f, .465f, 1};

    public float getHeight(float scale) {
        return 70f * scale * uniformscale;
    }

    public float getWidth(String text, float scale, boolean special) {
        float width = 0;
        int offset = special ? -33 : 61;
        for(int j = 0; j < text.length(); j++) {
            char c = text.charAt(j);
            int c_val = (int)c;
            c_val += offset;
            if(c == ' ') {
                // space
                width +=  RI_TEXT_SPACESIZE;

            } else if (c_val < 0 || c_val > FONT_CHAR.length - 1) {
                Log.e("NO font", "error no img eith idx" + c_val + " text" + text);
                continue;
            } else {
                // is 111 should
                int img_idx = FONT_CHAR[c_val];
                float margin = special ? RI_TEXT_MARGIN_SPECIAL : RI_TEXT_MARGIN;
                float font_width = special ? dimensions[img_idx][2]: RI_TEXT_WIDTH;
                width += font_width + margin;// TODO: use uniform scale
            }
        }
        return width * scale * RI_TEXT_LEFTTER_SPACING * uniformscale;
    }

    public void addText(String text, float x, float y, float[] color, float scale) {
        addText(text, x, y, color, scale, false);
    }
    public void addText(String text, float x, float y, float[] color, float scale, boolean special) {
        float dx = x / width,
                dy = y / height,
                fw,
                fh = 0.05f * scale * uniformscale;
        int offset = special ? -33 : 61;
// -33 is special
        // Create
        for(int j = 0; j < text.length(); j++) {
            char c = text.charAt(j);
            int c_val = (int)c;
            c_val += offset;
            if(c == ' ') {
                // space
                fw = uniformscale * RI_TEXT_SPACESIZE / width * scale;

            } else if (c_val < 0 || c_val > FONT_CHAR.length - 1) {
                Log.e("NO font", "error no img eith idx" + c_val + " text" + text);
                continue;
            } else {
                int img_idx = FONT_CHAR[c_val];
                fw = uniformscale * (dimensions[img_idx][2]) / width * scale;// TODO: use uniform scale
                if (!special && c > 47 && c < 58) { // numbers are monospace
                    float will_fw = RI_TEXT_WIDTH *  uniformscale / width * scale;// monospace font (center character)
                    containImage(img_idx, dx + (will_fw - fw) / 2,dy,dx + (will_fw - fw) / 2 + fw, dy + fh, color);
                } else {
                    containImage(img_idx, dx,dy,dx + fw, dy + fh, color);
                }



            }
            float margin = special ? RI_TEXT_MARGIN_SPECIAL : RI_TEXT_MARGIN;
            if (!special && c > 47 && c < 58) fw = RI_TEXT_WIDTH *  uniformscale / width * scale;// monospace font
            dx += (fw + margin * uniformscale / width * scale) * RI_TEXT_LEFTTER_SPACING;
        }
    }

    public void setUniformscale(float uniformscale) {
        this.uniformscale = uniformscale / 150; // so 32/3.375 (1 scale = 1px height on the reference design
    }
}
