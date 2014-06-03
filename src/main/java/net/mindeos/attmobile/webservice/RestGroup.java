package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.Group;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to groups.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestGroup extends RestBase {


    /**
     * Gets the groups in an AttHome context.
     *
     * @param athid the atthome id
     * @return the groups
     */
    public static ArrayList<Group> getGroups (int athid) throws Exception {
        JSONArray results = AttHomeCaller.call("get_groups", "athid=" + athid);

        RestGeneralData.initialize();

        ArrayList<Group> groups = new ArrayList<Group>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            groups.add(new Group(
                    row.getInt("groupid"),
                    row.getString("groupname")
            ));
        }

        return groups;
    }
}
