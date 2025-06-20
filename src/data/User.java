package data;

import java.io.Serial;
import java.io.Serializable;


public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;
    private final boolean isAdmin;
    // 用户发帖数量
    private int postCount;
    // 用户回贴数量
    private int replyCount;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;// 是否为管理员
        this.postCount = 0;// 用户发帖数量
        this.replyCount = 0;// 用户回贴数量
    }
    // Getters
    /**
     * 获取用户的用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取用户的密码
     */
    public String getPassword() {
        return password;
    }
    /**
     * 检查用户是否为管理员
     * 
     * @return 如果用户是管理员则返回true，否则返回false
     */
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
