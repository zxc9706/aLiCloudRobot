
import Result.Result;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @Author zxc
 * 加签方式
 * @DateTime 2020/8/5 1:45 下午
 */
public class robotT2 {

    private static String url = "https://oapi.dingtalk.com/robot/send?access_token=cec2b2aab7f308d5a1a65b2cd855daad64be4f5ea1cbdfcd3124fca2300e7943";
    private final static String secret = "SEC99fd37d6e60318d232407f560897af47f04d1b469beacc0ad8b1285b824a2b69";

    private static final Logger logger = LoggerFactory.getLogger(Exception.class);

    public static void main(String[] args) throws Exception {
        Long timestamp = System.currentTimeMillis();
        try {
            String result = "";
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("markdown");
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setTitle("代码审核结果");
            markdown.setText("hahahahahah");
            request.setMarkdown(markdown);
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            url = url.concat("&timestamp=" + timestamp + "&sign=" + sign);

            Result<String> stringResult = HttpClientManager.getInstance().sendPostByJSON(url, JSON.toJSONString(request));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
