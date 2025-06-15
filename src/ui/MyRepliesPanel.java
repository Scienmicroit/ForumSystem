package ui;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;

import main.ForumSystem;
import data.*;

public class MyRepliesPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private JList<Reply> replyList;
    private DefaultListModel<Reply> listModel;
    private Map<Reply, Post> replyPostMap;

    public MyRepliesPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;
        setLayout(new BorderLayout());
        replyPostMap = new HashMap<>();

        listModel = new DefaultListModel<>();
        replyList = new JList<>(listModel);
        replyList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reply) {
                    Reply reply = (Reply) value;
                    setText(String.format("回帖内容: %s\n日期: %s", reply.getContent(), reply.getDateString()));
                }
                return this;
            }
        });

        replyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Reply selectedReply = replyList.getSelectedValue();
                    if (selectedReply != null && replyPostMap.containsKey(selectedReply)) {
                        Post post = replyPostMap.get(selectedReply);
                        mainFrame.getViewPostPanel().setPost(post);
                        mainFrame.showViewPostPanel();
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(replyList);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
