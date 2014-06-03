package net.mindeos.attmobile.pojos;

import java.util.ArrayList;

/**
 * POJO representing an AttControl instancew in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttControl {


    private int acid;
    private String acname;
    private int groupmode;
    private ArrayList<Group> groups;

    /**
     * Instantiates a new AttControl.
     *
     * @param acid the AttConrol id
     * @param acname the AttControl name
     * @param groupmode the group mode of this AttControl
     */
    public AttControl(int acid, String acname, int groupmode) {
        this.acid = acid;
        this.acname = acname;
        this.groupmode = groupmode;
        this.groups = new ArrayList<Group>();
    }

    /**
     * Gets the AttConrol id.
     *
     * @return the AttConrol id
     */
    public int getAcid() {
        return acid;
    }

    /**
     * Sets the AttConrol id.
     *
     * @param acid the AttConrol id
     */
    public void setAcid(int acid) {
        this.acid = acid;
    }

    /**
     * Gets the AttControl name.
     *
     * @return the AttControl name
     */
    public String getAcname() {
        return acname;
    }

    /**
     * Sets the AttControl name.
     *
     * @param acname the AttControl name
     */
    public void setAcname(String acname) {
        this.acname = acname;
    }

    /**
     * Gets the AttControl group mode.
     *
     * @return the AttControl group mode
     */
    public int getGroupmode() {
        return groupmode;
    }

    /**
     * Sets the AttControl group mode.
     *
     * @param groupmode the AttControl group mode
     */
    public void setGroupmode(int groupmode) {
        this.groupmode = groupmode;
    }

    /**
     * Gets the groups associated to this AttControl.
     *
     * @return the groups associated to this AttControl instance
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     * Sets the groups associated to this AttControl.
     *
     * @param groups the groups associated to this AttControl
     */
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    /**
     * Adds a group to the AttControl instance.
     *
     * @param g the group to be added
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

}
