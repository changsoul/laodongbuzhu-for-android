package com.wudaosoft.laodongbuzhu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created on 2018/3/18 23:10.
 *
 * @author Changsoul.Wu
 */

public abstract class ListAdapter<T> extends BaseAdapter {

    private List<T> list = null;

    private Context context = null;

    private int layoutId;

    public ListAdapter(Context context, int layoutId, List<T> list) {
        this.list = list;
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.createIfNull(context, convertView, layoutId, parent, position);

        setViewData(holder, getItem(position));

        return holder.getConvertView();
    }

    public abstract void setViewData(ViewHolder holder, T data);
}
