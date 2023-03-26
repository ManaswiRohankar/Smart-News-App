package com.bhartiya.smartnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhartiya.smartnews.Fragments.ContactUs;
import com.bhartiya.smartnews.Fragments.Favorite;
import com.bhartiya.smartnews.Fragments.Home;
import com.bhartiya.smartnews.Fragments.About;

/**
 * Created by Manaswi Rohankar
 */
public class DrawerViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView icon;

    public DrawerViewHolder(final Context context, View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        icon = (ImageView) itemView.findViewById(R.id.titleIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDrawerAdapter.selected_item = getPosition();
                MainActivity.title.setText(MainActivity.menuTitles.get(getPosition()));
                switch (getPosition()) {
                    case 0:
                        ((MainActivity) context).loadFragment(new Home(), false);
                        break;
                    case 1:
                        ((MainActivity) context).loadFragment(new Favorite(), false);
                        break;
                    case 2:
                        ((MainActivity) context).loadFragment(new ContactUs(), false);
                        break;
                    case 3:
                        ((MainActivity) context).loadFragment(new About(), false);
                        break;
                }
                MainActivity.customDrawerAdapter.notifyDataSetChanged();
                MainActivity.drawerLayout.closeDrawers();

            }
        });
    }

}
