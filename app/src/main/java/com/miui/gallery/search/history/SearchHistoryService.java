package com.miui.gallery.search.history;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.utils.SearchLog;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes2.dex */
public class SearchHistoryService extends IntentService {
    public static String EXTRA_HISTORY_ITEM = "history_item";
    public static String EXTRA_OPERATION = "operation";

    public static void addHistory(Context context, Suggestion suggestion) {
        if (context == null || suggestion == null) {
            return;
        }
        Intent intent = new Intent(context, SearchHistoryService.class);
        intent.putExtra(EXTRA_OPERATION, 0);
        intent.putExtra(EXTRA_HISTORY_ITEM, toContentValues(suggestion));
        context.startService(intent);
    }

    public static void removeHistory(Context context, Suggestion suggestion) {
        if (context == null || suggestion == null) {
            return;
        }
        Intent intent = new Intent(context, SearchHistoryService.class);
        intent.putExtra(EXTRA_OPERATION, 1);
        intent.putExtra(EXTRA_HISTORY_ITEM, toContentValues(suggestion));
        context.startService(intent);
    }

    public static ContentValues toContentValues(Suggestion suggestion) {
        ContentValues contentValues = new ContentValues();
        String suggestionTitle = suggestion.getSuggestionTitle();
        String intentActionURI = suggestion.getIntentActionURI();
        if (suggestionTitle != null) {
            if (intentActionURI != null) {
                String queryParameter = Uri.parse(intentActionURI).getQueryParameter("title");
                if (!TextUtils.isEmpty(queryParameter)) {
                    suggestionTitle = queryParameter;
                }
            }
            contentValues.put("title", suggestionTitle);
        }
        if (suggestion.getSuggestionSubTitle() != null) {
            contentValues.put("subTitle", suggestion.getSuggestionSubTitle());
        }
        if (intentActionURI != null) {
            contentValues.put("actionUri", suggestion.getIntentActionURI());
        }
        if (suggestion.getSuggestionIcon() != null) {
            contentValues.put(CallMethod.RESULT_ICON, suggestion.getSuggestionIcon());
        }
        return contentValues;
    }

    public static void clearHistory(Context context) {
        Intent intent = new Intent(context, SearchHistoryService.class);
        intent.putExtra(EXTRA_OPERATION, 2);
        context.startService(intent);
    }

    public SearchHistoryService() {
        super("SearchHistoryService");
    }

    @Override // android.app.IntentService
    public void onHandleIntent(Intent intent) {
        int intExtra = intent.getIntExtra(EXTRA_OPERATION, -1);
        if (intExtra == 0) {
            ContentValues contentValues = (ContentValues) intent.getParcelableExtra(EXTRA_HISTORY_ITEM);
            if (contentValues == null) {
                SearchLog.w("SearchHistoryService", "No history item found for operation add");
            } else {
                getContentResolver().insert(SearchContract.History.URI, contentValues);
            }
        } else if (intExtra != 1) {
            if (intExtra == 2) {
                getContentResolver().delete(SearchContract.History.URI, null, null);
            } else {
                SearchLog.w("SearchHistoryService", "Invalid history operation type: %d", Integer.valueOf(intExtra));
            }
        } else {
            ContentValues contentValues2 = (ContentValues) intent.getParcelableExtra(EXTRA_HISTORY_ITEM);
            if (contentValues2 == null) {
                SearchLog.w("SearchHistoryService", "No history item found for operation delete");
            } else {
                getContentResolver().delete(SearchContract.History.URI, "(actionUri= ?)", new String[]{contentValues2.getAsString("actionUri")});
            }
        }
    }
}
