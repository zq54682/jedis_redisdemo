package com.atguigu;

import redis.clients.jedis.Jedis;

public class JedisDemo1 {

    private static String host = "192.168.188.100";
    private static int port = 6379;

    public static void main(String[] args) {
        Jedis jedis = new Jedis(host, port);

        String ping = jedis.ping();
        System.out.println(ping);

        System.out.println("--------------------------------");

        String set = jedis.set("k1", "v1");
        String get = jedis.get("k2");
        System.out.println(set);
        System.out.println(get);
        System.out.println(set);
        System.out.println(get);
        jedis.close();

    }

}
