package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.app.Preferences;
import net.mindeos.attmobile.pojos.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to users.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestUser extends RestBase  {

    /**
     * Instantiates a new RestUser instance.
     */
    public RestUser() {}

    /**
     * Gets the list of students for this course, attcontrol and group.
     *
     * @param courseid the course id
     * @param attcontrolid the attcontrol id
     * @param groupid the group id
     * @return the students according to this criteria
     */
    public static ArrayList<User> getStudents (int courseid, int attcontrolid, int groupid) throws Exception {
        JSONArray results = AttControlCaller.call("get_students", "courseid=" + courseid, "attcontrolid=" + attcontrolid, "groupid=" + groupid);

        RestGeneralData.initialize();

        ArrayList<User> users = new ArrayList<User>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            int pic1;
            if (!row.getString("pic1").equals("null")) pic1 = row.getInt("pic1");
            else pic1 = Preferences.NOPICTURE;

            users.add(new User(
                    row.getInt("id"),
                    row.getString("username"),
                    row.getString("firstname"),
                    row.getString("lastname"),
                    pic1,
                    row.getInt("pic2")
            ));
        }

        return users;

    }

    /**
     * Gets the students according to this atthome instance.
     *
     * @param athid the atthome id
     * @return the students according to this criteria
     */
    public static ArrayList<User> getStudents (int athid) throws Exception {
        JSONArray results = AttHomeCaller.call("get_students", "athid=" + athid);

        RestGeneralData.initialize();

        ArrayList<User> users = new ArrayList<User>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            users.add(new User(
                    row.getInt("id"),
                    row.getString("username"),
                    row.getString("firstname"),
                    row.getString("lastname"),
                    row.getInt("pic1"),
                    row.getInt("pic2")
            ));
        }

        return users;

    }

}
