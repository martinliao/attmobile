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

package net.mindeos.attmobile.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.RestSession;
import net.mindeos.attmobile.pojos.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment for the EditSession activity.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class EditSessionFragment extends Fragment implements
        View.OnClickListener,
        CalendarDatePickerDialog.OnDateSetListener,
        RadialTimePickerDialog.OnTimeSetListener,
        TimePickerDialogFragment.TimePickerDialogHandler
{

    private View v;

    private EditText etEditSessionDate;
    private EditText etEditSessionTime;
    private EditText etEditSessionDuration;
    private EditText etEditSessionDescripton;

    private long sessionTimestamp;
    private int sessionDuration;
    private String sessionDescription;
    private int sessionId;

    private Session session;

    private LayoutInflater inflater = null;


    /**
     * Instantiates a new EditSession fragment.
     */
    public EditSessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sessionId = getActivity().getIntent().getExtras().getInt("sessionId");
            sessionTimestamp = getActivity().getIntent().getExtras().getLong("sessionTimestamp");
            sessionDuration = getActivity().getIntent().getExtras().getInt("sessionDuration");
            sessionDescription = getActivity().getIntent().getExtras().getString("sessionDescription");


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Save edited data.
     */
    public void saveData() {
        try {
            SaveEditSessionTask sest = new SaveEditSessionTask();
            sest.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        v = inflater.inflate(R.layout.fragment_editsession, container, false);


        etEditSessionDate = (EditText) v.findViewById(R.id.etEditSessionDate);
        etEditSessionTime = (EditText) v.findViewById(R.id.etEditSessionTime);
        etEditSessionDuration = (EditText) v.findViewById(R.id.etEditSessionDuration);
        etEditSessionDescripton = (EditText) v.findViewById(R.id.etEditSessionDescripton);


        etEditSessionDate.setText(Att.sdf.format(sessionTimestamp));
        etEditSessionTime.setText(Att.shf.format(sessionTimestamp));

        etEditSessionDescripton.setText(sessionDescription+"");
        etEditSessionDuration.setText(String.format("%02d:%02d", sessionDuration/3600, (sessionDuration%3600)/60));

        etEditSessionDate.setOnClickListener(this);
        etEditSessionTime.setOnClickListener(this);
        etEditSessionDuration.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==etEditSessionDate.getId())
        {
            selectDate(v);
        }
        else if(v.getId()==etEditSessionTime.getId())
        {
            selectTime(v);
        }
        else if(v.getId()==etEditSessionDuration.getId())
        {
            selectDuration(v);
        }
    }

    private void selectDate(View v) {
        try {
            Date d = Att.sdf.parse(etEditSessionDate.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(EditSessionFragment.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), etEditSessionDate.getId()+"");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectTime(View v) {
        try {
            Date d = Att.shf.parse(etEditSessionTime.getText().toString());

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            RadialTimePickerDialog radialTimePickerDialog = RadialTimePickerDialog.newInstance(EditSessionFragment.this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
            radialTimePickerDialog.show(getActivity().getSupportFragmentManager(), etEditSessionTime.getId()+"");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void selectDuration(View v) {
        TimePickerBuilder tpb = new TimePickerBuilder()
                .setFragmentManager(getActivity().getSupportFragmentManager())
                .setStyleResId(R.style.AttControlPickerTheme);
        tpb.addTimePickerDialogHandler(EditSessionFragment.this);
        tpb.show();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        if (dialog.getTag().equals(etEditSessionDate.getId()+"")) {
            etEditSessionDate.setText(Att.sdf.format(c.getTime()));
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout dialog, int hour, int minutes) {
        Calendar c = Calendar.getInstance();
        c.set(1990, Calendar.JANUARY, 1, hour, minutes);

        etEditSessionTime.setText(Att.shf.format(c.getTime()));
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        etEditSessionDuration.setText("" + hourOfDay + ":" + String.format("%02d", minute));
    }

    private class SaveEditSessionTask extends AsyncTask<Void, Integer, Integer> {
        protected void onPreExecute() {
            v.findViewById(R.id.pbEditSessionLoading).bringToFront();
            v.findViewById(R.id.pbEditSessionLoading).setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Integer result) {
            v.findViewById(R.id.pbEditSessionLoading).setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(),getString(R.string.sessionupdatesuccessful), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        protected Integer doInBackground(Void... params) {
            try {
                String sessiondatetime = etEditSessionDate.getText()+" "+etEditSessionTime.getText();
                String[] durationarray = etEditSessionDuration.getText().toString().split(":");
                int duration = (Integer.parseInt(durationarray[0])*60 + Integer.parseInt(durationarray[1])) * 60;

                Long sessiontimestamp = Att.sdhf.parse(sessiondatetime).getTime();
                String description = etEditSessionDescripton.getText().toString();

                RestSession.saveEditSession(sessionId, sessiontimestamp, duration, description);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    }
}