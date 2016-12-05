package com.example.dkkow.kolkokrzyzyk;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class KolkoKrzyzyk extends AppCompatActivity {

    private int port;
    public static WifiP2pInfo info;
    public Button b;

    public static boolean run = true;

    public TraceListener listener = new TraceListener() {
        @Override
        public void setSend(int send) {

        }

        @Override
        public int getSend() {
            return 0;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolko_krzyzyk);
        this.info = WiFiDirectBroadcastReceiver.info;
        if (info.groupFormed && info.isGroupOwner) {
            port = 8988;
            new ServerAsyncTask(KolkoKrzyzyk.this, findViewById(R.id.status_text), port, listener).execute();
        }
        else {
            clinetTransfer(listener);
        }
    }

    protected void clinetTransfer (final TraceListener listener) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(info.groupOwnerAddress.getHostAddress(), 8988);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter printWriter  = new PrintWriter(socket.getOutputStream(), true);
                    while (run) {
                        System.out.println(input.readLine());
                        printWriter.println(listener.getSend());
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public static class ServerAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private TextView statusText;
        private int port;

        public static boolean run = true;
        public static PrintWriter printWriter;
        public static BufferedReader input;

        private TraceListener listener;

        public static void send(int data){
            if(printWriter!=null){
                printWriter.println(data);
            }
        }

        public static String up() throws IOException {
            if (input != null){
                String in = input.readLine();
                if(in != null) {
                    return in;
                }
            }
            return null;
        }

        public ServerAsyncTask(Context context, View statusText, int port, TraceListener listener) {
            this.context = context;
            this.port = port;
            this.statusText = (TextView) statusText;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (run) {
                    Socket client = serverSocket.accept();
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    printWriter = new PrintWriter(client.getOutputStream(), true);
                    send(1);
//                    send(listener.getSend());
                    System.out.println(up());
                    client.close();
                }
                serverSocket.close();
                return "done";
            } catch (IOException e) {
                System.out.println(e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            statusText.setText("Exit a server socket");
        }

        @Override
        protected void onPreExecute() {
            statusText.setText("Opening a server socket" + port + info.groupOwnerAddress.getHostAddress());
        }

    }

}
