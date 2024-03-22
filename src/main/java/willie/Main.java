package willie;

import willie.thread.ConnectionThread;
import willie.util.AccountsManager;
import willie.util.KeyUtils;

public class Main {
    public static String host = "127.0.0.1";
    public static int port = 8080;
    
    public static void main(String[] args){
        AccountsManager.addAccount("willie", "qwer1234");
        AccountsManager.addAccount("david", "qwer1234");
        AccountsManager.getAccount("willie").addFriend(AccountsManager.getAccount("david"));
        AccountsManager.getAccount("david").addFriend(AccountsManager.getAccount("willie"));
        
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