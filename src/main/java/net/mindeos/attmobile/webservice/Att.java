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

package net.mindeos.attmobile.webservice;

import net.mindeos.attmobile.pojos.Status;
import net.mindeos.attmobile.pojos.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Base class for all the common fields in the program
 * @author José Luis Antúnez Reales <jantunez@xtec.cat>
 */
public class Att {
    /**
     * The constant for the group mode "no groups".
     */
    public static final int NOGROUPS = 0;
    /**
     * The constant for the group mode "separate groups"
     */
    public static final int SEPARATEGROUPS = 1;
    /**
     * The constant for the group mode "visible groups"
     */
    public static final int VISIBLEGROUPS = 2;
    /**
     * The constant for the group mode "common group".
     */
    public static final int COMMONGROUP = -10;
    /**
     * The constant for the connection timeout.
     */
    public static final int CONNECTIONTIMEOUT = 30 * 1000;
    /**
     * The constant for the base date format.
     */
    public static final String dateformat = "dd/MM/yyyy";
    /**
     * The constant for the short date format.
     */
    public static final String shortdateformat = "dd/MM";
    /**
     * The constant for the date format for exporting.
     */
    public static final String exportdateformat = "yyyy-MM-dd";
    /**
     * The constant for the time format (only hour and minute).
     */
    public static final String hourformat = "kk:mm";
    /**
     * The constant for the date and time format.
     */
    public static final String datehourformat = "dd/MM/yyyy kk:mm";
    /**
     * The constant for the base date format as a SimpleDateFromat.
     */
    public static final SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
    /**
     * The constant for the exporting date format as a SimpleDateFromat.
     */
    public static final SimpleDateFormat edf = new SimpleDateFormat(exportdateformat);
    /**
     * The constant for the short date format as a SimpleDateFromat.
     */
    public static final SimpleDateFormat shdf = new SimpleDateFormat(shortdateformat);
    /**
     * The constant for the time format as a SimpleDateFromat.
     */
    public static final SimpleDateFormat shf = new SimpleDateFormat(hourformat);
    /**
     * The constant for the date and time format as a SimpleDateFromat.
     */
    public static final SimpleDateFormat sdhf = new SimpleDateFormat(datehourformat);

    private static String site = "";
    private static boolean attControl = false;
    private static String attControlToken = "";
    private static boolean attHome = false;
    private static String attHomeToken = "";

    private static ArrayList<Status> statuses = null;
    private static User user = null;

    private static ArrayList<Integer> colors = null;

    /**
     * Gets all the statuses available in the remote system.
     *
     * @return the statuses available in the remote system
     */
    public static ArrayList<Status> getStatuses() {
        return statuses;
    }

    /**
     * Sets the statuses available in the remote system.
     *
     * @param statuses the statuses available in the remote system
     */
    public static void setStatuses(ArrayList<Status> statuses) {
        Att.statuses = statuses;
    }

    /**
     * Gets the current user.
     *
     * @return the current user
     */
    public static User getUser() {
        return user;
    }

    /**
     * Sets the current user.
     *
     * @param user the current user
     */
    public static void setUser(User user) {
        Att.user = user;
    }

    /**
     * Gets the current user id.
     *
     * @return the current user id
     */
    public static int getUserId() {
        return user.getId();
    }

    /**
     * Gets the current user name.
     *
     * @return the current user name
     */
    public static String getUsername() {
        return user.getUsername();
    }

    /**
     * Gets the current user's first name.
     *
     * @return the current user's firstname
     */
    public static String getFirstname() {
        return user.getFirstname();
    }

    /**
     * Gets the current user's last name.
     *
     * @return the current user's last name
     */
    public static String getLastname() {
        return user.getLastname();
    }

    /**
     * Gets the acronym of the status indicated.
     *
     * @param stvalue the status value
     * @return the acronym for this status
     */
    public static String getAcronym(int stvalue) {
        for(Status s: statuses) {
            if (s.getId() == stvalue) return s.getAcronym();
        }
        return "";
    }

    /**
     * Gets the description of the status indicated.
     *
     * @param stvalue the status value
     * @return the status description
     */
    public static String getStatusDescription(int stvalue) {
        for(Status s: statuses) {
            if (s.getId() == stvalue) return s.getDescription();
        }
        return "";
    }

    /**
     * Gets next status value of a status.
     *
     * @param stvalue the value of the status
     * @return the next status
     */
    public static int getNextStatus(int stvalue) {
        int i;
        for (i=0; i<statuses.size(); i++) {
            if (statuses.get(i).getId() == stvalue) break;
        }

        return statuses.get((i+1)%statuses.size()).getId();
    }

    /**
     * Gets all statuses.
     *
     * @return all statuses
     */
    public static ArrayList<Status> getAllStatuses() {
        return statuses;
    }


    /**
     * Gets the attcontrol token.
     *
     * @return the attcontrol token
     */
    public static String getAttControlToken() {
        return attControlToken;
    }

    /**
     * Sets the attcontrol token.
     *
     * @param attControlToken the attcontrol token
     */
    public static void setAttControlToken(String attControlToken) {
        Att.attControlToken = attControlToken;
    }

    /**
     * Gets the atthome token.
     *
     * @return the atthome token
     */
    public static String getAttHomeToken() {
        return attHomeToken;
    }

    /**
     * Sets the atthome token.
     *
     * @param attHomeToken the atthome token
     */
    public static void setAttHomeToken(String attHomeToken) {
        Att.attHomeToken = attHomeToken;
    }

    /**
     * Gets the moodle site.
     *
     * @return the moodle site
     */
    public static String getSite() {
        return site;
    }

    /**
     * Sets the moodle site.
     *
     * @param attsite the moodle site
     */
    public static void setSite(String attsite) {
        Att.site = attsite;
    }

    /**
     * Has this user atthome access.
     *
     * @return the boolean indicating whether this user has atthome access.
     */
    public static boolean hasAttHome() {
        return attHome;
    }

    /**
     * Sets if this user has atthome access.
     *
     * @param attHome the boolean indicating whether this user has atthome access.
     */
    public static void setAttHome(boolean attHome) {
        Att.attHome = attHome;
    }

    /**
     * Has this user attcontrol access.
     *
     * @return the boolean indicating whether this user has atthome access.
     */
    public static boolean hasAttControl() {
        return attControl;
    }

    /**
     * Sets if this user has attcontrol access.
     *
     * @param attControl the boolean indicating whether this user has attcontrol access.
     */
    public static void setAttControl(boolean attControl) {
        Att.attControl = attControl;
    }

    /**
     * Clears the user data.
     */
    public static void clearUserData() {
        site = "";
        attControl = false;
        attControlToken = "";
        attHome = false;
        attHomeToken = "";
        user = null;


    }

    /**
     * Gets the available status colors.
     *
     * @return the colors
     */
    public static ArrayList<Integer> getColors() {
        return colors;
    }

    /**
     * Sets the available status colors.
     *
     * @param colors the status colors
     */
    public static void setColors(ArrayList<Integer> colors) {
        Att.colors = colors;
    }

    /**
     * Gets the color associated to a status.
     *
     * @param status the status color
     * @return the color
     */
    public static Integer getColor(int status) {
        return colors.get(status);
    }

    /**
     * Sets the color associated to a color.
     *
     * @param status the status
     * @param newcolor the color
     */
    public static void setColor(int status, int newcolor) {
        colors.set(status,newcolor);
    }
}
