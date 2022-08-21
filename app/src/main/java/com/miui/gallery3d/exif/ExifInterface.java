package com.miui.gallery3d.exif;

import android.text.TextUtils;
import android.util.SparseIntArray;
import androidx.documentfile.provider.DocumentFile;
import com.meicam.sdk.NvsMediaFileConvertor;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes3.dex */
public class ExifInterface {
    public static final ByteOrder DEFAULT_BYTE_ORDER;
    public static final String TAG = "com.miui.gallery3d.exif.ExifInterface";
    public static final int TAG_APERTURE_VALUE;
    public static final int TAG_ARTIST;
    public static final int TAG_BRIGHTNESS_VALUE;
    public static final int TAG_CFA_PATTERN;
    public static final int TAG_COLOR_SPACE;
    public static final int TAG_COMPONENTS_CONFIGURATION;
    public static final int TAG_COMPRESSED_BITS_PER_PIXEL;
    public static final int TAG_CONTRAST;
    public static final int TAG_COPYRIGHT;
    public static final int TAG_CUSTOM_RENDERED;
    public static final int TAG_DATE_TIME;
    public static final int TAG_DATE_TIME_DIGITIZED;
    public static final int TAG_DATE_TIME_ORIGINAL;
    public static final int TAG_DEVICE_SETTING_DESCRIPTION;
    public static final int TAG_DIGITAL_ZOOM_RATIO;
    public static final int TAG_EXIF_IFD;
    public static final int TAG_EXIF_VERSION;
    public static final int TAG_EXPOSURE_BIAS_VALUE;
    public static final int TAG_EXPOSURE_INDEX;
    public static final int TAG_EXPOSURE_MODE;
    public static final int TAG_EXPOSURE_PROGRAM;
    public static final int TAG_EXPOSURE_TIME;
    public static final int TAG_FILE_SOURCE;
    public static final int TAG_FLASH;
    public static final int TAG_FLASHPIX_VERSION;
    public static final int TAG_FLASH_ENERGY;
    public static final int TAG_FOCAL_LENGTH;
    public static final int TAG_FOCAL_LENGTH_IN_35_MM_FILE;
    public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT;
    public static final int TAG_FOCAL_PLANE_X_RESOLUTION;
    public static final int TAG_FOCAL_PLANE_Y_RESOLUTION;
    public static final int TAG_F_NUMBER;
    public static final int TAG_GAIN_CONTROL;
    public static final int TAG_GPS_ALTITUDE;
    public static final int TAG_GPS_ALTITUDE_REF;
    public static final int TAG_GPS_AREA_INFORMATION;
    public static final int TAG_GPS_DATE_STAMP;
    public static final int TAG_GPS_DEST_BEARING;
    public static final int TAG_GPS_DEST_BEARING_REF;
    public static final int TAG_GPS_DEST_DISTANCE;
    public static final int TAG_GPS_DEST_DISTANCE_REF;
    public static final int TAG_GPS_DEST_LATITUDE;
    public static final int TAG_GPS_DEST_LATITUDE_REF;
    public static final int TAG_GPS_DEST_LONGITUDE;
    public static final int TAG_GPS_DEST_LONGITUDE_REF;
    public static final int TAG_GPS_DIFFERENTIAL;
    public static final int TAG_GPS_DOP;
    public static final int TAG_GPS_IFD;
    public static final int TAG_GPS_IMG_DIRECTION;
    public static final int TAG_GPS_IMG_DIRECTION_REF;
    public static final int TAG_GPS_LATITUDE;
    public static final int TAG_GPS_LATITUDE_REF;
    public static final int TAG_GPS_LONGITUDE;
    public static final int TAG_GPS_LONGITUDE_REF;
    public static final int TAG_GPS_MAP_DATUM;
    public static final int TAG_GPS_MEASURE_MODE;
    public static final int TAG_GPS_PROCESSING_METHOD;
    public static final int TAG_GPS_SATTELLITES;
    public static final int TAG_GPS_SPEED;
    public static final int TAG_GPS_SPEED_REF;
    public static final int TAG_GPS_STATUS;
    public static final int TAG_GPS_TIME_STAMP;
    public static final int TAG_GPS_TRACK;
    public static final int TAG_GPS_TRACK_REF;
    public static final int TAG_GPS_VERSION_ID;
    public static final int TAG_IMAGE_UNIQUE_ID;
    public static final int TAG_INTEROPERABILITY_IFD;
    public static final int TAG_INTEROPERABILITY_INDEX;
    public static final int TAG_ISO_SPEED_RATINGS;
    public static final int TAG_JPEG_INTERCHANGE_FORMAT;
    public static final int TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
    public static final int TAG_LIGHT_SOURCE;
    public static final int TAG_MAKER_NOTE;
    public static final int TAG_MAX_APERTURE_VALUE;
    public static final int TAG_METERING_MODE;
    public static final int TAG_OECF;
    public static final int TAG_ORIENTATION;
    public static final int TAG_PARALLEL_PROCESS_COMMENT;
    public static final int TAG_PIXEL_X_DIMENSION;
    public static final int TAG_PIXEL_Y_DIMENSION;
    public static final int TAG_PLANAR_CONFIGURATION;
    public static final int TAG_PRIMARY_CHROMATICITIES;
    public static final int TAG_REFERENCE_BLACK_WHITE;
    public static final int TAG_RELATED_SOUND_FILE;
    public static final int TAG_RESOLUTION_UNIT;
    public static final int TAG_ROWS_PER_STRIP;
    public static final int TAG_SAMPLES_PER_PIXEL;
    public static final int TAG_SATURATION;
    public static final int TAG_SCENE_CAPTURE_TYPE;
    public static final int TAG_SCENE_TYPE;
    public static final int TAG_SENSING_METHOD;
    public static final int TAG_SHARPNESS;
    public static final int TAG_SHUTTER_SPEED_VALUE;
    public static final int TAG_SOFTWARE;
    public static final int TAG_SPATIAL_FREQUENCY_RESPONSE;
    public static final int TAG_SPECTRAL_SENSITIVITY;
    public static final int TAG_STRIP_BYTE_COUNTS;
    public static final int TAG_STRIP_OFFSETS;
    public static final int TAG_SUBJECT_AREA;
    public static final int TAG_SUBJECT_DISTANCE;
    public static final int TAG_SUBJECT_DISTANCE_RANGE;
    public static final int TAG_SUBJECT_LOCATION;
    public static final int TAG_SUB_SEC_TIME;
    public static final int TAG_SUB_SEC_TIME_DIGITIZED;
    public static final int TAG_SUB_SEC_TIME_ORIGINAL;
    public static final int TAG_TRANSFER_FUNCTION;
    public static final int TAG_USER_COMMENT;
    public static final int TAG_WATERMARK_ADDED;
    public static final int TAG_WHITE_BALANCE;
    public static final int TAG_WHITE_POINT;
    public static final int TAG_XIAOMI_COMMENT;
    public static final int TAG_XIAOMI_MODE;
    public static final int TAG_X_RESOLUTION;
    public static final int TAG_Y_CB_CR_COEFFICIENTS;
    public static final int TAG_Y_CB_CR_POSITIONING;
    public static final int TAG_Y_CB_CR_SUB_SAMPLING;
    public static final int TAG_Y_RESOLUTION;
    public static HashSet<Short> sBannedDefines;
    public static HashSet<Short> sOffsetTags;
    public ExifData mData = new ExifData(DEFAULT_BYTE_ORDER);
    public final DateFormat mDateTimeStampFormat = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
    public final DateFormat mGPSDateStampFormat;
    public final Calendar mGPSTimeStampCalendar;
    public SparseIntArray mTagInfo;
    public static final int TAG_IMAGE_WIDTH = defineTag(0, 256);
    public static final int TAG_IMAGE_LENGTH = defineTag(0, 257);
    public static final int TAG_BITS_PER_SAMPLE = defineTag(0, 258);
    public static final int TAG_COMPRESSION = defineTag(0, 259);
    public static final int TAG_PHOTOMETRIC_INTERPRETATION = defineTag(0, 262);
    public static final int TAG_IMAGE_DESCRIPTION = defineTag(0, 270);
    public static final int TAG_MAKE = defineTag(0, 271);
    public static final int TAG_MODEL = defineTag(0, 272);

