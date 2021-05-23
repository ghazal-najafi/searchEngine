package RankedRetrieval.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

import RankedRetrieval.model.Resources;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class Redis {
    ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRedisConfig.class);
    @SuppressWarnings("unchecked")
    RedisTemplate<String, ArrayList<Resources>> redisTemplate = (RedisTemplate<String, ArrayList<Resources>>) ctx.getBean("redisTemplate");
    ValueOperations<String, ArrayList<Resources>> values = redisTemplate.opsForValue();

    public void store(String sentence, ArrayList<Resources> resources) {
        try {
            values.set(sentence, resources);
            redisTemplate.expire(sentence, 5, TimeUnit.MINUTES);
        } finally {
            ctx.close();
        }
    }

    public ArrayList<Resources> get(String sentence) {
        return values.get(sentence);
    }

    public void deleteKey(String sentence) {
        values.getOperations().delete(sentence);
    }

    public void deleteResource(int id) {
        Set<String> keys = redisTemplate.keys("*");
        System.out.println("keysss:" + keys);
        for (String key : keys) {
            if (redisTemplate.hasKey(key)) {
                ArrayList<Resources> resources = values.get(key);
                if (resources.size() > 0) {
                    for (Resources resource : resources) {
                        if (resource.getId() == id) {
                            resources.remove(resource);
                            values.getAndSet(key, resources);
                            System.out.println(Arrays.toString(resources.toArray()));
                            if (resources == null)
                                deleteKey(key);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("keysss1:" + keys);
    }
}