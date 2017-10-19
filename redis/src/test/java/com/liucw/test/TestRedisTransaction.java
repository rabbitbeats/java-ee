package com.liucw.test;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Arrays;
import java.util.Set;

/**
 * @author liucw
 * @version 1.0
 * @date 2017/10/19
 */
public class TestRedisTransaction {
    Jedis jedis;

    @Before
    public void before() {
        jedis = new Jedis("127.0.0.1", 6379);
        //权限认证
        jedis.auth("foobared");
        System.out.println(jedis.ping());
    }

    /**
     * 通俗点讲，watch命令就是标记一个键，
     * 如果标记了一个键，在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中重新再尝试一次。
     */
    @Test
    public void testWatch() throws InterruptedException {
        boolean retValue = this.transMethod();
        System.out.println("main retValue-------: " + retValue);
    }

    /**
     * 首先标记了键balance，然后检查余额是否足够，
     * 不足就取消标记，并不做扣减；足够的话，就启动事务进行更新操作，
     * 如果在此期间键balance被其它人修改， 那在提交事务（执行exec）时就会报错，
     * 程序中通常可以捕获这类错误再重新执行一次，直到成功。
     */
    private boolean transMethod() throws InterruptedException {
        int balance;// 可用余额
        int debt;// 欠额
        int amtToSubtract = 10;// 实刷额度

        jedis.watch("balance");
        //jedis.set("balance","5");//此句不该出现，讲课方便。模拟其他程序已经修改了该条目
        // Thread.sleep(7000);
        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("modify");
            return false;

        } else {
            System.out.println("***********transaction");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("debt", amtToSubtract);
            transaction.exec();
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));

            System.out.println("*******" + balance);
            System.out.println("*******" + debt);
            return true;
        }
    }

    @Test
    public void testExecAndDiscard() {
        Transaction transaction = jedis.multi();
        transaction.set("test_exec_k1", "test_exec_v1");
        transaction.set("test_exec_k2", "test_exec_v2");
        transaction.exec();

        transaction = jedis.multi();
        transaction.set("test_discard_k1", "test_discard_v1");
        transaction.set("test_discard_k2", "test_discard_v2");
        // 取消事务，放弃执行事务块内的所有命令。
        transaction.discard();

        Set<String> keys = jedis.keys("*");
        System.out.println(Arrays.toString(keys.toArray()));  // [test_exec_k1, test_exec_k2]
    }

}
