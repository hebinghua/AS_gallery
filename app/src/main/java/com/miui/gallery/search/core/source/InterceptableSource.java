package com.miui.gallery.search.core.source;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes2.dex */
public abstract class InterceptableSource extends AbstractSource {
    public Context mContext;

    public abstract SourceResult doGetSuggestions(QueryInfo queryInfo);

    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return i == 12;
    }

    public InterceptableSource(Context context) {
        this.mContext = context;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.search.core.source.Source, com.miui.gallery.search.core.source.SuggestionResultProvider
    /* renamed from: getSuggestions */
    public SourceResult mo1333getSuggestions(QueryInfo queryInfo) {
        SourceResult prejudgeResult = getPrejudgeResult(queryInfo);
        return prejudgeResult != null ? prejudgeResult : doGetSuggestions(queryInfo);
    }

    public BaseSourceResult generateDefaultResult(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        return new BaseSourceResult(queryInfo, this, suggestionCursor);
    }

    public SourceResult getPrejudgeResult(QueryInfo queryInfo) {
        if (isFatalCondition(queryInfo, 3) && TextUtils.isEmpty(SearchUtils.getXiaomiId())) {
            BaseSourceResult generateDefaultResult = generateDefaultResult(queryInfo, null);
            generateDefaultResult.setErrorInfo(new ErrorInfo(3));
            return generateDefaultResult;
        } else if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(this.mContext)) {
            BaseSourceResult generateDefaultResult2 = generateDefaultResult(queryInfo, null);
            generateDefaultResult2.setErrorInfo(new ErrorInfo(4));
            return generateDefaultResult2;
        } else if (isFatalCondition(queryInfo, 10) && !Preference.sIsFirstSynced()) {
            if (!BaseNetworkUtils.isNetworkConnected()) {
                BaseSourceResult generateDefaultResult3 = generateDefaultResult(queryInfo, null);
                generateDefaultResult3.setErrorInfo(new ErrorInfo(1));
                return generateDefaultResult3;
            } else if (SyncUtil.isGalleryCloudSyncable(this.mContext)) {
                BaseSourceResult generateDefaultResult4 = generateDefaultResult(queryInfo, null);
                generateDefaultResult4.setErrorInfo(new ErrorInfo(10));
                return generateDefaultResult4;
            } else {
                BaseSourceResult generateDefaultResult5 = generateDefaultResult(queryInfo, null);
                generateDefaultResult5.setErrorInfo(new ErrorInfo(4));
                return generateDefaultResult5;
            }
        } else if (isFatalCondition(queryInfo, 12) && !AIAlbumStatusHelper.isLocalSearchOpen(true)) {
            BaseSourceResult generateDefaultResult6 = generateDefaultResult(queryInfo, null);
            generateDefaultResult6.setErrorInfo(new ErrorInfo(12));
            return generateDefaultResult6;
        } else if (isFatalCondition(queryInfo, 13) && !AIAlbumStatusHelper.isCloudControlSearchAIAlbumOpen()) {
            BaseSourceResult generateDefaultResult7 = generateDefaultResult(queryInfo, null);
            generateDefaultResult7.setErrorInfo(new ErrorInfo(13));
            return generateDefaultResult7;
        } else if (!isFatalCondition(queryInfo, 14) || AIAlbumStatusHelper.isCloudControlSearchBarOpen()) {
            return null;
        } else {
            BaseSourceResult generateDefaultResult8 = generateDefaultResult(queryInfo, null);
            generateDefaultResult8.setErrorInfo(new ErrorInfo(14));
            return generateDefaultResult8;
        }
    }
}
