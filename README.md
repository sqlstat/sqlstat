# sqlstat

## 使用方法
- 执行打包命令  mvn clean package
- 打包后的jar包在target文件夹下
- 执行java命令运行该jar文件 sqlstat-1.0.1-all.jar  
例如: -Xms1g -Xms1g -XX:_PrintGCDetails -XX:+PrintGCTieStamps -XX:+PrintHeapAtGC -XLogger:logs/gc.log -classpath sqlstat-1.0.1-all.jar com.gk.sqlstat.AppStart
- 配置文件如需自定义，可在主类后追加自定义配置文件路径，注意需要在路径前加 file:  
例如：file:D://customized.properties  


## change log
repo cheange to gitee~ 
v1.0.1
1.增加ibatis xml解析类，匹配sql数量

v1.0.0
1.通用文本扫描匹配正则表达试，识别需要更换sql，规则在appication.properties统一设置
2.文本输出
