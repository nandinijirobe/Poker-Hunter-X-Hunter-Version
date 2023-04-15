import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;


public class Client extends Thread {

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    String theIPAddress;
    int thePortNumber;
    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, String ipAddress, int portNumber) {
        callback = call;
        // change this hard code later! This is just temporary
        theIPAddress = "127.0.0.1";
        thePortNumber = 5555;
    }

    public void run() {

        try {
            socketClient = new Socket(theIPAddress, thePortNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) {
        }

        while (true) {

            try {
                PokerInfo fromServer = (PokerInfo)in.readObject();
                callback.accept(fromServer);
            } catch (Exception e) {
            }
        }

    }

    public void send(PokerInfo toServer) {
        try{
            out.writeObject(toServer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
