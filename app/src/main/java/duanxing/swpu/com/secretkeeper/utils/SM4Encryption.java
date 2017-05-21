package duanxing.swpu.com.secretkeeper.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by duanxing on 03/05/2017.
 */

public class SM4Encryption extends BaseEncryption{
    // s box.
    private final byte sBox[][] =
    {
            { (byte)0xd6, (byte)0x90, (byte)0xe9, (byte)0xfe, (byte)0xcc, (byte)0xe1, (byte)0x3d, (byte)0xb7, (byte)0x16, (byte)0xb6, (byte)0x14, (byte)0xc2, (byte)0x28, (byte)0xfb, (byte)0x2c, (byte)0x05 },
            { (byte)0x2b, (byte)0x67, (byte)0x9a, (byte)0x76, (byte)0x2a, (byte)0xbe, (byte)0x04, (byte)0xc3, (byte)0xaa, (byte)0x44, (byte)0x13, (byte)0x26, (byte)0x49, (byte)0x86, (byte)0x06, (byte)0x99 },
            { (byte)0x9c, (byte)0x42, (byte)0x50, (byte)0xf4, (byte)0x91, (byte)0xef, (byte)0x98, (byte)0x7a, (byte)0x33, (byte)0x54, (byte)0x0b, (byte)0x43, (byte)0xed, (byte)0xcf, (byte)0xac, (byte)0x62 },
            { (byte)0xe4, (byte)0xb3, (byte)0x1c, (byte)0xa9, (byte)0xc9, (byte)0x08, (byte)0xe8, (byte)0x95, (byte)0x80, (byte)0xdf, (byte)0x94, (byte)0xfa, (byte)0x75, (byte)0x8f, (byte)0x3f, (byte)0xa6 },
            { (byte)0x47, (byte)0x07, (byte)0xa7, (byte)0xfc, (byte)0xf3, (byte)0x73, (byte)0x17, (byte)0xba, (byte)0x83, (byte)0x59, (byte)0x3c, (byte)0x19, (byte)0xe6, (byte)0x85, (byte)0x4f, (byte)0xa8 },
            { (byte)0x68, (byte)0x6b, (byte)0x81, (byte)0xb2, (byte)0x71, (byte)0x64, (byte)0xda, (byte)0x8b, (byte)0xf8, (byte)0xeb, (byte)0x0f, (byte)0x4b, (byte)0x70, (byte)0x56, (byte)0x9d, (byte)0x35 },
            { (byte)0x1e, (byte)0x24, (byte)0x0e, (byte)0x5e, (byte)0x63, (byte)0x58, (byte)0xd1, (byte)0xa2, (byte)0x25, (byte)0x22, (byte)0x7c, (byte)0x3b, (byte)0x01, (byte)0x21, (byte)0x78, (byte)0x87 },
            { (byte)0xd4, (byte)0x00, (byte)0x46, (byte)0x57, (byte)0x9f, (byte)0xd3, (byte)0x27, (byte)0x52, (byte)0x4c, (byte)0x36, (byte)0x02, (byte)0xe7, (byte)0xa0, (byte)0xc4, (byte)0xc8, (byte)0x9e },
            { (byte)0xea, (byte)0xbf, (byte)0x8a, (byte)0xd2, (byte)0x40, (byte)0xc7, (byte)0x38, (byte)0xb5, (byte)0xa3, (byte)0xf7, (byte)0xf2, (byte)0xce, (byte)0xf9, (byte)0x61, (byte)0x15, (byte)0xa1 },
            { (byte)0xe0, (byte)0xae, (byte)0x5d, (byte)0xa4, (byte)0x9b, (byte)0x34, (byte)0x1a, (byte)0x55, (byte)0xad, (byte)0x93, (byte)0x32, (byte)0x30, (byte)0xf5, (byte)0x8c, (byte)0xb1, (byte)0xe3 },
            { (byte)0x1d, (byte)0xf6, (byte)0xe2, (byte)0x2e, (byte)0x82, (byte)0x66, (byte)0xca, (byte)0x60, (byte)0xc0, (byte)0x29, (byte)0x23, (byte)0xab, (byte)0x0d, (byte)0x53, (byte)0x4e, (byte)0x6f },
            { (byte)0xd5, (byte)0xdb, (byte)0x37, (byte)0x45, (byte)0xde, (byte)0xfd, (byte)0x8e, (byte)0x2f, (byte)0x03, (byte)0xff, (byte)0x6a, (byte)0x72, (byte)0x6d, (byte)0x6c, (byte)0x5b, (byte)0x51 },
            { (byte)0x8d, (byte)0x1b, (byte)0xaf, (byte)0x92, (byte)0xbb, (byte)0xdd, (byte)0xbc, (byte)0x7f, (byte)0x11, (byte)0xd9, (byte)0x5c, (byte)0x41, (byte)0x1f, (byte)0x10, (byte)0x5a, (byte)0xd8 },
            { (byte)0x0a, (byte)0xc1, (byte)0x31, (byte)0x88, (byte)0xa5, (byte)0xcd, (byte)0x7b, (byte)0xbd, (byte)0x2d, (byte)0x74, (byte)0xd0, (byte)0x12, (byte)0xb8, (byte)0xe5, (byte)0xb4, (byte)0xb0 },
            { (byte)0x89, (byte)0x69, (byte)0x97, (byte)0x4a, (byte)0x0c, (byte)0x96, (byte)0x77, (byte)0x7e, (byte)0x65, (byte)0xb9, (byte)0xf1, (byte)0x09, (byte)0xc5, (byte)0x6e, (byte)0xc6, (byte)0x84 },
            { (byte)0x18, (byte)0xf0, (byte)0x7d, (byte)0xec, (byte)0x3a, (byte)0xdc, (byte)0x4d, (byte)0x20, (byte)0x79, (byte)0xee, (byte)0x5f, (byte)0x3e, (byte)0xd7, (byte)0xcb, (byte)0x39, (byte)0x48 }
    };

