package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Keep
/* loaded from: classes3.dex */
public class MetaBox extends NodeBox {
    private static final String FOURCC = "meta";

    public static String fourcc() {
        return "meta";
    }

    public MetaBox(Header header) {
        super(header);
    }

    public static MetaBox createMetaBox() {
        return new MetaBox(Header.createHeader(fourcc(), 0L));
    }

    public Map<String, MetaValue> getKeyedMeta() {
        DataBox dataBox;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        IListBox iListBox = (IListBox) NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
        MdtaBox[] mdtaBoxArr = (MdtaBox[]) NodeBox.findAllPath(this, MdtaBox.class, new String[]{KeysBox.fourcc(), MdtaBox.fourcc()});
        if (iListBox != null && mdtaBoxArr.length != 0) {
            for (Map.Entry<Integer, List<Box>> entry : iListBox.getValues().entrySet()) {
                Integer key = entry.getKey();
                if (key != null && (dataBox = getDataBox(entry.getValue())) != null) {
                    MetaValue createOtherWithLocale = MetaValue.createOtherWithLocale(dataBox.getType(), dataBox.getLocale(), dataBox.getData());
                    if (key.intValue() > 0 && key.intValue() <= mdtaBoxArr.length) {
                        linkedHashMap.put(mdtaBoxArr[key.intValue() - 1].getKey(), createOtherWithLocale);
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private DataBox getDataBox(List<Box> list) {
        for (Box box : list) {
            if (box instanceof DataBox) {
                return (DataBox) box;
            }
        }
        return null;
    }

    public void setKeyedMeta(Map<String, MetaValue> map) {
        if (map.isEmpty()) {
            return;
        }
        KeysBox createKeysBox = KeysBox.createKeysBox();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i = 1;
        for (Map.Entry<String, MetaValue> entry : map.entrySet()) {
            createKeysBox.add(MdtaBox.createMdtaBox(entry.getKey()));
            MetaValue value = entry.getValue();
            ArrayList arrayList = new ArrayList();
            arrayList.add(DataBox.createDataBox(value.getType(), value.getLocale(), value.getData()));
            linkedHashMap.put(Integer.valueOf(i), arrayList);
            i++;
        }
        Box createIListBox = IListBox.createIListBox(linkedHashMap);
        replaceBox(createKeysBox);
        replaceBox(createIListBox);
    }
}
