package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.adapter.IgnorePeoplePageAdapter;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.FaceDisplayItemPreferFromThumbnailSource;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.face.PeopleCursorHelper;

/* loaded from: classes.dex */
public class IgnorePeoplePageAdapter extends BaseMediaAdapterDeprecated {
    public OnRecoveryButtonClickListener mRecoveryListener;

    /* loaded from: classes.dex */
    public interface OnRecoveryButtonClickListener {
        void onPeopleRecoveryButtonClick(String str, String str2, String str3, String str4, FaceRegionRectF faceRegionRectF);
    }

    public IgnorePeoplePageAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public RequestOptions getRequestOptions(int i) {
        return GlideOptions.peopleFaceOf(getFaceRegion(i), getFileLength(i));
    }

    @Override // android.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.ignore_people_page_item, viewGroup, false);
        inflate.setTag(new ViewHolder(this.mContext, null, inflate));
        return inflate;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapterDeprecated
    public void doBindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int position = cursor.getPosition();
        viewHolder.bindData(cursor);
        viewHolder.bindImage(PeopleCursorHelper.getThumbnailPath(cursor), getDownloadUri(position), getRequestOptions(position), PeopleCursorHelper.getThumbnailDownloadType(cursor));
    }

    public Uri getDownloadUri(int i) {
        return PeopleCursorHelper.getThumbnailDownloadUri(getCursorByPosition(i));
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapterDeprecated
    public long getItemKey(int i) {
        return PeopleCursorHelper.getPeopleLocalId(getCursorByPosition(i)).hashCode();
    }

    public long getFileLength(int i) {
        return PeopleCursorHelper.getCoverSize(getCursorByPosition(i));
    }

    public final FaceRegionRectF getFaceRegion(int i) {
        return PeopleCursorHelper.getFaceRegionRectF(getCursorByPosition(i));
    }

    public void setRecoveryListener(OnRecoveryButtonClickListener onRecoveryButtonClickListener) {
        this.mRecoveryListener = onRecoveryButtonClickListener;
    }

    /* loaded from: classes.dex */
    public class ViewHolder extends FaceDisplayItemPreferFromThumbnailSource {
        public String mThumbnail;

        public static /* synthetic */ void $r8$lambda$iACX4par7CSOPiK9dVq94OyHQxs(ViewHolder viewHolder, String str, String str2, String str3, FaceRegionRectF faceRegionRectF, View view) {
            viewHolder.lambda$bindData$0(str, str2, str3, faceRegionRectF, view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ViewHolder(Context context, AttributeSet attributeSet, View view) {
            super(context, attributeSet);
            IgnorePeoplePageAdapter.this = r1;
            this.mCover = (ImageView) view.findViewById(R.id.face);
        }

        @Override // com.miui.gallery.ui.FaceDisplayItemPreferFromThumbnailSource
        public void bindImage(String str, Uri uri, RequestOptions requestOptions, DownloadType downloadType) {
            super.bindImage(str, uri, requestOptions, downloadType);
            this.mThumbnail = str;
        }

        public void bindData(Cursor cursor) {
            final String peopleLocalId = PeopleCursorHelper.getPeopleLocalId(cursor);
            final String peopleServerId = PeopleCursorHelper.getPeopleServerId(cursor);
            final String peopleName = PeopleCursorHelper.getPeopleName(cursor);
            final FaceRegionRectF faceRegionRectF = PeopleCursorHelper.getFaceRegionRectF(cursor);
            this.mCover.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.adapter.IgnorePeoplePageAdapter$ViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    IgnorePeoplePageAdapter.ViewHolder.$r8$lambda$iACX4par7CSOPiK9dVq94OyHQxs(IgnorePeoplePageAdapter.ViewHolder.this, peopleLocalId, peopleServerId, peopleName, faceRegionRectF, view);
                }
            });
        }

        public /* synthetic */ void lambda$bindData$0(String str, String str2, String str3, FaceRegionRectF faceRegionRectF, View view) {
            if (IgnorePeoplePageAdapter.this.mRecoveryListener != null) {
                IgnorePeoplePageAdapter.this.mRecoveryListener.onPeopleRecoveryButtonClick(str, str2, str3, this.mThumbnail, faceRegionRectF);
            }
        }
    }
}
