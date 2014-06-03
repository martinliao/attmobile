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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.RestGeneralData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * The login activity for this app
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttMobile extends FragmentActivity {


    /**
     * The EditText for moodle site.
     */
    EditText etMoodleSite;
    /**
     * The EditText for username.
     */
    EditText etUsername;
    /**
     * The EditText for password.
     */
    EditText etPassword;
    /**
     * The login button.
     */
    Button btnLogin;
    /**
     * The checkbox for the remember session behaviour.
     */
    CheckBox chkRememberSession;
    /**
     * The progressbar showed while login.
     */
    ProgressBar pbLogin;
    /**
     * The complete view.
     */
    View view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attmobile);


        etMoodleSite = (EditText)findViewById(R.id.etMoodleSite);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        chkRememberSession = (CheckBox)findViewById(R.id.chkRememberSession);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);


        //Temporary login parameters for testing


        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);
        String site = pref.getString("moodlesite", null);
        String user = pref.getString("moodleuser", null);
        String pass = pref.getString("moodlepass", null);


        if (site != null && user != null && pass != null) {
            doRememberedLogin(site, user, pass);
        }
        else {
            findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
        }
    }

    private void doRememberedLogin(String site, String user, String pass) {
        etMoodleSite.setVisibility(View.GONE);
        etUsername.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        chkRememberSession.setVisibility(View.GONE);

        try {
            GetTokenTask gtt = new GetTokenTask();
            gtt.execute(site, user, pass);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            etMoodleSite.setVisibility(View.VISIBLE);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            chkRememberSession.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Does the login operations
     *
     * @param view the view in which the login is done.
     */
    public void login(View view) {
        this.view = view;

        String site = etMoodleSite.getText().toString();
        String user = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        try {
            GetTokenTask gtt = new GetTokenTask();
            gtt.execute(site, user, password);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();
        }
    }


    private class GetTokenTask extends AsyncTask<String, Integer, Void> {
        protected void onPreExecute() {
            findViewById(R.id.pbLogin).setVisibility(View.VISIBLE);
            Att.clearUserData();
        }
        protected void onPostExecute(Void res) {
            findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);

            if (chkRememberSession.getVisibility() == View.VISIBLE && chkRememberSession.isChecked()) {
                SharedPreferences pref = getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);
                pref.edit().putString("moodlesite", etMoodleSite.getText().toString()).commit();
                pref.edit().putString("moodleuser", etUsername.getText().toString()).commit();
                pref.edit().putString("moodlepass", etPassword.getText().toString()).commit();
            }

            if (Att.hasAttControl()) {
                startAttControlActivity();
                finish();
            }
            else if (Att.hasAttHome()) {
                startAttHomeActivity();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_LONG).show();

                SharedPreferences pref = getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);

                etMoodleSite.setText(pref.getString("moodlesite", etMoodleSite.getText().toString()));
                etUsername.setText(pref.getString("moodleuser", etUsername.getText().toString()));

                pref.edit().remove("moodlesite").commit();
                pref.edit().remove("moodleuser").commit();
                pref.edit().remove("moodlepass").commit();

                etMoodleSite.setVisibility(View.VISIBLE);
                etUsername.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                chkRememberSession.setVisibility(View.VISIBLE);

                pbLogin.setVisibility(View.INVISIBLE);

            }
        }

        protected Void doInBackground(String... params) {
            try {
                String site = params[0];
                String user = params[1];
                String password = params[2];

                String attcontroltoken = getUserToken(site, user, password, Preferences.ATTCONTROLSERVICENAME);

                Att.setSite(site);

                if (attcontroltoken.length() > 0) Att.setAttControlToken(attcontroltoken);

                String atthometoken = getUserToken(site, user, password, Preferences.ATTHOMESERVICENAME);

                if (atthometoken.length() > 0) Att.setAttHomeToken(atthometoken);

                RestGeneralData.initialize();

                initializeColours();


                if (!Att.hasAttControl() && !Att.hasAttHome()) {
                    Att.setAttControlToken("");
                    Att.setAttHomeToken("");
                    Att.setSite("");
                }
            }
            catch (Exception e) {
                //e.printStackTrace();
                Att.setAttControlToken("");
                Att.setAttHomeToken("");
                Att.setSite("");
                Att.setAttControl(false);
                Att.setAttHome(false);
            }

            return null;
        }
    }

    private void initializeColours() {
        SharedPreferences pref = getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);

        if (pref.getInt(Preferences.COLORA, -1) == -1)
            pref.edit().putInt(Preferences.COLORA, getResources().getColor(R.color.colora)).commit();

        if (pref.getInt(Preferences.COLOR1, -1) == -1)
            pref.edit().putInt(Preferences.COLOR1, getResources().getColor(R.color.color1)).commit();

        if (pref.getInt(Preferences.COLOR2, -1) == -1)
            pref.edit().putInt(Preferences.COLOR2, getResources().getColor(R.color.color2)).commit();

        if (pref.getInt(Preferences.COLOR3, -1) == -1)
            pref.edit().putInt(Preferences.COLOR3, getResources().getColor(R.color.color3)).commit();

        if (pref.getInt(Preferences.COLOR4, -1) == -1)
            pref.edit().putInt(Preferences.COLOR4, getResources().getColor(R.color.color4)).commit();

        if (pref.getInt(Preferences.COLOR5, -1) == -1)
            pref.edit().putInt(Preferences.COLOR5, getResources().getColor(R.color.color5)).commit();

        if (pref.getInt(Preferences.COLOR6, -1) == -1)
            pref.edit().putInt(Preferences.COLOR6, getResources().getColor(R.color.color6)).commit();

        if (pref.getInt(Preferences.COLOR7, -1) == -1)
            pref.edit().putInt(Preferences.COLOR7, getResources().getColor(R.color.color7)).commit();

        if (pref.getInt(Preferences.COLOR8, -1) == -1)
            pref.edit().putInt(Preferences.COLOR8, getResources().getColor(R.color.color8)).commit();


        //Load colors in Att class.
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(pref.getInt(Preferences.COLORA, -1));
        colors.add(pref.getInt(Preferences.COLOR1, -1));
        colors.add(pref.getInt(Preferences.COLOR2, -1));
        colors.add(pref.getInt(Preferences.COLOR3, -1));
        colors.add(pref.getInt(Preferences.COLOR4, -1));
        colors.add(pref.getInt(Preferences.COLOR5, -1));
        colors.add(pref.getInt(Preferences.COLOR6, -1));
        colors.add(pref.getInt(Preferences.COLOR7, -1));
        colors.add(pref.getInt(Preferences.COLOR8, -1));

        Att.setColors(colors);
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getUserToken (String site, String user, String password, String servicename) throws Exception {
        String tokenURL = site + "/login/token.php?username=" + user + "&password=" + password + "&service=" + servicename;


        HttpURLConnection http = null;
        URL url = new URL(tokenURL);

        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            http = https;
        } else {
            http = (HttpURLConnection) url.openConnection();
        }

        http.setConnectTimeout(Att.CONNECTIONTIMEOUT);

        InputStream inputStream = (InputStream)http.getContent();

        JSONObject retvalue = new JSONObject(convertInputStreamToString(inputStream));

        return retvalue.getString("token");
    }


    /**
     * Start the attcontrol activity.
     */
    public void startAttControlActivity() {
        Intent intent = new Intent(this, AttControlActivity.class);
        this.startActivity(intent);
    }

    /**
     * Start the atthome activity.
     */
    public void startAttHomeActivity() {
        Intent intent = new Intent(this, AttHomeActivity.class);
        this.startActivity(intent);
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws Exception {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
