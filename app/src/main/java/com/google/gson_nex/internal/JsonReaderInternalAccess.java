package com.google.gson_nex.internal;

import com.google.gson_nex.stream.JsonReader;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class JsonReaderInternalAccess {
    public static JsonReaderInternalAccess INSTANCE;

    public abstract void promoteNameToValue(JsonReader jsonReader) throws IOException;
}
