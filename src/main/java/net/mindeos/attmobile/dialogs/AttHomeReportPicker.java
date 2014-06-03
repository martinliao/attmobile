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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import net.mindeos.attmobile.app.R;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.pojos.Course;
import net.mindeos.attmobile.pojos.Group;
import net.mindeos.attmobile.pojos.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Dialog to pick the preferences for the AttHome Report
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeReportPicker extends DialogFragment implements CalendarDatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    /**
     * The courses available
     */
    ArrayList<Course> courses;
    /**
     * The groups available.
     */
    ArrayList<Group> groups;
    /**
     * The users available.
     */
    ArrayList<User> users;

    /**
     * The selected course.
     */
    int selcourse;
    /**
     * The selected group.
     */
    int selgroup;
    /**
     * The selected student.
     */
    int selstudent;

    private Date startdate;
    private Date enddate;

    private View v;

    private Spinner spnAthReportPickerStudent;
    private Spinner spnAthReportPickerCourse;

    private TextView tvAthReportPickerStartDate;
    private TextView tvAthReportPickerEndDate;

    /**
     * Creates a new instance of this dialog
     *
     * @return the atthome report picker dialog
     */
    public static AttHomeReportPicker newInstance() {
        AttHomeReportPicker fragment = new AttHomeReportPicker();
        return fragment;
    }

    /**
     * Creates a new instance of this dialog
     */
    public AttHomeReportPicker() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        courses = getArguments().getParcelableArrayList("courses");
        groups = getArguments().getParcelableArrayList("groups");
        users = getArguments().getParcelableArrayList("users");

        selcourse = getArguments().getInt("selcourse");
        selgroup = getArguments().getInt("selgroup");
        selstudent = getArguments().getInt("selstudent");

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
        v = inflater.inflate(R.layout.dialog_atthomereportpicker, null);


        tvAthReportPickerStartDate = (TextView) v.findViewById(R.id.tvAthReportPickerStartDate);
        tvAthReportPickerEndDate = (TextView) v.findViewById(R.id.tvAthReportPickerEndDate);

        spnAthReportPickerStudent = (Spinner) v.findViewById(R.id.spnAthReportPickerStudent);
        spnAthReportPickerCourse = (Spinner) v.findViewById(R.id.spnAthReportPickerCourse);


        tvAthReportPickerStartDate.setText(Att.sdf.format(startdate));
        tvAthReportPickerEndDate.setText(Att.sdf.format(enddate));

        tvAthReportPickerStartDate.setOnClickListener(this);
        tvAthReportPickerEndDate.setOnClickListener(this);

        prepareStudentsList();
        prepareCourseList();

        prepareSelections();

        spnAthReportPickerStudent.setOnItemSelectedListener(this);
        spnAthReportPickerCourse.setOnItemSelectedListener(this);

        CustomDialogBuilder builder = new CustomDialogBuilder(getActivity());

        builder.setView(v);
        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onPositiveButtonClicked(selcourse, selgroup, selstudent, startdate, enddate);
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
        if (selcourse == -1) spnAthReportPickerCourse.setSelection(0);
        else {
            for (int i=0; i<courses.size(); i++) {
                Course c = courses.get(i);

                if (c.getCourseid() == selcourse) {
                    spnAthReportPickerCourse.setSelection(i+1);
                    break;
                }
            }
        }

        if (selgroup == 0) spnAthReportPickerStudent.setSelection(0);
        else if (selgroup > 0) {
            for (int i=0; i<groups.size(); i++) {
                Group g = groups.get(i);

                if (g.getGroupid() == selgroup) {
                    spnAthReportPickerStudent.setSelection(i+1);
                    break;
                }
            }
        }
        else {
            for (int i=0; i< users.size(); i++) {
                User s = users.get(i);

                if (s.getId() == selstudent) {
                    spnAthReportPickerStudent.setSelection(i+groups.size()+1);
                    break;
                }
            }
        }
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
        if(parent.getId()==spnAthReportPickerCourse.getId()) {
            selectCourse(position);
        }
        else if (parent.getId()==spnAthReportPickerStudent.getId()) {
            selectStudent(position);
        }
    }


    private void selectDate(TextView v) {
        try {
            Date d = Att.sdf.parse(v.getText().toString());
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            CalendarDatePickerDialog cal = CalendarDatePickerDialog.newInstance(AttHomeReportPicker.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
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
            Date d1 = Att.sdf.parse(tvAthReportPickerStartDate.getText().toString());
            Date d2 = Att.sdf.parse(tvAthReportPickerEndDate.getText().toString());

            if (d2.getTime() < d1.getTime()) {
                tvAthReportPickerStartDate.setText(Att.sdf.format(d2));
                tvAthReportPickerEndDate.setText(Att.sdf.format(d1));

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
        if(t.getId()==tvAthReportPickerStartDate.getId())
        {
            selectDate((TextView)t);
        }
        else if(t.getId()==tvAthReportPickerEndDate.getId())
        {
            selectDate((TextView)t);
        }
    }

    /**
     * Prepare course list.
     */
    public void prepareCourseList() {
        //Put the course information in the dropdownlist

        ArrayList<String> courseList = new ArrayList<String>();

        courseList.add(getString(R.string.all_courses));

        for (Course c : courses) {
            courseList.add(c.getCoursename());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, courseList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAthReportPickerCourse.setAdapter(adp);
    }

    /**
     * Prepares the students list.
     */
    public void prepareStudentsList() {
        ArrayList<String> studentList = new ArrayList<String>();

        studentList.add(getString(R.string.all_students));

        for (Group g : groups) {
            studentList.add(getString(R.string.all_students_from_group)+" \""+g.getGroupname()+"\"");
        }

        for (User s : users) {
            studentList.add(s.getFirstname() + " " + s.getLastname());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, studentList);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAthReportPickerStudent.setAdapter(adp);
    }

    private void selectCourse(int pos) {
        if (pos > 0) selcourse = courses.get(pos-1).getCourseid();
        else selcourse = -1;
    }

    private void selectStudent(int pos) {
       if (pos == 0) {
           selstudent = -1;
           selgroup = 0;
       }
       else if (pos > 0 && pos <= groups.size()) {
           selstudent = -1;
           selgroup = groups.get(pos-1).getGroupid();
       }
       else {
           selstudent = users.get(pos - 1 - groups.size()).getId();
           selgroup = -1;
       }
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
         * @param selcourse the selected course
         * @param selgroup the selected group
         * @param selstudent the selected student
         * @param startdate the start date of this report
         * @param enddate the end date of this report
         */
        public void onPositiveButtonClicked(int selcourse, int selgroup, int selstudent, Date startdate, Date enddate);
    }

}
