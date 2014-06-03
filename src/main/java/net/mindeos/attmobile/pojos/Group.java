package net.mindeos.attmobile.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO representing a Class Group in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Group implements Parcelable {

    private int groupid;
    private String groupname;

    /**
     * Instantiates a new Group.
     *
     * @param groupid the group id
     * @param groupname the group name
     */
    public Group(int groupid, String groupname) {
        this.groupid = groupid;
        this.groupname = groupname;
    }

    /**
     * Gets group id.
     *
     * @return the groupid
     */
    public int getGroupid() {
        return groupid;
    }

    /**
     * Sets group id.
     *
     * @param groupid the groupid
     */
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    /**
     * Gets group name.
     *
     * @return the group name
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * Sets group name.
     *
     * @param groupname the group name
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
