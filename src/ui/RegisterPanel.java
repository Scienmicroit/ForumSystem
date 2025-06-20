package ui;

import data.ForumDataManager;
import data.User;
import java.awt.*;
import javax.swing.*;
import main.ForumSystem;

/**
 * RegisterPanel 类用于创建一个注册面板，用户可以在该面板输入用户名和密码进行注册操作。
 * 该面板提供了输入用户名、密码和确认密码的文本框，以及注册和返回登录页面的按钮。
 */
public class RegisterPanel extends JPanel {
    // 引用论坛系统主窗口，用于面板切换
    private ForumSystem mainFrame;
    // 数据管理器，负责与用户数据进行交互，如添加新用户
    private ForumDataManager dataManager;
    // 用于输入用户名的文本框
    private JTextField usernameField;
    // 用于输入密码的密码框
    private JPasswordField passwordField;
    // 用于再次输入密码以确认的密码框
    private JPasswordField confirmPasswordField;

    /**
     * 构造函数，初始化面板及其组件。
     * 
     * @param mainFrame    论坛系统主窗口实例
     * @param dataManager  数据管理器实例
     */
    public RegisterPanel(ForumSystem mainFrame, ForumDataManager dataManager) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;

        // 设置面板的布局为 GridBagLayout，方便组件的布局管理
        setLayout(new GridBagLayout());
        // 创建 GridBagConstraints 对象，用于设置组件在 GridBagLayout 中的约束条件
        GridBagConstraints gbc = new GridBagConstraints();
        // 设置组件之间的间距
        gbc.insets = new Insets(5, 5, 5, 5);
        // 初始列位置
        gbc.gridx = 0;
        // 初始行位置
        gbc.gridy = 0;

        // 添加用户名标签到面板
        add(new JLabel("用户名:"), gbc);
        // 移动到下一列
        gbc.gridx = 1;
        // 初始化用户名输入文本框
        usernameField = new JTextField(20);
        // 将用户名输入文本框添加到面板
        add(usernameField, gbc);

        // 移动到下一行，第一列
        gbc.gridx = 0;
        gbc.gridy = 1;
        // 添加密码标签到面板
        add(new JLabel("密码:"), gbc);
        // 移动到下一列
        gbc.gridx = 1;
        // 初始化密码输入框
        passwordField = new JPasswordField(20);
        // 将密码输入框添加到面板
        add(passwordField, gbc);

        // 移动到下一行，第一列
        gbc.gridx = 0;
        gbc.gridy = 2;
        // 添加确认密码标签到面板
        add(new JLabel("确认密码:"), gbc);
        // 移动到下一列
        gbc.gridx = 1;
        // 初始化确认密码输入框
        confirmPasswordField = new JPasswordField(20);
        // 将确认密码输入框添加到面板
        add(confirmPasswordField, gbc);

        // 移动到下一行，组件跨越两列
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        // 创建注册按钮
        JButton registerButton = new JButton("注册");
        // 为注册按钮添加点击事件监听器，点击时调用注册方法
        registerButton.addActionListener(e -> register());
        // 将注册按钮添加到面板
        add(registerButton, gbc);

        // 移动到下一行，组件跨越两列
        gbc.gridy = 4;
        // 创建返回按钮
        JButton backButton = new JButton("返回");
        // 为返回按钮添加点击事件监听器，点击时返回登录面板
        backButton.addActionListener(e -> mainFrame.showLoginPanel());
        // 将返回按钮添加到面板
        add(backButton, gbc);
    }

    /**
     * 注册方法，处理用户注册逻辑。
     * 检查用户名和密码的有效性，若有效则创建新用户并添加到数据管理器中。
     */
    private void register() {
        // 获取用户名输入框中的文本
        String username = usernameField.getText();
        // 获取密码输入框中的密码并转换为字符串
        String password = new String(passwordField.getPassword());
        // 获取确认密码输入框中的密码并转换为字符串
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            // 若为空，弹出错误提示框
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查两次输入的密码是否一致
        if (!password.equals(confirmPassword)) {
            // 若不一致，弹出错误提示框
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查用户名是否已存在
        if (dataManager.getUser(username) != null) {
            // 若已存在，弹出错误提示框
            JOptionPane.showMessageDialog(this, "用户名已存在！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建新的用户对象，设置为非管理员
        dataManager.addUser(new User(username, password, false));
        // 弹出提示框，告知用户注册成功并提示登录
        JOptionPane.showMessageDialog(this, "注册成功！请登录");
        // 显示登录面板
        mainFrame.showLoginPanel();
    }
}