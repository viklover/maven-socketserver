package com.company;

import com.company.dao.UserDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.company.HibernateSessionFactory.sessionFactory;

public class Main {

    public static Hashtable<String, Client> clients = new Hashtable<String, Client>();

    public static LinkedList<Command> commands = new LinkedList<Command>(List.of(
            new ListCommand(),
            new MessageCommand(),
            new DefaultCommand()
    ));

    public static UserDAOImpl userService = new UserDAOImpl();

    public static ServerSocket server;

    public static void main(String[] args) throws IOException
    {

        log("Started the Server Socket");
        log("Opening ports on the network..");

        server = new ServerSocket(8080);

        log("Listening for connections..");

        while (!server.isClosed())
        {
            Socket socket = server.accept();

            log("New connection: " + socket.getInetAddress());

            Client client = new Client(socket);
            client.start();
        }

        log("Server is closed");
    }


    public static boolean addClient(String nickname, Client client) {

        ArrayList<String> nicknames = new ArrayList<String>(clients.keySet());

        for (int i = 0; i < nicknames.size(); ++i) {
            if (nickname.equals(nicknames.get(i))) {
                return false;
            }
        }

        clients.put(nickname, client);

        return true;
    }

    public static void sendEventToAll(String event, String exception) {
        clients.forEach((nick, client) -> {
            try {
                if (!nick.equals(exception)) {
                    client.sendMessage(event);
                }
            } catch (IOException e) {
                clients.remove(client);
            }
        });
    }

    public static void sendEventToAll(Message event, String exception) {
        clients.forEach((nick, client) -> {
            try {
                if (!nick.equals(exception)) {
                    client.sendMessage(event);
                }
            } catch (IOException e) {
                clients.remove(client);
            }
        });
    }

    public static void sendEventToAll(String event) {
        sendEventToAll(event, null);
    }

    public static void sendEventToAll(Message event) {
        sendEventToAll(event, null);
    }

    public static void log(String text) {
        Date date = new Date();
        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.GERMAN).format(date) + " - " + text);
    }

    public static void log(String name, String text) {
        Date date = new Date();
        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.GERMAN).format(date) + " - " + name + ": " + text);
    }
}


