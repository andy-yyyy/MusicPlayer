package com.andy.music.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * 定位当前播放的歌曲以及记录上次播放的位置
 * Created by Andy on 2014/11/19.
 */
public class MusicLocator {

    private static SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = pref.edit();

    private static List<Music> currentMusicList;
    private static int currentPosition;

    public static List<Music> getCurrentMusicList() {
        return currentMusicList;
    }

    public static void setCurrentMusicList(List<Music> list) {
        currentMusicList = list;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public static Music getCurrentMusic() {
        if (currentMusicList==null || currentMusicList.size()<currentPosition) return null;
        return currentMusicList.get(currentPosition);
    }



    public static boolean toFirst() {
        currentPosition = 0;
        return true;
    }

    /**
     * 位当前歌曲下一首
     * @return 是否定位成功
     */
    public static boolean toNext() {

        // 新的位置
        int newPos = currentPosition + 1;

        // 判断新的歌曲位置是否越界
        if (isPosOutOfBound(newPos)) return false;

        // 更新歌曲位置
        currentPosition = newPos;
        return true;
    }

    /**
     * 定位歌曲到当前歌曲的前一首
     *
     * @return 是否定位成功
     */
    public static boolean toPrevious() {


        // 新的位置
        int newPos = currentPosition - 1;

        // 判断新的歌曲位置是否越界
        if (isPosOutOfBound(newPos)) return false;

        // 更新歌曲位置
        currentPosition = newPos;
        return true;
    }

    /**
     * 定位歌曲到一个随机位置
     */
    public static boolean toRandom() {

        // 判断歌曲列表是否存在
        if (currentMusicList==null) return false;

        // 新的位置 （获取列表大小范围内的随机数）
        int newPos = (int)(Math.random()*currentMusicList.size());

        // 判断新的歌曲位置是否越界
        if (isPosOutOfBound(newPos)) return false;

        // 更新歌曲位置
        currentPosition = newPos;
        return true;

    }

    private static boolean isPosOutOfBound(int position) {
        return (position<0 || currentMusicList==null ||  position>currentMusicList.size()-1);
    }


    public static void saveMusicLocation() {
        new Runnable() {
            @Override
            public void run() {
                MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_CURRENT);
                manager.setList(currentMusicList);
                editor.putInt("position", currentPosition).apply();
               Log.d(TagConstants.TAG, "播放列表已保存");
            }
        }.run();
    }



    public static void  getMusicLocation() {
        MusicListManager manager = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_CURRENT);
        currentMusicList = manager.getList();
        currentPosition = pref.getInt("position", 0);
    }


    public static int getLocatedPosition() {
        currentPosition = pref.getInt("position", 0);
        return currentPosition;
    }

//        public static Music getLocatedMusic() {
//        List<Music> list = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_LOCAL).getList();
//        Music music = null;
//        int pos = getId() - 1;
//        if (list != null) {
//            if (pos >= 0 && pos < list.size()) music = list.get(pos);
//        }
//        return music;
//    }

//    public static void setLocatedMusic(Music music) {
//        MusicLocator.setCurrentMusic(music);
//        editor.putInt("id", music.getId());
//        editor.putInt("source_id", music.getSrcId()).apply();
//    }

    //    public static int getId() {
//        return pref.getInt("id", 0);
//    }

//    public static int getSourceId() {
//        return pref.getInt("source_id", 0);
//    }

//    /**
//     * 获取上次播放的歌曲的位置信息
//     * @return 歌曲的位置信息
//     */
//    public static Bundle getLocation() {
////        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
//        Bundle bundle = new Bundle();
//        bundle.putString("list_name", pref.getString("list_name", MusicListManager.MUSIC_LIST_LOCAL));
//        bundle.putInt("position", pref.getInt("position", 0));
//        bundle.putInt("source_id", pref.getInt("source_id", 0));
//        return bundle;
//    }


//    public static MusicListManager getLocatedList() {
//        String listName = MusicLocator.getLocation().getString("list_name", MusicListManager.MUSIC_LIST_LOCAL);
//        return MusicListManager.getInstance(listName);
//    }


//    public static Music getLocatedMusic() {
//        int position = MusicLocator.getLocation().getInt("position", 0);
//        MusicList list = getLocatedList();
//        if (list==null) return  null;
//        if (list.getList()==null) return null;
//        if (list.getList().size()<=0) return null;
//        return getLocatedList().getList().get(position);
//    }

//    /**
//     * 定位当前播放的歌曲
//     * @param bundle  当前歌曲的位置信息，包括列表名，在列表中的位置及歌曲的 ID
//     */
//    public static void setLocation(Bundle bundle) {
//
//        // 将歌曲位置信息保存到 SharedPreferences
////        SharedPreferences pref = ContextUtil.getInstance().getSharedPreferences("music_location", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        String listName = bundle.getString("list_name");
//        int position = bundle.getInt("position");
//        int srcId = bundle.getInt("source_id");
//
//        if (listName!=null) {
//            editor.putString("list_name", listName);
//        }
//        editor.putInt("position", position);
//        editor.putInt("source_id", srcId);
//        editor.commit();
//
//    }
//
//


}
