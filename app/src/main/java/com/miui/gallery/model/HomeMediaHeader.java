package com.miui.gallery.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.JsonIOException;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class HomeMediaHeader extends Entity {
    public static final String[] PROJECTION = {"groupItemCount", "groupItemStartPos", "groupLocations"};
    public String mGroupItemCount;
    public String mGroupItemStartPos;
    public String mGroupLocations;

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        for (String str : PROJECTION) {
            Entity.addColumn(arrayList, str, "TEXT");
        }
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        String[] strArr = PROJECTION;
        this.mGroupItemCount = Entity.getString(cursor, strArr[0]);
        this.mGroupItemStartPos = Entity.getString(cursor, strArr[1]);
        this.mGroupLocations = Entity.getString(cursor, strArr[2]);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        String[] strArr = PROJECTION;
        contentValues.put(strArr[0], this.mGroupItemCount);
        contentValues.put(strArr[1], this.mGroupItemStartPos);
        contentValues.put(strArr[2], this.mGroupLocations);
    }

    public static ArrayList<Integer> getGroupItemCount(HomeMediaHeader homeMediaHeader) {
        if (homeMediaHeader == null || TextUtils.isEmpty(homeMediaHeader.mGroupItemCount)) {
            return null;
        }
        Iterable<String> split = Splitter.on((char) CoreConstants.COMMA_CHAR).split(homeMediaHeader.mGroupItemCount);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String str : split) {
            arrayList.add(Integer.valueOf(Integer.parseInt(str)));
        }
        return arrayList;
    }

    public static ArrayList<Integer> getGroupItemStartPos(HomeMediaHeader homeMediaHeader) {
        if (homeMediaHeader == null || TextUtils.isEmpty(homeMediaHeader.mGroupItemStartPos)) {
            return null;
        }
        Iterable<String> split = Splitter.on((char) CoreConstants.COMMA_CHAR).split(homeMediaHeader.mGroupItemStartPos);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String str : split) {
            arrayList.add(Integer.valueOf(Integer.parseInt(str)));
        }
        return arrayList;
    }

    public static ArrayList<String> getGroupLocations(HomeMediaHeader homeMediaHeader) {
        if (homeMediaHeader == null || TextUtils.isEmpty(homeMediaHeader.mGroupLocations)) {
            return null;
        }
        Iterable<String> split = Splitter.on((char) CoreConstants.COMMA_CHAR).split(homeMediaHeader.mGroupLocations);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : split) {
            arrayList.add(str);
        }
        return arrayList;
    }

    public static HomeMediaHeader packageMediaHeader(int i, List<Integer> list, List<Integer> list2, List<String> list3) throws JsonIOException {
        HomeMediaHeader homeMediaHeader = new HomeMediaHeader();
        if (i > 0) {
            Iterator<Integer> it = list.iterator();
            int i2 = 0;
            while (true) {
                if (!it.hasNext()) {
                    i = 0;
                    break;
                }
                Integer next = it.next();
                if (i <= next.intValue()) {
                    break;
                }
                i -= next.intValue();
                i2++;
            }
            Joiner useForNull = Joiner.on((char) CoreConstants.COMMA_CHAR).useForNull("");
            homeMediaHeader.mGroupItemCount = useForNull.join(list.subList(0, i2));
            if (i > 0) {
                if (i2 > 0) {
                    homeMediaHeader.mGroupItemCount += String.valueOf((char) CoreConstants.COMMA_CHAR) + i;
                } else {
                    homeMediaHeader.mGroupItemCount += i;
                }
                int i3 = i2 + 1;
                homeMediaHeader.mGroupItemStartPos = useForNull.join(list2.subList(0, i3));
                homeMediaHeader.mGroupLocations = useForNull.join(list3.subList(0, i3));
            } else {
                homeMediaHeader.mGroupItemStartPos = useForNull.join(list2.subList(0, i2));
                homeMediaHeader.mGroupLocations = useForNull.join(list3.subList(0, i2));
            }
        }
        return homeMediaHeader;
    }
}
