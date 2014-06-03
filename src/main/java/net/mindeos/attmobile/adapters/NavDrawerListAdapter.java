// This file is part of AttMobile - http://att.mindeos.net/
//
// AttMobile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// AttMobile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with AttMobile. If not, see <http://www.gnu.org/licenses/>.

package net.mindeos.attmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mindeos.attmobile.app.R;

import java.util.ArrayList;

/**
 * Full list adapter for the navigation drawer.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    /**
     * Instantiates a new Navigation Drawer ListAdapter.
     *
     * @param context the context
     * @param navDrawerItems the nav drawer items
     */
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerItem ndi = navDrawerItems.get(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (ndi.isTitle()) {
                convertView = mInflater.inflate(R.layout.itemadapter_title_item, null);
            }
            else {
                convertView = mInflater.inflate(R.layout.itemadapter_drawer_item, null);
            }
        }


        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(ndi.getTitle());

        if (!ndi.isTitle()) {
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            imgIcon.setImageResource(ndi.getIcon());
        }
        else {
            convertView.setClickable(false);
            convertView.setEnabled(false);
        }

        return convertView;
    }

}
