package net.mindeos.attmobile.app;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import net.mindeos.attmobile.dialogs.AttHomeReportPicker;
import net.mindeos.attmobile.pojos.Course;
import net.mindeos.attmobile.pojos.Group;
import net.mindeos.attmobile.pojos.Report;
import net.mindeos.attmobile.webservice.RestCourse;
import net.mindeos.attmobile.webservice.RestGroup;
import net.mindeos.attmobile.webservice.RestReport;
import net.mindeos.attmobile.webservice.RestSummary;
import net.mindeos.attmobile.webservice.RestUser;
import net.mindeos.attmobile.pojos.Summary;
import net.mindeos.attmobile.pojos.User;

import java.util.ArrayList;
import java.util.Date;


/**
 * Fragment for the AttHomeReports activity
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeReportsFragment extends ReportsFragmentBase {

    private int ahid;
    private String ahname;
    private String ahcourse;
    private ArrayList<Course> courses;
    private ArrayList<Group> groups;
    private ArrayList<User> users;
    private int selcourse;
    private int selgroup;
    private int selstudent;

    private Course selectedcourse;
    private Group selectedgroup;

    /**
     * Instantiates a new AttHome reports fragment.
     */
    public AttHomeReportsFragment() {
        isatthome = true;
    }


    @Override
    protected void prepareFragmentData() {
        ahid = getActivity().getIntent().getExtras().getInt("ahid");
        ahname = getActivity().getIntent().getExtras().getString("ahname");
        ahcourse = getActivity().getIntent().getExtras().getString("ahcourse");

        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setSubtitle(ahcourse + " (" + ahname + ")");

        courses = new ArrayList<Course>();

        GetCoursesTask gct = new GetCoursesTask();
        gct.execute();

        GetGroupsTask ggt = new GetGroupsTask();
        ggt.execute();

        GetStudentsTask gst = new GetStudentsTask();
        gst.execute();
    }

    @Override
    protected void showReportsDialog(Context ctx) {
        Bundle b = new Bundle();
        b.putParcelableArrayList("courses", courses);
        b.putParcelableArrayList("groups", groups);
        b.putParcelableArrayList("users", users);
        b.putInt("selcourse", selcourse);
        b.putInt("selgroup", selgroup);
        b.putInt("selstudent", selstudent);

        if (startdate != null) b.putLong("startdate", startdate.getTime());
        else b.putLong("startdate", -1);

        if (enddate != null) b.putLong("enddate", enddate.getTime());
        else b.putLong("enddate", -1);

        AttHomeReportPicker rp = AttHomeReportPicker.newInstance();
        rp.setArguments(b);
        rp.show(getActivity().getSupportFragmentManager(), "AttHomeReportPicker");
    }

    @Override
    protected ArrayList<Summary> generateSummary() {
        ArrayList<Summary> result = null;
        try {
            result = RestSummary.getSummary(ahid, selcourse, selgroup, selstudent, startdate, enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected ArrayList<Report> generateReport() {
        ArrayList<Report> result = null;
        try {
            result = RestReport.getReport(ahid, selcourse, selgroup, selstudent, startdate, enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void refreshAfterSave() {
        GetReportSummaryTask gst = new GetReportSummaryTask();
        gst.execute();

        v.findViewById(R.id.pbReportLoading).setVisibility(View.INVISIBLE);
    }

    /**
     * Positive button clicked.
     *
     * @param selcourse the selected course
     * @param selgroup the selected group
     * @param selstudent the selected student
     * @param startdate the start date
     * @param enddate the end date
     */
    public void positiveClicked(int selcourse, int selgroup, int selstudent, Date startdate, Date enddate) {
        tvReportTitle.setTextColor(getActivity().getResources().getColor(R.color.red0));
        getActivity().findViewById(R.id.vReportTitleLine).setBackground(new ColorDrawable(getActivity().getResources().getColor(R.color.red0)));

        this.selcourse = selcourse;
        this.selgroup = selgroup;
        this.selstudent = selstudent;
        this.startdate = startdate;
        this.enddate = enddate;

        StringBuilder subtitle = new StringBuilder();

        selectedstudent = null;
        if (selstudent != -1) {
            for (User s : users) {
                if (s.getId() == selstudent) {
                    selectedstudent = s;
                    break;
                }
            }
        }
        else {
            tvReportTitle.setText(getString(R.string.commonreport));
        }

        selectedcourse = null;
        if (selcourse != -1) {
            if (courses.size()>0) {
                selectedcourse = courses.get(0);
                subtitle.append(selectedcourse.getCoursename());
            }
        }
        else {
            subtitle.append(getString(R.string.all_courses));

        }

        selectedgroup = null;
        if (selgroup > 0) {
            for (Group g : groups) {
                if (g.getGroupid() == selgroup) {
                    selectedgroup = g;
                    break;
                }
            }
            subtitle.append(" (").append(getString(R.string.all_students_from_group)).append(" ").append(selectedgroup.getGroupname()).append(")");
        }
        else if (selgroup == 0) {
            subtitle.append(" (").append(getString(R.string.all_groups)).append(")");
        }

        tvReportSubtitle.setText(subtitle.toString());

        lyReportsUserData.setVisibility(View.VISIBLE);
        lyReportsSummary.setVisibility(View.VISIBLE);

        tvReportNotLoaded.setVisibility(View.GONE);

        //Fill in form data.
        GetReportSummaryTask gst = new GetReportSummaryTask();
        gst.execute();

        GetReportTask grt = new GetReportTask();
        grt.execute();

        refreshInterface();
    }


    private class GetCoursesTask extends AsyncTask<Integer, Integer, ArrayList<Course>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<Course> result) {
            courses = result;
        }

        protected ArrayList<Course> doInBackground(Integer... params) {
            ArrayList<Course> result = null;
            try {
                result = RestCourse.getCourses(ahid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    private class GetGroupsTask extends AsyncTask<Integer, Integer, ArrayList<Group>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<Group> result) {
            groups = result;
        }

        protected ArrayList<Group> doInBackground(Integer... params) {
            ArrayList<Group> result = null;
            try {
                result = RestGroup.getGroups(ahid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    private class GetStudentsTask extends AsyncTask<Integer, Integer, ArrayList<User>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<User> result) {
            v.findViewById(R.id.pbReportLoading).setVisibility(View.INVISIBLE);
            users = result;

            AttControlBase act = (AttControlBase) getActivity();
            act.changeMenuItem(R.id.reports_settings, true);
        }

        protected ArrayList<User> doInBackground(Integer... params) {
            ArrayList<User> result = null;
            try {
                result = RestUser.getStudents(ahid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
