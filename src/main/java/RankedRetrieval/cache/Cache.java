package RankedRetrieval.cache;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Cache<K, V> {

    public static final Long DEFAULT_CACHE_TIMEOUT = 60000L;

    protected Map<K, CacheValue<V>> cacheMap;
    protected Long cacheTimeout;

    public Cache() {
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public Cache(Long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        clear();
    }

    public V get(K key) {
        clean();
        if (!(cacheMap.containsKey(key))) {
            return null;
        } else
            return (V) cacheMap.get(key).getValue();
    }


    public void put(K key, V value) {
        cacheMap.put(key, createCacheValue(value));
    }

    public void clear() {
        cacheMap = new HashMap<>();
    }

    public void clean() {
        for (K key : getExpiredKeys()) {
            remove(key);
        }
    }

    public void remove(K key) {
        cacheMap.remove(key);
    }

    public boolean containsKey(K key) {
        return cacheMap.containsKey(key);
    }

    protected Set<K> getExpiredKeys() {
        return cacheMap.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(Collectors.toSet());
    }

    protected boolean isExpired(K key) {
        LocalDateTime expirationDateTime = cacheMap.get(key).getCreatedAt().plus(cacheTimeout, ChronoUnit.MILLIS);
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    protected CacheValue<V> createCacheValue(V value) {
        LocalDateTime now = LocalDateTime.now();
        return new CacheValue<V>() {
            @Override
            public V getValue() {
                return value;
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return now;
            }
        };
    }

    protected interface CacheValue<V> {
        V getValue();

        LocalDateTime getCreatedAt();
    }

}