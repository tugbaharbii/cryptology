import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws Exception {
        String serverAddress = "127.0.0.1"; // Sunucu IP adresi (localhost için)
        int port = 12345;

        try (
            Socket socket = new Socket(serverAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            Gonderici gonderici = new Gonderici("Gonderici", "909091");
                        Alici alici = new Alici("Alici");
            gonderici.out = out;
            gonderici.in = in;
                        alici.out = out;
                        alici.in=in;
                        ClientHandler.diffieHelmanBaslat(gonderici, alici);
                        gonderici.anahtarOlustur();
                        alici.anahtarOlustur();

            while (true) {
                System.out.print("Mesajınızı girin (çıkmak için 'exit' yazın): ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                gonderici.metinSifrele(message);
                gonderici.imzalama();
                byte[] messageToSend = yanFonksiyonlar.mesajiBirlestir(gonderici.getMesaj().getMetin(), gonderici.getMesaj().getImza());

                out.println(yanFonksiyonlar.byteToString(messageToSend));
                                System.out.println("Gönderilen şifreli mesaj:"+yanFonksiyonlar.byteToString(messageToSend));
            }
        } catch (UnknownHostException e) {
            System.err.println("Sunucu bulunamadı: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O hatası: " + e.getMessage());
        }
    }
}