package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: classes.dex */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        /* renamed from: build */
        MessageLite mo403build();

        /* renamed from: buildPartial */
        MessageLite mo414buildPartial();

        /* renamed from: clear */
        Builder mo405clear();

        /* renamed from: clone */
        Builder mo407clone();

        boolean mergeDelimitedFrom(InputStream inputStream) throws IOException;

        boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo354mergeFrom(ByteString byteString) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo355mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo356mergeFrom(CodedInputStream codedInputStream) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo409mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo358mergeFrom(MessageLite messageLite);

        /* renamed from: mergeFrom */
        Builder mo359mergeFrom(InputStream inputStream) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo360mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo361mergeFrom(byte[] bArr) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo410mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo411mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo364mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;
    }

    Parser<? extends MessageLite> getParserForType();

    int getSerializedSize();

    /* renamed from: newBuilderForType */
    Builder mo401newBuilderForType();

    /* renamed from: toBuilder */
    Builder mo402toBuilder();

    byte[] toByteArray();

    ByteString toByteString();

    void writeDelimitedTo(OutputStream outputStream) throws IOException;

    void writeTo(CodedOutputStream codedOutputStream) throws IOException;

    void writeTo(OutputStream outputStream) throws IOException;
}
