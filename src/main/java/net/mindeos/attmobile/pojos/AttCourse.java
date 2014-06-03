package net.mindeos.attmobile.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import net.mindeos.attmobile.pojos.AttControl;
import net.mindeos.attmobile.pojos.Course;

import java.util.ArrayList;


/**
 * POJO representing a course and a set of its attcontrols in the Moodle database.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttCourse implements Parcelable {

    private Course course;
    private ArrayList<AttControl> attcontrols;


    /**
     * Instantiates a new AttCourse.
     *
     * @param courseid the course id
     * @param coursename the course name
     * @param courseshortname the course short name
     */
    public AttCourse(int courseid, String coursename, String courseshortname) {
        this.course = new Course(courseid, coursename, courseshortname);
        this.attcontrols = new ArrayList<AttControl>();
    }

    /**
     * Instantiates a new AttCourse.
     *
     * @param c the course ofject
     */
    public AttCourse(Course c) {
        this.course = c;
        this.attcontrols = new ArrayList<AttControl>();
    }

    /**
     * Gets the course.
     *
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course.
     *
     * @param course the course
     */
    public void setCourse(Course course) {
        this.course = course;
    }


    /**
     * Gets all the attcontrols of a course.
     *
     * @return the attcontrols
     */
    public ArrayList<AttControl> getAttcontrols() {
        return attcontrols;
    }

    /**
     * Sets the attcontrols of a course.
     *
     * @param attcontrols the attcontrols for the course
     */
    public void setAttcontrols(ArrayList<AttControl> attcontrols) {
        this.attcontrols = attcontrols;
    }

    /**
     * Adds an attcontrol to the set.
     *
     * @param ac the attcontrol
     */
    public void addAttControl(AttControl ac) {
        this.attcontrols.add(ac);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}