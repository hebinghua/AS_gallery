package com.miui.gallery.magic.special.effects.video.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes2.dex */
public class SUndoHelper {
    public Stack<Entry> data = new Stack<>();
    public int max;

    /* loaded from: classes2.dex */
    public static class Entry {
        public int end;
        public int start;
        public byte type;

        public static Entry build(int i, int i2, byte b) {
            Entry entry = new Entry();
            entry.start = i;
            entry.end = i2;
            entry.type = b;
            return entry;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public byte getType() {
            return this.type;
        }

        public String toString() {
            return "Entry{start=" + this.start + ", end=" + this.end + ", type=" + ((int) this.type) + '}';
        }
    }

    public SUndoHelper(int i) {
        this.max = 0;
        this.max = i;
    }

    public void addEntry(Entry entry) {
        if (entry.getStart() < 0 || entry.getEnd() < 0) {
            throw new Error("The start or end cannot be less than zero");
        }
        if (entry.getStart() >= entry.getEnd()) {
            throw new Error("The end cannot be less or equal than start");
        }
        this.data.push(entry);
    }

    public int size() {
        return this.data.size();
    }

    public Entry undo() {
        if (this.data.empty()) {
            return null;
        }
        return this.data.pop();
    }

    public final byte[] getData() {
        byte[] bArr = new byte[this.max + 1];
        for (int i = 0; i < this.data.size(); i++) {
            Entry entry = this.data.get(i);
            for (int i2 = entry.start; i2 <= entry.getEnd(); i2++) {
                bArr[i2] = entry.getType();
            }
        }
        return bArr;
    }

    public List<Entry> getValueByLine(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        byte[] data = getData();
        byte b = data[i];
        int i3 = i;
        while (i < i2) {
            byte b2 = data[i];
            if (b != b2) {
                arrayList.add(Entry.build(i3, i - 1, b));
                i3 = i;
                b = b2;
            }
            i++;
        }
        if (i3 <= i2) {
            arrayList.add(Entry.build(i3, i2 + 20, b));
        }
        return arrayList;
    }

    public List<Entry> getValueByLine(Entry entry) {
        return getValueByLine(entry.getStart(), entry.getEnd());
    }
}
