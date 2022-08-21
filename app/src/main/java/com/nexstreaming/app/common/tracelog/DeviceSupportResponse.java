package com.nexstreaming.app.common.tracelog;

import com.nexstreaming.app.common.tracelog.TLP;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class DeviceSupportResponse implements TLP.BaseResponse {
    public DeviceInfo device_info;
    public MatchInfo match_info;
    public int next;
    public int result;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceSupportResponse:\n");
        sb.append("  result:");
        sb.append(this.result);
        sb.append('\n');
        sb.append("  next:");
        sb.append(this.next);
        sb.append('\n');
        if (this.match_info == null) {
            sb.append("  match_info: null\n");
        } else {
            sb.append("  match_info:\n");
            sb.append(this.match_info.toString().replaceAll("(?m)^", "    "));
        }
        if (this.device_info == null) {
            sb.append("  device_info: null\n");
        } else {
            sb.append("  device_info:\n");
            sb.append(this.device_info.toString().replaceAll("(?m)^", "    "));
        }
        return sb.toString();
    }

    /* loaded from: classes3.dex */
    public static class MatchInfo {
        public String board_platform;
        public String build_device;
        public String build_model;
        public String manufacturer;
        public int os_api_level_max;
        public int os_api_level_min;
        public Integer record_idx;

        public String toString() {
            return "MatchInfo:\n  record_idx:" + this.record_idx + "\n  build_device:" + this.build_device + "\n  build_model:" + this.build_model + "\n  board_platform:" + this.board_platform + "\n  manufacturer:" + this.manufacturer + "\n  os_api_level_min:" + this.os_api_level_min + "\n  os_api_level_max:" + this.os_api_level_max + '\n';
        }
    }

    /* loaded from: classes3.dex */
    public static class DeviceInfo {
        public int audio_codec_count;
        public List<ExportResInfo> export_res_extra;
        public List<ExportResInfo> export_res_hw;
        public List<ExportResInfo> export_res_sw;
        public int max_codec_mem_size;
        public int max_dec_count;
        public int max_dec_res_hw_b;
        public int max_dec_res_hw_h;
        public int max_dec_res_hw_m;
        public int max_dec_res_nexsw_b;
        public int max_dec_res_nexsw_h;
        public int max_dec_res_nexsw_m;
        public int max_dec_res_sw_b;
        public int max_dec_res_sw_h;
        public int max_dec_res_sw_m;
        public int max_enc_count;
        public int max_fhd_trans_time;
        public int max_fps;
        public int max_hw_import_res;
        public int max_sw_import_res;
        public Map<String, String> properties;
        public int rec_image_mode;
        public int rec_video_mode;
        public int support_avc;
        public int support_mpeg4v;
        public int supported;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DeviceSupportResponse:\n");
            sb.append("  support_avc:");
            sb.append(this.support_avc);
            sb.append('\n');
            sb.append("  support_mpeg4v:");
            sb.append(this.support_mpeg4v);
            sb.append('\n');
            sb.append("  max_fps:");
            sb.append(this.max_fps);
            sb.append('\n');
            sb.append("  max_codec_mem_size:");
            sb.append(this.max_codec_mem_size);
            sb.append('\n');
            sb.append("  max_dec_count:");
            sb.append(this.max_dec_count);
            sb.append('\n');
            sb.append("  max_enc_count:");
            sb.append(this.max_enc_count);
            sb.append('\n');
            sb.append("  max_fhd_trans_time:");
            sb.append(this.max_fhd_trans_time);
            sb.append('\n');
            sb.append("  rec_image_mode:");
            sb.append(this.rec_image_mode);
            sb.append('\n');
            sb.append("  rec_video_mode:");
            sb.append(this.rec_video_mode);
            sb.append('\n');
            sb.append("  audio_codec_count:");
            sb.append(this.audio_codec_count);
            sb.append('\n');
            sb.append("  max_sw_import_res:");
            sb.append(this.max_sw_import_res);
            sb.append('\n');
            sb.append("  max_hw_import_res:");
            sb.append(this.max_hw_import_res);
            sb.append('\n');
            sb.append("  max_dec_res_nexsw_b:");
            sb.append(this.max_dec_res_nexsw_b);
            sb.append('\n');
            sb.append("  max_dec_res_nexsw_b:");
            sb.append(this.max_dec_res_nexsw_b);
            sb.append('\n');
            sb.append("  max_dec_res_nexsw_h:");
            sb.append(this.max_dec_res_nexsw_h);
            sb.append('\n');
            sb.append("  max_dec_res_sw_b:");
            sb.append(this.max_dec_res_sw_b);
            sb.append('\n');
            sb.append("  max_dec_res_sw_m:");
            sb.append(this.max_dec_res_sw_m);
            sb.append('\n');
            sb.append("  max_dec_res_sw_h:");
            sb.append(this.max_dec_res_sw_h);
            sb.append('\n');
            sb.append("  max_dec_res_hw_b:");
            sb.append(this.max_dec_res_hw_b);
            sb.append('\n');
            sb.append("  max_dec_res_hw_m:");
            sb.append(this.max_dec_res_hw_m);
            sb.append('\n');
            sb.append("  max_dec_res_hw_h:");
            sb.append(this.max_dec_res_hw_h);
            sb.append('\n');
            int i = 0;
            if (this.export_res_sw == null) {
                sb.append("  export_res_sw: null\n");
            } else {
                sb.append("  export_res_sw:\n");
                Iterator<ExportResInfo> it = this.export_res_sw.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    sb.append("    [" + i2 + "] " + it.next().toString() + "\n");
                    i2++;
                }
            }
            if (this.export_res_hw == null) {
                sb.append("  export_res_hw: null\n");
            } else {
                sb.append("  export_res_hw:\n");
                Iterator<ExportResInfo> it2 = this.export_res_hw.iterator();
                int i3 = 0;
                while (it2.hasNext()) {
                    sb.append("    [" + i3 + "] " + it2.next().toString() + "\n");
                    i3++;
                }
            }
            if (this.export_res_extra == null) {
                sb.append("  export_res_extra: null\n");
            } else {
                sb.append("  export_res_extra:\n");
                Iterator<ExportResInfo> it3 = this.export_res_extra.iterator();
                while (it3.hasNext()) {
                    sb.append("    [" + i + "] " + it3.next().toString() + "\n");
                    i++;
                }
            }
            if (this.properties == null) {
                sb.append("  properties: null\n");
            } else {
                sb.append("  properties:\n");
                for (Map.Entry<String, String> entry : this.properties.entrySet()) {
                    sb.append("    " + entry.getKey() + "=" + entry.getValue());
                }
            }
            return sb.toString();
        }
    }

    /* loaded from: classes3.dex */
    public static class ExportResInfo {
        public int bitrate;
        public int display_height;
        public int height;
        public int width;

        public String toString() {
            return "<ExportResInfo " + this.width + "x" + this.height + " disp=" + this.display_height + " bitrate=" + this.bitrate + ">";
        }
    }

    @Override // com.nexstreaming.app.common.tracelog.TLP.BaseResponse
    public int getResult() {
        return this.result;
    }
}
