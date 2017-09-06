package cn.musicget.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 万洪基 on 2017/3/19.
 */
public class PageUtil {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();


    public static String doubanReadRegex(String targetString,String prefix,String suffix){
        Pattern pattern = Pattern.compile(prefix+".+"+suffix);
        Matcher matcher = pattern.matcher(targetString);
        String text = "";
        if (matcher.find()){
            text = matcher.group();
            text = text.replaceAll(prefix,"");
            text = text.replaceAll(suffix,"");
        }else{
            text = null;
        }
        return text.trim();
    }

    public static void stopRandomTime(int min,int max){
        Random random = new Random();
        long randomTimes = (random.nextInt(10)*(max - min) + min)/10;
        System.out.println("本次线程沉睡\t"+randomTimes+"\ts");
        try {
            Thread.sleep(randomTimes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getUserAgentList(String fileName) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        String lineText;
        while ((lineText = bufferedReader.readLine()) != null){
            stringArrayList.add(lineText.replaceAll("User-Agent:","").trim());
        }
        return stringArrayList;
    }

    public static String getValueByKeyInHtml(String src, String key) {
        Pattern pattern = Pattern.compile("(?:" + key + "\\s*=\\s*)" + "['\"](.*?)['\"]");
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group().replaceAll(key + "\\s*=\\s*", "").replaceAll("\"", "");
        }
        return null;
    }

    public static String getRequestName(String url){
        String []urls =  url.split("/");
        return urls[urls.length-1];
    }

    public static String sendRequest(String url) throws IOException {
        HttpGet httpGet=new HttpGet(url);
        httpGet.setHeader("Host","comment.kuwo.cn");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        return EntityUtils.toString(httpClient.execute(httpGet).getEntity(),"utf-8");
    }

    public static void toFile(String txt,String filename) throws IOException {
        File file=new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        fileOutputStream.write(txt.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    @Test
    public void test1(){
        final String string = "12";
    }

}