    // 密钥
    private final int MK[] = { 0x3bc87d21, 0x20a1b889, 0x12093cad, 0xe8c917ab };
    // 轮密钥
    private static int RK[] = new int[32];
    private static boolean rkInit = false;
    // 系统参数
    private final int FK[] = { 0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc };
    // 固定参数
    private final int CK[] = {
            0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
            0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
            0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
            0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
            0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
            0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
            0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
            0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279
    };

    private final int BLOCK_SIZE = 16;

    // construction
    public SM4Encryption(String src, String dest, OP_CIPHER_MODE mode) {
        this.cipherMode = mode;
        this.TAG = "SM4_ENCRYPTION";
        this.srcFilePath = src;
        this.saveFilePath = dest;
    }

    // create RK
    private void createRK() {
        int k[] = { MK[0] ^ FK[0], MK[1] ^ FK[1], MK[2] ^ FK[2], MK[3] ^ FK[3] };

        RK[0] = k[0] ^ keysLineConverted(keysUnLineConverted(k[1] ^ k[2] ^ k[3] ^ CK[0]));
        RK[1] = k[1] ^ keysLineConverted(keysUnLineConverted(k[2] ^ k[3] ^ RK[0] ^ CK[1]));
        RK[2] = k[2] ^ keysLineConverted(keysUnLineConverted(k[3] ^ RK[0] ^ RK[1] ^ CK[2]));
        RK[3] = k[3] ^ keysLineConverted(keysUnLineConverted(RK[0] ^ RK[1] ^ RK[2] ^ CK[3]));
        for (int i = 4; i < 32; i++) {
            RK[i] = RK[i - 4] ^ keysLineConverted(keysUnLineConverted(RK[i - 3] ^ RK[i - 2] ^ RK[i - 1] ^ CK[i]));
        }

        rkInit = true;
    }

    //8比特输入8比特输出置换
    private byte sBoxValue(byte val) {
        byte x = (byte) (val >> 4);
        x &= 0x0F;
        byte y = (byte) (val & 0x0F);

        return sBox[x][y];
    }

    //32比特非线性￡转换
    private int keysUnLineConverted(int conIn) {
        byte a0 = (byte)((conIn >> 24) & 0x000000FF);
        byte a1 = (byte)((conIn >> 16) & 0x000000FF);
        byte a2 = (byte)((conIn >> 8) & 0x000000FF);
        byte a3 = (byte)(conIn & 0x000000FF);

        int temp = 0;
        temp |= (sBoxValue(a0) << 24);
        temp |= (sBoxValue(a1) << 16);
        temp |= (sBoxValue(a2) << 8);
        temp |= sBoxValue(a3);

        return temp;
    }

    // i 循环左移 n 位
    private int loopMoveLeft(int i, int n) {
        return ((i << n) | (i >>> (32 - n)));
    }

    //32比特轮密钥线性L'转换
    private int keysLineConverted(int conIn) {
        return conIn ^ loopMoveLeft(conIn, 13) ^ loopMoveLeft(conIn, 23);
    }

