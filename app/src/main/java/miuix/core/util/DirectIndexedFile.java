package miuix.core.util;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/* loaded from: classes3.dex */
public class DirectIndexedFile {

    /* loaded from: classes3.dex */
    public interface InputFile extends DataInput {
        void close() throws IOException;

        long getFilePointer() throws IOException;

        void seek(long j) throws IOException;
    }

    /* loaded from: classes3.dex */
    public static class IndexGroupDescriptor {
        public int mMaxIndex;
        public int mMinIndex;
        public long mOffset;

        public IndexGroupDescriptor(int i, int i2, long j) {
            this.mMinIndex = i;
            this.mMaxIndex = i2;
            this.mOffset = j;
        }

        public static IndexGroupDescriptor read(DataInput dataInput) throws IOException {
            return new IndexGroupDescriptor(dataInput.readInt(), dataInput.readInt(), dataInput.readLong());
        }
    }

    /* loaded from: classes3.dex */
    public static class DataItemDescriptor {
        public static byte[] sBuffer = new byte[1024];
        public byte mIndexSize;
        public byte mLengthSize;
        public long mOffset;
        public byte mOffsetSize;
        public Type mType;

        /* loaded from: classes3.dex */
        public enum Type {
            BYTE,
            SHORT,
            INTEGER,
            LONG,
            STRING,
            BYTE_ARRAY,
            SHORT_ARRAY,
            INTEGER_ARRAY,
            LONG_ARRAY
        }

        public static byte[] aquireBuffer(int i) {
            byte[] bArr;
            synchronized (DataItemDescriptor.class) {
                byte[] bArr2 = sBuffer;
                if (bArr2 == null || bArr2.length < i) {
                    sBuffer = new byte[i];
                }
                bArr = sBuffer;
                sBuffer = null;
            }
            return bArr;
        }

        public static void releaseBuffer(byte[] bArr) {
            synchronized (DataItemDescriptor.class) {
                if (bArr != null) {
                    byte[] bArr2 = sBuffer;
                    if (bArr2 == null || bArr2.length < bArr.length) {
                        sBuffer = bArr;
                    }
                }
            }
        }

        public DataItemDescriptor(Type type, byte b, byte b2, byte b3, long j) {
            this.mType = type;
            this.mIndexSize = b;
            this.mLengthSize = b2;
            this.mOffsetSize = b3;
            this.mOffset = j;
        }

        public static DataItemDescriptor read(DataInput dataInput) throws IOException {
            return new DataItemDescriptor(Type.values()[dataInput.readByte()], dataInput.readByte(), dataInput.readByte(), dataInput.readByte(), dataInput.readLong());
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v1 */
        /* JADX WARN: Type inference failed for: r1v11, types: [long[]] */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v5 */
        /* JADX WARN: Type inference failed for: r1v9, types: [int[]] */
        public final Object readSingleDataItem(InputFile inputFile, int i) throws IOException {
            short[] str;
            long filePointer = inputFile.getFilePointer();
            if (i != 0) {
                inputFile.seek((this.mOffsetSize * i) + filePointer);
            }
            inputFile.seek(filePointer + readAccordingToSize(inputFile, this.mOffsetSize));
            int i2 = AnonymousClass1.$SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()];
            byte[] bArr = null;
            int i3 = 0;
            if (i2 == 1) {
                int readAccordingToSize = (int) readAccordingToSize(inputFile, this.mLengthSize);
                bArr = aquireBuffer(readAccordingToSize);
                inputFile.readFully(bArr, 0, readAccordingToSize);
                str = new String(bArr, 0, readAccordingToSize);
            } else if (i2 == 2) {
                byte[] bArr2 = new byte[(int) readAccordingToSize(inputFile, this.mLengthSize)];
                inputFile.readFully(bArr2);
                str = bArr2;
            } else if (i2 == 3) {
                int readAccordingToSize2 = (int) readAccordingToSize(inputFile, this.mLengthSize);
                str = new short[readAccordingToSize2];
                while (i3 < readAccordingToSize2) {
                    str[i3] = inputFile.readShort();
                    i3++;
                }
            } else if (i2 == 4) {
                int readAccordingToSize3 = (int) readAccordingToSize(inputFile, this.mLengthSize);
                str = new int[readAccordingToSize3];
                while (i3 < readAccordingToSize3) {
                    str[i3] = inputFile.readInt();
                    i3++;
                }
            } else if (i2 != 5) {
                str = 0;
            } else {
                int readAccordingToSize4 = (int) readAccordingToSize(inputFile, this.mLengthSize);
                str = new long[readAccordingToSize4];
                while (i3 < readAccordingToSize4) {
                    str[i3] = inputFile.readLong();
                    i3++;
                }
            }
            releaseBuffer(bArr);
            return str;
        }

