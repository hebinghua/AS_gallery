package com.miui.gallery.editor.photo.app.longcrop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractLongCropFragment;

@Deprecated
/* loaded from: classes2.dex */
public class LongCropFragment extends MenuFragment<AbstractLongCropFragment, SdkProvider<Object, AbstractLongCropFragment>> {
    public AsyncTask<Void, Void, Bitmap> mDecodeTask;
    public TextView mTitle;

    public LongCropFragment() {
        super(Effect.LONG_CROP);
        this.mDecodeTask = new AsyncTask<Void, Void, Bitmap>() { // from class: com.miui.gallery.editor.photo.app.longcrop.LongCropFragment.1
            @Override // android.os.AsyncTask
            public Bitmap doInBackground(Void... voidArr) {
                return LongCropFragment.this.decodeOrigin();
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ((AbstractLongCropFragment) LongCropFragment.this.getRenderFragment()).setOriginBitmap(bitmap, LongCropFragment.this.getPreRenderData());
                }
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDecodeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.common_menu_bottom_bar, viewGroup, false);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mTitle = textView;
        textView.setText(R.string.photo_editor_long_crop);
    }
}
