import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
/**
 *
 * A java class used to choose whether a user wants to edit
 * their name or password
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 * @author Tarun Nandamudi
 * @version May 1, 2023
 */
public class SettingsScreen extends JComponent implements Runnable {
    private JButton editPassword;
    Socket socket;
    private JButton editName;
    //private JButton editEmail;
    private JFrame setting;
    private JLabel titleLabel;
    private JLabel whattodo;
    private Seller seller;
    private Customer customer;
    private String email;

    private boolean isSeller;

    //public static void main(String[] args) {
    //}

    @Override
    public synchronized void run() {
        setting = new JFrame("Escape Room Scheduler Application");
        Container settingcontent = setting.getContentPane();
        settingcontent.setLayout(new GridLayout(4, 1));
        setting.setSize(600, 400);
        setting.setLocationRelativeTo(null);
        setting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setting.setVisible(true);
        titleLabel = new JLabel("Settings Page for " + this.email);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        settingcontent.add(titleLabel);

        whattodo = new JLabel("Click the Button Corresponding to What Part of Your Account You Want to Edit.");
        whattodo.setFont(new Font("Tahoma", Font.BOLD, 11));
        whattodo.setVerticalAlignment(SwingConstants.CENTER);
        whattodo.setHorizontalAlignment(SwingConstants.CENTER);
        settingcontent.add(whattodo);

        editName = new JButton("Change Name");
        //editEmail = new JButton("Change Email");
        editPassword = new JButton("Change Password");
        JPanel settingpanel = new JPanel();
        settingpanel.add(editName);
        //settingpanel.add(editEmail);
        settingpanel.add(editPassword);
        editName.addActionListener(actionListener);
        //editEmail.addActionListener(actionListener);
        editPassword.addActionListener(actionListener);
        settingcontent.add(settingpanel, BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == editName) {
                if (isSeller) {
                    try {
                        SwingUtilities.invokeLater(new EditName(seller, socket));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        SwingUtilities.invokeLater(new EditName(customer, socket));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                //} else if (e.getSource() == editEmail) {
                //    if (isSeller) {
                //        SwingUtilities.invokeLater(new EditEmail(seller));
                //    } else {
                //        SwingUtilities.invokeLater(new EditEmail(customer));
                //    }
            } else if (e.getSource() == editPassword) {
                if (isSeller) {
                    try {
                        SwingUtilities.invokeLater(new EditPassword(seller, socket));
                    } catch (IOException ex) {
                    }
                } else {
                    try {
                        SwingUtilities.invokeLater(new EditPassword(customer, socket));
                    } catch (IOException ex) {
                    }
                }
            }
        }
    };

    public SettingsScreen(Seller s, Socket socket) {
        this.seller = s;
        this.email = s.getEmail();
        this.isSeller = true;
        this.socket = socket;
    }

    public SettingsScreen(Customer customer, Socket socket) {
        this.customer = customer;
        this.email = customer.getEmail();
        this.isSeller = false;
        this.socket = socket;
    }

}
