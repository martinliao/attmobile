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

import java.io.Serializable;
import java.net.URLEncoder;


/**
 * The base class for all the REST bridges in the project
 */
public class RestBase implements Serializable {

    private static final String MOODLE_ENCODING = "UTF-8";

    /**
     * Encode a string to use it in the webservice.
     *
     * @param s the string to encode
     * @return the encoded string
     */
    public static String encode (String s) {
        if (s != null && s.length()>0) {
            try {
                return URLEncoder.encode(s, MOODLE_ENCODING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
