package net.mindeos.attmobile.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import net.mindeos.attmobile.adapters.SessionAdapter;
import net.mindeos.attmobile.dialogs.CustomDialogBuilder;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.RestSession;
import net.mindeos.attmobile.pojos.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Fragment for the AttControl activity
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttControlFragment extends Fragment implements View.OnClickListener, CalendarDatePickerDialog.OnDateSetListener {

    private OnFragmentInteractionListener mListener;

    private TextView tvSessionsDate;
    private View v;
    private ArrayList<Session> sessionsList;
    private ListView lvElements;
    private ImageButton btnSessionsPrevious;
    private ImageButton btnSessionsNext;
    private TextView tvNoSessions;

    private LayoutInflater inflater = null;


    /**
     * Create a new instance of this fragment.
     *
     * @return A new instance of fragment AttControlFragment.
     */
    public static AttControlFragment newInstance() {
        AttControlFragment fragment = new AttControlFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        jumpDate(v, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);

        this.inflater = inflater;

        v = inflater.inflate(R.layout.fragment_attcontrol, container, false);

        tvSessionsDate = (TextView) v.findViewById(R.id.tvSessionsDate);
        btnSessionsNext = (ImageButton) v.findViewById(R.id.btnSessionsNext);
        btnSessionsPrevious = (ImageButton) v.findViewById(R.id.btnSessionsPrevious);
        tvNoSessions = (TextView) v.findViewById(R.id.tvNoSessions);

        tvSessionsDate.setText(Att.sdf.format(new Date()));

        tvSessionsDate.setOnClickListener(this);
        btnSessionsPrevious.setOnClickListener(this);
        btnSessionsNext.setOnClickListener(this);

        sessionsList = new ArrayList<Session>();

        lvElements = (ListView) v.findViewById(R.id.lstSessions);
        lvElements.setAdapter(new SessionAdapter(getActivity(), sessionsList));

        lvElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Session s = sessionsList.get(position);

                FragmentActivity a = (FragmentActivity) v.getContext();
                Intent takeAttendance = new Intent(a, TakeAttendanceActivity.class);

                takeAttendance.putExtra("sessionid", s.getId());
                takeAttendance.putExtra("sessioncourse", s.getCoursename());
                takeAttendance.putExtra("sessiondate", Att.shdf.format(s.getSessdate()) + " - " + Att.shf.format(s.getSessdate()));

                startActivity(takeAttendance);
            }
        });

        final CharSequence[] lclickactions = {getString(R.string.editsession), getString(R.string.deletesession)};


        lvElements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final FragmentActivity a = (FragmentActivity) v.getContext();
                final Session s = sessionsList.get(position);

                CustomDialogBuilder bld=new CustomDialogBuilder(a);
                bld.setTitle(a.getString(R.string.selectaction)).setItems(lclickactions, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            showDeleteDialog(s.getId());
                        }
                        else {
                            showEditDialog(s);
                        }
                    }
                });

                bld.show();
                return true;
            }
        });

        jumpDate(v, 0);

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
        if(v.getId()==tvSessionsDate.getId())
        {
            selectDate(v);
        }
        else if(v.getId()==btnSessionsPrevious.getId())
        {
            jumpDate(v, -1);
        }
        else if(v.getId()==btnSessionsNext.getId())
        {
            jumpDate(v, 1);
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog,  int year, int monthOfYear, int dayOfMonth) {
        Calendar c2 = Calendar.getInstance();
        c2.set(year, monthOfYear,dayOfMonth);
        tvSessionsDate.setText(Att.sdf.format(c2.getTime()));

        try {
            GetSessionsTask gst = new GetSessionsTask();
            gst.execute(c2.getTime());
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
     * Select a date.
     *
     * @param view the full view
     */
    public void selectDate(View view) {
        try {
            Date d = Att.sdf.parse(tvSessionsDate.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(AttControlFragment.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), "hola");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Jump to a date date in the future or past.
     *
     * @param view the view
     * @param jump the jump, 1 in the future, -1 in the past
     */
    public void jumpDate(View view, int jump) {
        try {
            Date d = Att.sdf.parse(tvSessionsDate.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            c.add(Calendar.DAY_OF_MONTH, jump);

            tvSessionsDate.setText(Att.sdf.format(c.getTime()));

            GetSessionsTask gst = new GetSessionsTask();
            gst.execute(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Show the delete dialog.
     *
     * @param sessionid the id of the session to delete
     */
    public void showDeleteDialog(final int sessionid) {
        CustomDialogBuilder builder = new CustomDialogBuilder(getActivity());

        builder.setTitle(getString(R.string.deletesession));
        builder.setMessage(getString(R.string.suredeletelong));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DeleteSessionTask dst = new DeleteSessionTask();
                dst.execute(sessionid);

                dialog.dismiss();
            }

        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        builder.show();

    }


    /**
     * Show the edit dialog.
     *
     * @param s the session to edit, with all its information
     */
    public void showEditDialog(Session s) {
        Intent intent = new Intent(this.getActivity(), EditSessionActivity.class);

        intent.putExtra("sessionId", s.getId());
        intent.putExtra("sessionDuration", s.getDuration());
        intent.putExtra("sessionTimestamp", s.getSessdate().getTime());
        intent.putExtra("sessionDescription", s.getDescription());

        startActivity(intent);
    }

    private class DeleteSessionTask extends AsyncTask<Integer, Integer, Boolean> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbSessionsLoading).bringToFront();
            v.findViewById(R.id.pbSessionsLoading).setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                RestSession.deleteSession(params[0]);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean b) {
            v.findViewById(R.id.pbSessionsLoading).setVisibility(View.INVISIBLE);
            if (b) {
                Toast.makeText(getActivity(), getString(R.string.deletesuccessful), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), getString(R.string.deleteunsuccessful), Toast.LENGTH_LONG).show();
            }
            jumpDate(v, 0);
        }
    }

    private class GetSessionsTask extends AsyncTask<Date, Integer, ArrayList<Session>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbSessionsLoading).bringToFront();
            v.findViewById(R.id.pbSessionsLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<Session> result) {
            v.findViewById(R.id.pbSessionsLoading).setVisibility(View.INVISIBLE);

            try {
                sessionsList.clear();
                int i = 0;
                for(Session s : result) {
                    sessionsList.add(i++, s);
                }

                ((SessionAdapter) lvElements.getAdapter()).notifyDataSetChanged();

                if (result.size() == 0) {
                    tvNoSessions.setVisibility(View.VISIBLE);
                }
                else {
                    tvNoSessions.setVisibility(View.INVISIBLE);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected ArrayList<Session> doInBackground(Date... params) {
            ArrayList<Session> result = null;
            try {
                result = RestSession.getSessions(params[0], params[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }


    }

}
