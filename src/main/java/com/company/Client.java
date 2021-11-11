package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {

    private String name;

    private Socket socket;

    private DataOutputStream out;
    private DataInputStream in;

    public Client(Socket socket) throws IOException {
        super(String.format(
                "Socket <%s:%d>", socket.getInetAddress(), socket.getPort()
        ));

        this.socket = socket;

        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        Main.log(this.getName(), "registration..");

        if (!register()) {
            Main.log(this.getName(), "registration is failed");
            return;
        }

        Main.log(this.getName(), "listening for events");

        connect();

        while (!socket.isClosed()) {
            try {
                String message = in.readUTF();

                Request request = new Request();
                request.setJSON(message);

                System.out.println(request.toString());

                doEvent(request);

            } catch (SocketException e) {
                Main.log(this.getName(), "connection lost");
                break;
            } catch (IOException e) {
                Main.log(this.getName(), "error in while looper");
                e.printStackTrace();
                break;
            }
        }

        Main.clients.remove(name);

        disconnect();
    }

    public void doEvent(Request request) throws IOException
    {
        for (Command command : Main.commands)
        {
            if (command.execute(name, request)) {
                break;
            }
        }
    }

    public boolean register()
    {
        boolean success = false;

        do {
            try {
                String message = in.readUTF();

                Request req = new Request();
                req.setJSON(message);

                if (req.name.equals("check_account")) {
                    String nickname = (String) req.getArgument("nickname");
                    boolean exist = Main.userService.checkExist(nickname);

                    Response resp = new Response("check_account");
                    resp.setArgument("exist", exist);

                    sendMessage(resp);
                }

                if (req.name.equals("login") || req.name.equals("register")) {
                    String nickname = (String) req.getArgument("nickname");
                    String password = (String) req.getArgument("password");

                    User user = new User(nickname, password, false);

                    if (req.name.equals("register")) {

                        Main.userService.addUser(user);

                        success = Main.addClient(nickname, this);

                        Response response = new Response("register");
                        response.setArgument("success", success);

                        sendMessage(response);

                        if (success) {
                            this.name = nickname;
                        }

                    } else {

                        boolean auth = Main.userService.auth(user);

                        if (auth) {
                            success = Main.addClient(nickname, this);

                            if (success) {
                                this.name = nickname;
                            }
                        }

                        Response response = new Response("login");
                        response.setArgument("success", success);

                        sendMessage(response);
                    }
                }

            } catch (IOException e) {
                break;
            }
        }
        while (!success);

        return success;
    }

    public void connect() {
        Event event = new Event("updatedUserList");
        event.setArgument("type", "connect");
        event.setArgument("nickname", this.name);

        Main.sendEventToAll(event, this.name);
    }

    public void disconnect() {
        Event event = new Event("updatedUserList");
        event.setArgument("type", "disconnect");
        event.setArgument("nickname", this.name);

        Main.sendEventToAll(event);
    }

    public void sendMessage(Message message) throws IOException
    {
        try {
            out.writeUTF(message.getJSON());
            out.flush();
        } catch (IOException error) {
            socket.close();
        }
    }

    public void sendMessage(String message) throws IOException
    {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException error) {
            socket.close();
        }
    }
}
