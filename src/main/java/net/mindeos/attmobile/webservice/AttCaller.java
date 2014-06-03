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


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Class to call the webservice methods
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public abstract class AttCaller {

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
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


    /**
     * Web service call.
     *
     * @param site the site
     * @param token the token
     * @param wsfunction the function to call
     * @param args the arguments for this function
     * @return the results of this call
     */
    protected static JSONArray serviceCall(String site, String token, String wsfunction, String... args) throws Exception {

        JSONArray results = null;
        try {
            String url = site + "/webservice/rest/server.php";

            StringBuilder params = new StringBuilder("wstoken=" + token
                    + "&moodlewsrestformat=json&wsfunction=" + wsfunction);

            for (String arg : args) {
                params.append("&").append(arg);
            }

            URL getUrl = new URL(url);


            HttpURLConnection connection = null;

            if (getUrl.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) getUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);

                connection = https;
            } else {
                connection = (HttpURLConnection) getUrl.openConnection();
            }

            connection.setConnectTimeout(Att.CONNECTIONTIMEOUT);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());
            writer.write(params.toString());
            writer.flush();
            writer.close();

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = reader.readLine();
            buffer.append(line).append('\n');

            if (!buffer.toString().equals("null")) {
                results = new JSONArray(buffer.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    /**
     * Web service call with no results.
     *
     * @param site the site
     * @param token the token
     * @param wsfunction the function to call
     * @param args the arguments for this function
     */
    protected static void serviceCallNoResults(String site, String token, String wsfunction, String... args)
            throws Exception {

        try {
            String url = site + "/webservice/rest/server.php";

            StringBuilder params = new StringBuilder("wstoken=" + token
                    + "&moodlewsrestformat=json&wsfunction=" + wsfunction);

            for (String arg : args) {
                params.append("&").append(arg);
            }

            URL getUrl = new URL(url);

            HttpURLConnection connection = null;

            if (getUrl.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) getUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);

                connection = https;
            } else {
                connection = (HttpURLConnection) getUrl.openConnection();
            }

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setConnectTimeout(Att.CONNECTIONTIMEOUT);
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());
            writer.write(params.toString());
            writer.flush();
            writer.close();

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = reader.readLine();
            buffer.append(line).append('\n');

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Generates the user image URL.
     *
     * @param site the site
     * @param pic1 the pic1 value
     * @param pic2 the pic2 value
     * @return the user image URL
     */
    public static String getUserImageURL(int pic1, int pic2) {
        return Att.getSite() + "/pluginfile.php/" + pic1 + "/user/icon/clean/f1?rev=" + pic2;
    }
}
