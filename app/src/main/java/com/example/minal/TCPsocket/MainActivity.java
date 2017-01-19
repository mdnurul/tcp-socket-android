package com.example.minal.TCPsocket;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.minal.TCPsocket.R;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   //193.193.165.165:20758"

    private EditText serverIpET, serverPortET,dataEt;

    private Button startButton, stopButton;

    private String serverIpAddress = "193.193.165.165";
    private int serverPort = 20758;

    String myData = "Hello World!";

    private boolean connected = false;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        serverIpET = (EditText)findViewById(R.id.serverIpET);
        serverPortET = (EditText)findViewById(R.id.serverPortET);

        dataEt = (EditText)findViewById(R.id.dataEt);

        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);

        serverIpET.setText(serverIpAddress);
        serverPortET.setText(String.valueOf(serverPort));

        dataEt.setText(myData);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == startButton){
            serverIpAddress = serverIpET.getText().toString();
            serverPort =  Integer.valueOf(serverPortET.getText().toString());
            myData = dataEt.getText().toString();
            if (!serverIpAddress.equals("") && serverPort != 0) {
                Thread cThread = new Thread(new ClientThread());
                cThread.start();
                startButton.setClickable(false);
                stopButton.setClickable(true);
            }
        }

        if (v == stopButton){
            connected = false;
            startButton.setClickable(true);
            stopButton.setClickable(false);
        }


    }


    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, serverPort);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        // where you issue the commands
                        out.println(myData);
                        Log.d("ClientActivity", "C: Sent.");
                        Thread.sleep(1000*2);
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }

}
