package com.xiaomi.milab.gles;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class LutRenderX {
    public final byte[] FULL_QUAD_COORDINATES;
    public ByteBuffer fullQuadVertices;
    public final float[] orientationMatrix;
    public ShaderProgram shader;
    public final float[] transformMatrix;

    public LutRenderX() {
        byte[] bArr = {-1, 1, -1, -1, 1, 1, 1, -1};
        this.FULL_QUAD_COORDINATES = bArr;
        float[] fArr = new float[16];
        this.orientationMatrix = fArr;
        float[] fArr2 = new float[16];
        this.transformMatrix = fArr2;
        if (this.shader != null) {
            this.shader = null;
        }
        ShaderProgram shaderProgram = new ShaderProgram();
        this.shader = shaderProgram;
        shaderProgram.create("uniform mat4 uOrientationM;\nuniform mat4 uTransformM;\nattribute vec2 aPosition;\nvarying vec2 vTextureCoord;\nvoid main() {\ngl_Position = vec4(aPosition, 0.0, 1.0);\nvTextureCoord = (uTransformM * ((uOrientationM * gl_Position + 1.0) * 0.5)).xy;}", "precision highp float;\nuniform sampler2D mainTexture;\nuniform sampler2D LutTexture;\nvarying vec2 vTextureCoord;\nfloat m1 = 2610.0 / 16384.0;\nfloat m2 = 2523.0 / 4096.0 * 128.0;\nfloat c1 = 3423.0 / 4096.0;\nfloat c2 = 2413.0 / 4096.0 * 32.0;\nfloat c3 = 2392.0 / 4096.0 * 32.0;\nfloat linearProc(float src) {    float A = max(pow(src, 1.0 / m2) - c1, 0.0);\n    float B = c2 - c3 * pow(src, 1.0 / m2);\n    return clamp(pow(A / B, (1.0 / m1)) * 10.0 ,0.0,1.0);\n}\nfloat linearProcSimple(float src) {    return clamp(pow(src, 3.2),0.0,1.0);\n}\nvec3 hsv2rgb(vec3 c) {\n  vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n  vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n  return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvec3 rgb2hsv(vec3 c)\n{\n vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n\n float d = q.x - min(q.w, q.y);\n float e = 1.0e-10;\n return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 applyMat(vec3 incolor){\n   mat3 m = mat3(1.3436,- 0.2822,- 0.0614,-0.0653,1.07578,- 0.0105,-0.0028,- 0.0196,1.0168);\n   vec3 color = clamp(incolor.rgb * m,vec3(0.0),vec3(1.0));\n   return color;\n}\nfloat gammaProc(float src) {\n    return clamp(pow(src, 0.45), 0.0, 1.0);\n}\nfloat PQCurl(float src) {    return texture2D(LutTexture,vec2(src,0.5)).r;}void main(void) {\n   vec4 x, y, z, u, v;\n   vec2 uv = vTextureCoord;uv.x = 1.0 -uv.x;\n   vec4 result  = texture2D(mainTexture, uv);\n   result.r = PQCurl(result.r);\n   result.g = PQCurl(result.g);\n   result.b = PQCurl(result.b);\n   result.rgb = applyMat(result.rgb);\n   result.r = gammaProc(result.r);\n   result.g = gammaProc(result.g);\n   result.b = gammaProc(result.b);\n   //vec3 hsv = rgb2hsv(result.rgb);hsv.z += 0.0;\n   //result.rgb = hsv2rgb(hsv);\n   result.a = 1.0;\n   gl_FragColor = result;\n}\n");
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(8);
        this.fullQuadVertices = allocateDirect;
        allocateDirect.put(bArr).position(0);
        Matrix.setRotateM(fArr, 0, 180.0f, 0.0f, 0.0f, 1.0f);
        Matrix.setIdentityM(fArr2, 0);
    }

    public void draw(int i, int i2) {
        this.shader.use();
        int attributeLocation = this.shader.getAttributeLocation("mainTexture");
        int attributeLocation2 = this.shader.getAttributeLocation("LutTexture");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glUniform1i(attributeLocation, 0);
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, i2);
        GLES20.glUniform1i(attributeLocation2, 1);
        int attributeLocation3 = this.shader.getAttributeLocation("uOrientationM");
        int attributeLocation4 = this.shader.getAttributeLocation("uTransformM");
        GLES20.glUniformMatrix4fv(attributeLocation3, 1, false, this.orientationMatrix, 0);
        GLES20.glUniformMatrix4fv(attributeLocation4, 1, false, this.transformMatrix, 0);
        renderQuad(this.shader.getAttributeLocation("aPosition"));
        this.shader.unUse();
    }

    public final void renderQuad(int i) {
        GLES20.glVertexAttribPointer(i, 2, 5120, false, 0, (Buffer) this.fullQuadVertices);
        GLES20.glEnableVertexAttribArray(i);
        GLES20.glDrawArrays(5, 0, 4);
    }
}
