package src.ui;

import javax.swing.*;
import java.awt.*;

import src.main.ForumSystem;
import src.data.ForumDataManager;
import src.data.Post;

public class NewPostPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private JTextField titleField;
    private JTextArea contentArea;

    public NewPostPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        formPanel.add(new JLabel("标题:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(50);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("内容:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentArea = new JTextArea(20, 50);
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        formPanel.add(scrollPane, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("提交");
        submitButton.addActionListener(e -> submitPost());

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            titleField.setText("");
            contentArea.setText("");
            mainFrame.showForumPanel();
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void submitPost() {
        String title = titleField.getText();
        String content = contentArea.getText();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "标题和内容不能为空！", "发布失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Post newPost = new Post(title, content, mainFrame.getCurrentUser().getUsername());
        dataManager.addPost(newPost);

        JOptionPane.showMessageDialog(this, "帖子发布成功！");
        titleField.setText("");
        contentArea.setText("");
        mainFrame.refreshForumPanel();
        mainFrame.showForumPanel();
    }
}