package vttp.ssf.project.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.project.utils.Constants;

@Repository
public class GachaRepo {
    
    @Autowired
    @Qualifier(Constants.REDIS_TEMPLATE)
    RedisTemplate<String, String> redisTemplate;

    public void createUser(String username, int coinBalance) {
        redisTemplate.opsForHash().put(username, "username", username);
        redisTemplate.opsForHash().put(username, "balance", String.valueOf(coinBalance));
    }

    public void setPassword(String username, String password) {
        redisTemplate.opsForHash().put(username, "password", password);
    }

    public void setBalance(String username, int coinBalance) {
        redisTemplate.opsForHash().put(username, "balance", String.valueOf(coinBalance));
    }

    public void addSanrioFigurine(String username, String figurine) {
        String keyName = username + ":sanrio";
        redisTemplate.opsForList().leftPush(keyName, figurine);
    }

    public void addKirbyFigurine(String username, String figurine) {
        String keyName = username + ":kirby";
        redisTemplate.opsForList().leftPush(keyName, figurine);
    }

    public boolean userExists(String username) {
        return redisTemplate.opsForHash().hasKey(username, "username");
    }

    public String userUsername(String username) {
        Object userName = redisTemplate.opsForHash().get(username, "username");
        return userName.toString();
    }

    public String userPassword(String username) {
        Object userPassword = redisTemplate.opsForHash().get(username, "password");
        return userPassword.toString();
    }

    public String userBalance(String username) {
        Object userBalance = redisTemplate.opsForHash().get(username, "balance");
        return userBalance.toString();
    }

    public List<String> userSanrioAll(String username) {
        
        return redisTemplate.opsForList().range(username, 0, -1);
    }

    public List<String> userKirbyAll(String username) {

        return redisTemplate.opsForList().range(username, 0, -1);
    }
}
