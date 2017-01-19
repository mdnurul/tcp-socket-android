package com.example.minal.TCPsocket;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by bitcanny on 19/1/17.
 */

public class SocketClientTask extends AsyncTask<Void, Void, Void> {


    String dstAddress;
    int dstPort;
    String response = "";
    String data;
    Context mContext;

    SocketClientTask(Context context, String addr, int port, String data) {
        dstAddress = addr;
        dstPort = port;
        this.data = data;
        this.mContext = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }




    @Override
    protected Void doInBackground(Void... params) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

			/*
             * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(mContext,"RespMsg: "+response,Toast.LENGTH_SHORT).show();

    }
}
