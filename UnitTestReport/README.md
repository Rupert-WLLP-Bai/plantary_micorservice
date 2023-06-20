# 单元测试报告 - Allure

## 1. 安装allure
1. 使用npm安装allure-commandline
    ```shell
    npm install -g allure-commandline --save-dev
    ```
2. 配置环境变量

    将allure-commandline的bin目录添加到环境变量中，例如：
    ```shell
    C:\Users\username\AppData\Roaming\npm\node_modules\allure-commandline\dist\bin
    ```
3. 验证安装是否成功
    ```shell
    allure --version
    ```
    输出如下信息则表示安装成功：
    ```shell
    allure 2.13.8
    ```
## 2. 如何打开报告
1. circle模块

    ```shell
    cd circle
    allure open
    ```
2. UserService模块

    ```shell
    cd UserService
    allure open
    ```

## 3. 报告截图

![image-20230620231244102](https://s2.loli.net/2023/06/20/76ogdSUTKP31I4k.png)

![image-20230620231321857](https://s2.loli.net/2023/06/20/GHOlcJ8jxKNEpar.png)

![image-20230620231332781](https://s2.loli.net/2023/06/20/Ek2mUuD51ZNiasw.png)

![image-20230620231342861](https://s2.loli.net/2023/06/20/gueP2Dbn6OdQTLx.png)

![image-20230620231419105](https://s2.loli.net/2023/06/20/TiyBKJjveVAo3Um.png)

![image-20230620231430424](https://s2.loli.net/2023/06/20/Q98R1AmxhXZi3VC.png)

![image-20230620231439648](https://s2.loli.net/2023/06/20/oQlnR2XpSjyMD15.png)

![image-20230620231448209](https://s2.loli.net/2023/06/20/mSbWaiYuQjMxB1E.png)