package com.huanlin.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.huanlin.apiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.huanlin.apiclientsdk.utils.SignUtils.getSign;


/**
 * 调用第三方接口客户端
 */
public class ApiClient {

    private  String accessKey;

    private  String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        //可以单独传入http参数 这样参数会自动做URL编码，拼接在URL中
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String res = HttpUtil.get("http://localhost:8123/api/name/getname", paramMap);
        System.out.println(res);
        return res;
    }
    public String getNameByPost(String name){
        //可以单独传入http参数 这样参数会自动做URL编码，拼接在URL中
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String res = HttpUtil.post("http://localhost:8123/api/name/postname", paramMap);
        System.out.println(res);
        return res;
    }
    private Map<String,String>  addHeader(String body){
        HashMap<String, String> header = new HashMap<>();
        header.put("accessKey",accessKey);
        //不能直接发送给后端
//        header.put("secretKey",secretKey);
        //随机码 防止重放攻击
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("body",body);
        //时间戳 防止重新发送年龄过于久远的请求
        header.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        //签名认证
        header.put("sign",getSign(body,secretKey));
        return  header;
    }


    public String getUserByPost( User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8123/api/name/user")
                              .body(json)
                              .addHeaders(addHeader(json))
//                              .charset(StandardCharsets.UTF_8)

                              .execute();
        System.out.println(response.getStatus());
        return response.body();
    }
}
