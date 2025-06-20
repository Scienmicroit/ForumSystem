package ui;

import data.ForumDataManager;
import data.Post;
import java.awt.*;
import javax.swing.*;
import main.ForumSystem;

/**
 * NewPostPanel 类用于创建一个用于发布新帖子的面板。
 * 它提供了输入帖子标题和内容的文本框，并允许用户提交或取消帖子发布操作。
 */
public class NewPostPanel extends JPanel {
    // 引用论坛系统主窗口，用于面板切换和获取当前用户信息
    private ForumSystem mainFrame;
    // 数据管理器，负责与帖子数据进行交互，如添加新帖子
    private ForumDataManager dataManager;
    // 用于输入帖子标题的文本框
    private JTextField titleField;
    // 用于输入帖子内容的文本区域
    private JTextArea contentArea;

    /**
     * 构造函数，初始化面板及其组件。
     * 
     * @param mainFrame    论坛系统主窗口实例
     * @param dataManager  数据管理器实例
     */
    public NewPostPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        // 设置面板的布局为 BorderLayout，方便组件的布局管理
        setLayout(new BorderLayout());

        // 创建表单面板，用于放置标题和内容输入组件
        JPanel formPanel = new JPanel();
        // 使用 GridBagLayout 布局管理器，以便更灵活地控制组件的位置和大小
        formPanel.setLayout(new GridBagLayout());
        // 创建 GridBagConstraints 对象，用于设置组件在 GridBagLayout 中的约束条件
        GridBagConstraints gbc = new GridBagConstraints();
        // 设置组件之间的间距
        gbc.insets = new Insets(5, 5, 5, 5);
        // 初始列位置
        gbc.gridx = 0;
        // 初始行位置
        gbc.gridy = 0;
        // 组件在单元格中的对齐方式
        gbc.anchor = GridBagConstraints.WEST;

        // 添加标题标签到表单面板
        formPanel.add(new JLabel("标题:"), gbc);
        // 移动到下一列
        gbc.gridx = 1;
        // 组件跨越的列数
        gbc.gridwidth = 2;
        // 组件在水平方向上填充单元格
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // 初始化标题输入文本框
        titleField = new JTextField(50);
        // 将标题输入文本框添加到表单面板
        formPanel.add(titleField, gbc);

        // 移动到下一行，第一列
        gbc.gridx = 0;
        gbc.gridy = 1;
        // 组件跨越的列数恢复为 1
        gbc.gridwidth = 1;
        // 组件不填充单元格
        gbc.fill = GridBagConstraints.NONE;
        // 添加内容标签到表单面板
        formPanel.add(new JLabel("内容:"), gbc);

        // 移动到下一行，第二列
        gbc.gridx = 1;
        gbc.gridy = 1;
        // 组件跨越的列数为 2
        gbc.gridwidth = 2;
        // 组件跨越的行数为 5
        gbc.gridheight = 5;
        // 组件在水平和垂直方向上填充单元格
        gbc.fill = GridBagConstraints.BOTH;
        // 组件在水平方向上的权重
        gbc.weightx = 1.0;
        // 组件在垂直方向上的权重
        gbc.weighty = 1.0;
        // 初始化内容输入文本区域
        contentArea = new JTextArea(20, 50);
        // 设置文本区域自动换行
        contentArea.setLineWrap(true);
        // 创建滚动面板，将内容输入文本区域添加到滚动面板中，以支持文本区域的滚动显示
        JScrollPane scrollPane = new JScrollPane(contentArea);
        // 将滚动面板添加到表单面板
        formPanel.add(scrollPane, gbc);

        // 将表单面板添加到当前面板的中心位置
        add(formPanel, BorderLayout.CENTER);

        // 创建按钮面板，用于放置提交和取消按钮
        JPanel buttonPanel = new JPanel();
        // 创建提交按钮
        JButton submitButton = new JButton("提交");
        // 为提交按钮添加点击事件监听器，点击时调用提交帖子的方法
        submitButton.addActionListener(e -> submitPost());

        // 创建取消按钮
        JButton cancelButton = new JButton("取消");
        // 为取消按钮添加点击事件监听器，点击时清空输入框并返回论坛主面板
        cancelButton.addActionListener(e -> {
            titleField.setText("");
            contentArea.setText("");
            mainFrame.showForumPanel();
        });

        // 将提交和取消按钮添加到按钮面板
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        // 将按钮面板添加到当前面板的底部位置
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 提交帖子的方法。
     * 从输入框中获取标题和内容，验证其有效性，若有效则创建新帖子并添加到数据管理器中。
     */
    private void submitPost() {
        // 获取标题输入框中的文本
        String title = titleField.getText();
        // 获取内容输入文本区域中的文本
        String content = contentArea.getText();

        // 检查标题和内容是否为空
        if (title.isEmpty() || content.isEmpty()) {
            // 若为空，弹出错误提示框
            JOptionPane.showMessageDialog(this, "标题和内容不能为空！", "发布失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建新的帖子对象，使用当前用户作为作者
        Post newPost = new Post(title, content, mainFrame.getCurrentUser().getUsername());
        // 调用数据管理器的方法将新帖子添加到数据中
        dataManager.addPost(newPost);

        // 弹出提示框，告知用户帖子发布成功
        JOptionPane.showMessageDialog(this, "帖子发布成功！");
        // 清空标题输入框
        titleField.setText("");
        // 清空内容输入文本区域
        contentArea.setText("");
        // 刷新论坛主面板的帖子列表
        mainFrame.refreshForumPanel();
        // 显示论坛主面板
        mainFrame.showForumPanel();
    }
}