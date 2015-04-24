package com.andy.music.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.andy.music.entity.Music;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicListManager;
import com.andy.music.function.MusicNotification;
import com.andy.music.util.BroadCastHelper;
import com.andy.music.util.MusicLocator;

import java.io.IOException;

/**
 * 音乐播放服务
 * Created by Andy on 2014/11/20.
 */
public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    /**
     * 播放模式
     */
    public static final int MUSIC_PLAY_SCHEMA_ORDER = 0;    // 顺序播放
    public static final int MUSIC_PLAY_SCHEMA_RANDOM = 1;    // 随机播放
    public static final int MUSIC_PLAY_SCHEMA_LIST_CIRCULATE = 2;    // 列表循环
    public static final int MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE = 3;    // 单曲循环
    private static boolean isPlaying = false;  // 是否正在播放
    private static boolean isFirst = true;  // 是否是启动后第一次播放

    private Music music;
    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {
//        Log.d(TagConstants.TAG, "PlayService-->onCreate()");
        // 创建 MediaPlayer 对象
        mediaPlayer = new MediaPlayer();

        //要确保CPU在你的 MediaPlayer  播放的时候继续处于运行状态,当初始化你的 MediaPlayer 时调用 setWakeMode()  .
        // 一旦你这么做了, MediaPlayer 会持有指定的lock在播放的时候. 并且在paused或者stoped状态时,会释放掉这个lock.
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        // 获取上次的音乐位置
        MusicLocator.getMusicLocation();

        // 注册广播接收器
        registerReceiver();

        // 加载通知栏
        MusicNotification notification = MusicNotification.getInstance(this);
        notification.sendNotification();

        // 设置歌曲播放完成的监听事件
        mediaPlayer.setOnCompletionListener(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TagConstants.TAG, "PlayService-->onStartCommand()");
        String playAction = null;
        if (intent!=null) {
            playAction = intent.getStringExtra("play_action");
        }
        if (playAction!=null) {
            switch (playAction) {
                case "next":
                    MusicLocator.toNext();
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
                    break;
                case "pause":
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                    break;
                case "start":
                    BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_START);
                    break;
                default:break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        Log.d(TagConstants.TAG, "PlayService-->onError()");
        mp.stop();
        mp.reset();
        return false;
    }

    @Override
    public void onDestroy() {
//        Log.d(TagConstants.TAG, "PlayService-->onDestroy()");
        MusicLocator.saveMusicLocation();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Log.d(TagConstants.TAG, "PlayService-->onCompletion()");
        // 歌曲播放完成，自动播放下一首
        MusicLocator.toNext();
        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
    }

    /**
     * 为歌曲的播放做准备
     */
    public void prepare() {
        music = MusicLocator.getCurrentMusic();
        if (music == null) {
            Log.d(TagConstants.TAG, "歌曲为空");
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
            addToRecent(music);   // 添加最近播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放（从歌曲开头开始）
     */
    public void play() {
        prepare();
        start();
    }

    /**
     * 开始播放（从上次停止的地方开始）
     */
    public void start() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 判断是否正在播放
     * @return 是否正在播放
     */
    public  static boolean isPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * 动态注册广播接收器
     */
    private void registerReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_START);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PAUSE);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS);
        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
        Receiver receiver = new Receiver();
        registerReceiver(receiver, filter);

    }

    /**
     * 将播放的歌曲添加到最近播放
     * @param music 正在播放的歌曲
     */
    private void addToRecent(Music music) {
        MusicListManager recentList = MusicListManager.getInstance(MusicListManager.MUSIC_LIST_RECENT);   // 获取最近列表
        if (recentList != null && !recentList.isMusicExist(music)) {
            recentList.add(music);
        }
    }


    /**
     * 接收与音乐播放有关的广播，然后做出相应操作
     */
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case BroadCastHelper.ACTION_MUSIC_START:
                    // 开始播放音乐的广播（从断点开始）
                    // 判断是否是第一次播放音乐(防止打开应用后直接点击开始播放按钮后没有执行prepare函数)
                    if (isFirst) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);
                        isFirst = false;
                        break;
                    }
                    if (MusicLocator.getCurrentMusic()==null) {
                        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PAUSE);
                        break;
                    }
                    start();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PAUSE:
                    // 暂停音乐的广播
                    pause();
                    setIsPlaying(false);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY:
                    // 播放音乐的广播（从头开始）
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_NEXT:
                    // 播放下一首的广播
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_PREVIOUS:
                    // 播放上一首的广播
                    play();
                    setIsPlaying(true);
                    break;
                case BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM:
                    // 随机播放的广播
                    play();
                    setIsPlaying(true);
                default: break;
            }

        }
    }

}