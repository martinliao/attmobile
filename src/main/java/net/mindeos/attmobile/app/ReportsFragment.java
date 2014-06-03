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


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import net.mindeos.attmobile.dialogs.AttControlReportPicker;
import net.mindeos.attmobile.pojos.AttControl;
import net.mindeos.attmobile.pojos.AttCourse;
import net.mindeos.attmobile.pojos.Group;
import net.mindeos.attmobile.pojos.Report;
import net.mindeos.attmobile.webservice.RestAttCourse;
import net.mindeos.attmobile.webservice.RestReport;
import net.mindeos.attmobile.webservice.RestSummary;
import net.mindeos.attmobile.pojos.Summary;
import net.mindeos.attmobile.pojos.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment for the AttControl Reports activity.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class ReportsFragment extends ReportsFragmentBase {

    private ArrayList<AttCourse> attcourses;

    private int coursepos = -1;
    private int attcontrolpos = -1;
    private int groupidpos = -1;
    private int studentid = -1;

    @Override
    protected void refreshInterface() {
        super.refreshInterface();

        //Particularities of this type of fragment.
        StringBuilder subtitle = new StringBuilder();

        if (coursepos == -1) {
            subtitle.append(getString(R.string.all_courses));
        }
        else {
            subtitle.append(attcourses.get(coursepos).getCourse().getCoursename());
            subtitle.append(" (");
            if (attcontrolpos == -1) {
                subtitle.append(getString(R.string.all_attcontrols));
            }
            else {
                subtitle.append(attcourses.get(coursepos).getAttcontrols().get(attcontrolpos).getAcname()).append(": ");

                if (groupidpos == -1) {
                    subtitle.append(getString(R.string.all_groups));
                }
                else {
                    subtitle.append(attcourses.get(coursepos).getAttcontrols().get(attcontrolpos).getGroups().get(groupidpos).getGroupname());
                }
            }
            subtitle.append(")");
        }

        tvReportSubtitle.setText(subtitle.toString());
    }

    @Override
    protected void prepareFragmentData() {
        GetAttCoursesTask gst = new GetAttCoursesTask();
        gst.execute();
    }

    @Override
    protected void showReportsDialog(Context ctx) {
        //Prepare dialog data
        Bundle b = new Bundle();
        b.putParcelableArrayList("courses", attcourses);
        b.putInt("coursepos", coursepos);
        b.putInt("attcontrolpos", attcontrolpos);
        b.putInt("groupidpos", groupidpos);
        b.putInt("studentid", studentid);

        if (startdate != null) b.putLong("startdate", startdate.getTime());
        else b.putLong("startdate", -1);

        if (enddate != null) b.putLong("enddate", enddate.getTime());
        else b.putLong("enddate", -1);

        AttControlReportPicker rp = AttControlReportPicker.newInstance();
        rp.setArguments(b);
        rp.show(getActivity().getSupportFragmentManager(), "AttControlReportPicker");
    }

    /**
     * Positive button clicked.
     *
     * @param course the selected course
     * @param attcontrol the selected attcontrol
     * @param grouppos the selected group
     * @param user the selected user
     * @param startDate the start date
     * @param endDate the end date
     */
    public void positiveClicked(int course, int attcontrol, int grouppos, User user, Date startDate, Date endDate) {
        this.coursepos = course;
        this.attcontrolpos = attcontrol;
        this.groupidpos = grouppos;

        this.startdate = startDate;
        this.enddate = endDate;

        this.selectedstudent = user;
        if (user != null) {
            this.studentid = user.getId();
        }
        else {
            this.studentid = -1;
            tvReportTitle.setText(getString(R.string.commonreport));
        }

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

    @Override
    protected ArrayList<Report> generateReport() {
        ArrayList<Report> result = null;
        try {
            //Prepare parameters for query
            ArrayList<Integer> courses = new ArrayList<Integer>();
            ArrayList<Integer> attcontrols = new ArrayList<Integer>();
            ArrayList<Integer> groups = new ArrayList<Integer>();
            int studentid = -1;

            if (selectedstudent != null) {
                studentid = selectedstudent.getId();
            }


            if (coursepos == -1) {
                for(AttCourse c : attcourses) courses.add(c.getCourse().getCourseid());
            }
            else {
                AttCourse currentCourse = attcourses.get(coursepos);
                courses.add(currentCourse.getCourse().getCourseid());

                if (attcontrolpos == -1) {
                    for(AttControl a : currentCourse.getAttcontrols()) attcontrols.add(a.getAcid());
                }
                else {
                    AttControl currentAttControl = currentCourse.getAttcontrols().get(attcontrolpos);
                    attcontrols.add(currentAttControl.getAcid());

                    if (groupidpos == -1) {
                        for(Group g : currentAttControl.getGroups()) groups.add(g.getGroupid());
                    }
                    else {
                        groups.add(currentAttControl.getGroups().get(groupidpos).getGroupid());
                    }
                }
            }
            result = RestReport.getReport(courses, attcontrols, groups, studentid, startdate, enddate);
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

    @Override
    protected ArrayList<Summary> generateSummary() {
        ArrayList<Summary> result = null;
        try {
            //Prepare parameters for query
            ArrayList<Integer> courses = new ArrayList<Integer>();
            ArrayList<Integer> attcontrols = new ArrayList<Integer>();
            ArrayList<Integer> groups = new ArrayList<Integer>();
            int studentid = -1;

            if (selectedstudent != null) {
                studentid = selectedstudent.getId();
            }


            if (coursepos == -1) {
                for(AttCourse c : attcourses) courses.add(c.getCourse().getCourseid());
            }
            else {
                AttCourse currentCourse = attcourses.get(coursepos);
                courses.add(currentCourse.getCourse().getCourseid());

                if (attcontrolpos == -1) {
                    for(AttControl a : currentCourse.getAttcontrols()) attcontrols.add(a.getAcid());
                }
                else {
                    AttControl currentAttControl = currentCourse.getAttcontrols().get(attcontrolpos);
                    attcontrols.add(currentAttControl.getAcid());

                    if (groupidpos == -1) {
                        for(Group g : currentAttControl.getGroups()) groups.add(g.getGroupid());
                    }
                    else {
                        groups.add(currentAttControl.getGroups().get(groupidpos).getGroupid());
                    }
                }
            }
            result = RestSummary.getSummary(courses, attcontrols, groups, studentid, startdate, enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private class GetAttCoursesTask extends AsyncTask<Integer, Integer, ArrayList<AttCourse>> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbReportLoading).bringToFront();
            v.findViewById(R.id.pbReportLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(ArrayList<AttCourse> result) {
            v.findViewById(R.id.pbReportLoading).setVisibility(View.INVISIBLE);
            attcourses = result;

            AttControlBase act = (AttControlBase) getActivity();
            act.changeMenuItem(R.id.reports_settings, true);
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
}