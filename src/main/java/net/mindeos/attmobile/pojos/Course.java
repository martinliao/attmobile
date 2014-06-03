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
 * POJO representing a Course in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Course implements Parcelable {
    private int courseid;
    private String coursename;
    private String courseshortname;

    /**
     * Instantiates a new Course.
     *
     * @param courseid the course id
     * @param coursename the course name
     * @param courseshortname the course short name
     */
    public Course(int courseid, String coursename, String courseshortname) {
        this.courseid = courseid;
        this.coursename = coursename;
        this.courseshortname = courseshortname;
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

    /**
     * Gets the course short name.
     *
     * @return the course short name
     */
    public String getCourseshortname() {
        return courseshortname;
    }

    /**
     * Sets the course short name.
     *
     * @param courseshortname the course short name
     */
    public void setCourseshortname(String courseshortname) {
        this.courseshortname = courseshortname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
