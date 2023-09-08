import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SellerStoreStats extends JComponent implements Runnable {

    private Seller seller;
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

    public SellerStoreStats(Seller seller, boolean sort) {
        this.seller = new Seller(seller.getName(), seller.getPassword(),seller.getEmail());
        this.sort = sort;
        if (sort) {
            for (int i = 0; i < this.seller.getSortedStoreStat().size(); i++) {
                statStuff += this.seller.getSortedStoreStat().get(i) + "\n";
            }
        } else if (!sort) {
            for (int i = 0; i < this.seller.getStoreStat().size(); i++) {
                statStuff += this.seller.getStoreStat().get(i) + "\n";
            }
        }
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new SellerStoreStats(new Seller("Tarun", "tstraw",
    //            "tnandamu@purdue.edu"), sort));
    //}

    public void run() {
        // Setting up frame
        frame = new JFrame("Seller: Stats");
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
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back) {
                frame.dispose();
            }
        }
    };
}
