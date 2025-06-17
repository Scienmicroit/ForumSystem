package src.ui;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

import src.data.Post;
import src.data.Reply;
import src.data.ForumDataManager;
import src.main.ForumSystem;

public class ViewPostPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private Post currentPost;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JTextArea contentArea;
    private JList<Reply> replyList;
    private DefaultListModel<Reply> replyListModel;
    private JTextArea replyArea;

    public ViewPostPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        // 帖子内容面板
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("宋体", Font.BOLD, 16));

        authorLabel = new JLabel();
        authorLabel.setFont(new Font("宋体", Font.PLAIN, 12));

        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setFont(new Font("宋体", Font.PLAIN, 14));

        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        postPanel.add(titleLabel);
        postPanel.add(authorLabel);
        postPanel.add(contentScrollPane);

        // 回帖列表
        replyListModel = new DefaultListModel<>();
        replyList = new JList<>(replyListModel);
        replyList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reply) {
                    Reply reply = (Reply) value;
                    setText(String.format("%s - %s (%s)", reply.getAuthor(), reply.getContent(),
                            reply.getDateString()));
                }
                return this;
            }
        });
        JScrollPane replyScrollPane = new JScrollPane(replyList);

        // 回帖输入区域
        JPanel replyInputPanel = new JPanel();
        replyInputPanel.setLayout(new BorderLayout());

        replyArea = new JTextArea(5, 40);
        replyArea.setLineWrap(true);
        JScrollPane replyAreaScrollPane = new JScrollPane(replyArea);

        JPanel buttonPanel = new JPanel();
        JButton replyButton = new JButton("回复");
        replyButton.addActionListener(e -> submitReply());

        JButton likeButton = new JButton("点赞");
        likeButton.addActionListener(e -> likePost());

        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());

        buttonPanel.add(replyButton);
        buttonPanel.add(likeButton);
        buttonPanel.add(backButton);

        replyInputPanel.add(replyAreaScrollPane, BorderLayout.CENTER);
        replyInputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, postPanel, replyScrollPane);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);
        add(replyInputPanel, BorderLayout.SOUTH);
    }

    public void setPost(Post post) {
        this.currentPost = post;
        titleLabel.setText(post.getTitle());
        authorLabel.setText(
                "作者: " + post.getAuthor() + "   发布日期: " + post.getDateString() + "   点赞: " + post.getLikes());
        contentArea.setText(post.getContent());

        replyListModel.clear();
        for (Reply reply : post.getReplies()) {
            replyListModel.addElement(reply);
        }

        replyArea.setText("");
    }

    private void submitReply() {
    }

    private void likePost() {
    }
}