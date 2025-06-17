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
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public User getUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean authenticate(String username, String password) {
        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }
     // 帖子管理
    public void addPost(Post post) {
        posts.add(post);
        User user = getUser(post.getAuthor());
        if (user != null) {
            user.incrementPostCount();
        }
        savePosts();
        saveUsers();
    }

    public void updatePost(Post post) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                savePosts();
                break;
            }
        }
    }

    public void deletePost(int postId) {
        posts.removeIf(p -> p.getId() == postId);
        savePosts();
    }

    public List<Post> getAllPosts() {
        // 置顶帖子排在前面
        List<Post> stickyPosts = posts.stream()
                .filter(Post::isSticky)
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());

        List<Post> normalPosts = posts.stream()
                .filter(p -> !p.isSticky())
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());

        stickyPosts.addAll(normalPosts);
        return stickyPosts;
    }
    // 管理员功能
    public List<User> getAllUsers() {
        return users;
    }

    public void setPostSticky(int postId, boolean sticky) {
        posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .ifPresent(post -> {
                    post.setSticky(sticky);
                    savePosts();
                });
    }

    public List<Post> getPostsByAuthor(String author) {
        return posts.stream()
                .filter(p -> p.getAuthor().equals(author))
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .collect(Collectors.toList());
    }


    // 数据持久化
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
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

    private void savePosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(POSTS_FILE))) {
            oos.writeObject(posts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
