package com.liucw.test;

import com.liucw.redis.HomeworkApplication;
import com.liucw.redis.service.CacheCoreService;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

/**
 * @author liucw
 * @version 1.0
 * @date 2017/10/15
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringSecurityApplication.class)
@SpringBootTest(classes = HomeworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRedisCache {
    @Autowired
    private CacheCoreService cacheCoreService;

    // 执行顺序是：@BeforeClass ，@Before，@Test，@After，@AfterClass
    @Before
    public void before() {
        // 清理数据
        cacheCoreService.batchDelete("test_*");
    }

    // 测试主从复制
    @Test
    public void testMS() {
        Jedis jedis_M = new Jedis("127.0.0.1", 6379);
        Jedis jedis_S = new Jedis("127.0.0.1", 6380);

        jedis_S.slaveof("127.0.0.1", 6379);

        jedis_M.set("class", "1122V2");

        String result = jedis_S.get("class");
        System.out.println(result);
    }


    @Test
    public void testTransaction() {
        String redisKey;
        int expire = 10;

        Transaction transaction = cacheCoreService.multi();
        transaction.set("test_redis_k1", "test_redis_v11");
        transaction.set("test_redis_k2", "test_redis_v22");
        transaction.discard();
    }

    /**
     * Redis 键(key)命令用于管理 redis 的键。
     */
    @Test
    public void testKey() {
        String redisKey;
        int expire = 10;

        // 删除del
        redisKey = "test_del";
        cacheCoreService.set(redisKey, "liucw.cn", expire);

        // key是否存在
        boolean isExists = cacheCoreService.exists(redisKey);
        Assert.assertTrue(isExists);

        Long actual1 = cacheCoreService.delete(redisKey);
        Assert.assertEquals(1, actual1.intValue());

        isExists = cacheCoreService.exists(redisKey);
        Assert.assertFalse(isExists);

        String result = cacheCoreService.get(redisKey);
        Assert.assertEquals(null, result);

        // 设置key过期时间
        redisKey = "test_expire";
        cacheCoreService.set(redisKey, "liucw.cn");

        Long actual2 = cacheCoreService.expire(redisKey, expire);
        Assert.assertEquals(1, actual2.intValue());

        // keys
        redisKey = "test_keys";
        cacheCoreService.set(redisKey, "liucw");
        Set<String> actual3 = cacheCoreService.keys("*");
        Set<String> expected = new LinkedHashSet<>();
        expected.add("test_keys");
        expected.add("test_expire");
        Assert.assertArrayEquals(expected.toArray(), actual3.toArray());
    }

    /**
     * 1.string类型是二进制安全的。意思是redis的string可以包含任何数据。比如jpg图片或者序列化的对象 。
     * 2.string类型是Redis最基本的数据类型，一个键最大能存储512MB。
     */
    @Test
    public void stringTest() {
        String redisKey;
        int expire = 10;

        // 赋值与取值 set与get
        redisKey = "test_get";
        cacheCoreService.set(redisKey, "liucw.cn", expire);
        String result = cacheCoreService.get(redisKey);
        Assert.assertEquals("liucw.cn", result);

        /* 操作数字 */
        // 1.递增数字
        redisKey = "test_incr";
        Long result2 = cacheCoreService.incr(redisKey);
        Assert.assertEquals(1, result2.intValue());

        redisKey = "test_incr2";
        cacheCoreService.set(redisKey, 20, expire);
        result2 = cacheCoreService.incr(redisKey);  // 21
        Assert.assertEquals(21, result2.intValue());

        // 2.增加指定的整数
        redisKey = "test_incrBy";
        Long result3 = cacheCoreService.incrBy(redisKey, 20);  // 20
        Assert.assertEquals(20, result3.intValue());

        // 3.增加指定浮点数
        redisKey = "test_incrByFloat";
        Double result4 = cacheCoreService.incrByFloat(redisKey, 2.700000000);  // 2.7 后跟的 0 会被移除
        Assert.assertEquals(2.7, result4.intValue(), 1);

        // 4.减少指定的整数
        redisKey = "test_decr";
        Long result5 = cacheCoreService.decr(redisKey); // -1
        Assert.assertEquals(-1, result5.intValue());

        // 向尾部追加值
        redisKey = "test_append";
        cacheCoreService.set(redisKey, "my", expire);
        Long result6 = cacheCoreService.append(redisKey, "liucw"); // 7,  my(2) + liucw(5)
        System.out.println(String.format("%s, --> result=%s", redisKey, result6));

//        String result7 = cacheCoreService.get(redisKey);
//        System.out.println(String.format("%s, --> result=%s", redisKey, result7));

        // 解决请求多次问题
        redisKey = "setnx";
        Long setnxReturn = cacheCoreService.setnx(redisKey, new Date(), expire);
        Assert.assertEquals(1, setnxReturn.intValue());
        if (setnxReturn == 0) {
//            LOGGER.warn("调用该接口时，setNxKey={}已经存在，即被处理过了", redisKey);
            return;
        }
    }

    /**
     * Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。
     * Redis 中每个 hash 可以存储 2^32 - 1 键值对（40多亿）。
     */
    @Test
    public void testHash() {
        String redisKey;
        int expire = 10;

        // 赋值与取值 hset与hget
        redisKey = "test_hset";
        Long actual1 = cacheCoreService.hset(redisKey, "google", "www.google.cn");
        Assert.assertEquals(1, actual1.intValue());

        String actual2 = cacheCoreService.hget(redisKey, "google");
        Assert.assertEquals("www.google.cn", actual2);

        // 赋值与取值 hmset与hmget
        redisKey = "test_hmset";
        Map<String, String> hash = new HashMap<>();
        hash.put("google2", "www.google.cn");
        hash.put("baidu2", "www.baidu.cn");
        hash.put("weibo2", "www.weibo2.cn");
        String actual3 = cacheCoreService.hmset(redisKey, hash);
        Assert.assertEquals("OK", actual3);

        List<String> actual4 = cacheCoreService.hmget(redisKey, "google2", "baidu2");
        String[] expected = {"www.google.cn", "www.baidu.cn"};
        Assert.assertArrayEquals(expected, actual4.toArray());

        // hgetAll
        Map<String, String> actual5 = cacheCoreService.hgetAll(redisKey);
        System.out.println("hgetAll actual5-->" + JSON.toJSONString(actual5));

        // hdel
        Long actual6 = cacheCoreService.hdel(redisKey, "google2", "weibo2");
        Assert.assertEquals(2, actual6.intValue());

        actual5 = cacheCoreService.hgetAll(redisKey);
        System.out.println("hgetAll actual5-->" + JSON.toJSONString(actual5));
    }

    /**
     * Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）
     * 一个列表最多可以包含 2^(32 - 1) 个元素 (4294967295, 每个列表超过40亿个元素)。
     */
    @Test
    public void testList() {
        String redisKey;
        int expire = 10;

        // rpush向列表右边增加元素
        redisKey = "test_rpush";
        Long result1 = cacheCoreService.rpush(redisKey, "liucw");
        Assert.assertEquals(1, result1.intValue());

        Long result2 = cacheCoreService.rpush(redisKey, ".cn");
        Assert.assertEquals(2, result2.intValue());

        // 返回列表的长度
        Long len = cacheCoreService.llen(redisKey);
        Assert.assertEquals(2, len.intValue());

        // 返回列表所有的数据
        List<String> result = cacheCoreService.lrange(redisKey, 0, -1);
        String[] expecteds = {"liucw", ".cn"};
        Assert.assertArrayEquals(expecteds, result.toArray());


        // 向列表左边(头部)增加元素
        redisKey = "test_lpush";
        Long result3 = cacheCoreService.lpush(redisKey, "liucw");
        Assert.assertEquals(1, result3.intValue());
        cacheCoreService.lpush(redisKey, ".cn");

        result = cacheCoreService.lrange(redisKey, 0, -1);
        String[] expecteds2 = {".cn", "liucw"};
        Assert.assertArrayEquals(expecteds2, result.toArray());

        //  Lpop 命令用于移除并返回列表的第一个元素。
        // 在test_lpush基础上测试
        String result4 = cacheCoreService.lpop(redisKey);
        Assert.assertEquals(".cn", result4);

        //  rpop 命令用于移除并返回列表的第一个元素。
        // 在test_lpush基础上测试
        String result5 = cacheCoreService.rpop(redisKey);
        Assert.assertEquals("liucw", result5);

    }

    /**
     * Redis 有序集合和集合一样也是string类型元素的集合,且不允许重复的成员。
     * 不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
     * 有序集合的成员是唯一的,但分数(score)却可以重复。
     * 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。 集合中最大的成员数为 2^(32 - 1) (4294967295, 每个集合可存储40多亿个成员)。
     */
    @Test
    public void testSortedSet() {
        String redisKey;
        int expire = 10;

        // Zadd
        redisKey = "test_zadd";
        Long result1 = cacheCoreService.zadd(redisKey, 20, "liucw");
        Assert.assertEquals(1, result1.intValue());

        Long result2 = cacheCoreService.zadd(redisKey, 10, ".cn");
        Assert.assertEquals(1, result2.intValue());

        // zcard 在上面的基础上测试
        Long actual1 = cacheCoreService.zcard(redisKey);
        Assert.assertEquals(2, actual1.intValue());

        // zrange  递增排列
        Set<String> result3 = cacheCoreService.zrange(redisKey, 0, -1);
        Set<String> result4 = new LinkedHashSet<>();
        result4.add(".cn");
        result4.add("liucw");
        Assert.assertEquals(result4, result3);

        // zrevrange  递减排列
        result3 = cacheCoreService.zrevrange(redisKey, 0, -1);
        Set<String> result6 = new LinkedHashSet<>();
        result6.add("liucw");
        result6.add(".cn");
        Assert.assertEquals(result6, result3);

        // Zrank返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
        // 从0开始
        Long actual2 = cacheCoreService.zrank(redisKey, ".cn");
        Assert.assertEquals(0, actual2.intValue());

        //zrevrank 从大到小
        Long actual3 = cacheCoreService.zrevrank(redisKey, ".cn");
        Assert.assertEquals(1, actual3.intValue());
    }
}
