package ui;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

import data.Post;
import data.Reply;
import data.ForumDataManager;
import main.ForumSystem;

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

    }
}