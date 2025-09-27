#!/bin/bash
# jdk 路径
jdk_path="/xx/xx/openJDK21/jdk-21.0.2/bin/java"
# jvm 参数 -XX:+UseZGC -XX:+ZGenerational
jvm_options="-XX:MetaspaceSize=640m -XX:MaxMetaspaceSize=640m -Xms4096m -Xmx4096m"
# jar 名称
jar_name="xxxxx.jar"
# 进程id
pid=

# 启动
start() {
    # 获取进程id
    getpid
    # 如果进程id存在，且对应的文件夹也存在
    if [ "$pid" != "" ] && [ -d /proc/$pid ]; then
        echo "$jar_name 正在运行，进程id是 $pid"
    else
        echo "启动：$jar_name"
        nohup $jdk_path $jvm_options -jar $jar_name --spring.profiles.active=test > /dev/null 2>&1 &
    fi
}

# 停止
stop() {
    # 获取进程id
    getpid
    # 如果进程id存在，且对应的文件夹也存在
    if [ "$pid" != "" ] && [ -d /proc/$pid ]; then
        # 杀死进程id
        kill -KILL $pid >/dev/null 2>&1
        # 睡眠10秒，单位微秒
        usleep 100000
        echo "$jar_name 已停止"
    else
        echo "$jar_name 未运行"
    fi
}

# 重启
restart() {
    stop
    start
}

# 进程id函数,获取进程id
getPid() {
 pid=`ps -ef|grep "$jar_name"|grep -v grep|awk '{print $2}'`
}

# 启动参数
case "$1" in
    start)
        echo "------------------------执行启动------------------------"
        start
        ;;
    stop)
        echo "------------------------执行停止------------------------"
        stop
        ;;
    restart)
        echo "------------------------执行重启------------------------"
        stop
        start
        ;;
    status)
        echo "------------------------执行查看------------------------"
        getPid
        if [ "$pid" != "" ] && [ -d /proc/$pid ]; then
            echo "$jar_name 正在运行，进程id是 $pid"
        else
            echo "$jar_name 已停止"
        fi
        ;;
    *)
        echo $"参数提示: $0 {start|stop|restart|status}"
esac
