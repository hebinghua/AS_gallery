package com.miui.gallery.editor.photo.core.imports.sticker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.imports.sticker.StickerEditorView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class StickerFragment extends AbstractEffectFragment {
    public Callback mCallback;
    public List<StickerItem> mData = new ArrayList();
    public StickerEditorView mStickerEditorView;

    /* loaded from: classes2.dex */
    public interface Callback {
        void onEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new StickerRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new StickerRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        StickerEditorView stickerEditorView = (StickerEditorView) view.findViewById(R.id.sticker_editor_view);
        this.mStickerEditorView = stickerEditorView;
        stickerEditorView.setBitmap(getBitmap());
        this.mStickerEditorView.setOnEmptyCallback(new StickerEditorView.OnEmptyCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sticker.StickerFragment.1
            @Override // com.miui.gallery.editor.photo.core.imports.sticker.StickerEditorView.OnEmptyCallback
            public void onEmpty() {
                if (StickerFragment.this.mCallback != null) {
                    StickerFragment.this.mCallback.onEmpty();
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        StickerProvider.INSTANCE.writeRecentToFile();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        StickerItem stickerItem = (StickerItem) metadata;
        StickerProvider stickerProvider = StickerProvider.INSTANCE;
        Bitmap fromCache = stickerProvider.fromCache(stickerItem);
        if (fromCache != null) {
            this.mStickerEditorView.add(fromCache, stickerItem.content, stickerItem.id, stickerItem.cateName);
        } else {
            new DecodeTask(stickerItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
        this.mData.add(stickerItem);
        stickerProvider.touch(stickerItem);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        StickerEditorView stickerEditorView = this.mStickerEditorView;
        if (stickerEditorView != null) {
            stickerEditorView.setBitmap(bitmap);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mData.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mData.clear();
        this.mStickerEditorView.onClear();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void release() {
        this.mData.clear();
        this.mStickerEditorView.onRelease();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mStickerEditorView.onDestroy();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new StickerRenderData(this.mStickerEditorView.export());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        List<StickerNode> cacheNode = this.mStickerEditorView.getCacheNode();
        if (BaseMiscUtil.isValid(cacheNode)) {
            for (StickerNode stickerNode : cacheNode) {
                String str = stickerNode.mStickerCateName;
                String l = Long.toString(stickerNode.mStickerId);
                arrayList.add(String.format(Locale.US, "%s(%s)", str, l.substring(l.length() - 2)));
            }
        }
        return arrayList;
    }

    /* loaded from: classes2.dex */
    public class DecodeTask extends AsyncTask<Void, Void, Bitmap> {
        public StickerItem mItem;

        public DecodeTask(StickerItem stickerItem) {
            this.mItem = stickerItem;
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            return BitmapFactory.decodeFile(this.mItem.content);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute((DecodeTask) bitmap);
            StickerProvider.INSTANCE.putToCache(this.mItem, bitmap);
            StickerEditorView stickerEditorView = StickerFragment.this.mStickerEditorView;
            StickerItem stickerItem = this.mItem;
            stickerEditorView.add(bitmap, stickerItem.content, stickerItem.id, stickerItem.cateName);
        }
    }
}
