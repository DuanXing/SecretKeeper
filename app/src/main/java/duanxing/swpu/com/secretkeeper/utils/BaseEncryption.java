package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 03/05/2017.
 */

public abstract class BaseEncryption {
    // initial before encrypt
    public abstract void init();

    // encryption
    public abstract void encrypt();

    // decryption
    public abstract void decrypt();
}
