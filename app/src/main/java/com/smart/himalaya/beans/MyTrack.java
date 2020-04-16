package com.smart.himalaya.beans;

import com.smart.himalaya.utils.TrackUniversalCastConvert;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MyTrack {

    @Id
    @Unique
    private long dataId;
    @Convert(columnType = String.class, converter = TrackUniversalCastConvert.class)
    private Track track;
    @Generated(hash = 725413162)
    public MyTrack(long dataId, Track track) {
        this.dataId = dataId;
        this.track = track;
    }
    @Generated(hash = 1945207357)
    public MyTrack() {
    }
    public long getDataId() {
        return this.dataId;
    }
    public void setDataId(long dataId) {
        this.dataId = dataId;
    }
    public Track getTrack() {
        return this.track;
    }
    public void setTrack(Track track) {
        this.track = track;
    }
}
