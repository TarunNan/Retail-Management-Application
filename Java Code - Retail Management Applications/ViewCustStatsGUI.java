import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewCustStatsGUI extends JComponent implements Runnable {

    private Customer customer;
    boolean sort;

    // Frame and content
    JFrame frame;
    Container content;

    // Buttons
    JLabel sortQ;
    JButton sorted;
    JButton unsorted;
    JButton back;

    // Grid layout and layered panes
    GridLayout gridLayout;

    public ViewCustStatsGUI(Customer customer) {
        this.customer = customer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ViewCustStatsGUI(new Customer("Tarun", "tstraw", "tnandamu@purdue.edu")));
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
        sortQ = new JLabel("How would you like to view the customer stats?");
        panel.add(sortQ);
        sorted = new JButton("Sorted");
        sorted.addActionListener(actionListener);
        panel.add(sorted);
        unsorted = new JButton("Unsorted");
        unsorted.addActionListener(actionListener);
        panel.add(unsorted);
        back = new JButton("Back");
        back.addActionListener(actionListener);
        panel.add(back);
        panel.setBounds(5, 550, 400, 40);
        content.add(panel, BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == sorted) {
                SwingUtilities.invokeLater(new StatsScreen(customer, true));
            }
            if (e.getSource() == unsorted) {
                SwingUtilities.invokeLater(new StatsScreen(customer, false));
            }
            if (e.getSource() == back) {
                frame.dispose();
            }
        }
    };
}
