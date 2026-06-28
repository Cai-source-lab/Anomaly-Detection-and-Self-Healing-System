import redis.clients.jedis.Jedis;

public class RedisUtil {

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public static String get(String key){
        return jedis.get(key);
    }

    public static void setex(String key, int seconds, String value){
        jedis.setex(key, seconds, value);
    }

    public static void del(String key){
        jedis.del(key);
    }
}
