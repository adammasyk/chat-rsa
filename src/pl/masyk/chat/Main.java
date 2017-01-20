package pl.masyk.chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private boolean isServer = false;

    private TextArea messages = new TextArea();
    private TextArea encryptedMessage = new TextArea();
    private NetworkConnection connection = isServer ? createServer() : createClient();
    private boolean keySended = false;

    private Parent createContent() {
        messages.appendText("Decrypted msg:\n");
        messages.setFont(Font.font(24));
        messages.setPrefHeight(550);
        encryptedMessage.setFont(Font.font(24));
        encryptedMessage.setPrefHeight(550);
        encryptedMessage.appendText("Encrypted msg:\n");
        TextField input = new TextField();
        messages.appendText("Do you want to connect to other client ? (y/n) \n");
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();
            if (keySended) {
                messages.appendText(message + "\n");
                encryptedMessage.appendText(connection.rsaKey.encryptMessage(message) + "\n");
            }
            try {
                if (!keySended && (message.toLowerCase().contains("y"))) {
                    connection.send(connection.rsaKey.getPublicKey());
                    keySended = true;
                    messages.appendText("Connection succesfull ! \n");
                } else if (!keySended && message.toLowerCase().contains("n")){
                    Platform.exit();
                    System.exit(0);
                } else {
                    connection.send(connection.rsaKey.encryptMessage(message));
                }
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });
        VBox root = new VBox(20, messages, encryptedMessage, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle(isServer ? "Server" : "Client");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> {
            Platform.runLater(() -> {
                if (keySended) {
                    messages.appendText(connection.rsaKey.decryptMessage(data) + "\n");
                    encryptedMessage.appendText(data + "\n");
                }
            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                if (keySended) {
                messages.appendText(connection.rsaKey.decryptMessage(data) + "\n");
                encryptedMessage.appendText(data + "\n");
                }
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
