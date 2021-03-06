package com.andy.music.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.andy.music.util.ContextUtil;

/**
 * 根据指定条件创建用于查询媒体库的 Cursor
 * Created by Andy on 2014/11/17.
 */
public class CursorAdapter {

    // 查询系统媒体库的参数申明
    private static final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String[] projection = {MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM};
    private static final String selection =MediaStore.Audio.Media.DURATION+">60000";
    private static final String[] selectionArgs =null;
    private static final String order = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

    /**
     * query()方法参数说明 :
     * public final Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
     *
     * uri(Uri)--> The URI, using the content:// scheme, for the content to retrieve.
     *
     * projection(String[])--> A list of which columns to return. Passing null will return all columns, which is inefficient.
     *
     * selection(String)--> A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself)
     *      Passing null will return all rows for the given URI.
     *
     * selectionArgs(String[])--> You may include ?s in selection, which will be replaced by the values from selectionArgs,
     *      in the order that they appear in the selection. The values will be bound as Strings.
     *
     * sortOrder(String)--> How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself).
     *      Passing null will use the default sort order, which may be unordered.
     *
     * 以下 方法返回媒体库所有音乐文件
     *  Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
     *               null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
     *
     */


    /**
     * 根据 WHERE 语句得到相应的 Cursor
     *
     * @return 返回 Cursor
     */

    public static Cursor get(String selection, String[] selectionArgs) {
        return getMediaLibCursor(uri, projection,selection, selectionArgs, order);
    }

    /**
     * 根据 ID 得到相应的 Cursor
     *
     * @param srcId 音乐文件的 ID
     * @return 返回 Cursor
     */
    public static Cursor get(int srcId) {
        String selection = "_id = " + srcId;
        return getMediaLibCursor(uri, projection,selection, selectionArgs, order);
    }

    /**
     * 将与音乐列表数据库关联的 Cursor 转换成 与系统媒体库关联的 Cursor
     * 注 : 音乐列表中的数据库只有数据文件的原有 ID, 不能查询文件其他信息
     *
     * @param cursor 与音乐列表数据库关联的 Cursor
     * @return 与系统媒体库关联的 Cursor
     */
    public static Cursor translate(Cursor cursor) {

        StringBuilder idString = new StringBuilder();

        try {
            while (cursor.moveToNext()) {
                int srcId = cursor.getInt(cursor.getColumnIndexOrThrow("source_id"));
                idString.append(srcId + ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();  // 关闭 Cursor ，释放资源
        }

        String selection;
        if (idString.toString().equals("")) {
            selection = "_id IN()";
        } else {
            selection = "_id IN(" + idString.toString().substring(0, idString.length() - 1) + ")";
        }
        return getMediaLibCursor(uri, projection,selection, selectionArgs, order);
    }

    public static Cursor getMediaLibCursor() {
        return getMediaLibCursor(uri, projection,selection, selectionArgs, order);
    }

    public static Cursor getMediaLibCursor(Uri uri,String[] projection, String selection, String[] selectionArgs, String order) {
        return ContextUtil.getInstance().getContentResolver().query(uri, projection, selection, selectionArgs, order);
    }

}
