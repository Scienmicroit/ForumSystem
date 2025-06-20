package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import data.ForumDataManager;
import data.User;
import main.ForumSystem;

public class ActivityRankingPanel extends JPanel {
    // 数据管理器：用于获取用户活跃度、用户信息等数据
    private ForumDataManager dataManager;
    // 排行榜表格：展示用户排名、发帖数、回帖数等信息
    private JTable rankingTable;
    // 表格模型：管理表格数据，支持动态增删行
    private DefaultTableModel tableModel;

    /**
     * 构造方法 - 初始化界面组件和布局
     * 主窗口引用，用于切换回论坛主面板
     * 数据管理器，提供数据访问支持
     */
    public ActivityRankingPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.dataManager = dataManager;
        // 设置布局为BorderLayout（边界布局）
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[] { "排名", "用户名", "发帖数", "回帖数", "总活跃度" }, 0);
        // 创建表格并关联模型，配合滚动面板使用
        rankingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        // 将表格放入中心区域（自动占满剩余空间）
        add(scrollPane, BorderLayout.CENTER);

        // 底部按钮面板：放置返回按钮
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showForumPanel());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 刷新排行榜数据 - 从数据管理器获取最新活跃度排行，更新表格内容
     */
    public void refreshRanking() {
        // 清空原有数据行
        tableModel.setRowCount(0);
        // 获取活跃度排行（已按活跃度降序排序）
        Map<String, Integer> ranking = dataManager.getActivityRanking();
        int rank = 1;

        for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
            User user = dataManager.getUser(entry.getKey());
            if (user != null) {
                tableModel.addRow(new Object[] {
                        rank++,
                        entry.getKey(),
                        user.getPostCount(),
                        user.getReplyCount(),
                        entry.getValue()
                });
            }
        }
    }
}