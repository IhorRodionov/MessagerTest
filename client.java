import java.net.*;
import java.io.*;

public class client {
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    private Socket socket;
    private static int port;
    private static String serverName;
    private static String nickname;

    public client(String serverName, int port) {
        this.port = port;
        this.serverName = serverName;

        System.out.println(String.format("Connecting to %s:%s", serverName, port));
        try {
            this.socket = new Socket(serverName, port);
            System.out.println("Connected to the server");
        } catch (IOException e) {
            System.err.println("Socket failed");
        }


        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            this.enterNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            client.this.CloseConnection();
        }
    }

    private void enterNickname(){
        System.out.print("Enter your nickname: ");
        try{
            nickname=reader.readLine();
            out.write(nickname+"\n");
            out.flush();
        }catch(IOException e){}
    }

    private void CloseConnection() {
        try {
            if (!socket.isClosed()) {
                System.out.println("Closed connection");
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
                    if (str==null) client.this.CloseConnection();
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
                        out.write("stop\n");
                        out.flush();
                        client.this.CloseConnection();
                    }
                    out.write(String.format("%s: %s\n", nickname, str));
                    out.flush();
                }catch(IOException e){
                    client.this.CloseConnection();
                }
            }
        }
    }

    public static void main(String[] args) {
        if(args.length>0){
            new client(args[0], Integer.parseInt(args[1]));
        }else{
            new client("localhost", 7770);
        }
    }
}
