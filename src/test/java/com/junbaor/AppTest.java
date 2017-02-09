package com.junbaor;

import com.junbaor.sharding.dao.BaseDao;
import com.junbaor.sharding.dao.OrdersMapper;
import com.junbaor.sharding.entity.Orders;
import com.junbaor.sharding.share.SharingUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class AppTest {

    @Autowired
    private BaseDao baseDao;
    @Autowired
    private OrdersMapper ordersMapper;

    @Test
    public void test() {
        Orders orders = new Orders();
        orders.setId(1);
        orders.setInfo("info");
        SharingUtils.setSuffix("01");
        ordersMapper.insert(orders);

        SharingUtils.setSuffix("02");
        ordersMapper.deleteByPrimaryKey(1);

        orders.setInfo("info2");
        ordersMapper.updateByPrimaryKey(orders);

        SharingUtils.setSuffix("01"); //设置表后缀
        ordersMapper.selectByPrimaryKey(1);
    }

    @Test
    public void testSubQuery() {
        SharingUtils.setSuffix("03");
        baseDao.getSqlSession().selectList("getSubQuery");
    }
}
