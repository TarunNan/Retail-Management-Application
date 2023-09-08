import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StatsScreen extends JComponent implements Runnable {

    private Customer customer;
    static boolean sort;
    private String statStuff;

    // Frame and content
    JFrame frame;
    Container content;

    // Buttons
    JLabel stats;
    JButton back;

    // Grid layout and layered panes
    GridLayout gridLayout;

    public StatsScreen(Customer customer, boolean sort) {
        this.customer = new Customer(customer.getName(), customer.getPassword(),customer.getEmail());
        this.sort = sort;
        if (sort) {
            for (int i = 0; i < customer.sortedStats().size(); i++) {
                statStuff += customer.sortedStats().get(i) + "\n";
            }
        } else if (!sort) {
            for (int i = 0; i < customer.stats().size(); i++) {
                statStuff += customer.stats().get(i) + "\n";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StatsScreen(new Customer("Tarun", "tstraw",
                "tnandamu@purdue.edu"), sort));
    }

    public synchronized void run() {
        // Setting up frame
        frame = new JFrame("Customer: Stats");
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        content = frame.getContentPane();
        content.setLayout(gridLayout);

        // Put buttons on customer side
        JPanel panel = new JPanel();
        stats = new JLabel(statStuff);
        panel.add(stats);
        back = new JButton("Back");
        back.addActionListener(actionListener);
        panel.add(back);
        panel.setBounds(5, 550, 400, 40);
        content.add(panel, BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == back) {
                frame.dispose();
            }
        }
    };
}
