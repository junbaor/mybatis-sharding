package com.junbaor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by junbaor on 2017/2/9.
 */
public class SQLParseTest extends AppTest {

    @Test
    public void test() {
        //String sql = "SELECT  *  FROM  USERS";
        String sql = "SELECT * FROM users as u left join orders as o on o.id = u.id left join orders as w on w.id = o.id";
        String dbType = JdbcConstants.MYSQL;

        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        System.out.println("size is:" + stmtList.size());

        for (SQLStatement sqlStatement : stmtList) {
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);

            Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
            Set<TableStat.Name> names = tabmap.keySet();

        }

    }
}
