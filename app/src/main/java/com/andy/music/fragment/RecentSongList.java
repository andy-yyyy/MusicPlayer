package com.andy.music.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.andy.music.entity.Music;
import com.andy.music.function.MusicListManager;

import java.util.List;

/**
 * 最近播放列表
 * Created by Andy on 2015/1/11.
 */
public class RecentSongList extends BaseSongList {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        showLoadingView (true);
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run() {
                MusicListManager manager = MusicListManager.getInstance (MusicListManager.MUSIC_LIST_RECENT);
                List<Music> list = (manager != null ? manager.getList() : null);
                updateList (list);
                showLoadingView (false);
            }
        }, 0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TopBarFragment topBar = (TopBarFragment)getActivity().getSupportFragmentManager().findFragmentByTag("topBar");
        if (topBar!=null) {
            topBar.setCustomTitle("最近播放");
        }
    }


}