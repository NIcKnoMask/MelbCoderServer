package com.unimelbCoder.melbcode;
import net.sourceforge.pinyin4j.PinyinHelper;

public class test {
    public static void main(String[] args){
        String myString = "我的人-1";
        StringBuilder result = new StringBuilder();

        for (char ch : myString.toCharArray()) {
            // 判断是否是中文字符
            if (Character.toString(ch).matches("[\\u4E00-\\u9FA5]")) {
                // 中文字符转拼音
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    result.append(pinyinArray[0].replaceAll("\\d", ""));
                }
            } else {
                // 英文字符保留不变
                result.append(ch);
            }
        }
        System.out.println(result.toString());
    }
}
