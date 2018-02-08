package com.fxtx.framework.chivox.util;

import java.util.HashMap;

public class EnScoreMap {

    private static HashMap<String, String> data = null;

    public EnScoreMap() {
        if (data == null) {
            data = new HashMap<String, String>();
            data.put("ih", "ɪ");
            data.put("ax", "ə");
            data.put("oh", "ɒ");
            data.put("uh", "ʊ");
            data.put("ah", "ʌ");
            data.put("eh", "e");
            data.put("ae", "æ");
            data.put("iy", "i:");
            data.put("er", "ɜ:");
            data.put("axr", "ɚ");// 注意这个音标的字体
            data.put("ao", "ɔ:");
            data.put("uw", "u:");
            data.put("aa", "ɑ:");
            data.put("ey", "eɪ");
            data.put("ay", "aɪ");
            data.put("oy", "ɔɪ");
            data.put("aw", "aʊ");
            data.put("ow", "әʊ");
            data.put("ia", "ɪə");
            data.put("ea", "ɛә");
            data.put("ua", "ʊə");
            data.put("p", "p");
            data.put("b", "b");
            data.put("t", "t");
            data.put("d", "d");
            data.put("k", "k");
            data.put("g", "g");
            data.put("l", "l");
            data.put("r", "r");
            data.put("m", "m");
            data.put("n", "n");
            data.put("ng", "ŋ");
            data.put("hh", "h");
            data.put("s", "s");
            data.put("z", "z");
            data.put("th", "θ");
            data.put("dh", "ð");
            data.put("f", "f");
            data.put("v", "v");
            data.put("w", "w");
            data.put("y", "j");
            data.put("sh", "ʃ");
            data.put("zh", "ʒ");
            data.put("ch", "tʃ");
            data.put("jh", "dʒ");
        }
    }

    public String get(String cString) {
        if (data.containsKey(cString)) {
            return data.get(cString);
        }
        return cString;
    }

}
