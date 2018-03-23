package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */

import android.opengl.GLES20;

/**
 * Created by aschmelz on 27/01/2018.
 */


import android.opengl.GLES20;

public class riGraphicTools {



    // Program variables
    public static int sp_SolidColor;
    public static int sp_Image, sp_multiply_Image;
    public static int sp_Text;


    /* SHADER Solid
     *
     * This shader is for rendering a colored primitive.
     *
     */
    public static final String vs_SolidColor =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    public static final String fs_SolidColor =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(v_texCoord.x / 1.0, v_texCoord.y,1,1);" +
                    "}";

    /* SHADER Image
 *
 * This shader is for rendering 2D images straight from a texture
 * No additional effects.
 *
 */
    public static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "attribute vec4 a_Color;" +
                    "varying vec2 v_texCoord;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +


                    "vec4 col = texture2D( s_texture, v_texCoord );" +

                    "  gl_FragColor = texture2D( s_texture, v_texCoord ) * v_Color;" +
                    "}";

    public static final String fs_multiply_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "varying vec2 v_texCoord2;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    " vec4 col = texture2D( s_texture, v_texCoord );" +

                    "  gl_FragColor = col;" +
                    "}";

    // x,y, w, h

    public static final String vs_Text =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";
    public static final String fs_Text =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord ) * v_Color;" +
                    "  gl_FragColor.rgb *= v_Color.a;" +
                    "}";

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }
}