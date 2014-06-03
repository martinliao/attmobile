package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.AttHome;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to atthomes.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestAttHome extends RestBase {

    /**
     * Gets the atthomes available for this user.
     *
     * @return the atthome instances
     */
    public static ArrayList<AttHome> getAttHomes () throws Exception {

        JSONArray results = AttHomeCaller.call("get_atthomes");

        ArrayList<AttHome> atthomes = new ArrayList<AttHome>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            atthomes.add(new AttHome(
                    row.getInt("ahid"),
                    row.getString("ahname"),
                    row.getInt("courseid"),
                    row.getString("coursename")
            ));
        }

        return atthomes;
    }

}
