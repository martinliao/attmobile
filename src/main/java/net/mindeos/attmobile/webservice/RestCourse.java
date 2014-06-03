package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to courses.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestCourse extends RestBase {
    /**
     * Gets the courses associated to an atthome instance.
     *
     * @param athid the atthome id
     * @return the courses
     */
    public static ArrayList<Course> getCourses (int athid) throws Exception {
        JSONArray results = AttHomeCaller.call("get_courses", "athid=" + athid);

        RestGeneralData.initialize();

        ArrayList<Course> courses = new ArrayList<Course>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            courses.add(new Course(
                    row.getInt("courseid"),
                    row.getString("coursename"),
                    row.getString("courseshortname")
            ));
        }

        return courses;
    }
}
