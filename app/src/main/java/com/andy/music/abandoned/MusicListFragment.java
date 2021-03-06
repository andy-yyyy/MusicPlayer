//package com.andy.music.abandoned;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.andy.music.R;
//import com.andy.music.adapter.MusicListAdapter;
//import com.andy.music.data.CursorAdapter;
//import com.andy.music.data.MusicScanner;
//import com.andy.music.entity.Music;
//import com.andy.music.function.MusicListManager;
//import com.andy.music.utility.BroadCastHelper;
//import com.andy.music.utility.MusicLocator;
//
//import java.util.List;
//
///**
//* 音乐列表模块
//* Created by Andy on 2014/11/28.
//*/
//public class MusicListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
//
//    /**
//     * 视图中显示的音乐列表
//     */
//    private MusicListManager musicListManager;
//
//    /**
//     * Activity 中用于显示音乐列表的 ListView
//     */
//    private ListView musicListView;
//
//    /**
//     * 要显示的音乐列表
//     */
//    private List<Music> list;
//
//    private MusicListAdapter adapter;
//    private TextView title;
//    private View view;
//
//    private Receiver receiver;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_music_list, container, false);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        refreshMusicList();   // 刷新列表
//    }
//
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//        // 获取从 Activity 传递过来的信息
//        Bundle bundle = getArguments();
//        String listName = "";
//        String searchContent = "";
//        if (bundle!=null) {
//            listName = bundle.getString("list_name");
//            searchContent = bundle.getString("search_content");
//        }
//
//        // 初始化变量
//        musicListView = (ListView) getActivity().findViewById(R.id.lv_music_list);
//        musicListManager = MusicListManager.getInstance(listName);
////        title = (TextView) getActivity().findViewById(R.id.tv_title);
//
////        title.setText("本地列表");
//
//        // 加载音乐列表
//        if (musicListManager != null && !musicListManager.isEmpty()) {
//            list = musicListManager.getList();
//            adapter = new MusicListAdapter(getActivity(), list, R.layout.list_cell_song);
//            musicListView.setAdapter(adapter);
//        } else if (searchContent!=null) {
//            String whereClause = "title LIKE '%"+ searchContent + "%'";
//            Cursor cursor = CursorAdapter.get(whereClause);
//            if (cursor!=null) {
//                list = MusicScanner.scan(cursor);
//            }
//            adapter = new MusicListAdapter(getActivity(), list, R.layout.list_cell_song);
//            musicListView.setAdapter(adapter);
//        } else  {          // 列表为空
//            Toast.makeText(getActivity(), "一首歌曲都没有呢~~", Toast.LENGTH_SHORT).show();
//        }
//
//        // 设置监听事件
//        musicListView.setOnItemClickListener(this);
//
//        // 注册广播接收
//        registerReceiver();
//
//        // 更新列表
//        refreshMusicList();
//
//    }
//
//    /**
//     * 刷新列表。当前歌曲改变的时候刷新
//     */
//    private void refreshMusicList() {
//
//        for (int i = 0; i < musicListView.getChildCount(); i++) {
//
//            // 遍历当前的可见部分的 View
//            View view = musicListView.getChildAt(i);
//
//            // 设置不是当前播放的歌曲的样式
//            view.setBackgroundColor(Color.parseColor("#00000000"));
//            view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#00000000"));
//            ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ccffffff"));
//            ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#78ffffff"));
//            ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#78ffffff"));
//
//            // 当前歌曲的序号
//            String musicNum = ((TextView) view.findViewById(R.id.tv_music_number)).getText().toString();
//
//            // 设置当前歌曲的样式
//            if (Integer.parseInt(musicNum) == MusicLocator.getId() + 1) {
//                view.setBackgroundColor(Color.parseColor("#34000000"));
//                view.findViewById(R.id.v_locator_bar).setBackgroundColor(Color.parseColor("#ec505e"));
//                ((TextView) view.findViewById(R.id.tv_music_name)).setTextColor(Color.parseColor("#ec505e"));
//                ((TextView) view.findViewById(R.id.tv_music_singer)).setTextColor(Color.parseColor("#ec505e"));
//                ((TextView) view.findViewById(R.id.tv_music_number)).setTextColor(Color.parseColor("#ec505e"));
//            }
//        }
//
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        TextView name = ((TextView) view.findViewById(R.id.tv_music_name));
//        name.setTextColor(Color.parseColor("#ec505e"));
//
//        // 定位当前播放歌曲
//        String listName;
//        int sourceId;
//        if (musicListManager !=null) {
//            listName = musicListManager.getListName();
//            sourceId = musicListManager.getList().get(position).getId();
//        } else {
////            listName = MusicLocator.getLocatedList().getListName();
//            sourceId = MusicLocator.getSourceId();
//        }
//        Bundle bundle = new Bundle();
////        bundle.putString("list_name", listName);
//        bundle.putInt("position", position);
//        bundle.putInt("source_id", sourceId);
////        MusicLocator.setLocation(bundle);
//
//        // 发送播放音乐的广播
//        BroadCastHelper.send(BroadCastHelper.ACTION_MUSIC_PLAY);
//
//        // 更新列表
//        refreshMusicList();
//    }
//
//    /**
//     * 动态注册广播接收
//     */
//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(-1000);
//        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT);
//        filter.addAction(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM);
//        receiver = new Receiver();
//        getActivity().registerReceiver(receiver, filter);
//    }
//
//    public class Receiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_NEXT)) {
//                refreshMusicList();
//            } else if (action.equals(BroadCastHelper.ACTION_MUSIC_PLAY_RANDOM)) {
//                refreshMusicList();
//            }
//        }
//    }
//
//}
