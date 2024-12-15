import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;


public class Gonderici {
    // ... (diğer değişkenler ve metotlar aynı kalır)
    public PrintWriter out;
    public BufferedReader in;
    public static PublicKey publicKey;
    private PrivateKey privateKey;
    public String isim;
   private Mesaj mesaj;
   private BigInteger ozelAnahtar;
   public BigInteger pDegeri = BigInteger.probablePrime(64, new SecureRandom());
   public BigInteger gDegeri;
   private byte[] ortakAnahtar;
   private byte[][] anahtarListesi;
   public BigInteger gondericiPublic;
   public BigInteger aliciPublic;
   private KeyPair keyPair;
    private PublicKey gondericiPublic1;

       public  Gonderici(String isim, String asalSayi) throws Exception {
        this.gDegeri = new BigInteger(asalSayi);
        this.ozelAnahtarHesapla(8);
        this.anahtarListesi = new byte[16][];
        this.mesaj = new Mesaj();
        this.isim = isim;
        KeyPair keyPair = imzalamaVeDogrulama.generateKeyPair();
        publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
        public void imzalama() throws Exception {
                String metin = yanFonksiyonlar.byteToString(this.mesaj.getMetin());
                this.mesaj.setImza(imzalamaVeDogrulama.sign(metin, this.privateKey));
        }

        public void publicKeyHesapla() {
            this.gondericiPublic = this.gDegeri.modPow(this.ozelAnahtar, this.pDegeri);
         }
      
         public void keyGonder(Alici alici) {
            alici.gondericiPublic1 = this.gondericiPublic1;
            alici.gDegeri = this.gDegeri;
            alici.pDegeri = this.pDegeri;
         }
      
         public void ortakAnahtarHesapla() {
            this.ortakAnahtar = this.aliciPublic.modPow(this.ozelAnahtar, this.pDegeri).toByteArray();
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
      
         public void metinSifrele(String Metin) {
            this.mesaj.setMetin(SifrelemeDes.sifrele(Metin.getBytes(), this.anahtarListesi));
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
      
         public void ozelAnahtarYaz() {
            System.out.println(this.getOzelAnahtar());
         }
      
         public PrivateKey getPrivateKey() {
            return this.privateKey;
         }
      }
      
