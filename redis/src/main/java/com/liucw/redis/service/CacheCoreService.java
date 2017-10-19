package com.liucw.redis.service;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheCoreService {

    /**
     * 检查给定 key 是否存在。
     *
     * @param key
     * @return 若 key 存在返回 1 ，否则返回 0 。
     * @version >= 1.0.0
     */
    Boolean exists(String key);

    /**
     * 查找所有符合给定模式 pattern 的 key （也就是模糊查找）
     *
     * @param pattern
     * @return 符合给定模式的 key 列表 (Array)。
     * @version >= 1.0.0
     */
    Set<String> keys(String pattern);

    /**
     * 设置 key 的过期时间。key 过期后将不再可用。
     *
     * @param key
     * @param expire
     * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。
     */
    Long expire(String key, int expire);

    /**
     * 删除已存在的键。不存在的 key 会被忽略。
     *
     * @param key
     * @return 被删除 key 的数量。
     * @version >= 1.0.0
     */
    Long delete(String key);

    /**
     * 批量删除缓存，key支持正则表达式
     *
     * @param key 支持正则表达式
     */
    void batchDelete(String key);

    /**
     * ++++++++++++++++   String +++++++++++++++++++
     **/
    /**
     * 设置缓存
     *
     * @param key
     * @return
     */
    void set(final String key, final Object value);

    void set(String key, Object value, int expired);

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 读取缓存并序列化
     *
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 读取缓存并序列化List
     *
     * @param key
     * @param clazz
     * @return
     */
    <T> List<T> getBeanList(String key, Class<T> clazz);

    /**
     * 将 key 中储存的数字值增一。
     * 1.如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 2.如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 3.本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @return 执行 INCR 命令之后 key 的值。
     * @version >= 1.0.0
     */
    Long incr(String key);

    /**
     * 将 key 所储存的值加上给定的增量值（increment）
     * 1.如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令
     * 2.如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 3.本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @param increment 增量值
     * @return 加上指定的增量值之后， key 的值。
     * @version >= 1.0.0
     */
    Long incrBy(String key, long increment);

    /**
     * 将 key 所储存的值加上给定的浮点增量值（increment）
     * 1.如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作
     *
     * @param key
     * @param increment 增量值
     * @return 执行命令之后 key 的值
     * @version >= >= 2.6.0
     */
    Double incrByFloat(String key, double increment);

    /**
     * 将 key 中储存的数字值减一。
     * 1.如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 2.如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 3.本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @return 执行命令之后 key 的值。
     * @version >= 1.0.0
     */
    Long decr(final String key);


    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 1.如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 2.如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样
     *
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度。
     * @version >= 2.0.0
     */
    Long append(final String key, final String value);

    /**
     * 只有在 key 不存在时设置 key 的值。
     * 1.这个方法可以在处理接口并发时加锁，保证在指定的时间内只处理一个请求
     *
     * @param key
     * @param value
     * @param expired
     * @return 设置成功，返回 1 。 设置失败，返回 0 。
     * @version >= 1.0.0
     */
    Long setnx(String key, Object value, int expired);

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   list +++++++++++++++++++
     **/

    /**
     * 返回列表中指定区间内的元素
     * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     * @version >= 1.0.0
     */
    List<String> lrange(String key, int start, int end);

    /**
     * 获得list的长度
     * 1.如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。
     * 2.如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return 列表的长度。
     * @version >= 1.0.0
     */
    Long llen(final String key);

    /**
     * 将一个或多个值插入到列表头部(最左边)。
     * 1.如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 2.当 key 存在但不是列表类型时，返回一个错误。
     * 【注意】：在Redis 2.4版本以前的 LPUSH 命令，都只接受单个 value 值。
     *
     * @param key
     * @param strings
     * @return 列表的长度。
     * @version >= 1.0.0
     */
    Long lpush(final String key, final String... strings);

    /**
     * 将一个或多个值插入到列表的尾部(最右边)。
     * 1.如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。
     * 2.当列表存在但不是列表类型时，返回一个错误。
     * 【注意】：在 Redis 2.4 版本以前的 RPUSH 命令，都只接受单个 value 值。
     *
     * @param key
     * @param strings
     * @return 执行 rpush 操作后，列表的长度。
     * @version >= 1.0.0
     */
    Long rpush(final String key, final String... strings);

    /**
     * 移除并返回列表的第一个元素。
     *
     * @param key
     * @return 列表的第一个元素。 当列表 key 不存在时，返回 nil 。
     * @version >= 1.0.0
     */
    String lpop(final String key);

    <T> T lpop(final String key, Class<T> clazz);

    /**
     * 移除并返回列表的最后一个元素
     *
     * @param key
     * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
     * @version >= 1.0.0
     */
    String rpop(final String key);

    <T> T rpop(final String key, Class<T> clazz);

    /**
     * @param channel
     * @param message
     * @description 发布订阅
     */
    Long publish(final String channel, Object message);

    /**
     * @param listener
     * @param channel
     * @description 接受订阅
     */
    void subscribe(JedisPubSub listener, String channel);

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   zset +++++++++++++++++++
     **/

    /**
     * 将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     * 注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素。
     *
     * @param key
     * @param score
     * @param value
     * @return 被成功添加的[新成员的数量]，不包括那些被更新的、已经存在的成员。
     * @version >= 1.2.0
     */
    Long zadd(final String key, final double score, final String value);

    Long zadd(final String key, final double score, final String value, final int expired);


    /**
     * 计算集合中元素的数量。
     *
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     * @version >= 1.2.0
     */
    Long zcard(final String key);

    /**
     * 返回有序集中，指定区间内的成员。其中成员的位置按分数值递增(从小到大)来排序。具有相同分数值的成员按字典序(lexicographical order )来排列。
     * 1.如果你需要成员按值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * 2.下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 3.你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     * @version >= 1.2.0
     */
    Set<String> zrange(String key, long start, long end);

    Set<String> zrangeByScore(String key, double min, double max);

    /**
     * 递减排列
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @see #zrange
     */
    Set<String> zrevrange(String key, long start, long end);

    /**
     * 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
     *
     * @param key
     * @param member
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。 如果成员不是有序集 key 的成员，返回 nil 。
     * @version >= 2.0.0
     */
    Long zrank(String key, String member);

    /**
     * 返回有序集中指定成员的排名， 其中成员的位置按分数值递减(从大到小)来排列。
     *
     * @param key
     * @param member
     * @return
     * @version >= 2.2.0
     */
    Long zrevrank(String key, String member);

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   hash +++++++++++++++++++
     **/

    /**
     * 返回哈希表中指定字段的值。
     *
     * @param key
     * @param field
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。
     * @version >= 2.0.0
     */
    String hget(final String key, final String field);

    /**
     * 为哈希表中的字段赋值 。
     * 1.如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 2.如果字段已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0
     * @version >= 2.0.0
     */
    Long hset(final String key, final String field, final String value);

    /**
     * 同时将多个 field-value (字段-值)对设置到哈希表中。
     * 此命令会覆盖哈希表中已存在的字段。
     * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
     *
     * @param key
     * @param hash
     * @return 如果命令执行成功，返回 OK
     * @version >= 2.0.0
     */
    String hmset(final String key, final Map<String, String> hash);

    /**
     * 返回哈希表中，一个或多个给定字段的值。
     * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
     *
     * @param key
     * @param fields
     * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     * @version >= 2.0.0
     */
    List<String> hmget(final String key, final String... fields);

    /**
     * 返回哈希表中，所有的字段和值。
     * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍。
     *
     * @param key
     * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
     * @version >= 2.0.0
     */
    Map<String, String> hgetAll(final String key);

    /**
     * 删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
     *
     * @param key
     * @param fields
     * @return 被成功删除字段的数量，不包括被忽略的字段。
     * @version >= 2.0.0
     */
    Long hdel(final String key, final String... fields);

    /**
     * hash判断一个键是否存在
     *
     * @param key
     * @return
     */
    boolean hExists(String key, String lid);


    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   事务 +++++++++++++++++++
     **/

    /**
     * 用于监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断
     *
     * @param keys
     * @return 总是返回 OK 。
     * @version >= 2.2.0
     */
    String watch(final byte[]... keys);

    /**
     * 取消 WATCH 命令对[所有] key 的监视
     *
     * @return 总是返回 OK 。
     * @version >= 2.2.0
     */
    String unwatch();

    /**
     * 用于标记一个事务块的开始。
     * 事务块内的多条命令会按照先后顺序被放进一个队列当中，最后由 EXEC 命令原子性(atomic)地执行。
     *
     * @return 总是返回 OK 。
     * @version >= 1.2.0
     */
    Transaction multi();

    /**
     * 用于执行所有事务块内的命令。
     * 执行exec命令后会取消对[所有]键的监控，如果不想执行事务中的命令也可以使用UNWATCH命令来取消监控。
     *
     * @return 事务块内所有命令的返回值，按命令执行的先后顺序排列。 当操作被打断时，返回空值 nil 。
     * @version >= 1.2.0
     */
    // Transaction.exec()

    /**
     * 用于取消事务，放弃执行事务块内的所有命令。
     * @return 总是返回 OK 。
     * @version >= 2.0.0
     */
    // Transaction.discard()


}
