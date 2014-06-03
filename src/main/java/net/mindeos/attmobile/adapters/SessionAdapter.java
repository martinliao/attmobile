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
import android.widget.ImageButton;
import android.widget.TextView;

import net.mindeos.attmobile.app.R;
import net.mindeos.attmobile.pojos.Session;

import java.util.ArrayList;


/**
 * Adapter for the Session List items.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class SessionAdapter extends BaseAdapter {

    /**
     * Class for the accessible elements of this view.
     */
    static class ViewHolder {
        /**
         * TextView for the session time.
         */
        TextView tvSessionTime;
        /**
         * TextView for the for the session course.
         */
        TextView tvSessionCourse;
        /**
         * TextView for the attcontrol instance name.
         */
        TextView tvSessionAttControl;
        /**
         * ImageButton with an arrow, sending to take or retake attendance.
         */
        ImageButton imbTake;
        /**
         * The Session that this list item is representing.
         */
        Session session;
    }


    private static final String TAG = "SessionItemAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<Session> data;
    private LayoutInflater inflater = null;

    private static android.text.format.DateFormat df;


    /**
     * Instantiates a new Session adapter.
     *
     * @param c the context in which the adapter is defined.
     * @param sessionlist the list of sessions to be shown.
     */
    public SessionAdapter(Context c, ArrayList<Session> sessionlist) {
        this.data = sessionlist;
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
    public int getViewTypeCount()
    {
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
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.itemadapter_session, null);

            convertViewCounter++;

            holder = new ViewHolder();

            holder.tvSessionTime = (TextView) convertView
                    .findViewById(R.id.tvSessionTime);
            holder.tvSessionAttControl = (TextView) convertView
                    .findViewById(R.id.tvSessionAttendance);
            holder.tvSessionCourse = (TextView) convertView
                    .findViewById(R.id.tvSessionCourse);
            holder.imbTake = (ImageButton) convertView
                    .findViewById(R.id.imbTake);

            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        // Setting all values in listview
        holder.session = (Session) getItem(position);

        holder.tvSessionTime.setText(df.format("kk:mm", holder.session.getSessdate()));
        holder.tvSessionAttControl.setText(holder.session.getAttcontrolname());
        holder.tvSessionCourse.setText(holder.session.getCoursename());


        if (holder.session.getLasttaken() == null) {
            holder.imbTake.setImageResource(R.drawable.arrow_take);
        }
        else {
            holder.imbTake.setImageResource(R.drawable.arrow_retake);
        }

        return convertView;
    }
}
