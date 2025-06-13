package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    private final int port;
    public Server(int port) {
        this.port = port;
    }

    public static Server bindToPort(int port) {
        return new Server(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.printf("Сервер запущен на порту %s", port);
            while (true) {
                try (Socket socket = server.accept()) {
                    handle(socket);
                }
            }
        } catch (IOException e) {
            System.out.printf("Порт %s занят или возникла ошибка.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        PrintWriter writer = new PrintWriter(outputStream, true);

        try (Scanner scanner = new Scanner(isr)) {
            while (true) {
                String message = scanner.nextLine().strip();
                System.out.printf("Получено: %s%n", message);

                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Завершение соединения.");
                    writer.println("Goodbye!");
                    return;
                }

                String response = new StringBuilder(message).reverse().toString();
                writer.println(response);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Клиент отключился.");
        }
    }
}
