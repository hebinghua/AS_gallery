package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.TextManager;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import java.util.List;

/* loaded from: classes2.dex */
public class Text extends ActivableTool {
    public int currentSelectIndex;
    public List<TextData> textDataList;

    public Text() {
        super(Tool.ToolType.TEXT, R.drawable.screen_ic_tool_text_body, R.drawable.screen_ic_tool_text_shadow_select, R.drawable.screen_ic_tool_text_shadow_unselect);
        this.textDataList = TextManager.getScreenTextBubbleData();
    }

    public int getCurrentSelectIndex() {
        return this.currentSelectIndex;
    }

    public void setCurrentSelectIndex(int i) {
        this.currentSelectIndex = i;
    }

    public List<TextData> getTextDataList() {
        return this.textDataList;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Tool
    public String toString() {
        return "Text{currentSelectIndex=" + this.currentSelectIndex + ", textDataList=" + this.textDataList + '}';
    }
}
