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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.mindeos.attmobile.app.R;
import net.mindeos.attmobile.pojos.AttHome;

import java.util.ArrayList;


/**
 * Adapter for the AttHome items
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeAdapter extends BaseAdapter {

    /**
     * Class for the accessible elements of this view.
     */
    static class ViewHolder {
        /**
         * Course TextView
         */
        TextView tvAttHomeCourse;
        /**
         * AttHome name TextView
         */
        TextView tvAttHomeName;
        /**
         * AttHome instance
         */
        AttHome atthome;
    }


    private static final String TAG = "AttHomeItemAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<AttHome> data;
    private LayoutInflater inflater = null;


    /**
     * Instantiates a new AttHome adapter.
     *
     * @param c the Context in which we use the adapter.
     * @param atthomeList the AttHome list, containing all the AttHome instances
     */
    public AttHomeAdapter(Context c, ArrayList<AttHome> atthomeList) {
        this.data = atthomeList;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.itemadapter_atthome, null);

            convertViewCounter++;

            holder = new ViewHolder();

            holder.tvAttHomeCourse = (TextView) convertView
                    .findViewById(R.id.tvAttHomeCourse);
            holder.tvAttHomeName = (TextView) convertView
                    .findViewById(R.id.tvAttHomeName);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Setting all values in listview
        holder.atthome = (AttHome) getItem(position);

        holder.tvAttHomeCourse.setText(holder.atthome.getCoursename());
        holder.tvAttHomeName.setText(holder.atthome.getAhname());


        return convertView;
    }
}
