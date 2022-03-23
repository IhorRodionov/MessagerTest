import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
public class server {
    public static ArrayList<serverClient> serverList = new ArrayList<>(); // список всех нитей

    public static void main(String [] args) throws IOException {
        int PORT = 7770;
        if(args.length>0){
            PORT = Integer.parseInt(args[0]);
        }

        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started");
        System.out.println("***length: "+serverList.size());

        while (true) {
            Socket socket = server.accept();
            System.out.println("New connection "+socket.getRemoteSocketAddress());
            try {
                serverList.add(new serverClient(socket));
            } catch (IOException e) {
                socket.close();
            }
        }
    }
}
