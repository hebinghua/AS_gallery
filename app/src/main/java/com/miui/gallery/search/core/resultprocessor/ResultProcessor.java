package com.miui.gallery.search.core.resultprocessor;

import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.utils.SearchLog;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class ResultProcessor<OUT extends SuggestionResult> {
    public abstract OUT getMergedResult(List<SourceResult> list);

    public OUT process(List<SourceResult> list) {
        if (list == null || list.isEmpty()) {
            SearchLog.w("ResultProcessor", "Received a null input value!");
            return null;
        }
        for (SourceResult sourceResult : list) {
            sourceResult.acquire();
        }
        List<SourceResult> sortedSourceResults = getSortedSourceResults(list);
        if (sortedSourceResults == null) {
            SearchLog.w("ResultProcessor", "The sorted source results are null!");
        }
        OUT mergedResult = getMergedResult(sortedSourceResults);
        for (SourceResult sourceResult2 : list) {
            sourceResult2.release();
        }
        return mergedResult;
    }

    public List<SourceResult> getSortedSourceResults(List<SourceResult> list) {
        if (list.size() <= 1) {
            return list;
        }
        Collections.sort(list, new Comparator<SourceResult>() { // from class: com.miui.gallery.search.core.resultprocessor.ResultProcessor.1
            @Override // java.util.Comparator
            public int compare(SourceResult sourceResult, SourceResult sourceResult2) {
                if (sourceResult.getSource() == null) {
                    return 1;
                }
                if (sourceResult2.getSource() != null) {
                    return sourceResult.getSource().getPriority(sourceResult.getQueryInfo()) - sourceResult2.getSource().getPriority(sourceResult2.getQueryInfo());
                }
                return -1;
            }
        });
        return list;
    }

    public ErrorInfo getMergedErrorInfo(List<SourceResult> list) {
        return new SimpleErrorProcessor().process(list);
    }

    public BaseSuggestion toSuggestion(Suggestion suggestion) {
        if (suggestion == null) {
            return null;
        }
        if (suggestion instanceof BaseSuggestion) {
            return (BaseSuggestion) suggestion;
        }
        return new BaseSuggestion(suggestion.getSuggestionTitle(), suggestion.getSuggestionSubTitle(), suggestion.getSuggestionIcon(), suggestion.getIntentActionURI(), suggestion.getSuggestionExtras(), suggestion.getSuggestionSource(), suggestion.getBackupIcons());
    }
}
