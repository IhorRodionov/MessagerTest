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
        this.clientSocket.setSoTimeout(100000);
    }

    @Override
    public void run() {
        while(true) {
            try {

                while (true){
                    String word = in.readLine();
                    if(word.equals("stop")){
                        this.CloseConnection();
                        break;
                    }
                    System.out.println(clientSocket.getLocalSocketAddress()+": "+word);
                    for(serverClient cl: server.serverList){
                            cl.send(word);
                    }
                }
            } catch (SocketTimeoutException s) {
                System.out.println("Socket time ended: "+clientSocket.getLocalSocketAddress());
                this.CloseConnection();
                break;
            } catch (IOException e) {
                System.out.println("Close connection "+clientSocket.getLocalSocketAddress());
                this.CloseConnection();
                e.printStackTrace();
                break;
            }
        }
    }

    private void CloseConnection(){
        try{
            if(!clientSocket.isClosed()){
                clientSocket.close();
                in.close();
                out.close();
                for(serverClient cl: server.serverList){
                    if(cl.equals(this)) cl.interrupt();
                    server.serverList.remove(this);
                }
            }
        }catch(IOException e){}
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

}