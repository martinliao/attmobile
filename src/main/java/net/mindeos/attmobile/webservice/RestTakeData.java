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

import net.mindeos.attmobile.app.Preferences;
import net.mindeos.attmobile.pojos.TakeData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to attendance take data.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestTakeData extends RestBase  {

    /**
     * Instantiates a new RestTakeData
     */
    public RestTakeData() {}

    /**
     * Gets the attendance take information from the session indicated.
     *
     * @param sessionid the session id
     * @return the information to take attendance from that session
     */
    public static ArrayList<TakeData> getTakeData (Integer sessionid) throws Exception {
        JSONArray results = AttControlCaller.call("get_session_take", "sessionid=" + sessionid);

        RestGeneralData.initialize();

        ArrayList<TakeData> takedata = new ArrayList<TakeData>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);

            int status = Preferences.NOSTATUS;
            int pic1 = Preferences.NOPICTURE;
            int pic2 = Preferences.NOPICTURE;
            String remarks = "";

            if (!row.getString("status").equals("null")) status = row.getInt("status");
            if (!row.getString("remarks").equals("null")) remarks = row.getString("remarks");

            int id = row.getInt("id");
            String username = row.getString("username");
            String firstname = row.getString("firstname");
            String lastname = row.getString("lastname");


            if (!row.getString("pic1").equals("null")) pic1 = row.getInt("pic1");
            if (!row.getString("pic2").equals("null")) pic2 = row.getInt("pic2");

            TakeData td = new TakeData(
                    id,
                    username,
                    firstname,
                    lastname,
                    status,
                    remarks,
                    pic1,
                    pic2);

            takedata.add(td);
        }

        return takedata;


    }

    /**
     * Save the attendance take data.
     *
     * @param sessionid the session id
     * @param takedata the information from taking attendance in this session
     */
    public static void saveTakeData (Integer sessionid, ArrayList<TakeData> takedata) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("sessionid=").append(sessionid);


        int i = 0;
        for (TakeData td : takedata) {
            sb.append("&takedata[").append(i).append("][userid]=").append(td.getId());
            sb.append("&takedata[").append(i).append("][statusid]=").append(td.getStatus());
            sb.append("&takedata[").append(i).append("][remarks]=").append(encode(td.getRemarks()));

            i++;
        }

        AttControlCaller.callNoResults("save_session_take", sb.toString());
    }
}
