package com.xh.robot.util;

import com.blankj.utilcode.util.EncryptUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MapUtil {

    public static String getSignature(Map<String, String> params,String key) throws IOException {
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            if (param.getValue() != null && !"".equals(param.getKey().trim()) &&
                    !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue())) {
                baseString.append(param.getKey().trim()).append("=")
                        .append(URLEncoder.encode(param.getValue().toString(), "UTF-8")).append("&");
            }
        }
        if (baseString.length() > 0) {
            baseString.deleteCharAt(baseString.length() - 1).append("&app_key=")
                    .append(key);
        }
        try {
            String sign = EncryptUtils.encryptMD5ToString(baseString.toString());
            return sign.toUpperCase();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

}
