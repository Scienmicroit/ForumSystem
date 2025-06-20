package ui;

import data.ForumDataManager;
import data.Post;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import main.ForumSystem;

/**
 * MyPostsPanel 类用于显示当前用户发布的所有帖子，
 * 提供编辑和删除帖子的功能，同时支持返回论坛主面板。
 */
public class MyPostsPanel extends JPanel {
    // 引用论坛系统主窗口，用于面板切换和获取当前用户信息
    private ForumSystem mainFrame;
    // 数据管理器，负责与帖子数据进行交互，如读取、更新和删除帖子
    private ForumDataManager dataManager;
    // 用于存储帖子的列表模型，为 JList 提供数据支持
    private DefaultListModel<Post> listModel;
    // 显示帖子列表的组件，用户可以通过该列表选择要操作的帖子
    private JList<Post> postList;

    /**
     * 构造函数，初始化面板及其组件。
     *
     * @param mainFrame    论坛系统主窗口实例
     * @param dataManager  数据管理器实例
     */
    public MyPostsPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        // 设置面板的布局为 BorderLayout，方便组件的布局管理
        setLayout(new BorderLayout());

        // 初始化列表模型
        listModel = new DefaultListModel<>();
        // 初始化帖子列表，并将列表模型关联到列表上
        postList = new JList<>(listModel);
        // 设置帖子列表的单元格渲染器，自定义每个帖子在列表中的显示格式
        postList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                // 调用父类的方法进行基本的渲染
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Post) {
                    // 将对象转换为 Post 类型
                    Post post = (Post) value;
                    // 设置单元格的文本内容，包含帖子标题、作者和发布日期
                    setText(post.getTitle() + " - " + post.getAuthor() + " (" + post.getDateString() + ")");
                }
                return this;
            }
        });
        // 为帖子列表添加鼠标监听器，支持双击进入帖子详情
        postList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // 获取用户双击选中的帖子
                    Post selectedPost = postList.getSelectedValue();
                    if (selectedPost != null) {
                        // 将选中的帖子设置到帖子查看面板中
                        mainFrame.getViewPostPanel().setPost(selectedPost);
                        // 显示帖子查看面板
                        mainFrame.showViewPostPanel();
                    }
                }
            }
        });

        // 创建滚动面板，将帖子列表添加到滚动面板中，以支持列表的滚动显示
        JScrollPane scrollPane = new JScrollPane(postList);
        // 将滚动面板添加到面板的中心位置
        add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板，用于放置编辑、删除和返回按钮
        JPanel buttonPanel = new JPanel();
        // 创建编辑按钮
        JButton editButton = new JButton("编辑");
        // 为编辑按钮添加点击事件监听器，点击时调用编辑帖子的方法
        editButton.addActionListener(e -> editPost());

        // 创建删除按钮
        JButton deleteButton = new JButton("删除");
        // 为删除按钮添加点击事件监听器，点击时调用删除帖子的方法
        deleteButton.addActionListener(e -> deletePost());

        // 创建返回按钮
        JButton backButton = new JButton("返回");
        // 为返回按钮添加点击事件监听器，点击时返回论坛主面板
        backButton.addActionListener(e -> mainFrame.showForumPanel());

        // 将编辑、删除和返回按钮添加到按钮面板中
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        // 将按钮面板添加到面板的底部位置
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 刷新帖子列表，从数据管理器中获取当前用户的所有帖子，并更新列表模型。
     */
    public void refreshPosts() {
        // 清空列表模型中的所有数据
        listModel.clear();
        // 从数据管理器中获取当前用户发布的所有帖子
        List<Post> myPosts = dataManager.getPostsByAuthor(mainFrame.getCurrentUser().getUsername());
        // 将获取到的帖子逐个添加到列表模型中
        for (Post post : myPosts) {
            listModel.addElement(post);
        }
    }

    /**
     * 编辑选中的帖子。
     * 提示用户输入新的标题和内容，更新帖子信息并保存到数据管理器中。
     */
    private void editPost() {
        // 获取用户选中的帖子
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            // 如果未选中帖子，弹出错误提示框
            JOptionPane.showMessageDialog(this, "请选择要编辑的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 弹出输入对话框，让用户输入新的帖子标题
        String newTitle = JOptionPane.showInputDialog(this, "编辑标题", selectedPost.getTitle());
        if (newTitle == null) {
            // 如果用户取消输入，直接返回
            return;
        }

        // 弹出输入对话框，让用户输入新的帖子内容
        String newContent = JOptionPane.showInputDialog(this, "编辑内容", selectedPost.getContent());
        if (newContent == null) {
            // 如果用户取消输入，直接返回
            return;
        }

        // 更新帖子的标题和内容
        selectedPost.setTitle(newTitle);
        selectedPost.setContent(newContent);

        // 调用数据管理器的方法更新帖子信息
        dataManager.updatePost(selectedPost);
        // 弹出提示框，告知用户帖子编辑成功
        JOptionPane.showMessageDialog(this, "帖子编辑成功！");
        // 刷新当前面板的帖子列表
        refreshPosts();
        // 刷新论坛主面板的帖子列表
        mainFrame.refreshForumPanel();
    }

    /**
     * 删除选中的帖子。
     * 提示用户确认删除操作，确认后从数据管理器中删除该帖子。
     */
    private void deletePost() {
        // 获取用户选中的帖子
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            // 如果未选中帖子，弹出错误提示框
            JOptionPane.showMessageDialog(this, "请选择要删除的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 弹出确认对话框，让用户确认是否删除该帖子
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除此帖子吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 调用数据管理器的方法删除帖子
            dataManager.deletePost(selectedPost.getId());
            // 弹出提示框，告知用户帖子删除成功
            JOptionPane.showMessageDialog(this, "帖子删除成功！");
            // 刷新当前面板的帖子列表
            refreshPosts();
            // 刷新论坛主面板的帖子列表
            mainFrame.refreshForumPanel();
        }
    }
}