package chat;

import chat.ClientThread;

import java.io.IOException;


public interface Message {

    public void sendEveryoneMsg(ClientThread client, String msg) throws IOException;

    public void disconnectMsg(ClientThread client) throws IOException;

    public void newClientMsg(ClientThread client) throws IOException;

}
