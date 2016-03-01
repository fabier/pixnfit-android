package com.pixnfit.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by fabier on 29/02/16.
 */
public class HTTPPostFile {
    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    private static final int maxBufferSize = 64 * 1024; // 64Ko

    public static final String TAG = HTTPPostFile.class.getSimpleName();

    public static HttpURLConnection uploadFile(String sUrl, String filePath) throws IOException {
        return uploadFile(sUrl, filePath, null);
    }

    public static HttpURLConnection uploadFile(String sUrl, String filePath, Map<String, String> params) throws IOException {
        URL url = new URL(sUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true); // Allow Inputs
        httpURLConnection.setDoOutput(true); // Allow Outputs
        httpURLConnection.setUseCaches(false); // Don't use a Cached Copy
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        return uploadFile(httpURLConnection, filePath, params);
    }

    public static HttpURLConnection uploadFile(HttpURLConnection httpURLConnection, String filepath) throws IOException {
        return uploadFile(httpURLConnection, filepath, null);
    }

    public static HttpURLConnection uploadFile(HttpURLConnection httpURLConnection, String filepath, Map<String, String> params) throws IOException {
        DataOutputStream dos = null;


        File sourceFile = new File(filepath);
        String fileName = sourceFile.getName();

        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
        httpURLConnection.setRequestProperty("data", fileName);

        httpURLConnection.connect();

        dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);

        // Adding Parameters
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_END);
                // dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_END);
                // dos.writeBytes("Content-Length: " + name.length() + LINE_END);
                dos.writeBytes(LINE_END);
                dos.writeBytes(value);
                dos.writeBytes(LINE_END);
            }
        }

        // Adding Parameter media file(audio,video and image)
        dos.writeBytes("Content-Disposition: form-data; name=\"data\";filename=\"" + fileName + "\"" + LINE_END);
        dos.writeBytes(LINE_END);
        // create a buffer of maximum size

        int bytesAvailable = fileInputStream.available();
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];
        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        // send multipart form data necesssary after file data...
        dos.writeBytes(LINE_END);
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

        // close the streams //
        fileInputStream.close();
        dos.flush();
        dos.close();

        return httpURLConnection;
    }
}
