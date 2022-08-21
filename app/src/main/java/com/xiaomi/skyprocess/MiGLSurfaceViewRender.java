package com.xiaomi.skyprocess;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes3.dex */
public class MiGLSurfaceViewRender implements GLSurfaceView.Renderer {
    private static final String TAG = "MiGLSurfaceViewRender";
    private static final String dispalyFragmentShaderString = "precision mediump float;uniform sampler2D tex_rgb_res;varying vec2 textureOut;void main() {vec4 res = texture2D(tex_rgb_res, textureOut);gl_FragColor = res;}";
    private static final String vertexShaderString = "attribute vec4 vertexIn;attribute vec2 textureIn;varying vec2 textureOut;uniform mat4 modelViewProjectionMatrix;void main() {gl_Position = modelViewProjectionMatrix*vertexIn ;textureOut = (vec4(textureIn.x, 1.0-textureIn.y, 0.0, 1.0)).xy;}";
    private SurfaceTexture mInputSurfaceTexture;
    private OpenGlRender mOpenGlRender;
    private int mProgramID;
    private GLSurfaceView mTargetSurface;
    private int mWindowHeight;
    private int mWindowWidth;
    private int player_fragshader_texture;
    private static float[] vertexVertices = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] textureVertices = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    public int ATTRIB_VERTEX = 0;
    public int ATTRIB_TEXTURE = 0;
    public ByteBuffer vertexVertices_buffer = null;
    public ByteBuffer textureVertices_buffer = null;
    private float[] mtransformMatrix = new float[16];
    public int[] pause_fragshader_texture = new int[1];
    public int[] pause_fragshader_texture_fbo = new int[1];
    private int[] mRgbTexture = new int[1];

    public MiGLSurfaceViewRender(GLSurfaceView gLSurfaceView, OpenGlRender openGlRender) {
        Log.d(TAG, "Construct MiGLSurfaceViewRender " + gLSurfaceView);
        this.mTargetSurface = gLSurfaceView;
        gLSurfaceView.setEGLContextClientVersion(2);
        this.mOpenGlRender = openGlRender;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        int[] iArr;
        Log.d(TAG, "onSurfaceCreated :");
        InitShaders();
        byte[] bArr = new byte[this.vertexVertices_buffer.remaining()];
        this.vertexVertices_buffer.get(bArr);
        this.vertexVertices_buffer.position(0);
        byte[] bArr2 = new byte[this.textureVertices_buffer.remaining()];
        this.textureVertices_buffer.get(bArr2);
        this.textureVertices_buffer.position(0);
        this.mOpenGlRender.SetOpengGlRenderParams(this.mProgramID, this.player_fragshader_texture, this.ATTRIB_VERTEX, this.ATTRIB_TEXTURE, bArr, bArr2);
        int[] iArr2 = this.pause_fragshader_texture;
        if (iArr2 != null && (iArr = this.pause_fragshader_texture_fbo) != null) {
            this.mOpenGlRender.setCopyTextureOfFBO(iArr2[0], iArr[0]);
        }
        this.mOpenGlRender.SetCurrentGLContextJniForGraph();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Log.d(TAG, "wangqm onSurfaceChanged width:" + i + " height:" + i2);
        this.mWindowWidth = i;
        this.mWindowHeight = i2;
        GLES20.glViewport(0, 0, i, i2);
        OpenGlRender openGlRender = this.mOpenGlRender;
        if (openGlRender != null) {
            openGlRender.SetWindowSize(0, 0, i, i2);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        DrawFrameInNative();
    }

    public void onDestroy() {
        Log.d(TAG, "wangqm onDestroy");
        GLES20.glDeleteTextures(1, this.pause_fragshader_texture, 0);
        checkNoGLES2Error();
        GLES20.glDeleteFramebuffers(1, this.pause_fragshader_texture_fbo, 0);
        checkNoGLES2Error();
        int i = this.mProgramID;
        if (i != 0) {
            GLES20.glDeleteProgram(i);
        }
    }

    private void DrawFrameInNative() {
        this.mOpenGlRender.RenderFrame();
    }

    public void ReadyRenderFrame() {
        Log.d(TAG, "ReadyRenderFrame");
        this.mTargetSurface.requestRender();
    }

    public void InitShaders() {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(vertexVertices.length * 4);
        this.vertexVertices_buffer = allocateDirect;
        allocateDirect.order(ByteOrder.nativeOrder());
        this.vertexVertices_buffer.asFloatBuffer().put(vertexVertices);
        this.vertexVertices_buffer.position(0);
        ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        this.textureVertices_buffer = allocateDirect2;
        allocateDirect2.order(ByteOrder.nativeOrder());
        this.textureVertices_buffer.asFloatBuffer().put(textureVertices);
        this.textureVertices_buffer.position(0);
        this.mProgramID = createProgram(vertexShaderString, dispalyFragmentShaderString);
        checkNoGLES2Error();
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.mProgramID, "vertexIn");
        this.ATTRIB_VERTEX = glGetAttribLocation;
        if (glGetAttribLocation == -1) {
            Log.d(TAG, "glGetAttribLocation error ");
        }
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.mProgramID, "textureIn");
        this.ATTRIB_TEXTURE = glGetAttribLocation2;
        if (glGetAttribLocation2 == -1) {
            Log.d(TAG, "glGetAttribLocation error ");
        }
        GLES20.glUseProgram(this.mProgramID);
        checkNoGLES2Error();
        this.player_fragshader_texture = GLES20.glGetUniformLocation(this.mProgramID, "tex_rgb_res");
        checkNoGLES2Error();
        Log.d(TAG, "glGetAttribLocation player_fragshader_texture: " + this.player_fragshader_texture);
        GLES20.glGenTextures(1, this.pause_fragshader_texture, 0);
        checkNoGLES2Error();
        GLES20.glBindTexture(3553, this.pause_fragshader_texture[0]);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        checkNoGLES2Error();
        GLES20.glGenFramebuffers(1, this.pause_fragshader_texture_fbo, 0);
        checkNoGLES2Error();
    }

    public int createProgram(String str, String str2) {
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        Log.d(TAG, "vertex shader: " + str + " -- " + loadShader);
        Log.d(TAG, "fragment shader: " + str2 + " -- " + loadShader2);
        int glCreateProgram = GLES20.glCreateProgram();
        abortUnless(glCreateProgram > 0, "Create OpenGL program failed.");
        Log.d(TAG, "program: " + glCreateProgram);
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, loadShader);
            GLES20.glAttachShader(glCreateProgram, loadShader2);
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                GLES20.glDeleteProgram(glCreateProgram);
                glCreateProgram = 0;
            }
        }
        Log.d(TAG, " end if program: " + glCreateProgram);
        return glCreateProgram;
    }

    private int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        Log.d(TAG, "shader: " + glCreateShader);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                GLES20.glDeleteShader(glCreateShader);
                glCreateShader = 0;
            }
        }
        Log.d(TAG, "end shader: " + glCreateShader);
        return glCreateShader;
    }

    private static void checkNoGLES2Error() {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            Log.e(TAG, "GLES20 error:" + glGetError);
        }
        boolean z = glGetError == 0;
        abortUnless(z, "GLES20 error: " + glGetError);
    }

    private static void abortUnless(boolean z, String str) {
        if (z) {
            return;
        }
        throw new RuntimeException(str);
    }
}
