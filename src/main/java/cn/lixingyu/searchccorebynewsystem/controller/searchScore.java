package cn.lixingyu.searchccorebynewsystem.controller;

import cn.lixingyu.searchccorebynewsystem.entity.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rlxy93
 * @time 2019/10/05 09:53
 */
@Controller
public class searchScore {

    CookieStore cookieStore = new BasicCookieStore();
    public StringBuilder sb = new StringBuilder();

    @GetMapping(value = "/search")
    public String getScore(HttpServletRequest request, Model model) throws IOException {
        Map<String, String> map = new HashMap<String, String>();

        //这里我使用自己的校园网账号密码，写死了的
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");

        //获取查询的名字或者学号
        String name = request.getParameter("XM");
        String id = request.getParameter("XH");

        CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建httpClient实例
        HttpPost HttpPost = new HttpPost("http://authserver.cqwu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.cqwu.edu.cn%2Fpublicapp%2Fsys%2Fcjcx%2Fmodules%2Fcjcx%2FV_IT_KSCJ_CJB_QUERY.do"); // 创建httpget实例
        HttpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent

        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        CloseableHttpResponse response1 = httpClient.execute(HttpPost);
        String result = "";
        if (response1 != null) {
            HttpEntity entity = response1.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
//            System.out.println("网页内容:" + result);
        }
        String ltRegex = "LT-(.*)-cas";
        Pattern ltP = Pattern.compile(ltRegex);
        Matcher ltM = ltP.matcher(result);
        String ltRes = "";
        if (ltM.find()) {
            ltRes = ltM.group(0);
        } else {
//            System.out.println("NO MATCH");
        }
        map.put("dllt", "userNamePasswordLogin");
        map.put("execution", "e1s1");
        map.put("lt", ltRes);
        map.put("username", username);
        map.put("password", password);
        map.put("_eventId", "submit");
        map.put("rmShown", "1");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
//        System.out.println("请求参数：" + nvps.toString());

        HttpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        HttpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");


        //执行请求操作,拿到结果，查询状态码
        CloseableHttpResponse res = httpClient.execute(HttpPost);

        String result1 = "";
        if (res != null) {
            HttpEntity entity = res.getEntity();  //获取网页内容
            result1 = EntityUtils.toString(entity, "UTF-8");
            System.out.println("网页内容:" + result1);
        }
        String regex = "<a href=\"(.*?)\">";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(result1);
        String u = "";
        while (m.find()) {
            u = m.group(1);
        }
//        StringBuilder  sb = new StringBuilder (u);
//        sb.insert(sb.charAt('?')-3,"XH=201758274039&");
//        u = sb.toString();

//        --------------------登录成功--------------------------------
//        System.out.println(u);
//        send1(httpClient, "http://authserver.cqwu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.cqwu.edu.cn%2Fpublicapp%2Fsys%2Fcjcx%2Fmodules%2Fcjcx%2FV_IT_KSCJ_CJB_QUERY.do%3FXH%3D201758274039");
        send1(httpClient, u);
//        send1(httpClient, u);
        String score = "";
        if (name != null) {
            score = send2(httpClient, "http://ehall.cqwu.edu.cn/publicapp/sys/cjcx/modules/cjcx/V_IT_KSCJ_CJB_QUERY.do?XM=" + name);
        } else if (id != null) {
            score = send2(httpClient, "http://ehall.cqwu.edu.cn/publicapp/sys/cjcx/modules/cjcx/V_IT_KSCJ_CJB_QUERY.do?XH=" + id);
        }
        score = score.substring(score.indexOf('['),score.indexOf(']')+1);
        List<Student> list = new Gson().fromJson(score,new TypeToken<ArrayList<Student>>(){}.getType());
        model.addAttribute("score", list);
        httpClient.getConnectionManager().closeExpiredConnections();
        sb=new StringBuilder();
        cookieStore.clear();
        return "result";
    }

    private void send1(CloseableHttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        getCookies(httpClient, cookieStore, sb);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Host", "ehall.cqwu.edu.cn");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Pragma", "no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Cookie", sb.toString());
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        try {
            if (execute != null) {
                HttpEntity entity = execute.getEntity();  //获取网页内容
                result = EntityUtils.toString(entity, "UTF-8");
//                System.out.println("网页内容:" + result);
            }
        } catch (Exception e) {

        }
    }


    public void getCookies(CloseableHttpClient httpClient, CookieStore cookieStore, StringBuilder sb) throws IOException {
        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
//            遍历Cookies
//            System.out.println(cookies1.get(i));
//            System.out.println("cookiename==" + cookies1.get(i).getName());
//            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }
        List<Cookie> cookies = cookieStore.getCookies();
        for (int j = 0; j < cookies.size(); j++) {
            sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
        }

//        System.out.println("成功后的Cookie---->" + sb.toString());

    }

    private String send2(CloseableHttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        getCookies(httpClient, cookieStore, sb);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Host", "ehall.cqwu.edu.cn");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Pragma", "no-cache");
//        httpGet.setHeader("Origin", "http://authserver.cqwu.edu.cn");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Cookie", sb.toString());
//        httpGet.setHeader("Referer", "http://authserver.cqwu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.cqwu.edu.cn%2Fpublicapp%2Fsys%2Fcjcx%2Fmodules%2Fcjcx%2FV_IT_KSCJ_CJB_QUERY.do");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        try {
            if (execute != null) {
                HttpEntity entity = execute.getEntity();  //获取网页内容
                result = EntityUtils.toString(entity, "UTF-8");
                return result;
            }
        } catch (Exception e) {

        }
        return "";
    }

}