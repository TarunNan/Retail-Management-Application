import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
/**
 * A java class that is the main page for customers
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 * 
 *
 * @author Raelee Lance
 * @version May 1, 2023
 */
public class CustomerGUI extends JComponent implements Runnable {
    Customer customer;
    private String email;
    private String password;
    private String role;
    private String name;

    JFrame jf;
    JButton viewStores;
    JButton viewConfApts;
    JButton viewPendApts;
    JButton viewCustStats;
    JButton editAccount;
    JButton logout;
    JLabel welcome;
    Socket socket;

    public CustomerGUI(Customer customer, Socket socket) {
        this.customer = customer;
        this.socket = socket;
    }
    //public static void main(String[] args) {
    //SwingUtilities.invokeLater(new CustomerGUI("email", "pw", "customer", "name"));
    //}
    public synchronized void run() {
        jf = new JFrame();
        jf.setTitle("Customer");

        GridLayout gridLayout = new GridLayout(7, 1, 200, 20);
        Container content = jf.getContentPane();
        content.setLayout(gridLayout);
        jf.setSize(900, 650);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);

        welcome = new JLabel("Welcome " + customer.getName() + "!");
        jf.add(welcome);
        welcome.setFont(new Font("Serif", Font.PLAIN, 48));
        welcome.setVerticalAlignment(SwingConstants.CENTER);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        viewStores = new JButton("View Stores");
        viewStores.setSize(new Dimension(30, 10));
        viewStores.setFont(new Font("Serif", Font.PLAIN, 24));
        viewStores.addActionListener(actionListener);

        viewConfApts = new JButton("View Confirmed Appointments");
        viewConfApts.setFont(new Font("Serif", Font.PLAIN, 24));
        viewConfApts.addActionListener(actionListener);

        viewPendApts = new JButton("View Pending Appointments");
        viewPendApts.setFont(new Font("Serif", Font.PLAIN, 24));
        viewPendApts.addActionListener(actionListener);

        viewCustStats = new JButton("View Customer Stats");
        viewCustStats.setFont(new Font("Serif", Font.PLAIN, 24));
        viewCustStats.addActionListener(actionListener);

        editAccount = new JButton("Edit Account");
        editAccount.setFont(new Font("Serif", Font.PLAIN, 24));
        editAccount.addActionListener(actionListener);

        logout = new JButton("Logout");
        logout.setFont(new Font("Serif", Font.PLAIN, 24));
        logout.addActionListener(actionListener);

        content.add(welcome);
        content.add(viewStores);
        content.add(viewConfApts);
        content.add(viewPendApts);
        content.add(viewCustStats);
        content.add(editAccount);
        content.add(logout);
    }
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewStores) {
                try {
                    SwingUtilities.invokeLater(new ViewStoresGUI(customer, socket));
                } catch (IOException ex) {
                }
            }
            if (e.getSource() == viewConfApts) {
                SwingUtilities.invokeLater(new ViewAppAppts(customer));
            }
            if (e.getSource() == viewPendApts) {
                SwingUtilities.invokeLater(new ViewPendAppts(customer));
            }
            if (e.getSource() == viewCustStats) {
                SwingUtilities.invokeLater(new ViewCustStatsGUI(customer));
            }
            if (e.getSource() == editAccount) {
                SwingUtilities.invokeLater(new SettingsScreen(customer, socket));
            }
            if (e.getSource() == logout) {
                jf.dispose();
                jf.setVisible(false);
                JOptionPane.showMessageDialog(null, "You have successfully logged out!",
                        "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };
}
