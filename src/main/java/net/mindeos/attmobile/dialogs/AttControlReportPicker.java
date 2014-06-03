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

package net.mindeos.attmobile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import net.mindeos.attmobile.app.R;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.pojos.AttControl;
import net.mindeos.attmobile.pojos.AttCourse;
import net.mindeos.attmobile.pojos.Group;
import net.mindeos.attmobile.webservice.RestUser;
import net.mindeos.attmobile.pojos.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Dialog to pick the preferences for the AttControl Report
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttControlReportPicker extends DialogFragment implements CalendarDatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<AttCourse> attcourses;

    private int coursepos = -1;
    private int attcontrolpos = -1;
    private int groupidpos = -1;
    private int studentpos = -1;
    private int studentid = -1;
    private Date startdate;
    private Date enddate;

    private boolean spnStudentChanged = false;

    /**
     * The user list.
     */
    ArrayList<User> userList = new ArrayList<User>();

    private View v;

    private TextView tvReportPickerAttControl;
    private TextView tvReportPickerGroup;

    private Spinner spnReportPickerCourse;
    private Spinner spnReportPickerAttControl;
    private Spinner spnReportPickerGroup;
    private Spinner spnReportPickerStudent;

    private TextView tvReportPickerStartDate;
    private TextView tvReportPickerEndDate;

    /**
     * Creates a new instance of this dialog
     *
     * @return the attcontrol report picker dialog
     */
    public static AttControlReportPicker newInstance() {
        AttControlReportPicker fragment = new AttControlReportPicker();
        return fragment;
    }

    /**
     * Creates a new instance of this dialog
     */
    public AttControlReportPicker() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        attcourses = getArguments().getParcelableArrayList("courses");

        coursepos = getArguments().getInt("coursepos");
        attcontrolpos = getArguments().getInt("attcontrolpos");
        groupidpos = getArguments().getInt("groupidpos");
        studentid = getArguments().getInt("studentid");

        Calendar cal = Calendar.getInstance();

        if (getArguments().getLong("enddate") != -1) {
            enddate = new Date(getArguments().getLong("enddate"));
        }
        else {
            enddate = cal.getTime();
        }

        cal.setTime(enddate);

        if (getArguments().getLong("startdate") != -1) {
            startdate = new Date(getArguments().getLong("startdate"));
        }
        else {
            cal.add(Calendar.MONTH, -1);
            startdate = cal.getTime();
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_attcontrolreportpicker, null);

        tvReportPickerStartDate = (TextView) v.findViewById(R.id.tvReportPickerStartDate);
        tvReportPickerEndDate = (TextView) v.findViewById(R.id.tvReportPickerEndDate);
        tvReportPickerAttControl = (TextView) v.findViewById(R.id.tvReportPickerAttControl);

        tvReportPickerGroup = (TextView) v.findViewById(R.id.tvReportPickerGroup);

        spnReportPickerCourse = (Spinner) v.findViewById(R.id.spnReportPickerCourse);
        spnReportPickerAttControl = (Spinner) v.findViewById(R.id.spnReportPickerAttControl);
        spnReportPickerGroup = (Spinner) v.findViewById(R.id.spnReportPickerGroup);
        spnReportPickerStudent = (Spinner) v.findViewById(R.id.spnReportPickerStudent);

        tvReportPickerStartDate.setText(Att.sdf.format(startdate));
        tvReportPickerEndDate.setText(Att.sdf.format(enddate));

        tvReportPickerStartDate.setOnClickListener(this);
        tvReportPickerEndDate.setOnClickListener(this);

        prepareEmptyStudentList();
        prepareCourseList();

        prepareSelections();

        spnReportPickerCourse.setOnItemSelectedListener(this);
        spnReportPickerAttControl.setOnItemSelectedListener(this);
        spnReportPickerGroup.setOnItemSelectedListener(this);


        spnReportPickerStudent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (spnStudentChanged) {
                    prepareStudentsList();
                    spnStudentChanged = false;
                }
                return false;
            }
        });
        spnReportPickerStudent.setOnItemSelectedListener(this);


        CustomDialogBuilder builder = new CustomDialogBuilder(getActivity());

        builder.setView(v);
        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                User selectedstudent = null;
                if (studentpos != -1) selectedstudent = userList.get(studentpos);

                mListener.onPositiveButtonClicked(coursepos, attcontrolpos, groupidpos, selectedstudent, startdate, enddate);
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dismiss();
            }
        });

        return builder.create();
    }

    private void prepareSelections() {

        if (coursepos != -1) {
            selectCourse(coursepos+1);

            if (attcontrolpos != -1) {
                selectAttControl(attcontrolpos+1);

                if (groupidpos != -1) {
                    selectGroup(groupidpos+1);
                }
            }
            else {
                spnReportPickerGroup.setVisibility(View.GONE);
                tvReportPickerGroup.setVisibility(View.GONE);
            }
        }
        else {
            spnReportPickerAttControl.setVisibility(View.GONE);
            tvReportPickerAttControl.setVisibility(View.GONE);

            spnReportPickerGroup.setVisibility(View.GONE);
            tvReportPickerGroup.setVisibility(View.GONE);
        }


        if (studentid != -1) prepareStudentsList();
        else spnStudentChanged = true;


        spnReportPickerCourse.setSelection(coursepos+1, true);
        spnReportPickerAttControl.setSelection(attcontrolpos+1, true);
        spnReportPickerGroup.setSelection(groupidpos+1, true);
        spnReportPickerStudent.setSelection(studentpos+1, true);

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
    public void onItemSelected(AdapterView<?> parent, View t, int position, long id) {
        if(parent.getId()==spnReportPickerCourse.getId()) {
            selectCourse(position);
        }
        else if (parent.getId()==spnReportPickerAttControl.getId()) {
            selectAttControl(position);
        }
        else if (parent.getId()==spnReportPickerGroup.getId()) {
            selectGroup(position);
        }
        else if (parent.getId()==spnReportPickerStudent.getId()) {
            selectStudent(position);
        }
    }




    private void selectDate(TextView v) {
        try {
            Date d = Att.sdf.parse(v.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            CalendarDatePickerDialog cal = CalendarDatePickerDialog.newInstance(AttControlReportPicker.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            cal.show(getActivity().getSupportFragmentManager(), v.getId()+"");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        TextView tvResult = (TextView) v.findViewById(Integer.parseInt(dialog.getTag()));

        tvResult.setText(Att.sdf.format(c.getTime()));

        checkDates();
    }

    private void checkDates() {
        try {
            Date d1 = Att.sdf.parse(tvReportPickerStartDate.getText().toString());
            Date d2 = Att.sdf.parse(tvReportPickerEndDate.getText().toString());

            if (d2.getTime() < d1.getTime()) {
                tvReportPickerStartDate.setText(Att.sdf.format(d2));
                tvReportPickerEndDate.setText(Att.sdf.format(d1));

                startdate = d2;
                enddate = d1;
            }
            else {
                startdate = d1;
                enddate = d2;
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View t) {
        if(t.getId()==tvReportPickerStartDate.getId())
        {
            selectDate((TextView)t);
        }
        else if(t.getId()==tvReportPickerEndDate.getId())
        {
            selectDate((TextView)t);
        }
    }

    /**
     * Prepares the course list.
     */
    public void prepareCourseList() {
        ArrayList<String> courseList = new ArrayList<String>();

        courseList.add(getString(R.string.all_courses));

        for (AttCourse c : attcourses) {
            courseList.add(c.getCourse().getCoursename());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, courseList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReportPickerCourse.setAdapter(adp);
    }

    /**
     * Prepares the students list.
     */
    public void prepareStudentsList() {
        GetStudentsTask snst = new GetStudentsTask();
        snst.execute();
    }

    private void selectCourse(int pos) {
        coursepos = pos - 1;

        if (pos == 0) {
            //hide attcontrol spinner
            spnReportPickerAttControl.setVisibility(View.GONE);
            tvReportPickerAttControl.setVisibility(View.GONE);
            spnReportPickerAttControl.setSelection(0);
            attcontrolpos = -1;

            spnReportPickerGroup.setVisibility(View.GONE);
            tvReportPickerGroup.setVisibility(View.GONE);
            spnReportPickerGroup.setSelection(0);
            groupidpos = -1;
        }
        else {
            spnReportPickerAttControl.setVisibility(View.VISIBLE);
            tvReportPickerAttControl.setVisibility(View.VISIBLE);

            spnReportPickerGroup.setVisibility(View.VISIBLE);
            tvReportPickerGroup.setVisibility(View.VISIBLE);
            ArrayList<String> attcontrolList = new ArrayList<String>();

            attcontrolList.add(getString(R.string.all_attcontrols));

            for (AttControl c : attcourses.get(coursepos).getAttcontrols() ) {
                attcontrolList.add(c.getAcname());
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, attcontrolList);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnReportPickerAttControl.setAdapter(adp);
        }

        spnStudentChanged = true;
        spnReportPickerStudent.setSelection(0);
    }

    private void selectAttControl(int pos) {
        attcontrolpos = pos - 1;

        if (pos == 0) {
            spnReportPickerGroup.setVisibility(View.GONE);
            tvReportPickerGroup.setVisibility(View.GONE);
            spnReportPickerGroup.setSelection(0);
            groupidpos = -1;
        }
        else {
            ArrayList<String> groupList = new ArrayList<String>();

            AttControl selectedAttControl = attcourses.get(coursepos).getAttcontrols().get(attcontrolpos);

            if (selectedAttControl.getGroupmode() == Att.NOGROUPS) {
                spnReportPickerGroup.setVisibility(View.GONE);
                tvReportPickerGroup.setVisibility(View.GONE);
            } else {
                spnReportPickerGroup.setVisibility(View.VISIBLE);
                tvReportPickerGroup.setVisibility(View.VISIBLE);
            }

            groupList.add(getString(R.string.all_groups));

            for (Group g : selectedAttControl.getGroups()) {
                groupList.add(g.getGroupname());
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, groupList);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnReportPickerGroup.setAdapter(adp);
        }
        spnStudentChanged = true;
        spnReportPickerStudent.setSelection(0);
    }

    private void selectGroup(int pos) {
        groupidpos = pos - 1;

        spnStudentChanged = true;
        spnReportPickerStudent.setSelection(0);
    }

    private void selectStudent(int pos) {
        studentpos = pos-1;
    }

    private void prepareEmptyStudentList() {
        ArrayList<String> stList = new ArrayList<String>();

        stList.add(getString(R.string.all_students));

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReportPickerStudent.setAdapter(adp);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing.
    }


    /**
     * The interface for all the listeners of this dialog
     */
    public interface OnFragmentInteractionListener {
        /**
         * Fires when the positive button is clicked.
         *
         * @param course the selected course
         * @param attcontrol the selected attcontrol
         * @param group the selected group
         * @param user the user
         * @param startdate the start date
         * @param enddate the end date
         */
        public void onPositiveButtonClicked(int course, int attcontrol, int group, User user, Date startdate, Date enddate);
    }


    private class GetStudentsTask extends AsyncTask<Integer, Integer, ArrayList<User>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportPickerLoading).bringToFront();
            v.findViewById(R.id.pbReportPickerLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<User> result) {
            v.findViewById(R.id.pbReportPickerLoading).setVisibility(View.INVISIBLE);
            userList = result;

            ArrayList<String> stList = new ArrayList<String>();

            stList.add(getString(R.string.all_students));

            for (int i = 0; i< userList.size(); i++) {
                stList.add(userList.get(i).getFirstname()+" "+ userList.get(i).getLastname());
                if (userList.get(i).getId() == studentid) studentpos = i;
            }
            studentpos++;

            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stList);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnReportPickerStudent.setAdapter(adp);

            spnReportPickerStudent.setSelection(studentpos);
        }

        protected ArrayList<User> doInBackground(Integer... params) {
            ArrayList<User> result = null;
            try {
                int course = -1;
                int attcontrol = -1;
                int group = -1;

                if (coursepos != -1) {
                    course = attcourses.get(coursepos).getCourse().getCourseid();

                    if (attcontrolpos != -1) {
                        attcontrol = attcourses.get(coursepos).getAttcontrols().get(attcontrolpos).getAcid();

                        if (groupidpos != -1) {
                            group = attcourses.get(coursepos).getAttcontrols().get(attcontrolpos).getGroups().get(groupidpos).getGroupid();
                        }
                    }
                }

                result = RestUser.getStudents(course, attcontrol, group);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

}
