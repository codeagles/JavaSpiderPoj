package test;

import java.io.UnsupportedEncodingException;

/**
 * Created by codeagles on 2017/12/22.
 */
public class TestUTF8 {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "竷鐨勮嵂鏁堜环鍊硷紝涓";
        byte[] b = str.getBytes();
        String s = new String(b, "GBK");//解码
        System.out.println(s);
    }
}
