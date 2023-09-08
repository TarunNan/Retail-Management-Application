import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A java class used to create the frame for sellers.
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Alexa Lau
 * @version May 1, 2023
 */
public class SellerFrame extends JComponent implements Runnable {

    // Private variables
    private Seller seller;

    // Frame
    JFrame sellerFrame;

    // Buttons
    JButton createEscapeRoom;
    JButton viewEscapeRoom;
    JButton editAccount;
    JButton approveAppointments;
    JButton storeStats;
    JButton logout;

    public SellerFrame(Seller seller) {
        this.seller = seller;
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new SellerFrame(new Seller("Alexa", "password", "alexa@email.com")));
    //}

    public void run() {
        sellerFrame = new JFrame("Seller");

        GridLayout gridLayout = new GridLayout(0, 1, 20, 20);
        Container content = sellerFrame.getContentPane();
        content.setLayout(gridLayout);

        sellerFrame.setSize(900, 650);
        sellerFrame.setLocationRelativeTo(null);
        sellerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sellerFrame.setVisible(true);

        createEscapeRoom = new JButton("Create a new escape room");
        createEscapeRoom.addActionListener(actionListener);
        viewEscapeRoom = new JButton("View your escape rooms");
        viewEscapeRoom.addActionListener(actionListener);
        editAccount = new JButton("Edit your account");
        editAccount.addActionListener(actionListener);
        approveAppointments = new JButton("Approve pending appointments");
        approveAppointments.addActionListener(actionListener);
        storeStats = new JButton("See store statistics");
        storeStats.addActionListener(actionListener);
        logout = new JButton("Logout");
        logout.addActionListener(actionListener);

        JLabel welcomeLabel = new JLabel("Welcome " + seller.getName() + "!");
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 48));

        content.add(welcomeLabel);
        content.add(createEscapeRoom);
        content.add(viewEscapeRoom);
        content.add(editAccount);
        content.add(approveAppointments);
        content.add(storeStats);
        content.add(logout);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == createEscapeRoom) {
                int option = JOptionPane.showConfirmDialog(null,
                        "Would you like to import a calendar", "Seller", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    String csvFile = JOptionPane.showInputDialog(null,
                            "What csv file would you like to enter?", "Seller", JOptionPane.QUESTION_MESSAGE);
                    if (csvFile != null) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                            String line = reader.readLine();
                            while (line != null) {
                                String temp = line;
                                // String calendarTitle = temp.substring(0, temp.indexOf(','));
                                // temp = temp.substring(temp.indexOf(',')+1);
                                // String storeName = temp.substring(0, temp.indexOf(','));
                                // temp = temp.substring(temp.indexOf(',')+1);
                                // String sellerEmail = temp.substring(0, temp.indexOf(','));
                                // temp = temp.substring(temp.indexOf(',')+1);
                                // String description = temp.substring(0, temp.indexOf(','));
                                String[] data = temp.split(",");
                                Calendar c = new Calendar(data[0], data[3], data[1], data[2]);
                                c.writeCSVFile();
                                line = reader.readLine();
                            }
                            reader.close();
                            JOptionPane.showMessageDialog(null,
                                    "Calendar created successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Calendar failed to create", "Seller", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                } else if (option == JOptionPane.NO_OPTION) {
                    String storeName = JOptionPane.showInputDialog(null,
                            "Please enter the name of the escape room",
                            "Seller", JOptionPane.QUESTION_MESSAGE);
                    if (storeName != null) {
                        Store store = new Store(seller.getEmail(), storeName);
                        store.writeCSVFile();
                        ArrayList<Store> currentStores = seller.getStores();
                        currentStores.add(store);
                        seller.setStores(currentStores);
                        JOptionPane.showMessageDialog(null,
                                "Escape room created successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }

            if (e.getSource() == viewEscapeRoom) {
                SwingUtilities.invokeLater(new ViewEscapeRoomFrame(seller));
            }

            if (e.getSource() == editAccount) {
                SwingUtilities.invokeLater(new SettingsScreen(seller));
            }

            if (e.getSource() == approveAppointments) {
                SwingUtilities.invokeLater(new ViewSellerAppts(seller));
            }

            if (e.getSource() == storeStats) {
                String[] isSortedOptions = {"sorted", "unsorted"};
                String isSorted = (String) JOptionPane.showInputDialog(null,
                        "How would you like to view the statistics?", "Seller",
                        JOptionPane.QUESTION_MESSAGE, null, isSortedOptions, null);
                if (isSorted != null) {
                    if (isSorted.equals(isSortedOptions[0])) {  // Sorted
                        SwingUtilities.invokeLater(new SellerStoreStats(seller, true));
                    }
                    if (isSorted.equals(isSortedOptions[1])) {  // Unsorted
                        SwingUtilities.invokeLater(new SellerStoreStats(seller, false));
                    }
                }
            }

            if (e.getSource() == logout) {
                sellerFrame.dispose();
                JOptionPane.showMessageDialog(null, "You have successfully logged out");
            }
        }
    };
}
