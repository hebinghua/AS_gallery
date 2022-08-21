package com.miui.gallery.net.library;

import ch.qos.logback.core.joran.action.Action;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.json.BaseJsonRequest;
import com.miui.gallery.util.BaseMiscUtil;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class LibraryRequest extends BaseGalleryRequest {
    public final long mLibraryId;

    public LibraryRequest(long j) {
        super(0, "https://i.mi.com/gallery/public/resource/info/v2");
        this.mLibraryId = j;
        addParam("nameSpace", "miui_assistant");
        addParam("group", String.valueOf(j));
        addParam(Action.KEY_ATTRIBUTE, LibraryStrategyUtils.getKeyForLibraryId(j));
        setUseCache(false);
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        try {
            Library library = (Library) BaseJsonRequest.fromJson(jSONObject.toString(), new TypeToken<Library>() { // from class: com.miui.gallery.net.library.LibraryRequest.1
            }.getType());
            if (library == null || !BaseMiscUtil.isValid(library.getLibraryItems())) {
                library = null;
            } else {
                library.setLibraryId(this.mLibraryId);
            }
            deliverResponse(library);
        } catch (JsonParseException e) {
            e.printStackTrace();
            deliverError(ErrorCode.PARSE_ERROR, e.getMessage(), jSONObject);
        } catch (Exception e2) {
            e2.printStackTrace();
            deliverError(ErrorCode.HANDLE_ERROR, e2.getMessage(), jSONObject);
        }
    }
}
