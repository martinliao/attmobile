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
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import net.mindeos.attmobile.app.Preferences;
import net.mindeos.attmobile.app.R;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.AttControlCaller;
import net.mindeos.attmobile.pojos.TakeData;

import java.util.ArrayList;


/**
 * Adapter for the Take Attendance items.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class TakeDataAdapter extends BaseAdapter {

    /**
     * Class for the accessible elements of this view.
     */
    static class ViewHolder {
        /**
         * TextView for the user's full name.
         */
        TextView tvTakeDataFullname;
        /**
         * TextView for the attendance status.
         */
        TextView tvTakeDataStatus;
        /**
         * TextView for the remarks information.
         */
        TextView tvTakeDataRemarks;
        /**
         * ImageView for the user's picture.
         */
        ImageView imgTakeDataUserpicture;


        /**
         * The object representing the take attendance data.
         */
        TakeData takedata;
    }


    private static final String TAG = "TakeDataAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<TakeData> data;
    private LayoutInflater inflater = null;

    /**
     * The Contex in which the adapter is defined.
     */
    Context ctx;


    /**
     * Instantiates a new TakeData adapter.
     *
     * @param c the context in which the adapter is defined.
     * @param takedatalist the list of takedata to be shown.
     */
    public TakeDataAdapter(Context c, ArrayList<TakeData> takedatalist) {
        this.data = takedatalist;
        this.ctx = c;
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
    public int getViewTypeCount() {
        if (getCount() != 0)
            return getCount();

        return 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.itemadapter_takedata, null);

            convertViewCounter++;

            holder = new ViewHolder();


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTakeDataStatus = (TextView) convertView.findViewById(R.id.tvTakeDataStatus);
        holder.tvTakeDataRemarks = (TextView) convertView.findViewById(R.id.tvTakeDataRemarks);
        holder.tvTakeDataFullname = (TextView) convertView.findViewById(R.id.tvTakeDataFullname);
        holder.imgTakeDataUserpicture = (ImageView) convertView.findViewById(R.id.imgTakeDataUserpicture);
        holder.takedata = (TakeData) getItem(position);


        //Unchanged values of this holder.
        holder.tvTakeDataFullname.setText(holder.takedata.getFirstname() + " " + holder.takedata.getLastname());

        if (holder.takedata.getPic1() != Preferences.NOPICTURE) {
            UrlImageViewHelper.setUrlDrawable(holder.imgTakeDataUserpicture, AttControlCaller.getUserImageURL(holder.takedata.getPic1(), holder.takedata.getPic2()), R.drawable.userplaceholder);
        }
        else {
            UrlImageViewHelper.setUrlDrawable(holder.imgTakeDataUserpicture, "", R.drawable.userplaceholder);
        }

        //Changeable values for this holder.
        holder.tvTakeDataStatus.setText(Att.getAcronym(holder.takedata.getStatus()));

        if (holder.takedata.getRemarks().length() > 0) {
            holder.tvTakeDataRemarks.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvTakeDataRemarks.setVisibility(View.INVISIBLE);
        }

        holder.tvTakeDataStatus.setTextColor(Att.getColor(0));
        holder.tvTakeDataStatus.setBackgroundColor(Att.getColor(holder.takedata.getStatus()));

        convertView.setTag(holder);

        return convertView;
    }


}
