package cn.musicget.po;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by 万洪基 on 2017/8/13.
 */
public class ErrorSong {

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date errorTime;

    private String songName;

    private String singer;

    private Long kwId;

    private String lyric;

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Long getKwId() {
        return kwId;
    }

    public void setKwId(Long kwId) {
        this.kwId = kwId;
    }
}
