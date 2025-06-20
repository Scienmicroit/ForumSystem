package ui;

import javax.swing.*;
import java.awt.*;

import main.ForumSystem;
import data.ForumDataManager;
// import src.data.Post;

public class LoginPanel extends JPanel {
    /**
     * 登陆界面属性
     * dataManager,数据管理器验证用户登录
     * mainFrame,主窗口引用,用于切换面板等功能
     * usernameField,passwordField,输入框
     */
    private ForumDataManager dataManager;
    private ForumSystem mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * 初始化登陆面板
     * dataManager,数据管理器验证用户登录
     * mainFrame,主窗口引用,用于切换面板等功能
     */
    public LoginPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        // 为当前容器设置布局管理器为GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // 设置组件的间距
        gbc.insets = new Insets(5, 5, 5, 5);

        // 添加用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // 添加密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("密码"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // 添加登录按钮并注册事件监听器
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> login());
        add(loginButton, gbc);

        // 添加注册按钮并注册事件监听器
        gbc.gridy = 3;

        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> mainFrame.showRegisterPanel());
        add(registerButton, gbc);
    }

    /**
     * 处理登录按钮点击事件
     * 需要进行验证当前用户名与密码,并根据验证结果显示相应的信息,
     */
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