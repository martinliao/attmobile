package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.AttControl;
import net.mindeos.attmobile.pojos.AttCourse;
import net.mindeos.attmobile.pojos.Group;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to attcourses.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestAttCourse extends RestBase  {

    /**
     * Gets courses and the attendance instances associated to them.
     *
     * @return the attcourses list
     */
    public static ArrayList<AttCourse> getAttCourses() throws Exception {
        JSONArray results = AttControlCaller.call("get_attcourses");

        ArrayList<AttCourse> attcourses = new ArrayList<AttCourse>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject course = results.getJSONObject(i);

            AttCourse c = new AttCourse(
                    course.getInt("courseid"),
                    course.getString("coursename"),
                    course.getString("courseshortname")
            );

            JSONArray attcontrols = course.getJSONArray("attcontrols");

            for (int j = 0; j < attcontrols.length(); j++) {
                JSONObject attcontrol = attcontrols.getJSONObject(j);

                JSONArray groups = attcontrol.getJSONArray("groups");

                AttControl ac = new AttControl(
                        attcontrol.getInt("acid"),
                        attcontrol.getString("acname"),
                        attcontrol.getInt("groupmode")
                );

                for (int k = 0; k < groups.length(); k++) {
                    JSONObject group = groups.getJSONObject(k);

                    Group g = new Group(
                            group.getInt("groupid"),
                            group.getString("groupname")
                    );

                    ac.addGroup(g);
                }

                c.addAttControl(ac);
            }

            attcourses.add(c);
        }

        return attcourses;
    }
}
