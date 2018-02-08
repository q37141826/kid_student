package com.fxtx.framework.chivox.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Xml;

import com.chivox.core.CoreType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class chivoxEngineHelper {

    protected static EnScoreMap enScoreMap = new EnScoreMap();

    public static SpannableStringBuilder processResultJson(JSONObject json, String refText, CoreType coreType, boolean isOnline) throws JSONException {
        //String refText = json.getJSONObject("params").getJSONObject("request").getString("refText");
        SpannableStringBuilder returnTextspan = new SpannableStringBuilder(refText);
        //String coreType = json.getJSONObject("params").getJSONObject("request").getString("coreType");
        int rank = 100;
        if (isOnline) {
            rank = json.getJSONObject("params").getJSONObject("request").getInt("rank");
        } else {
            rank = json.getJSONObject("result").getInt("rank");
        }
        if (coreType == CoreType.en_sent_score) {
            SpannableStringBuilder sentTextspan = new SpannableStringBuilder(refText);
            Log.d("chivoxEngineHelper", "CoreType :" + CoreType.en_sent_score.toString());
            Log.d("inside process", String.valueOf(json.getJSONObject("result").getJSONArray("details").length()));
            for (int i = 0; i < json.getJSONObject("result").getJSONArray("details").length(); i++) {
                int beginIndex = 0;
                int endIndex = 0;
                int score = 0;
                JSONObject charDetailsjson = (JSONObject) json.getJSONObject("result").getJSONArray("details").get(i);
                if (isOnline) {
                    beginIndex = charDetailsjson.getInt("beginindex");
                    endIndex = charDetailsjson.getInt("endindex") + 1;
                    score = charDetailsjson.getInt("score");
                } else {
                    //JSONObject charDetailsjson = (JSONObject)json.getJSONObject("result").getJSONArray("details").get(i);
                    beginIndex = refText.toUpperCase().indexOf(charDetailsjson.getString("char").toUpperCase());
                    endIndex = beginIndex + charDetailsjson.getString("char").length();
                    score = charDetailsjson.getInt("score");
                }
                paintSent(score, rank, beginIndex, endIndex, sentTextspan);
            }
            returnTextspan = sentTextspan;
        } else if (coreType == CoreType.en_word_score) {
            Log.d("process word", "processResultJson: " + json.getJSONObject("result").getJSONArray("details").toString());
            try {
                JSONArray phoneDetailsjson = json.getJSONObject("result").getJSONArray("details").getJSONObject(0).getJSONArray("phone");
                String wordText = refText;
                String phoneText = getPhonetext(phoneDetailsjson);
                refText += ' ' + phoneText;
                Log.d("inside try", "after refText :" + refText);
                SpannableStringBuilder wordTextspan = new SpannableStringBuilder(refText);
                //if phone is not null, start paint the word
                paintWord(phoneDetailsjson, wordText, rank, wordTextspan);
                Log.d("inside try", "Phone json :" + phoneDetailsjson.toString());
                returnTextspan = wordTextspan;
            } catch (JSONException jse) {
                Log.d("inside catch", "Phone json is null");
            }
        } else if (coreType == CoreType.en_pred_score) {
            //refText = json.getJSONObject("params").getJSONObject("request").getJSONObject("refText").getString("lm");
            JSONArray predDetailsjson = json.getJSONObject("result").getJSONArray("details");
            SpannableStringBuilder predTextspan = new SpannableStringBuilder(refText);
            paintPred(predDetailsjson, refText, rank, predTextspan);
            returnTextspan = predTextspan;
        }
        return returnTextspan;
    }

    public static void paintSent(int score, int rank, int beginIndex, int endIndex, SpannableStringBuilder refTextspan) {
        switch (rank) {
            case 4: {
                if (score == 0) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (score == 1) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (score == 2) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            case 100: {
                if (0 <= score && score <= 54) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (55 <= score && score <= 69) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (70 <= score && score <= 84) {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    refTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    public static void paintWord(JSONArray phoneDetailsjson, String wordText, int rank, SpannableStringBuilder wordTextspan) throws JSONException {
        if (wordText.startsWith("s")) {
            checkforvoiceless(wordText);
        }
        int beginIndex = wordText.length() + 1;
        for (int i = 0; i < phoneDetailsjson.length(); i++) {
            String phoneChar = String.format(enScoreMap.get(phoneDetailsjson.getJSONObject(i).getString("char")), Xml.Encoding.UTF_8);
            int score = phoneDetailsjson.getJSONObject(i).getInt("score");
            int phoneCharLength = phoneChar.length();
            switch (rank) {
                case 4: {
                    if (score == 0) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (score == 1) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (score == 2) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                case 100: {
                    if (0 <= score && score <= 54) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (55 <= score && score <= 69) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (70 <= score && score <= 84) {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        wordTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, beginIndex + phoneCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            beginIndex += phoneCharLength;
        }
    }

    public static void paintPred(JSONArray predDetailsjson, String refText, int rank, SpannableStringBuilder PredTextspan) throws JSONException {
        int beginIndex = 0;
        for (int i = 0; i < predDetailsjson.length(); i++) {
            if (refText.toUpperCase().contains(predDetailsjson.getJSONObject(i).getString("text").toUpperCase())) {
                int score = predDetailsjson.getJSONObject(i).getInt("score");
                beginIndex = refText.toUpperCase().indexOf(predDetailsjson.getJSONObject(i).getString("text").toUpperCase());
                int length = predDetailsjson.getJSONObject(i).getString("text").length();
                switch (rank) {
                    case 4: {
                        if (score == 0) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (score == 1) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (score == 2) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    case 100: {
                        if (0 <= score && score <= 54) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.RED), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (55 <= score && score <= 69) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.BLUE), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (70 <= score && score <= 84) {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.BLACK), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            PredTextspan.setSpan(new ForegroundColorSpan(Color.GREEN), beginIndex, beginIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
    }

    public static String getPhonetext(JSONArray phoneDetailsjson) throws JSONException {
        String phonetext = "";
        boolean needToCheck = false;
        for (int i = 0; i < phoneDetailsjson.length(); i++) {
            phonetext += String.format(enScoreMap.get(phoneDetailsjson.getJSONObject(i).getString("char")), Xml.Encoding.UTF_8);
        }
        return phonetext;
    }

    public static String checkforvoiceless(String phoneText) {
        String returnPhoneText = phoneText;
        if (phoneText.contains("sd")) {
            returnPhoneText = phoneText.replaceFirst("sd", "st");
            Log.d("inside checkVoiceless", "hit sd, after replace, phoneText: " + returnPhoneText);
        } else if (phoneText.contains("sb")) {
            returnPhoneText = phoneText.replaceFirst("sb", "sp");
            Log.d("inside checkVoiceless", "hit sb, after replace, phoneText: " + returnPhoneText);
        } else if (phoneText.contains("sg")) {
            returnPhoneText = phoneText.replaceFirst("sg", "sk");
            Log.d("inside checkVoiceless", "hit sg, after replace, phoneText: " + returnPhoneText);
        }
        return returnPhoneText;
    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params, String encode) {

        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
   * Function  :   处理服务器的响应结果（将输入流转化成字符串）
   * Param     :   inputStream服务器的响应输入流
   */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @param uploadFilePath 文件路径
     * @author ylbf_dev
     */
    public static void uploadFile(String uploadUrl,String uploadFilePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"testFile.txt\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
            FileInputStream fis = new FileInputStream(uploadFilePath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();
            System.out.println("file send to server............");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            // 读取服务器返回结果
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Log.d("POST","reslut: "+ result);
            dos.close();
            is.close();
        } catch (Exception e) {
            Log.d("POST","exception");
            e.printStackTrace();
        }

    }

    public static void uploadFileToChivox(String appkey,String tokenid,String logType,String userId, String uploadFilePath){
        String realUrl =String.format("http://demo.chivox.com/public/netool/sdklog.php?appkey=%s&userid=%s&logtype=%s&tokenid=%s",appkey,userId,logType,tokenid);
        uploadFile(realUrl,uploadFilePath);
    }

    public static void writeLog(String path, String log){
        try {
            File resultDetailsfile = new File(path);
            if (!resultDetailsfile.exists()) {
                System.out.println("file does not exists");
                resultDetailsfile.createNewFile();
            }
            FileOutputStream foDetails = new FileOutputStream(resultDetailsfile,false);
            foDetails.write(log.getBytes());
            foDetails.flush();
            foDetails.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
