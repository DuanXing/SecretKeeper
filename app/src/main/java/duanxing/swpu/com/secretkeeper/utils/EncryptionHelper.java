package duanxing.swpu.com.secretkeeper.utils;

/**
 * Created by duanxing on 10/05/2017.
 */

/**
 * helper class of Encrytion
 */
public class EncryptionHelper {
    // key of encryption.
    public static final String DES_KEY = "M=2cOx0q";
    public static final String AES_KEY = "Lq5Y/sQudP5idjREjRmfVM6dWdMJrPtS";
    public static final String DESEDE_KEY = "guY5Z8BE0S9Kbs2K/y62h1vX";
    public static final String SM4_KEY = "";

    // file header of encryption.
    public static final String DES_FILE_HEADER = "FdNKmfob2Sua7GDmtkudo+CbbLlg==";
    public static final String AES_FILE_HEADER = "wcU+TjirSafiRusgG0FNYcwPoTE47/";
    public static final String DESEDE_FILE_HEADER = "LS34iRmH160pYtg4Fqa1gVDr3nobXx";
    public static final String SM$_FILE_HEADER = "hp5eHwIkjZgIshfU4FDw4i8zIyG3ec";

    // the length of file header
    public static final int fileHeaderLen = 30;
}
