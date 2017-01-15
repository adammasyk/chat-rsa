package pl.masyk.chat;

import java.util.function.Consumer;

/**
 * Created by adamm on 18.11.2016.
 */
public class Client extends NetworkConnection{
    private String ip;
    private int port;

    public Client(String ip, int port, Consumer<String> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
