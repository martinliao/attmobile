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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.mindeos.attmobile.adapters.TakeDataAdapter;
import net.mindeos.attmobile.dialogs.CustomDialogBuilder;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.RestTakeData;
import net.mindeos.attmobile.pojos.Status;
import net.mindeos.attmobile.pojos.TakeData;

import java.util.ArrayList;


/**
 * The type Take attendance fragment.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class TakeAttendanceFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private View v;
    private ArrayList<TakeData> takedata;
    private ListView lvElements;
    private CharSequence[] statusList;

    private int sessionid;
    private String sessioncourse;
    private String sessiondate;

    private LayoutInflater inflater;

    /**
     * Create a new instance of this fragment
     *
     * @return A new instance of fragment TakeAttendanceFragment.
     */
    public static TakeAttendanceFragment newInstance() {
        TakeAttendanceFragment fragment = new TakeAttendanceFragment();
        return fragment;
    }

    /**
     * Instantiates a new TakeAttendance fragment.
     */
    public TakeAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sessionid = getActivity().getIntent().getExtras().getInt("sessionid");
            sessioncourse = getActivity().getIntent().getExtras().getString("sessioncourse");
            sessiondate = getActivity().getIntent().getExtras().getString("sessiondate");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);

        inflater = inf;

        v = inflater.inflate(R.layout.fragment_takeattendance, container, false);

        takedata = new ArrayList<TakeData>();

        lvElements = (ListView) v.findViewById(R.id.lstTakeAttendance);
        lvElements.setAdapter(new TakeDataAdapter(getActivity(), takedata));


        lvElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                TakeDataAdapter tda = (TakeDataAdapter)parent.getAdapter();
                TakeData td = (TakeData) tda.getItem(position);

                td.setStatus(Att.getNextStatus(td.getStatus()));

                tda.notifyDataSetChanged();

            }
        });


        lvElements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                TakeDataAdapter tda = (TakeDataAdapter)parent.getAdapter();
                TakeData td = (TakeData) tda.getItem(position);

                showRemarksDialog(getActivity(), td);

                tda.notifyDataSetChanged();

                return true;
            }
        });


        getActivity().getActionBar().setSubtitle(sessioncourse+" ("+sessiondate+")");

        prepareData();
        prepareSetAllDialog();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * The interface to interact with this fragment
     */
    public interface OnFragmentInteractionListener {
        /**
         * On fragment interaction.
         *
         * @param uri the uri
         */
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * Prepare the data for this fragment.
     */
    public void prepareData() {
        try {
            GetTakeDataTask gst = new GetTakeDataTask();
            gst.execute(sessionid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the edited data in this fragment.
     */
    public void saveData() {
        try {
            SaveTakeDataTask sst = new SaveTakeDataTask();
            sst.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the remarks dialog.
     *
     * @param ctx the context
     * @param td the attendance take data
     */
    protected void showRemarksDialog(Context ctx, final TakeData td) {
        // get prompts.xml view
        View promptView = inflater.inflate(R.layout.dialog_remarks, null);
        CustomDialogBuilder alertDialogBuilder = new CustomDialogBuilder(ctx);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle(ctx.getString(R.string.remarks));

        final EditText etRemarks = (EditText)promptView.findViewById(R.id.etRemarksDialogRemarks);
        etRemarks.setText(td.getRemarks());

        // setup a dialog window
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );
        alertDialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                td.setRemarks(etRemarks.getText().toString());
                ((TakeDataAdapter) lvElements.getAdapter()).notifyDataSetChanged();
            }
        });

        alertDialogBuilder.show();
    }

    /**
     * Prepare set all dialog.
     */
    protected void prepareSetAllDialog() {

        ArrayList<String> statuses = new ArrayList<String>();

        for(Status s: Att.getAllStatuses()) {
            statuses.add(s.getDescription());
        }

        statusList = statuses.toArray(new CharSequence[statuses.size()]);


    }

    /**
     * Show set all dialog.
     */
    public void showSetAllDialog() {
        Activity a = getActivity();

        CustomDialogBuilder bld = new CustomDialogBuilder(a);
        bld.setTitle(a.getString(R.string.action_setallto));
        bld.setItems(statusList, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (TakeData td : takedata) {
                    td.setStatus(which+1);
                }
                ((TakeDataAdapter) lvElements.getAdapter()).notifyDataSetChanged();
            }
        });

        bld.show();
    }


    private class GetTakeDataTask extends AsyncTask<Integer, Integer, ArrayList<TakeData>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbTakeAttendanceLoading).bringToFront();
            v.findViewById(R.id.pbTakeAttendanceLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<TakeData> result) {
            v.findViewById(R.id.pbTakeAttendanceLoading).setVisibility(View.INVISIBLE);

            try {
                takedata.clear();
                int i = 0;
                for (TakeData s : result) {
                    takedata.add(i++, s);
                }
                ((TakeDataAdapter) lvElements.getAdapter()).notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        protected ArrayList<TakeData> doInBackground(Integer... params) {
            ArrayList<TakeData> result = null;
            try {
                result = RestTakeData.getTakeData(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
        }
    }


    private class SaveTakeDataTask extends AsyncTask<Void, Integer, Integer> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbTakeAttendanceLoading).bringToFront();
            v.findViewById(R.id.pbTakeAttendanceLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Integer result) {
            v.findViewById(R.id.pbTakeAttendanceLoading).setVisibility(View.INVISIBLE);

            Toast.makeText(getActivity(), getString(R.string.attendance_saved), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        protected Integer doInBackground(Void... params) {
            try {
                RestTakeData.saveTakeData(sessionid, takedata);
            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.attendance_notsaved), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return 0;
        }
    }

}
