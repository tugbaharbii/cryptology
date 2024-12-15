import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


public class Server {
    public static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet(); // Thread-safe set
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345); // Port numarası
        ExecutorService executor = Executors.newFixedThreadPool(10); // İş parçacığı havuzu

        System.out.println("Sunucu başlatıldı. Bağlantı bekleniyor...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Yeni istemci bağlandı: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                executor.submit(clientHandler);
            } catch (IOException e) {
                System.err.println("Bağlantı hatası: " + e.getMessage());
            }
        }
    }

    public static void broadcastMessage(byte[] message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}


