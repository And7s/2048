package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static com.example.aschmelz.opengl2048.ImgConsts.*;

public class GLRenderer implements GLSurfaceView.Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    private static float vertices[], colors[], uvs[];
    public static short indices[];

    public FloatBuffer vertexBuffer, uvBuffer, colorBuffer;
    public ShortBuffer drawListBuffer;
    public static int debugIdx = 0;

    private ByteBuffer bbVertices, bbIndices, bbUvs, bbColors;

    public static float zPos = 0;
    private int nDrawCalls = 0, maxDrawCalls = 0;


    float   ssu = 1.0f;
    float   ssx = 1.0f;
    float   ssy = 1.0f;
    float   swp = 320.0f;
    float   shp = 480.0f;

    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 768;

    // Misc
    Context mContext;
    long mLastTime;

    public static TextManager tm;

    private Overlay overlay;

    public GLRenderer(Context c) {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;

        overlay = new Overlay(this);
        reallocBuffers(1);
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    int curFPS = 0, FPS = 0;
    long elapsed = 0, frameTime, longestFrame = 0;

    @Override
    public void onDrawFrame(GL10 unused) {

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;
        frameTime = elapsed;
        longestFrame = Math.max(longestFrame, frameTime);
        try {
            if (elapsed < 66)
                Thread.sleep(66 - elapsed);
        } catch(InterruptedException e) {

        }
        now = System.currentTimeMillis();
        elapsed = now - mLastTime;
        UpdateSprite(elapsed);
/*
        drawText("elapsed " + elapsed + longestFrame, 10, 20);
        drawText( "frame took: " + frameTime , 10, 80);
        drawText("longestFrame: " + longestFrame, 10, 140);
        drawText("FPS: " + FPS, 10, 200);
*/
        this.elapsed += elapsed;
        curFPS++;
        if (this.elapsed >= 1000) {
            Log.d("FPS", "cur" + curFPS);
            FPS = curFPS;
            curFPS = 0;
            this.elapsed = 0;
            longestFrame *= 0.5;   // reset every second
        }
        // Update our example


        // Render our example
        Render(mtrxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;
    }

    public void drawText(String text, float x, float y) {
        tm.addText(text, x, y, COLOR_BLACK, 60);
    }
    public void drawText(String text, float x, float y, float[] color, float scale) {

        tm.addText(text, x, y, color, scale);
    }

    // fits width, scaleY proportional
    public void fitImageX(int idx, float left, float top, float right) {
        float width =  ssu * dimensions[idx][2],
                height =  ssu * dimensions[idx][3];
        float scaleX = (right - left) / width;

        drawImage(idx, left, top, scaleX, scaleX);
    }


    // will scale to fit entire space provided
    public void coverImage(int idx, float left, float top, float right, float bottom) {
        float width =  ssu * dimensions[idx][2],
                height =  ssu * dimensions[idx][3];
        float scaleX = (right - left) / width,
                scaleY = (bottom - top) / height;
        // Log.d("coverImage", "img: " + idx + " scaleX" + scaleX + " scaleY: " + scaleY + " ratio " + (scaleX / scaleY));
        drawImage(idx, left, top, scaleX, scaleY);
    }

    public void coverImage(int idx, float left, float top, float right, float bottom, float progress, float[] color) {
        float width =  ssu * dimensions[idx][2],
                height =  ssu * dimensions[idx][3];
        float scaleX = (right - left) / width,
                scaleY = (bottom - top) / height;
        // Log.d("coverImage", "img: " + idx + " scaleX" + scaleX + " scaleY: " + scaleY + " ratio " + (scaleX / scaleY));
        drawImage(idx, left, top, scaleX, scaleY, debugIdx, progress, color, 0);
    }

    public void coverImage(int idx, float left, float top, float right, float bottom, float progress, float[] color, float rotation) {
        float width =  ssu * dimensions[idx][2],
                height =  ssu * dimensions[idx][3];
        float scaleX = (right - left) / width,
                scaleY = (bottom - top) / height;
        // Log.d("coverImage", "img: " + idx + " scaleX" + scaleX + " scaleY: " + scaleY + " ratio " + (scaleX / scaleY));
        drawImage(idx, left, top, scaleX, scaleY, debugIdx, progress, color, rotation);
    }

    public void containImage(int idx, float left, float top, float right, float bottom) {
        containImage(idx, left, top, right, bottom, 1, COLOR_WHITE);
    }
    public void containImage(int idx, float left, float top, float right, float bottom, float progress, float [] color) {
        float width =  ssu * dimensions[idx][2],
                height =  ssu * dimensions[idx][3];
        float scaleX = (right - left) / width,
                scaleY = (bottom - top) / height;
        if (debugIdx != 0)
            drawImage(idx, left, top, scaleX, scaleY, SPR_SQUARE, debugIdx);  // show the containing frame

        if (scaleX > scaleY) {
            float offsetX =  (right - left - width * scaleY) / 2;    // center
            drawImage(idx, left + offsetX, top, scaleY, scaleY,0, progress, color, 0);
        } else {
            float offsetY =  (bottom - top - height * scaleX) / 2;
            drawImage(idx, left, top + offsetY, scaleX, scaleX,0, progress, color, 0);
        }
    }
    public void drawImage(int idx, float offset_x, float offset_y, float scaleX, float scaleY) {
        drawImage(idx, offset_x, offset_y, scaleX, scaleY, debugIdx,1);
    }

    public void drawImage(int idx, float offset_x, float offset_y, float scaleX, float scaleY, int debugIdx, float progress) {
        drawImage(idx, offset_x, offset_y, scaleX, scaleY, debugIdx, progress, COLOR_WHITE, 0);
    }

    // draws an image into the rect spanned of x1,y1, x2, y2 (=coverImage) all values in pixels
    public void drawImagePx(int idx, float x1, float y1, float x2, float y2, float[] color) {
        reallocBuffers(nDrawCalls + 1);

        int i = nDrawCalls;

        // Create the 2D parts of our 3D vertices, others are default 0.0f
        vertices[(i*12) + 0] = x1;
        vertices[(i*12) + 1] = y2;
        vertices[(i*12) + 2] = zPos;
        vertices[(i*12) + 3] = x1;
        vertices[(i*12) + 4] = y1;
        vertices[(i*12) + 5] = zPos;
        vertices[(i*12) + 6] = x2;
        vertices[(i*12) + 7] = y1;
        vertices[(i*12) + 8] = zPos;
        vertices[(i*12) + 9] = x2;
        vertices[(i*12) + 10] = y2;
        vertices[(i*12) + 11] = zPos;

        zPos += 0.001;

        int last = nDrawCalls * 4;
        indices[(i*6) + 0] = (short) (last + 0);
        indices[(i*6) + 1] = (short) (last + 1);
        indices[(i*6) + 2] = (short) (last + 2);
        indices[(i*6) + 3] = (short) (last + 0);
        indices[(i*6) + 4] = (short) (last + 2);
        indices[(i*6) + 5] = (short) (last + 3);

        if (debugIdx != 0) {
            idx = debugIdx;
        }
        float u1 = dimensions[idx][0] / 1024f,
                v1 = dimensions[idx][1] / 1024f,
                u2 = (dimensions[idx][0] + dimensions[idx][2]) / 1024f,
                v2 = (dimensions[idx][1] + dimensions[idx][3]) / 1024f;

        // Adding the UV's using the offsets
        uvs[(i*8) + 0] = u1;
        uvs[(i*8) + 1] = v2;
        uvs[(i*8) + 2] = u1;
        uvs[(i*8) + 3] = v1;
        uvs[(i*8) + 4] = u2;
        uvs[(i*8) + 5] = v1;
        uvs[(i*8) + 6] = u2;
        uvs[(i*8) + 7] = v2;

        int cIdx = i * 16;
        for (int j = 0; j < 4; j++) {
            colors[cIdx++] = color[0];
            colors[cIdx++] = color[1];
            colors[cIdx++] = color[2];
            colors[cIdx++] = color[3];

        }

        nDrawCalls++;
    }

    // draws an image with scaleX, scaleY diemensions starting from pixel coordinates scaleX, scaleY
    public void drawImage(int idx, float offset_x, float offset_y, float scaleX, float scaleY, int debugIdx, float progress, float[] color, float rotation) {
        reallocBuffers(nDrawCalls + 1);

        float width = scaleX * ssu * (dimensions[idx][2]),
                height = scaleY * ssu * (dimensions[idx][3]);

        int i = nDrawCalls;

        width *= progress;


        if (rotation != 0) {
            float rotate = (float)(rotation / 180 * Math.PI);   // convert from degree to radians
            float halfWidth = width / 2f,
                    halfHeight = height / 2f,
                    midX = offset_x + halfWidth,
                    midY = offset_y + halfHeight;
            float sin = (float) Math.sin(rotate),
                    cos = (float) Math.cos(rotate);
            // Create the 2D parts of our 3D vertices, others are default 0.0f

            float x0 =  halfWidth,
                    y0 =  halfHeight;

            vertices[(i*12) + 0] = midX - cos * x0 - sin * y0;
            vertices[(i*12) + 1] = midY - sin * x0 + cos * y0;
            vertices[(i*12) + 2] = zPos;
            vertices[(i*12) + 3] = midX - cos * x0 + sin * y0;
            vertices[(i*12) + 4] = midY - sin * x0 - cos * y0;
            vertices[(i*12) + 5] = zPos;
            vertices[(i*12) + 6] = midX + cos * x0 + sin * y0;
            vertices[(i*12) + 7] = midY + sin * x0 - cos * y0;
            vertices[(i*12) + 8] = zPos;
            vertices[(i*12) + 9] = midX + cos * x0 - sin * y0;
            vertices[(i*12) + 10] = midY + sin * x0 + cos * y0;
            vertices[(i*12) + 11] = zPos;

        } else {
            // Create the 2D parts of our 3D vertices, others are default 0.0f
            vertices[(i*12) + 0] = offset_x;
            vertices[(i*12) + 1] = offset_y + height;
            vertices[(i*12) + 2] = zPos;
            vertices[(i*12) + 3] = offset_x;
            vertices[(i*12) + 4] = offset_y;
            vertices[(i*12) + 5] = zPos;
            vertices[(i*12) + 6] = offset_x + width;
            vertices[(i*12) + 7] = offset_y;
            vertices[(i*12) + 8] = zPos;
            vertices[(i*12) + 9] = offset_x + width;
            vertices[(i*12) + 10] = offset_y + height;
            vertices[(i*12) + 11] = zPos;

        }

        zPos += 0.001;

        int last = nDrawCalls * 4;
        indices[(i*6) + 0] = (short) (last + 0);
        indices[(i*6) + 1] = (short) (last + 1);
        indices[(i*6) + 2] = (short) (last + 2);
        indices[(i*6) + 3] = (short) (last + 0);
        indices[(i*6) + 4] = (short) (last + 2);
        indices[(i*6) + 5] = (short) (last + 3);

        if (debugIdx != 0) {
            idx = debugIdx;
        }
        float u1 = dimensions[idx][0] / 1024f,
                v1 = dimensions[idx][1] / 1024f,
                u2 = (dimensions[idx][0] + dimensions[idx][2] * progress) / 1024f,
                v2 = (dimensions[idx][1] + dimensions[idx][3]) / 1024f;



        // Adding the UV's using the offsets
        uvs[(i*8) + 0] = u1;
        uvs[(i*8) + 1] = v2;
        uvs[(i*8) + 2] = u1;
        uvs[(i*8) + 3] = v1;
        uvs[(i*8) + 4] = u2;
        uvs[(i*8) + 5] = v1;
        uvs[(i*8) + 6] = u2;
        uvs[(i*8) + 7] = v2;


        int cIdx = i * 16;
        for (int j = 0; j < 4; j++) {
            colors[cIdx++] = color[0];
            colors[cIdx++] = color[1];
            colors[cIdx++] = color[2];
            colors[cIdx++] = color[3];

        }


        nDrawCalls++;

    }

    private void Render(float[] m) {

        GLES20.glUseProgram(riGraphicTools.sp_Image);


        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //renderTexture(m);

        GLES20.glBlendFunc(GLES20.GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //GLES20.glBlendFunc(GL_ZERO, GL_ONE_MINUS_SRC_ALPHA);

        //renderMultiply(m, 0);

        renderTexture(m);

    }

    private int mPositionHandle, mColorHandle, mTexCoordLoc, mtrxhandle, mSamplerLoc;

    public void renderTexture(float[] m) {

        GLES20.glUseProgram(riGraphicTools.sp_Image);
        // get handle to vertex shader's vPosition member


        // Enable generic vertex attribute array
        //GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Enable generic vertex attribute array
        // GLES20.glEnableVertexAttribArray(mColorHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);


        // Enable generic vertex attribute array
        // GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, nDrawCalls * 6,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);
        //GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        //GLES20.glDisableVertexAttribArray(mColorHandle);

    }



    public void SetupImage() {

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier("drawable/merged", null, mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        overlay.onSurfaceChanged(width, height);
        tm.onSurfaceChanged(width, height);

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0, mScreenWidth, mScreenHeight, 0, 0, 50);
        //Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);


        // get attribute pointers
        mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");
        mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_Color");
        mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord");
        mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");
        mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image, "s_texture" );
        SetupScaling();


        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glEnableVertexAttribArray(mtrxhandle);
        GLES20.glEnableVertexAttribArray(mSamplerLoc);


        // Enable generic vertex attribute array
        //
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        SetupScaling();

        // Create the image information
        SetupImage();
        // Create our texts
        SetupText();

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Create the shaders, solid color
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                riGraphicTools.fs_SolidColor);

        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);

        // Create the shaders, images
        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_Image);
        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                riGraphicTools.fs_Image);

        riGraphicTools.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);
        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
        GLES20.glLinkProgram(riGraphicTools.sp_Image);



        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GL_ONE_MINUS_SRC_ALPHA);


