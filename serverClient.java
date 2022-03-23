import java.net.*;
import java.io.*;

public class serverClient extends Thread {
    private Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public serverClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                String nick = in.readLine();
                for(serverClient cl: server.serverList){
                    cl.send(nick+" joined server");
                }

                while (true){
                    String word = in.readLine();
                    if(word.equals("stop")){
                        this.CloseConnection();
                        break;
                    }
                    System.out.println(clientSocket.getRemoteSocketAddress()+": "+word);
                    for(serverClient cl: server.serverList){
                        cl.send(word);
                    }
                }
            } catch (SocketTimeoutException s) {
                this.CloseConnection();
                break;
            } catch (IOException e) {
                this.CloseConnection();
                break;
            }
        }
    }

    private void CloseConnection(){
        try{
            if(!clientSocket.isClosed()){
                System.out.println("Close connection: "+clientSocket.getRemoteSocketAddress());
                clientSocket.close();
                in.close();
                out.close();
                for(serverClient cl: server.serverList){
                    if(cl.equals(this)) cl.interrupt();
                }
                server.serverList.remove(this);
                testinfo();
            }
        }catch(IOException e){}
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
            System.out.println("***Sended to"+this.clientSocket.getRemoteSocketAddress());
        } catch (IOException ignored) {
            this.CloseConnection();
        }
    }

    private void testinfo(){
        System.out.println("***length: "+server.serverList.size());
        for(serverClient cl: server.serverList){
            System.out.println("***"+cl.clientSocket.getRemoteSocketAddress());
        }

    }

}