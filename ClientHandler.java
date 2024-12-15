import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Gonderici gonderici;
    private Alici alici;
    private PublicKey otherPublicKey;
    private PrivateKey privateKey;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
public void run() {
    try (
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
    ) {
        this.gonderici = new Gonderici("Gonderici", "909091");
        this.alici = new Alici("Alici");
        this.gonderici.out = out;
        this.gonderici.in = in;
        this.alici.out = out;
        this.alici.in = in;

        // Anahtar oluşturma işlemleri
        KeyPair keyPair = imzalamaVeDogrulama.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.otherPublicKey = keyPair.getPublic();
        Gonderici.publicKey = otherPublicKey;

        byte[] publicKeyBytes = Gonderici.publicKey.getEncoded();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKeyBytes);
        alici.gondericiPublicStr = publicKeyStr;

        // publicKeyOlustur çağrısı
        alici.publicKeyOlustur();

        // Diffie-Hellman başlatma
        alici.gDegeri = gonderici.gDegeri;
        alici.pDegeri = gonderici.pDegeri;
        ClientHandler.diffieHelmanBaslat(this.gonderici, this.alici);
        this.gonderici.anahtarOlustur();
        this.alici.anahtarOlustur();

        while (true) {
            String inputLine = in.readLine();
            if (inputLine == null) break;

            // Mesaj işlemleri
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            clientSocket.close();
            Server.clients.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    

    public void sendMessage(byte[] message) {
        this.gonderici.out.println(yanFonksiyonlar.byteToString(message));
    }

    public static void diffieHelmanBaslat(Gonderici gonderici, Alici alici) {
        gonderici.publicKeyHesapla();
        gonderici.keyGonder(alici);
        alici.publicKeyHesapla();
        alici.keyGonder(gonderici);
        alici.ortakAnahtarHesapla();
        gonderici.ortakAnahtarHesapla();
        yanFonksiyonlar.esitMi(gonderici, alici);
    }
}