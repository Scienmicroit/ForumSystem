package src.main;
// 导包
import javax.swing.*;
import java.awt.*;
import src.data.*;
import src.ui.*;

public class ForumSystem extends JFrame {
    private ForumDataManager dataManager;
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // 各功能面板
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ForumPanel forumPanel;
    private NewPostPanel newPostPanel;
    private ViewPostPanel viewPostPanel;
    private MyPostsPanel myPostsPanel;
    private MyRepliesPanel myRepliesPanel;
    private ActivityRankingPanel activityRankingPanel;
    private AdminPanel adminPanel;

    public ForumSystem() {
        dataManager = new ForumDataManager();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, dataManager);
        registerPanel = new RegisterPanel(this, dataManager);
        forumPanel = new ForumPanel(this, dataManager);
        newPostPanel = new NewPostPanel(this, dataManager);
        viewPostPanel = new ViewPostPanel(this, dataManager);
        myPostsPanel = new MyPostsPanel(this, dataManager);
        myRepliesPanel = new MyRepliesPanel(this, dataManager);
        activityRankingPanel = new ActivityRankingPanel(this, dataManager);
        adminPanel = new AdminPanel(this, dataManager, forumPanel);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(forumPanel, "forum");
        mainPanel.add(newPostPanel, "newPost");
        mainPanel.add(viewPostPanel, "viewPost");
        mainPanel.add(myPostsPanel, "myPosts");
        mainPanel.add(myRepliesPanel, "myReplies");
        mainPanel.add(activityRankingPanel, "activityRanking");
        mainPanel.add(adminPanel, "admin");

        setTitle("简易论坛系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(mainPanel);

        cardLayout.show(mainPanel, "login");
    }

    public void showRegisterPanel() {
        cardLayout.show(mainPanel, "register");
    }

    public void showForumPanel() {
        cardLayout.show(mainPanel, "forum");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void refreshForumPanel() {
        forumPanel.refreshPosts();
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "login");
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public ViewPostPanel getViewPostPanel() {
        return this.viewPostPanel;
    }

    public void showViewPostPanel() {
        cardLayout.show(mainPanel, "viewPost");
    }

    public void showNewPostPanel() {
        cardLayout.show(mainPanel, "newPost");
    }

    public void showMyPostsPanel() {
        myPostsPanel.refreshPosts();
        cardLayout.show(mainPanel, "myPosts");
    }

    public void showMyRepliesPanel() {
        myRepliesPanel.refreshReplies();
        cardLayout.show(mainPanel, "myReplies");
    }

    public void showActivityRankingPanel() {
        activityRankingPanel.refreshRanking();
        cardLayout.show(mainPanel, "activityRanking");
    }

    public void showAdminPanel() {
        adminPanel.refreshPosts();
        cardLayout.show(mainPanel, "admin");
    }
    // 主函数
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForumSystem().setVisible(true));
    }
}