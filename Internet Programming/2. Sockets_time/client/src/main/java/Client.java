import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.Buffer;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    static class Connected extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5 * 1000);
                    out.println("hello server");
                    String response = in.readLine();
                    if (response == null) {
                        return;
                    }
                } catch (IOException e) {
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class CommandHandler extends Thread {
        Client client;

        public CommandHandler(Client client) {
            this.client = client;
        }

        public void run() {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while (true) {
                try {
                    line = console.readLine().toLowerCase();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                String[] args = line.split(" ");

                if (args[0].equals("exit") || args[0].equals("quit")) {
                    client.stopConnection();
                }

                if (args[0].equals("time")) {
                    line += " " + TimeZone.getDefault().getRawOffset();
                }

                client.sendMessage(line);
            }
        }
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.err.println("invalid host");
            System.exit(3);
        }
        catch (IOException e) {
            System.err.println("connection not possible");
            System.exit(4);
        }
    }

    public void sendMessage(String msg) {
        try {
            out.println(msg);
            String response = in.readLine();
            if (response == null) {
                throw new Exception();
            }
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("server disconnect");
            System.exit(0);
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        String[] ip = args[0].split(":");

        if (ip.length != 2) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        Client client = new Client();

        try {
            int port = Integer.parseInt(ip[1]);
            if (port < 0 || port > 65535) {
                System.err.println("invalid arguments");
                System.exit(1);
            }

            client.startConnection(ip[0], port);
        }
        catch (NumberFormatException e) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        CommandHandler commandHandler = new CommandHandler(client);
        commandHandler.start();
        Connected connected = new Connected();
        connected.start();

        try {
            connected.join();
            System.out.println("server disconnect");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}