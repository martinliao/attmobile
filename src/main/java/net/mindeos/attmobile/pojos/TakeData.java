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
 * POJO representing the information needed to Take Attendance in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class TakeData implements Parcelable {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private Integer status;
    private String remarks;
    private Integer pic1;
    private Integer pic2;

    /**
     * Instantiates a new TakeData object.
     *
     * @param id the id of this take attendance log
     * @param username the user's name
     * @param firstname the user's firstname
     * @param lastname the user'slastname
     * @param status the attendance status
     * @param remarks the remarks assigned to this log (if needed)
     * @param pic1 the user's 1st pic
     * @param pic2 the user's 2nd pic
     */
    public TakeData(int id, String username, String firstname, String lastname, Integer status, String remarks, Integer pic1, Integer pic2) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.remarks = remarks;
        this.pic1 = pic1;
        this.pic2 = pic2;
    }

    /**
     * Gets the attcontrol log id.
     *
     * @return the log id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets attcontrol log id.
     *
     * @param id the attcontrol log id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's firstname.
     *
     * @return the the user's firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets  the user'sfirstname.
     *
     * @param firstname the the user's firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets the user's lastname.
     *
     * @return the the user's lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the user's lastname.
     *
     * @param lastname the the user's lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets the user's status.
     *
     * @return the the user's status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the user's status.
     *
     * @param status the the user's status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets the attcontrol log remarks.
     *
     * @return the the user's remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the attcontrol log remarks.
     *
     * @param remarks the remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the user's 2nd pic.
     *
     * @return the user's 2nd pic.
     */
    public Integer getPic2() {
        return pic2;
    }

    /**
     * Sets the user's 2nd pic.
     *
     * @param pic2 the user's 2nd pic.
     */
    public void setPic2(Integer pic2) {
        this.pic2 = pic2;
    }

    /**
     * Gets the user's 1st pic.
     *
     * @return the user's the 1st pic.
     */
    public Integer getPic1() {
        return pic1;
    }

    /**
     * Sets the user's 1st pic.
     *
     * @param pic1 the user's 1st pic
     */
    public void setPic1(Integer pic1) {
        this.pic1 = pic1;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeInt(status);
        parcel.writeString(remarks);
        parcel.writeInt(pic1);
        parcel.writeInt(pic2);
    }
}
