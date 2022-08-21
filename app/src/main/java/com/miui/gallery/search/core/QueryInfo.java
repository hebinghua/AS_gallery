package com.miui.gallery.search.core;

import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* loaded from: classes2.dex */
public class QueryInfo {
    public Map<String, String> mParams = new HashMap();
    public SearchConstants.SearchType mSearchType;

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public String getParam(String str) {
        return this.mParams.get(str);
    }

    public SearchConstants.SearchType getSearchType() {
        return this.mSearchType;
    }

    public String toString() {
        return "search type = " + this.mSearchType + ", params = " + DefaultLogger.desensitizeMap(this.mParams, "query");
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean mAppendSerialInfo = false;
        public Map<String, String> mParams;
        public SearchConstants.SearchType mSearchType;

        public Builder() {
        }

        public Builder(SearchConstants.SearchType searchType) {
            if (searchType == null) {
                throw new IllegalArgumentException("Empty search type!");
            }
            this.mSearchType = searchType;
        }

        public Builder setSearchType(SearchConstants.SearchType searchType) {
            this.mSearchType = searchType;
            return this;
        }

        public Builder addParams(Map<String, String> map) {
            if (this.mParams == null) {
                this.mParams = new HashMap();
            }
            this.mParams.putAll(map);
            return this;
        }

        public Builder addParam(String str, String str2) {
            if (this.mParams == null) {
                this.mParams = new HashMap();
            }
            this.mParams.put(str, str2);
            return this;
        }

        public String removeParam(String str) {
            Map<String, String> map = this.mParams;
            if (map == null) {
                return null;
            }
            return map.remove(str);
        }

        public Builder setAppendSerialInfo(boolean z) {
            this.mAppendSerialInfo = z;
            return this;
        }

        public QueryInfo build() {
            if (this.mSearchType == null) {
                throw new IllegalArgumentException("Empty search type!");
            }
            return new QueryInfo(this);
        }

        public Builder cloneFrom(QueryInfo queryInfo) {
            this.mSearchType = queryInfo.mSearchType;
            this.mParams = new HashMap(queryInfo.mParams);
            return this;
        }
    }

    public QueryInfo(Builder builder) {
        this.mSearchType = builder.mSearchType;
        if (builder.mParams != null) {
            this.mParams.putAll(builder.mParams);
        }
        if (builder.mAppendSerialInfo) {
            this.mParams.put("serialId", SearchStatUtils.getCurrentSerial());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QueryInfo queryInfo = (QueryInfo) obj;
        return this.mSearchType == queryInfo.mSearchType && Objects.equals(this.mParams, queryInfo.mParams);
    }

    public int hashCode() {
        return Objects.hash(this.mSearchType, this.mParams);
    }
}
