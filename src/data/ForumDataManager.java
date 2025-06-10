package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;




public class ForumDataManager {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";
    private List<User> users;
    private List<Post> posts;

    public ForumDataManager() {
        loadUsers();
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

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
