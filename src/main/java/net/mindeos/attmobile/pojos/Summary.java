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
 * POJO representing to show the report summary information
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Summary implements Parcelable {

    private int statusid;
    private int statuscount;

    /**
     * Instantiates a new Summary element.
     *
     * @param statusid the id of the status represented.
     * @param statuscount the number of elements with this status.
     */
    public Summary(int statusid, int statuscount) {
        this.statusid = statusid;
        this.statuscount = statuscount;
    }

    /**
     * Gets the status count.
     *
     * @return the status count.
     */
    public int getStatuscount() {
        return statuscount;
    }

    /**
     * Sets the status count.
     *
     * @param statuscount the status count.
     */
    public void setStatuscount(int statuscount) {
        this.statuscount = statuscount;
    }

    /**
     * Gets the status id.
     *
     * @return the status id.
     */
    public int getStatusid() {
        return statusid;
    }

    /**
     * Sets the status id.
     *
     * @param statusid the status id.
     */
    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
