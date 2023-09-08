import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewPendAppts extends JComponent implements Runnable {

    private Customer customer;
    private int currentApptIndex;

    // Frame and content
    JFrame viewPendingApptFrame;
    Container content;

    // Buttons and combo box
    JButton enterAppt;
    JButton back;
    JComboBox<String> apptOptionsComboBox;

    // Grid layout and layered panes
    GridLayout gridLayout;

    public ViewPendAppts(Customer customer) {
        this.customer = new Customer(customer.getName(), customer.getPassword(), customer.getEmail());
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new ViewAppAppts(new Customer("Tarun", "tstraw", "tnandamu@purdue.edu")));
    //}

    public synchronized void run() {
        // Setting up frame
        viewPendingApptFrame = new JFrame("Customer: Pending Appointments");
        viewPendingApptFrame.setSize(900, 650);
        viewPendingApptFrame.setLocationRelativeTo(null);
        viewPendingApptFrame.setVisible(true);
        viewPendingApptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        content = viewPendingApptFrame.getContentPane();
        content.setLayout(gridLayout);

        // Prints out customer's stores
        ArrayList<Appointment> pendingApp = customer.getPendApts();
        String[] appOptionsArray = new String[pendingApp.size()];
        for (int i = 0; i < pendingApp.size(); i++) {
            String message = (i + 1) + ". " + pendingApp.get(i).toString();
            content.add(new Label(message));

            appOptionsArray[i] = i + 1 + "";
        }

        // Put buttons and combo box on customer side
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
        content.add(panel, BorderLayout.SOUTH);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterAppt) {
                currentApptIndex = apptOptionsComboBox.getSelectedIndex();
                ArrayList<String> pendApptTitle = new ArrayList<String>();
                for(int i = 0; i < customer.getPendApts().size(); i ++){
                    pendApptTitle.add(customer.getPendApts().get(i).getTitle());
                }
                String indexAndStore = (currentApptIndex + 1) + ". " + pendApptTitle.get(currentApptIndex);
                int enterApptSelection = JOptionPane.showInternalConfirmDialog(null,
                        indexAndStore + "\nWhat you like to cancel this appointment?", "Customer", JOptionPane.YES_NO_OPTION);
                if (enterApptSelection == JOptionPane.YES_OPTION) {
                    synchronized(this) {
                        customer.getPendApts().get(currentApptIndex).delete();
                    }
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! Your Appointment is Cancelled", "Customer", JOptionPane.INFORMATION_MESSAGE);
                }else if(enterApptSelection == JOptionPane.NO_OPTION){
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! Your Appointment is not Cancelled", "Customer", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (e.getSource() == back) {
                viewPendingApptFrame.dispose();
            }
        }

    };
}
