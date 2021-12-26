package chat;

import chat.ClientThread;

import java.io.IOException;


public interface Message {

    public void sendEveryone(ClientThread client, String msg) throws IOException;

    public void disconnectMsg(ClientThread client) throws IOException;

    public void newClient(ClientThread client) throws IOException;

}
