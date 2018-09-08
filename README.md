# spring-cloud-stream-demo
spring-cloud-stream 有用的实例,包括简单的mns-binder

## 配置ali mns properties

如果在user.home环境变量下面配置了`.aliyun-mns.properties`文件，会优先读取该文件的信息，否则读取spring的配置信息


`.aliyun-mns.properties` 配置方式如下

```
mns.accountendpoint=http://{endpoint}/
mns.accesskeyid={accesskeyid}
mns.accesskeysecret={secret}
```
