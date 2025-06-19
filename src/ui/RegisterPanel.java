package ui;

import javax.swing.*;
import java.awt.*;

import main.ForumSystem;
import data.ForumDataManager;
import data.User;

public class RegisterPanel extends JPanel {
    private ForumSystem mainFrame;
    private ForumDataManager dataManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("密码:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("确认密码:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> register());
        add(registerButton, gbc);

        gbc.gridy = 4;
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> mainFrame.showLoginPanel());
        add(backButton, gbc);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dataManager.getUser(username) != null) {
            JOptionPane.showMessageDialog(this, "用户名已存在！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dataManager.addUser(new User(username, password, false));
        JOptionPane.showMessageDialog(this, "注册成功！请登录");
        mainFrame.showLoginPanel();
    }

}