package chat;

import log.Log;

import java.io.*;
import java.lang.constant.Constable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Message, Log {
    private Set<String> names = new HashSet<>();
    private ArrayList<ClientThread> clients = new ArrayList<>();
    private int port;
    private final String pathLog = "src/main/java/log/log.txt";
    private final String pathSettings = "src/main/java/chat/settings.txt";
    static ServerSocket serverSocket;
    private final String serverName = "server";
    Socket socket;

    private Server() throws IOException {
        serverSocket = new ServerSocket(addPort());
        log(serverName, "start");
        while (true) {
            socket = serverSocket.accept();
            System.out.println("new client");
            new ClientThread(this, socket, serverName);
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Set<String> getNames() {
        return names;
    }

    public void addName(String name) {
        names.add(name);
    }

    @Override
    public void sendEveryone(ClientThread client, String msg) throws IOException {
        log(client.getUserName(), msg);
        for (ClientThread c : clients) {
            c.sendMsg(client.getUserName() + ": " + msg);
        }
    }

    @Override
    public void disconnectMsg(ClientThread client) throws IOException {
        sendEveryone(client, "disconnected");
        names.remove(client.getUserName());
        clients.remove(client);

    }

    @Override
    public void newClient(ClientThread client) throws IOException {
        clients.add(client);
        sendEveryone(client, "joined the chat");
    }

    @Override
    public void log(String name, String msg) throws IOException {
        PrintWriter writer2 = new PrintWriter((new FileWriter(pathLog, true)));
        writer2.println(name + ": " + msg);
        writer2.close();
    }

    public int addPort() throws IOException {
        int myPort = 0;
        try (Scanner scanner = new Scanner(System.in);
             PrintWriter writer2 = new PrintWriter((new FileWriter(pathSettings)))) {
            System.out.println("Enter port");
            myPort = scanner.nextInt();
            writer2.println();
            writer2.close();
        } catch (IOException ex) {

        } catch (InputMismatchException ex) {
            System.out.println("try again");
            addPort();
        } finally {
            System.out.println("Your port: " + myPort);
            log(serverName, "port - " + myPort);
            return myPort;
        }
    }
}