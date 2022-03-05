package com.openhack.service.redis;

import io.quarkus.redis.client.RedisClient;
import io.vertx.redis.client.Response;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;

@ApplicationScoped
public class EmbeddingsCache {
    private final RedisClient redisClient;
    private final Logger LOG;

    public EmbeddingsCache(RedisClient redisClient, Logger log) {
        this.redisClient = redisClient;
        LOG = log;
    }

    public void set(String key, String value){
        Response set = redisClient.set(Arrays.asList(key, value));
        LOG.info("key {}", set);
    }

    public void remove(String key){
        Response del = redisClient.del(Arrays.asList(key));
        LOG.info("{} deleted", del);
    }
}
