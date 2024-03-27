<div align="center" style="text-align: center;">
    <h1>spring-cloud-xuxiaowei</h1>
    <h3>徐晓伟微服务</h3>
    <h4>支持 Spring Boot 2、3 最新版</h4>
    <a target="_blank" href="https://github.com/996icu/996.ICU/blob/master/LICENSE">
        <img alt="License-Anti" src="https://img.shields.io/badge/License-Anti 996-blue.svg">
    </a>
    <a target="_blank" href="https://996.icu/#/zh_CN">
        <img alt="Link-996" src="https://img.shields.io/badge/Link-996.icu-red.svg">
    </a>
    <a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=ZieC6s1WB4njfVbrDHYgoNS8YpT26VtF&jump_from=webapi">
        <img alt="QQ群" src="https://img.shields.io/badge/QQ群-696503132-blue.svg"/>
    </a>
    <a target="_blank" href="https://app.codacy.com/gh/xuxiaowei-cloud/spring-cloud-xuxiaowei/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
        <img alt="Codacy Badge" src="https://app.codacy.com/project/badge/Grade/3d8354e4593c4fbea4ad325910b32ada"/>
    </a>
    <a target="_blank" href="https://codecov.io/gh/xuxiaowei-cloud/spring-cloud-xuxiaowei">
        <img alt="Codecov Badge" src="https://codecov.io/gh/xuxiaowei-cloud/spring-cloud-xuxiaowei/graph/badge.svg?token=XS72L4GIAX"/> 
    </a>
</div>

<p></p>

<div align="center" style="text-align: center;">
    <a target="_blank" href="https://work.weixin.qq.com/gm/75cfc47d6a341047e4b6aca7389bdfa8">
        <img alt="企业微信群" src="static/wechat-work.jpg" height="100"/>
    </a>
</div>

<p></p>

## 项目说明

