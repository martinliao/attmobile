package net.mindeos.attmobile.webservice;

import android.text.format.DateFormat;

import net.mindeos.attmobile.pojos.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to sessions.
 */
public class RestSession extends RestBase {

    /**
     * Instantiates a new RestSession object.
     */
    public RestSession(){}

    /**
     * Gets the sessions according to the dates indicated, taking into account the current user.
     *
     * @param inidate the date at the beginning of the period
     * @param enddate the date at the end of the period
     * @return the sessions accessible for this user in the period indicated.
     */
    public static ArrayList<Session> getSessions (Date inidate, Date enddate) throws Exception {
        DateFormat df = new DateFormat();

        JSONArray results = AttControlCaller.call("get_sessions", "inidate=" + df.format("yyyy-MM-dd", inidate), "enddate=" + df.format("yyyy-MM-dd", enddate));

        ArrayList<Session> sessions = new ArrayList<Session>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            Date lasttaken;

            if (row.getString("lasttaken").equals("null")) {
                lasttaken = null;
            }
            else {
                lasttaken = new Date(row.getLong("lasttaken") * 1000);
            }


            sessions.add(new Session(
                row.getInt("id"),
                row.getInt("cid"),
                row.getInt("courseid"),
                row.getString("name"),
                row.getString("coursename"),
                new Date(row.getLong("sessdate") * 1000),
                lasttaken,
                row.getString("description"),
                row.getInt("duration")
            ));
        }

        return sessions;
    }

    /**
     * Saves a newly created session.
     *
     * @param courseid the course id
     * @param acid the attendance control id
     * @param selgroup the group selected for this session
     * @param sessiontimestamp the timestamp for this session
     * @param duration the duration of this session
     * @param recurrencerule the recurrencerule for this session (if defined)
     * @param description the description of this session (if defined)
     */
    public static void saveNewSession(int courseid, int acid, int selgroup, Long sessiontimestamp, int duration, String recurrencerule, String description) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("courseid=").append(courseid);
        sb.append("&attid=").append(acid);
        if (selgroup != -100) sb.append("&group=").append(selgroup);
        sb.append("&timestamp=").append(sessiontimestamp);
        sb.append("&duration=").append(duration);
        sb.append("&recurrence=").append(encode(recurrencerule));
        sb.append("&description=").append(encode(description));

        AttControlCaller.callNoResults("save_new_session", sb.toString());
    }

    /**
     * Saves an edited session.
     *
     * @param sessionid the session id
     * @param sessiontimestamp the timestamp for this session
     * @param duration the duration of this session
     * @param description the description for this session
     */
    public static void saveEditSession(int sessionid, Long sessiontimestamp, int duration, String description) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("sessionid=").append(sessionid);
        sb.append("&timestamp=").append(sessiontimestamp);
        sb.append("&duration=").append(duration);
        sb.append("&description=").append(encode(description));

        AttControlCaller.callNoResults("save_edit_session", sb.toString());
    }

    /**
     * Deletes a session.
     *
     * @param sessionid the session id
     */
    public static void deleteSession (int sessionid) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("sessionid=").append(sessionid);

        AttControlCaller.callNoResults("delete_session", sb.toString());
    }
}
