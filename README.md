# DnsBlocker 基于hook的域名屏蔽器

## 简介

基于Hook的域名屏蔽器，无需VPN服务。可精确对不同的应用设置不同的屏蔽规则

## 使用

在lsposed管理器里启用模块并且勾选对应的应用，模块启用后，即可设置对应应用的域名屏蔽规则。

一行一个域名，匹配方式是是否包含。

假设你要屏蔽`www.baidu.com`，规则可以是`www.baidu.com`或`baidu.com`还有`baidu`

假设你要屏蔽百度和今日头条两个网站，那么规则可以是

```
baidu.com
toutiao.com
```

## 截图

![](E:\DnsBlocker\Screenshot_2023-12-06-18-25-59-151_top.weixiansen574.DnsBlocker.jpg)