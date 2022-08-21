package com.miui.gallery.card.scenario.time.holiday;

import android.content.res.Resources;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.assistant.HolidaysUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class HolidayScenario extends BaseHolidayScenario {
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00c8, code lost:
        return false;
     */
    @Override // com.miui.gallery.card.scenario.Scenario
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onPrepare(java.util.List<com.miui.gallery.card.scenario.Record> r12, java.util.List<com.miui.gallery.card.Card> r13) {
        /*
            r11 = this;
            r0 = 1
            java.util.List r12 = r11.getRecordStartTimesFromRecordAndCards(r12, r13, r0)
            miuix.pickerwidget.date.Calendar r13 = new miuix.pickerwidget.date.Calendar
            r13.<init>()
            r1 = r0
        Lb:
            r2 = 5
            r3 = 0
            if (r1 > r2) goto Lc8
            long r4 = com.miui.gallery.card.scenario.DateUtils.getCurrentTimeMillis()
            long r6 = (long) r1
            r8 = 86400000(0x5265c00, double:4.2687272E-316)
            long r6 = r6 * r8
            long r4 = r4 - r6
            r13.setTimeInMillis(r4)
            com.miui.gallery.util.assistant.HolidaysUtil$Holiday r2 = com.miui.gallery.util.assistant.HolidaysUtil.getHoliday(r13)
            com.miui.gallery.util.assistant.HolidaysUtil$Holiday r6 = com.miui.gallery.util.assistant.HolidaysUtil.Holiday.E_HOLIDAY_NONE
            if (r2 != r6) goto L2a
            com.miui.gallery.util.assistant.HolidaysUtil$Holiday r2 = com.miui.gallery.util.assistant.HolidaysUtil.getChineseHoliday(r4)
            r7 = r0
            goto L2b
        L2a:
            r7 = r3
        L2b:
            r10 = 2
            long[] r10 = new long[r10]
            if (r2 == r6) goto Lc4
            r13 = 1051(0x41b, float:1.473E-42)
            if (r7 == 0) goto L3a
            int r1 = r11.mScenarioId
            if (r1 < r13) goto L3a
            goto Lc8
        L3a:
            if (r7 != 0) goto L42
            int r1 = r11.mScenarioId
            if (r1 >= r13) goto L42
            goto Lc8
        L42:
            int r13 = r2.getKey()
            int r1 = r11.mHolidayId
            int r6 = r2.getTotalDay()
            int r1 = r1 + r6
            int r1 = r1 - r0
            if (r13 == r1) goto L52
            goto Lc8
        L52:
            int r13 = r2.getTotalDay()
            if (r13 != r0) goto L84
            com.miui.gallery.util.assistant.HolidaysUtil$Holiday r13 = com.miui.gallery.util.assistant.HolidaysUtil.Holiday.E_SOLAR_CHRISTMAS_DAY
            if (r2 == r13) goto L6d
            com.miui.gallery.util.assistant.HolidaysUtil$Holiday r13 = com.miui.gallery.util.assistant.HolidaysUtil.Holiday.E_SOLAR_HALLOWEEN_DAY
            if (r2 != r13) goto L61
            goto L6d
        L61:
            long r1 = com.miui.gallery.card.scenario.DateUtils.getDateTime(r4)
            r10[r3] = r1
            r1 = r10[r3]
            long r1 = r1 + r8
            r10[r0] = r1
            goto L7c
        L6d:
            long r1 = com.miui.gallery.card.scenario.DateUtils.getDateTime(r4)
            long r1 = r1 - r8
            r10[r3] = r1
            r1 = r10[r3]
            r6 = 172800000(0xa4cb800, double:8.53745436E-316)
            long r1 = r1 + r6
            r10[r0] = r1
        L7c:
            long r1 = com.miui.gallery.card.scenario.DateUtils.getDateTime(r4)
            r11.setTargetTime(r1)
            goto Lac
        L84:
            int r13 = r2.getDayNum()
            int r1 = r2.getTotalDay()
            if (r13 != r1) goto Lc8
            long r4 = com.miui.gallery.card.scenario.DateUtils.getDateTime(r4)
            int r13 = r2.getDayNum()
            int r13 = r13 - r0
            long r6 = (long) r13
            long r6 = r6 * r8
            long r4 = r4 - r6
            r10[r3] = r4
            r4 = r10[r3]
            int r13 = r2.getTotalDay()
            long r1 = (long) r13
            long r1 = r1 * r8
            long r4 = r4 + r1
            r10[r0] = r4
            r1 = r10[r3]
            r11.setTargetTime(r1)
        Lac:
            r1 = r10[r3]
            java.lang.Long r13 = java.lang.Long.valueOf(r1)
            boolean r12 = r12.contains(r13)
            if (r12 == 0) goto Lb9
            return r3
        Lb9:
            r12 = r10[r3]
            r11.setStartTime(r12)
            r12 = r10[r0]
            r11.setEndTime(r12)
            return r0
        Lc4:
            int r1 = r1 + 1
            goto Lb
        Lc8:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.card.scenario.time.holiday.HolidayScenario.onPrepare(java.util.List, java.util.List):boolean");
    }

    @Override // com.miui.gallery.card.scenario.time.holiday.BaseHolidayScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        HolidaysUtil.Holiday holiday = HolidaysUtil.getHoliday(getTargetTime());
        this.mTargetHoliday = holiday;
        if (holiday == HolidaysUtil.Holiday.E_HOLIDAY_NONE) {
            this.mTargetHoliday = HolidaysUtil.getChineseHoliday(getStartTime());
        }
        List<Long> arrayList = new ArrayList<>();
        if (BaseMiscUtil.isValid(this.mTagIdList)) {
            arrayList = getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
        }
        if (this.mTargetHoliday == HolidaysUtil.Holiday.E_SOLAR_CHILDREN_DAY) {
            List<Long> mediaIdsByAges = getMediaIdsByAges(0, 18);
            if (BaseMiscUtil.isValid(mediaIdsByAges)) {
                arrayList.addAll(distinctMediaIds(mediaIdsByAges));
            }
        }
        return arrayList;
    }

    /* renamed from: com.miui.gallery.card.scenario.time.holiday.HolidayScenario$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday;

        static {
            int[] iArr = new int[HolidaysUtil.Holiday.values().length];
            $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday = iArr;
            try {
                iArr[HolidaysUtil.Holiday.E_SOLAR_NEW_YEAR_EVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_NEW_YEAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_VALENTINES_DAY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_LABOUR_DAY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_CHILDREN_DAY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_NATIONAL_DAY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_HALLOWEEN_DAY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_CHRISTMAS_DAY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_NEW_YEAR_EVE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_NEW_YEAR.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_LANTERN_FESTIVAL.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_DRAGON_HEAD_UP_FESTIVAL.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_DRAGON_BOAT_FESTIVAL.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_NIGHT_OF_SEVENS.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_MID_AUTUMN_FESTIVAL.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_CHINESE_NEW_YEAR_SIX.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[this.mTargetHoliday.ordinal()]) {
            case 1:
                ArrayList arrayList = new ArrayList(2);
                arrayList.add(resources.getString(R.string.solar_new_year_eve_title_one));
                arrayList.add(resources.getString(R.string.solar_new_year_eve_title_two, DateUtils.getYearLocale(record.getStartTime())));
                return getRandomArrayString(arrayList);
            case 2:
                return resources.getString(R.string.solar_new_year_title);
            case 3:
                return resources.getString(R.string.solar_valentines_day_title);
            case 4:
                return resources.getString(R.string.solar_labour_day_title);
            case 5:
                return resources.getString(R.string.solar_children_day_title);
            case 6:
                return resources.getString(R.string.solar_national_day_title);
            case 7:
                return resources.getString(R.string.solar_halloween_title);
            case 8:
                return resources.getString(R.string.solar_christmas_title);
            case 9:
                return getRandomArrayString(R.array.chinese_new_year_eve_title);
            case 10:
                List<String> list2 = (List) Arrays.stream(resources.getStringArray(R.array.chinese_new_year_title)).collect(Collectors.toList());
                list2.add(resources.getString(R.string.chinese_new_year_title, HolidaysUtil.getZodiacYear(record.getStartTime())));
                return getRandomArrayString(list2);
            case 11:
                return resources.getString(R.string.chinese_lantern_festival_title);
            case 12:
                return resources.getString(R.string.chinese_dragon_head_up_title);
            case 13:
                return resources.getString(R.string.chinese_dragon_boat_festival_title);
            case 14:
                return resources.getString(R.string.chinese_night_of_sevens_title);
            case 15:
                return resources.getString(R.string.chinese_mid_automn_festival_title);
            default:
                return "";
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        String yearLocale = DateUtils.getYearLocale(record.getStartTime());
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[this.mTargetHoliday.ordinal()]) {
            case 4:
            case 6:
            case 7:
            case 8:
                return DateUtils.getYearMonthDayLocale(record.getStartTime()) + "-" + DateUtils.getDayLocale(record.getEndTime());
            case 5:
            case 10:
            default:
                return DateUtils.getYearMonthDayLocale(record.getStartTime());
            case 9:
                return resources.getString(R.string.chinese_new_year_eve, yearLocale);
            case 11:
                return resources.getString(R.string.chinese_lantern_festival, yearLocale);
            case 12:
                return resources.getString(R.string.chinese_dragon_head_up, yearLocale);
            case 13:
                return resources.getString(R.string.chinese_dragon_boat_festival, yearLocale);
            case 14:
                return resources.getString(R.string.chinese_night_of_sevens, yearLocale);
            case 15:
                return resources.getString(R.string.chinese_mid_automn_festival, yearLocale);
            case 16:
                return resources.getString(R.string.chinese_new_year, yearLocale);
        }
    }
}
