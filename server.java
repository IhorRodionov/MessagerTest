import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
public class server {
    public static ArrayList<serverClient> serverList = new ArrayList<>(); // список всех нитей

    public static void main(String [] args) throws IOException {
        int PORT = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started");
        while (true) {
            Socket socket = server.accept();
            System.out.println("New connection "+socket.getLocalSocketAddress());
            try {
                serverList.add(new serverClient(socket));
            } catch (IOException e) {
                socket.close();
            }
        }
    }
}
