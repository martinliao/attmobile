package net.mindeos.attmobile.app;

/**
 * Created by mindeos on 30/03/14.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.recurrencepicker.RecurrencePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import net.mindeos.attmobile.dialogs.RecurrencePicker;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.pojos.AttControl;
import net.mindeos.attmobile.pojos.AttCourse;
import net.mindeos.attmobile.pojos.Group;
import net.mindeos.attmobile.webservice.RestAttCourse;
import net.mindeos.attmobile.webservice.RestSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class NewSessionFragment extends Fragment implements
        View.OnClickListener,
        CalendarDatePickerDialog.OnDateSetListener,
        RadialTimePickerDialog.OnTimeSetListener,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RecurrencePicker.OnRecurrenceSetListener

{

    private View v;

    private Spinner courseSpinner;
    private Spinner attcontrolSpinner;
    private Spinner groupSpinner;

    private TextView groupLabel;

    private EditText etNewSessionDate;
    private EditText etNewSessionTime;
    private EditText etNewSessionDuration;
    private EditText etNewSessionRecurrence;
    private EditText etNewSessionDescripton;

    private ArrayList<AttCourse> attcourse;

    private SimpleDateFormat vshf = new SimpleDateFormat("kk:00");

    private String recurrencerule;
    private String basedate;

    private LayoutInflater inflater = null;


    /**
     * Instantiates a new NewSession fragment.
     */
    public NewSessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            basedate = getActivity().getIntent().getExtras().getString("currentdate");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        v = inflater.inflate(R.layout.fragment_newsession, container, false);

        courseSpinner = (Spinner) v.findViewById(R.id.spnNewSessionCourse);
        attcontrolSpinner = (Spinner) v.findViewById(R.id.spnNewSessionAttControl);
        groupSpinner = (Spinner) v.findViewById(R.id.spnNewSessionGroup);

        groupLabel = (TextView) v.findViewById(R.id.lblNewSessionGroup);

        etNewSessionDate = (EditText) v.findViewById(R.id.etNewSessionDate);
        etNewSessionTime = (EditText) v.findViewById(R.id.etNewSessionTime);
        etNewSessionDuration = (EditText) v.findViewById(R.id.etNewSessionDuration);
        etNewSessionRecurrence = (EditText) v.findViewById(R.id.etNewSessionRecurrence);
        etNewSessionDescripton = (EditText) v.findViewById(R.id.etNewSessionDescripton);


        if (basedate != null) {
            etNewSessionDate.setText(basedate);
        }
        else {
            etNewSessionDate.setText(Att.sdf.format(new Date()));
        }

        etNewSessionTime.setText(vshf.format(new Date()));
        etNewSessionDuration.setText("1:00");

        prepareData();

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prepareAttControlList(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        attcontrolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prepareGroupList((int) courseSpinner.getSelectedItemId(), i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        etNewSessionDate.setOnClickListener(this);
        etNewSessionTime.setOnClickListener(this);
        etNewSessionDuration.setOnClickListener(this);
        etNewSessionRecurrence.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==etNewSessionDate.getId())
        {
            selectDate(v);
        }
        else if(v.getId()==etNewSessionTime.getId())
        {
            selectTime(v);
        }
        else if(v.getId()==etNewSessionDuration.getId())
        {
            selectDuration(v);
        }
        else if(v.getId()==etNewSessionRecurrence.getId())
        {
            selectRecurrence(v);
        }
    }

    private void selectDate(View v) {
        try {
            Date d = Att.sdf.parse(etNewSessionDate.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(NewSessionFragment.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), etNewSessionDate.getId()+"");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectTime(View v) {
        try {
            Date d = Att.shf.parse(etNewSessionTime.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            RadialTimePickerDialog radialTimePickerDialog = RadialTimePickerDialog.newInstance(NewSessionFragment.this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)%30, true);
            radialTimePickerDialog.show(getActivity().getSupportFragmentManager(), etNewSessionTime.getId()+"");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectRecurrence(View v) {

        try {
            Bundle b = new Bundle();
            Date d = Att.sdf.parse(etNewSessionDate.getText().toString());

            Time t = new Time();
            t.set(d.getTime());

            b.putLong(RecurrencePickerDialog.BUNDLE_START_TIME_MILLIS, t.toMillis(false));
            b.putString(RecurrencePickerDialog.BUNDLE_TIME_ZONE, t.timezone);
            b.putString(RecurrencePickerDialog.BUNDLE_RRULE, recurrencerule);

            RecurrencePicker rpd = new RecurrencePicker();
            rpd.setArguments(b);
            rpd.setOnRecurrenceSetListener(NewSessionFragment.this);
            rpd.show(getActivity().getSupportFragmentManager(), "recurrencedialog");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectDuration(View v) {
        TimePickerBuilder tpb = new TimePickerBuilder()
                .setFragmentManager(getActivity().getSupportFragmentManager())
                .setStyleResId(R.style.AttControlPickerTheme);
        tpb.addTimePickerDialogHandler(NewSessionFragment.this);
        tpb.show();
    }


    private void prepareData() {
        try {
            GetAttCoursesTask gst = new GetAttCoursesTask();
            gst.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareCourseList() {
        ArrayList<String> courseList = new ArrayList<String>();

        for (AttCourse c : attcourse) {
            courseList.add(c.getCourse().getCoursename());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, courseList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adp);
    }


    private void prepareAttControlList(int course) {
        ArrayList<String> attcontrolList = new ArrayList<String>();

        for (AttControl c : attcourse.get(course).getAttcontrols() ) {
            attcontrolList.add(c.getAcname());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, attcontrolList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attcontrolSpinner.setAdapter(adp);
    }

    private void prepareGroupList(int course, int attcontrol) {
        ArrayList<String> groupList = new ArrayList<String>();

        AttControl selectedAttControl = attcourse.get(course).getAttcontrols().get(attcontrol);

        if (selectedAttControl.getGroupmode() == Att.NOGROUPS) {
            groupSpinner.setVisibility(View.GONE);
            groupLabel.setVisibility(View.GONE);
        }
        else {
            groupSpinner.setVisibility(View.VISIBLE);
            groupLabel.setVisibility(View.VISIBLE);
        }


        for (Group g : selectedAttControl.getGroups() ) {
            groupList.add(g.getGroupname());
        }

        if (selectedAttControl.getGroupmode() == Att.VISIBLEGROUPS) {
            groupList.add(getString(R.string.common_session));
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, groupList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(adp);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        if (dialog.getTag().equals(etNewSessionDate.getId()+"")) {
            etNewSessionDate.setText(Att.sdf.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout dialog, int hour, int minutes) {
        Calendar c = Calendar.getInstance();
        c.set(1990, Calendar.JANUARY, 1, hour, minutes);

        etNewSessionTime.setText(Att.shf.format(c.getTime()));
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        etNewSessionDuration.setText("" + hourOfDay + ":" + String.format("%02d", minute));
    }

    @Override
    public void onRecurrenceSet(String s) {
        recurrencerule = s;

        if (s != null) etNewSessionRecurrence.setText(getString(R.string.session_recurrent));
        else etNewSessionRecurrence.setText(getString(R.string.session_notrecurrent));
    }

    /**
     * Save the data of this fragment.
     */
    public void saveData() {
        try {
            SaveNewSessionTask snst = new SaveNewSessionTask();
            snst.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class GetAttCoursesTask extends AsyncTask<Integer, Integer, ArrayList<AttCourse>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbNewSessionLoading).bringToFront();
            v.findViewById(R.id.pbNewSessionLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<AttCourse> result) {
            v.findViewById(R.id.pbNewSessionLoading).setVisibility(View.INVISIBLE);
            attcourse = result;
            prepareCourseList();
        }

        protected ArrayList<AttCourse> doInBackground(Integer... params) {
            ArrayList<AttCourse> result = null;
            try {
                result = RestAttCourse.getAttCourses();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    private class SaveNewSessionTask extends AsyncTask<Void, Integer, Integer> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbNewSessionLoading).bringToFront();
            v.findViewById(R.id.pbNewSessionLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Integer result) {
            v.findViewById(R.id.pbNewSessionLoading).setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Session successfully saved", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        protected Integer doInBackground(Void... params) {
            try {
                String sessiondatetime = etNewSessionDate.getText()+" "+etNewSessionTime.getText();

                //Session duration will always have a value.
                String sessionduration = etNewSessionDuration.getText().toString();
                String[] durationarray = sessionduration.split(":");
                int duration = (Integer.parseInt(durationarray[0])*60 + Integer.parseInt(durationarray[1])) * 60;

                AttCourse ac = attcourse.get(courseSpinner.getSelectedItemPosition());
                AttControl atc = ac.getAttcontrols().get(attcontrolSpinner.getSelectedItemPosition());

                int selgroup = -100;

                if (atc.getGroupmode() != Att.NOGROUPS) {
                    selgroup = groupSpinner.getSelectedItemPosition();
                    if (selgroup >= atc.getGroups().size()) {
                        selgroup = Att.COMMONGROUP;
                    }
                    else {
                        selgroup = atc.getGroups().get(groupSpinner.getSelectedItemPosition()).getGroupid();
                    }
                }

                Long sessiontimestamp = Att.sdhf.parse(sessiondatetime).getTime();

                String description = etNewSessionDescripton.getText().toString();

                RestSession.saveNewSession(ac.getCourse().getCourseid(), atc.getAcid(), selgroup, sessiontimestamp, duration, recurrencerule, description);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    }
}