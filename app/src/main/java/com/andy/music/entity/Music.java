package com.andy.music.entity;

import cn.bmob.v3.BmobObject;

/**
 * 该类用于描述音乐文件
 * Created by Andy on 2014/11/14.
 */
public class Music extends BmobObject{

    private int id;
    private int srcId;
    private int duration;
    private String name;
    private String singer;
    private String path;
    private String album;

    public Music() {}
    public Music(int id) {
        this.id = id;
    }
    public Music(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Music(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }




    @Override
    public String toString() {
        return "Name : "+this.name + "  Path :  "+this.path +"\n";
    }

    /**
     * 判断两个音乐对象是否相等
     * 如果两个音乐的路径 path 一样，则认为是两个音乐对象等同
     * @param o 被比较的对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (path == null || !(o instanceof Music)) return false;
        Music music = (Music)o;
        return path.equals(music.getPath());
    }
}
