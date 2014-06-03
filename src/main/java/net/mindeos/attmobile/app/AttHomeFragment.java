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

package net.mindeos.attmobile.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.mindeos.attmobile.adapters.AttHomeAdapter;
import net.mindeos.attmobile.pojos.AttHome;
import net.mindeos.attmobile.webservice.RestAttHome;

import java.util.ArrayList;
import java.util.Date;


/**
 * Fragment for the AttHome activity
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeFragment extends Fragment {

    private View v;
    private ArrayList<AttHome> atthomeList;
    private ListView lvAttHomes;
    private TextView tvNoAttHomes;
    private LayoutInflater inflater = null;


    /**
     * Instantiates a new AttHome fragment.
     */
    public AttHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_atthome, container, false);

        atthomeList = new ArrayList<AttHome>();

        lvAttHomes = (ListView) v.findViewById(R.id.lvAttHomes);
        tvNoAttHomes = (TextView) v.findViewById(R.id.tvNoAttHome);
        lvAttHomes.setAdapter(new AttHomeAdapter(getActivity(), atthomeList));



        lvAttHomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                AttHome ah = atthomeList.get(position);

                FragmentActivity a = (FragmentActivity) v.getContext();
                Intent ahreports = new Intent(a, AttHomeReportsActivity.class);

                ahreports.putExtra("ahid", ah.getAhid());
                ahreports.putExtra("ahname", ah.getAhname());
                ahreports.putExtra("ahcourse", ah.getCoursename());

                startActivity(ahreports);
            }
        });

        GetAttHomesTask ght = new GetAttHomesTask();
        ght.execute();

        return v;
    }


    private class GetAttHomesTask extends AsyncTask<Date, Integer, ArrayList<AttHome>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbAttHomesLoading).bringToFront();
            v.findViewById(R.id.pbAttHomesLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<AttHome> result) {
            v.findViewById(R.id.pbAttHomesLoading).setVisibility(View.INVISIBLE);

            try {
                atthomeList.clear();
                int i = 0;
                for(AttHome s : result) {
                    atthomeList.add(i++, s);
                }

                ((AttHomeAdapter) lvAttHomes.getAdapter()).notifyDataSetChanged();

                if (result.size() == 0) {
                    tvNoAttHomes.setVisibility(View.VISIBLE);
                }
                else {
                    tvNoAttHomes.setVisibility(View.INVISIBLE);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected ArrayList<AttHome> doInBackground(Date... params) {
            ArrayList<AttHome> result = null;
            try {
                result = RestAttHome.getAttHomes();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }


    }
}