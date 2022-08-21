package androidx.heifwriter;

import android.graphics.Bitmap;
import android.graphics.Rect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class EglRectBlt {
    public static final FloatBuffer FULL_RECTANGLE_BUF;
    public static final float[] FULL_RECTANGLE_COORDS;
    public Texture2dProgram mProgram;
    public final FloatBuffer mTexCoordArray;
    public final float[] mTexCoords;
    public final int mTexHeight;
    public final int mTexWidth;

    static {
        float[] fArr = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        FULL_RECTANGLE_COORDS = fArr;
        FULL_RECTANGLE_BUF = createFloatBuffer(fArr);
    }

    public static FloatBuffer createFloatBuffer(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    public EglRectBlt(Texture2dProgram texture2dProgram, int i, int i2) {
        float[] fArr = new float[8];
        this.mTexCoords = fArr;
        this.mTexCoordArray = createFloatBuffer(fArr);
        this.mProgram = texture2dProgram;
        this.mTexWidth = i;
        this.mTexHeight = i2;
    }

    public void release(boolean z) {
        Texture2dProgram texture2dProgram = this.mProgram;
        if (texture2dProgram != null) {
            if (z) {
                texture2dProgram.release();
            }
            this.mProgram = null;
        }
    }

    public int createTextureObject() {
        return this.mProgram.createTextureObject();
    }

    public void loadTexture(int i, Bitmap bitmap) {
        this.mProgram.loadTexture(i, bitmap);
    }

    public void copyRect(int i, float[] fArr, Rect rect) {
        setTexRect(rect);
        this.mProgram.draw(Texture2dProgram.IDENTITY_MATRIX, FULL_RECTANGLE_BUF, 0, 4, 2, 8, fArr, this.mTexCoordArray, i, 8);
    }

    public void setTexRect(Rect rect) {
        float[] fArr = this.mTexCoords;
        int i = rect.left;
        int i2 = this.mTexWidth;
        fArr[0] = i / i2;
        int i3 = rect.bottom;
        int i4 = this.mTexHeight;
        fArr[1] = 1.0f - (i3 / i4);
        int i5 = rect.right;
        fArr[2] = i5 / i2;
        fArr[3] = 1.0f - (i3 / i4);
        fArr[4] = i / i2;
        int i6 = rect.top;
        fArr[5] = 1.0f - (i6 / i4);
        fArr[6] = i5 / i2;
        fArr[7] = 1.0f - (i6 / i4);
        this.mTexCoordArray.put(fArr);
        this.mTexCoordArray.position(0);
    }
}
