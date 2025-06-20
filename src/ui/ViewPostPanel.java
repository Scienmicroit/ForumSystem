package ui;

import data.ForumDataManager;
import data.Post;
import data.Reply;
import java.awt.*;
import javax.swing.*;
import main.ForumSystem;

/**
 * ViewPostPanel 类用于创建一个显示帖子详情及回帖的面板。
 * 用户可以在该面板查看帖子的标题、作者、内容、回帖列表，
 * 还能进行回复帖子、点赞帖子以及返回论坛主界面的操作。
 */
public class ViewPostPanel extends JPanel {
    // 引用论坛系统主窗口，用于面板切换和获取当前用户信息
    private ForumSystem mainFrame;
    // 数据管理器，负责与帖子和回帖数据进行交互，如添加回帖、更新帖子信息
    private ForumDataManager dataManager;
    // 当前正在查看的帖子对象
    private Post currentPost;
    // 用于显示帖子标题的标签
    private JLabel titleLabel;
    // 用于显示帖子作者及发布日期、点赞数的标签
    private JLabel authorLabel;
    // 用于显示帖子内容的文本区域
    private JTextArea contentArea;
    // 用于显示回帖列表的列表组件
    private JList<Reply> replyList;
    // 回帖列表的数据模型
    private DefaultListModel<Reply> replyListModel;
    // 用于输入回帖内容的文本区域
    private JTextArea replyArea;

    /**
     * 构造函数，初始化面板及其组件。
     * 
     * @param mainFrame    论坛系统主窗口实例
     * @param dataManager  数据管理器实例
     */
    public ViewPostPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;
        // 设置面板的布局为 BorderLayout，方便组件的布局管理
        setLayout(new BorderLayout());

