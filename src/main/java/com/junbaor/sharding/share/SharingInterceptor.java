package com.junbaor.sharding.share;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SharingInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(SharingInterceptor.class);
    protected static ThreadLocal<String> suffix = new ThreadLocal<String>();
    public Set<String> ignoreTable = new HashSet<String>();


    public Object intercept(Invocation invocation) throws Throwable {
        if (suffix.get() == null) {
            return invocation.proceed();
        }

        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");

            String sql = boundSql.getSql();
            log.info("原始SQL: " + sql);

            String pageSql = overrideSQL(sql);
            log.info("改写后的SQL: " + pageSql);

            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
        }
        return invocation.proceed();
    }

    public String overrideSQL(String sql) {
        Set<String> table = SharingUtils.getTable(sql);
        for (String name : table) {
            if (ignoreTable.contains(name)) {
                continue;
            }
            sql = sql.replace(name, name + "_" + suffix.get());
        }
        suffix.remove();
        return sql;
    }


    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties properties) {
        //TODO：暂只支持 mysql
        String dbType = properties.getProperty("dbType");

        String ignoreTable = properties.getProperty("ignoreTable");
        if (ignoreTable != null) {
            String[] ignoreTables = ignoreTable.split(",");
            for (String table : ignoreTables) {
                this.ignoreTable.add(table);
            }
        }
    }
}
