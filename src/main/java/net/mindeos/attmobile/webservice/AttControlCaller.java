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

import org.json.JSONArray;

/**
 * Class to call the attcontrol webservice methods
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttControlCaller extends AttCaller {
    private static AttControlCaller instance = new AttControlCaller();

    private static final String FUNCTIONPREFIX = "mod_attcontrol_";

    private AttControlCaller() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AttControlCaller getInstance() {
        return AttControlCaller.instance;
    }


    /**
     * Calls a service returning a JSON array.
     *
     * @param wsfunction the function
     * @param args the arguments for this function
     * @return the result array
     */
    public static JSONArray call(String wsfunction, String... args)
            throws Exception {
        return serviceCall(Att.getSite(), Att.getAttControlToken(), FUNCTIONPREFIX + wsfunction, args);
    }


    /**
     * Calls a service with no results.
     *
     * @param wsfunction the function
     * @param args the arguments for this function
     */
    public static void callNoResults(String wsfunction, String... args)
            throws Exception {

        serviceCallNoResults(Att.getSite(), Att.getAttControlToken(), FUNCTIONPREFIX + wsfunction, args);
    }


}

