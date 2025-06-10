package src.ui;

import javax.swing.*;
import java.awt.*;

import src.main.ForumSystem;
import src.data.ForumDataManager;
// import src.data.Post;

public class LoginPanel extends JPanel {
    private ForumDataManager dataManager;
    private ForumSystem mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
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
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> login());
        add(loginButton, gbc);

        gbc.gridy = 3;
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> mainFrame.showRegisterPanel());
        add(registerButton, gbc);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (dataManager.authenticate(username, password)) {
            mainFrame.setCurrentUser(dataManager.getUser(username));
            mainFrame.refreshForumPanel();
            mainFrame.showForumPanel();
            JOptionPane.showMessageDialog(this, "登录成功！");
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}