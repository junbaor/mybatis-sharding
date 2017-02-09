package com.junbaor.sharding.share;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.*;

/**
 * Created by junbaor on 2017/2/8.
 */
public class SharingUtils {
    private static String dbType = JdbcConstants.MYSQL;

    public static void setSuffix(String suffix) {
        SharingInterceptor.suffix.set(suffix);
    }

    protected static Set<String> getTable(String sql) {
        //String result = SQLUtils.format(sql, dbType);
        //System.out.println(result); // 缺省大写格式

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        Set<String> tableNames = new HashSet<String>();
        if (stmtList.size() == 1) {
            SQLStatement sqlStatement = stmtList.get(0);

            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);

            Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
            Iterator<TableStat.Name> iterator = tabmap.keySet().iterator();
            while (iterator.hasNext()) {
                TableStat.Name next = iterator.next();
                tableNames.add(next.getName());
            }
        }
        return tableNames;
    }

}
