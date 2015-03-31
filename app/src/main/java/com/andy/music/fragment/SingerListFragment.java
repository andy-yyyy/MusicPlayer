package com.andy.music.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.entity.TagConstants;
import com.andy.music.utility.MusicLocator;

/**
 * 歌曲列表模块
 * Created by Andy on 2014/12/16.
 */
public class SingerListFragment extends Fragment {

    private View mainView;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "SingerList--> onCreate");
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mainView = inflater.inflate(R.layout.fragment_list_common, (ViewGroup)getActivity().findViewById(R.id.view_pager_local_music), false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "SingerList--> onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TagConstants.TAG, "SingerList--> onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TagConstants.TAG, "SingerList--> onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TagConstants.TAG, "SingerList--> onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        Log.d(TagConstants.TAG, "SingerList--> onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TagConstants.TAG, "SingerList--> onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TagConstants.TAG, "SingerList--> onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TagConstants.TAG, "SingerList--> onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TagConstants.TAG, "SingerList--> onDetach");
        super.onDetach();
    }



    @Override
    public void onAttach(Activity activity) {
        Log.d(TagConstants.TAG, "SingerList--> onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "SingerList--> onInflate");
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "SingerList--> onCreateView");
        // 移除已存在的 view
        ViewGroup group = ((ViewGroup) mainView.getParent());
        if (group!=null) {
            group.removeAllViewsInLayout();
            Log.d(TagConstants.TAG, "已移除已存在的view--> SongList");
        }
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TagConstants.TAG, "SingerList--> onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        // 获取 ListView
        listView =(ListView) view.findViewById(R.id.lv_list_common);

        // 为 ListView 设置适配器
        listView.setAdapter(getAdapter());

        // 为 ListView 设置监听器
        listView.setOnItemClickListener(getOnItemClickListener());
    }

    public BaseAdapter getAdapter() {
//        String sql = MediaStore.Audio.Media.ARTIST +" = '杨幂'";
//        Cursor cursor = com.andy.music.data.CursorAdapter.get(sql);
        Cursor cursor = CursorAdapter.getMediaLibCursor();
        String[] from = {MediaStore.Audio.Media.ARTIST};
        int[] to = {R.id.tv_list_cell_double_line_first};
        SimpleCursorAdapter adapter =new SimpleCursorAdapter(getActivity(), R.layout.list_cell_double_line, cursor, from, to, 0);
        return adapter;
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取当前 item 的歌手名
                String singerName = ((TextView)view.findViewById(R.id.tv_list_cell_double_line_first)).getText().toString();

                // 将主体部分替换成 songListFragment
                SongListFragment fragment = new SongListFragment();
                Bundle bundle = new Bundle();
                String whereClause = MediaStore.Audio.Media.ARTIST + "='" + singerName + "'";
                bundle.putString("where_clause", whereClause);    // 将查询语句传递到 fragment
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_container_main_content, fragment).addToBackStack(null).commit();

            }
        };
    }


}
