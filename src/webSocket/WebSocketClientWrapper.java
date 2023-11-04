package webSocket;

import org.java_websocket.exceptions.WebsocketNotConnectedException;

import main.Stock;

/**
 * An instance of this class is used to interface the actual web socket client (an instance of StockDataWebSocketClient) 
 * with the rest of the program. This abstraction makes it easier to, for example, change the active data provider while the program is running. 
 * Since web socket client instances are not reusable for a new connection, this wrapper handles creation of a new instance, so that instances of 
 * other classes need not update their references to a new client instance.
 * 
 */
public class WebSocketClientWrapper {
    private StockDataWebSocketClient client;
    private IWebSocketListener listener;

    /**
     * Constructs a new web socket client wrapper that contains a web socket client connecting to 
     * the specified uri with the specified listener to process received messages.
     *
     * @param listener the listener to pass received messages to
     * @param uri the server uri to connect to
     */
    public WebSocketClientWrapper(IWebSocketListener listener, String uri) {
        this.listener = listener;
        client = new StockDataWebSocketClient(uri, listener);
    }

    /**
     * Closes the web socket connection and establishes a new connection to the specified server uri.
     *
     * @param newUri the new server uri to connect to
     */
    public void changeUri(String newUri) {
        try {
            client.closeBlocking();
            client = new StockDataWebSocketClient(newUri, listener);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while closing web socket connection prior to connecting to new uri");
            e.printStackTrace();
        }
    }

    /**
     * Establish the connection.
     */
    public void connect() {
        client.connect();
    }

    /**
     * Send the specified string through a web socket connection to the server.
     *
     * @param message the message to send
     */
    public void send(String message) {
        client.send(message);
    }

    /**
     * Establish the connection and block until ready.
     */
    public void connectBlocking() {
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for web socket connection.");
            e.printStackTrace();
            
//            client.connect();
        }
    }

    /**
     * Subscribe with the server to receive trade messages for the specified stock symbol.
     *
     * @param stockSymbol the stock symbol to subscribe for
     */
    public void subscribe(String stockSymbol) {
//        if (client.isConnected()) {
            try {
                client.send("{\"type\":\"subscribe\", \"symbol\":\"" + stockSymbol + "\"}");
            } catch (WebsocketNotConnectedException e) {
                String message = "Web Socket not connected. Please check data provider details in Preferences and restart the program.";
                System.out.println(message);
//                JOptionPane.showMessageDialog(null, message + ". Cause: " + e.getCause());
            } catch (Exception e) {
                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, e.getMessage() + ". Cause: " + e.getCause());
            } 
//        }
    }
    
    
    /**
     * Subscribe with the server to receive trade messages for the specified stock.
     *
     * @param stock the stock to subscribe for
     */
    public void subscribe(Stock stock) {
        subscribe(stock.getSymbol());
    }

    /**
     * Unsubscribe from receiving trade message for the specified stock symbol.
     *
     * @param stockSymbol the stock symbol to unsubscribe from
     */
    public void unsubscribe(String stockSymbol) {
//      if (client.isConnected()) {
        try {
            client.send("{\"type\":\"unsubscribe\", \"symbol\":\"" + stockSymbol + "\"}");
        } catch (WebsocketNotConnectedException e) {
            String message = "Web Socket not connected. Please check data provider details in Preferences and restart the program.";
            System.out.println(message);
//            JOptionPane.showMessageDialog(null, message + ". Cause: " + e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, e.getMessage() + ". Cause: " + e.getCause());
        } 
//    }
    }

    /**
     * Unsubscribe from receiving trade message for the specified stock.
     *
     * @param stock the stock to unsubscribe from
     */
    public void unsubscribe(Stock stock) {
        unsubscribe(stock.getSymbol());
    }

    /**
     * Checks if the web socket client is connected the server.
     *
     * @return true, if a connected is established, false if not
     */
    public boolean isConnected() {
        return client.isConnected();
    }

}
