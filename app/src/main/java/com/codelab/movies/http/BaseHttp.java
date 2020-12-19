package com.codelab.movies.http;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseHttp extends AsyncTask<Void, Void, String> {

    private String requestMethod;
    private final String requestUrl;
    private HashMap<String, String> requestParams;

    protected static final String GET = "GET";
    protected static final String POST = "POST";

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    protected BaseHttp(String requestUrl) {
        this.requestUrl = requestUrl;
        this.requestMethod = GET;
    }

    protected void setParams(HashMap<String, String> requestParams, String requestMethod) {
        this.requestMethod = requestMethod;
        this.requestParams = requestParams;
    }

    @Override
    protected String doInBackground(Void... max) {

        String response = sendRequest(requestUrl, requestMethod, requestParams);
        onBackgroundTask(response);
        return response;

    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        onTaskComplete();
    }


    protected abstract void onBackgroundTask(String response);

    protected abstract void onTaskComplete();

    private String sendRequest(String requestUrl, String requestMethod, HashMap<String, String> requestParams) {
        try {
            URL url;
            if (requestMethod.equals(GET) && requestParams != null) {
                Uri.Builder builder = new Uri.Builder();
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
                url = new URL(requestUrl + builder.build().toString());

            } else {
                url = new URL(requestUrl);
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);

            if (requestMethod.equals(GET)) {
                httpURLConnection.setDoOutput(false);
            } else {
                httpURLConnection.setDoOutput(true);
            }

            if (requestMethod.equals(POST) && requestParams != null) {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(requestParams));

                writer.flush();
                writer.close();
                outputStream.close();
            }

            return getResponseInString(httpURLConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private String getResponseInString(HttpURLConnection httpURLConnection) {
        StringBuilder result = new StringBuilder();
        try {
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}
