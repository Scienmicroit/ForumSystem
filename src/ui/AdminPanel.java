package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.ForumSystem;
import data.ForumDataManager;
import data.Post;

public class AdminPanel extends JPanel {

    private ForumDataManager dataManager;
    private ForumPanel forumPanel;
    private DefaultListModel<Post> listModel;
    private JList<Post> postList;

    public AdminPanel(ForumSystem mainFrame, ForumDataManager dataManager, ForumPanel forumPanel) {
        this.dataManager = dataManager;
        this.forumPanel = forumPanel;

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Post) {
                    Post post = (Post) value;
                    StringBuilder sb = new StringBuilder();
                    if (post.isSticky()) {
                        sb.append("[置顶] ");
                    }
                    sb.append(post.getTitle())
                            .append(" - ")
                            .append(post.getAuthor())
                            .append(" (")
                            .append(post.getDateString())
                            .append(") ❤️")
                            .append(post.getLikes());
                    setText(sb.toString());
                }
                return this;
            }
        });
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

        JPanel buttonPanel = new JPanel();
        JButton stickyButton = new JButton("置顶");
        stickyButton.addActionListener(e -> setSticky());

        JButton removeStickyButton = new JButton("取消置顶");
        removeStickyButton.addActionListener(e -> removeSticky());

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deletePost());

        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());

        buttonPanel.add(stickyButton);
        buttonPanel.add(removeStickyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshPosts() {
        listModel.clear();
        List<Post> allPosts = dataManager.getAllPosts();
        for (Post post : allPosts) {
            listModel.addElement(post);
        }
    }

    private void setSticky() {
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "请选择要置顶的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }
        selectedPost.setSticky(true);
        dataManager.updatePost(selectedPost);
        JOptionPane.showMessageDialog(this, "帖子已置顶！");
        refreshPosts();
        forumPanel.refreshPosts();
    }

    private void removeSticky() {
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "请选择要取消置顶的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }
        selectedPost.setSticky(false);
        dataManager.updatePost(selectedPost);
        JOptionPane.showMessageDialog(this, "帖子已取消置顶！");
        refreshPosts();
        forumPanel.refreshPosts();
    }

    private void deletePost() {
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "请选择要删除的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除此帖子吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.deletePost(selectedPost.getId());
            JOptionPane.showMessageDialog(this, "帖子删除成功！");
            refreshPosts();
            forumPanel.refreshPosts();
        }
    }
}