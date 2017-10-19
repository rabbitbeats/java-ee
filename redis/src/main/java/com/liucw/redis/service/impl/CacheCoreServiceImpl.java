package com.liucw.redis.service.impl;

import com.liucw.redis.service.CacheCoreService;
import com.liucw.redis.utils.JedisTemplate;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CacheCoreServiceImpl implements CacheCoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheCoreServiceImpl.class);

    @Autowired
    private JedisTemplate jedisTemplate;

    @Override
    public Long expire(final String key, final int expire) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.expire(key, expire);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("expire cache fail;key=%s, expire=%s", key, expire), e);
        }
        return null;
    }

    /**
     * ++++++++++++++++   String +++++++++++++++++++
     **/
    @Override
    public void set(final String key, final Object value) {
        this.set(key, value, -1);
    }

    @Override
    public void set(final String key, final Object value, final int expired) {
        try {
            final String serValue = JSON.toJSONString(value);
            jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {

                @Override
                public void action(Jedis jedis) {
                    if (expired > 0) {
                        jedis.setex(key, expired, serValue);

                    } else if (expired < 0) {
                        jedis.set(key, serValue);
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("set cache fail; key=%s, value=%s", key, value), e);
        }
    }

    @Override
    public String get(String key) {
        return get(key, String.class);
    }

    @Override
    public <T> T get(final String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || clazz == null) {
            LOGGER.error("args error;key={}, clazz={}", key, clazz);
            return null;
        }

        String value = null;
        try {
            value = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.get(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("get cache fail;key=%s", key), e);
        }

        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    @Override
    public <T> List<T> getBeanList(final String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || clazz == null) {
            LOGGER.error("args error;key={}, clazz={}", key, clazz);
            return null;
        }

        String value = null;
        try {
            value = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.get(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("get cache fail;key=%s", key), e);
        }

        if (StringUtils.isNotBlank(value)) {
            return JSON.parseArray(value, clazz);
        }
        return null;
    }

    @Override
    public Long incr(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.incr(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("incr cache fail;key=%s", key), e);
        }
        return null;
    }

    @Override
    public Long incrBy(final String key, final long increment) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.incrBy(key, increment);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("incrBy cache fail;key=%s, increment=%s", key, increment), e);
        }
        return null;
    }

    @Override
    public Double incrByFloat(final String key, final double increment) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Double>() {

                @Override
                public Double action(Jedis jedis) {
                    return jedis.incrByFloat(key, increment);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("incrByFloat cache fail;key=%s, increment=%s", key, increment), e);
        }
        return null;
    }

    @Override
    public Long decr(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.decr(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("decr cache fail;key=%s", key), e);
        }
        return null;
    }

    @Override
    public Long append(final String key, final String value) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.append(key, value);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("append cache fail;key=%s, value=%s", key, value), e);
        }
        return null;
    }

    @Override
    public Long setnx(final String key, final Object value, final int expired) {
        try {
            final String serValue = JSON.toJSONString(value);
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    if (expired > 0) {
                        Long setnx = jedis.setnx(key, serValue);
                        jedis.expire(key, expired);
                        return setnx;
                    } else if (expired < 0) {
                        return jedis.setnx(key, serValue);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("setnx cache fail;key=%s", key), e);
        }
        return null;
    }

    @Override
    public Long delete(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.del(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("delete cache fail;key=%s", key), e);
        }

        return null;
    }

    @Override
    public void batchDelete(String key) {
        Set<String> keysSet = this.keys(key);
        if (keysSet != null) {
            for (String keyStr : keysSet) {
                this.delete(keyStr);
            }

        } else {
            LOGGER.info("keysSet is null");
        }
    }

    /**
     * ++++++++++++++++   list +++++++++++++++++++
     **/
    @Override
    public Long llen(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.llen(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("llen cache fail;key=%s", key), e);
        }

        return null;
    }

    @Override
    public Long lpush(final String key, final String... strings) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.lpush(key, strings);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("lpush cache fail;key=%s, value=%s", key, Arrays.toString(strings)), e);
        }
        return null;
    }

    @Override
    public Long rpush(final String key, final String... strings) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.rpush(key, strings);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("rpush cache fail;key=%s, strings=%s", key, Arrays.toString(strings)), e);
        }
        return null;
    }

