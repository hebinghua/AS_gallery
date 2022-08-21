package com.nexstreaming.nexeditorsdk;

import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public final class nexAudioEnvelop implements Cloneable {
    private static final int maxIndex = 65536;
    private int[] m_cachedLevelList;
    private int[] m_cachedTimeList;
    private boolean m_modify;
    private int m_totalTime;
    private int m_trimEndTime;
    private int m_trimStartTime;
    private ArrayList<Integer> m_volumeEnvelopeLevel;
    private ArrayList<Integer> m_volumeEnvelopeTime;

    private nexAudioEnvelop() {
        this.m_modify = true;
    }

    public static nexAudioEnvelop clone(nexAudioEnvelop nexaudioenvelop) {
        nexAudioEnvelop nexaudioenvelop2;
        nexAudioEnvelop nexaudioenvelop3 = null;
        try {
            nexaudioenvelop2 = (nexAudioEnvelop) nexaudioenvelop.clone();
        } catch (CloneNotSupportedException e) {
            e = e;
        }
        try {
            nexaudioenvelop2.m_volumeEnvelopeTime = nexaudioenvelop.m_volumeEnvelopeTime;
            nexaudioenvelop2.m_volumeEnvelopeLevel = nexaudioenvelop.m_volumeEnvelopeLevel;
            nexaudioenvelop2.m_cachedTimeList = nexaudioenvelop.m_cachedTimeList;
            nexaudioenvelop2.m_cachedLevelList = nexaudioenvelop.m_cachedLevelList;
            return nexaudioenvelop2;
        } catch (CloneNotSupportedException e2) {
            e = e2;
            nexaudioenvelop3 = nexaudioenvelop2;
            e.printStackTrace();
            return nexaudioenvelop3;
        }
    }

    public nexAudioEnvelop(nexClip nexclip) {
        this.m_modify = true;
        this.m_totalTime = nexclip.getTotalTime();
        this.m_trimStartTime = 0;
        this.m_trimEndTime = nexclip.getTotalTime();
    }

    public void updateTrimTime(int i, int i2) {
        if (i2 > i) {
            if (i < 0) {
                throw new InvalidRangeException(0, this.m_totalTime, i);
            }
            this.m_trimStartTime = i;
            this.m_trimEndTime = i2;
            this.m_modify = true;
            return;
        }
        throw new InvalidRangeException(i, i2);
    }

    public int addVolumeEnvelope(int i, int i2) {
        if (i >= 0) {
            if (i2 > 200 || i2 < 0) {
                throw new InvalidRangeException(0, 200, i2);
            }
            int findTime2Index = findTime2Index(i, i2);
            this.m_modify = true;
            if (findTime2Index >= 65536) {
                return findTime2Index - 65536;
            }
            this.m_volumeEnvelopeTime.add(findTime2Index, Integer.valueOf(i));
            this.m_volumeEnvelopeLevel.add(findTime2Index, Integer.valueOf(i2));
            return findTime2Index;
        }
        throw new InvalidRangeException(0, this.m_totalTime, i);
    }

    private int findTime2Index(int i, int i2) {
        if (this.m_volumeEnvelopeLevel == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            this.m_volumeEnvelopeLevel = arrayList;
            arrayList.add(0, 100);
            this.m_volumeEnvelopeLevel.add(1, 100);
        }
        ArrayList<Integer> arrayList2 = this.m_volumeEnvelopeTime;
        if (arrayList2 == null) {
            ArrayList<Integer> arrayList3 = new ArrayList<>();
            this.m_volumeEnvelopeTime = arrayList3;
            arrayList3.add(0, 0);
            this.m_volumeEnvelopeTime.add(1, Integer.valueOf(this.m_totalTime));
            return 1;
        }
        int size = arrayList2.size();
        for (int i3 = 0; i3 < this.m_volumeEnvelopeTime.size(); i3++) {
            int intValue = this.m_volumeEnvelopeTime.get(i3).intValue();
            if (intValue == i) {
                int i4 = i3 + 65536;
                this.m_volumeEnvelopeLevel.set(i3, Integer.valueOf(i2));
                return i4;
            } else if (intValue > i) {
                return i3;
            }
        }
        return size;
    }

    public void changeVolumeLevelValue(int i, int i2) {
        if (i <= this.m_volumeEnvelopeLevel.size() - 1) {
            if (i2 > 200 || i2 < 0) {
                throw new InvalidRangeException(0, 200, i2);
            }
            this.m_volumeEnvelopeLevel.set(i, Integer.valueOf(i2));
            this.m_modify = true;
            return;
        }
        throw new InvalidRangeException(0, this.m_volumeEnvelopeLevel.size() - 1, i);
    }

    public void removeVolumeEnvelop() {
        ArrayList<Integer> arrayList = this.m_volumeEnvelopeTime;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<Integer> arrayList2 = this.m_volumeEnvelopeLevel;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        this.m_modify = true;
    }

    public void removeVolumeEnvelop(int i) {
        if (i > this.m_volumeEnvelopeLevel.size() - 1) {
            throw new InvalidRangeException(0, this.m_volumeEnvelopeLevel.size() - 1, i);
        }
        this.m_volumeEnvelopeTime.remove(i);
        this.m_volumeEnvelopeLevel.remove(i);
        this.m_modify = true;
    }

    public int getVolumeEnvelopeTime(int i) {
        if (i > this.m_volumeEnvelopeTime.size() - 1) {
            throw new InvalidRangeException(0, this.m_volumeEnvelopeTime.size() - 1, i);
        }
        ArrayList<Integer> arrayList = this.m_volumeEnvelopeTime;
        if (arrayList != null) {
            return arrayList.get(i).intValue();
        }
        return -1;
    }

    public int getVolumeEnvelopeLevel(int i) {
        if (i > this.m_volumeEnvelopeLevel.size() - 1) {
            throw new InvalidRangeException(0, this.m_volumeEnvelopeLevel.size() - 1, i);
        }
        ArrayList<Integer> arrayList = this.m_volumeEnvelopeLevel;
        if (arrayList != null) {
            return arrayList.get(i).intValue();
        }
        return -1;
    }

    public int getVolumeEnvelopeTimeAdj(int i) {
        if (i > this.m_volumeEnvelopeTime.size() - 1) {
            throw new InvalidRangeException(0, this.m_volumeEnvelopeTime.size() - 1, i);
        }
        ArrayList<Integer> arrayList = this.m_volumeEnvelopeTime;
        if (arrayList != null) {
            return arrayList.get(i).intValue() - this.m_trimStartTime;
        }
        return -1;
    }

    public int getVolumeEnvelopeLength() {
        return this.m_volumeEnvelopeTime.size();
    }

    private int getProjectDuration() {
        return this.m_trimEndTime - this.m_trimStartTime;
    }

    public int[] getVolumeEnvelopeTimeList() {
        return getVolumeEnvelopeList(true);
    }

    public int[] getVolumeEnvelopeLevelList() {
        return getVolumeEnvelopeList(false);
    }

    private int[] getVolumeEnvelopeList(boolean z) {
        if (this.m_modify) {
            this.m_modify = false;
            if (this.m_volumeEnvelopeLevel != null) {
                ArrayList arrayList = new ArrayList(this.m_volumeEnvelopeLevel.size() + 2);
                ArrayList arrayList2 = new ArrayList(this.m_volumeEnvelopeLevel.size() + 2);
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    if (i >= this.m_volumeEnvelopeLevel.size()) {
                        break;
                    }
                    int volumeEnvelopeTimeAdj = getVolumeEnvelopeTimeAdj(i);
                    int volumeEnvelopeLevel = getVolumeEnvelopeLevel(i);
                    int projectDuration = getProjectDuration();
                    boolean z2 = this.m_totalTime == this.m_trimEndTime;
                    if (volumeEnvelopeTimeAdj <= projectDuration && volumeEnvelopeTimeAdj > 0) {
                        arrayList.add(Integer.valueOf(volumeEnvelopeTimeAdj));
                        arrayList2.add(Integer.valueOf(volumeEnvelopeLevel));
                    } else if (volumeEnvelopeTimeAdj <= projectDuration && volumeEnvelopeLevel > 0 && volumeEnvelopeTimeAdj == 0) {
                        arrayList.add(Integer.valueOf(volumeEnvelopeTimeAdj));
                        arrayList2.add(Integer.valueOf(volumeEnvelopeLevel));
                    } else if (volumeEnvelopeTimeAdj > projectDuration && !z2) {
                        arrayList.add(Integer.valueOf(projectDuration));
                        arrayList2.add(Integer.valueOf((int) ((((projectDuration - i2) / (volumeEnvelopeTimeAdj - i2)) * (volumeEnvelopeLevel - i3)) + i3)));
                        break;
                    }
                    i++;
                    i2 = volumeEnvelopeTimeAdj;
                    i3 = volumeEnvelopeLevel;
                }
                this.m_cachedTimeList = com.nexstreaming.app.common.util.f.a(arrayList);
                this.m_cachedLevelList = com.nexstreaming.app.common.util.f.a(arrayList2);
            } else {
                this.m_cachedTimeList = null;
                this.m_cachedLevelList = null;
            }
        }
        if (z) {
            return this.m_cachedTimeList;
        }
        return this.m_cachedLevelList;
    }

    public nexSaveDataFormat.nexAudioEnvelopOf getSaveData() {
        nexSaveDataFormat.nexAudioEnvelopOf nexaudioenvelopof = new nexSaveDataFormat.nexAudioEnvelopOf();
        nexaudioenvelopof.m_volumeEnvelopeLevel = this.m_volumeEnvelopeLevel;
        nexaudioenvelopof.m_volumeEnvelopeTime = this.m_volumeEnvelopeTime;
        nexaudioenvelopof.m_totalTime = this.m_totalTime;
        nexaudioenvelopof.m_trimStartTime = this.m_trimStartTime;
        nexaudioenvelopof.m_trimEndTime = this.m_trimEndTime;
        return nexaudioenvelopof;
    }

    public nexAudioEnvelop(nexSaveDataFormat.nexAudioEnvelopOf nexaudioenvelopof) {
        this.m_modify = true;
        this.m_volumeEnvelopeLevel = nexaudioenvelopof.m_volumeEnvelopeLevel;
        this.m_volumeEnvelopeTime = nexaudioenvelopof.m_volumeEnvelopeTime;
        this.m_totalTime = nexaudioenvelopof.m_totalTime;
        this.m_trimStartTime = nexaudioenvelopof.m_trimStartTime;
        this.m_trimEndTime = nexaudioenvelopof.m_trimEndTime;
    }
}
