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

package net.mindeos.attmobile.pojos;

/**
 * POJO representing a Report Header in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class ReportHeader implements ReportBase {
    private int uid;
    private String ufirstname;
    private String ulastname;


    /**
     * Instantiates a new Report header.
     *
     * @param uid the user id
     * @param ufirstname the user first name
     * @param ulastname the user last name
     */
    public ReportHeader(int uid, String ufirstname, String ulastname) {
        this.uid = uid;
        this.ufirstname = ufirstname;
        this.ulastname = ulastname;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets user id.
     *
     * @param uid the user id
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Gets user first name.
     *
     * @return the user first name
     */
    public String getUfirstname() {
        return ufirstname;
    }

    /**
     * Sets user first name.
     *
     * @param ufirstname the user first name
     */
    public void setUfirstname(String ufirstname) {
        this.ufirstname = ufirstname;
    }

    /**
     * Gets user last name.
     *
     * @return the user last name
     */
    public String getUlastname() {
        return ulastname;
    }

    /**
     * Sets user last name.
     *
     * @param ulastname the user last name
     */
    public void setUlastname(String ulastname) {
        this.ulastname = ulastname;
    }

    @Override
    public boolean isHeader() {
        return true;
    }
}
