package com.gfq.gbaseutl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * create by 高富强
 * on {2019/11/6} {10:55}
 * desctapion:
 */
public class TestMain {

        public static void main(String[] args) {
            PrintWriter pw;
            try {
                pw = new PrintWriter("D://abcdefg.txt");
                pw.print("3.常用方法：\n" +
                        "\n" +
                        "（1）print(String str)：向文件写入一个字符串。\n" +
                        "\n" +
                        "（2）print(char[] ch)：向文件写入一个字符数组。\n" +
                        "\n" +
                        "（3）print(char c)：向文件写入一个字符。\n" +
                        "\n" +
                        "（4）print(int i)：向文件写入一个int型值。\n" +
                        "\n" +
                        "（5）print(long l)：向文件写入一个long型值。\n" +
                        "\n" +
                        "（6）print(float f)：向文件写入一个float型值。\n" +
                        "\n" +
                        "（7）print(double d)：向文件写入一个double型值。\n" +
                        "\n" +
                        "（8）print(boolean b)：向文件写入一个boolean型值。\n" );
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


}
