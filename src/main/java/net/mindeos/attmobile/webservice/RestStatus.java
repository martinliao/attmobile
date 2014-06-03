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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Bridge class between the webservice manager and the POJO's
 * for the functions related to attendance statuses.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class RestStatus extends RestBase {

    /**
     * Instantiates a new RestStatus.
     */
    public RestStatus(){}

    /**
     * Gets the attendance statuses.
     *
     * @return the statuses
     */
    public static ArrayList<Status> getStatuses() throws Exception {

        JSONArray results = AttControlCaller.call("get_attendance_statuses");
        ArrayList<Status> statuses = new ArrayList<Status>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);


            statuses.add(new Status(
                    row.getInt("id"),
                    row.getString("acronym"),
                    row.getString("description")
            ));
        }

        return statuses;

    }
}
