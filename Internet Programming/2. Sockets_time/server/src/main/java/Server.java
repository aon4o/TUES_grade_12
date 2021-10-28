import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ServerConnection(clientSocket).start();
        }
    }

    static class ServerConnection extends Thread {
        private final Socket clientSocket;

        ServerConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.toLowerCase();
                    ArrayList<String> args = new ArrayList<>(List.of(line.split(" ")));
                    String command = args.remove(0);

                    switch (command) {
                        case "time":
                            out.println(new Command().time(args));
                            break;
                        default:
                            out.println("invalid input");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            if (port < 0 || port > 65535) {
                System.err.println("invalid arguments");
                System.exit(1);
            }

            Server server=new Server();
            server.start(port);
        }
        catch (NumberFormatException e) {
            System.err.println("invalid arguments");
            System.exit(1);
        }
        catch (BindException e) {
            System.err.println("port is already in use");
            System.exit(2);
        }
    }
}