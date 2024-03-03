# RELEASE 说明

# 📗 Links | 链接

# ⭐ New Features | 新功能

# 🐞 Bug Fixes | 漏洞修补

# 📔 Documentation | 文档

# 🔨 Dependency Upgrades | 依赖项升级

# ❤ Contributors | 贡献者

---

# 项目 RELEASE 说明

发布新版时，如：从 `0.0.1-SNAPSHOT` 快照版 发布正式版 `0.0.1` 时

1. 使用命令修改为新版本号：
    ```shell
    mvn versions:set -DnewVersion=0.0.1
    mvn versions:set -DnewVersion=0.0.1 -pl nacos
    mvn versions:set -DnewVersion=0.0.1 -pl spring-cloud-xuxiaowei-dependencies
    ```
2. 修改流水线 [.gitlab-ci.yml](.gitlab-ci.yml) 中使用的版本号
3. 提交修改后的新版本号代码
4. 在新提交中新建标签
5. 将版本号修改为下一个快照 `0.0.2-SNAPSHOT`，修改的文件位置与上述相同
6. 提交修改后的下一个快照代码
7. 推送代码与标签
8. 流水线测试快照代码
9. 流水线发布标签到中央仓库

---

# 版本说明

- 快照版
    - SNAPSHOT
- 内侧版
    - alpha
- 公测版
    - bate
- 里程碑版
    - M
- 候选版
    - RC
- 正式版
    - GA
    - RELEASE
    - Stable
    - 无后缀名
