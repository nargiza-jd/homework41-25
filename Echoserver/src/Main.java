import server.Server;

public class Main {
    public static void main(String[] args) {
        Server.bindToPort(8089).run();
    }
}