    //32比特数据线性L转换
    private int dataLineConverted(int conIn) {
        return conIn ^ loopMoveLeft(conIn, 2) ^ loopMoveLeft(conIn, 10) ^ loopMoveLeft(conIn, 18) ^ loopMoveLeft(conIn, 24);
    }

    //组数据加密
    private void groupEncrypt(byte[] in, byte[] out)
    {
        int[] iArr = new int[4];
        int[] temp = new int[4];
        // byte to int
        for(int i = 0;i < 4;++i) {
            temp[0] = in[4 * i + 0] & 0xFF;
            temp[1] = in[4 * i + 1] & 0xFF;
            temp[2] = in[4 * i + 2] & 0xFF;
            temp[3] = in[4 * i + 3] & 0xFF;
            iArr[i] = (temp[0] << 24) | (temp[1] << 16) | (temp[2] << 8) | temp[3];
        }

        // encrypt
        for(int i = 0;i < 32;i += 4) {
            iArr[0] = iArr[0] ^ dataLineConverted(keysUnLineConverted(iArr[1] ^ iArr[2] ^ iArr[3] ^ RK[i + 0]));
            iArr[1] = iArr[1] ^ dataLineConverted(keysUnLineConverted(iArr[2] ^ iArr[3] ^ iArr[0] ^ RK[i + 1]));
            iArr[2] = iArr[2] ^ dataLineConverted(keysUnLineConverted(iArr[3] ^ iArr[0] ^ iArr[1] ^ RK[i + 2]));
            iArr[3] = iArr[3] ^ dataLineConverted(keysUnLineConverted(iArr[0] ^ iArr[1] ^ iArr[2] ^ RK[i + 3]));
        }

        // reverse
        for(int i = 0;i < 16;i += 4) {
            out[i]     = (byte) ((iArr[3 - i / 4] >>> 24) & 0xFF);
            out[i + 1] = (byte) ((iArr[3 - i / 4] >>> 16) & 0xFF);
            out[i + 2] = (byte) ((iArr[3 - i / 4] >>> 8) & 0xFF);
            out[i + 3] = (byte) ((iArr[3 - i / 4]) & 0xFF);
        }
    }

    //组数据解密
    private void groupDecrypt(byte[] in, byte[] out)
    {
        int[] iArr = new int[4];
        int[] temp = new int[4];
        // byte to int
        for(int i = 0;i < 4;++i) {
            temp[0] = in[4 * i + 0] & 0xFF;
            temp[1] = in[4 * i + 1] & 0xFF;
            temp[2] = in[4 * i + 2] & 0xFF;
            temp[3] = in[4 * i + 3] & 0xFF;
            iArr[i] = (temp[0] << 24) | (temp[1] << 16) | (temp[2] << 8) | temp[3];
        }

        // encrypt
        for(int i = 0;i < 32;i += 4) {
            iArr[0] = iArr[0] ^ dataLineConverted(keysUnLineConverted(iArr[1] ^ iArr[2] ^ iArr[3] ^ RK[31 - (i + 0)]));
            iArr[1] = iArr[1] ^ dataLineConverted(keysUnLineConverted(iArr[2] ^ iArr[3] ^ iArr[0] ^ RK[31 - (i + 1)]));
            iArr[2] = iArr[2] ^ dataLineConverted(keysUnLineConverted(iArr[3] ^ iArr[0] ^ iArr[1] ^ RK[31 - (i + 2)]));
            iArr[3] = iArr[3] ^ dataLineConverted(keysUnLineConverted(iArr[0] ^ iArr[1] ^ iArr[2] ^ RK[31 - (i + 3)]));
        }

        // reverse
        for(int i = 0;i < 16;i += 4) {
            out[i]     = (byte) ((iArr[3 - i / 4] >>> 24) & 0xFF);
            out[i + 1] = (byte) ((iArr[3 - i / 4] >>> 16) & 0xFF);
            out[i + 2] = (byte) ((iArr[3 - i / 4] >>> 8) & 0xFF);
            out[i + 3] = (byte) ((iArr[3 - i / 4]) & 0xFF);
        }
    }

    // 数据加密处理函数
    private void doEncrypt(byte[] in, int inLen, byte[] out) {
        byte[] inData = new byte[BLOCK_SIZE];
        byte[] outData = new byte[BLOCK_SIZE];
        int index = 0;

        while (inLen >= BLOCK_SIZE) {
            inData = Arrays.copyOfRange(in, index, index + BLOCK_SIZE);
            groupEncrypt(inData, outData);
            System.arraycopy(outData, 0, out, index, BLOCK_SIZE);
            inLen -= BLOCK_SIZE;
            index += BLOCK_SIZE;
        }
    }

