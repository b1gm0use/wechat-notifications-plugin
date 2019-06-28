package com.qy.wechat;

import java.util.*;

import com.qy.wechat.plugin.WechatNotifier;

import java.io.*;

/**
 * Created by Administrator on 2018/3/2.
 */
public class Messages {

    
    private static String pluginName;
    private static String startContent;
    private static String successContent;
    private static String failedContent;
    private static String sendMsgApi;
    private static String updateTokenApi;

    static {
        FileReader reader;

        InputStream inputStream = null;
        try{

            String resourceName = "Messages_zh_CN.properties";
            Properties props = new Properties();
            ClassLoader cl = Messages.class.getClassLoader();
   
            InputStream stream = cl.getResourceAsStream(resourceName);
            props.load(stream);

            pluginName = props.getProperty("WechatNotifier.pluginName");
            startContent = props.getProperty("WechatNotifier.startContent");
            successContent = props.getProperty("WechatNotifier.successContent");
            failedContent = props.getProperty("WechatNotifier.failedContent");
            sendMsgApi = props.getProperty("WechatNotifier.sendMsgApi");
            updateTokenApi = props.getProperty("WechatNotifier.updateTokenApi");

            System.out.println("=============="); 
            System.out.println(pluginName); 
            System.out.println(startContent);  
            System.out.println(successContent);  
            System.out.println(failedContent);  
            System.out.println(sendMsgApi);  
            System.out.println(updateTokenApi);  
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try{
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String WechatNotifier_pluginName() {
        return pluginName;
    }

    public static String WechatNotifier_startContent() {
        return startContent;
    }

    public static String WechatNotifier_successContent() {
        return successContent;
    }

    public static String WechatNotifier_failedContent() {
        return failedContent;
    }

    public static String WechatNotifier_sendMsgApi() {
        return sendMsgApi;
    }

    public static String WechatNotifier_updateTokenApi() {
        return updateTokenApi;
    }

    private Messages() {
 
    }
}