        // 帖子内容面板，用于放置帖子标题、作者和内容
        JPanel postPanel = new JPanel();
        // 使用 BoxLayout 布局，使组件垂直排列
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));

        // 初始化标题标签，并设置字体为宋体、加粗、字号 16
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("宋体", Font.BOLD, 16));

        // 初始化作者标签，并设置字体为宋体、普通样式、字号 12
        authorLabel = new JLabel();
        authorLabel.setFont(new Font("宋体", Font.PLAIN, 12));

        // 初始化内容文本区域，设置为不可编辑、自动换行，并设置字体为宋体、普通样式、字号 14
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setFont(new Font("宋体", Font.PLAIN, 14));

        // 创建滚动面板，将内容文本区域添加到滚动面板中，以支持文本区域的滚动显示
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        // 将标题、作者和内容滚动面板添加到帖子内容面板
        postPanel.add(titleLabel);
        postPanel.add(authorLabel);
        postPanel.add(contentScrollPane);

        // 初始化回帖列表的数据模型
        replyListModel = new DefaultListModel<>();
        // 初始化回帖列表，并设置数据模型
        replyList = new JList<>(replyListModel);
        // 设置回帖列表的渲染器，自定义回帖的显示格式
        replyList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                // 调用父类的渲染方法
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reply) {
                    // 如果值是 Reply 类型，将其转换为 Reply 对象
                    Reply reply = (Reply) value;
                    // 设置显示的文本，格式为 作者 - 内容 (日期)
                    setText(String.format("%s - %s (%s)", reply.getAuthor(), reply.getContent(),
                            reply.getDateString()));
                }
                return this;
            }
        });
        // 创建滚动面板，将回帖列表添加到滚动面板中，以支持回帖列表的滚动显示
        JScrollPane replyScrollPane = new JScrollPane(replyList);

        // 回帖输入区域面板，用于放置回帖输入文本区域和操作按钮
        JPanel replyInputPanel = new JPanel();
        // 设置回帖输入区域面板的布局为 BorderLayout
        replyInputPanel.setLayout(new BorderLayout());

        // 初始化回帖输入文本区域，设置行数为 5，列数为 40，并设置自动换行
        replyArea = new JTextArea(5, 40);
        replyArea.setLineWrap(true);
        // 创建滚动面板，将回帖输入文本区域添加到滚动面板中，以支持回帖输入文本区域的滚动显示
        JScrollPane replyAreaScrollPane = new JScrollPane(replyArea);

        // 按钮面板，用于放置回复、点赞和返回按钮
        JPanel buttonPanel = new JPanel();
        // 创建回复按钮，并添加点击事件监听器，点击时调用提交回帖的方法
        JButton replyButton = new JButton("回复");
        replyButton.addActionListener(e -> submitReply());

        // 创建点赞按钮，并添加点击事件监听器，点击时调用点赞帖子的方法
        JButton likeButton = new JButton("点赞");
        likeButton.addActionListener(e -> likePost());

        // 创建返回按钮，并添加点击事件监听器，点击时返回论坛主面板
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());

        // 将回复、点赞和返回按钮添加到按钮面板
        buttonPanel.add(replyButton);
        buttonPanel.add(likeButton);
        buttonPanel.add(backButton);

        // 将回帖输入文本区域的滚动面板添加到回帖输入区域面板的中心位置
        replyInputPanel.add(replyAreaScrollPane, BorderLayout.CENTER);
        // 将按钮面板添加到回帖输入区域面板的底部位置
        replyInputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建分割面板，将帖子内容面板和回帖列表滚动面板垂直分割显示
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, postPanel, replyScrollPane);
        // 设置分割面板的分割线位置为 200
        splitPane.setDividerLocation(200);

        // 将分割面板添加到当前面板的中心位置
        add(splitPane, BorderLayout.CENTER);
        // 将回帖输入区域面板添加到当前面板的底部位置
        add(replyInputPanel, BorderLayout.SOUTH);
    }

    /**
     * 设置当前要查看的帖子，并更新面板上的显示信息。
     * 
     * @param post 要查看的帖子对象
     */
    public void setPost(Post post) {
        // 设置当前帖子对象
        this.currentPost = post;
        // 设置标题标签的文本为帖子的标题
        titleLabel.setText(post.getTitle());
        // 设置作者标签的文本，包含作者、发布日期和点赞数
        authorLabel.setText(
                "作者: " + post.getAuthor() + "   发布日期: " + post.getDateString() + "   点赞: " + post.getLikes());
        // 设置内容文本区域的文本为帖子的内容
        contentArea.setText(post.getContent());

        // 清空回帖列表的数据模型
        replyListModel.clear();
        // 遍历帖子的所有回帖，将其添加到回帖列表的数据模型中
        for (Reply reply : post.getReplies()) {
            replyListModel.addElement(reply);
        }

        // 清空回帖输入文本区域的内容
        replyArea.setText("");
    }

    /**
     * 提交回帖的方法。
     * 检查回帖内容是否为空，若不为空则添加回帖到帖子中，并更新数据。
     */
    private void submitReply() {
        // 检查当前帖子是否为空，若为空则直接返回
        if (currentPost == null) {
            return;
        }

        // 获取回帖输入文本区域的内容
        String replyContent = replyArea.getText();
        // 检查回帖内容是否为空，若为空则弹出错误提示框并返回
        if (replyContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "回复内容不能为空！", "回复失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建新的回帖对象，使用当前用户作为作者
        Reply reply = new Reply(replyContent, mainFrame.getCurrentUser().getUsername());
        // 调用数据管理器的方法将回帖添加到当前帖子中
        dataManager.addReply(currentPost.getId(), reply);

        // 弹出提示框，告知用户回帖成功
        JOptionPane.showMessageDialog(this, "回复成功！");
        // 刷新帖子信息，更新回帖列表
        setPost(currentPost);
    }

    /**
     * 点赞帖子的方法。
     * 增加帖子的点赞数，并更新数据。
     */
    private void likePost() {
        // 检查当前帖子是否为空，若为空则直接返回
        if (currentPost == null) {
            return;
        }

        // 调用帖子对象的方法增加点赞数
        currentPost.addLike();
        // 调用数据管理器的方法更新帖子信息
        dataManager.updatePost(currentPost);

        // 弹出提示框，告知用户点赞成功
        JOptionPane.showMessageDialog(this, "点赞成功！");
        // 刷新帖子信息，更新点赞数显示
        setPost(currentPost);
        // 刷新论坛主面板的帖子列表，更新点赞数显示
        mainFrame.refreshForumPanel(); 
    }
}