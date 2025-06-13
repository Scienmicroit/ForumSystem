package data;

import java.io.Serial;
import java.io.Serializable;


public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;
    private final boolean isAdmin;
    private int postCount;
    private int replyCount;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;// 是否为管理员
        this.postCount = 0;// 用户发帖数量
        this.replyCount = 0;// 用户回贴数量
    }
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public int getPostCount() {
        return postCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    // 增加用户帖子的计数
    public void incrementPostCount() {
        postCount++;
    }

    // 增加用户的回帖的计数
    public void incrementReplyCount() {
        replyCount++;
    }


}
