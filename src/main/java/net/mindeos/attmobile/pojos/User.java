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
 * POJO representing a User in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class User implements Parcelable {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private Integer pic1;
    private Integer pic2;

    /**
     * Instantiates a new User.
     *
     * @param id the user id
     * @param username the username
     * @param firstname the user's firstname
     * @param lastname the user's lastname
     * @param pic1 the user's 1st pic
     * @param pic2 the user's 2nd pic
     */
    public User(int id, String username, String firstname, String lastname, Integer pic1, Integer pic2) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pic1 = pic1;
        this.pic2 = pic2;
    }

    /**
     * Gets the user id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user id.
     *
     * @param id the id
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
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the user's firstname.
     *
     * @param firstname the user's firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets the user's lastname.
     *
     * @return the user's lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the user's lastname.
     *
     * @param lastname the user's lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets the user's 1st pic.
     *
     * @return the user's pic 1
     */
    public Integer getPic1() {
        return pic1;
    }

    /**
     * Sets the user's 1st pic.
     *
     * @param pic1 the user's pic 1
     */
    public void setPic1(Integer pic1) {
        this.pic1 = pic1;
    }

    /**
     * Gets the user's 2nd pic.
     *
     * @return the user's pic 2
     */
    public Integer getPic2() {
        return pic2;
    }

    /**
     * Sets the user's 2nd pic.
     *
     * @param pic2 the user's pic 2
     */
    public void setPic2(Integer pic2) {
        this.pic2 = pic2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
