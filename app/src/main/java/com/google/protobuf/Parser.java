package com.google.protobuf;

import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface Parser<MessageType> {
    /* renamed from: parseDelimitedFrom */
    MessageType mo365parseDelimitedFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseDelimitedFrom */
    MessageType mo366parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo367parseFrom(ByteString byteString) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo368parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo369parseFrom(CodedInputStream codedInputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo370parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo371parseFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo372parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo373parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo374parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo375parseFrom(byte[] bArr) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo376parseFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo377parseFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo378parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialDelimitedFrom */
    MessageType mo379parsePartialDelimitedFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialDelimitedFrom */
    MessageType mo380parsePartialDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo381parsePartialFrom(ByteString byteString) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo382parsePartialFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo383parsePartialFrom(CodedInputStream codedInputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo413parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo384parsePartialFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo385parsePartialFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo386parsePartialFrom(byte[] bArr) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo387parsePartialFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo412parsePartialFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo389parsePartialFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;
}