/*    @Override
    public long rpush(final String key, final int expired, String... strings) {
        try {
            return jedisTemplate.execute(new JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    Long rpush = jedis.rpush(key, strings);
                    if (expired > 0) {
                        jedis.expire(key, expired);
                    }
                    return rpush;
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("rpush cache fail;key=%s, value=%s, expired=%s", key, Arrays.toString(strings), expired), e);
        }
        return 0;
    }*/

    @Override
    public String lpop(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.lpop(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("lpop cache fail;key=%s", key), e);
        }

        return null;
    }

    @Override
    public <T> T lpop(final String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || clazz == null) {
            LOGGER.error("args error;key={}, clazz={}", key, clazz);
            return null;
        }

        String value = null;
        try {
            value = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.lpop(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("lpop cache fail;key=%s", key), e);
        }

        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    @Override
    public String rpop(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.rpop(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("rpop cache fail;key=%s", key), e);
        }

        return null;
    }

    @Override
    public <T> T rpop(final String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || clazz == null) {
            LOGGER.error("args error;key={}, clazz={}", key, clazz);
            return null;
        }

        String value = null;
        try {
            value = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.rpop(key);
                }
            });
        } catch (JedisException e) {
            LOGGER.error(String.format("rpop cache fail;key=%s", key), e);
        }

        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }


    @Override
    public List<String> lrange(final String key, final int start, final int end) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<List<String>>() {
                @Override
                public List<String> action(Jedis jedis) {
                    return jedis.lrange(key, start, end);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("lrange fail;key=%s,start=%s,end=%s", key, start, end), e);
        }
        return null;
    }

    @Override
    public Long publish(final String channel, Object message) {
        try {
            final String serValue = JSON.toJSONString(message);
            Long value = jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.publish(channel, serValue);
                }
            });
            return value;
        } catch (Exception e) {
            LOGGER.error(String.format("set cache fail;channel=%s", channel), e);
        }
        return null;
    }

    @Override
    public void subscribe(final JedisPubSub listener, final String channel) {
        try {
            jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {

                @Override
                public void action(Jedis jedis) {
                    jedis.subscribe(listener, channel);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("set cache fail;channel=%s", channel), e);
        }
    }


    @Override
    public Long zadd(final String key, final double score, final String value) {
        if (StringUtils.isEmpty(key) || value == null) {
            LOGGER.error("args error;key={},score={}, Object={}", key, score, value);
            return null;
        }
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.zadd(key, score, value);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zadd cache fail;key=%s,score=%s,value=%s", key, score, value), e);
        }
        return null;
    }

    @Override
    public Long zadd(final String key, final double score, final String value, final int expired) {
        if (StringUtils.isBlank(key) || value == null) {
            LOGGER.error("args error;key={},score={}, Object={}", key, score, value);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    Long aLong = jedis.zadd(key, score, value);
                    jedis.expire(key, expired);
                    return aLong;
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zadd cache fail;key=%s,score=%s,value=%s", key, score, value), e);
        }
        return null;
    }

    @Override
    public Long zcard(final String key) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.zcard(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zcard fail;key=%s", key), e);
        }
        return null;
    }

    @Override
    public Set<String> zrange(final String key, final long start, final long end) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Set<String>>() {
                @Override
                public Set<String> action(Jedis jedis) {
                    return jedis.zrange(key, start, end);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrange fail;key=%s,start=%s,end=%s", key, start, end), e);
        }
        return null;
    }

    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Set<String>>() {
                @Override
                public Set<String> action(Jedis jedis) {
                    return jedis.zrangeByScore(key, min, max);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrange fail;key=%s,start=%s,end=%s", key, min, max), e);
        }
        return null;
    }

    @Override
    public Set<String> zrevrange(final String key, final long start, final long end) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Set<String>>() {
                @Override
                public Set<String> action(Jedis jedis) {
                    return jedis.zrevrange(key, start, end);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrevrange fail;key=%s,start=%s,end=%s", key, start, end), e);
        }
        return null;
    }

    @Override
    public Long zrank(final String key, final String member) {
        if (StringUtils.isBlank(key) || member == null) {
            LOGGER.error("args error;key={},member={}", key, member);
            return 0L;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.zrank(key, member);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrank fail;key=%s,member=%s", key, member), e);
        }
        return -1L;
    }

    @Override
    public Long zrevrank(final String key, final String member) {
        if (StringUtils.isBlank(key) || member == null) {
            LOGGER.error("args error;key={},member={}", key, member);
            return 0L;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {
                @Override
                public Long action(Jedis jedis) {
                    return jedis.zrevrank(key, member);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrank fail;key=%s,member=%s", key, member), e);
        }
        return -1L;
    }

    @Override
    public Boolean exists(final String key) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return false;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {
                @Override
                public Boolean action(Jedis jedis) {
                    return jedis.exists(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("zrank fail;key=%s", key), e);
        }
        return false;
    }

    @Override
    public Set<String> keys(final String pattern) {
        if (StringUtils.isBlank(pattern)) {
            LOGGER.error("args error;pattern={}", pattern);
            return null;
        }

        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Set<String>>() {
                @Override
                public Set<String> action(Jedis jedis) {
                    return jedis.keys(pattern);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("keys fail;pattern=%s", pattern), e);
        }
        return null;
    }

    @Override
    public String hget(final String key, final String field) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.hget(key, field);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hget cache fail;key=%s, field=%s", key, field), e);
        }

        return null;
    }


    @Override
    public Long hset(final String key, final String field, final String value) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.hset(key, field, value);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hset cache fail;key=%s, field=%s, value=%s", key, field, value), e);
        }

        return null;
    }

    @Override
    public String hmset(final String key, final Map<String, String> hash) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {

                @Override
                public String action(Jedis jedis) {
                    return jedis.hmset(key, hash);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hmset cache fail;key=%s, hash=%s", key, hash), e);
        }

        return null;
    }

    @Override
    public List<String> hmget(final String key, final String... fields) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<List<String>>() {

                @Override
                public List<String> action(Jedis jedis) {
                    return jedis.hmget(key, fields);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hmget cache fail;key=%s, fields=%s", key, Arrays.toString(fields)), e);
        }

        return null;
    }

    @Override
    public Map<String, String> hgetAll(final String key) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Map<String, String>>() {

                @Override
                public Map<String, String> action(Jedis jedis) {
                    return jedis.hgetAll(key);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hgetAll cache fail;key=%s", key), e);
        }

        return null;
    }

    @Override
    public Long hdel(final String key, final String... fields) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Long>() {

                @Override
                public Long action(Jedis jedis) {
                    return jedis.hdel(key, fields);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hdel cache fail;key=%s, fields=%s", key, Arrays.toString(fields)), e);
        }

        return null;
    }

    @Override
    public boolean hExists(final String key, final String lid) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("args error;key={}", key);
            return false;
        }
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {
                @Override
                public Boolean action(Jedis jedis) {
                    return jedis.hexists(key, lid);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("hExists cache fail;key=%s", key), e);
        }
        return false;
    }


    @Override
    public Transaction multi() {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<Transaction>() {
                @Override
                public Transaction action(Jedis jedis) {
                    return jedis.multi();
                }
            });
        } catch (Exception e) {
            LOGGER.error("multi fail;", e);
        }
        return null;
    }

    @Override
    public String watch(final byte[]... keys) {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
                @Override
                public String action(Jedis jedis) {
                    return jedis.watch(keys);
                }
            });
        } catch (Exception e) {
            LOGGER.error("watch fail;keys={}, e={}", keys, e);
        }
        return null;
    }

    @Override
    public String unwatch() {
        try {
            return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
                @Override
                public String action(Jedis jedis) {
                    return jedis.unwatch();
                }
            });
        } catch (Exception e) {
            LOGGER.error("unwatch fail;", e);
        }
        return null;
    }


}
