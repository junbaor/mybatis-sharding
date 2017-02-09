# mybatis-sharding
### 简介
一个简单的分表插件, 通过 mybatis 拦截器对原 SQL 进行改写.
利用 druid 数据源中的 sql 解析模块识别表名加后缀, 支持增、删、改、关联查询、子查询等其他常见操作.
典型的应用场景就是相同业务的表都要进行分表且后缀相同.

    
### 使用方式
```java
SharingUtils.setSuffix("01");   //设置表后缀
ordersMapper.selectByPrimaryKey(1);   //DB 查询操作
```
如果不明确设置后缀将不进行任何改写

### 配置方式
```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mapperLocations">
        <list>
            <value>classpath*:mybatis/**/*.xml</value>
        </list>
    </property>
    <property name="plugins">
        <array>
            <bean class="com.junbaor.sharding.share.SharingInterceptor">
                <property name="properties">
                    <value>
                        dbType=mysql
                        ignoreTable=demo_ignore
                    </value>
                </property>
            </bean>
        </array>
    </property>
</bean>
```
### 配置项
`dbType` 配置暂时不生效
拦截器中可通过 `ignoreTable` 设置忽略的表名, SQL 发现有忽略的表名将不进行改写. 
    
### 注意事项
- 不允许出现列名和表名一致的情况 
- 一条 sql 中如果出现同一个物理表名,表名的写法应相同,例如 `user` 后不应再次出现 `User` 或 `uSEr` 等其他形式 
 
### TODO
- 支持多种数据库
- 通过配置支持多种改写规则
