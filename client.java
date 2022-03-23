import java.net.*;
import java.io.*;

public class client {
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    private Socket socket;
    private static int port;
    private static String serverName;

    public client(String serverName, int port) {
        this.port = port;
        this.serverName = serverName;
        try {
            this.socket = new Socket(serverName, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        System.out.println("Connected to the server");

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            client.this.CloseConnection();
        }
    }

    private void CloseConnection() {
        System.out.println("Closed connection");
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                System.exit(0);
            }
        } catch (IOException ignored) {}
    }

    public class ReadMsg extends Thread {
        public void run(){
            String str;

            try {
                while (true){
                    str = in.readLine();
                    System.out.println(str);
                }
            }catch(IOException e){
                client.this.CloseConnection();
            }
        }
    }

    public class WriteMsg extends Thread{
        public void run(){
            while (true){

                try{
                    String str = reader.readLine();
                    if(str.equals("stop")){
                        client.this.CloseConnection();
                    }
                    out.write(str+"\n");
                    out.flush();
                }catch(IOException e){
                    client.this.CloseConnection();
                }
            }
        }
    }

    public static void main(String[] args) {
        new client(args[0], Integer.parseInt(args[1]));
    }
}
