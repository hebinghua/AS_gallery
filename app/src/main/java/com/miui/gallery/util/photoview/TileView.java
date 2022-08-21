package com.miui.gallery.util.photoview;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.View;
import com.miui.gallery.Config$TileConfig;
import com.miui.gallery.util.ReusedBitmapCache;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class TileView {
    public WeakReference<View> mAttachViewRef;
    public TileDecodeHandler mDecodeHandler;
    public TileDecodeManager mDecodeManager;
    public boolean mIsMemoryTrimmed;
    public TileBitProvider mProvider;
    public BitmapRecycleCallback mRecycleCallback;
    public float mRotation;
    public final int mTileSize;
    public TrimMemoryCallback mTrimMemoryCallback;
    public Rect mViewPort = new Rect();
    public Rect mTileRange = new Rect();
    public RectF mTileIntersect = new RectF();
    public Matrix mRotateMatrix = new Matrix();
    public int mRefreshId = -1;
    public SparseArray<Tile> mDrawingTiles = new SparseArray<>();
    public List<Tile> mDestroyedTiles = new LinkedList();
    public SparseArray<Tile> mTempDrawingTiles = new SparseArray<>();
    public List<Tile> mTempDecodeTiles = new ArrayList();

    public TileView(TileBitProvider tileBitProvider, View view, BitmapRecycleCallback bitmapRecycleCallback, TrimMemoryCallback trimMemoryCallback) {
        this.mProvider = tileBitProvider;
        this.mAttachViewRef = new WeakReference<>(view);
        this.mRecycleCallback = bitmapRecycleCallback;
        this.mTrimMemoryCallback = trimMemoryCallback;
        TileDecodeHandler tileDecodeHandler = new TileDecodeHandler(this);
        this.mDecodeHandler = tileDecodeHandler;
        TileDecodeManager tileDecodeManager = new TileDecodeManager(tileDecodeHandler, this.mProvider);
        this.mDecodeManager = tileDecodeManager;
        this.mDecodeHandler.setDecodeManager(tileDecodeManager);
        this.mTileSize = computeTileSize(tileBitProvider);
    }

    public final int computeTileSize(TileBitProvider tileBitProvider) {
        int i = Config$TileConfig.TILE_SIZE;
        int imageHeight = tileBitProvider.getImageHeight();
        int imageWidth = tileBitProvider.getImageWidth();
        if (imageHeight <= i || imageHeight >= Config$TileConfig.TILE_SIZE_UPPER_LIMIT) {
            imageHeight = i;
        }
        return (imageWidth <= i || imageWidth >= Config$TileConfig.TILE_SIZE_UPPER_LIMIT) ? imageHeight : Math.max(imageHeight, imageWidth);
    }

    public void notifyInvalidate(RectF rectF, float f) {
        layoutTiles(rectF, f);
    }

    public void setViewPort(Rect rect) {
        this.mViewPort.set(rect);
    }

    public int getTileProviderWidth() {
        TileBitProvider tileBitProvider = this.mProvider;
        if (tileBitProvider != null) {
            return tileBitProvider.getImageWidth();
        }
        return 0;
    }

    public int getTileProviderHeight() {
        TileBitProvider tileBitProvider = this.mProvider;
        if (tileBitProvider != null) {
            return tileBitProvider.getImageHeight();
        }
        return 0;
    }

    public int getTileProviderRotation() {
        TileBitProvider tileBitProvider = this.mProvider;
        if (tileBitProvider != null) {
            return tileBitProvider.getRotation();
        }
        return 0;
    }

    public void cleanup() {
        int size = this.mDrawingTiles.size();
        this.mDecodeManager.cancel();
        this.mDecodeHandler.removeCallbacksAndMessages(null);
        this.mDecodeHandler.release();
        for (int i = 0; i < size; i++) {
            final Tile valueAt = this.mDrawingTiles.valueAt(i);
            if (valueAt != null) {
                ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.util.photoview.TileView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        Tile.this.recycle();
                    }
                });
            }
        }
        this.mDrawingTiles.clear();
        if (this.mIsMemoryTrimmed) {
            TrimMemoryCallback trimMemoryCallback = this.mTrimMemoryCallback;
            if (trimMemoryCallback != null) {
                trimMemoryCallback.onStopTrimMemory(1);
                this.mTrimMemoryCallback = null;
            }
            this.mIsMemoryTrimmed = false;
        }
        this.mRecycleCallback = null;
        if (size > 0) {
            invalidate();
        }
    }

    public final int calculateInSampleSize(RectF rectF) {
        int imageWidth = this.mProvider.getImageWidth() / 2;
        int imageHeight = this.mProvider.getImageHeight() / 2;
        int i = 1;
        while (true) {
            if (imageWidth / i >= rectF.width() || imageHeight / i >= rectF.height()) {
                i *= 2;
            } else {
                return i;
            }
        }
    }

    public final void layoutTiles(RectF rectF, float f) {
        if (rectF == null) {
            return;
        }
        SystemClock.uptimeMillis();
        this.mDecodeManager.clear();
        this.mTileIntersect.set(rectF);
        RectF rectF2 = this.mTileIntersect;
        Rect rect = this.mViewPort;
        rectF2.intersect(rect.left, rect.top, rect.right, rect.bottom);
        this.mRotation = f;
        this.mRotateMatrix.reset();
        this.mRotateMatrix.postRotate(f, this.mViewPort.centerX(), this.mViewPort.centerY());
        if (this.mProvider.getRotation() != 0) {
            this.mRotateMatrix.postRotate(-this.mProvider.getRotation(), this.mViewPort.centerX(), this.mViewPort.centerY());
        }
        this.mRotateMatrix.mapRect(rectF);
        this.mRotateMatrix.mapRect(this.mTileIntersect);
        int calculateInSampleSize = calculateInSampleSize(rectF);
        float f2 = this.mTileIntersect.left - rectF.left;
        if (this.mProvider.isFlip()) {
            f2 = this.mTileIntersect.right - rectF.right;
        }
        float imageWidth = this.mProvider.getImageWidth() / rectF.width();
        float imageHeight = this.mProvider.getImageHeight() / rectF.height();
        int width = (int) ((f2 / rectF.width()) * this.mProvider.getImageWidth());
        int height = (int) (((this.mTileIntersect.top - rectF.top) / rectF.height()) * this.mProvider.getImageHeight());
        this.mTileRange.set(floor(width, calculateInSampleSize), floor(height, calculateInSampleSize), ceil((int) (width + (this.mTileIntersect.width() * imageWidth)), calculateInSampleSize), ceil((int) (height + (this.mTileIntersect.height() * imageHeight)), calculateInSampleSize));
        Rect rect2 = this.mTileRange;
        float f3 = (rect2.left / imageWidth) + rectF.left;
        float f4 = (rect2.top / imageHeight) + rectF.top;
        int i = this.mTileSize;
        refreshTiles(f3, f4, (i * calculateInSampleSize) / imageWidth, (i * calculateInSampleSize) / imageHeight, calculateInSampleSize);
    }

    public final boolean invalidate() {
        View attachView = getAttachView();
        if (attachView != null) {
            attachView.invalidate();
            return true;
        }
        return false;
    }

    public final Tile obtainTile(Rect rect, RectF rectF, int i) {
        if (this.mDestroyedTiles.size() > 0) {
            Tile remove = this.mDestroyedTiles.remove(0);
            remove.updateTileParam(rect, i);
            remove.updateDisplayParam(rectF);
            return remove;
        }
        return new Tile(rect, rectF, i, this.mRecycleCallback);
    }

    public final void refreshTiles(float f, float f2, float f3, float f4, int i) {
        increaseRefreshId();
        int i2 = this.mTileRange.top;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            Rect rect = this.mTileRange;
            if (i2 >= rect.bottom) {
                break;
            }
            i3++;
            int i5 = rect.left;
            float f5 = f;
            int i6 = 0;
            while (i5 < this.mTileRange.right) {
                i6++;
                int i7 = this.mTileSize;
                Rect rect2 = new Rect(i5, i2, (i7 * i) + i5, (i7 * i) + i2);
                float f6 = f5 + f3;
                RectF rectF = new RectF(f5, f2, f6, f2 + f4);
                int makeTileKey = makeTileKey(rect2);
                Tile tile = this.mDrawingTiles.get(makeTileKey);
                if (tile != null) {
                    tile.updateDisplayParam(rectF);
                    this.mTempDrawingTiles.put(makeTileKey, tile);
                } else {
                    tile = this.mDecodeManager.getDecodingTile(makeTileKey);
                    if (tile != null) {
                        tile.updateDisplayParam(rectF);
                    } else {
                        tile = obtainTile(rect2, rectF, i);
                        this.mTempDecodeTiles.add(tile);
                    }
                }
                tile.setIndex(i3, i6);
                tile.setRefreshId(this.mRefreshId);
                i5 += this.mTileSize * i;
                f5 = f6;
            }
            f2 += f4;
            i2 += this.mTileSize * i;
            i4 = i6;
        }
        trimMemoryIfNeeded(i3, i4);
        int size = this.mDrawingTiles.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (this.mTempDrawingTiles.get(this.mDrawingTiles.keyAt(i8)) == null) {
                Tile valueAt = this.mDrawingTiles.valueAt(i8);
                valueAt.recycle();
                this.mDestroyedTiles.add(valueAt);
            }
        }
        this.mDrawingTiles.clear();
        int size2 = this.mTempDrawingTiles.size();
        for (int i9 = 0; i9 < size2; i9++) {
            Tile valueAt2 = this.mTempDrawingTiles.valueAt(i9);
            this.mDrawingTiles.put(makeTileKey(valueAt2.getTileRect()), valueAt2);
        }
        if (this.mDrawingTiles.size() > 0) {
            invalidate();
        }
        for (Tile tile2 : this.mTempDecodeTiles) {
            this.mDecodeManager.put(tile2);
        }
        this.mTempDrawingTiles.clear();
        this.mTempDecodeTiles.clear();
    }

    public final void trimMemoryIfNeeded(int i, int i2) {
        if (this.mIsMemoryTrimmed || this.mTrimMemoryCallback == null) {
            return;
        }
        int i3 = i * i2;
        int i4 = this.mTileSize;
        if (!(((long) ((i3 * i4) * i4)) * ((long) ReusedBitmapCache.getBytesPerPixel(Config$TileConfig.getBitmapConfig())) >= 67108864)) {
            return;
        }
        this.mTrimMemoryCallback.onTrimMemory(1);
        this.mIsMemoryTrimmed = true;
    }

    public void draw(Canvas canvas, Paint paint) {
        int save;
        SystemClock.uptimeMillis();
        int size = this.mDrawingTiles.size();
        if (size == 0) {
            return;
        }
        if (!this.mProvider.isFlip() && this.mProvider.getRotation() == 0 && this.mRotation == 0.0f) {
            save = 0;
        } else {
            save = canvas.save();
            canvas.rotate(this.mProvider.getRotation() - this.mRotation, this.mViewPort.centerX(), this.mViewPort.centerY());
            if (this.mProvider.isFlip()) {
                canvas.scale(-1.0f, 1.0f, this.mViewPort.centerX(), this.mViewPort.centerY());
            }
        }
        for (int i = 0; i < size; i++) {
            Tile valueAt = this.mDrawingTiles.valueAt(i);
            if (valueAt.getRefreshId() == this.mRefreshId) {
                valueAt.draw(canvas, paint);
            }
        }
        if (save <= 0) {
            return;
        }
        canvas.restoreToCount(save);
    }

    public final void increaseRefreshId() {
        if (this.mRefreshId == Integer.MAX_VALUE) {
            this.mRefreshId = -1;
        }
        this.mRefreshId++;
    }

    public final int floor(int i, int i2) {
        int i3 = 0;
        while (true) {
            int i4 = this.mTileSize;
            if (i3 * i4 * i2 <= i) {
                i3++;
            } else {
                return (i3 - 1) * i4 * i2;
            }
        }
    }

    public final int ceil(int i, int i2) {
        int i3 = 0;
        while (true) {
            int i4 = this.mTileSize;
            if (i3 * i4 * i2 < i) {
                i3++;
            } else {
                return i3 * i4 * i2;
            }
        }
    }

    public static int makeTileKey(Rect rect) {
        if (rect == null) {
            return 0;
        }
        return ((((((527 + rect.left) * 31) + rect.top) * 31) + rect.right) * 31) + rect.bottom;
    }

    public final View getAttachView() {
        WeakReference<View> weakReference = this.mAttachViewRef;
        View view = weakReference != null ? weakReference.get() : null;
        if (view == null) {
            cleanup();
        }
        return view;
    }

    /* loaded from: classes2.dex */
    public static class TileDecodeHandler extends Handler {
        public WeakReference<TileDecodeManager> mDecodeManager;
        public WeakReference<TileView> mTileView;

        public TileDecodeHandler(TileView tileView) {
            this.mTileView = new WeakReference<>(tileView);
        }

        public void setDecodeManager(TileDecodeManager tileDecodeManager) {
            this.mDecodeManager = new WeakReference<>(tileDecodeManager);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakReference<TileDecodeManager> weakReference;
            WeakReference<TileView> weakReference2;
            WeakReference<TileDecodeManager> weakReference3;
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    return;
                }
                if (message.obj != null && (weakReference3 = this.mDecodeManager) != null && weakReference3.get() != null) {
                    this.mDecodeManager.get().removeDecodingTile(TileView.makeTileKey(((Tile) message.obj).getTileRect()));
                }
                DefaultLogger.w("TileView", "tile decode fail: %s", message.obj);
            } else if (message.obj == null || (weakReference = this.mDecodeManager) == null || weakReference.get() == null) {
            } else {
                Tile tile = (Tile) message.obj;
                int makeTileKey = TileView.makeTileKey(tile.getTileRect());
                this.mDecodeManager.get().removeDecodingTile(makeTileKey);
                if (tile.isActive() && (weakReference2 = this.mTileView) != null && weakReference2.get() != null) {
                    this.mTileView.get().mDrawingTiles.put(makeTileKey, tile);
                    this.mTileView.get().invalidate();
                    return;
                }
                tile.recycle();
            }
        }

        public void release() {
            WeakReference<TileDecodeManager> weakReference = this.mDecodeManager;
            if (weakReference != null) {
                weakReference.clear();
                this.mDecodeManager = null;
            }
            WeakReference<TileView> weakReference2 = this.mTileView;
            if (weakReference2 != null) {
                weakReference2.clear();
                this.mTileView = null;
            }
        }
    }
}
