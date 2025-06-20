package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




public class ForumDataManager {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";
    private List<User> users;
    private List<Post> posts;

    public ForumDataManager() {
        loadUsers();
        loadPosts();

    }

    // 用户管理
    /**
     * 根据用户名获取用户对象
     *
     * username 用户名
     * 用户对象，如果未找到则返回null
     *    此方法通过用户名来过滤用户列表，返回匹配用户名的第一个用户对象，如果没有匹配的则返回null
     */
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }
    /**
     * 根据用户名获取用户对象
     *
     * @param username 用户名
     * @return 用户对象，如果未找到则返回null
     *         此方法通过用户名来过滤用户列表，返回匹配用户名的第一个用户对象，如果没有匹配的则返回null
     */
    public User getUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    /**
     * 验证用户身份
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果用户名和密码匹配则返回true，否则返回false
     *         此方法首先通过用户名获取用户对象，然后检查用户的密码是否与提供的密码匹配
     */

    public boolean authenticate(String username, String password) {
        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }
     // 帖子管理
    /**
     * 添加一个新的帖子到系统中
     * 此方法首先将帖子添加到帖子列表中，然后尝试更新作者的帖子计数，
     * 最后保存更新后的帖子列表和用户列表
     *
     * @param post 要添加的新帖子，包含帖子信息和作者信息
     */
    public void addPost(Post post) {
        posts.add(post);
        User user = getUser(post.getAuthor());
        if (user != null) {
            user.incrementPostCount();
        }
        savePosts();
        saveUsers();
    }
    /**
     * 更新系统中的某个帖子信息
     * 此方法遍历帖子列表，寻找与给定帖子ID匹配的帖子，
     * 如果找到，则用新提供的帖子信息替换旧的帖子信息，并保存更新后的帖子列表
     *
     * @param post 包含更新后的帖子信息的对象
     */
    public void updatePost(Post post) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                savePosts();
                break;
            }
        }
    }
    /**
     * 删除指定的帖子
     * 此方法通过移除与给定postId匹配的帖子来删除帖子它使用了内部的posts列表的removeIf方法来进行筛选和移除
     * 在移除完成后，它通过调用savePosts方法来保存更新后的帖子列表
     *
     * @param postId 要删除的帖子的唯一标识符
     */
    public void deletePost(int postId) {
        posts.removeIf(p -> p.getId() == postId);
        savePosts();
    }
    /**
     * 获取所有帖子
     * 此方法首先将帖子分为置顶帖子和普通帖子两类置顶帖子会根据日期降序排列，
     * 以确保最新的置顶帖子排在最前面普通帖子也会根据日期降序排列，但位于置顶帖子之后
     *
     * @return 所有帖子的列表，其中置顶帖子优先显示， followed by normal posts, all sorted by date in descending order.
     */
    public List<Post> getAllPosts() {
        // 置顶帖子排在前面
        List<Post> stickyPosts = posts.stream()
                .filter(Post::isSticky)
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());
        // 非置顶帖子排在置顶帖子之后
        List<Post> normalPosts = posts.stream()
                .filter(p -> !p.isSticky())
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());
        // 合并置顶帖子和非置顶帖子列表
        stickyPosts.addAll(normalPosts);
        return stickyPosts;
    }
    // 管理员功能
    /**
     * 获取所有用户信息
     *
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return users;
    }
    /**
     * 设置帖子是否置顶
     *
     * 帖子ID
     * 是否置顶
     */
    public void setPostSticky(int postId, boolean sticky) {
        posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .ifPresent(post -> {
                    post.setSticky(sticky);
                    savePosts();
                });
    }
    /**
     * 根据作者获取帖子列表
     *
     *  作者名称
     * 按日期降序排列的帖子列表
     */
    public List<Post> getPostsByAuthor(String author) {
        return posts.stream()
                .filter(p -> p.getAuthor().equals(author))
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());
    }
    /**
    * 添加回复到指定帖子
    * 需要添加回复的帖子ID
    * 要添加的回复对象
    */
        // 回帖管理
    public void addReply(int postId, Reply reply) {
        for (Post post : posts) {
            // 当找到匹配的帖子时，添加回复
            if (post.getId() == postId) {
                post.addReply(reply);
                // 获取回复的作者信息                
                User user = getUser(reply.getAuthor());
                // 如果作者存在，则增加其回复计数
                if (user != null) {
                    user.incrementReplyCount();
                }
                // 保存更新后的帖子和用户信息
                savePosts();
                saveUsers();
                break;
            }
        }
    }

    // 活跃度统计
    /**
    * 获取用户活跃度排名
    * 
    * 本方法通过计算用户发帖和回帖数量之和来衡量用户的活跃度，并返回按照活跃度降序排列的用户排名
    * 使用了HashMap来存储用户及其活跃度，然后通过Stream API进行排序和重新收集，以确保最终结果是按照活跃度降序排列的
    * 
    * 返回一个映射，键是用户名，值是用户的活跃度（发帖数加回帖数），按照活跃度降序排列
    */
    public Map<String, Integer> getActivityRanking() {
        // 初始化一个HashMap来存储用户及其活跃度
        Map<String, Integer> ranking = new HashMap<>();
        // 遍历用户列表，计算每个用户的活跃度，并存入Map中
        for (User user : users) {
            int activity = user.getPostCount() + user.getReplyCount();
            ranking.put(user.getUsername(), activity);
        }
        // 使用Stream API对Map中的用户活跃度进行降序排序，并收集到一个新的LinkedHashMap中，以保持顺序
        return ranking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    // 数据持久化
    /**
    * 加载用户信息列表
    * 该方法首先检查用户信息文件是否存在如果存在，则尝试从文件中反序列化用户列表
    * 如果文件不存在，或者反序列化过程中发生错误，则创建一个新的用户列表，并添加一个默认的管理员用户
    */
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        // 创建用户信息文件对象
        File file = new File(USERS_FILE);
        // 检查文件是否存在
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // 如果读取过程中发生IO错误或类找不到异常，则创建新的用户列表
                users = new ArrayList<>();
                e.printStackTrace();
            }
        } else {
            users = new ArrayList<>();
            // 添加默认管理员
            addUser(new User("admin", "admin123", true));
        }
    }

    /**
    * 将用户信息列表保存到文件中
    * 此方法使用对象输出流将内存中的用户信息列表序列化并保存到磁盘上
    * 这样做是为了在程序终止运行后能够持久化用户信息
    */
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    * 加载帖子信息从文件中
    * 该方法首先检查帖子文件是否存在如果存在，则使用对象输入流从文件中反序列化帖子列表
    * 如果文件不存在或读取过程中发生错误，则初始化一个新的帖子列表
    */
    @SuppressWarnings("unchecked")
    private void loadPosts() {
        File file = new File(POSTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                posts = (List<Post>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                posts = new ArrayList<>();
                e.printStackTrace();
            }
        } else {
            posts = new ArrayList<>();
        }
    }

    /**
    * 将帖子信息保存到文件中
    * 该方法使用对象输出流将帖子列表序列化并保存到指定的文件中
    */

    private void savePosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(POSTS_FILE))) {
            oos.writeObject(posts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
