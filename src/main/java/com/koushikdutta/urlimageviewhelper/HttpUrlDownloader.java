package com.koushikdutta.urlimageviewhelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.AsyncTask;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.RequestPropertiesCallback;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUrlDownloader implements UrlDownloader {
    private RequestPropertiesCallback mRequestPropertiesCallback;

    public RequestPropertiesCallback getRequestPropertiesCallback() {
        return mRequestPropertiesCallback;
    }

    public void setRequestPropertiesCallback(final RequestPropertiesCallback callback) {
        mRequestPropertiesCallback = callback;
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


    @Override
    public void download(final Context context, final String url, final String filename, final UrlDownloaderCallback callback, final Runnable completion) {
        final AsyncTask<Void, Void, Void> downloader = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
                try {
                    InputStream is = null;

                    String thisUrl = url;
                    HttpURLConnection urlConnection;
                    while (true) {
                        final URL u = new URL(thisUrl);


                        //Mindeos: adapted this code so as to accept https connections.
                        if (u.getProtocol().toLowerCase().equals("https")) {
                            trustAllHosts();
                            HttpsURLConnection https = (HttpsURLConnection) u.openConnection();
                            https.setHostnameVerifier(DO_NOT_VERIFY);
                            urlConnection = https;
                        } else {
                            urlConnection = (HttpURLConnection) u.openConnection();
                        }

                        urlConnection.setInstanceFollowRedirects(true);

                        if (mRequestPropertiesCallback != null) {
                            final ArrayList<NameValuePair> props = mRequestPropertiesCallback.getHeadersForRequest(context, url);
                            if (props != null) {
                                for (final NameValuePair pair: props) {
                                    urlConnection.addRequestProperty(pair.getName(), pair.getValue());
                                }
                            }
                        }

                        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP && urlConnection.getResponseCode() != HttpURLConnection.HTTP_MOVED_PERM)
                            break;
                        thisUrl = urlConnection.getHeaderField("Location");
                    }

                    if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        UrlImageViewHelper.clog("Response Code: " + urlConnection.getResponseCode());
                        return null;
                    }
                    is = urlConnection.getInputStream();
                    callback.onDownloadComplete(HttpUrlDownloader.this, is, null);
                    return null;
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final Void result) {
                completion.run();
            }
        };

        UrlImageViewHelper.executeTask(downloader);
    }

    @Override
    public boolean allowCache() {
        return true;
    }
    
    @Override
    public boolean canDownloadUrl(String url) {
        return url.startsWith("http");
    }
}
