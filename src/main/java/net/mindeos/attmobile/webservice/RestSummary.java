package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.Summary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to report summaries.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestSummary extends RestBase {

    /**
     * Instantiates a new RestSummary.
     */
    public RestSummary() {}

    /**
     * Gets the report summary in the AttControl context.
     *
     * @param courses the courses to take the information from
     * @param attcontrols the attcontrols to take the information from
     * @param groups the groups to take the information from
     * @param studentid the student id to take the information from
     * @param startdate the start of the period to take the information from
     * @param enddate the end of the period to take the information from
     * @return the summary
     */
    public static ArrayList<Summary> getSummary (ArrayList<Integer> courses, ArrayList<Integer> attcontrols, ArrayList<Integer> groups, int studentid, Date startdate, Date enddate) throws Exception {

        RestGeneralData.initialize();

        StringBuilder params = new StringBuilder();

        params.append("studentid=").append(studentid);
        params.append("&startdate=").append(Att.edf.format(startdate));
        params.append("&enddate=").append(Att.edf.format(enddate));

        for(Integer c : courses) params.append("&courses[]=").append(c);
        for(Integer a : attcontrols) params.append("&attcontrols[]=").append(a);
        for(Integer g : groups) params.append("&groups[]=").append(g);

        JSONArray results = AttControlCaller.call("get_reportsummary", params.toString());

        ArrayList<Summary> report = new ArrayList<Summary>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            report.add(new Summary(
                    row.getInt("statusid"),
                    row.getInt("statuscount")
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
     * @return the summary
     */
    public static ArrayList<Summary> getSummary(int ahid, int selcourse, int selgroup, int selstudent, Date startdate, Date enddate) throws Exception {
        RestGeneralData.initialize();

        StringBuilder params = new StringBuilder();

        params.append("athid=").append(ahid);
        params.append("&studentid=").append(selstudent);
        params.append("&courseid=").append(selcourse);
        params.append("&groupid=").append(selgroup);
        params.append("&startdate=").append(Att.edf.format(startdate));
        params.append("&enddate=").append(Att.edf.format(enddate));

        JSONArray results = AttHomeCaller.call("get_report_summary", params.toString());

        ArrayList<Summary> report = new ArrayList<Summary>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            report.add(new Summary(
                    row.getInt("statusid"),
                    row.getInt("statuscount")
            ));
        }

        return report;
    }
}