// Text shader
        int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_Text);
        int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                riGraphicTools.fs_Text);

        riGraphicTools.sp_Text = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
        GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert);
        GLES20.glLinkProgram(riGraphicTools.sp_Text);



    }
    public void SetupText()
    {
        // Create our text manager
        tm = new TextManager(this);

        // Tell our text manager to use index 1 of textures loaded


    }


    public void SetupScaling()
    {
        // The screen resolutions
        swp = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
        shp = (int) (mContext.getResources().getDisplayMetrics().heightPixels);

        // Orientation is assumed portrait
        ssx = swp / 320.0f;
        ssy = shp / 320.0f;
        /* alternatives if scaling should change on orientation
        ssx = swp / 320.0f;
        ssy = shp / 480.0f;
         */

        // Get our uniform scaler
        if(ssx > ssy)
            ssu = ssy;
        else
            ssu = ssx;
    }



    public void processTouchEvent(MotionEvent event) {
        // Get the half of screen value


        overlay.processTouchEvent(event);
    }
    public void UpdateSprite(long elapsed) {

        zPos = 0;
        nDrawCalls = 0;
        double dt = elapsed / 1000f;

        //draw the background
        coverImage(SPR_SOLID_WHITE, 0, 0, mScreenWidth, mScreenHeight, 1, COLOR_BACKGROUND);

        overlay.update(dt);

        // The vertex buffer.
        vertexBuffer = bbVertices.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        drawListBuffer = bbIndices.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        // The texture buffer
        uvBuffer = bbUvs.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);


        // The colors buffer.
        colorBuffer = bbColors.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    private void reallocBuffers(int size) {
        if (size > maxDrawCalls) {
            maxDrawCalls = Math.max(maxDrawCalls * 2, size);
            Log.d("realloc", "to size" + maxDrawCalls);

            vertices = resizeArray(vertices, maxDrawCalls * 12);
            indices = resizeArray(indices, maxDrawCalls * 16);   // was 8
            uvs = resizeArray(uvs, maxDrawCalls * 8);
            colors = resizeArray(colors, maxDrawCalls * 16);

            bbVertices = ByteBuffer.allocateDirect(vertices.length * 4);
            bbVertices.order(ByteOrder.nativeOrder());

            bbIndices = ByteBuffer.allocateDirect(indices.length * 4);
            bbIndices.order(ByteOrder.nativeOrder());

            bbUvs = ByteBuffer.allocateDirect(uvs.length * 4);
            bbUvs.order(ByteOrder.nativeOrder());

            bbColors = ByteBuffer.allocateDirect(colors.length * 4);
            bbColors.order(ByteOrder.nativeOrder());
        }
    }
    private float[] resizeArray(float[] arr, int size) {
        float[] newArr = new float[size];
        // on resize the array is emptied and we construct from the beginning again without copyng everything
        if (arr != null) System.arraycopy(arr, 0, newArr, 0, Math.min(size, arr.length));
        return newArr;
    }
    private short[] resizeArray(short[] arr, int size) {
        short[] newArr = new short[size];
        if (arr != null) System.arraycopy(arr, 0, newArr, 0, Math.min(size, arr.length));
        return newArr;
    }
}