        public final Object[] readDataItems(InputFile inputFile) throws IOException {
            switch (AnonymousClass1.$SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    Object[] objArr = new Object[inputFile.readInt()];
                    objArr[0] = readSingleDataItem(inputFile, 0);
                    return objArr;
                case 6:
                    return new Object[]{Byte.valueOf(inputFile.readByte())};
                case 7:
                    return new Object[]{Short.valueOf(inputFile.readShort())};
                case 8:
                    return new Object[]{Integer.valueOf(inputFile.readInt())};
                case 9:
                    return new Object[]{Long.valueOf(inputFile.readLong())};
                default:
                    return null;
            }
        }

        public static long readAccordingToSize(DataInput dataInput, int i) throws IOException {
            int readByte;
            if (i == 1) {
                readByte = dataInput.readByte();
            } else if (i == 2) {
                readByte = dataInput.readShort();
            } else if (i != 4) {
                if (i == 8) {
                    return dataInput.readLong();
                }
                throw new IllegalArgumentException("Unsuppoert size " + i);
            } else {
                readByte = dataInput.readInt();
            }
            return readByte;
        }
    }

    /* renamed from: miuix.core.util.DirectIndexedFile$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type;

        static {
            int[] iArr = new int[DataItemDescriptor.Type.values().length];
            $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type = iArr;
            try {
                iArr[DataItemDescriptor.Type.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.BYTE_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.SHORT_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.INTEGER_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.LONG_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.BYTE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.SHORT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.INTEGER.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[DataItemDescriptor.Type.LONG.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DescriptionPair {
        public long mDataItemDescriptionOffset;
        public long mIndexGroupDescriptionOffset;

        public DescriptionPair(long j, long j2) {
            this.mIndexGroupDescriptionOffset = j;
            this.mDataItemDescriptionOffset = j2;
        }

        public static DescriptionPair read(DataInput dataInput) throws IOException {
            return new DescriptionPair(dataInput.readLong(), dataInput.readLong());
        }
    }

    /* loaded from: classes3.dex */
    public static class FileHeader {
        public static final byte[] FILE_TAG = {73, 68, 70, 32};
        public int mDataVersion;
        public DescriptionPair[] mDescriptionOffsets;

        public FileHeader(int i, int i2) {
            this.mDescriptionOffsets = new DescriptionPair[i];
            this.mDataVersion = i2;
        }

