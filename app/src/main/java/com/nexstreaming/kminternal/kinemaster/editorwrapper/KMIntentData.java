package com.nexstreaming.kminternal.kinemaster.editorwrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class KMIntentData {
    public int kmIntentFormatVersion = 0;
    public Project project = new Project();

    /* loaded from: classes3.dex */
    public static class AudioClip {
        public boolean background;
        public int endTime;
        public int endTrim;
        public boolean extendToEnd;
        public boolean loop;
        public String mediaTitle;
        public boolean mute;
        public String path;
        public boolean pinned;
        public int startTime;
        public int startTrim;
        public int volume;
    }

    /* loaded from: classes3.dex */
    public static class ImageLayerAttributes {
        public int brightness;
        public String colorFilterId;
        public int contrast;
        public String path;
        public int saturation;
    }

    /* loaded from: classes3.dex */
    public static class KeyFrame {
        public float x;
        public float y;
        public float time = 0.0f;
        public float scale = 1.0f;
        public float angle = 0.0f;
        public float alpha = 1.0f;
    }

    /* loaded from: classes3.dex */
    public static class Layer {
        public String animationIn;
        public int animationInDuration;
        public String animationOut;
        public int animationOutDuration;
        public String animationOverall;
        public int endTime;
        public ImageLayerAttributes imageLayerAttributes;
        public List<KeyFrame> keyFrames;
        public LayerType layerType;
        public boolean pinned;
        public int startTime;
        public StickerLayerAttributes stickerLayerAttributes;
        public TextLayerAttributes textLayerAttributes;
        public VideoLayerAttributes videoLayerAttributes;
    }

    /* loaded from: classes3.dex */
    public enum LayerType {
        Image,
        Video,
        Text,
        Sticker
    }

    /* loaded from: classes3.dex */
    public static class Project {
        public String name;
        public int compositionWidth = 1920;
        public int compositionHeight = 1080;
        public int videoFadeInTime = 0;
        public int videoFadeOutTime = 0;
        public int audioFadeInTime = 0;
        public int audioFadeOutTime = 0;
        public List<VisualClip> visualClips = new ArrayList();
        public List<AudioClip> audioClips = new ArrayList();
        public List<Layer> layers = new ArrayList();
    }

    /* loaded from: classes3.dex */
    public static class StickerLayerAttributes {
        public String stickerId;
    }

    /* loaded from: classes3.dex */
    public static class TextLayerAttributes {
        public int align;
        public int backgroundColor;
        public String fontId;
        public int glowColor;
        public int outlineColor;
        public int shadowColor;
        public String text;
        public int textColor;
        public float textSize;
    }

    /* loaded from: classes3.dex */
    public static class VideoLayerAttributes {
        public int brightness;
        public String colorFilterId;
        public int contrast;
        public boolean mute;
        public String path;
        public int saturation;
        public int startTrim;
        public int volume;
    }

    /* loaded from: classes3.dex */
    public static class VisualClip {
        public String clipEffectId;
        public Map<String, String> clipEffectParameters;
        public String colorFilterId;
        public float cropEndBottom;
        public float cropEndLeft;
        public float cropEndRight;
        public float cropEndTop;
        public float cropStartBottom;
        public float cropStartLeft;
        public float cropStartRight;
        public float cropStartTop;
        public int duration;
        public String path;
        public int startTrim;
        public String transitionEffectId;
        public Map<String, String> transitionEffectParameters;
        public boolean vignette;
        public int transitionDuration = 0;
        public int volume = 100;
        public boolean mute = false;
        public int playbackSpeed = 100;
        public int brightness = 0;
        public int contrast = 0;
        public int saturation = 0;
        public int rotation = 0;
        public boolean fliph = false;
        public boolean flipv = false;
    }
}
