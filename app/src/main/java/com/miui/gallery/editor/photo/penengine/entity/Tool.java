package com.miui.gallery.editor.photo.penengine.entity;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class Tool {
    public String name;
    public ToolType toolType;

    public Tool(ToolType toolType) {
        this.toolType = toolType;
        this.name = toolType.getName();
    }

    public ToolType getToolType() {
        return this.toolType;
    }

    public String toString() {
        return "Tool{toolType=" + this.toolType + ", name='" + this.name + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    /* loaded from: classes2.dex */
    public enum ToolType {
        PEN("pen"),
        MARK("mark"),
        MOSAIC("mosaic"),
        ERASER("eraser"),
        TEXT("text"),
        SHAPE("shape");
        
        private final String name;

        ToolType(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }
    }
}
