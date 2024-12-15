import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.interfaces.RSAPublicKey;

public final class Alici {
    public String isim;
    public Mesaj mesaj;
    private BigInteger ozelAnahtar;
    public BigInteger pDegeri;
    public BigInteger gDegeri;
    private byte[] ortakAnahtar;
    private byte[][] anahtarListesi;
    public BigInteger gondericiPublic;
    public BigInteger aliciPublic;
    public PrintWriter out;
    public BufferedReader in;
    public String gondericiPublicStr; 
    public PublicKey gondericiPublic1;

    Alici(String isim) {
        this.ozelAnahtarHesapla(8);
        this.anahtarListesi = new byte[16][];
        this.mesaj = new Mesaj();
        this.isim = isim;
    }

    public void publicKeyOlustur() throws Exception {
      if (gondericiPublicStr != null && !gondericiPublicStr.isEmpty()) {
          byte[] publicBytes = Base64.getDecoder().decode(gondericiPublicStr);
          X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          gondericiPublic1 = keyFactory.generatePublic(keySpec);
      } else {
          throw new IllegalArgumentException("Gonderici public key string is null or empty.");
      }
  }
  
    public String metinDesifreleByte(byte[] metin) {
        byte[] acikMetin = SifrelemeDes.desifrele(yanFonksiyonlar.mesajAyir(metin,metin.length - this.mesaj.getImza().length), this.anahtarListesi);
        return yanFonksiyonlar.byteToString(acikMetin);
    }
    public boolean dogrulamaByte(byte[] gelenMesaj) throws Exception {
        int mesajBoyutu = gelenMesaj.length - this.mesaj.getImza().length;
                byte[] mesajByte = yanFonksiyonlar.mesajAyir(gelenMesaj,mesajBoyutu);
                byte[] imzaByte = yanFonksiyonlar.imzaAyir(gelenMesaj,mesajBoyutu,this.mesaj.getImza().length);
        String metin = yanFonksiyonlar.byteToString(mesajByte);
        return imzalamaVeDogrulama.verify(metin, imzaByte, Gonderici.publicKey);
    }
    
    public void publicKeyHesapla() {
      this.aliciPublic = this.gDegeri.modPow(this.ozelAnahtar, this.pDegeri);
   }

   public void keyGonder(Gonderici gonderici) {
      gonderici.aliciPublic = this.aliciPublic;
   }

   public void ortakAnahtarHesapla() throws Exception {
    if (this.gondericiPublic1 == null) {
        throw new IllegalStateException("Gonderici public key is not initialized.");
    }

    // PublicKey'i RSAPublicKey'e cast et
    RSAPublicKey rsaPublicKey = (RSAPublicKey) this.gondericiPublic1;

    // Modulus değeri alınarak modPow işlemi yapılır
    this.ortakAnahtar = rsaPublicKey.getModulus().modPow(this.ozelAnahtar, this.pDegeri).toByteArray();
}

   public void ozelAnahtarHesapla(int uzunluk) {
      SecureRandom secureRandom = new SecureRandom();
      BigInteger t = BigInteger.probablePrime(64, secureRandom);
      secureRandom.setSeed(System.currentTimeMillis());

      do {
         do {
            this.ozelAnahtar = new BigInteger(uzunluk * 8, secureRandom);
         } while(this.ozelAnahtar.compareTo(BigInteger.ONE) <= 0);
      } while(this.ozelAnahtar.compareTo(t.subtract(BigInteger.ONE)) >= 0);

   }
   
   public void anahtarOlustur() {
    this.anahtarListesi = anahtarUret.anahtar16Uret(this.ortakAnahtar);
 }
  
 public byte[] getOrtakAnahtar() {
    return this.ortakAnahtar;
 }

 public Mesaj getMesaj() {
    return this.mesaj;
 }

 public void setMesaj(Mesaj mesaj) {
    this.mesaj = mesaj;
 }

 public BigInteger getOzelAnahtar() {
    return this.ozelAnahtar;
 } 

}