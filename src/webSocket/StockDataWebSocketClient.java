package webSocket;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * This class implements a web socket client, which connects to a data provider 
 * in order to receive push messages when new trade occur. When it receives a message,
 * it hands it over to the registered web socket listener.
 */
public class StockDataWebSocketClient extends WebSocketClient {
    private boolean isConnected = false; 
    private IWebSocketListener listener;


    /**
     * Constructs a new stock data web socket client that connects to the specified server uri
     * and passes received messages to the specified web socket listener.
     *
     * @param serverUri the server uri to connect to
     * @param listener the listener to pass received messages to
     */
    StockDataWebSocketClient(String serverUri, IWebSocketListener listener) {
        super(URI.create(serverUri));
        this.listener = listener;
    }

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be written on.
     *
     * @param handshakedata the handshake of the websocket instance
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Verbindung hergestellt");
        isConnected = true;
    }

    /**
     * Called when a message is received through the web socket connection. The message string
     * is passed to the registered web socket listener for processing.
     *
     * @param message the message received
     */
    @Override
    public void onMessage(String message) {
//        System.out.println("recieved > " + message);
        listener.processWebSocketTradeMessage(message);
    }

    /**
     * Called after the websocket connection has been closed. Prints a message with information
     * about why and by which side it has been closed.
     *
     * @param code the error code
     * @param reason the reason for closing the connection
     * @param remote Returns whether or not the closing of the connection was initiated by the remote host.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        String message = "Verbindung beendet vom " + (remote ? "Server" : "Client") + " Code: " + code + " Ursache: " + reason;
        System.out.println(message);
        isConnected = false;
//        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Called when an error occurs with the connection. Prints error information to the console.
     *
     * @param ex the ex
     */
    @Override
    public void onError(Exception ex) {
        if (ex != null) {
            System.err.println("Fehler: " + ex.getMessage());
            ex.printStackTrace();
        } else
            System.out.println("Unbekannter Fehler");
    }

    /**
     * Checks if the web socket client is connected to a server.
     *
     * @return true, if this client is connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

}