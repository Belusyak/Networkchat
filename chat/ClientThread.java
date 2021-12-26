package chat;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private Server server;
    private String userName;

    public ClientThread(Server server, Socket socket, String userName) throws IOException {
        this.userName = userName;
        this.server = server;
        this.socket = socket;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.start();
    }

    @Override
    public void run() {
        try {
            userName = addName();
            while (!interrupted()) {
                String msg = in.readLine();
                if(msg.equals("/exit")) disconnect();
                server.sendEveryoneMsg(ClientThread.this, msg);
            }
        } catch (Exception e) {
            try {
                this.disconnect();
            } catch (IOException ioException) {

            }
        }
    }

    public String addName() throws IOException {
        while (true) {
            sendMsg("Write your nickname");
            String userName = in.readLine();
            if (!server.getNames().contains(userName)) {
                server.addName(userName);
                sendMsg("Welcome to the chat)");
                this.userName = userName;
                server.newClientMsg(this);
                return userName;
            }
            sendMsg("This name already busy. Try to write other name");
        }
    }

    public String getUserName() {
        return userName;
    }

    public synchronized void sendMsg(String msg) throws IOException {
        out.write(msg + "\r\n");
        out.flush();
    }

    public synchronized void disconnect() throws IOException {
        server.disconnectMsg(this);
        interrupt();
        socket.close();
    }
}