    public static int defineTag(int i, short s) {
        return (i << 16) | (s & 65535);
    }

    public static int getAllowedIfdFlagsFromInfo(int i) {
        return i >>> 24;
    }

    public static int getComponentCountFromInfo(int i) {
        return i & NvsMediaFileConvertor.CONVERTOR_ERROR_UNKNOWN;
    }

    public static int getTrueIfd(int i) {
        return i >>> 16;
    }

    public static short getTrueTagKey(int i) {
        return (short) i;
    }

    public static short getTypeFromInfo(int i) {
        return (short) ((i >> 16) & 255);
    }

    static {
        int defineTag = defineTag(0, (short) 273);
        TAG_STRIP_OFFSETS = defineTag;
        TAG_ORIENTATION = defineTag(0, (short) 274);
        TAG_SAMPLES_PER_PIXEL = defineTag(0, (short) 277);
        TAG_ROWS_PER_STRIP = defineTag(0, (short) 278);
        int defineTag2 = defineTag(0, (short) 279);
        TAG_STRIP_BYTE_COUNTS = defineTag2;
        TAG_X_RESOLUTION = defineTag(0, (short) 282);
        TAG_Y_RESOLUTION = defineTag(0, (short) 283);
        TAG_PLANAR_CONFIGURATION = defineTag(0, (short) 284);
        TAG_RESOLUTION_UNIT = defineTag(0, (short) 296);
        TAG_TRANSFER_FUNCTION = defineTag(0, (short) 301);
        TAG_SOFTWARE = defineTag(0, (short) 305);
        TAG_DATE_TIME = defineTag(0, (short) 306);
        TAG_ARTIST = defineTag(0, (short) 315);
        TAG_WHITE_POINT = defineTag(0, (short) 318);
        TAG_PRIMARY_CHROMATICITIES = defineTag(0, (short) 319);
        TAG_Y_CB_CR_COEFFICIENTS = defineTag(0, (short) 529);
        TAG_Y_CB_CR_SUB_SAMPLING = defineTag(0, (short) 530);
        TAG_Y_CB_CR_POSITIONING = defineTag(0, (short) 531);
        TAG_REFERENCE_BLACK_WHITE = defineTag(0, (short) 532);
        TAG_COPYRIGHT = defineTag(0, (short) -32104);
        int defineTag3 = defineTag(0, (short) -30871);
        TAG_EXIF_IFD = defineTag3;
        int defineTag4 = defineTag(0, (short) -30683);
        TAG_GPS_IFD = defineTag4;
        int defineTag5 = defineTag(1, (short) 513);
        TAG_JPEG_INTERCHANGE_FORMAT = defineTag5;
        int defineTag6 = defineTag(1, (short) 514);
        TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = defineTag6;
        TAG_EXPOSURE_TIME = defineTag(2, (short) -32102);
        TAG_F_NUMBER = defineTag(2, (short) -32099);
        TAG_EXPOSURE_PROGRAM = defineTag(2, (short) -30686);
        TAG_SPECTRAL_SENSITIVITY = defineTag(2, (short) -30684);
        TAG_ISO_SPEED_RATINGS = defineTag(2, (short) -30681);
        TAG_OECF = defineTag(2, (short) -30680);
        TAG_EXIF_VERSION = defineTag(2, (short) -28672);
        TAG_DATE_TIME_ORIGINAL = defineTag(2, (short) -28669);
        TAG_DATE_TIME_DIGITIZED = defineTag(2, (short) -28668);
        TAG_COMPONENTS_CONFIGURATION = defineTag(2, (short) -28415);
        TAG_COMPRESSED_BITS_PER_PIXEL = defineTag(2, (short) -28414);
        TAG_SHUTTER_SPEED_VALUE = defineTag(2, (short) -28159);
        TAG_APERTURE_VALUE = defineTag(2, (short) -28158);
        TAG_BRIGHTNESS_VALUE = defineTag(2, (short) -28157);
        TAG_EXPOSURE_BIAS_VALUE = defineTag(2, (short) -28156);
        TAG_MAX_APERTURE_VALUE = defineTag(2, (short) -28155);
        TAG_SUBJECT_DISTANCE = defineTag(2, (short) -28154);
        TAG_METERING_MODE = defineTag(2, (short) -28153);
        TAG_LIGHT_SOURCE = defineTag(2, (short) -28152);
        TAG_FLASH = defineTag(2, (short) -28151);
        TAG_FOCAL_LENGTH = defineTag(2, (short) -28150);
        TAG_SUBJECT_AREA = defineTag(2, (short) -28140);
        TAG_MAKER_NOTE = defineTag(2, (short) -28036);
        TAG_USER_COMMENT = defineTag(2, (short) -28026);
        TAG_SUB_SEC_TIME = defineTag(2, (short) -28016);
        TAG_SUB_SEC_TIME_ORIGINAL = defineTag(2, (short) -28015);
        TAG_SUB_SEC_TIME_DIGITIZED = defineTag(2, (short) -28014);
        TAG_FLASHPIX_VERSION = defineTag(2, (short) -24576);
        TAG_COLOR_SPACE = defineTag(2, (short) -24575);
        TAG_PIXEL_X_DIMENSION = defineTag(2, (short) -24574);
        TAG_PIXEL_Y_DIMENSION = defineTag(2, (short) -24573);
        TAG_RELATED_SOUND_FILE = defineTag(2, (short) -24572);
        int defineTag7 = defineTag(2, (short) -24571);
        TAG_INTEROPERABILITY_IFD = defineTag7;
        TAG_FLASH_ENERGY = defineTag(2, (short) -24053);
        TAG_SPATIAL_FREQUENCY_RESPONSE = defineTag(2, (short) -24052);
        TAG_FOCAL_PLANE_X_RESOLUTION = defineTag(2, (short) -24050);
        TAG_FOCAL_PLANE_Y_RESOLUTION = defineTag(2, (short) -24049);
        TAG_FOCAL_PLANE_RESOLUTION_UNIT = defineTag(2, (short) -24048);
        TAG_SUBJECT_LOCATION = defineTag(2, (short) -24044);
        TAG_EXPOSURE_INDEX = defineTag(2, (short) -24043);
        TAG_SENSING_METHOD = defineTag(2, (short) -24041);
        TAG_FILE_SOURCE = defineTag(2, (short) -23808);
        TAG_SCENE_TYPE = defineTag(2, (short) -23807);
        TAG_CFA_PATTERN = defineTag(2, (short) -23806);
        TAG_CUSTOM_RENDERED = defineTag(2, (short) -23551);
        TAG_EXPOSURE_MODE = defineTag(2, (short) -23550);
        TAG_WHITE_BALANCE = defineTag(2, (short) -23549);
        TAG_DIGITAL_ZOOM_RATIO = defineTag(2, (short) -23548);
        TAG_FOCAL_LENGTH_IN_35_MM_FILE = defineTag(2, (short) -23547);
        TAG_SCENE_CAPTURE_TYPE = defineTag(2, (short) -23546);
        TAG_GAIN_CONTROL = defineTag(2, (short) -23545);
        TAG_CONTRAST = defineTag(2, (short) -23544);
        TAG_SATURATION = defineTag(2, (short) -23543);
        TAG_SHARPNESS = defineTag(2, (short) -23542);
        TAG_DEVICE_SETTING_DESCRIPTION = defineTag(2, (short) -23541);
        TAG_SUBJECT_DISTANCE_RANGE = defineTag(2, (short) -23540);
        TAG_IMAGE_UNIQUE_ID = defineTag(2, (short) -23520);
        TAG_PARALLEL_PROCESS_COMMENT = defineTag(2, (short) -30584);
        TAG_WATERMARK_ADDED = defineTag(2, (short) -30565);
        TAG_XIAOMI_COMMENT = defineTag(2, (short) -26215);
        TAG_GPS_VERSION_ID = defineTag(4, (short) 0);
        TAG_GPS_LATITUDE_REF = defineTag(4, (short) 1);
        TAG_GPS_LATITUDE = defineTag(4, (short) 2);
        TAG_GPS_LONGITUDE_REF = defineTag(4, (short) 3);
        TAG_GPS_LONGITUDE = defineTag(4, (short) 4);
        TAG_GPS_ALTITUDE_REF = defineTag(4, (short) 5);
        TAG_GPS_ALTITUDE = defineTag(4, (short) 6);
        TAG_GPS_TIME_STAMP = defineTag(4, (short) 7);
        TAG_GPS_SATTELLITES = defineTag(4, (short) 8);
        TAG_GPS_STATUS = defineTag(4, (short) 9);
        TAG_GPS_MEASURE_MODE = defineTag(4, (short) 10);
        TAG_GPS_DOP = defineTag(4, (short) 11);
        TAG_GPS_SPEED_REF = defineTag(4, (short) 12);
        TAG_GPS_SPEED = defineTag(4, (short) 13);
        TAG_GPS_TRACK_REF = defineTag(4, (short) 14);
        TAG_GPS_TRACK = defineTag(4, (short) 15);
        TAG_GPS_IMG_DIRECTION_REF = defineTag(4, (short) 16);
        TAG_GPS_IMG_DIRECTION = defineTag(4, (short) 17);
        TAG_GPS_MAP_DATUM = defineTag(4, (short) 18);
        TAG_GPS_DEST_LATITUDE_REF = defineTag(4, (short) 19);
        TAG_GPS_DEST_LATITUDE = defineTag(4, (short) 20);
        TAG_GPS_DEST_LONGITUDE_REF = defineTag(4, (short) 21);
        TAG_GPS_DEST_LONGITUDE = defineTag(4, (short) 22);
        TAG_GPS_DEST_BEARING_REF = defineTag(4, (short) 23);
        TAG_GPS_DEST_BEARING = defineTag(4, (short) 24);
        TAG_GPS_DEST_DISTANCE_REF = defineTag(4, (short) 25);
        TAG_GPS_DEST_DISTANCE = defineTag(4, (short) 26);
        TAG_GPS_PROCESSING_METHOD = defineTag(4, (short) 27);
        TAG_GPS_AREA_INFORMATION = defineTag(4, (short) 28);
        TAG_GPS_DATE_STAMP = defineTag(4, (short) 29);
        TAG_GPS_DIFFERENTIAL = defineTag(4, (short) 30);
        TAG_INTEROPERABILITY_INDEX = defineTag(3, (short) 1);
        TAG_XIAOMI_MODE = defineTag(2, (short) -22943);
        HashSet<Short> hashSet = new HashSet<>();
        sOffsetTags = hashSet;
        hashSet.add(Short.valueOf(getTrueTagKey(defineTag4)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(defineTag3)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(defineTag5)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(defineTag7)));
        sOffsetTags.add(Short.valueOf(getTrueTagKey(defineTag)));
        HashSet<Short> hashSet2 = new HashSet<>(sOffsetTags);
        sBannedDefines = hashSet2;
        hashSet2.add(Short.valueOf(getTrueTagKey(-1)));
        sBannedDefines.add(Short.valueOf(getTrueTagKey(defineTag6)));
        sBannedDefines.add(Short.valueOf(getTrueTagKey(defineTag2)));
        DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    }

