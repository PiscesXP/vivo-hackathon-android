# vivo-hackathon-android
## 仓库使用约定：
- 开发时不要直接push到master上（只在自己的分支上修改）
- 整合时会把大家的分支merge到master上
- 不要上传IDE配置等等文件，设置自己的.gitignore
## 代码分析
使用Sonarqube，可以查看分析出来的bug/漏洞。
### 在根目录运行：
```Shell
.\gradlew sonarqube 
```
### 查看结果：
[sonarqube页面](http://aliyun.piscesxp.xyz:9000/dashboard?id=vivo-hackathon-android)
