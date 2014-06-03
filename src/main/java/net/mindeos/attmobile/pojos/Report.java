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
 * POJO representing a report item in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Report implements ReportBase, Parcelable {
    private int id;
    private int uid;
    private String ufirstname;
    private String ulastname;
    private int sid;
    private Date sdate;
    private String cname;
    private String acname;
    private int statusid;
    private String remarks;

    /**
     * Instantiates a new Report.
     *
     * @param id the log id
     * @param uid the user id
     * @param ufirstname the user's firstname
     * @param ulastname the user's lastname
     * @param sid the session id
     * @param sdate the session date
     * @param cname the course name
     * @param acname the attcontrol name
     * @param statusid the status id
     * @param remarks the remarks for this log
     */
    public Report(int id, int uid, String ufirstname, String ulastname, int sid, Date sdate, String cname, String acname, int statusid, String remarks) {
        this.id = id;
        this.uid = uid;
        this.ufirstname = ufirstname;
        this.ulastname = ulastname;
        this.sid = sid;
        this.sdate = sdate;
        this.cname = cname;
        this.acname = acname;
        this.statusid = statusid;
        this.remarks = remarks;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
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

    /**
     * Gets status id.
     *
     * @return the status id
     */
    public int getSid() {
        return sid;
    }

    /**
     * Sets status id.
     *
     * @param sid the status id
     */
    public void setSid(int sid) {
        this.sid = sid;
    }

    /**
     * Gets status date.
     *
     * @return the status date
     */
    public Date getSdate() {
        return sdate;
    }

    /**
     * Sets status date.
     *
     * @param sdate the status date
     */
    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCname() {
        return cname;
    }

    /**
     * Sets course name.
     *
     * @param cname the course name
     */
    public void setCname(String cname) {
        this.cname = cname;
    }

    /**
     * Gets attcontrol name.
     *
     * @return the attcontrol name
     */
    public String getAcname() {
        return acname;
    }

    /**
     * Sets attcontrol name.
     *
     * @param acname the attcontrol name
     */
    public void setAcname(String acname) {
        this.acname = acname;
    }

    /**
     * Gets status id.
     *
     * @return the status id
     */
    public int getStatusid() {
        return statusid;
    }

    /**
     * Sets status id.
     *
     * @param statusid the status id
     */
    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public boolean isHeader() {
        return false;
    }


    /**
     * Gets remarks.
     *
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks.
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
