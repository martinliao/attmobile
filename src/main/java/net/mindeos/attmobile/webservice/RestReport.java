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

package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.Report;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to reports.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestReport extends RestBase {

    /**
     * Instantiates a new RestReport.
     */
    public RestReport() {}

    /**
     * Gets the report in the AttControl context.
     *
     * @param courses the courses to take the
     * @param attcontrols the attcontrols
     * @param groups the groups
     * @param studentid the studentid
     * @param startdate the startdate
     * @param enddate the enddate
     * @return the report
     */
    public static ArrayList<Report> getReport (ArrayList<Integer> courses, ArrayList<Integer> attcontrols, ArrayList<Integer> groups, int studentid, Date startdate, Date enddate) throws Exception {

        RestGeneralData.initialize();

        StringBuilder params = new StringBuilder();

        params.append("studentid=").append(studentid);
        params.append("&startdate=").append(Att.edf.format(startdate));
        params.append("&enddate=").append(Att.edf.format(enddate));

        for(Integer c : courses) params.append("&courses[]=").append(c);
        for(Integer a : attcontrols) params.append("&attcontrols[]=").append(a);
        for(Integer g : groups) params.append("&groups[]=").append(g);

        JSONArray results = AttControlCaller.call("get_reports", params.toString());

        ArrayList<Report> report = new ArrayList<Report>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            String remarks = "";
            if (!row.getString("remarks").equals("null")) remarks = row.getString("remarks");

            report.add(new Report(
                    row.getInt("id"),
                    row.getInt("uid"),
                    row.getString("ufirstname"),
                    row.getString("ulastname"),
                    row.getInt("sid"),
                    new Date(row.getLong("sdate")*1000),
                    row.getString("cname"),
                    row.getString("acname"),
                    row.getInt("statusid"),
                    remarks
            ));
        }

        return report;

    }

    /**
     * Gets the report summary in the AttHome context.
     *
     * @param ahid the atthome id to take the information from
     * @param selcourse the course to take the information from
     * @param selgroup the group to take the information from
     * @param selstudent the student to take the information from
     * @param startdate the start of the period to take the information from
     * @param enddate the end of the period to take the information from
     * @return the report
     */
    public static ArrayList<Report> getReport(int ahid, int selcourse, int selgroup, int selstudent, Date startdate, Date enddate) throws Exception {
        RestGeneralData.initialize();

        StringBuilder params = new StringBuilder();

        params.append("athid=").append(ahid);
        params.append("&studentid=").append(selstudent);
        params.append("&courseid=").append(selcourse);
        params.append("&groupid=").append(selgroup);
        params.append("&startdate=").append(Att.edf.format(startdate));
        params.append("&enddate=").append(Att.edf.format(enddate));

        JSONArray results = AttHomeCaller.call("get_report_detail", params.toString());

        ArrayList<Report> report = new ArrayList<Report>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            String remarks = "";
            if (!row.getString("remarks").equals("null")) remarks = row.getString("remarks");

            report.add(new Report(
                    row.getInt("id"),
                    row.getInt("uid"),
                    row.getString("ufirstname"),
                    row.getString("ulastname"),
                    row.getInt("sid"),
                    new Date(row.getLong("sdate")*1000),
                    row.getString("cname"),
                    row.getString("acname"),
                    row.getInt("statusid"),
                    remarks
            ));
        }

        return report;

    }

    /**
     * Save report data.
     *
     * @param reports the reports
     * @throws Exception the exception
     */
    public static void saveReportData(ArrayList<Report> reports) throws Exception {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (Report r : reports) {
            sb.append("&reportdata[").append(i).append("][atlid]=").append(r.getId());
            sb.append("&reportdata[").append(i).append("][statusid]=").append(r.getStatusid());
            sb.append("&reportdata[").append(i).append("][remarks]=").append(encode(r.getRemarks()));

            i++;
        }

        AttControlCaller.callNoResults("save_report_data", sb.toString());
    }
}
