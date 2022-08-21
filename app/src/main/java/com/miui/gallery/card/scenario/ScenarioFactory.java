package com.miui.gallery.card.scenario;

import android.util.SparseArray;
import com.baidu.mapapi.UIMsg;
import com.miui.gallery.card.scenario.time.CloudTimeScenario;
import com.miui.gallery.card.scenario.time.annual.AnnualImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.annual.AnnualScenario;
import com.miui.gallery.card.scenario.time.annual.AnnualTravelsScenario;
import com.miui.gallery.card.scenario.time.annual.PastYearAnnualImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.annual.PastYearAnnualScenario;
import com.miui.gallery.card.scenario.time.annual.PastYearAnnualTravelScenario;
import com.miui.gallery.card.scenario.time.combination.PeopleAndFoodsScenario;
import com.miui.gallery.card.scenario.time.combination.PeopleAndTravelScenario;
import com.miui.gallery.card.scenario.time.guarantee.RecentMemoryScenario;
import com.miui.gallery.card.scenario.time.holiday.HolidayScenario;
import com.miui.gallery.card.scenario.time.holiday.HolidayTravelScenario;
import com.miui.gallery.card.scenario.time.holiday.PastYearHolidayScenario;
import com.miui.gallery.card.scenario.time.lasthalfYear.LastHalfYearScenario;
import com.miui.gallery.card.scenario.time.lastsomeday.LastSomeDayImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.lastsomeday.LastSomeDayScenario;
import com.miui.gallery.card.scenario.time.lastsomeday.RecentTravelScenario;
import com.miui.gallery.card.scenario.time.month.MonthlyImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.month.MonthlyScenario;
import com.miui.gallery.card.scenario.time.month.MonthlyTravelScenario;
import com.miui.gallery.card.scenario.time.month.PastYearMonthImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.month.PastYearMonthScenario;
import com.miui.gallery.card.scenario.time.month.PastYearMonthTravelScenario;
import com.miui.gallery.card.scenario.time.quarterly.PastYearQuarterlyImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.quarterly.PastYearQuarterlyScenario;
import com.miui.gallery.card.scenario.time.quarterly.QuarterlyImportantPeopleScenario;
import com.miui.gallery.card.scenario.time.quarterly.QuarterlyScenario;
import com.miui.gallery.card.scenario.time.season.PastYearSeasonScenario;
import com.miui.gallery.card.scenario.time.season.SeasonScenario;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexVideoClipEdit;

/* loaded from: classes.dex */
public class ScenarioFactory {
    public static final SparseArray<Class<? extends Scenario>> sScenarioClass;

    static {
        SparseArray<Class<? extends Scenario>> sparseArray = new SparseArray<>();
        sScenarioClass = sparseArray;
        sparseArray.put(1000, HolidayScenario.class);
        sparseArray.put(1100, PastYearHolidayScenario.class);
        sparseArray.put(1400, SeasonScenario.class);
        sparseArray.put(1500, PastYearSeasonScenario.class);
        sparseArray.put(nexVideoClipEdit.kSpeedControl_MaxValue, LastSomeDayScenario.class);
        sparseArray.put(1700, MonthlyScenario.class);
        sparseArray.put(1800, PastYearMonthScenario.class);
        sparseArray.put(1900, QuarterlyScenario.class);
        sparseArray.put(2000, PastYearQuarterlyScenario.class);
        sparseArray.put(2100, AnnualScenario.class);
        sparseArray.put(2200, PastYearAnnualScenario.class);
        sparseArray.put(2300, LastHalfYearScenario.class);
        sparseArray.put(1605, LastSomeDayImportantPeopleScenario.class);
        sparseArray.put(1705, MonthlyImportantPeopleScenario.class);
        sparseArray.put(1805, PastYearMonthImportantPeopleScenario.class);
        sparseArray.put(1905, QuarterlyImportantPeopleScenario.class);
        sparseArray.put(UIMsg.m_AppUI.MSG_APP_VERSION_FORCE, PastYearQuarterlyImportantPeopleScenario.class);
        sparseArray.put(2105, AnnualImportantPeopleScenario.class);
        sparseArray.put(2205, PastYearAnnualImportantPeopleScenario.class);
        sparseArray.put(1619, RecentTravelScenario.class);
        sparseArray.put(1719, MonthlyTravelScenario.class);
        sparseArray.put(1819, PastYearMonthTravelScenario.class);
        sparseArray.put(2119, AnnualTravelsScenario.class);
        sparseArray.put(2219, PastYearAnnualTravelScenario.class);
        sparseArray.put(2401, HolidayTravelScenario.class);
        sparseArray.put(2402, PeopleAndFoodsScenario.class);
        sparseArray.put(2403, RecentTravelScenario.class);
        sparseArray.put(2404, PeopleAndTravelScenario.class);
        sparseArray.put(2405, RecentMemoryScenario.class);
    }

    public static Scenario createLocalScenario(AssistantScenarioStrategy.ScenarioRule scenarioRule) {
        if (scenarioRule != null) {
            SparseArray<Class<? extends Scenario>> sparseArray = sScenarioClass;
            Class<? extends Scenario> cls = sparseArray.get(scenarioRule.getScenarioId());
            if (cls == null) {
                cls = sparseArray.get(scenarioRule.getScenarioTimeType());
            }
            if (cls == null) {
                return null;
            }
            try {
                Scenario newInstance = cls.newInstance();
                newInstance.onFillScenarioRule(scenarioRule);
                return newInstance;
            } catch (IllegalAccessException e) {
                DefaultLogger.w("ScenarioFactory", e);
                throw new IllegalStateException(e);
            } catch (InstantiationException e2) {
                DefaultLogger.w("ScenarioFactory", e2);
                throw new IllegalStateException(e2);
            }
        }
        return null;
    }

    public static Scenario createCloudTimeScenario(AssistantScenarioStrategy.CloudTimeScenarioRule cloudTimeScenarioRule) {
        if (cloudTimeScenarioRule != null) {
            CloudTimeScenario cloudTimeScenario = new CloudTimeScenario();
            cloudTimeScenario.onFillScenarioRule(cloudTimeScenarioRule);
            return cloudTimeScenario;
        }
        return null;
    }
}
