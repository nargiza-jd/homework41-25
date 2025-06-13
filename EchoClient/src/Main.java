import client.Client;

public class Main {
    public static void main(String[] args) {

        Client.connectTo(8089).run();
    }

}