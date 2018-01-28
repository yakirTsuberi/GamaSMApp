package com.example.yakirtsuberi.gamasm.Requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Requests {
    private static final String BASE_URL = "http://10.0.2.2:8080";


    private String readResponse(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();

    }

    public String get(String url, String token) {
        try {
            URL urlObj = new URL(BASE_URL + url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "JWT " + token);
            if (conn.getResponseCode() == 200) {
                return readResponse(conn.getInputStream());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public String post(String url, String data) {
        try {
            URL urlObj = new URL(BASE_URL + url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.close();
            if (conn.getResponseCode() == 200) {
                return readResponse(conn.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