    public ExifInterface() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        this.mGPSDateStampFormat = simpleDateFormat;
        this.mGPSTimeStampCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.mTagInfo = null;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void readExif(byte[] bArr) throws ExifInvalidFormatException, IOException {
        readExif(new ByteArrayInputStream(bArr));
    }

    public void readExif(InputStream inputStream) throws ExifInvalidFormatException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        this.mData = new ExifReader(this).read(inputStream);
    }

    public void readExif(String str) throws ExifInvalidFormatException, IOException {
        if (str == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(str));
            try {
                readExif(bufferedInputStream2);
                closeSilently(bufferedInputStream2);
            } catch (Throwable th) {
                th = th;
                bufferedInputStream = bufferedInputStream2;
                closeSilently(bufferedInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public OutputStream getExifWriterStream(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        ExifOutputStream exifOutputStream = new ExifOutputStream(outputStream, this);
        exifOutputStream.setExifData(this.mData);
        return exifOutputStream;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean rewriteExif(String str, Collection<ExifTag> collection) throws FileNotFoundException, IOException {
        BufferedInputStream bufferedInputStream = null;
        try {
            try {
                File file = new File(str);
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
                try {
                    try {
                        try {
                            long offsetToExifEndFromSOF = ExifParser.parse(bufferedInputStream2, this).getOffsetToExifEndFromSOF();
                            bufferedInputStream2.close();
                            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                            try {
                                if (randomAccessFile.length() < offsetToExifEndFromSOF) {
                                    throw new IOException("Filesize changed during operation");
                                }
                                boolean rewriteExif = rewriteExif(randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, offsetToExifEndFromSOF), collection);
                                closeSilently(null);
                                randomAccessFile.close();
                                return rewriteExif;
                            } catch (IOException e) {
                                e = e;
                                bufferedInputStream = randomAccessFile;
                                closeSilently(bufferedInputStream);
                                throw e;
                            }
                        } catch (ExifInvalidFormatException e2) {
                            throw new IOException("Invalid exif format : ", e2);
                        }
                    } catch (Throwable th) {
                        bufferedInputStream = bufferedInputStream2;
                        th = th;
                        closeSilently(bufferedInputStream);
                        throw th;
                    }
                } catch (IOException e3) {
                    e = e3;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e4) {
            e = e4;
        }
    }

    public boolean rewriteExif(ByteBuffer byteBuffer, Collection<ExifTag> collection) throws IOException {
        try {
            ExifModifier exifModifier = new ExifModifier(byteBuffer, this);
            for (ExifTag exifTag : collection) {
                exifModifier.modifyTag(exifTag);
            }
            return exifModifier.commit();
        } catch (ExifInvalidFormatException e) {
            throw new IOException("Invalid exif format : " + e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.util.Collection<com.miui.gallery3d.exif.ExifTag>, java.util.Collection] */
    /* JADX WARN: Type inference failed for: r10v12 */
    /* JADX WARN: Type inference failed for: r10v15, types: [com.miui.gallery3d.exif.ExifOutputStream, java.io.OutputStream, java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r10v5 */
    /* JADX WARN: Type inference failed for: r8v0, types: [com.miui.gallery3d.exif.ExifInterface] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v8, types: [java.io.OutputStream, java.io.Closeable] */
    public void forceRewriteExif(String str, Collection<ExifTag> collection) throws FileNotFoundException, IOException {
        boolean z;
        Closeable closeable;
        FileInputStream fileInputStream;
        try {
            z = rewriteExif(str, collection);
        } catch (Exception unused) {
            z = false;
        }
        if (!z) {
            ExifData exifData = this.mData;
            this.mData = new ExifData(DEFAULT_BYTE_ORDER);
            Closeable closeable2 = null;
            try {
                try {
                    fileInputStream = new FileInputStream((String) str);
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = null;
                }
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    doExifStreamIO(fileInputStream, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    readExif(byteArray);
                    setTags(collection);
                    if (str == 0) {
                        throw new IllegalArgumentException("Argument is null");
                    }
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile((String) str, IStoragePermissionStrategy.Permission.UPDATE, FileHandleRecordHelper.appendInvokerTag(TAG, "forceRewriteExif"));
                    if (documentFile != null) {
                        str = StorageSolutionProvider.get().openOutputStream(documentFile);
                        try {
                            collection = new ExifOutputStream(str, this);
                            collection.setExifData(this.mData);
                            try {
                                collection.write(byteArray, 0, byteArray.length);
                                collection.flush();
                                closeSilently(str);
                                closeSilently(fileInputStream);
                                closeSilently(collection);
                                this.mData = exifData;
                            } catch (ExifInvalidFormatException e) {
                                e = e;
                                throw new IOException("Invalid exif format : " + e);
                            } catch (IOException e2) {
                                e = e2;
                                throw e;
                            } catch (Throwable th2) {
                                th = th2;
                                closeable2 = str;
                                closeable = collection;
                                closeSilently(closeable2);
                                closeSilently(fileInputStream);
                                closeSilently(closeable);
                                this.mData = exifData;
                                throw th;
                            }
                        } catch (ExifInvalidFormatException e3) {
                            e = e3;
                        } catch (IOException e4) {
                            e = e4;
                        } catch (Throwable th3) {
                            th = th3;
                            collection = 0;
                        }
                    } else {
                        closeSilently(null);
                        closeSilently(fileInputStream);
                        closeSilently(null);
                        this.mData = exifData;
                    }
                } catch (ExifInvalidFormatException e5) {
                    e = e5;
                } catch (IOException e6) {
                    e = e6;
                } catch (Throwable th4) {
                    th = th4;
                    closeable = null;
                    closeSilently(closeable2);
                    closeSilently(fileInputStream);
                    closeSilently(closeable);
                    this.mData = exifData;
                    throw th;
                }
            } catch (ExifInvalidFormatException e7) {
                e = e7;
            } catch (IOException e8) {
                e = e8;
            } catch (Throwable th5) {
                th = th5;
                closeable = null;
                fileInputStream = null;
            }
        }
    }

    public void forceRewriteExif(String str) throws FileNotFoundException, IOException {
        forceRewriteExif(str, getAllTags());
    }

    public List<ExifTag> getAllTags() {
        return this.mData.getAllTags();
    }

    public ExifTag getTag(int i, int i2) {
        if (!ExifTag.isValidIfd(i2)) {
            return null;
        }
        return this.mData.getTag(getTrueTagKey(i), i2);
    }

    public String getTagStringValue(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsString();
    }

    public Integer getTagIntValue(int i, int i2) {
        int[] tagIntValues = getTagIntValues(i, i2);
        if (tagIntValues == null || tagIntValues.length <= 0) {
            return null;
        }
        return new Integer(tagIntValues[0]);
    }

    public Integer getTagIntValue(int i) {
        return getTagIntValue(i, getDefinedTagDefaultIfd(i));
    }

    public Byte getTagByteValue(int i, int i2) {
        byte[] tagByteValues = getTagByteValues(i, i2);
        if (tagByteValues == null || tagByteValues.length <= 0) {
            return null;
        }
        return new Byte(tagByteValues[0]);
    }

    public int[] getTagIntValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsInts();
    }

    public byte[] getTagByteValues(int i, int i2) {
        ExifTag tag = getTag(i, i2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsBytes();
    }

    public int getDefinedTagDefaultIfd(int i) {
        if (getTagInfo().get(i) == 0) {
            return -1;
        }
        return getTrueIfd(i);
    }

    public static boolean isOffsetTag(short s) {
        return sOffsetTags.contains(Short.valueOf(s));
    }

    public ExifTag buildTag(int i, int i2, Object obj) {
        int i3 = getTagInfo().get(i);
        if (i3 == 0 || obj == null) {
            return null;
        }
        short typeFromInfo = getTypeFromInfo(i3);
        int componentCountFromInfo = getComponentCountFromInfo(i3);
        boolean z = componentCountFromInfo != 0;
        if (!isIfdAllowed(i3, i2)) {
            return null;
        }
        ExifTag exifTag = new ExifTag(getTrueTagKey(i), typeFromInfo, componentCountFromInfo, i2, z);
        if (exifTag.setValue(obj)) {
            return exifTag;
        }
        return null;
    }

    public ExifTag buildTag(int i, Object obj) {
        return buildTag(i, getTrueIfd(i), obj);
    }

    public ExifTag buildUninitializedTag(int i) {
        int i2 = getTagInfo().get(i);
        if (i2 == 0) {
            return null;
        }
        short typeFromInfo = getTypeFromInfo(i2);
        int componentCountFromInfo = getComponentCountFromInfo(i2);
        boolean z = componentCountFromInfo != 0;
        return new ExifTag(getTrueTagKey(i), typeFromInfo, componentCountFromInfo, getTrueIfd(i), z);
    }

    public ExifTag setTag(ExifTag exifTag) {
        if (exifTag != null) {
            if (exifTag.getDataSize() < 65535) {
                return this.mData.addTag(exifTag);
            }
            DefaultLogger.e(TAG, "tagId: %s, tagSize: %d", String.format(Locale.US, "%04X\n", Short.valueOf(exifTag.getTagId())), Integer.valueOf(exifTag.getDataSize()));
            return null;
        }
        return null;
    }

    public void setTags(Collection<ExifTag> collection) {
        for (ExifTag exifTag : collection) {
            setTag(exifTag);
        }
    }

    public String getXiaomiComment() {
        return this.mData.getXiaomiComment();
    }

    public static short getOrientationValueForRotation(int i) {
        int i2 = i % 360;
        if (i2 < 0) {
            i2 += 360;
        }
        if (i2 < 90) {
            return (short) 1;
        }
        if (i2 < 180) {
            return (short) 6;
        }
        return i2 < 270 ? (short) 3 : (short) 8;
    }

    public boolean addDateTimeStampTag(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (i != TAG_DATE_TIME && i != TAG_DATE_TIME_DIGITIZED && i != TAG_DATE_TIME_ORIGINAL) {
            return false;
        }
        if (!str.endsWith("\u0000")) {
            str = str + (char) 0;
        }
        ExifTag buildTag = buildTag(i, str);
        if (buildTag == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public boolean addSubSecTimeStampTag(long j) {
        ExifTag buildTag;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(14);
        if (i <= 0 || (buildTag = buildTag(TAG_SUB_SEC_TIME, String.valueOf(i))) == null) {
            return false;
        }
        setTag(buildTag);
        return true;
    }

    public final ExifTag buildTagIgnoreIfdAllowed(int i, int i2, Object obj) {
        int i3 = getTagInfo().get(i);
        if (i3 == 0 || obj == null) {
            return null;
        }
        short typeFromInfo = getTypeFromInfo(i3);
        int componentCountFromInfo = getComponentCountFromInfo(i3);
        ExifTag exifTag = new ExifTag(getTrueTagKey(i), typeFromInfo, componentCountFromInfo, i2, componentCountFromInfo != 0);
        if (exifTag.setValue(obj)) {
            return exifTag;
        }
        return null;
    }

    public String getUserCommentAsASCII() {
        return this.mData.getUserCommentAsASCII();
    }

    public boolean addUserComment(String str) {
        ExifTag buildTagIgnoreIfdAllowed = buildTagIgnoreIfdAllowed(TAG_USER_COMMENT, 0, str);
        if (buildTagIgnoreIfdAllowed == null) {
            return false;
        }
        setTag(buildTagIgnoreIfdAllowed);
        return true;
    }

    public boolean addGpsTags(double d, double d2) {
        ExifTag buildTag = buildTag(TAG_GPS_LATITUDE, toExifLatLong(d));
        ExifTag buildTag2 = buildTag(TAG_GPS_LONGITUDE, toExifLatLong(d2));
        ExifTag buildTag3 = buildTag(TAG_GPS_LATITUDE_REF, d >= SearchStatUtils.POW ? "N" : "S");
        ExifTag buildTag4 = buildTag(TAG_GPS_LONGITUDE_REF, d2 >= SearchStatUtils.POW ? "E" : "W");
        if (buildTag == null || buildTag2 == null || buildTag3 == null || buildTag4 == null) {
            return false;
        }
        setTag(buildTag);
        setTag(buildTag2);
        setTag(buildTag3);
        setTag(buildTag4);
        return true;
    }

    public boolean addGpsDateTimeStampTag(String str, String str2) {
        ExifTag buildTag;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            int i = TAG_GPS_DATE_STAMP;
            ExifTag buildTag2 = buildTag(i, str + (char) 0);
            if (buildTag2 == null) {
                return false;
            }
            setTag(buildTag2);
            String[] split = str2.split(":");
            if (split == null || split.length != 3 || (buildTag = buildTag(TAG_GPS_TIME_STAMP, new Rational[]{new Rational(Integer.parseInt(split[0]), 1L), new Rational(Integer.parseInt(split[1]), 1L), new Rational(Integer.parseInt(split[2]), 1L)})) == null) {
                return false;
            }
            setTag(buildTag);
            return true;
        }
        return false;
    }

    public static Rational[] toExifLatLong(double d) {
        double abs = Math.abs(d);
        int i = (int) abs;
        double d2 = (abs - i) * 60.0d;
        int i2 = (int) d2;
        return new Rational[]{new Rational(i, 1L), new Rational(i2, 1L), new Rational((int) ((d2 - i2) * 6000.0d), 100L)};
    }

    public final void doExifStreamIO(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        int read = inputStream.read(bArr, 0, 1024);
        while (read != -1) {
            outputStream.write(bArr, 0, read);
            read = inputStream.read(bArr, 0, 1024);
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public SparseIntArray getTagInfo() {
        if (this.mTagInfo == null) {
            this.mTagInfo = new SparseIntArray();
            initTagInfo();
        }
        return this.mTagInfo;
    }

    public final void initTagInfo() {
        int flagsFromAllowedIfds = getFlagsFromAllowedIfds(new int[]{0, 1}) << 24;
        SparseIntArray sparseIntArray = this.mTagInfo;
        int i = TAG_MAKE;
        int i2 = flagsFromAllowedIfds | 131072;
        int i3 = i2 | 0;
        sparseIntArray.put(i, i3);
        SparseIntArray sparseIntArray2 = this.mTagInfo;
        int i4 = TAG_IMAGE_WIDTH;
        int i5 = flagsFromAllowedIfds | nexEngine.ExportHEVCMainTierLevel52;
        int i6 = i5 | 1;
        sparseIntArray2.put(i4, i6);
        this.mTagInfo.put(TAG_IMAGE_LENGTH, i6);
        int i7 = flagsFromAllowedIfds | 196608;
        this.mTagInfo.put(TAG_BITS_PER_SAMPLE, i7 | 3);
        int i8 = i7 | 1;
        this.mTagInfo.put(TAG_COMPRESSION, i8);
        this.mTagInfo.put(TAG_PHOTOMETRIC_INTERPRETATION, i8);
        this.mTagInfo.put(TAG_ORIENTATION, i8);
        this.mTagInfo.put(TAG_SAMPLES_PER_PIXEL, i8);
        this.mTagInfo.put(TAG_PLANAR_CONFIGURATION, i8);
        this.mTagInfo.put(TAG_Y_CB_CR_SUB_SAMPLING, i7 | 2);
        this.mTagInfo.put(TAG_Y_CB_CR_POSITIONING, i8);
        int i9 = flagsFromAllowedIfds | 327680;
        int i10 = i9 | 1;
        this.mTagInfo.put(TAG_X_RESOLUTION, i10);
        this.mTagInfo.put(TAG_Y_RESOLUTION, i10);
        this.mTagInfo.put(TAG_RESOLUTION_UNIT, i8);
        int i11 = i5 | 0;
        this.mTagInfo.put(TAG_STRIP_OFFSETS, i11);
        this.mTagInfo.put(TAG_ROWS_PER_STRIP, i6);
        this.mTagInfo.put(TAG_STRIP_BYTE_COUNTS, i11);
        this.mTagInfo.put(TAG_TRANSFER_FUNCTION, i7 | 768);
        this.mTagInfo.put(TAG_WHITE_POINT, i9 | 2);
        int i12 = i9 | 6;
        this.mTagInfo.put(TAG_PRIMARY_CHROMATICITIES, i12);
        this.mTagInfo.put(TAG_Y_CB_CR_COEFFICIENTS, i9 | 3);
        this.mTagInfo.put(TAG_REFERENCE_BLACK_WHITE, i12);
        this.mTagInfo.put(TAG_DATE_TIME, i2 | 20);
        this.mTagInfo.put(TAG_IMAGE_DESCRIPTION, i3);
        this.mTagInfo.put(i, i3);
        this.mTagInfo.put(TAG_MODEL, i3);
        this.mTagInfo.put(TAG_SOFTWARE, i3);
        this.mTagInfo.put(TAG_ARTIST, i3);
        this.mTagInfo.put(TAG_COPYRIGHT, i3);
        this.mTagInfo.put(TAG_EXIF_IFD, i6);
        this.mTagInfo.put(TAG_GPS_IFD, i6);
        SparseIntArray sparseIntArray3 = this.mTagInfo;
        int i13 = TAG_JPEG_INTERCHANGE_FORMAT;
        int flagsFromAllowedIfds2 = (getFlagsFromAllowedIfds(new int[]{1}) << 24) | nexEngine.ExportHEVCMainTierLevel52 | 1;
        sparseIntArray3.put(i13, flagsFromAllowedIfds2);
        this.mTagInfo.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, flagsFromAllowedIfds2);
        int flagsFromAllowedIfds3 = getFlagsFromAllowedIfds(new int[]{2}) << 24;
        int i14 = flagsFromAllowedIfds3 | 458752;
        int i15 = i14 | 4;
        this.mTagInfo.put(TAG_EXIF_VERSION, i15);
        this.mTagInfo.put(TAG_FLASHPIX_VERSION, i15);
        int i16 = flagsFromAllowedIfds3 | 196608;
        int i17 = i16 | 1;
        this.mTagInfo.put(TAG_COLOR_SPACE, i17);
        this.mTagInfo.put(TAG_COMPONENTS_CONFIGURATION, i15);
        int i18 = flagsFromAllowedIfds3 | 327680 | 1;
        this.mTagInfo.put(TAG_COMPRESSED_BITS_PER_PIXEL, i18);
        int i19 = 262144 | flagsFromAllowedIfds3 | 1;
        this.mTagInfo.put(TAG_PIXEL_X_DIMENSION, i19);
        this.mTagInfo.put(TAG_PIXEL_Y_DIMENSION, i19);
        int i20 = i14 | 0;
        this.mTagInfo.put(TAG_MAKER_NOTE, i20);
        this.mTagInfo.put(TAG_USER_COMMENT, i20);
        int i21 = flagsFromAllowedIfds3 | 131072;
        this.mTagInfo.put(TAG_RELATED_SOUND_FILE, i21 | 13);
        int i22 = i21 | 20;
        this.mTagInfo.put(TAG_DATE_TIME_ORIGINAL, i22);
        this.mTagInfo.put(TAG_DATE_TIME_DIGITIZED, i22);
        int i23 = i21 | 0;
        this.mTagInfo.put(TAG_SUB_SEC_TIME, i23);
        this.mTagInfo.put(TAG_SUB_SEC_TIME_ORIGINAL, i23);
        this.mTagInfo.put(TAG_SUB_SEC_TIME_DIGITIZED, i23);
        this.mTagInfo.put(TAG_IMAGE_UNIQUE_ID, i21 | 33);
        this.mTagInfo.put(TAG_EXPOSURE_TIME, i18);
        this.mTagInfo.put(TAG_F_NUMBER, i18);
        this.mTagInfo.put(TAG_EXPOSURE_PROGRAM, i17);
        this.mTagInfo.put(TAG_SPECTRAL_SENSITIVITY, i23);
        int i24 = i16 | 0;
        this.mTagInfo.put(TAG_ISO_SPEED_RATINGS, i24);
        this.mTagInfo.put(TAG_OECF, i20);
        int i25 = flagsFromAllowedIfds3 | 655360 | 1;
        this.mTagInfo.put(TAG_SHUTTER_SPEED_VALUE, i25);
        this.mTagInfo.put(TAG_APERTURE_VALUE, i18);
        this.mTagInfo.put(TAG_BRIGHTNESS_VALUE, i25);
        this.mTagInfo.put(TAG_EXPOSURE_BIAS_VALUE, i25);
        this.mTagInfo.put(TAG_MAX_APERTURE_VALUE, i18);
        this.mTagInfo.put(TAG_SUBJECT_DISTANCE, i18);
        this.mTagInfo.put(TAG_METERING_MODE, i17);
        this.mTagInfo.put(TAG_LIGHT_SOURCE, i17);
        this.mTagInfo.put(TAG_FLASH, i17);
        this.mTagInfo.put(TAG_FOCAL_LENGTH, i18);
        this.mTagInfo.put(TAG_SUBJECT_AREA, i24);
        this.mTagInfo.put(TAG_FLASH_ENERGY, i18);
        this.mTagInfo.put(TAG_SPATIAL_FREQUENCY_RESPONSE, i20);
        this.mTagInfo.put(TAG_FOCAL_PLANE_X_RESOLUTION, i18);
        this.mTagInfo.put(TAG_FOCAL_PLANE_Y_RESOLUTION, i18);
        this.mTagInfo.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, i17);
        this.mTagInfo.put(TAG_SUBJECT_LOCATION, 2 | i16);
        this.mTagInfo.put(TAG_EXPOSURE_INDEX, i18);
        this.mTagInfo.put(TAG_SENSING_METHOD, i17);
        int i26 = i14 | 1;
        this.mTagInfo.put(TAG_FILE_SOURCE, i26);
        this.mTagInfo.put(TAG_SCENE_TYPE, i26);
        this.mTagInfo.put(TAG_CFA_PATTERN, i20);
        this.mTagInfo.put(TAG_CUSTOM_RENDERED, i17);
        this.mTagInfo.put(TAG_EXPOSURE_MODE, i17);
        this.mTagInfo.put(TAG_WHITE_BALANCE, i17);
        this.mTagInfo.put(TAG_DIGITAL_ZOOM_RATIO, i18);
        this.mTagInfo.put(TAG_FOCAL_LENGTH_IN_35_MM_FILE, i17);
        this.mTagInfo.put(TAG_SCENE_CAPTURE_TYPE, i17);
        this.mTagInfo.put(TAG_GAIN_CONTROL, i18);
        this.mTagInfo.put(TAG_CONTRAST, i17);
        this.mTagInfo.put(TAG_SATURATION, i17);
        this.mTagInfo.put(TAG_SHARPNESS, i17);
        this.mTagInfo.put(TAG_DEVICE_SETTING_DESCRIPTION, i20);
        this.mTagInfo.put(TAG_SUBJECT_DISTANCE_RANGE, i17);
        this.mTagInfo.put(TAG_INTEROPERABILITY_IFD, i19);
        this.mTagInfo.put(TAG_PARALLEL_PROCESS_COMMENT, i23);
        this.mTagInfo.put(TAG_XIAOMI_COMMENT, i23);
        this.mTagInfo.put(TAG_WATERMARK_ADDED, i17);
        this.mTagInfo.put(TAG_XIAOMI_MODE, flagsFromAllowedIfds3 | 65536 | 1);
        int flagsFromAllowedIfds4 = getFlagsFromAllowedIfds(new int[]{4}) << 24;
        int i27 = 65536 | flagsFromAllowedIfds4;
        this.mTagInfo.put(TAG_GPS_VERSION_ID, i27 | 4);
        int i28 = flagsFromAllowedIfds4 | 131072;
        int i29 = i28 | 2;
        this.mTagInfo.put(TAG_GPS_LATITUDE_REF, i29);
        this.mTagInfo.put(TAG_GPS_LONGITUDE_REF, i29);
        int i30 = flagsFromAllowedIfds4 | 655360 | 3;
        this.mTagInfo.put(TAG_GPS_LATITUDE, i30);
        this.mTagInfo.put(TAG_GPS_LONGITUDE, i30);
        this.mTagInfo.put(TAG_GPS_ALTITUDE_REF, i27 | 1);
        int i31 = 327680 | flagsFromAllowedIfds4;
        int i32 = i31 | 1;
        this.mTagInfo.put(TAG_GPS_ALTITUDE, i32);
        this.mTagInfo.put(TAG_GPS_TIME_STAMP, i31 | 3);
        int i33 = i28 | 0;
        this.mTagInfo.put(TAG_GPS_SATTELLITES, i33);
        this.mTagInfo.put(TAG_GPS_STATUS, i29);
        this.mTagInfo.put(TAG_GPS_MEASURE_MODE, i29);
        this.mTagInfo.put(TAG_GPS_DOP, i32);
        this.mTagInfo.put(TAG_GPS_SPEED_REF, i29);
        this.mTagInfo.put(TAG_GPS_SPEED, i32);
        this.mTagInfo.put(TAG_GPS_TRACK_REF, i29);
        this.mTagInfo.put(TAG_GPS_TRACK, i32);
        this.mTagInfo.put(TAG_GPS_IMG_DIRECTION_REF, i29);
        this.mTagInfo.put(TAG_GPS_IMG_DIRECTION, i32);
        this.mTagInfo.put(TAG_GPS_MAP_DATUM, i33);
        this.mTagInfo.put(TAG_GPS_DEST_LATITUDE_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_LATITUDE, i32);
        this.mTagInfo.put(TAG_GPS_DEST_BEARING_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_BEARING, i32);
        this.mTagInfo.put(TAG_GPS_DEST_DISTANCE_REF, i29);
        this.mTagInfo.put(TAG_GPS_DEST_DISTANCE, i32);
        int i34 = 458752 | flagsFromAllowedIfds4 | 0;
        this.mTagInfo.put(TAG_GPS_PROCESSING_METHOD, i34);
        this.mTagInfo.put(TAG_GPS_AREA_INFORMATION, i34);
        this.mTagInfo.put(TAG_GPS_DATE_STAMP, i28 | 11);
        this.mTagInfo.put(TAG_GPS_DIFFERENTIAL, flagsFromAllowedIfds4 | 196608 | 11);
        this.mTagInfo.put(TAG_INTEROPERABILITY_INDEX, (getFlagsFromAllowedIfds(new int[]{3}) << 24) | 131072 | 0);
    }

    public static boolean isIfdAllowed(int i, int i2) {
        int[] ifds = IfdData.getIfds();
        int allowedIfdFlagsFromInfo = getAllowedIfdFlagsFromInfo(i);
        for (int i3 = 0; i3 < ifds.length; i3++) {
            if (i2 == ifds[i3] && ((allowedIfdFlagsFromInfo >> i3) & 1) == 1) {
                return true;
            }
        }
        return false;
    }

    public static int getFlagsFromAllowedIfds(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return 0;
        }
        int[] ifds = IfdData.getIfds();
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            int length = iArr.length;
            int i3 = 0;
            while (true) {
                if (i3 < length) {
                    if (ifds[i2] == iArr[i3]) {
                        i |= 1 << i2;
                        break;
                    }
                    i3++;
                }
            }
        }
        return i;
    }
}
