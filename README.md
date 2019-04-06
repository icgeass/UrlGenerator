# UrlGenerator

### 介绍
获取站点文件下载链接

### 使用方式
```java -jar <jarfile> <webRoot> <baseDirectory> [charset]```

### 其他
- 生成链接保存在当前目录下文件```url.<folderName>.<yyyyMMdd>.txt```中
- 链接格式 ```http://<ip>/<fileAbsolutePathRelatedWebRoot>```

### 示例

```bash
root@storage0:~# java -jar UrlGenerator-1.0.0-jar-with-dependencies.jar /var/www/html/ /var/www/html/downloads/\[2018\]\[Shingeki\ no\ Kyojin\ S3\]\[BDRIP\]\[1080P\]\[1-12\(38-49\)+SP\]/
Info: webRoot: /var/www/html
Info: baseDirectory: /var/www/html/downloads/[2018][Shingeki no Kyojin S3][BDRIP][1080P][1-12(38-49)+SP]
Info: netIp: 45.32.91.64
Info: charset: UTF-8
Info: generating...
Info: writing...
Info: success, result save to /root/url.[2018][Shingeki no Kyojin S3][BDRIP][1080P][1-12(38-49)+SP].20190406.txt
root@storage0:~# 
```
