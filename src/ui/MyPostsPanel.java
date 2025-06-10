package src.ui;

import javax.swing.*;
import java.awt.*;

import src.main.ForumSystem;


public class MyPostsPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private DefaultListModel<Post> listModel;
    private JList<Post> postList;

    public MyPostsPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setCellRenderer(new DefaultListCellRenderer() {
           
        });
        postList.addMouseListener(new MouseAdapter() {
           
        });

        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("编辑");
        editButton.addActionListener(e -> editPost());

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deletePost());

        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshPosts() {
        listModel.clear();
        List<Post> myPosts = dataManager.getPostsByAuthor(mainFrame.getCurrentUser().getUsername());
        for (Post post : myPosts) {
            listModel.addElement(post);
        }
    }

    private void editPost() {
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newTitle = JOptionPane.showInputDialog(this, "编辑标题", selectedPost.getTitle());
        if (newTitle == null) {
            return;
        }

        String newContent = JOptionPane.showInputDialog(this, "编辑内容", selectedPost.getContent());
        if (newContent == null) {
            return;
        }

        selectedPost.setTitle(newTitle);
        selectedPost.setContent(newContent);

        dataManager.updatePost(selectedPost);
        JOptionPane.showMessageDialog(this, "帖子编辑成功！");
        refreshPosts();
        mainFrame.refreshForumPanel();
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
            mainFrame.refreshForumPanel();
        }
    }
}