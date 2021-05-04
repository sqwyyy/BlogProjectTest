package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @date 2020/3/22 - 23:13
 */
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {
    //private Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Autowired
    private LettuceConnectionFactory redisConnectionFactory;

    @Override
    public CacheManager cacheManager() {
        // 重新配置缓存
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        //设置缓存的默认超时时间：30分钟
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L))
                .disableCachingNullValues()
                .disableKeyPrefix()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((new GenericJackson2JsonRedisSerializer())));

        /*
        1. entryTtl: 定义默认的cache time-to-live.

        2. disableCachingNullValues: 禁止缓存Null对象. 这个识需求=而定.

        3. computePrefixWith: 此处定义了cache key的前缀, 避免公司不同项目之间的key名称冲突.

        4. serializeKeysWith, serializeValuesWith: 定义key和value的序列化协议, 同时的hash key和hash value也被定义.
        * */
        return RedisCacheManager.builder(RedisCacheWriter
                .nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Override
    public KeyGenerator keyGenerator() {
        //  设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存,使用：进行分割，可以很多显示出层级关系
        // 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法，我感觉很好用，大家感兴趣可以去了解下
        /*return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            String rsToUse = String.valueOf(sb);
            //System.out.println("调用Redis缓存Key : " + rsToUse);
            return rsToUse;
        };

         */
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            /*sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());

             */
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            String rsToUse = String.valueOf(sb);
            //System.out.println("调用Redis缓存Key : " + rsToUse);
            return rsToUse;
        };
    }
}