1. 本项目为重构项目，与原始项目存在较大差异
2. 本项目从头开发，在 [B站](https://www.bilibili.com/) 有配套视频（充电专属）
    1. 搜索 [重构微服务](https://search.bilibili.com/video?keyword=%E9%87%8D%E6%9E%84%E5%BE%AE%E6%9C%8D%E5%8A%A1)
    2. 查看 [充电专属视频](https://space.bilibili.com/198580655)
3. 表结构、数据（包含 Nacos 配置）在 [sql](sql)
4. [CodeCov 代码覆盖率](https://app.codecov.io/gh/xuxiaowei-cloud/spring-cloud-xuxiaowei/)
5. 原始项目地址:

|               | Gitee                                                  | GitHub                                                  |
|---------------|--------------------------------------------------------|---------------------------------------------------------|
| Spring Boot 2 | https://gitee.com/xuxiaowei-cloud/xuxiaowei-cloud      | https://github.com/xuxiaowei-cloud/xuxiaowei-cloud      |
| Spring Boot 3 | https://gitee.com/xuxiaowei-cloud/xuxiaowei-cloud-next | https://github.com/xuxiaowei-cloud/xuxiaowei-cloud-next |

## 项目开发的原则

### 分支

1. 功能同时更新 `spring-boot-2`、`spring-boot-3` 两个分支
2. 先开发 `spring-boot-2` 分支，然后使用 `git cherry-pick`(`摘樱桃`) 将功能同步到 `spring-boot-3` 分支，并根据代码内容做调整

### 依赖

1. Spring 全家桶（不使用 `Shiro` 等其他安全框架）
    1. Spring Security
    2. Spring Security OAuth2 Authorization Server（OAuth 2.1）
2. 尽量少的使用其他依赖
    1. 使用 `springdoc` 生成 API 文档：支持 `OpenAPI 3`、`Swagger-ui`、`OAuth 2.1` 等
    2. JSON 处理使用 `jackson`，不使用 `fastjson`、`hutool` 等
    3. 连接池使用 Spring Boot 默认的 `hikari`，不使用 `druid`
    4. 其他工具类使用 `org.apache.commons:commons-lang3` 和 `com.google.guava:guava`，不使用 `hutool` 等
3. 已上依赖不能满足的情况，自己写，并且包含完善的测试类
4. 依赖保存最新
    1. 使用 [dependabot.yml](.github/dependabot.yml) `检测依赖升级`、`创建升级依赖PR`，`执行自动化测试`，`人工审核`，
       通过后合并到目标分支

### 代码格式

1. 使用 [spring-javaformat](https://github.com/spring-io/spring-javaformat/)
    1. Spring 提供：IntelliJ IDEA、Visual Studio Code、Eclipse 插件

### 功能

1. 每开发一个功能，要包含详细的自动化测试
2. 每发现一个bug，修复时要包含详细的自动化测试，测试内容包含能复现 bug 的测试
3. [十二要素应用宣言](https://12factor.net/zh_cn/)
    1. [I. 基准代码](https://12factor.net/zh_cn/codebase) 一份基准代码（Codebase），多份部署（deploy）
    2. [II. 依赖](https://12factor.net/zh_cn/dependencies) 显式声明依赖关系
    3. [III. 配置](https://12factor.net/zh_cn/config) 在环境中存储配置
    4. [V. 构建，发布，运行](https://12factor.net/zh_cn/build-release-run) 严格分离构建和运行
    5. [X. 开发环境与线上环境等价](https://12factor.net/zh_cn/dev-prod-parity) 尽可能的保持开发，预发布，线上环境相同
4. 测试：包含自动化创建 MySQL（以及数据库表结构和数据初始化）、Redis、Maven 构建与测试
    1. 支持 [GitLab 流水线](.gitlab-ci.yml)
       [自动化测试](https://jihulab.com/xuxiaowei-jihu/xuxiaowei-cloud/spring-cloud-xuxiaowei/-/pipelines?page=1&scope=all&ref=spring-boot-2)
    2. 支持 [GitHub 流水线](.github/workflows/maven-spring-boot-2.yml)
       [自动化测试](https://github.com/xuxiaowei-cloud/spring-cloud-xuxiaowei/actions/workflows/maven-spring-boot-2.yml)
    3. 支持 [Jenkins 流水线](Jenkinsfile)
    4. 测试开始时，创建一个空白的数据库，自动化脚本创建数据库与表结构，导入基础数据，然后再打包，运行单元测试、集成测试
5. 评价一个项目是否优秀的其中一个因素：在不修改基础代码和基础数据的情况下，是否可以随时开源

## 项目功能

### OAuth 2.1

- 基于 OAuth 2.1 的 0.4.5 标签默认示例配置
    - 其中 `bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=` 是 `客户ID:客户秘钥` `messaging-client:secret` 计算 base64 得到的

| 名称              | 值                                                                                                                                                                                                                                                                                                  | 自动化测试                                                                                                                                   |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| 用户名             | user1                                                                                                                                                                                                                                                                                              |                                                                                                                                         |
| 密码              | password                                                                                                                                                                                                                                                                                           |                                                                                                                                         |
| 客户ID            | messaging-client                                                                                                                                                                                                                                                                                   |                                                                                                                                         |
| 客户秘钥            | secret                                                                                                                                                                                                                                                                                             |                                                                                                                                         |
| 客户认证方式          | client_secret_basic                                                                                                                                                                                                                                                                                |                                                                                                                                         |
| 客户授权方式          | authorization_code、refresh_token、client_credentials                                                                                                                                                                                                                                                |                                                                                                                                         |
| 客户回调地址          | http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc , http://127.0.0.1:8080/authorized , https://home.baidu.com/home/index/contact_us                                                                                                                                                    |                                                                                                                                         |
| 客户授权范围          | openid、profile、message.read、message.write                                                                                                                                                                                                                                                          |                                                                                                                                         |
| 客户手动授权同意        | true                                                                                                                                                                                                                                                                                               |                                                                                                                                         |
| 授权请求 URL        | http://127.0.0.1:9000/oauth2/authorize?client_id=messaging-client&redirect_uri=https://home.baidu.com/home/index/contact_us&response_type=code&scope=openid%20profile%20message.read%20message.write&state=beff3dfc-bad8-40db-b25f-e5459e3d6ad7                                                    |                                                                                                                                         |
| 授权码获取 Token URL | curl --request POST --url http://127.0.0.1:9000/oauth2/token --header 'Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=' --header 'content-type: multipart/form-data' --form redirect_uri=https://home.baidu.com/home/index/contact_us --form grant_type=authorization_code --form code=你的授权码 | [AuthorizationCodeTests.java](spring-cloud-xuxiaowei-passport/src/test/java/cloud/xuxiaowei/passport/oauth/AuthorizationCodeTests.java) |
| 刷新 Token URL    | curl --request POST --url http://127.0.0.1:9000/oauth2/token --header 'Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=' --header 'content-type: multipart/form-data' --form grant_type=refresh_token --form refresh_token=你的刷新Token                                                          | [AuthorizationCodeTests.java](spring-cloud-xuxiaowei-passport/src/test/java/cloud/xuxiaowei/passport/oauth/AuthorizationCodeTests.java) |
| 凭证式 URL         | curl --request POST --url http://127.0.0.1:9000/oauth2/token --header 'Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=' --header 'content-type: multipart/form-data' --form scope="openid profile message.read message.write" --form grant_type=client_credentials                           | [ClientCredentialsTests.java](spring-cloud-xuxiaowei-passport/src/test/java/cloud/xuxiaowei/passport/oauth/ClientCredentialsTests.java) |

### Springdoc (API)

| Name     | 服务     | 地址                                                  | 说明               |
|----------|--------|-----------------------------------------------------|------------------|
| gateway  | 网关     | http://127.0.0.1:8000/webjars/swagger-ui/index.html | 网关聚合所有服务的 API 文档 |
| passport | 登录授权服务 | http://127.0.0.1:9000/swagger-ui/index.html         |                  |
| file     | 文件服务   | http://127.0.0.1:12000/swagger-ui/index.html        |                  |

## 参考文档

### Docker 镜像

- maven-mysql-client
    - [gitee](https://gitee.com/xuxiaowei-com-cn/maven-mysql-client)
    - [docker](https://hub.docker.com/r/xuxiaoweicomcn/maven-mysql-client/tags)
- openjdk-mysql-client
    - [gitee](https://gitee.com/xuxiaowei-com-cn/openjdk-mysql-client)
    - [docker](https://hub.docker.com/r/xuxiaoweicomcn/openjdk-mysql-client/tags)

### GitHub

- [dependabot.yml 文件的配置选项](https://docs.github.com/zh/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file)
- [自定义依赖项更新](https://docs.github.com/zh/code-security/dependabot/dependabot-version-updates/customizing-dependency-updates)
- [管理依赖项更新的所有拉取请求](https://docs.github.com/zh/code-security/dependabot/working-with-dependabot/managing-pull-requests-for-dependency-updates)

### GitLab

- [GitLab CI/CD](https://docs.gitlab.cn/jh/ci/index.html)
- [GitLab Runner](https://docs.gitlab.cn/runner/)
- [Services](https://docs.gitlab.cn/jh/ci/services/)
    - [使用 MySQL](https://docs.gitlab.cn/jh/ci/services/mysql.html)
    - [使用 Redis](https://docs.gitlab.cn/jh/ci/services/redis.html)
- [优化 GitLab CI/CD 配置文件](https://docs.gitlab.cn/jh/ci/yaml/yaml_optimization.html)

### Jenkins

- Installing Jenkins
    - [docker](https://www.jenkins.io/doc/book/installing/docker/)
    - [kubernetes](https://www.jenkins.io/doc/book/installing/kubernetes/)
    - [linux](https://www.jenkins.io/doc/book/installing/linux/)
    - [macos](https://www.jenkins.io/doc/book/installing/macos/)
    - [windows](https://www.jenkins.io/doc/book/installing/windows/)
    - [war-file](https://www.jenkins.io/doc/book/installing/war-file/)
    - [initial-settings](https://www.jenkins.io/doc/book/installing/initial-settings/)
- [pipeline](https://www.jenkins.io/doc/book/pipeline/)

### Maven

- [依赖传递性](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

### Nacos

- [GitHub](https://github.com/alibaba/nacos)
    - [mysql-schema.sql](https://github.com/alibaba/nacos/blob/2.3.0/distribution/conf/mysql-schema.sql)
- 个人编译
    - [Gitee](https://gitee.com/xuxiaowei-io/nacos)
    - [github](https://github.com/xuxiaowei-io/nacos)
    - [pipelines](https://framagit.org/xuxiaowei-io/nacos/-/pipelines)
- [编译工具](https://gitee.com/xuxiaowei-com-cn/maven-dependencies)

### OAuth 2.1

- [AuthorizationServerConfig](https://github.com/spring-projects/spring-authorization-server/blob/0.4.5/samples/default-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java)
- [DefaultSecurityConfig](https://github.com/spring-projects/spring-authorization-server/blob/0.4.5/samples/default-authorizationserver/src/main/java/sample/config/DefaultSecurityConfig.java)
- [Jwks](https://github.com/spring-projects/spring-authorization-server/blob/0.4.5/samples/default-authorizationserver/src/main/java/sample/jose/Jwks.java)
- [KeyGeneratorUtils](https://github.com/spring-projects/spring-authorization-server/blob/0.4.5/samples/default-authorizationserver/src/main/java/sample/jose/KeyGeneratorUtils.java)
- [users.ddl](https://github.com/spring-projects/spring-security/blob/main/core/src/main/resources/org/springframework/security/core/userdetails/jdbc/users.ddl)
- [oauth2-registered-client-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql)
- [oauth2-authorization-consent-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql)
- [oauth2-authorization-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql)
- [OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
    - [OAuth 2.0 Login](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html)
        - [Core Configuration](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html)
        - [Advanced Configuration](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html)
    - [OAuth 2.0 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
        - [JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
        - [OAuth 2.0 Resource Server Multi-tenancy](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/multitenancy.html)
        - [OAuth 2.0 Bearer Tokens](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/bearer-tokens.html)
    - [Add custom claims to JWT access tokens](https://docs.spring.io/spring-authorization-server/reference/guides/how-to-custom-claims-authorities.html#custom-claims)
    - [OAuth2TokenCustomizer](https://docs.spring.io/spring-authorization-server/reference/core-model-components.html#oauth2-token-customizer)
    - [Map Claims to an ID Token](https://docs.spring.io/spring-authorization-server/reference/guides/how-to-social-login.html#advanced-use-cases-map-claims)

### Spring

- [Search](https://docs.spring.io/spring-security/reference/search.html)

### Springdoc (API)

- [Spring Boot 2.x](https://springdoc.org/v1)
- [Spring Boot 3.x](https://springdoc.org)
- [demos](https://github.com/springdoc/springdoc-openapi-demos/)

### spring-javaformat

- [GitHub](https://github.com/spring-io/spring-javaformat/)
- [spring-javaformat-intellij-idea-plugin](https://repo1.maven.org/maven2/io/spring/javaformat/spring-javaformat-intellij-idea-plugin/)

### [十二要素应用宣言](https://12factor.net/zh_cn/)

## 问题

1. 无法读取 Nacos 配置 ？

   启动项目出现错误，yaml 配置文件无法正确读取

    ```
    Caused by: org.yaml.snakeyaml.error.YAMLException: java.nio.charset.MalformedInputException: Input length = 1
    ```

    1. 使用 `java -Dfile.encoding=UTF-8 -jar *.jar`
    2. 如果 Windows 控制台中文乱码，执行 `chcp 65001 && java -Dfile.encoding=UTF-8 -jar *.jar`
