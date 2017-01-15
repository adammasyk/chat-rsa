package pl.masyk.chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by adamm on 18.11.2016.
 */
abstract class NetworkConnection {
    private ConnectionThread connThread = new ConnectionThread();
    private Consumer<String> onReceiveCallback;
    public RSA rsaKey = new RSA();
    public boolean keySended = false;


    public NetworkConnection(Consumer<String> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    public void startConnection() throws Exception {
        connThread.start();
    }

    public void send(Serializable data) throws Exception {
        connThread.out.writeObject(data);
    }

    public void closeConnection() throws Exception {
        connThread.socket.close();
    }

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    private class ConnectionThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        @Override
        public void run() {
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while (true) {
                    Object data = in.readObject();
                    if (!keySended) {
                        rsaKey.setForeginKey(data.toString());
                        keySended = true;
                    }
                    onReceiveCallback.accept(data.toString());
                }
            }
            catch (Exception e) {
                onReceiveCallback.accept("Connection closed");
            }
        }
    }
}
