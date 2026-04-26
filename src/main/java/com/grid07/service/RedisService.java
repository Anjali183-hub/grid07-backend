package com.grid07.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //  Virality Score
    public void updateVirality(Long postId, String type) {

        String key = "post:" + postId + ":virality";

        int score = switch (type) {
            case "LIKE" -> 20;
            case "COMMENT" -> 50;
            case "BOT" -> 1;
            default -> 0;
        };

        redisTemplate.opsForValue().increment(key, score);
    }

    //  Horizontal Cap (max 100 bot replies)
    public boolean allowBot(Long postId) {

        String key = "post:" + postId + ":bot_count";

        Long count = redisTemplate.opsForValue().increment(key);

        return count <= 100;
    }

    //  Cooldown (10 min)
    public boolean checkCooldown(Long botId, Long humanId) {

        String key = "cooldown:bot_" + botId + ":human_" + humanId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        }

        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(10));
        return true;
    }
}