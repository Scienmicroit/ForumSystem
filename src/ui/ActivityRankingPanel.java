package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import data.ForumDataManager;
import data.User;
import main.ForumSystem;

public class ActivityRankingPanel extends JPanel {
    private ForumDataManager dataManager;
    private JTable rankingTable;
    private DefaultTableModel tableModel;

    public ActivityRankingPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[] { "排名", "用户名", "发帖数", "回帖数", "总活跃度" }, 0);

        rankingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshRanking() {
    }
}