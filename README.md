# transaction
分布式事务学习项目

# 项目介绍
    主要通过两阶段提交的思路解决服务间调用产生的事务问题

# 模板介绍
- transaction-server1 请求发调用方
- transaction-server2 请求被调用方
- transaction-txmanager 事务管理器

# 启动流程
  1. 修改transaction-server服务中的数据库配置文件
  2. 启动transaction-txmanager项目，运行TxManagerMain类中的main方法
  3. 分别启动transaction-server1和transaction-server2项目
  4. 调用transaction-server1中controller中DemoController的测试请求

# 代码设计思路

    想要控制各个本地事务提交或者回滚，必须要做到以下两点
    
 - 拿到事务控制权
 - 各个服务实时通信
    

## 拿到事务控制权
    查看spring源码发现，事务控制的接口是java.sql.Connection，而获取到具体实例的方法是javax.sql.DataSource#getConnection()
    所以我自定义了一个Connection来控制事务，并通过AOP让spring使用我自定义的Connection
    
    在YhyConnection类中，只是重写了commit和rollback方法，其他的方法使用了spring的默认实现  
    
    通过AOPorg.yu.study.transactionserver1.transaction.aspect.YhyDataSourceAspect让spirng使用我们自定义的事务控制类
    
## 各个服务实时通信
    使用netty实现各个服务之间的通信，若每个服务事务都执行成功，则通过netty服务端通知各事务提交，若有一个服务的本地事务执行失败，则通知所有的服务回滚事务

# 执行流程
1. transaction-server1启动一个分布式事务
2. 创建一个事务组id和transaction-server1的本地事务
3. 将事务组id和本地事务注册到netty服务端，执行本地事务，然后执行本地事务（本地事务提交阻塞）
4. 通过http拦截器将事务组信息传递给transaction-server2服务
5. transaction-server2服务收到请求后，创建本地事务id，然后将本地事务注册到netty服务端，然后执行本地事务（本地事务提交阻塞）
6. 两个服务的本地事务执行完毕后，想netty服务端发送当前执行结果
7. 服务端收到所有事务的执行结果后开始判断，如果全是commit状态，则发送提交命令，各服务的事务提交方法被唤醒，反之个服务的回滚方法被唤醒
8. 至此，实现了一个简单的分布式事务

# 写在最后
1. 仅用于学习用途，未做性能压测和各种异常流程处理，不可应用于生产
2. 后续迭代方向：集成springboot开箱即用，性能优化，部分异常流程处理
