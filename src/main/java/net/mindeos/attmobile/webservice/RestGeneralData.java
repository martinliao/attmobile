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

import net.mindeos.attmobile.pojos.Status;
import net.mindeos.attmobile.pojos.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to general data of the app.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestGeneralData extends RestBase {

    private RestGeneralData(){}

    /**
     * Initialize all the general data of the app.
     */
    public static void initialize() throws Exception {
        if (Att.getUser() == null) {
            prepareUser();
        }

        if (Att.getStatuses() == null && (Att.hasAttControl() || Att.hasAttHome())) {
            prepareStatuses();
        }
    }

    private static void prepareUser() throws Exception {
        JSONArray results = AttControlCaller.call("get_user", "");

        if (results.length()>0) {
            JSONObject row = results.getJSONObject(0);

            int pic1 = 0;
            if (!row.getString("pic1").equals("null")) pic1 = row.getInt("pic1");

            User user = new User(
                    row.getInt("id"),
                    row.getString("username"),
                    row.getString("firstname"),
                    row.getString("lastname"),
                    pic1,
                    row.getInt("pic2")
            );

            int attcontrols = row.getInt("hasattcontrols");
            int atthomes = row.getInt("hasatthomes");

            Att.setAttControl(attcontrols > 0);
            Att.setAttHome(atthomes > 0);

            Att.setUser(user);
            return;
        }
    }

    private static void prepareStatuses() throws Exception {
        JSONArray results = AttControlCaller.call("get_attendance_statuses");
        ArrayList<Status> statuses = new ArrayList<Status>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);


            statuses.add(new Status(
                    row.getInt("id"),
                    row.getString("acronym"),
                    row.getString("description")
            ));

            Att.setStatuses(statuses);
        }
    }

}
