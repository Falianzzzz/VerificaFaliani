package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MioThread extends Thread {

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    private static int disponibilitaGold = 10;
    private static int disponibilitaPit = 30;
    private static int disponibilitaParterre = 60;
    private static ArrayList<String> utentiConn = new ArrayList<>();

    public MioThread(Socket s) throws IOException {

        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    @Override
    public void run() {

        String utente = "";

        out.println("WELCOME");

        boolean success = false;
        while (!success) {

            String cmd = "";
            try {
                cmd = in.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (cmd == null)
                break;
            String[] messaggio = cmd.split(" ", 2);

            if (messaggio.length == 2 && messaggio[0].equals("LOGIN") || !(messaggio[1] == "")) {
              
                success = true;
                utente = messaggio[1];
                
                  for (String ut : utentiConn) {
                    if (messaggio[1].equals(ut)) {
                        success = false;
                        out.println("ERR USERINUSE");
                        break;
                    }
                }
            } else
                out.println("ERR LOGINREQUIRED");

        }
        utentiConn.add(utente);
        out.println("OK");

        String[] command = { "", "" };
        while (true) {
            String cmd = "";
            try {
                cmd = in.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            command = cmd.split(" ", 2);

            if (command[0].equals("QUIT")) {
                out.println("BYE");
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }

            switch (command[0]) {

                case "N":

                    // AVAIL Gold:5 Pit:30 Parterre:60

                    out.println("AVAIL Gold:" + disponibilitaGold + " PIT:" + disponibilitaPit
                            + " Parterre:" + disponibilitaParterre);

                    break;

                case "BUY":

                    String com = "";
                    try {
                        com = in.readLine();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String[] buy = com.split(" ", 2);

                    if (buy[1].equals("")) {
                        out.println("ERR SYNTAX");
                        break;
                    }
                    int quantita = Integer.parseInt(buy[1]);

                    switch (buy[0]) {

                        case "Gold":

                            if (quantita > disponibilitaGold)
                                out.println("KO");
                            else
                                disponibilitaGold -= quantita;

                            break;
                        case "Pit":

                            if (quantita > disponibilitaPit)
                                out.println("KO");
                            else
                                disponibilitaPit -= quantita;

                            break;
                        case "Parterre":

                            if (quantita > disponibilitaParterre)
                                out.println("KO");
                            else
                                disponibilitaParterre -= quantita;

                            break;

                    }

                    break;

                default:
                    out.println("ERR SYNTAX");
                    break;

            }

        }

    }
}
