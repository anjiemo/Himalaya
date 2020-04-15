package com.smart.himalaya.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MyAlbum {

    @Id
    @Unique
    private long id;
    private String coverUrlLarge;
    private String albumTitle;
    private String albumIntro;
    private long includeTrackCount;
    private long playCount;
    private String nickName;
    @Generated(hash = 1853946491)
    public MyAlbum(long id, String coverUrlLarge, String albumTitle,
            String albumIntro, long includeTrackCount, long playCount,
            String nickName) {
        this.id = id;
        this.coverUrlLarge = coverUrlLarge;
        this.albumTitle = albumTitle;
        this.albumIntro = albumIntro;
        this.includeTrackCount = includeTrackCount;
        this.playCount = playCount;
        this.nickName = nickName;
    }
    @Generated(hash = 1091479401)
    public MyAlbum() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCoverUrlLarge() {
        return this.coverUrlLarge;
    }
    public void setCoverUrlLarge(String coverUrlLarge) {
        this.coverUrlLarge = coverUrlLarge;
    }
    public String getAlbumTitle() {
        return this.albumTitle;
    }
    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
    public String getAlbumIntro() {
        return this.albumIntro;
    }
    public void setAlbumIntro(String albumIntro) {
        this.albumIntro = albumIntro;
    }
    public long getIncludeTrackCount() {
        return this.includeTrackCount;
    }
    public void setIncludeTrackCount(long includeTrackCount) {
        this.includeTrackCount = includeTrackCount;
    }
    public long getPlayCount() {
        return this.playCount;
    }
    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
