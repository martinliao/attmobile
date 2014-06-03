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
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.pojos.Report;
import net.mindeos.attmobile.pojos.ReportBase;
import net.mindeos.attmobile.pojos.ReportHeader;

import java.util.ArrayList;

/**
 * Adapter for the report items.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class ReportAdapter extends BaseAdapter {

    /**
     * Class for the accessible elements of this view.
     */
    static class ViewHolder {
        /**
         * TextView for the item date.
         */
        TextView tvReportItemDate;
        /**
         * TextView for the item time.
         */
        TextView tvReportItemHour;
        /**
         * TextView for the item course name.
         */
        TextView tvReportItemCourse;
        /**
         * TextView for the item AttControl.
         */
        TextView tvReportItemAttControl;
        /**
         * TextView for the item status.
         */
        TextView tvReportItemStatus;
        /**
         * TextView for the item remarks.
         */
        TextView tvReportItemRemarks;

        /**
         * TextView for the item group title.
         */
        TextView tvReportGroupTitle;

        /**
         * The report that is representing this item.
         */
        ReportBase report;
    }


    private static final String TAG = "ReportItemAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<ReportBase> data;
    private LayoutInflater inflater = null;
    private boolean isatthome;

    private Context ctx;

    /**
     * Instantiates a new Report adapter.
     *
     * @param c the context in which the adapter is defined.
     * @param reportlist the report, as a list of report items.
     * @param isatthome is this an atthome report or an attcontrol report.
     */
    public ReportAdapter(Context c, ArrayList<ReportBase> reportlist, boolean isatthome) {
        this.data = reportlist;
        this.ctx = c;
        this.isatthome = isatthome;
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
        final ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.report = (ReportBase) getItem(position);


        if (holder.report.getClass() == Report.class) {
            Report r = (Report)holder.report;

            convertView = inflater.inflate(R.layout.itemadapter_report, null);

            holder.tvReportItemDate = (TextView) convertView.findViewById(R.id.tvReportItemDate);
            holder.tvReportItemHour = (TextView) convertView.findViewById(R.id.tvReportItemHour);
            holder.tvReportItemCourse = (TextView) convertView.findViewById(R.id.tvReportItemCourse);
            holder.tvReportItemAttControl = (TextView) convertView.findViewById(R.id.tvReportItemAttControl);
            holder.tvReportItemStatus = (TextView) convertView.findViewById(R.id.tvReportItemStatus);
            holder.tvReportItemRemarks = (TextView) convertView.findViewById(R.id.tvReportItemRemarks);


            holder.tvReportItemDate.setText(Att.shdf.format(r.getSdate()));
            holder.tvReportItemHour.setText(Att.shf.format(r.getSdate()));
            holder.tvReportItemCourse.setText(r.getCname());
            holder.tvReportItemAttControl.setText(r.getAcname());
            holder.tvReportItemStatus.setText(Att.getAcronym(r.getStatusid()));



            if (r.getRemarks().length() > 0) {
                holder.tvReportItemRemarks.setVisibility(View.VISIBLE);
            }
            else {
                holder.tvReportItemRemarks.setVisibility(View.INVISIBLE);
            }

            holder.tvReportItemStatus.setTextColor(Att.getColor(0));
            holder.tvReportItemStatus.setBackgroundColor(Att.getColor(r.getStatusid()));

            if (isatthome)
                holder.tvReportItemCourse.setTextColor(ctx.getResources().getColor(R.color.red0));

        }
        else {
            ReportHeader r = (ReportHeader)holder.report;

            convertView = inflater.inflate(R.layout.itemgroupadapter_report, null);

            holder.tvReportGroupTitle = (TextView) convertView.findViewById(R.id.tvReportGroupTitle);

            holder.tvReportGroupTitle.setText(r.getUfirstname()+" "+r.getUlastname());

            if (isatthome) {
                holder.tvReportGroupTitle.setBackground(ctx.getResources().getDrawable(R.color.red0));
            }
        }



        convertViewCounter++;
        convertView.setTag(holder);

        return convertView;
    }
}
