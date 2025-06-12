package ui;

import javax.swing.*;
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

    }
}
