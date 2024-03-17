package willie;

import willie.thread.ConnectionThread;
import willie.util.KeyUtils;

public class Main {
    public static String host = "127.0.0.1";
    public static int port = 8080;
    
    public static void main(String[] args){
        KeyUtils.generateKey();
        String encrypt;
        try{
            encrypt = KeyUtils.encrypt(KeyUtils.publicKey, "sussy");
//            System.out.println("PublicKey: " + KeyUtils.publicKey);
//            System.out.println("Encrypt: " + encrypt);
//            System.out.println("Decrypt: " + KeyUtils.decrypt(encrypt));
        }catch(Exception e){
            e.printStackTrace();
        }
        ConnectionThread connectionThread = new ConnectionThread(host, port);
        connectionThread.start();
    }
}