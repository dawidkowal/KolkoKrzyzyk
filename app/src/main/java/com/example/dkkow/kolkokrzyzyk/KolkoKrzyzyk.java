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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class KolkoKrzyzyk extends AppCompatActivity {

    private int port = 8988;
    public static WifiP2pInfo info;
    public static boolean run = true;
    public static int pos[];
    public static String your = null;

    public Button start;
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button button5;
    public Button button6;
    public Button button7;
    public Button button8;
    public Button button9;
    public TextView statusText;

    public OnDataReceived onDataReceived = new OnDataReceived() {
        @Override
        public void process(final String data) {
            final int i = Integer.parseInt(data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(your.equals("O")) {
                        pos[i] = 0;
                        position(true);
                        win(0, "Przegrana");
                    } else if(your.equals("X")) {
                        pos[i] = 1;
                        position(true);
                        win(1, "Przegrana");
                    }
                }
            });
        }
    };

    public void position(boolean k) {
        boolean status;
        String games;
        for(int i = 0; i < 9; i++) {
            if(pos[i] == 1) {
                games = "O";
                status = false;
            } else if(pos[i] == 0) {
                games = "X";
                status = false;
            } else {
                games = "";
                status = k;
            }
            maps(i, status, games);
        }
    }

    public void win(int k, String textWin) {
        boolean w = check(k);
        boolean remis = true;

        if(w) {
            statusText.setText(textWin);
            position(false);
        }

        for (int i = 0; i < 9; i++) {
            if (pos[i] == -1) {
                remis = false;
                break;
            }
        }

        if(remis) {
            statusText.setText("Remis");
            position(false);
        }
    }

    private boolean check(int k) {
        if (pos[0] == k && pos[1] == k && pos[2] == k ) {
            return true;
        }
        if (pos[3] == k && pos[4] == k && pos[5] == k ) {
            return true;
        }
        if (pos[6] == k && pos[7] == k && pos[8] == k ) {
            return true;
        }
        if (pos[0] == k && pos[3] == k && pos[6] == k ) {
            return true;
        }
        if (pos[1] == k && pos[4] == k && pos[7] == k ) {
            return true;
        }
        if (pos[2] == k && pos[5] == k && pos[8] == k ) {
            return true;
        }
        if (pos[0] == k && pos[4] == k && pos[8] == k ) {
            return true;
        }
        if (pos[2] == k && pos[4] == k && pos[6] == k ) {
            return true;
        }
        return false;
    }

    private void maps(int i, boolean status, String games) {
        switch (i) {
            case(0):
                button1.setEnabled(status);
                button1.setText(games);
                break;
            case(1):
                button2.setEnabled(status);
                button2.setText(games);
                break;
            case(2):
                button3.setEnabled(status);
                button3.setText(games);
                break;
            case(3):
                button4.setEnabled(status);
                button4.setText(games);
                break;
            case(4):
                button5.setEnabled(status);
                button5.setText(games);
                break;
            case(5):
                button6.setEnabled(status);
                button6.setText(games);
                break;
            case(6):
                button7.setEnabled(status);
                button7.setText(games);
                break;
            case(7):
                button8.setEnabled(status);
                button8.setText(games);
                break;
            case(8):
                button9.setEnabled(status);
                button9.setText(games);
                break;
            default:
                System.out.println(i);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolko_krzyzyk);
        this.info = WiFiDirectBroadcastReceiver.info;

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        statusText = (TextView) findViewById(R.id.status_text);

        pos = new int[9];

        for(int i = 0; i < 9; i++) {
            pos[i] = -1;
        }

        if (info.groupFormed && info.isGroupOwner) {
            new ServerAsyncTask(KolkoKrzyzyk.this, statusText, port, onDataReceived).execute();

            your = "X";

            start = (Button) findViewById(R.id.button);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start.setVisibility(View.GONE);
                    visibility();
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[0] = 0;
                    position(false);
                    ServerAsyncTask.send("0");
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[1] = 0;
                    position(false);
                    ServerAsyncTask.send("1");
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[2] = 0;
                    position(false);
                    ServerAsyncTask.send("2");
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[3] = 0;
                    position(false);
                    ServerAsyncTask.send("3");
                }
            });
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[4] = 0;
                    position(false);
                    ServerAsyncTask.send("4");
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[5] = 0;
                    position(false);
                    ServerAsyncTask.send("5");
                }
            });
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[6] = 0;
                    position(false);
                    ServerAsyncTask.send("6");
                }
            });
            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[7] = 0;
                    position(false);
                    ServerAsyncTask.send("7");
                }
            });
            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[8] = 0;
                    position(false);
                    ServerAsyncTask.send("8");
                }
            });
        }
        else {

            your = "O";

            start = (Button) findViewById(R.id.button);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ClinetTransfer(onDataReceived).start();
                    start.setVisibility(View.GONE);
                    visibility();
                    position(false);
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[0] = 1;
                    position(false);
                    ClinetTransfer.send("0");
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[1] = 1;
                    position(false);
                    ClinetTransfer.send("1");
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[2] = 1;
                    position(false);
                    ClinetTransfer.send("2");
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[3] = 1;
                    position(false);
                    ClinetTransfer.send("3");
                }
            });
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[4] = 1;
                    position(false);
                    ClinetTransfer.send("4");
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[5] = 1;
                    position(false);
                    ClinetTransfer.send("5");
                }
            });
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[6] = 1;
                    position(false);
                    ClinetTransfer.send("6");
                }
            });
            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[7] = 1;
                    position(false);
                    ClinetTransfer.send("7");
                }
            });
            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos[8] = 1;
                    position(false);
                    ClinetTransfer.send("8");
                }
            });
        }
    }

    public void visibility() {
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
        button5.setVisibility(View.VISIBLE);
        button6.setVisibility(View.VISIBLE);
        button7.setVisibility(View.VISIBLE);
        button8.setVisibility(View.VISIBLE);
        button9.setVisibility(View.VISIBLE);
    }

    public static class ClinetTransfer extends Thread {

        public static OnDataReceived receivedListener;
        public static PrintWriter printWriter;

        public ClinetTransfer(OnDataReceived onDataReceived) {
            receivedListener = onDataReceived;
        }

        public static void send(String data){
            if(printWriter != null){
                System.out.println("printWriter OK");
                printWriter.println(data);
            }
        }

        public void run() {
            Socket socket = new Socket();
            try {
                socket.connect((new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), 8988)), 5000);
                printWriter  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (run) {
                    String data = input.readLine();
                    if (data != null && receivedListener != null) {
                        receivedListener.process(data);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }

    public static class ServerAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private TextView statusText;
        private int port;

        public static OnDataReceived receivedListener;
        public static boolean run = true;
        public static PrintWriter printWriter;

        public static void send(String data){
            if(printWriter != null){
                System.out.println("printWriter OK");
                printWriter.println(data);
            }
        }

        public ServerAsyncTask(Context context, View statusText, int port, OnDataReceived onDataReceived) {
            this.context = context;
            this.port = port;
            this.statusText = (TextView) statusText;
            receivedListener = onDataReceived;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket client = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                printWriter = new PrintWriter(client.getOutputStream(), true);
                while (run) {
                    String data = input.readLine();
                    if (data != null && receivedListener != null) {
                        receivedListener.process(data);
                    }
                }
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
