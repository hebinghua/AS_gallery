package com.miui.gallery.signature.core;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes2.dex */
public class BitmapUtil {
    public static Bitmap clearBlank(Bitmap bitmap, int i, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int[] iArr = new int[width];
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < height; i5++) {
                bitmap.getPixels(iArr, 0, width, 0, i5, width, 1);
                int i6 = 0;
                while (true) {
                    if (i6 >= width) {
                        z4 = false;
                        break;
                    } else if (iArr[i6] != i2) {
                        i4 = i5;
                        z4 = true;
                        break;
                    } else {
                        i6++;
                    }
                }
                if (z4) {
                    break;
                }
            }
            int i7 = height - 1;
            int i8 = 0;
            for (int i9 = i7; i9 >= 0; i9--) {
                bitmap.getPixels(iArr, 0, width, 0, i9, width, 1);
                int i10 = 0;
                while (true) {
                    if (i10 >= width) {
                        z3 = false;
                        break;
                    } else if (iArr[i10] != i2) {
                        z3 = true;
                        i8 = i9;
                        break;
                    } else {
                        i10++;
                    }
                }
                if (z3) {
                    break;
                }
            }
            int[] iArr2 = new int[height];
            int i11 = 0;
            int i12 = 0;
            while (i11 < width) {
                int i13 = i11;
                bitmap.getPixels(iArr2, 0, 1, i11, 0, 1, height);
                int i14 = 0;
                while (true) {
                    if (i14 >= height) {
                        z2 = false;
                        break;
                    } else if (iArr2[i14] != i2) {
                        z2 = true;
                        i12 = i13;
                        break;
                    } else {
                        i14++;
                    }
                }
                if (z2) {
                    break;
                }
                i11 = i13 + 1;
            }
            int i15 = width - 1;
            int i16 = 0;
            for (int i17 = i15; i17 > 0; i17--) {
                bitmap.getPixels(iArr2, 0, 1, i17, 0, 1, height);
                int i18 = 0;
                while (true) {
                    if (i18 >= height) {
                        z = false;
                        break;
                    } else if (iArr2[i18] != i2) {
                        z = true;
                        i16 = i17;
                        break;
                    } else {
                        i18++;
                    }
                }
                if (z) {
                    break;
                }
            }
            int i19 = i < 0 ? 0 : i;
            int i20 = i12 - i19;
            int i21 = i20 > 0 ? i20 : 0;
            int i22 = i4 - i19;
            if (i22 > 0) {
                i3 = i22;
            }
            int i23 = i16 + i19;
            if (i23 <= i15) {
                i15 = i23;
            }
            int i24 = i8 + i19;
            if (i24 <= i7) {
                i7 = i24;
            }
            return Bitmap.createBitmap(bitmap, i21, i3, i15 - i21, i7 - i3);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.graphics.Bitmap] */
    public static String saveImage(Bitmap bitmap, int i, String str, String str2) {
        FileOutputStream fileOutputStream;
        ?? r0 = 0;
        try {
            try {
                if (bitmap == 0) {
                    return null;
                }
                try {
                    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
                    if ("JPG".equals(str2)) {
                        compressFormat = Bitmap.CompressFormat.JPEG;
                    }
                    File file = new File(str);
                    fileOutputStream = new FileOutputStream(file);
                    try {
                        bitmap.compress(compressFormat, i, fileOutputStream);
                        fileOutputStream.flush();
                        String absolutePath = file.getAbsolutePath();
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return absolutePath;
                    } catch (FileNotFoundException e2) {
                        e = e2;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        return null;
                    } catch (Exception e3) {
                        e = e3;
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        return null;
                    }
                } catch (FileNotFoundException e4) {
                    e = e4;
                    fileOutputStream = null;
                } catch (Exception e5) {
                    e = e5;
                    fileOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    if (r0 != 0) {
                        try {
                            r0.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
            r0 = str;
        }
    }
}
