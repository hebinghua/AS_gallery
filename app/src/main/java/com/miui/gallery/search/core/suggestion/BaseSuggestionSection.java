package com.miui.gallery.search.core.suggestion;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.Source;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseSuggestionSection implements SuggestionSection {
    public String mDataURI;
    public final SuggestionCursor mItems;
    public Suggestion mMoreItem;
    public final QueryInfo mQueryInfo;
    public List<RankInfo> mRankInfos;
    public SuggestionExtras mSectionExtras;
    public String mSectionSubTitle;
    public String mSectionTitle;
    public final String mSectionTypeString;

    public BaseSuggestionSection(QueryInfo queryInfo, String str, SuggestionCursor suggestionCursor) {
        this(queryInfo, str, suggestionCursor, null, null, null, null, null, null);
    }

    public BaseSuggestionSection(QueryInfo queryInfo, SearchConstants.SectionType sectionType, SuggestionCursor suggestionCursor, String str, String str2, String str3, Suggestion suggestion) {
        this(queryInfo, sectionType != null ? sectionType.getName() : SearchConstants.SectionType.SECTION_TYPE_DEFAULT.getName(), suggestionCursor, str, str2, str3, suggestion, null, null);
    }

    public BaseSuggestionSection(QueryInfo queryInfo, String str, SuggestionCursor suggestionCursor, String str2, String str3, String str4, Suggestion suggestion, List<RankInfo> list, Bundle bundle) {
        this.mQueryInfo = queryInfo;
        this.mItems = suggestionCursor;
        if (TextUtils.isEmpty(str)) {
            this.mSectionTypeString = SearchConstants.SectionType.SECTION_TYPE_DEFAULT.getName();
        } else {
            this.mSectionTypeString = str;
        }
        this.mDataURI = str2;
        this.mSectionTitle = str3;
        this.mSectionSubTitle = str4;
        this.mMoreItem = suggestion;
        if (list != null && list.size() > 0) {
            this.mRankInfos = new ArrayList(list);
        }
        if (bundle == null || bundle == Bundle.EMPTY) {
            return;
        }
        setExtras(bundle);
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public String getSectionTypeString() {
        return this.mSectionTypeString;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public SearchConstants.SectionType getSectionType() {
        return SearchConstants.SectionType.fromName(this.mSectionTypeString);
    }

    public void setSectionMoreItem(Suggestion suggestion) {
        this.mMoreItem = suggestion;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public Suggestion moveToMore() {
        return this.mMoreItem;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public String getSectionTitle() {
        return this.mSectionTitle;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public String getSectionSubTitle() {
        return this.mSectionSubTitle;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public String getDataURI() {
        return this.mDataURI;
    }

    public void setSectionExtras(SuggestionExtras suggestionExtras) {
        this.mSectionExtras = suggestionExtras;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public SuggestionExtras getSectionExtras() {
        return this.mSectionExtras;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection
    public List<RankInfo> getRankInfos() {
        return this.mRankInfos;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor
    public QueryInfo getQueryInfo() {
        return this.mQueryInfo;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor
    public Suggestion getCurrent() {
        return this.mItems.getCurrent();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public Source getSuggestionSource() {
        return this.mItems.getSuggestionSource();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getIntentActionURI() {
        return this.mItems.getIntentActionURI();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionIcon() {
        return this.mItems.getSuggestionIcon();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionTitle() {
        return this.mItems.getSuggestionTitle();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionSubTitle() {
        return this.mItems.getSuggestionSubTitle();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public SuggestionExtras getSuggestionExtras() {
        return this.mItems.getSuggestionExtras();
    }

    @Override // com.miui.gallery.search.core.QuietlyCloseable, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.close();
        }
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getBackupIcons() {
        return this.mItems.getBackupIcons();
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionSection, android.database.Cursor
    public int getCount() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return 0;
        }
        return suggestionCursor.getCount();
    }

    @Override // android.database.Cursor
    public String[] getColumnNames() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor == null ? new String[0] : suggestionCursor.getColumnNames();
    }

    @Override // android.database.Cursor
    public String getString(int i) {
        return this.mItems.getString(i);
    }

    @Override // android.database.Cursor
    public short getShort(int i) {
        return this.mItems.getShort(i);
    }

    @Override // android.database.Cursor
    public int getInt(int i) {
        return this.mItems.getInt(i);
    }

    @Override // android.database.Cursor
    public long getLong(int i) {
        return this.mItems.getLong(i);
    }

    @Override // android.database.Cursor
    public float getFloat(int i) {
        return this.mItems.getFloat(i);
    }

    @Override // android.database.Cursor
    public double getDouble(int i) {
        return this.mItems.getDouble(i);
    }

    @Override // android.database.Cursor
    public int getPosition() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return -1;
        }
        return suggestionCursor.getPosition();
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.getWantsAllOnMoveCalls();
    }

    @Override // android.database.Cursor
    public boolean isAfterLast() {
        return this.mItems.isAfterLast();
    }

    @Override // android.database.Cursor
    public boolean isBeforeFirst() {
        return this.mItems.isBeforeFirst();
    }

    @Override // android.database.Cursor
    public boolean isFirst() {
        return this.mItems.isFirst();
    }

    @Override // android.database.Cursor
    public boolean isLast() {
        return this.mItems.isLast();
    }

    @Override // android.database.Cursor
    public int getType(int i) {
        return this.mItems.getType(i);
    }

    @Override // android.database.Cursor
    public boolean isNull(int i) {
        return this.mItems.isNull(i);
    }

    @Override // android.database.Cursor
    public boolean moveToLast() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.moveToLast();
    }

    @Override // android.database.Cursor
    public boolean move(int i) {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.move(i);
    }

    @Override // android.database.Cursor
    public boolean moveToPosition(int i) {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.moveToPosition(i);
    }

    @Override // android.database.Cursor
    public boolean moveToNext() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.moveToNext();
    }

    @Override // android.database.Cursor
    public boolean moveToPrevious() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.moveToPrevious();
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver contentObserver) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.registerContentObserver(contentObserver);
        }
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.registerDataSetObserver(dataSetObserver);
        }
    }

    @Override // android.database.Cursor
    public boolean moveToFirst() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.moveToFirst();
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String str) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return -1;
        }
        return suggestionCursor.getColumnIndex(str);
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String str) throws IllegalArgumentException {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            throw new IllegalArgumentException("column '" + str + "' does not exist");
        }
        return suggestionCursor.getColumnIndexOrThrow(str);
    }

    @Override // android.database.Cursor
    public String getColumnName(int i) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return null;
        }
        return suggestionCursor.getColumnName(i);
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return 0;
        }
        return suggestionCursor.getColumnCount();
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int i) {
        return this.mItems.getBlob(i);
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.copyStringToBuffer(i, charArrayBuffer);
        }
    }

    @Override // android.database.Cursor
    public void deactivate() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.deactivate();
        }
    }

    @Override // android.database.Cursor
    public boolean requery() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.requery();
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        SuggestionCursor suggestionCursor = this.mItems;
        return suggestionCursor != null && suggestionCursor.isClosed();
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return null;
        }
        return suggestionCursor.getExtras();
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor
    public void setExtras(Bundle bundle) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.setExtras(bundle);
        }
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle bundle) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return null;
        }
        return suggestionCursor.respond(bundle);
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.setNotificationUri(contentResolver, uri);
        }
    }

    @Override // android.database.Cursor
    public Uri getNotificationUri() {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor == null) {
            return null;
        }
        return suggestionCursor.getNotificationUri();
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver contentObserver) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.unregisterContentObserver(contentObserver);
        }
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        SuggestionCursor suggestionCursor = this.mItems;
        if (suggestionCursor != null) {
            suggestionCursor.unregisterDataSetObserver(dataSetObserver);
        }
    }
}