    // 数据解密处理函数
    private void doDecrypt(byte[] in, int inLen, byte[] out) {
        byte[] inData = new byte[BLOCK_SIZE];
        byte[] outData = new byte[BLOCK_SIZE];
        int index = 0;

        while (inLen >= BLOCK_SIZE) {
            inData = Arrays.copyOfRange(in, index, index + BLOCK_SIZE);
            groupDecrypt(inData, outData);
            System.arraycopy(outData, 0, out, index, BLOCK_SIZE);
            inLen -= BLOCK_SIZE;
            index += BLOCK_SIZE;
        }
    }

    // 提供给外部的单组数据加密接口
    public void singleDecrypt(byte[] in, int inLen, byte[] out) {
        doDecrypt(in, inLen, out);
    }

    @Override
    public boolean init() {
        // TODO initialized = true
        // 因为密钥固定了，所以rk只需初始化一次
        if(!rkInit)
            createRK();
        initialized = true;

        return true;
    }

    @Override
    protected boolean encrypt() {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_ENCRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Encryption mode.");
            return false;
        }

        try {
            InputStream inFile = new FileInputStream(srcFilePath);
            if(null == inFile) {
                Log.e(TAG, "no inputStream.");
                return false;
            }

            OutputStream outFile = new FileOutputStream(saveFilePath);

            // first, write the file header.
            byte[] fileHeader = EncryptionHelper.SM4_FILE_HEADER.getBytes();
            outFile.write(fileHeader, 0, EncryptionHelper.fileHeaderLen);

            // encrypt
            byte[] fileCache = new byte[CACHE_SIZE];
            byte[] out = new byte[CACHE_SIZE];
            int len, extraData = 0;
            while((len = inFile.read(fileCache, 0, CACHE_SIZE)) > 0) {
                // 读到了文件末尾了
                if(0 != len % BLOCK_SIZE) {
                    extraData = (16 - (len % 16));

                    // 数据填充
                    for(int i = 0;i < extraData;++i) {
                        fileCache[len + i] = (byte) extraData;
                    }
                }

                doEncrypt(fileCache, len + extraData, out);

                outFile.write(out, 0, len + extraData);
                outFile.flush();
            }

            // 如果数据刚好为16的整数倍，那么追加16字节填充
            if(0 == extraData) {
                for(int i = 0;i < BLOCK_SIZE;++i) {
                    out[i] = 16;
                }

                outFile.write(out, 0, BLOCK_SIZE);
                outFile.flush();
            }

            inFile.close();
            outFile.close();

            Log.i(TAG, "Encrypt success!");

            return true;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();

            Log.e(TAG, "Encrypt failed!");
        }
        catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();

            Log.e(TAG, "Encrypt failed!");
        }

        return false;
    }

    @Override
    protected boolean decrypt() {
        if(!initialized) {
            Log.i(TAG, "Not initialized.");
            return false;
        }

        if(OP_CIPHER_MODE.BASE_DECRYPT_MODE != cipherMode) {
            Log.i(TAG, "the cipherMode not in Decryption mode.");
            return false;
        }

        try {
            InputStream inFile = new FileInputStream(srcFilePath);
            if(null == inFile) {
                Log.e(TAG, "no inputStream.");
                return false;
            }

            // skip the file header.
            inFile.skip(EncryptionHelper.fileHeaderLen);

            OutputStream outFile = new FileOutputStream(saveFilePath);

            // 解密时需解密的数据一定是: 16*N + 1 (1为该文件的多余字节数)
            byte[] fileCache = new byte[CACHE_SIZE];
            byte[] out = new byte[CACHE_SIZE];
            boolean firstWrite = true;
            int len, frontLen = 0;

            while((len = inFile.read(fileCache, 0, CACHE_SIZE)) > 0) {
                if(!firstWrite) {
                    outFile.write(out, 0, frontLen);
                    outFile.flush();
                }
                else {
                    // 需处理最后的多余字节，所以每一次写入的是上一次while循环解密的数据
                    firstWrite = false;
                }

                doDecrypt(fileCache, len, out);

                frontLen = len;
            }

            // 处理最后一组带填充的数据
            byte extraLen = out[frontLen - 1];
            outFile.write(out, 0, frontLen - extraLen);
            outFile.flush();

            inFile.close();
            outFile.close();

            Log.i(TAG, "Decrypt success!");

            return true;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();

            Log.e(TAG, "Decrypt failed!");
        }
        catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();

            Log.e(TAG, "Decrypt failed!");
        }

        return false;
    }
}

