import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewSellerAppts extends JComponent implements Runnable {

    private Seller seller;
    private int currentApptIndex;

    // Frame and content
    JFrame viewAppApptFrame;
    Container content;

    // Buttons and combo box
    JButton enterAppt;
    JButton back;
    JComboBox<String> apptOptionsComboBox;

    // Grid layout and layered panes
    GridLayout gridLayout;

    public ViewSellerAppts(Seller seller) {
        this.seller = new Seller(seller.getName(), seller.getPassword(), seller.getEmail());
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new ViewSellerAppts(new Seller("Tarun", "tstraw", "tnandamu@purdue.edu")));
    //}

    public synchronized void run() {
        // Setting up frame
        viewAppApptFrame = new JFrame("Seller: Requested Appointments");
        viewAppApptFrame.setSize(900, 650);
        viewAppApptFrame.setLocationRelativeTo(null);
        viewAppApptFrame.setVisible(true);
        viewAppApptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        content = viewAppApptFrame.getContentPane();
        content.setLayout(gridLayout);

        // Prints out seller's apppointments
        ArrayList<Appointment> apptReqs = seller.getApptReqs();
        String[] appOptionsArray = new String[apptReqs.size()];
        for (int i = 0; i < apptReqs.size(); i++) {
            String message = (i + 1) + ". " + apptReqs.get(i).toString();
            String[] lines = message.split("\n");
            appOptionsArray[i] = i + 1 + "";
        }

        // Put buttons and combo box on seller side
        JPanel panel = new JPanel();
        apptOptionsComboBox = new JComboBox<>(appOptionsArray);
        apptOptionsComboBox.setVisible(true);
        panel.add(apptOptionsComboBox);
        enterAppt = new JButton("Enter Appointment");
        enterAppt.addActionListener(actionListener);
        panel.add(enterAppt);
        back = new JButton("Back");
        back.addActionListener(actionListener);
        panel.add(back);
        panel.setBounds(5, 550, 400, 40);
        content.add(panel, BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterAppt) {
                currentApptIndex = apptOptionsComboBox.getSelectedIndex();
                ArrayList<String> appApptTitle = new ArrayList<String>();
                for(int i = 0; i < seller.getApptReqs().size(); i ++){
                    appApptTitle.add(seller.getApptReqs().get(i).getTitle());
                }
                String indexAndStore = (currentApptIndex + 1) + ". " + appApptTitle.get(currentApptIndex);
                int enterApptSelection = JOptionPane.showInternalConfirmDialog(null,
                        indexAndStore + "\nWould you like to approve this appointment?", "Seller", JOptionPane.YES_NO_OPTION);
                if (enterApptSelection == JOptionPane.YES_OPTION) {
                    synchronized (this) {
                        seller.getApptReqs().get(currentApptIndex).setStatus("approved");
                    }
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! Your Appointment is Approved", "Seller", JOptionPane.INFORMATION_MESSAGE);
                }else if(enterApptSelection == JOptionPane.NO_OPTION){
                    synchronized(this) {
                        seller.getApptReqs().get(currentApptIndex).setStatus("declined");
                    }
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! This Appointment is Declined", "Seller", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (e.getSource() == back) {
                viewAppApptFrame.dispose();
            }
        }
    };
}
