# 企业微信通知器
用于产品构建后，人员能收到企业微信的相关通知

## 如何使用
注：
保证本机已正常配置jdk以及maven的环境
```
mvn -version
```

1.创建企业微信应用,记录corpsecret以及agentid

2.记录企业微信的corpid

3.下载代码,在工作目录执行```mvn package```打包,进入工作路径下的target目录,查看是否正确的生成wechat-notifications.hpi

4.打开Jenkins,进入系统 > 插件管理 > 高级,选择该target目录下的wechat-notifications.hpi,点击上传安装

6.安装重启结束后,进入系统 > 系统设置,新增wechat installation配置项,且测试配置的企业微信corpid,corpsecret,agentid是否能正常连接

7.在每一个job构建后,可以选择"微信配置通知器",将上一步添加的配置加入后,填入人员ID,就可以在企业微信中收到构建信息了

8.关于安装包以及测试报告的高级配置项
如果需要安装包下载或测试报告,则可以在对应的高级项中填写<br>
注意：
安装包填写的$DATE,构建后会自动转义成对应的年月日,例如20010101<br>
测试报告填写的$BUILD_ID,构建后会自动转移成构建ID,例如30


