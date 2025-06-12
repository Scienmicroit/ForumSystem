package main;

import javax.swing.*;
import java.awt.*;
import data.*;
import ui.*;

/**
 * 论坛系统主窗口，负责各功能面板的初始化、切换和主流程控制。
 */
public class ForumSystem extends JFrame {
    /** 数据管理器，负责用户、帖子、回帖等数据的持久化和操作 */
    private ForumDataManager dataManager;
    /** 当前登录用户 */
    private User currentUser;
    /** 卡片布局，用于主面板的多界面切换 */
    private CardLayout cardLayout;
    /** 主面板，承载所有功能子面板 */
    private JPanel mainPanel;

    // 各功能面板
    /** 登录面板 */
    private LoginPanel loginPanel;
    /** 注册面板 */
    private RegisterPanel registerPanel;
    /** 论坛主界面（帖子列表） */
    private ForumPanel forumPanel;
    /** 发帖面板 */
    private NewPostPanel newPostPanel;
    /** 查看帖子及回帖面板 */
    private ViewPostPanel viewPostPanel;
    /** 我的帖子面板 */
    private MyPostsPanel myPostsPanel;
    /** 我的回帖面板 */
    private MyRepliesPanel myRepliesPanel;
    /** 活跃度排行面板 */
    private ActivityRankingPanel activityRankingPanel;
    /** 管理员管理面板 */
    private AdminPanel adminPanel;

    /**
     * 构造方法：初始化所有面板和主窗口
     */
    public ForumSystem() {
        dataManager = new ForumDataManager();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 初始化各功能面板，并传递主窗口和数据管理器引用
        loginPanel = new LoginPanel(this, dataManager);
        registerPanel = new RegisterPanel(this, dataManager);
        forumPanel = new ForumPanel(this, dataManager);
        newPostPanel = new NewPostPanel(this, dataManager);
        viewPostPanel = new ViewPostPanel(this, dataManager);
        myPostsPanel = new MyPostsPanel(this, dataManager);
        myRepliesPanel = new MyRepliesPanel(this, dataManager);
        activityRankingPanel = new ActivityRankingPanel(this, dataManager);
        adminPanel = new AdminPanel(this, dataManager, forumPanel);

        // 将所有面板添加到主面板，并为每个面板分配唯一标识
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

        // 默认显示登录面板
        cardLayout.show(mainPanel, "login");
    }

    /** 显示注册面板 */
    public void showRegisterPanel() {
        cardLayout.show(mainPanel, "register");
    }

    /** 显示论坛主面板 */
    public void showForumPanel() {
        cardLayout.show(mainPanel, "forum");
    }

    /**
     * 设置当前登录用户
     * 
     * @param user 当前登录的用户对象
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /** 刷新论坛主面板的帖子列表 */
    public void refreshForumPanel() {
        forumPanel.refreshPosts();
    }

    /** 显示登录面板 */
    public void showLoginPanel() {
        cardLayout.show(mainPanel, "login");
    }

    /**
     * 获取当前登录用户
     * 
     * @return 当前用户对象
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * 获取帖子查看面板
     * 
     * @return ViewPostPanel 实例
     */
    public ViewPostPanel getViewPostPanel() {
        return this.viewPostPanel;
    }

    /** 显示帖子查看面板 */
    public void showViewPostPanel() {
        cardLayout.show(mainPanel, "viewPost");
    }

    /** 显示发帖面板 */
    public void showNewPostPanel() {
        cardLayout.show(mainPanel, "newPost");
    }

    /** 显示“我的帖子”面板，并刷新数据 */
    public void showMyPostsPanel() {
        myPostsPanel.refreshPosts();
        cardLayout.show(mainPanel, "myPosts");
    }

    /** 显示“我的回帖”面板，并刷新数据 */
    public void showMyRepliesPanel() {
        myRepliesPanel.refreshReplies();
        cardLayout.show(mainPanel, "myReplies");
    }

    /** 显示活跃度排行面板，并刷新数据 */
    public void showActivityRankingPanel() {
        activityRankingPanel.refreshRanking();
        cardLayout.show(mainPanel, "activityRanking");
    }

    /** 显示管理员面板，并刷新数据 */
    public void showAdminPanel() {
        adminPanel.refreshPosts();
        cardLayout.show(mainPanel, "admin");
    }

    /**
     * 程序入口，启动主窗口
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForumSystem().setVisible(true));
    }
}