package ui;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.util.List;

import main.ForumSystem;
import data.ForumDataManager;
import data.Post;

public class ForumPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private DefaultListModel<Post> listModel;
    private JList<Post> postList;
    private JPanel navPanel;

    public ForumPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        setLayout(new BorderLayout());

        // 帖子列表
        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Post) {
                    Post post = (Post) value;
                    setText(post.getTitle() + " - " + post.getAuthor() + " (" + post.getDateString() + ")");
                }
                return this;
            }
        });

        // 支持双击进入帖子详情
        postList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Post selectedPost = postList.getSelectedValue();
                    if (selectedPost != null) {
                        mainFrame.getViewPostPanel().setPost(selectedPost);
                        mainFrame.showViewPostPanel();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        // 顶部导航栏
        navPanel = new JPanel();
        add(navPanel, BorderLayout.NORTH);

        refreshNavPanel();
    }

    // 刷新顶部按钮
    private void refreshNavPanel() {
        navPanel.removeAll();

        // 发帖按钮
        JButton newPostButton = new JButton("发帖");
        newPostButton.addActionListener(e -> mainFrame.showNewPostPanel());
        navPanel.add(newPostButton);

        // 我的帖子
        JButton myPostsButton = new JButton("我的帖子");
        myPostsButton.addActionListener(e -> mainFrame.showMyPostsPanel());
        navPanel.add(myPostsButton);

        // 我的回帖
        JButton myRepliesButton = new JButton("我的回帖");
        myRepliesButton.addActionListener(e -> mainFrame.showMyRepliesPanel());
        navPanel.add(myRepliesButton);

        // 活跃度排行
        JButton rankingButton = new JButton("活跃度排行");
        rankingButton.addActionListener(e -> mainFrame.showActivityRankingPanel());
        navPanel.add(rankingButton);

        // 管理员入口（仅管理员可见）
        if (mainFrame.getCurrentUser() != null && mainFrame.getCurrentUser().isAdmin()) {
            JButton adminButton = new JButton("管理员入口");
            adminButton.addActionListener(e -> mainFrame.showAdminPanel());
            navPanel.add(adminButton);
        }

        // 退出登录
        JButton logoutButton = new JButton("退出登录");
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());
        navPanel.add(logoutButton);

        navPanel.revalidate();
        navPanel.repaint();
    }

    // 刷新帖子列表和顶部按钮
    public void refreshPosts() {
        refreshNavPanel();
        listModel.clear();
        List<Post> posts = dataManager.getAllPosts();
        for (Post post : posts) {
            listModel.addElement(post);
        }
    }
}