package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private final int port;
    private final String host;

    private Client(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static Client connectTo(int port) {
        return new Client(port, "LocalHost");
    }

    public void run() {
        System.out.printf("Для выхода напишите 'bye'%n%n%n");

        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, "UTF-8");
            Scanner serverScanner = new Scanner(socket.getInputStream(), "UTF-8");
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            try (scanner; serverScanner) {
                while (true) {
                    System.out.print("Введите сообщение: ");
                    String message = scanner.nextLine();
                    writer.println(message);

                    if (message.equalsIgnoreCase("bye")) {
                        System.out.println("Завершение соединения...");
                        return;
                    }

                    if (serverScanner.hasNextLine()) {
                        String response = serverScanner.nextLine();
                        System.out.printf("Ответ от сервера: %s%n", response);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Соединение прервано.");
        } catch (IOException e) {
            System.out.printf("Не удаётся подключиться к %s:%s%n", host, port);
            e.printStackTrace();
        }
    }
}
