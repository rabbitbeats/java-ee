local key = "rate.limit:" .. KEYS[1] --限流KEY（一秒一个）
local limit = tonumber(ARGV[1]) --限流大小
local limitTime = tonumber(ARGV[2]) --限流时间
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then
    --如果超出限流大小
    return 0
else
    --请求数+1，并设置2秒过期
    redis.call("INCRBY", key, "1")
    redis.call("expire", key, limitTime)
    return current + 1
end