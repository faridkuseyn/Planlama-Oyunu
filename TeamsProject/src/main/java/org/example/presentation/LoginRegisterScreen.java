package org.example.presentation;


import org.example.cronjob.NotificationCronjob;
import org.example.model.Event;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class LoginRegisterScreen extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField registerUsernameField;
    private final JPasswordField registerPasswordField;
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField tcField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JComboBox<String> userTypeComboBox;

    private final UserRepository userRepository;

    public LoginRegisterScreen() {
        // Kullanıcı veritabanını oluştur
        userRepository = new UserRepository();

        // Giriş ve kayıt ekranını içeren ana paneli oluştur
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Giriş ekranını oluştur
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kullanıcı adı ve şifrenin doğruluğunu kontrol et
                User user = userRepository.getUserByUsername(username);
                if (user != null && user.getPassword().equals(password)) {
                    // Giriş başarılı, ana ekranı göster
                    showMainScreen(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Boş alan
        loginPanel.add(loginButton);

        // Kayıt ekranını oluştur
        JPanel registerPanel = new JPanel(new GridLayout(6, 2));

        JLabel registerUsernameLabel = new JLabel("Username:");
        registerUsernameField = new JTextField(20);
        JLabel registerPasswordLabel = new JLabel("Password:");
        registerPasswordField = new JPasswordField(20);
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JLabel surnameLabel = new JLabel("Surname:");
        surnameField = new JTextField(20);
        JLabel tcLabel = new JLabel("TC ID:");
        tcField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JLabel userTypeLabel = new JLabel("User Type:");
        String[] userTypes = {"Admin", "User"};
        userTypeComboBox = new JComboBox<>(userTypes);
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = registerUsernameField.getText();
                String password = new String(registerPasswordField.getPassword());
                String name = nameField.getText();
                String surname = surnameField.getText();
                String tc = tcField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                String usertype = (String) userTypeComboBox.getSelectedItem();

                // Kullanıcıyı kaydet
                User newUser = new User(username, password, name, surname, tc, phone, email, address, usertype);
                if (userRepository.addUser(newUser)) {
                    JOptionPane.showMessageDialog(null, "Registration successful", "Registration", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Please try again.", "Registration", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerPanel.add(registerUsernameLabel);
        registerPanel.add(registerUsernameField);
        registerPanel.add(registerPasswordLabel);
        registerPanel.add(registerPasswordField);
        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(surnameLabel);
        registerPanel.add(surnameField);
        registerPanel.add(tcLabel);
        registerPanel.add(tcField);
        registerPanel.add(phoneLabel);
        registerPanel.add(phoneField);
        registerPanel.add(emailLabel);
        registerPanel.add(emailField);
        registerPanel.add(addressLabel);
        registerPanel.add(addressField);
        registerPanel.add(userTypeLabel);
        registerPanel.add(userTypeComboBox);
        registerPanel.add(new JLabel()); // Boş alan
        registerPanel.add(registerButton);

        // Ana paneli oluşturulan giriş ve kayıt panelleriyle güncelle
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(registerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void showMainScreen(User user) {
        // Ana ekranı göstermek için bir JFrame oluşturabilirsiniz
        JFrame mainScreen = new JFrame("Calendar");
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.setSize(800, 600);

        MainFrame mainFrame = new MainFrame();
        mainFrame.CalendarApp();


        // Giriş ekranını kapat
        dispose();
    }


    public static void main(String[] args) {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                NotificationCronjob notificationCronjob = new NotificationCronjob();
                notificationCronjob.main();
            }
        };
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, 1, 20, TimeUnit.SECONDS);
        //period ile ne kadar sıklıkla bildirim göndereliceği değiştirilebilir


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginRegisterScreen calendar = new LoginRegisterScreen();
                calendar.setTitle("Login / Register");
                calendar.setSize(800, 600);
                calendar.setResizable(false);
                calendar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                calendar.setVisible(true);
            }
        });
    }
}
