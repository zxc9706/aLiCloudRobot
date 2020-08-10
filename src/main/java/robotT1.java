import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.taobao.api.ApiException;

import java.util.Arrays;

/**
 * @Desc 钉钉机器人报警接入
 * 自定义关键词
 * @Author zxc
 * @DateTime 2020/8/5 1:38 下午
 */
public class robotT1 {

    /**
     * curl 'https://oapi.dingtalk.com/robot/send?access_token=cec2b2aab7f308d5a1a65b2cd855daad64be4f5ea1cbdfcd3124fca2300e7943' -H 'Content-Type: application/json' -d '{"msgtype": "text","text": {"content": "robot 我就是我, 是不一样的烟火","sign":"SEC1ec368a1facba0a84eb80c38cf7c653766f1567bba662cd6d091583be88be1a5"}'
     *  sign not match
     */

    public static void main(String[] args) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=cec2b2aab7f308d5a1a65b2cd855daad64be4f5ea1cbdfcd3124fca2300e7943");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
//        request.setMsgtype("text");
//        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
//        text.setContent("robot 哈哈");
//        request.setText(text);
//        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
////        at.setAtMobiles(Arrays.asList("13645368976"));
//        at.setIsAtAll(true);
//        request.setAt(at);



        /*request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://www.dingtalk.com/");
        link.setPicUrl("");
        link.setTitle("时代的火车向前开");
        link.setText("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
        request.setLink(link);*/

        request.setMsgtype("markdown");

        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("杭州天气");
        markdown.setText("#### robot @156xxxx8827\n" +
                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
                "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n"  +
                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
        request.setMarkdown(markdown);

        try {
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMarkdown(OapiRobotSendRequest request) {
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("好消息！好消息！");
        markdown.setText(
                "#### 杭州天气 @156xxxx8827\n> 9度，西北风1级，空气良89，相对温度73%\n\n"
                        + "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n"
                        + "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");

        request.setMsgtype("markdown");
        request.setMarkdown(markdown);
    }
}
