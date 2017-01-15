package pl.masyk.chat;

import java.util.function.Consumer;

/**
 * Created by adamm on 18.11.2016.
 */
public class Server extends NetworkConnection {

    private int port;

    public Server(int port, Consumer<String> onReceiveCallback) {
        super(onReceiveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }

}
