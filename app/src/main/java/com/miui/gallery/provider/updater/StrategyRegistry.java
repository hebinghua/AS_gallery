package com.miui.gallery.provider.updater;

import ch.qos.logback.core.CoreConstants;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class StrategyRegistry {
    public final Map<String, Object> mMap;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final StrategyRegistry INSTANCE = new StrategyRegistry();
    }

    public StrategyRegistry() {
        this.mMap = new HashMap(84);
    }

    public static StrategyRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized Object get(String str) {
        Object obj;
        obj = this.mMap.get(str);
        if (obj == null) {
            char c = 65535;
            int hashCode = str.hashCode();
            switch (hashCode) {
                case -1481606308:
                    if (str.equals("GalleryDBUpdater14")) {
                        c = 'R';
                        break;
                    }
                    break;
                case -1481606126:
                    if (str.equals("GalleryDBUpdater70")) {
                        c = 26;
                        break;
                    }
                    break;
                case -1481606095:
                    if (str.equals("GalleryDBUpdater80")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1481606094:
                    if (str.equals("GalleryDBUpdater81")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1481606093:
                    if (str.equals("GalleryDBUpdater82")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1481606092:
                    if (str.equals("GalleryDBUpdater83")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1481606091:
                    if (str.equals("GalleryDBUpdater84")) {
                        c = '\t';
                        break;
                    }
                    break;
                case -1481606090:
                    if (str.equals("GalleryDBUpdater85")) {
                        c = '\f';
                        break;
                    }
                    break;
                case -1481606089:
                    if (str.equals("GalleryDBUpdater86")) {
                        c = 14;
                        break;
                    }
                    break;
                case -1481606088:
                    if (str.equals("GalleryDBUpdater87")) {
                        c = 18;
                        break;
                    }
                    break;
                case -1481606087:
                    if (str.equals("GalleryDBUpdater88")) {
                        c = 19;
                        break;
                    }
                    break;
                case -1481606086:
                    if (str.equals("GalleryDBUpdater89")) {
                        c = 21;
                        break;
                    }
                    break;
                case -1481606064:
                    if (str.equals("GalleryDBUpdater90")) {
                        c = '@';
                        break;
                    }
                    break;
                case -1481606063:
                    if (str.equals("GalleryDBUpdater91")) {
                        c = 'A';
                        break;
                    }
                    break;
                case -1481606062:
                    if (str.equals("GalleryDBUpdater92")) {
                        c = 'B';
                        break;
                    }
                    break;
                case -1481606061:
                    if (str.equals("GalleryDBUpdater93")) {
                        c = 'C';
                        break;
                    }
                    break;
                case -1481606060:
                    if (str.equals("GalleryDBUpdater94")) {
                        c = 'D';
                        break;
                    }
                    break;
                case -1481606059:
                    if (str.equals("GalleryDBUpdater95")) {
                        c = 'F';
                        break;
                    }
                    break;
                case -1481606058:
                    if (str.equals("GalleryDBUpdater96")) {
                        c = 'I';
                        break;
                    }
                    break;
                case -1481606057:
                    if (str.equals("GalleryDBUpdater97")) {
                        c = 'K';
                        break;
                    }
                    break;
                case -1481606056:
                    if (str.equals("GalleryDBUpdater98")) {
                        c = 'M';
                        break;
                    }
                    break;
                case -1481606055:
                    if (str.equals("GalleryDBUpdater99")) {
                        c = 'O';
                        break;
                    }
                    break;
                case 1314844632:
                    if (str.equals("GalleryDBUpdater100")) {
                        c = '\r';
                        break;
                    }
                    break;
                case 1314844633:
                    if (str.equals("GalleryDBUpdater101")) {
                        c = 15;
                        break;
                    }
                    break;
                case 1314844634:
                    if (str.equals("GalleryDBUpdater102")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 1314844635:
                    if (str.equals("GalleryDBUpdater103")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1314844636:
                    if (str.equals("GalleryDBUpdater104")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1314844637:
                    if (str.equals("GalleryDBUpdater105")) {
                        c = 6;
                        break;
                    }
                    break;
                case 1314844638:
                    if (str.equals("GalleryDBUpdater106")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1314844639:
                    if (str.equals("GalleryDBUpdater107")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1314844640:
                    if (str.equals("GalleryDBUpdater108")) {
                        c = CoreConstants.COMMA_CHAR;
                        break;
                    }
                    break;
                case 1314844641:
                    if (str.equals("GalleryDBUpdater109")) {
                        c = CoreConstants.DASH_CHAR;
                        break;
                    }
                    break;
                case 1314844664:
                    if (str.equals("GalleryDBUpdater111")) {
                        c = 30;
                        break;
                    }
                    break;
                case 1314844665:
                    if (str.equals("GalleryDBUpdater112")) {
                        c = '!';
                        break;
                    }
                    break;
                case 1614774240:
                    if (str.equals("GalleryDBUpdater9")) {
                        c = '?';
                        break;
                    }
                    break;
                default:
                    switch (hashCode) {
                        case -1481606304:
                            if (str.equals("GalleryDBUpdater18")) {
                                c = '=';
                                break;
                            }
                            break;
                        case -1481606303:
                            if (str.equals("GalleryDBUpdater19")) {
                                c = '>';
                                break;
                            }
                            break;
                        default:
                            switch (hashCode) {
                                case -1481606280:
                                    if (str.equals("GalleryDBUpdater21")) {
                                        c = '1';
                                        break;
                                    }
                                    break;
                                case -1481606279:
                                    if (str.equals("GalleryDBUpdater22")) {
                                        c = '3';
                                        break;
                                    }
                                    break;
                                case -1481606278:
                                    if (str.equals("GalleryDBUpdater23")) {
                                        c = '5';
                                        break;
                                    }
                                    break;
                                case -1481606277:
                                    if (str.equals("GalleryDBUpdater24")) {
                                        c = '6';
                                        break;
                                    }
                                    break;
                                case -1481606276:
                                    if (str.equals("GalleryDBUpdater25")) {
                                        c = '8';
                                        break;
                                    }
                                    break;
                                case -1481606275:
                                    if (str.equals("GalleryDBUpdater26")) {
                                        c = CoreConstants.COLON_CHAR;
                                        break;
                                    }
                                    break;
                                case -1481606274:
                                    if (str.equals("GalleryDBUpdater27")) {
                                        c = ';';
                                        break;
                                    }
                                    break;
                                case -1481606273:
                                    if (str.equals("GalleryDBUpdater28")) {
                                        c = '<';
                                        break;
                                    }
                                    break;
                                default:
                                    switch (hashCode) {
                                        case -1481606249:
                                            if (str.equals("GalleryDBUpdater31")) {
                                                c = 31;
                                                break;
                                            }
                                            break;
                                        case -1481606248:
                                            if (str.equals("GalleryDBUpdater32")) {
                                                c = '#';
                                                break;
                                            }
                                            break;
                                        case -1481606247:
                                            if (str.equals("GalleryDBUpdater33")) {
                                                c = CoreConstants.PERCENT_CHAR;
                                                break;
                                            }
                                            break;
                                        default:
                                            switch (hashCode) {
                                                case -1481606245:
                                                    if (str.equals("GalleryDBUpdater35")) {
                                                        c = CoreConstants.SINGLE_QUOTE_CHAR;
                                                        break;
                                                    }
                                                    break;
                                                case -1481606244:
                                                    if (str.equals("GalleryDBUpdater36")) {
                                                        c = CoreConstants.RIGHT_PARENTHESIS_CHAR;
                                                        break;
                                                    }
                                                    break;
                                                case -1481606243:
                                                    if (str.equals("GalleryDBUpdater37")) {
                                                        c = '*';
                                                        break;
                                                    }
                                                    break;
                                                case -1481606242:
                                                    if (str.equals("GalleryDBUpdater38")) {
                                                        c = '+';
                                                        break;
                                                    }
                                                    break;
                                                default:
                                                    switch (hashCode) {
                                                        case -1481606218:
                                                            if (str.equals("GalleryDBUpdater41")) {
                                                                c = 11;
                                                                break;
                                                            }
                                                            break;
                                                        case -1481606217:
                                                            if (str.equals("GalleryDBUpdater42")) {
                                                                c = 16;
                                                                break;
                                                            }
                                                            break;
                                                        case -1481606216:
                                                            if (str.equals("GalleryDBUpdater43")) {
                                                                c = 17;
                                                                break;
                                                            }
                                                            break;
                                                        default:
                                                            switch (hashCode) {
                                                                case -1481606214:
                                                                    if (str.equals("GalleryDBUpdater45")) {
                                                                        c = 20;
                                                                        break;
                                                                    }
                                                                    break;
                                                                case -1481606213:
                                                                    if (str.equals("GalleryDBUpdater46")) {
                                                                        c = 22;
                                                                        break;
                                                                    }
                                                                    break;
                                                                case -1481606212:
                                                                    if (str.equals("GalleryDBUpdater47")) {
                                                                        c = 23;
                                                                        break;
                                                                    }
                                                                    break;
                                                                case -1481606211:
                                                                    if (str.equals("GalleryDBUpdater48")) {
                                                                        c = 24;
                                                                        break;
                                                                    }
                                                                    break;
                                                                case -1481606210:
                                                                    if (str.equals("GalleryDBUpdater49")) {
                                                                        c = 25;
                                                                        break;
                                                                    }
                                                                    break;
                                                                default:
                                                                    switch (hashCode) {
                                                                        case -1481606188:
                                                                            if (str.equals("GalleryDBUpdater50")) {
                                                                                c = 'E';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606187:
                                                                            if (str.equals("GalleryDBUpdater51")) {
                                                                                c = 'G';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606186:
                                                                            if (str.equals("GalleryDBUpdater52")) {
                                                                                c = 'H';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606185:
                                                                            if (str.equals("GalleryDBUpdater53")) {
                                                                                c = 'J';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606184:
                                                                            if (str.equals("GalleryDBUpdater54")) {
                                                                                c = 'L';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606183:
                                                                            if (str.equals("GalleryDBUpdater55")) {
                                                                                c = 'N';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606182:
                                                                            if (str.equals("GalleryDBUpdater56")) {
                                                                                c = 'P';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606181:
                                                                            if (str.equals("GalleryDBUpdater57")) {
                                                                                c = 'Q';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        case -1481606180:
                                                                            if (str.equals("GalleryDBUpdater58")) {
                                                                                c = 'S';
                                                                                break;
                                                                            }
                                                                            break;
                                                                        default:
                                                                            switch (hashCode) {
                                                                                case -1481606154:
                                                                                    if (str.equals("GalleryDBUpdater63")) {
                                                                                        c = CoreConstants.DOT;
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606153:
                                                                                    if (str.equals("GalleryDBUpdater64")) {
                                                                                        c = '/';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606152:
                                                                                    if (str.equals("GalleryDBUpdater65")) {
                                                                                        c = '0';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606151:
                                                                                    if (str.equals("GalleryDBUpdater66")) {
                                                                                        c = '2';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606150:
                                                                                    if (str.equals("GalleryDBUpdater67")) {
                                                                                        c = '4';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606149:
                                                                                    if (str.equals("GalleryDBUpdater68")) {
                                                                                        c = '7';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                case -1481606148:
                                                                                    if (str.equals("GalleryDBUpdater69")) {
                                                                                        c = '9';
                                                                                        break;
                                                                                    }
                                                                                    break;
                                                                                default:
                                                                                    switch (hashCode) {
                                                                                        case -1481606124:
                                                                                            if (str.equals("GalleryDBUpdater72")) {
                                                                                                c = 27;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606123:
                                                                                            if (str.equals("GalleryDBUpdater73")) {
                                                                                                c = 28;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606122:
                                                                                            if (str.equals("GalleryDBUpdater74")) {
                                                                                                c = 29;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606121:
                                                                                            if (str.equals("GalleryDBUpdater75")) {
                                                                                                c = ' ';
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606120:
                                                                                            if (str.equals("GalleryDBUpdater76")) {
                                                                                                c = CoreConstants.DOUBLE_QUOTE_CHAR;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606119:
                                                                                            if (str.equals("GalleryDBUpdater77")) {
                                                                                                c = CoreConstants.DOLLAR;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606118:
                                                                                            if (str.equals("GalleryDBUpdater78")) {
                                                                                                c = '&';
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                        case -1481606117:
                                                                                            if (str.equals("GalleryDBUpdater79")) {
                                                                                                c = CoreConstants.LEFT_PARENTHESIS_CHAR;
                                                                                                break;
                                                                                            }
                                                                                            break;
                                                                                    }
                                                                            }
                                                                    }
                                                            }
                                                    }
                                            }
                                    }
                            }
                    }
            }
            switch (c) {
                case 0:
                    obj = new GalleryDBUpdater106();
                    this.mMap.put(str, obj);
                    break;
                case 1:
                    obj = new GalleryDBUpdater107();
                    this.mMap.put(str, obj);
                    break;
                case 2:
                    obj = new GalleryDBUpdater80();
                    this.mMap.put(str, obj);
                    break;
                case 3:
                    obj = new GalleryDBUpdater81();
                    this.mMap.put(str, obj);
                    break;
                case 4:
                    obj = new GalleryDBUpdater104();
                    this.mMap.put(str, obj);
                    break;
                case 5:
                    obj = new GalleryDBUpdater82();
                    this.mMap.put(str, obj);
                    break;
                case 6:
                    obj = new GalleryDBUpdater105();
                    this.mMap.put(str, obj);
                    break;
                case 7:
                    obj = new GalleryDBUpdater83();
                    this.mMap.put(str, obj);
                    break;
                case '\b':
                    obj = new GalleryDBUpdater102();
                    this.mMap.put(str, obj);
                    break;
                case '\t':
                    obj = new GalleryDBUpdater84();
                    this.mMap.put(str, obj);
                    break;
                case '\n':
                    obj = new GalleryDBUpdater103();
                    this.mMap.put(str, obj);
                    break;
                case 11:
                    obj = new GalleryDBUpdater41();
                    this.mMap.put(str, obj);
                    break;
                case '\f':
                    obj = new GalleryDBUpdater85();
                    this.mMap.put(str, obj);
                    break;
                case '\r':
                    obj = new GalleryDBUpdater100();
                    this.mMap.put(str, obj);
                    break;
                case 14:
                    obj = new GalleryDBUpdater86();
                    this.mMap.put(str, obj);
                    break;
                case 15:
                    obj = new GalleryDBUpdater101();
                    this.mMap.put(str, obj);
                    break;
                case 16:
                    obj = new GalleryDBUpdater42();
                    this.mMap.put(str, obj);
                    break;
                case 17:
                    obj = new GalleryDBUpdater43();
                    this.mMap.put(str, obj);
                    break;
                case 18:
                    obj = new GalleryDBUpdater87();
                    this.mMap.put(str, obj);
                    break;
                case 19:
                    obj = new GalleryDBUpdater88();
                    this.mMap.put(str, obj);
                    break;
                case 20:
                    obj = new GalleryDBUpdater45();
                    this.mMap.put(str, obj);
                    break;
                case 21:
                    obj = new GalleryDBUpdater89();
                    this.mMap.put(str, obj);
                    break;
                case 22:
                    obj = new GalleryDBUpdater46();
                    this.mMap.put(str, obj);
                    break;
                case 23:
                    obj = new GalleryDBUpdater47();
                    this.mMap.put(str, obj);
                    break;
                case 24:
                    obj = new GalleryDBUpdater48();
                    this.mMap.put(str, obj);
                    break;
                case 25:
                    obj = new GalleryDBUpdater49();
                    this.mMap.put(str, obj);
                    break;
                case 26:
                    obj = new GalleryDBUpdater70();
                    this.mMap.put(str, obj);
                    break;
                case 27:
                    obj = new GalleryDBUpdater72();
                    this.mMap.put(str, obj);
                    break;
                case 28:
                    obj = new GalleryDBUpdater73();
                    this.mMap.put(str, obj);
                    break;
                case 29:
                    obj = new GalleryDBUpdater74();
                    this.mMap.put(str, obj);
                    break;
                case 30:
                    obj = new GalleryDBUpdater111();
                    this.mMap.put(str, obj);
                    break;
                case 31:
                    obj = new GalleryDBUpdater31();
                    this.mMap.put(str, obj);
                    break;
                case ' ':
                    obj = new GalleryDBUpdater75();
                    this.mMap.put(str, obj);
                    break;
                case '!':
                    obj = new GalleryDBUpdater112();
                    this.mMap.put(str, obj);
                    break;
                case '\"':
                    obj = new GalleryDBUpdater76();
                    this.mMap.put(str, obj);
                    break;
                case '#':
                    obj = new GalleryDBUpdater32();
                    this.mMap.put(str, obj);
                    break;
                case '$':
                    obj = new GalleryDBUpdater77();
                    this.mMap.put(str, obj);
                    break;
                case '%':
                    obj = new GalleryDBUpdater33();
                    this.mMap.put(str, obj);
                    break;
                case '&':
                    obj = new GalleryDBUpdater78();
                    this.mMap.put(str, obj);
                    break;
                case '\'':
                    obj = new GalleryDBUpdater35();
                    this.mMap.put(str, obj);
                    break;
                case '(':
                    obj = new GalleryDBUpdater79();
                    this.mMap.put(str, obj);
                    break;
                case ')':
                    obj = new GalleryDBUpdater36();
                    this.mMap.put(str, obj);
                    break;
                case '*':
                    obj = new GalleryDBUpdater37();
                    this.mMap.put(str, obj);
                    break;
                case '+':
                    obj = new GalleryDBUpdater38();
                    this.mMap.put(str, obj);
                    break;
                case ',':
                    obj = new GalleryDBUpdater108();
                    this.mMap.put(str, obj);
                    break;
                case '-':
                    obj = new GalleryDBUpdater109();
                    this.mMap.put(str, obj);
                    break;
                case '.':
                    obj = new GalleryDBUpdater63();
                    this.mMap.put(str, obj);
                    break;
                case '/':
                    obj = new GalleryDBUpdater64();
                    this.mMap.put(str, obj);
                    break;
                case '0':
                    obj = new GalleryDBUpdater65();
                    this.mMap.put(str, obj);
                    break;
                case '1':
                    obj = new GalleryDBUpdater21();
                    this.mMap.put(str, obj);
                    break;
                case '2':
                    obj = new GalleryDBUpdater66();
                    this.mMap.put(str, obj);
                    break;
                case '3':
                    obj = new GalleryDBUpdater22();
                    this.mMap.put(str, obj);
                    break;
                case '4':
                    obj = new GalleryDBUpdater67();
                    this.mMap.put(str, obj);
                    break;
                case '5':
                    obj = new GalleryDBUpdater23();
                    this.mMap.put(str, obj);
                    break;
                case '6':
                    obj = new GalleryDBUpdater24();
                    this.mMap.put(str, obj);
                    break;
                case '7':
                    obj = new GalleryDBUpdater68();
                    this.mMap.put(str, obj);
                    break;
                case '8':
                    obj = new GalleryDBUpdater25();
                    this.mMap.put(str, obj);
                    break;
                case '9':
                    obj = new GalleryDBUpdater69();
                    this.mMap.put(str, obj);
                    break;
                case ':':
                    obj = new GalleryDBUpdater26();
                    this.mMap.put(str, obj);
                    break;
                case ';':
                    obj = new GalleryDBUpdater27();
                    this.mMap.put(str, obj);
                    break;
                case '<':
                    obj = new GalleryDBUpdater28();
                    this.mMap.put(str, obj);
                    break;
                case '=':
                    obj = new GalleryDBUpdater18();
                    this.mMap.put(str, obj);
                    break;
                case '>':
                    obj = new GalleryDBUpdater19();
                    this.mMap.put(str, obj);
                    break;
                case '?':
                    obj = new GalleryDBUpdater9();
                    this.mMap.put(str, obj);
                    break;
                case '@':
                    obj = new GalleryDBUpdater90();
                    this.mMap.put(str, obj);
                    break;
                case 'A':
                    obj = new GalleryDBUpdater91();
                    this.mMap.put(str, obj);
                    break;
                case 'B':
                    obj = new GalleryDBUpdater92();
                    this.mMap.put(str, obj);
                    break;
                case 'C':
                    obj = new GalleryDBUpdater93();
                    this.mMap.put(str, obj);
                    break;
                case 'D':
                    obj = new GalleryDBUpdater94();
                    this.mMap.put(str, obj);
                    break;
                case 'E':
                    obj = new GalleryDBUpdater50();
                    this.mMap.put(str, obj);
                    break;
                case 'F':
                    obj = new GalleryDBUpdater95();
                    this.mMap.put(str, obj);
                    break;
                case 'G':
                    obj = new GalleryDBUpdater51();
                    this.mMap.put(str, obj);
                    break;
                case 'H':
                    obj = new GalleryDBUpdater52();
                    this.mMap.put(str, obj);
                    break;
                case 'I':
                    obj = new GalleryDBUpdater96();
                    this.mMap.put(str, obj);
                    break;
                case 'J':
                    obj = new GalleryDBUpdater53();
                    this.mMap.put(str, obj);
                    break;
                case 'K':
                    obj = new GalleryDBUpdater97();
                    this.mMap.put(str, obj);
                    break;
                case 'L':
                    obj = new GalleryDBUpdater54();
                    this.mMap.put(str, obj);
                    break;
                case 'M':
                    obj = new GalleryDBUpdater98();
                    this.mMap.put(str, obj);
                    break;
                case 'N':
                    obj = new GalleryDBUpdater55();
                    this.mMap.put(str, obj);
                    break;
                case 'O':
                    obj = new GalleryDBUpdater99();
                    this.mMap.put(str, obj);
                    break;
                case 'P':
                    obj = new GalleryDBUpdater56();
                    this.mMap.put(str, obj);
                    break;
                case 'Q':
                    obj = new GalleryDBUpdater57();
                    this.mMap.put(str, obj);
                    break;
                case 'R':
                    obj = new GalleryDBUpdater14();
                    this.mMap.put(str, obj);
                    break;
                case 'S':
                    obj = new GalleryDBUpdater58();
                    this.mMap.put(str, obj);
                    break;
            }
        }
        return obj;
    }
}
