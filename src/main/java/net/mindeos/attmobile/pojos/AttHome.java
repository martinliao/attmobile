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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO representing an AttHome instance in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHome implements Parcelable {

    private int ahid;
    private String ahname;
    private int courseid;
    private String coursename;

    /**
     * Instantiates a new AttHome.
     *
     * @param ahid the atthome id
     * @param ahname the atthome name
     * @param courseid the course id
     * @param coursename the course name
     */
    public AttHome(int ahid, String ahname, int courseid, String coursename) {
        this.ahid = ahid;
        this.ahname = ahname;
        this.courseid = courseid;
        this.coursename = coursename;
    }

    /**
     * Gets the atthome id.
     *
     * @return the atthome id
     */
    public int getAhid() {
        return ahid;
    }

    /**
     * Sets atthome id.
     *
     * @param ahid the atthome id
     */
    public void setAhid(int ahid) {
        this.ahid = ahid;
    }

    /**
     * Gets the atthome name.
     *
     * @return the atthome name
     */
    public String getAhname() {
        return ahname;
    }

    /**
     * Sets the atthome name.
     *
     * @param ahname the atthome name
     */
    public void setAhname(String ahname) {
        this.ahname = ahname;
    }

    /**
     * Gets the course id.
     *
     * @return the course id
     */
    public int getCourseid() {
        return courseid;
    }

    /**
     * Sets the course id.
     *
     * @param courseid the course id
     */
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Gets the course name.
     *
     * @return the course name
     */
    public String getCoursename() {
        return coursename;
    }

    /**
     * Sets the course name.
     *
     * @param coursename the course name
     */
    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