        public static FileHeader read(DataInput dataInput) throws IOException {
            int length = FILE_TAG.length;
            byte[] bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                bArr[i] = dataInput.readByte();
            }
            if (!Arrays.equals(bArr, FILE_TAG)) {
                throw new IOException("File tag unmatched, file may be corrupt");
            }
            if (dataInput.readInt() != 2) {
                throw new IOException("File version unmatched, please upgrade your reader");
            }
            int readInt = dataInput.readInt();
            FileHeader fileHeader = new FileHeader(readInt, dataInput.readInt());
            for (int i2 = 0; i2 < readInt; i2++) {
                fileHeader.mDescriptionOffsets[i2] = DescriptionPair.read(dataInput);
            }
            return fileHeader;
        }
    }

    /* loaded from: classes3.dex */
    public static class DataInputRandom implements InputFile {
        public RandomAccessFile mFile;

        public DataInputRandom(RandomAccessFile randomAccessFile) {
            this.mFile = randomAccessFile;
        }

        @Override // java.io.DataInput
        public boolean readBoolean() throws IOException {
            return this.mFile.readBoolean();
        }

        @Override // java.io.DataInput
        public byte readByte() throws IOException {
            return this.mFile.readByte();
        }

        @Override // java.io.DataInput
        public char readChar() throws IOException {
            return this.mFile.readChar();
        }

        @Override // java.io.DataInput
        public double readDouble() throws IOException {
            return this.mFile.readDouble();
        }

        @Override // java.io.DataInput
        public float readFloat() throws IOException {
            return this.mFile.readFloat();
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr) throws IOException {
            this.mFile.readFully(bArr);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mFile.readFully(bArr, i, i2);
        }

        @Override // java.io.DataInput
        public int readInt() throws IOException {
            return this.mFile.readInt();
        }

        @Override // java.io.DataInput
        public String readLine() throws IOException {
            return this.mFile.readLine();
        }

        @Override // java.io.DataInput
        public long readLong() throws IOException {
            return this.mFile.readLong();
        }

        @Override // java.io.DataInput
        public short readShort() throws IOException {
            return this.mFile.readShort();
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() throws IOException {
            return this.mFile.readUnsignedByte();
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() throws IOException {
            return this.mFile.readUnsignedShort();
        }

        @Override // java.io.DataInput
        public String readUTF() throws IOException {
            return this.mFile.readUTF();
        }

        @Override // java.io.DataInput
        public int skipBytes(int i) throws IOException {
            return this.mFile.skipBytes(i);
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public void seek(long j) throws IOException {
            this.mFile.seek(j);
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public void close() throws IOException {
            this.mFile.close();
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public long getFilePointer() throws IOException {
            return this.mFile.getFilePointer();
        }
    }

    /* loaded from: classes3.dex */
    public static class DataInputStream implements InputFile {
        public InputStream mInputFile;
        public long mInputPos;

        public DataInputStream(InputStream inputStream) {
            this.mInputFile = inputStream;
            inputStream.mark(0);
            this.mInputPos = 0L;
        }

        @Override // java.io.DataInput
        public boolean readBoolean() throws IOException {
            this.mInputPos++;
            return this.mInputFile.read() != 0;
        }

        @Override // java.io.DataInput
        public byte readByte() throws IOException {
            this.mInputPos++;
            return (byte) this.mInputFile.read();
        }

        @Override // java.io.DataInput
        public char readChar() throws IOException {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (char) (((char) (bArr[1] & 255)) | ((bArr[0] << 8) & 65280));
            }
            return (char) 0;
        }

        @Override // java.io.DataInput
        public double readDouble() throws IOException {
            throw new IOException();
        }

        @Override // java.io.DataInput
        public float readFloat() throws IOException {
            throw new IOException();
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr) throws IOException {
            this.mInputPos += this.mInputFile.read(bArr);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mInputPos += this.mInputFile.read(bArr, i, i2);
        }

        @Override // java.io.DataInput
        public int readInt() throws IOException {
            byte[] bArr = new byte[4];
            this.mInputPos += 4;
            if (this.mInputFile.read(bArr) == 4) {
                return (bArr[3] & 255) | ((bArr[2] << 8) & 65280) | ((bArr[1] << 16) & 16711680) | ((bArr[0] << 24) & (-16777216));
            }
            return 0;
        }

        @Override // java.io.DataInput
        public String readLine() throws IOException {
            throw new IOException();
        }

        @Override // java.io.DataInput
        public long readLong() throws IOException {
            byte[] bArr = new byte[8];
            this.mInputPos += 8;
            if (this.mInputFile.read(bArr) == 8) {
                return ((bArr[0] << 56) & (-72057594037927936L)) | (bArr[7] & 255) | ((bArr[6] << 8) & 65280) | ((bArr[5] << 16) & 16711680) | ((bArr[4] << 24) & 4278190080L) | ((bArr[3] << 32) & 1095216660480L) | ((bArr[2] << 40) & 280375465082880L) | ((bArr[1] << 48) & 71776119061217280L);
            }
            return 0L;
        }

        @Override // java.io.DataInput
        public short readShort() throws IOException {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (short) (((short) (bArr[1] & 255)) | ((bArr[0] << 8) & 65280));
            }
            return (short) 0;
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() throws IOException {
            this.mInputPos++;
            return (byte) this.mInputFile.read();
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() throws IOException {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (short) (((short) (bArr[1] & 255)) | ((bArr[0] << 8) & 65280));
            }
            return 0;
        }

        @Override // java.io.DataInput
        public String readUTF() throws IOException {
            throw new IOException();
        }

        @Override // java.io.DataInput
        public int skipBytes(int i) throws IOException {
            int skip = (int) this.mInputFile.skip(i);
            this.mInputPos += skip;
            return skip;
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public void seek(long j) throws IOException {
            this.mInputFile.reset();
            if (this.mInputFile.skip(j) == j) {
                this.mInputPos = j;
                return;
            }
            throw new IOException("Skip failed");
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public void close() throws IOException {
            this.mInputFile.close();
        }

        @Override // miuix.core.util.DirectIndexedFile.InputFile
        public long getFilePointer() throws IOException {
            return this.mInputPos;
        }
    }

    /* loaded from: classes3.dex */
    public static class Reader {
        public InputFile mFile;
        public FileHeader mHeader;
        public IndexData[] mIndexData;

        /* loaded from: classes3.dex */
        public static class IndexData {
            public DataItemDescriptor[] mDataItemDescriptions;
            public Object[][] mDataItems;
            public IndexGroupDescriptor[] mIndexGroupDescriptions;
            public int mSizeOfItems;

            public IndexData() {
            }

            public /* synthetic */ IndexData(AnonymousClass1 anonymousClass1) {
                this();
            }
        }

        public /* synthetic */ Reader(InputStream inputStream, AnonymousClass1 anonymousClass1) throws IOException {
            this(inputStream);
        }

        public /* synthetic */ Reader(String str, AnonymousClass1 anonymousClass1) throws IOException {
            this(str);
        }

        public Reader(InputStream inputStream) throws IOException {
            this.mFile = new DataInputStream(inputStream);
            constructFromFile("assets");
        }

        public Reader(String str) throws IOException {
            this.mFile = new DataInputRandom(new RandomAccessFile(str, "r"));
            constructFromFile(str);
        }

        public final void constructFromFile(String str) throws IOException {
            System.currentTimeMillis();
            try {
                this.mFile.seek(0L);
                FileHeader read = FileHeader.read(this.mFile);
                this.mHeader = read;
                this.mIndexData = new IndexData[read.mDescriptionOffsets.length];
                for (int i = 0; i < this.mHeader.mDescriptionOffsets.length; i++) {
                    this.mIndexData[i] = new IndexData(null);
                    this.mFile.seek(this.mHeader.mDescriptionOffsets[i].mIndexGroupDescriptionOffset);
                    int readInt = this.mFile.readInt();
                    this.mIndexData[i].mIndexGroupDescriptions = new IndexGroupDescriptor[readInt];
                    for (int i2 = 0; i2 < readInt; i2++) {
                        this.mIndexData[i].mIndexGroupDescriptions[i2] = IndexGroupDescriptor.read(this.mFile);
                    }
                    this.mFile.seek(this.mHeader.mDescriptionOffsets[i].mDataItemDescriptionOffset);
                    int readInt2 = this.mFile.readInt();
                    this.mIndexData[i].mSizeOfItems = 0;
                    this.mIndexData[i].mDataItemDescriptions = new DataItemDescriptor[readInt2];
                    for (int i3 = 0; i3 < readInt2; i3++) {
                        this.mIndexData[i].mDataItemDescriptions[i3] = DataItemDescriptor.read(this.mFile);
                        this.mIndexData[i].mSizeOfItems += this.mIndexData[i].mDataItemDescriptions[i3].mIndexSize;
                    }
                    this.mIndexData[i].mDataItems = new Object[readInt2];
                    for (int i4 = 0; i4 < readInt2; i4++) {
                        this.mFile.seek(this.mIndexData[i].mDataItemDescriptions[i4].mOffset);
                        this.mIndexData[i].mDataItems[i4] = this.mIndexData[i].mDataItemDescriptions[i4].readDataItems(this.mFile);
                    }
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        public synchronized Object get(int i, int i2, int i3) {
            Object obj;
            if (this.mFile == null) {
                throw new IllegalStateException("Get data from a corrupt file");
            }
            if (i >= 0) {
                IndexData[] indexDataArr = this.mIndexData;
                if (i < indexDataArr.length) {
                    if (i3 < 0 || i3 >= indexDataArr[i].mDataItemDescriptions.length) {
                        throw new IllegalArgumentException("DataIndex " + i3 + " out of range[0, " + this.mIndexData[i].mDataItemDescriptions.length + ")");
                    }
                    System.currentTimeMillis();
                    long offset = offset(i, i2);
                    Object obj2 = null;
                    if (offset < 0) {
                        obj = this.mIndexData[i].mDataItems[i3][0];
                    } else {
                        try {
                            this.mFile.seek(offset);
                            for (int i4 = 0; i4 <= i3; i4++) {
                                switch (AnonymousClass1.$SwitchMap$miuix$core$util$DirectIndexedFile$DataItemDescriptor$Type[this.mIndexData[i].mDataItemDescriptions[i4].mType.ordinal()]) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        try {
                                            int readAccordingToSize = (int) DataItemDescriptor.readAccordingToSize(this.mFile, this.mIndexData[i].mDataItemDescriptions[i4].mIndexSize);
                                            if (i4 == i3) {
                                                obj2 = readSingleDataItem(i, i3, readAccordingToSize);
                                                break;
                                            } else {
                                                break;
                                            }
                                        } catch (IOException e) {
                                            throw new IllegalStateException("File may be corrupt due to invalid data index size", e);
                                        }
                                    case 6:
                                        obj2 = Byte.valueOf(this.mFile.readByte());
                                        break;
                                    case 7:
                                        obj2 = Short.valueOf(this.mFile.readShort());
                                        break;
                                    case 8:
                                        obj2 = Integer.valueOf(this.mFile.readInt());
                                        break;
                                    case 9:
                                        obj2 = Long.valueOf(this.mFile.readLong());
                                        break;
                                    default:
                                        throw new IllegalStateException("Unknown type " + this.mIndexData[i].mDataItemDescriptions[i4].mType);
                                }
                            }
                            obj = obj2;
                        } catch (IOException e2) {
                            throw new IllegalStateException("Seek data from a corrupt file", e2);
                        }
                    }
                }
            }
            throw new IllegalArgumentException("Kind " + i + " out of range[0, " + this.mIndexData.length + ")");
            return obj;
        }

        public synchronized void close() {
            InputFile inputFile = this.mFile;
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException unused) {
                }
            }
            this.mFile = null;
            this.mHeader = null;
            this.mIndexData = null;
        }

        public final Object readSingleDataItem(int i, int i2, int i3) throws IOException {
            if (this.mIndexData[i].mDataItems[i2][i3] == null) {
                this.mFile.seek(this.mIndexData[i].mDataItemDescriptions[i2].mOffset + 4);
                this.mIndexData[i].mDataItems[i2][i3] = this.mIndexData[i].mDataItemDescriptions[i2].readSingleDataItem(this.mFile, i3);
            }
            return this.mIndexData[i].mDataItems[i2][i3];
        }

        public final long offset(int i, int i2) {
            IndexGroupDescriptor indexGroupDescriptor;
            int length = this.mIndexData[i].mIndexGroupDescriptions.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    indexGroupDescriptor = null;
                    break;
                }
                int i4 = (length + i3) / 2;
                if (this.mIndexData[i].mIndexGroupDescriptions[i4].mMinIndex <= i2) {
                    if (this.mIndexData[i].mIndexGroupDescriptions[i4].mMaxIndex > i2) {
                        indexGroupDescriptor = this.mIndexData[i].mIndexGroupDescriptions[i4];
                        break;
                    }
                    i3 = i4 + 1;
                } else {
                    length = i4;
                }
            }
            if (indexGroupDescriptor != null) {
                return indexGroupDescriptor.mOffset + ((i2 - indexGroupDescriptor.mMinIndex) * this.mIndexData[i].mSizeOfItems);
            }
            return -1L;
        }
    }

    public static Reader open(String str) throws IOException {
        return new Reader(str, (AnonymousClass1) null);
    }

    public static Reader open(InputStream inputStream) throws IOException {
        return new Reader(inputStream, (AnonymousClass1) null);
    }
}
