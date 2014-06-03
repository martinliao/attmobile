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

import java.util.Date;

/**
 * POJO representing to attendance status
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Session implements Parcelable {

    private int id;
    private int attcontrolid;
    private int courseid;
    private String attcontrolname;
    private String coursename;
    private Date sessdate;
    private Date lasttaken;
    private String description;
    private int duration;

    /**
     * Instantiates a new Session.
     *
     * @param id the session id
     * @param attcontrolid the AttControl id
     * @param courseid the course id
     * @param attcontrolname the AttControl attcontrolname
     * @param coursename the course attcontrolname
     * @param sessdate the session date
     * @param lasttaken the date when this session was last taken
     * @param description the description of this session
     * @param duration the duration of this session
     */
    public Session(int id, int attcontrolid, int courseid, String attcontrolname, String coursename, Date sessdate, Date lasttaken, String description, int duration) {
        this.id = id;
        this.attcontrolid = attcontrolid;
        this.courseid = courseid;
        this.attcontrolname = attcontrolname;
        this.coursename = coursename;
        this.sessdate = sessdate;
        this.lasttaken = lasttaken;
        this.description = description;
        this.duration = duration;
    }

    /**
     * Gets the session id.
     *
     * @return the session id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the session id.
     *
     * @param id the session id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets session's AttControl id.
     *
     * @return the session's AttControl id
     */
    public int getAttcontrolid() {
        return attcontrolid;
    }

    /**
     * Sets session's AttControl id.
     *
     * @param attcontrolid the session's AttControl id
     */
    public void setAttcontrolid(int attcontrolid) {
        this.attcontrolid = attcontrolid;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public int getCourseid() {
        return courseid;
    }

    /**
     * Sets course id.
     *
     * @param courseid the course id
     */
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Gets AttControl name.
     *
     * @return the AttControl name
     */
    public String getAttcontrolname() {
        return attcontrolname;
    }

    /**
     * Sets AttControl name.
     *
     * @param attcontrolname the AttControl name
     */
    public void setAttcontrolname(String attcontrolname) {
        this.attcontrolname = attcontrolname;
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
     * Gets the session date.
     *
     * @return the session date
     */
    public Date getSessdate() {
        return sessdate;
    }

    /**
     * Sets the session date.
     *
     * @param sessdate the session date
     */
    public void setSessdate(Date sessdate) {
        this.sessdate = sessdate;
    }

    /**
     * Gets when this session was last taken.
     *
     * @return when this session was last taken
     */
    public Date getLasttaken() {
        return lasttaken;
    }

    /**
     * Sets when this session was last taken
     *
     * @param lasttaken when this session was last taken
     */
    public void setLasttaken(Date lasttaken) {
        this.lasttaken = lasttaken;
    }

    /**
     * Gets this session's duration.
     *
     * @return the session duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the session duration.
     *
     * @param duration the session duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the session's description.
     *
     * @return the session description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the session's description.
     *
     * @param description the session's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(attcontrolid);
        parcel.writeInt(courseid);
        parcel.writeString(attcontrolname);
        parcel.writeString(coursename);
        if (sessdate != null) parcel.writeString(sessdate.toString());
        if (lasttaken != null) parcel.writeString(lasttaken.toString());
        parcel.writeString(description);
        parcel.writeString(duration+"");
    }
}
