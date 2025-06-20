package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.ForumSystem;
import data.ForumDataManager;
import data.Post;

/**
 * 管理员面板 置顶,取消置顶,删除帖子的功能
 */
public class AdminPanel extends JPanel {

    private ForumDataManager dataManager;
    private ForumPanel forumPanel;
    // 列表模型,管理JList要显示的帖子数据
    private DefaultListModel<Post> listModel;
    // 帖子列表可视化展示所有的帖子,支持双击查看
    private JList<Post> postList;

    /**
     * 初始化管理员面板的UI组件以及交互逻辑
     * mainFrame 主窗口：用于界面跳转（如返回论坛主面板）
     * dataManager 数据管理器：操作帖子数据的核心依赖
     * forumPanel 论坛主面板：操作后需同步刷新其帖子列表
     */
    public AdminPanel(ForumSystem mainFrame, ForumDataManager dataManager, ForumPanel forumPanel) {
        this.dataManager = dataManager;
        this.forumPanel = forumPanel;

        // 设置边界布局：中心放帖子列表，底部放操作按钮
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        postList = new JList<>(listModel);
        postList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                // 先执行父类默认渲染逻辑（处理选中、焦点样式）
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // 仅处理 Post 类型数据
                if (value instanceof Post) {
                    Post post = (Post) value;
                    StringBuilder sb = new StringBuilder();
                    if (post.isSticky()) {
                        sb.append("[置顶] ");
                    }
                    // 拼接帖子信息：标题、作者、日期、点赞数
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
        // 双击事件：打开帖子详情页
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

        // 将贴子放入滚动面板避免内容过多时无法显示
        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);
        /** 初始化底部操作按钮面板 */
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

    /** 刷新帖子列表 - 从数据管理器获取最新帖子，更新到列表 */
    public void refreshPosts() {
        listModel.clear();
        List<Post> allPosts = dataManager.getAllPosts();
        for (Post post : allPosts) {
            listModel.addElement(post);
        }
    }

    /**
     * 置顶帖子：
     * 检查是否选中帖子
     * 设置帖子为置顶状态
     * 同步更新数据管理器和论坛主面板
     */
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

    /** 取消置顶逻辑：与置顶逻辑对称，仅状态设置为 false */
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

    /**
     * 删除帖子：
     * 检查是否选中帖子
     * 二次确认删除操作
     * 调用数据管理器删除帖子
     * 同步刷新界面
     */
    private void deletePost() {
        Post selectedPost = postList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "请选择要删除的帖子！", "操作失败", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 二次确认：防止误操作
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除此帖子吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 调用数据管理器删除帖子
            dataManager.deletePost(selectedPost.getId());
            JOptionPane.showMessageDialog(this, "帖子删除成功！");
            refreshPosts();
            forumPanel.refreshPosts();
        }
    }
}