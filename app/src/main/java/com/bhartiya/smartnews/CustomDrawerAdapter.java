package com.bhartiya.smartnews;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manaswi Rohankar
 */
public class CustomDrawerAdapter extends RecyclerView.Adapter<DrawerViewHolder> {
    Context context;
    List<String> menuTitles;
    public static int selected_item = 0;
    ArrayList<Integer> icons;

    public CustomDrawerAdapter(Context context,
                               List<String> menuTitles, ArrayList<Integer> icons) {
        this.context = context;
        this.menuTitles = menuTitles;
        this.icons=icons;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drawer_list_item, null);
        DrawerViewHolder drawerViewHolder = new DrawerViewHolder(context, view);
        return drawerViewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {

        if (position == selected_item) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        } else
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));


        holder.title.setText(menuTitles.get(position));
        holder.icon.setImageResource(icons.get(position));
    }

    @Override
    public int getItemCount() {
        return menuTitles.size();
    }
}
