package com.miui.gallery.backup;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class GalleryBackupProtos {

    /* loaded from: classes.dex */
    public interface BackupMessageOrBuilder extends MessageLiteOrBuilder {
        BackupMessage.AlbumProfile getAlbumProfiles(int i);

        int getAlbumProfilesCount();

        List<BackupMessage.AlbumProfile> getAlbumProfilesList();

        BackupMessage.CloudProfile getCloudProfiles(int i);

        int getCloudProfilesCount();

        List<BackupMessage.CloudProfile> getCloudProfilesList();

        BackupMessage.Settings getSettings();

        boolean hasSettings();
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }

    private GalleryBackupProtos() {
    }

    /* loaded from: classes.dex */
    public static final class BackupMessage extends GeneratedMessageLite<BackupMessage, Builder> implements BackupMessageOrBuilder {
        public static final int ALBUMPROFILES_FIELD_NUMBER = 2;
        public static final int CLOUDPROFILES_FIELD_NUMBER = 3;
        private static final BackupMessage DEFAULT_INSTANCE;
        private static volatile Parser<BackupMessage> PARSER = null;
        public static final int SETTINGS_FIELD_NUMBER = 1;
        private Internal.ProtobufList<AlbumProfile> albumProfiles_ = GeneratedMessageLite.emptyProtobufList();
        private Internal.ProtobufList<CloudProfile> cloudProfiles_ = GeneratedMessageLite.emptyProtobufList();
        private Settings settings_;

        /* loaded from: classes.dex */
        public interface AlbumProfileOrBuilder extends MessageLiteOrBuilder {
            long getAttributes();

            String getName();

            ByteString getNameBytes();

            String getPath();

            ByteString getPathBytes();
        }

        /* loaded from: classes.dex */
        public interface CloudProfileOrBuilder extends MessageLiteOrBuilder {
            long getDateTaken();

            long getDuration();

            String getExifAperture();

            ByteString getExifApertureBytes();

            String getExifDateTime();

            ByteString getExifDateTimeBytes();

            String getExifExposureTime();

            ByteString getExifExposureTimeBytes();

            int getExifFlash();

            String getExifFocalLength();

            ByteString getExifFocalLengthBytes();

            String getExifGpsAltitude();

            ByteString getExifGpsAltitudeBytes();

            int getExifGpsAltitudeRef();

            String getExifGpsDateStamp();

            ByteString getExifGpsDateStampBytes();

            String getExifGpsProcessingMethod();

            ByteString getExifGpsProcessingMethodBytes();

            String getExifGpsTimeStamp();

            ByteString getExifGpsTimeStampBytes();

            String getExifISO();

            ByteString getExifISOBytes();

            String getExifMake();

            ByteString getExifMakeBytes();

            String getExifModel();

            ByteString getExifModelBytes();

            int getExifWhiteBalance();

            int getHeight();

            String getLatitude();

            ByteString getLatitudeBytes();

            String getLatitudeRef();

            ByteString getLatitudeRefBytes();

            String getLocation();

            ByteString getLocationBytes();

            String getLongitude();

            ByteString getLongitudeBytes();

            String getLongitudeRef();

            ByteString getLongitudeRefBytes();

            int getOrientation();

            String getPath();

            ByteString getPathBytes();

            String getSha1();

            ByteString getSha1Bytes();

            long getSpecialTypeFlags();

            int getWidth();
        }

        /* loaded from: classes.dex */
        public interface SettingsOrBuilder extends MessageLiteOrBuilder {
            BoolValue getAutoConvertHeifToJpegEnable();

            BoolValue getMemoriesEnable();

            boolean getOnlyShowLocalPhoto();

            @Deprecated
            boolean getRemindConnectNetworkEveryTime();

            BoolValue getSelectBestPhotoEnable();

            @Deprecated
            boolean getShowHiddenAlbum();

            BoolValue getSlideShowLoop();

            int getSlideshowInterval();

            boolean hasAutoConvertHeifToJpegEnable();

            boolean hasMemoriesEnable();

            boolean hasSelectBestPhotoEnable();

            boolean hasSlideShowLoop();
        }

        private BackupMessage() {
        }

        /* loaded from: classes.dex */
        public static final class Settings extends GeneratedMessageLite<Settings, Builder> implements SettingsOrBuilder {
            public static final int AUTOCONVERTHEIFTOJPEGENABLE_FIELD_NUMBER = 7;
            private static final Settings DEFAULT_INSTANCE;
            public static final int MEMORIESENABLE_FIELD_NUMBER = 6;
            public static final int ONLYSHOWLOCALPHOTO_FIELD_NUMBER = 1;
            private static volatile Parser<Settings> PARSER = null;
            public static final int REMINDCONNECTNETWORKEVERYTIME_FIELD_NUMBER = 4;
            public static final int SELECTBESTPHOTOENABLE_FIELD_NUMBER = 5;
            public static final int SHOWHIDDENALBUM_FIELD_NUMBER = 2;
            public static final int SLIDESHOWINTERVAL_FIELD_NUMBER = 3;
            public static final int SLIDESHOWLOOP_FIELD_NUMBER = 8;
            private BoolValue autoConvertHeifToJpegEnable_;
            private BoolValue memoriesEnable_;
            private boolean onlyShowLocalPhoto_;
            private boolean remindConnectNetworkEveryTime_;
            private BoolValue selectBestPhotoEnable_;
            private boolean showHiddenAlbum_;
            private BoolValue slideShowLoop_;
            private int slideshowInterval_;

            private Settings() {
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public boolean getOnlyShowLocalPhoto() {
                return this.onlyShowLocalPhoto_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setOnlyShowLocalPhoto(boolean z) {
                this.onlyShowLocalPhoto_ = z;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearOnlyShowLocalPhoto() {
                this.onlyShowLocalPhoto_ = false;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            @Deprecated
            public boolean getShowHiddenAlbum() {
                return this.showHiddenAlbum_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setShowHiddenAlbum(boolean z) {
                this.showHiddenAlbum_ = z;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearShowHiddenAlbum() {
                this.showHiddenAlbum_ = false;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public int getSlideshowInterval() {
                return this.slideshowInterval_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSlideshowInterval(int i) {
                this.slideshowInterval_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearSlideshowInterval() {
                this.slideshowInterval_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            @Deprecated
            public boolean getRemindConnectNetworkEveryTime() {
                return this.remindConnectNetworkEveryTime_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setRemindConnectNetworkEveryTime(boolean z) {
                this.remindConnectNetworkEveryTime_ = z;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearRemindConnectNetworkEveryTime() {
                this.remindConnectNetworkEveryTime_ = false;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public boolean hasSelectBestPhotoEnable() {
                return this.selectBestPhotoEnable_ != null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public BoolValue getSelectBestPhotoEnable() {
                BoolValue boolValue = this.selectBestPhotoEnable_;
                return boolValue == null ? BoolValue.getDefaultInstance() : boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSelectBestPhotoEnable(BoolValue boolValue) {
                boolValue.getClass();
                this.selectBestPhotoEnable_ = boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void mergeSelectBestPhotoEnable(BoolValue boolValue) {
                boolValue.getClass();
                BoolValue boolValue2 = this.selectBestPhotoEnable_;
                if (boolValue2 != null && boolValue2 != BoolValue.getDefaultInstance()) {
                    this.selectBestPhotoEnable_ = BoolValue.newBuilder(this.selectBestPhotoEnable_).mergeFrom((BoolValue.Builder) boolValue).mo414buildPartial();
                } else {
                    this.selectBestPhotoEnable_ = boolValue;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearSelectBestPhotoEnable() {
                this.selectBestPhotoEnable_ = null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public boolean hasMemoriesEnable() {
                return this.memoriesEnable_ != null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public BoolValue getMemoriesEnable() {
                BoolValue boolValue = this.memoriesEnable_;
                return boolValue == null ? BoolValue.getDefaultInstance() : boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setMemoriesEnable(BoolValue boolValue) {
                boolValue.getClass();
                this.memoriesEnable_ = boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void mergeMemoriesEnable(BoolValue boolValue) {
                boolValue.getClass();
                BoolValue boolValue2 = this.memoriesEnable_;
                if (boolValue2 != null && boolValue2 != BoolValue.getDefaultInstance()) {
                    this.memoriesEnable_ = BoolValue.newBuilder(this.memoriesEnable_).mergeFrom((BoolValue.Builder) boolValue).mo414buildPartial();
                } else {
                    this.memoriesEnable_ = boolValue;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearMemoriesEnable() {
                this.memoriesEnable_ = null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public boolean hasAutoConvertHeifToJpegEnable() {
                return this.autoConvertHeifToJpegEnable_ != null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public BoolValue getAutoConvertHeifToJpegEnable() {
                BoolValue boolValue = this.autoConvertHeifToJpegEnable_;
                return boolValue == null ? BoolValue.getDefaultInstance() : boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setAutoConvertHeifToJpegEnable(BoolValue boolValue) {
                boolValue.getClass();
                this.autoConvertHeifToJpegEnable_ = boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void mergeAutoConvertHeifToJpegEnable(BoolValue boolValue) {
                boolValue.getClass();
                BoolValue boolValue2 = this.autoConvertHeifToJpegEnable_;
                if (boolValue2 != null && boolValue2 != BoolValue.getDefaultInstance()) {
                    this.autoConvertHeifToJpegEnable_ = BoolValue.newBuilder(this.autoConvertHeifToJpegEnable_).mergeFrom((BoolValue.Builder) boolValue).mo414buildPartial();
                } else {
                    this.autoConvertHeifToJpegEnable_ = boolValue;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearAutoConvertHeifToJpegEnable() {
                this.autoConvertHeifToJpegEnable_ = null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public boolean hasSlideShowLoop() {
                return this.slideShowLoop_ != null;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
            public BoolValue getSlideShowLoop() {
                BoolValue boolValue = this.slideShowLoop_;
                return boolValue == null ? BoolValue.getDefaultInstance() : boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSlideShowLoop(BoolValue boolValue) {
                boolValue.getClass();
                this.slideShowLoop_ = boolValue;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void mergeSlideShowLoop(BoolValue boolValue) {
                boolValue.getClass();
                BoolValue boolValue2 = this.slideShowLoop_;
                if (boolValue2 != null && boolValue2 != BoolValue.getDefaultInstance()) {
                    this.slideShowLoop_ = BoolValue.newBuilder(this.slideShowLoop_).mergeFrom((BoolValue.Builder) boolValue).mo414buildPartial();
                } else {
                    this.slideShowLoop_ = boolValue;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearSlideShowLoop() {
                this.slideShowLoop_ = null;
            }

            public static Settings parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
            }

            public static Settings parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
            }

            public static Settings parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
            }

            public static Settings parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
            }

            public static Settings parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
            }

            public static Settings parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
            }

            public static Settings parseFrom(InputStream inputStream) throws IOException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static Settings parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static Settings parseDelimitedFrom(InputStream inputStream) throws IOException {
                return (Settings) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static Settings parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (Settings) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static Settings parseFrom(CodedInputStream codedInputStream) throws IOException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
            }

            public static Settings parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (Settings) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
            }

            public static Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static Builder newBuilder(Settings settings) {
                return DEFAULT_INSTANCE.createBuilder(settings);
            }

            /* loaded from: classes.dex */
            public static final class Builder extends GeneratedMessageLite.Builder<Settings, Builder> implements SettingsOrBuilder {
                public /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
                    this();
                }

                private Builder() {
                    super(Settings.DEFAULT_INSTANCE);
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public boolean getOnlyShowLocalPhoto() {
                    return ((Settings) this.instance).getOnlyShowLocalPhoto();
                }

                public Builder setOnlyShowLocalPhoto(boolean z) {
                    copyOnWrite();
                    ((Settings) this.instance).setOnlyShowLocalPhoto(z);
                    return this;
                }

                public Builder clearOnlyShowLocalPhoto() {
                    copyOnWrite();
                    ((Settings) this.instance).clearOnlyShowLocalPhoto();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                @Deprecated
                public boolean getShowHiddenAlbum() {
                    return ((Settings) this.instance).getShowHiddenAlbum();
                }

                @Deprecated
                public Builder setShowHiddenAlbum(boolean z) {
                    copyOnWrite();
                    ((Settings) this.instance).setShowHiddenAlbum(z);
                    return this;
                }

                @Deprecated
                public Builder clearShowHiddenAlbum() {
                    copyOnWrite();
                    ((Settings) this.instance).clearShowHiddenAlbum();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public int getSlideshowInterval() {
                    return ((Settings) this.instance).getSlideshowInterval();
                }

                public Builder setSlideshowInterval(int i) {
                    copyOnWrite();
                    ((Settings) this.instance).setSlideshowInterval(i);
                    return this;
                }

                public Builder clearSlideshowInterval() {
                    copyOnWrite();
                    ((Settings) this.instance).clearSlideshowInterval();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                @Deprecated
                public boolean getRemindConnectNetworkEveryTime() {
                    return ((Settings) this.instance).getRemindConnectNetworkEveryTime();
                }

                @Deprecated
                public Builder setRemindConnectNetworkEveryTime(boolean z) {
                    copyOnWrite();
                    ((Settings) this.instance).setRemindConnectNetworkEveryTime(z);
                    return this;
                }

                @Deprecated
                public Builder clearRemindConnectNetworkEveryTime() {
                    copyOnWrite();
                    ((Settings) this.instance).clearRemindConnectNetworkEveryTime();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public boolean hasSelectBestPhotoEnable() {
                    return ((Settings) this.instance).hasSelectBestPhotoEnable();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public BoolValue getSelectBestPhotoEnable() {
                    return ((Settings) this.instance).getSelectBestPhotoEnable();
                }

                public Builder setSelectBestPhotoEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).setSelectBestPhotoEnable(boolValue);
                    return this;
                }

                public Builder setSelectBestPhotoEnable(BoolValue.Builder builder) {
                    copyOnWrite();
                    ((Settings) this.instance).setSelectBestPhotoEnable(builder.mo403build());
                    return this;
                }

                public Builder mergeSelectBestPhotoEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).mergeSelectBestPhotoEnable(boolValue);
                    return this;
                }

                public Builder clearSelectBestPhotoEnable() {
                    copyOnWrite();
                    ((Settings) this.instance).clearSelectBestPhotoEnable();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public boolean hasMemoriesEnable() {
                    return ((Settings) this.instance).hasMemoriesEnable();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public BoolValue getMemoriesEnable() {
                    return ((Settings) this.instance).getMemoriesEnable();
                }

                public Builder setMemoriesEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).setMemoriesEnable(boolValue);
                    return this;
                }

                public Builder setMemoriesEnable(BoolValue.Builder builder) {
                    copyOnWrite();
                    ((Settings) this.instance).setMemoriesEnable(builder.mo403build());
                    return this;
                }

                public Builder mergeMemoriesEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).mergeMemoriesEnable(boolValue);
                    return this;
                }

                public Builder clearMemoriesEnable() {
                    copyOnWrite();
                    ((Settings) this.instance).clearMemoriesEnable();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public boolean hasAutoConvertHeifToJpegEnable() {
                    return ((Settings) this.instance).hasAutoConvertHeifToJpegEnable();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public BoolValue getAutoConvertHeifToJpegEnable() {
                    return ((Settings) this.instance).getAutoConvertHeifToJpegEnable();
                }

                public Builder setAutoConvertHeifToJpegEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).setAutoConvertHeifToJpegEnable(boolValue);
                    return this;
                }

                public Builder setAutoConvertHeifToJpegEnable(BoolValue.Builder builder) {
                    copyOnWrite();
                    ((Settings) this.instance).setAutoConvertHeifToJpegEnable(builder.mo403build());
                    return this;
                }

                public Builder mergeAutoConvertHeifToJpegEnable(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).mergeAutoConvertHeifToJpegEnable(boolValue);
                    return this;
                }

                public Builder clearAutoConvertHeifToJpegEnable() {
                    copyOnWrite();
                    ((Settings) this.instance).clearAutoConvertHeifToJpegEnable();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public boolean hasSlideShowLoop() {
                    return ((Settings) this.instance).hasSlideShowLoop();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.SettingsOrBuilder
                public BoolValue getSlideShowLoop() {
                    return ((Settings) this.instance).getSlideShowLoop();
                }

                public Builder setSlideShowLoop(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).setSlideShowLoop(boolValue);
                    return this;
                }

                public Builder setSlideShowLoop(BoolValue.Builder builder) {
                    copyOnWrite();
                    ((Settings) this.instance).setSlideShowLoop(builder.mo403build());
                    return this;
                }

                public Builder mergeSlideShowLoop(BoolValue boolValue) {
                    copyOnWrite();
                    ((Settings) this.instance).mergeSlideShowLoop(boolValue);
                    return this;
                }

                public Builder clearSlideShowLoop() {
                    copyOnWrite();
                    ((Settings) this.instance).clearSlideShowLoop();
                    return this;
                }
            }

            @Override // com.google.protobuf.GeneratedMessageLite
            public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
                switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                    case 1:
                        return new Settings();
                    case 2:
                        return new Builder(null);
                    case 3:
                        return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0000\b\u0000\u0000\u0001\b\b\u0000\u0000\u0000\u0001\u0007\u0002\u0007\u0003\u0004\u0004\u0007\u0005\t\u0006\t\u0007\t\b\t", new Object[]{"onlyShowLocalPhoto_", "showHiddenAlbum_", "slideshowInterval_", "remindConnectNetworkEveryTime_", "selectBestPhotoEnable_", "memoriesEnable_", "autoConvertHeifToJpegEnable_", "slideShowLoop_"});
                    case 4:
                        return DEFAULT_INSTANCE;
                    case 5:
                        Parser<Settings> parser = PARSER;
                        if (parser == null) {
                            synchronized (Settings.class) {
                                parser = PARSER;
                                if (parser == null) {
                                    parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                                    PARSER = parser;
                                }
                            }
                        }
                        return parser;
                    case 6:
                        return (byte) 1;
                    case 7:
                        return null;
                    default:
                        throw new UnsupportedOperationException();
                }
            }

            static {
                Settings settings = new Settings();
                DEFAULT_INSTANCE = settings;
                GeneratedMessageLite.registerDefaultInstance(Settings.class, settings);
            }

            public static Settings getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<Settings> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }
        }

        /* loaded from: classes.dex */
        public static final class AlbumProfile extends GeneratedMessageLite<AlbumProfile, Builder> implements AlbumProfileOrBuilder {
            public static final int ATTRIBUTES_FIELD_NUMBER = 3;
            private static final AlbumProfile DEFAULT_INSTANCE;
            public static final int NAME_FIELD_NUMBER = 2;
            private static volatile Parser<AlbumProfile> PARSER = null;
            public static final int PATH_FIELD_NUMBER = 1;
            private long attributes_;
            private String path_ = "";
            private String name_ = "";

            private AlbumProfile() {
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
            public String getPath() {
                return this.path_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
            public ByteString getPathBytes() {
                return ByteString.copyFromUtf8(this.path_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setPath(String str) {
                str.getClass();
                this.path_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearPath() {
                this.path_ = getDefaultInstance().getPath();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setPathBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.path_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
            public String getName() {
                return this.name_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
            public ByteString getNameBytes() {
                return ByteString.copyFromUtf8(this.name_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setName(String str) {
                str.getClass();
                this.name_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearName() {
                this.name_ = getDefaultInstance().getName();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setNameBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.name_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
            public long getAttributes() {
                return this.attributes_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setAttributes(long j) {
                this.attributes_ = j;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearAttributes() {
                this.attributes_ = 0L;
            }

            public static AlbumProfile parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
            }

            public static AlbumProfile parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
            }

            public static AlbumProfile parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
            }

            public static AlbumProfile parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
            }

            public static AlbumProfile parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
            }

            public static AlbumProfile parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
            }

            public static AlbumProfile parseFrom(InputStream inputStream) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static AlbumProfile parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static AlbumProfile parseDelimitedFrom(InputStream inputStream) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static AlbumProfile parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static AlbumProfile parseFrom(CodedInputStream codedInputStream) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
            }

            public static AlbumProfile parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (AlbumProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
            }

            public static Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static Builder newBuilder(AlbumProfile albumProfile) {
                return DEFAULT_INSTANCE.createBuilder(albumProfile);
            }

            /* loaded from: classes.dex */
            public static final class Builder extends GeneratedMessageLite.Builder<AlbumProfile, Builder> implements AlbumProfileOrBuilder {
                public /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
                    this();
                }

                private Builder() {
                    super(AlbumProfile.DEFAULT_INSTANCE);
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
                public String getPath() {
                    return ((AlbumProfile) this.instance).getPath();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
                public ByteString getPathBytes() {
                    return ((AlbumProfile) this.instance).getPathBytes();
                }

                public Builder setPath(String str) {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).setPath(str);
                    return this;
                }

                public Builder clearPath() {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).clearPath();
                    return this;
                }

                public Builder setPathBytes(ByteString byteString) {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).setPathBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
                public String getName() {
                    return ((AlbumProfile) this.instance).getName();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
                public ByteString getNameBytes() {
                    return ((AlbumProfile) this.instance).getNameBytes();
                }

                public Builder setName(String str) {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).setName(str);
                    return this;
                }

                public Builder clearName() {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).clearName();
                    return this;
                }

                public Builder setNameBytes(ByteString byteString) {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).setNameBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.AlbumProfileOrBuilder
                public long getAttributes() {
                    return ((AlbumProfile) this.instance).getAttributes();
                }

                public Builder setAttributes(long j) {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).setAttributes(j);
                    return this;
                }

                public Builder clearAttributes() {
                    copyOnWrite();
                    ((AlbumProfile) this.instance).clearAttributes();
                    return this;
                }
            }

            @Override // com.google.protobuf.GeneratedMessageLite
            public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
                switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                    case 1:
                        return new AlbumProfile();
                    case 2:
                        return new Builder(null);
                    case 3:
                        return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003\u0002", new Object[]{"path_", "name_", "attributes_"});
                    case 4:
                        return DEFAULT_INSTANCE;
                    case 5:
                        Parser<AlbumProfile> parser = PARSER;
                        if (parser == null) {
                            synchronized (AlbumProfile.class) {
                                parser = PARSER;
                                if (parser == null) {
                                    parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                                    PARSER = parser;
                                }
                            }
                        }
                        return parser;
                    case 6:
                        return (byte) 1;
                    case 7:
                        return null;
                    default:
                        throw new UnsupportedOperationException();
                }
            }

            static {
                AlbumProfile albumProfile = new AlbumProfile();
                DEFAULT_INSTANCE = albumProfile;
                GeneratedMessageLite.registerDefaultInstance(AlbumProfile.class, albumProfile);
            }

            public static AlbumProfile getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<AlbumProfile> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }
        }

        /* loaded from: classes.dex */
        public static final class CloudProfile extends GeneratedMessageLite<CloudProfile, Builder> implements CloudProfileOrBuilder {
            public static final int DATETAKEN_FIELD_NUMBER = 6;
            private static final CloudProfile DEFAULT_INSTANCE;
            public static final int DURATION_FIELD_NUMBER = 22;
            public static final int EXIFAPERTURE_FIELD_NUMBER = 12;
            public static final int EXIFDATETIME_FIELD_NUMBER = 21;
            public static final int EXIFEXPOSURETIME_FIELD_NUMBER = 11;
            public static final int EXIFFLASH_FIELD_NUMBER = 10;
            public static final int EXIFFOCALLENGTH_FIELD_NUMBER = 17;
            public static final int EXIFGPSALTITUDEREF_FIELD_NUMBER = 16;
            public static final int EXIFGPSALTITUDE_FIELD_NUMBER = 15;
            public static final int EXIFGPSDATESTAMP_FIELD_NUMBER = 20;
            public static final int EXIFGPSPROCESSINGMETHOD_FIELD_NUMBER = 18;
            public static final int EXIFGPSTIMESTAMP_FIELD_NUMBER = 19;
            public static final int EXIFISO_FIELD_NUMBER = 13;
            public static final int EXIFMAKE_FIELD_NUMBER = 9;
            public static final int EXIFMODEL_FIELD_NUMBER = 8;
            public static final int EXIFWHITEBALANCE_FIELD_NUMBER = 14;
            public static final int HEIGHT_FIELD_NUMBER = 4;
            public static final int LATITUDEREF_FIELD_NUMBER = 24;
            public static final int LATITUDE_FIELD_NUMBER = 23;
            public static final int LOCATION_FIELD_NUMBER = 27;
            public static final int LONGITUDEREF_FIELD_NUMBER = 26;
            public static final int LONGITUDE_FIELD_NUMBER = 25;
            public static final int ORIENTATION_FIELD_NUMBER = 5;
            private static volatile Parser<CloudProfile> PARSER = null;
            public static final int PATH_FIELD_NUMBER = 1;
            public static final int SHA1_FIELD_NUMBER = 2;
            public static final int SPECIALTYPEFLAGS_FIELD_NUMBER = 7;
            public static final int WIDTH_FIELD_NUMBER = 3;
            private long dateTaken_;
            private long duration_;
            private int exifFlash_;
            private int exifGpsAltitudeRef_;
            private int exifWhiteBalance_;
            private int height_;
            private int orientation_;
            private long specialTypeFlags_;
            private int width_;
            private String path_ = "";
            private String sha1_ = "";
            private String exifModel_ = "";
            private String exifMake_ = "";
            private String exifExposureTime_ = "";
            private String exifAperture_ = "";
            private String exifISO_ = "";
            private String exifGpsAltitude_ = "";
            private String exifFocalLength_ = "";
            private String exifGpsProcessingMethod_ = "";
            private String exifGpsTimeStamp_ = "";
            private String exifGpsDateStamp_ = "";
            private String exifDateTime_ = "";
            private String latitude_ = "";
            private String latitudeRef_ = "";
            private String longitude_ = "";
            private String longitudeRef_ = "";
            private String location_ = "";

            private CloudProfile() {
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getPath() {
                return this.path_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getPathBytes() {
                return ByteString.copyFromUtf8(this.path_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setPath(String str) {
                str.getClass();
                this.path_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearPath() {
                this.path_ = getDefaultInstance().getPath();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setPathBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.path_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getSha1() {
                return this.sha1_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getSha1Bytes() {
                return ByteString.copyFromUtf8(this.sha1_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSha1(String str) {
                str.getClass();
                this.sha1_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearSha1() {
                this.sha1_ = getDefaultInstance().getSha1();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSha1Bytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.sha1_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getWidth() {
                return this.width_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setWidth(int i) {
                this.width_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearWidth() {
                this.width_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getHeight() {
                return this.height_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setHeight(int i) {
                this.height_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearHeight() {
                this.height_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getOrientation() {
                return this.orientation_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setOrientation(int i) {
                this.orientation_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearOrientation() {
                this.orientation_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public long getDateTaken() {
                return this.dateTaken_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setDateTaken(long j) {
                this.dateTaken_ = j;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearDateTaken() {
                this.dateTaken_ = 0L;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public long getSpecialTypeFlags() {
                return this.specialTypeFlags_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setSpecialTypeFlags(long j) {
                this.specialTypeFlags_ = j;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearSpecialTypeFlags() {
                this.specialTypeFlags_ = 0L;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifModel() {
                return this.exifModel_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifModelBytes() {
                return ByteString.copyFromUtf8(this.exifModel_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifModel(String str) {
                str.getClass();
                this.exifModel_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifModel() {
                this.exifModel_ = getDefaultInstance().getExifModel();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifModelBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifModel_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifMake() {
                return this.exifMake_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifMakeBytes() {
                return ByteString.copyFromUtf8(this.exifMake_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifMake(String str) {
                str.getClass();
                this.exifMake_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifMake() {
                this.exifMake_ = getDefaultInstance().getExifMake();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifMakeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifMake_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getExifFlash() {
                return this.exifFlash_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifFlash(int i) {
                this.exifFlash_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifFlash() {
                this.exifFlash_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifExposureTime() {
                return this.exifExposureTime_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifExposureTimeBytes() {
                return ByteString.copyFromUtf8(this.exifExposureTime_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifExposureTime(String str) {
                str.getClass();
                this.exifExposureTime_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifExposureTime() {
                this.exifExposureTime_ = getDefaultInstance().getExifExposureTime();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifExposureTimeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifExposureTime_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifAperture() {
                return this.exifAperture_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifApertureBytes() {
                return ByteString.copyFromUtf8(this.exifAperture_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifAperture(String str) {
                str.getClass();
                this.exifAperture_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifAperture() {
                this.exifAperture_ = getDefaultInstance().getExifAperture();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifApertureBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifAperture_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifISO() {
                return this.exifISO_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifISOBytes() {
                return ByteString.copyFromUtf8(this.exifISO_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifISO(String str) {
                str.getClass();
                this.exifISO_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifISO() {
                this.exifISO_ = getDefaultInstance().getExifISO();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifISOBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifISO_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getExifWhiteBalance() {
                return this.exifWhiteBalance_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifWhiteBalance(int i) {
                this.exifWhiteBalance_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifWhiteBalance() {
                this.exifWhiteBalance_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifGpsAltitude() {
                return this.exifGpsAltitude_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifGpsAltitudeBytes() {
                return ByteString.copyFromUtf8(this.exifGpsAltitude_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsAltitude(String str) {
                str.getClass();
                this.exifGpsAltitude_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifGpsAltitude() {
                this.exifGpsAltitude_ = getDefaultInstance().getExifGpsAltitude();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsAltitudeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifGpsAltitude_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public int getExifGpsAltitudeRef() {
                return this.exifGpsAltitudeRef_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsAltitudeRef(int i) {
                this.exifGpsAltitudeRef_ = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifGpsAltitudeRef() {
                this.exifGpsAltitudeRef_ = 0;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifFocalLength() {
                return this.exifFocalLength_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifFocalLengthBytes() {
                return ByteString.copyFromUtf8(this.exifFocalLength_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifFocalLength(String str) {
                str.getClass();
                this.exifFocalLength_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifFocalLength() {
                this.exifFocalLength_ = getDefaultInstance().getExifFocalLength();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifFocalLengthBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifFocalLength_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifGpsProcessingMethod() {
                return this.exifGpsProcessingMethod_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifGpsProcessingMethodBytes() {
                return ByteString.copyFromUtf8(this.exifGpsProcessingMethod_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsProcessingMethod(String str) {
                str.getClass();
                this.exifGpsProcessingMethod_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifGpsProcessingMethod() {
                this.exifGpsProcessingMethod_ = getDefaultInstance().getExifGpsProcessingMethod();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsProcessingMethodBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifGpsProcessingMethod_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifGpsTimeStamp() {
                return this.exifGpsTimeStamp_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifGpsTimeStampBytes() {
                return ByteString.copyFromUtf8(this.exifGpsTimeStamp_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsTimeStamp(String str) {
                str.getClass();
                this.exifGpsTimeStamp_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifGpsTimeStamp() {
                this.exifGpsTimeStamp_ = getDefaultInstance().getExifGpsTimeStamp();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsTimeStampBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifGpsTimeStamp_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifGpsDateStamp() {
                return this.exifGpsDateStamp_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifGpsDateStampBytes() {
                return ByteString.copyFromUtf8(this.exifGpsDateStamp_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsDateStamp(String str) {
                str.getClass();
                this.exifGpsDateStamp_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifGpsDateStamp() {
                this.exifGpsDateStamp_ = getDefaultInstance().getExifGpsDateStamp();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifGpsDateStampBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifGpsDateStamp_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getExifDateTime() {
                return this.exifDateTime_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getExifDateTimeBytes() {
                return ByteString.copyFromUtf8(this.exifDateTime_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifDateTime(String str) {
                str.getClass();
                this.exifDateTime_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearExifDateTime() {
                this.exifDateTime_ = getDefaultInstance().getExifDateTime();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExifDateTimeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.exifDateTime_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public long getDuration() {
                return this.duration_;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setDuration(long j) {
                this.duration_ = j;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearDuration() {
                this.duration_ = 0L;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getLatitude() {
                return this.latitude_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getLatitudeBytes() {
                return ByteString.copyFromUtf8(this.latitude_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLatitude(String str) {
                str.getClass();
                this.latitude_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearLatitude() {
                this.latitude_ = getDefaultInstance().getLatitude();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLatitudeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.latitude_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getLatitudeRef() {
                return this.latitudeRef_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getLatitudeRefBytes() {
                return ByteString.copyFromUtf8(this.latitudeRef_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLatitudeRef(String str) {
                str.getClass();
                this.latitudeRef_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearLatitudeRef() {
                this.latitudeRef_ = getDefaultInstance().getLatitudeRef();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLatitudeRefBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.latitudeRef_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getLongitude() {
                return this.longitude_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getLongitudeBytes() {
                return ByteString.copyFromUtf8(this.longitude_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLongitude(String str) {
                str.getClass();
                this.longitude_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearLongitude() {
                this.longitude_ = getDefaultInstance().getLongitude();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLongitudeBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.longitude_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getLongitudeRef() {
                return this.longitudeRef_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getLongitudeRefBytes() {
                return ByteString.copyFromUtf8(this.longitudeRef_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLongitudeRef(String str) {
                str.getClass();
                this.longitudeRef_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearLongitudeRef() {
                this.longitudeRef_ = getDefaultInstance().getLongitudeRef();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLongitudeRefBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.longitudeRef_ = byteString.toStringUtf8();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public String getLocation() {
                return this.location_;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
            public ByteString getLocationBytes() {
                return ByteString.copyFromUtf8(this.location_);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLocation(String str) {
                str.getClass();
                this.location_ = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void clearLocation() {
                this.location_ = getDefaultInstance().getLocation();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setLocationBytes(ByteString byteString) {
                AbstractMessageLite.checkByteStringIsUtf8(byteString);
                this.location_ = byteString.toStringUtf8();
            }

            public static CloudProfile parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
            }

            public static CloudProfile parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
            }

            public static CloudProfile parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
            }

            public static CloudProfile parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
            }

            public static CloudProfile parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
            }

            public static CloudProfile parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
            }

            public static CloudProfile parseFrom(InputStream inputStream) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static CloudProfile parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static CloudProfile parseDelimitedFrom(InputStream inputStream) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
            }

            public static CloudProfile parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
            }

            public static CloudProfile parseFrom(CodedInputStream codedInputStream) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
            }

            public static CloudProfile parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return (CloudProfile) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
            }

            public static Builder newBuilder() {
                return DEFAULT_INSTANCE.createBuilder();
            }

            public static Builder newBuilder(CloudProfile cloudProfile) {
                return DEFAULT_INSTANCE.createBuilder(cloudProfile);
            }

            /* loaded from: classes.dex */
            public static final class Builder extends GeneratedMessageLite.Builder<CloudProfile, Builder> implements CloudProfileOrBuilder {
                public /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
                    this();
                }

                private Builder() {
                    super(CloudProfile.DEFAULT_INSTANCE);
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getPath() {
                    return ((CloudProfile) this.instance).getPath();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getPathBytes() {
                    return ((CloudProfile) this.instance).getPathBytes();
                }

                public Builder setPath(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setPath(str);
                    return this;
                }

                public Builder clearPath() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearPath();
                    return this;
                }

                public Builder setPathBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setPathBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getSha1() {
                    return ((CloudProfile) this.instance).getSha1();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getSha1Bytes() {
                    return ((CloudProfile) this.instance).getSha1Bytes();
                }

                public Builder setSha1(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setSha1(str);
                    return this;
                }

                public Builder clearSha1() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearSha1();
                    return this;
                }

                public Builder setSha1Bytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setSha1Bytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getWidth() {
                    return ((CloudProfile) this.instance).getWidth();
                }

                public Builder setWidth(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setWidth(i);
                    return this;
                }

                public Builder clearWidth() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearWidth();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getHeight() {
                    return ((CloudProfile) this.instance).getHeight();
                }

                public Builder setHeight(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setHeight(i);
                    return this;
                }

                public Builder clearHeight() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearHeight();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getOrientation() {
                    return ((CloudProfile) this.instance).getOrientation();
                }

                public Builder setOrientation(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setOrientation(i);
                    return this;
                }

                public Builder clearOrientation() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearOrientation();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public long getDateTaken() {
                    return ((CloudProfile) this.instance).getDateTaken();
                }

                public Builder setDateTaken(long j) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setDateTaken(j);
                    return this;
                }

                public Builder clearDateTaken() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearDateTaken();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public long getSpecialTypeFlags() {
                    return ((CloudProfile) this.instance).getSpecialTypeFlags();
                }

                public Builder setSpecialTypeFlags(long j) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setSpecialTypeFlags(j);
                    return this;
                }

                public Builder clearSpecialTypeFlags() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearSpecialTypeFlags();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifModel() {
                    return ((CloudProfile) this.instance).getExifModel();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifModelBytes() {
                    return ((CloudProfile) this.instance).getExifModelBytes();
                }

                public Builder setExifModel(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifModel(str);
                    return this;
                }

                public Builder clearExifModel() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifModel();
                    return this;
                }

                public Builder setExifModelBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifModelBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifMake() {
                    return ((CloudProfile) this.instance).getExifMake();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifMakeBytes() {
                    return ((CloudProfile) this.instance).getExifMakeBytes();
                }

                public Builder setExifMake(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifMake(str);
                    return this;
                }

                public Builder clearExifMake() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifMake();
                    return this;
                }

                public Builder setExifMakeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifMakeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getExifFlash() {
                    return ((CloudProfile) this.instance).getExifFlash();
                }

                public Builder setExifFlash(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifFlash(i);
                    return this;
                }

                public Builder clearExifFlash() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifFlash();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifExposureTime() {
                    return ((CloudProfile) this.instance).getExifExposureTime();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifExposureTimeBytes() {
                    return ((CloudProfile) this.instance).getExifExposureTimeBytes();
                }

                public Builder setExifExposureTime(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifExposureTime(str);
                    return this;
                }

                public Builder clearExifExposureTime() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifExposureTime();
                    return this;
                }

                public Builder setExifExposureTimeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifExposureTimeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifAperture() {
                    return ((CloudProfile) this.instance).getExifAperture();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifApertureBytes() {
                    return ((CloudProfile) this.instance).getExifApertureBytes();
                }

                public Builder setExifAperture(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifAperture(str);
                    return this;
                }

                public Builder clearExifAperture() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifAperture();
                    return this;
                }

                public Builder setExifApertureBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifApertureBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifISO() {
                    return ((CloudProfile) this.instance).getExifISO();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifISOBytes() {
                    return ((CloudProfile) this.instance).getExifISOBytes();
                }

                public Builder setExifISO(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifISO(str);
                    return this;
                }

                public Builder clearExifISO() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifISO();
                    return this;
                }

                public Builder setExifISOBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifISOBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getExifWhiteBalance() {
                    return ((CloudProfile) this.instance).getExifWhiteBalance();
                }

                public Builder setExifWhiteBalance(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifWhiteBalance(i);
                    return this;
                }

                public Builder clearExifWhiteBalance() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifWhiteBalance();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifGpsAltitude() {
                    return ((CloudProfile) this.instance).getExifGpsAltitude();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifGpsAltitudeBytes() {
                    return ((CloudProfile) this.instance).getExifGpsAltitudeBytes();
                }

                public Builder setExifGpsAltitude(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsAltitude(str);
                    return this;
                }

                public Builder clearExifGpsAltitude() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifGpsAltitude();
                    return this;
                }

                public Builder setExifGpsAltitudeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsAltitudeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public int getExifGpsAltitudeRef() {
                    return ((CloudProfile) this.instance).getExifGpsAltitudeRef();
                }

                public Builder setExifGpsAltitudeRef(int i) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsAltitudeRef(i);
                    return this;
                }

                public Builder clearExifGpsAltitudeRef() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifGpsAltitudeRef();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifFocalLength() {
                    return ((CloudProfile) this.instance).getExifFocalLength();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifFocalLengthBytes() {
                    return ((CloudProfile) this.instance).getExifFocalLengthBytes();
                }

                public Builder setExifFocalLength(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifFocalLength(str);
                    return this;
                }

                public Builder clearExifFocalLength() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifFocalLength();
                    return this;
                }

                public Builder setExifFocalLengthBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifFocalLengthBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifGpsProcessingMethod() {
                    return ((CloudProfile) this.instance).getExifGpsProcessingMethod();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifGpsProcessingMethodBytes() {
                    return ((CloudProfile) this.instance).getExifGpsProcessingMethodBytes();
                }

                public Builder setExifGpsProcessingMethod(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsProcessingMethod(str);
                    return this;
                }

                public Builder clearExifGpsProcessingMethod() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifGpsProcessingMethod();
                    return this;
                }

                public Builder setExifGpsProcessingMethodBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsProcessingMethodBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifGpsTimeStamp() {
                    return ((CloudProfile) this.instance).getExifGpsTimeStamp();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifGpsTimeStampBytes() {
                    return ((CloudProfile) this.instance).getExifGpsTimeStampBytes();
                }

                public Builder setExifGpsTimeStamp(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsTimeStamp(str);
                    return this;
                }

                public Builder clearExifGpsTimeStamp() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifGpsTimeStamp();
                    return this;
                }

                public Builder setExifGpsTimeStampBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsTimeStampBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifGpsDateStamp() {
                    return ((CloudProfile) this.instance).getExifGpsDateStamp();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifGpsDateStampBytes() {
                    return ((CloudProfile) this.instance).getExifGpsDateStampBytes();
                }

                public Builder setExifGpsDateStamp(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsDateStamp(str);
                    return this;
                }

                public Builder clearExifGpsDateStamp() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifGpsDateStamp();
                    return this;
                }

                public Builder setExifGpsDateStampBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifGpsDateStampBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getExifDateTime() {
                    return ((CloudProfile) this.instance).getExifDateTime();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getExifDateTimeBytes() {
                    return ((CloudProfile) this.instance).getExifDateTimeBytes();
                }

                public Builder setExifDateTime(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifDateTime(str);
                    return this;
                }

                public Builder clearExifDateTime() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearExifDateTime();
                    return this;
                }

                public Builder setExifDateTimeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setExifDateTimeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public long getDuration() {
                    return ((CloudProfile) this.instance).getDuration();
                }

                public Builder setDuration(long j) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setDuration(j);
                    return this;
                }

                public Builder clearDuration() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearDuration();
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getLatitude() {
                    return ((CloudProfile) this.instance).getLatitude();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getLatitudeBytes() {
                    return ((CloudProfile) this.instance).getLatitudeBytes();
                }

                public Builder setLatitude(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLatitude(str);
                    return this;
                }

                public Builder clearLatitude() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearLatitude();
                    return this;
                }

                public Builder setLatitudeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLatitudeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getLatitudeRef() {
                    return ((CloudProfile) this.instance).getLatitudeRef();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getLatitudeRefBytes() {
                    return ((CloudProfile) this.instance).getLatitudeRefBytes();
                }

                public Builder setLatitudeRef(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLatitudeRef(str);
                    return this;
                }

                public Builder clearLatitudeRef() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearLatitudeRef();
                    return this;
                }

                public Builder setLatitudeRefBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLatitudeRefBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getLongitude() {
                    return ((CloudProfile) this.instance).getLongitude();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getLongitudeBytes() {
                    return ((CloudProfile) this.instance).getLongitudeBytes();
                }

                public Builder setLongitude(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLongitude(str);
                    return this;
                }

                public Builder clearLongitude() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearLongitude();
                    return this;
                }

                public Builder setLongitudeBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLongitudeBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getLongitudeRef() {
                    return ((CloudProfile) this.instance).getLongitudeRef();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getLongitudeRefBytes() {
                    return ((CloudProfile) this.instance).getLongitudeRefBytes();
                }

                public Builder setLongitudeRef(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLongitudeRef(str);
                    return this;
                }

                public Builder clearLongitudeRef() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearLongitudeRef();
                    return this;
                }

                public Builder setLongitudeRefBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLongitudeRefBytes(byteString);
                    return this;
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public String getLocation() {
                    return ((CloudProfile) this.instance).getLocation();
                }

                @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfileOrBuilder
                public ByteString getLocationBytes() {
                    return ((CloudProfile) this.instance).getLocationBytes();
                }

                public Builder setLocation(String str) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLocation(str);
                    return this;
                }

                public Builder clearLocation() {
                    copyOnWrite();
                    ((CloudProfile) this.instance).clearLocation();
                    return this;
                }

                public Builder setLocationBytes(ByteString byteString) {
                    copyOnWrite();
                    ((CloudProfile) this.instance).setLocationBytes(byteString);
                    return this;
                }
            }

            @Override // com.google.protobuf.GeneratedMessageLite
            public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
                switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                    case 1:
                        return new CloudProfile();
                    case 2:
                        return new Builder(null);
                    case 3:
                        return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0000\u001b\u0000\u0000\u0001\u001b\u001b\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003\u0004\u0004\u0004\u0005\u0004\u0006\u0002\u0007\u0002\bȈ\tȈ\n\u0004\u000bȈ\fȈ\rȈ\u000e\u0004\u000fȈ\u0010\u0004\u0011Ȉ\u0012Ȉ\u0013Ȉ\u0014Ȉ\u0015Ȉ\u0016\u0002\u0017Ȉ\u0018Ȉ\u0019Ȉ\u001aȈ\u001bȈ", new Object[]{"path_", "sha1_", "width_", "height_", "orientation_", "dateTaken_", "specialTypeFlags_", "exifModel_", "exifMake_", "exifFlash_", "exifExposureTime_", "exifAperture_", "exifISO_", "exifWhiteBalance_", "exifGpsAltitude_", "exifGpsAltitudeRef_", "exifFocalLength_", "exifGpsProcessingMethod_", "exifGpsTimeStamp_", "exifGpsDateStamp_", "exifDateTime_", "duration_", "latitude_", "latitudeRef_", "longitude_", "longitudeRef_", "location_"});
                    case 4:
                        return DEFAULT_INSTANCE;
                    case 5:
                        Parser<CloudProfile> parser = PARSER;
                        if (parser == null) {
                            synchronized (CloudProfile.class) {
                                parser = PARSER;
                                if (parser == null) {
                                    parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                                    PARSER = parser;
                                }
                            }
                        }
                        return parser;
                    case 6:
                        return (byte) 1;
                    case 7:
                        return null;
                    default:
                        throw new UnsupportedOperationException();
                }
            }

            static {
                CloudProfile cloudProfile = new CloudProfile();
                DEFAULT_INSTANCE = cloudProfile;
                GeneratedMessageLite.registerDefaultInstance(CloudProfile.class, cloudProfile);
            }

            public static CloudProfile getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            public static Parser<CloudProfile> parser() {
                return DEFAULT_INSTANCE.getParserForType();
            }
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public boolean hasSettings() {
            return this.settings_ != null;
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public Settings getSettings() {
            Settings settings = this.settings_;
            return settings == null ? Settings.getDefaultInstance() : settings;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSettings(Settings settings) {
            settings.getClass();
            this.settings_ = settings;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeSettings(Settings settings) {
            settings.getClass();
            Settings settings2 = this.settings_;
            if (settings2 != null && settings2 != Settings.getDefaultInstance()) {
                this.settings_ = Settings.newBuilder(this.settings_).mergeFrom((Settings.Builder) settings).mo414buildPartial();
            } else {
                this.settings_ = settings;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSettings() {
            this.settings_ = null;
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public List<AlbumProfile> getAlbumProfilesList() {
            return this.albumProfiles_;
        }

        public List<? extends AlbumProfileOrBuilder> getAlbumProfilesOrBuilderList() {
            return this.albumProfiles_;
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public int getAlbumProfilesCount() {
            return this.albumProfiles_.size();
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public AlbumProfile getAlbumProfiles(int i) {
            return this.albumProfiles_.get(i);
        }

        public AlbumProfileOrBuilder getAlbumProfilesOrBuilder(int i) {
            return this.albumProfiles_.get(i);
        }

        private void ensureAlbumProfilesIsMutable() {
            Internal.ProtobufList<AlbumProfile> protobufList = this.albumProfiles_;
            if (!protobufList.isModifiable()) {
                this.albumProfiles_ = GeneratedMessageLite.mutableCopy(protobufList);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAlbumProfiles(int i, AlbumProfile albumProfile) {
            albumProfile.getClass();
            ensureAlbumProfilesIsMutable();
            this.albumProfiles_.set(i, albumProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAlbumProfiles(AlbumProfile albumProfile) {
            albumProfile.getClass();
            ensureAlbumProfilesIsMutable();
            this.albumProfiles_.add(albumProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAlbumProfiles(int i, AlbumProfile albumProfile) {
            albumProfile.getClass();
            ensureAlbumProfilesIsMutable();
            this.albumProfiles_.add(i, albumProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllAlbumProfiles(Iterable<? extends AlbumProfile> iterable) {
            ensureAlbumProfilesIsMutable();
            AbstractMessageLite.addAll((Iterable) iterable, (List) this.albumProfiles_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearAlbumProfiles() {
            this.albumProfiles_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeAlbumProfiles(int i) {
            ensureAlbumProfilesIsMutable();
            this.albumProfiles_.remove(i);
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public List<CloudProfile> getCloudProfilesList() {
            return this.cloudProfiles_;
        }

        public List<? extends CloudProfileOrBuilder> getCloudProfilesOrBuilderList() {
            return this.cloudProfiles_;
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public int getCloudProfilesCount() {
            return this.cloudProfiles_.size();
        }

        @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
        public CloudProfile getCloudProfiles(int i) {
            return this.cloudProfiles_.get(i);
        }

        public CloudProfileOrBuilder getCloudProfilesOrBuilder(int i) {
            return this.cloudProfiles_.get(i);
        }

        private void ensureCloudProfilesIsMutable() {
            Internal.ProtobufList<CloudProfile> protobufList = this.cloudProfiles_;
            if (!protobufList.isModifiable()) {
                this.cloudProfiles_ = GeneratedMessageLite.mutableCopy(protobufList);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCloudProfiles(int i, CloudProfile cloudProfile) {
            cloudProfile.getClass();
            ensureCloudProfilesIsMutable();
            this.cloudProfiles_.set(i, cloudProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCloudProfiles(CloudProfile cloudProfile) {
            cloudProfile.getClass();
            ensureCloudProfilesIsMutable();
            this.cloudProfiles_.add(cloudProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCloudProfiles(int i, CloudProfile cloudProfile) {
            cloudProfile.getClass();
            ensureCloudProfilesIsMutable();
            this.cloudProfiles_.add(i, cloudProfile);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCloudProfiles(Iterable<? extends CloudProfile> iterable) {
            ensureCloudProfilesIsMutable();
            AbstractMessageLite.addAll((Iterable) iterable, (List) this.cloudProfiles_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCloudProfiles() {
            this.cloudProfiles_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeCloudProfiles(int i) {
            ensureCloudProfilesIsMutable();
            this.cloudProfiles_.remove(i);
        }

        public static BackupMessage parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static BackupMessage parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static BackupMessage parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static BackupMessage parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static BackupMessage parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static BackupMessage parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static BackupMessage parseFrom(InputStream inputStream) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static BackupMessage parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static BackupMessage parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static BackupMessage parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static BackupMessage parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static BackupMessage parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (BackupMessage) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(BackupMessage backupMessage) {
            return DEFAULT_INSTANCE.createBuilder(backupMessage);
        }

        /* loaded from: classes.dex */
        public static final class Builder extends GeneratedMessageLite.Builder<BackupMessage, Builder> implements BackupMessageOrBuilder {
            public /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
                this();
            }

            private Builder() {
                super(BackupMessage.DEFAULT_INSTANCE);
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public boolean hasSettings() {
                return ((BackupMessage) this.instance).hasSettings();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public Settings getSettings() {
                return ((BackupMessage) this.instance).getSettings();
            }

            public Builder setSettings(Settings settings) {
                copyOnWrite();
                ((BackupMessage) this.instance).setSettings(settings);
                return this;
            }

            public Builder setSettings(Settings.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).setSettings(builder.mo403build());
                return this;
            }

            public Builder mergeSettings(Settings settings) {
                copyOnWrite();
                ((BackupMessage) this.instance).mergeSettings(settings);
                return this;
            }

            public Builder clearSettings() {
                copyOnWrite();
                ((BackupMessage) this.instance).clearSettings();
                return this;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public List<AlbumProfile> getAlbumProfilesList() {
                return Collections.unmodifiableList(((BackupMessage) this.instance).getAlbumProfilesList());
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public int getAlbumProfilesCount() {
                return ((BackupMessage) this.instance).getAlbumProfilesCount();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public AlbumProfile getAlbumProfiles(int i) {
                return ((BackupMessage) this.instance).getAlbumProfiles(i);
            }

            public Builder setAlbumProfiles(int i, AlbumProfile albumProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).setAlbumProfiles(i, albumProfile);
                return this;
            }

            public Builder setAlbumProfiles(int i, AlbumProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).setAlbumProfiles(i, builder.mo403build());
                return this;
            }

            public Builder addAlbumProfiles(AlbumProfile albumProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAlbumProfiles(albumProfile);
                return this;
            }

            public Builder addAlbumProfiles(int i, AlbumProfile albumProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAlbumProfiles(i, albumProfile);
                return this;
            }

            public Builder addAlbumProfiles(AlbumProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAlbumProfiles(builder.mo403build());
                return this;
            }

            public Builder addAlbumProfiles(int i, AlbumProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAlbumProfiles(i, builder.mo403build());
                return this;
            }

            public Builder addAllAlbumProfiles(Iterable<? extends AlbumProfile> iterable) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAllAlbumProfiles(iterable);
                return this;
            }

            public Builder clearAlbumProfiles() {
                copyOnWrite();
                ((BackupMessage) this.instance).clearAlbumProfiles();
                return this;
            }

            public Builder removeAlbumProfiles(int i) {
                copyOnWrite();
                ((BackupMessage) this.instance).removeAlbumProfiles(i);
                return this;
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public List<CloudProfile> getCloudProfilesList() {
                return Collections.unmodifiableList(((BackupMessage) this.instance).getCloudProfilesList());
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public int getCloudProfilesCount() {
                return ((BackupMessage) this.instance).getCloudProfilesCount();
            }

            @Override // com.miui.gallery.backup.GalleryBackupProtos.BackupMessageOrBuilder
            public CloudProfile getCloudProfiles(int i) {
                return ((BackupMessage) this.instance).getCloudProfiles(i);
            }

            public Builder setCloudProfiles(int i, CloudProfile cloudProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).setCloudProfiles(i, cloudProfile);
                return this;
            }

            public Builder setCloudProfiles(int i, CloudProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).setCloudProfiles(i, builder.mo403build());
                return this;
            }

            public Builder addCloudProfiles(CloudProfile cloudProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).addCloudProfiles(cloudProfile);
                return this;
            }

            public Builder addCloudProfiles(int i, CloudProfile cloudProfile) {
                copyOnWrite();
                ((BackupMessage) this.instance).addCloudProfiles(i, cloudProfile);
                return this;
            }

            public Builder addCloudProfiles(CloudProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).addCloudProfiles(builder.mo403build());
                return this;
            }

            public Builder addCloudProfiles(int i, CloudProfile.Builder builder) {
                copyOnWrite();
                ((BackupMessage) this.instance).addCloudProfiles(i, builder.mo403build());
                return this;
            }

            public Builder addAllCloudProfiles(Iterable<? extends CloudProfile> iterable) {
                copyOnWrite();
                ((BackupMessage) this.instance).addAllCloudProfiles(iterable);
                return this;
            }

            public Builder clearCloudProfiles() {
                copyOnWrite();
                ((BackupMessage) this.instance).clearCloudProfiles();
                return this;
            }

            public Builder removeCloudProfiles(int i) {
                copyOnWrite();
                ((BackupMessage) this.instance).removeCloudProfiles(i);
                return this;
            }
        }

        @Override // com.google.protobuf.GeneratedMessageLite
        public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new BackupMessage();
                case 2:
                    return new Builder(null);
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0002\u0000\u0001\t\u0002\u001b\u0003\u001b", new Object[]{"settings_", "albumProfiles_", AlbumProfile.class, "cloudProfiles_", CloudProfile.class});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<BackupMessage> parser = PARSER;
                    if (parser == null) {
                        synchronized (BackupMessage.class) {
                            parser = PARSER;
                            if (parser == null) {
                                parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                                PARSER = parser;
                            }
                        }
                    }
                    return parser;
                case 6:
                    return (byte) 1;
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        static {
            BackupMessage backupMessage = new BackupMessage();
            DEFAULT_INSTANCE = backupMessage;
            GeneratedMessageLite.registerDefaultInstance(BackupMessage.class, backupMessage);
        }

        public static BackupMessage getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<BackupMessage> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    /* renamed from: com.miui.gallery.backup.GalleryBackupProtos$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke;

        static {
            int[] iArr = new int[GeneratedMessageLite.MethodToInvoke.values().length];
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke = iArr;
            try {
                iArr[GeneratedMessageLite.MethodToInvoke.NEW_MUTABLE_INSTANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.NEW_BUILDER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.BUILD_MESSAGE_INFO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_DEFAULT_INSTANCE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_PARSER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_MEMOIZED_IS_INITIALIZED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.SET_MEMOIZED_IS_INITIALIZED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }
}
