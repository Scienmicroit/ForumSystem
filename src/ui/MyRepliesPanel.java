package ui;

import data.ForumDataManager;
import data.Post;
import data.Reply;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import main.ForumSystem;

/**
 * MyRepliesPanel 类用于显示当前用户的所有回帖，
 * 支持通过双击回帖查看对应的帖子详情，并提供返回论坛主面板的功能。
 */
public class MyRepliesPanel extends JPanel {
    // 引用论坛系统主窗口，用于面板切换和获取当前用户信息
    private ForumSystem mainFrame;
    // 数据管理器，负责与帖子和回帖数据进行交互，如读取和更新数据
    private ForumDataManager dataManager;
    // 显示回帖列表的组件，用户可以通过该列表选择要操作的回帖
    private JList<Reply> replyList;
    // 用于存储回帖的列表模型，为 JList 提供数据支持
    private DefaultListModel<Reply> listModel;
    // 映射回帖和对应的帖子，方便在用户双击回帖时找到对应的帖子
    private Map<Reply, Post> replyPostMap;

    /**
     * 构造函数，初始化面板及其组件。
     * 
     * @param mainFrame    论坛系统主窗口实例
     * @param dataManager  数据管理器实例
     */
    public MyRepliesPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;
        // 设置面板的布局为 BorderLayout，方便组件的布局管理
        setLayout(new BorderLayout());
        // 初始化回帖和帖子的映射
        replyPostMap = new HashMap<>();

        // 初始化列表模型
        listModel = new DefaultListModel<>();
        // 初始化回帖列表，并将列表模型关联到列表上
        replyList = new JList<>(listModel);
        // 设置回帖列表的单元格渲染器，自定义每个回帖在列表中的显示格式
        replyList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                // 调用父类的方法进行基本的渲染
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reply) {
                    // 将对象转换为 Reply 类型
                    Reply reply = (Reply) value;
                    // 设置单元格的文本内容，包含回帖内容和日期
                    setText(String.format("回帖内容: %s\n日期: %s", reply.getContent(), reply.getDateString()));
                }
                return this;
            }
        });
        // 为回帖列表添加鼠标监听器，支持双击进入帖子详情
        replyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // 获取用户双击选中的回帖
                    Reply selectedReply = replyList.getSelectedValue();
                    if (selectedReply != null && replyPostMap.containsKey(selectedReply)) {
                        // 根据回帖获取对应的帖子
                        Post post = replyPostMap.get(selectedReply);
                        // 将选中的帖子设置到帖子查看面板中
                        mainFrame.getViewPostPanel().setPost(post);
                        // 显示帖子查看面板
                        mainFrame.showViewPostPanel();
                    }
                }
            }
        });

        // 创建滚动面板，将回帖列表添加到滚动面板中，以支持列表的滚动显示
        JScrollPane scrollPane = new JScrollPane(replyList);
        // 将滚动面板添加到面板的中心位置
        add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板，用于放置返回按钮
        JPanel buttonPanel = new JPanel();
        // 创建返回按钮
        JButton backButton = new JButton("返回");
        // 为返回按钮添加点击事件监听器，点击时返回论坛主面板
        backButton.addActionListener(e -> mainFrame.showForumPanel());
        // 将返回按钮添加到按钮面板中
        buttonPanel.add(backButton);
        // 将按钮面板添加到面板的底部位置
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 刷新回帖列表，从数据管理器中获取当前用户的所有回帖，并更新列表模型和映射。
     */
    public void refreshReplies() {
        // 清空列表模型中的所有数据
        listModel.clear();
        // 清空回帖和帖子的映射
        replyPostMap.clear();

        // 从数据管理器中获取所有帖子
        List<Post> allPosts = dataManager.getAllPosts();
        // 遍历所有帖子
        for (Post post : allPosts) {
            // 遍历每个帖子的所有回帖
            for (Reply reply : post.getReplies()) {
                // 检查回帖的作者是否为当前用户
                if (reply.getAuthor().equals(mainFrame.getCurrentUser().getUsername())) {
                    // 将回帖添加到列表模型中
                    listModel.addElement(reply);
                    // 将回帖和对应的帖子添加到映射中
                    replyPostMap.put(reply, post);
                }
            }
        }
    }
}