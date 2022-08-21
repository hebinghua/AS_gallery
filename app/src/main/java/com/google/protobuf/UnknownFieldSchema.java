package com.google.protobuf;

import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class UnknownFieldSchema<T, B> {
    public abstract void addFixed32(B b, int i, int i2);

    public abstract void addFixed64(B b, int i, long j);

    public abstract void addGroup(B b, int i, T t);

    public abstract void addLengthDelimited(B b, int i, ByteString byteString);

    public abstract void addVarint(B b, int i, long j);

    /* renamed from: getBuilderFromMessage */
    public abstract B mo436getBuilderFromMessage(Object obj);

    /* renamed from: getFromMessage */
    public abstract T mo437getFromMessage(Object obj);

    public abstract int getSerializedSize(T t);

    public abstract int getSerializedSizeAsMessageSet(T t);

    public abstract void makeImmutable(Object obj);

    public abstract T merge(T t, T t2);

    /* renamed from: newBuilder */
    public abstract B mo438newBuilder();

    public abstract void setBuilderToMessage(Object obj, B b);

    public abstract void setToMessage(Object obj, T t);

    public abstract boolean shouldDiscardUnknownFields(Reader reader);

    public abstract T toImmutable(B b);

    public abstract void writeAsMessageSetTo(T t, Writer writer) throws IOException;

    public abstract void writeTo(T t, Writer writer) throws IOException;

    public final boolean mergeOneFieldFrom(B b, Reader reader) throws IOException {
        int tag = reader.getTag();
        int tagFieldNumber = WireFormat.getTagFieldNumber(tag);
        int tagWireType = WireFormat.getTagWireType(tag);
        if (tagWireType == 0) {
            addVarint(b, tagFieldNumber, reader.readInt64());
            return true;
        } else if (tagWireType == 1) {
            addFixed64(b, tagFieldNumber, reader.readFixed64());
            return true;
        } else if (tagWireType == 2) {
            addLengthDelimited(b, tagFieldNumber, reader.readBytes());
            return true;
        } else if (tagWireType != 3) {
            if (tagWireType == 4) {
                return false;
            }
            if (tagWireType == 5) {
                addFixed32(b, tagFieldNumber, reader.readFixed32());
                return true;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        } else {
            B mo438newBuilder = mo438newBuilder();
            int makeTag = WireFormat.makeTag(tagFieldNumber, 4);
            mergeFrom(mo438newBuilder, reader);
            if (makeTag != reader.getTag()) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
            addGroup(b, tagFieldNumber, toImmutable(mo438newBuilder));
            return true;
        }
    }

    public final void mergeFrom(B b, Reader reader) throws IOException {
        while (reader.getFieldNumber() != Integer.MAX_VALUE && mergeOneFieldFrom(b, reader)) {
        }
    }
}
