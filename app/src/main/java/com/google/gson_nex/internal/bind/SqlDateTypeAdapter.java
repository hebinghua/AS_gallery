package com.google.gson_nex.internal.bind;

import com.google.gson_nex.Gson;
import com.google.gson_nex.JsonSyntaxException;
import com.google.gson_nex.TypeAdapter;
import com.google.gson_nex.TypeAdapterFactory;
import com.google.gson_nex.reflect.TypeToken;
import com.google.gson_nex.stream.JsonReader;
import com.google.gson_nex.stream.JsonToken;
import com.google.gson_nex.stream.JsonWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public final class SqlDateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() { // from class: com.google.gson_nex.internal.bind.SqlDateTypeAdapter.1
        @Override // com.google.gson_nex.TypeAdapterFactory
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Date.class) {
                return new SqlDateTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat format = new SimpleDateFormat("MMM d, yyyy");

    @Override // com.google.gson_nex.TypeAdapter
    /* renamed from: read  reason: collision with other method in class */
    public synchronized Date mo352read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        try {
            return new Date(this.format.parse(jsonReader.nextString()).getTime());
        } catch (ParseException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override // com.google.gson_nex.TypeAdapter
    public synchronized void write(JsonWriter jsonWriter, Date date) throws IOException {
        jsonWriter.value(date == null ? null : this.format.format((java.util.Date) date));
    }
}
