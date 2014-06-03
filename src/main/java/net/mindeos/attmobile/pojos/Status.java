package net.mindeos.attmobile.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO representing an attendance status
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class Status implements Parcelable {

    private int id;
    private String acronym;
    private String description;

    /**
     * Instantiates a new Status.
     *
     * @param id the id of this status.
     * @param acronym the acronym for this status.
     * @param description the description of this status.
     */
    public Status(int id, String acronym, String description) {
        this.id = id;
        this.acronym = acronym;
        this.description = description;
    }

    /**
     * Gets the status id.
     *
     * @return the id of this status
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the status id.
     *
     * @param id the id of this status
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the status acronym.
     *
     * @return the acronym of this status
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * Sets the acronym of this status.
     *
     * @param acronym the acronym of this status
     */
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
     * Gets the description of this status.
     *
     * @return the description of this status
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of this status.
     *
     * @param description the description of this status
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
        parcel.writeInt(id);
        parcel.writeString(acronym);
        parcel.writeString(description);
    }
}
