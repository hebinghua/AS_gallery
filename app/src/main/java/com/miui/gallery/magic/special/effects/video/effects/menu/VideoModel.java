package com.miui.gallery.magic.special.effects.video.effects.menu;

import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.fetch.AudioResourceFetcher;
import com.miui.gallery.magic.fetch.VideoResourceFetcher;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.util.ResourceUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoModel extends BaseModel<VideoMenuPresenter, IMenu$M> {
    public String[] musics;
    public String[] videos;

    public VideoModel(VideoMenuPresenter videoMenuPresenter) {
        super(videoMenuPresenter);
        this.musics = new String[]{"", "", "magic_video_1.mp3", "magic_video_2.mp3", "magic_video_3.mp3", "magic_video_4.mp3", "magic_video_5.mp3", "magic_video_6.mp3", "magic_video_7.mp3", "magic_video_8.mp3", "magic_video_9.mp3", "magic_video_10.mp3", "magic_video_11.mp3", "magic_video_12.mp3"};
        this.videos = new String[]{"", "", "", "", "", "", "", "text_layout.mp4", "devil_wing.mp4", "particle_surround.mp4", "angel_wing.mp4", "devil_wing.mp4"};
    }

    public static List<ListItem> getVideoList() {
        String[] arrayById = ResourceUtil.getArrayById(R$array.magic_effects_video_titles);
        String str = arrayById[0];
        ListItem.ItemType itemType = ListItem.ItemType.NONE;
        return Arrays.asList(new ListItem("magic_recycler_video_0", str, "", 0L, false, itemType), new ListItem("magic_recycler_video_1", arrayById[1], "", 0L, false, itemType), new ListItem("magic_recycler_video_2", arrayById[2], "", 0L, false, itemType), new ListItem("magic_recycler_video_3", arrayById[3], "", 0L, false, itemType), new ListItem("magic_recycler_video_4", arrayById[4], "", 0L, false, itemType), new ListItem("magic_recycler_video_5", arrayById[5], "", 0L, false, itemType), new ListItem("magic_recycler_video_6", arrayById[6], "", 0L, false, itemType), new ListItem("magic_recycler_video_7", arrayById[7], "", 0L, false, itemType), new ListItem("magic_recycler_video_8", arrayById[8], "particle_surround", 14710740242596000L, true, itemType), new ListItem("magic_recycler_video_9", arrayById[9], "text_layout", 14710736401924192L, true, itemType), new ListItem("magic_recycler_video_10", arrayById[10], "devil_wing", 14779678140334080L, true, itemType), new ListItem("magic_recycler_video_11", arrayById[11], "angel_wing", 14977559678615584L, true, itemType));
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$M mo1070initContract() {
        return new IMenu$M() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoModel.1
            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$M
            public List getVideoData() {
                List<ListItem> videoList = VideoModel.getVideoList();
                for (int i = 0; i < videoList.size(); i++) {
                    ListItem listItem = videoList.get(i);
                    if (listItem.getResId() != 0 && VideoResourceFetcher.INSTANCE.isExistResource(listItem)) {
                        listItem.setDownLoaded(false);
                    }
                }
                return videoList;
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$M
            public List getAudioData() {
                new ArrayList();
                List<ListItem> audioList = VideoModel.getAudioList();
                for (int i = 0; i < audioList.size(); i++) {
                    ListItem listItem = audioList.get(i);
                    if (listItem.getResId() != 0 && listItem.getResId() != 0 && AudioResourceFetcher.INSTANCE.isExistResource(listItem)) {
                        listItem.setDownLoaded(false);
                    }
                }
                return audioList;
            }
        };
    }

    public static List<ListItem> getAudioList() {
        String[] arrayById = ResourceUtil.getArrayById(R$array.magic_effects_audio_titles);
        String str = arrayById[2];
        ListItem.ItemType itemType = ListItem.ItemType.NONE;
        return Arrays.asList(new ListItem("magic_recycler_clear_icon", arrayById[0], "", 0L, false, ListItem.ItemType.CLOSE, "", true), new ListItem("magic_recycler_audio", arrayById[1], "", 0L, false, ListItem.ItemType.SELECT, "", true), new ListItem("magic_recycler_audio_3", str, "magic_video_1", 14739123296993376L, true, itemType, "magic_video_1.mp3", true), new ListItem("magic_recycler_audio_4", arrayById[3], "magic_video_2", 14739127053189248L, true, itemType, "magic_video_2.mp3", true), new ListItem("magic_recycler_audio_5", arrayById[4], "magic_video_3", 14739129705562272L, true, itemType, "magic_video_3.mp3", true), new ListItem("magic_recycler_audio_6", arrayById[5], "magic_video_4", 14739132526559328L, true, itemType, "magic_video_4.mp3", true), new ListItem("magic_recycler_audio_7", arrayById[6], "magic_video_5", 14739135436030048L, true, itemType, "magic_video_5.mp3", true), new ListItem("magic_recycler_audio_8", arrayById[7], "magic_video_6", 14739138088992928L, true, itemType, "magic_video_6.mp3", true), new ListItem("magic_recycler_audio_9", arrayById[8], "magic_video_7", 14739141385519264L, true, itemType, "magic_video_7.mp3", true), new ListItem("magic_recycler_audio_10", arrayById[9], "magic_video_8", 14739144328478880L, true, itemType, "magic_video_8.mp3", true), new ListItem("magic_recycler_audio_11", arrayById[10], "magic_video_9", 14739146773102752L, true, itemType, "magic_video_9.mp3", true), new ListItem("magic_recycler_audio_12", arrayById[11], "magic_video_10", 14739149614612576L, true, itemType, "magic_video_10.mp3", true), new ListItem("magic_recycler_audio_13", arrayById[12], "magic_video_11", 14739152059891840L, true, itemType, "magic_video_11.mp3", true), new ListItem("magic_recycler_audio_14", arrayById[13], "magic_video_12", 14739154545934432L, true, itemType, "magic_video_12.mp3", true));
    }
}
