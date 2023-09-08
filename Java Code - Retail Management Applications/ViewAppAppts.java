import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewAppAppts extends JComponent implements Runnable {

    private Customer customer;
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

    public ViewAppAppts(Customer customer) {
        this.customer = customer;
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new ViewAppAppts(new Customer("Tarun", "tstraw", "tnandamu@purdue.edu")));
    //}

    public void run() {
        // Setting up frame
        viewAppApptFrame = new JFrame("Customer: Approved Appointments");
        viewAppApptFrame.setSize(900, 650);
        viewAppApptFrame.setLocationRelativeTo(null);
        viewAppApptFrame.setVisible(true);
        viewAppApptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        content = viewAppApptFrame.getContentPane();
        content.setLayout(gridLayout);

        // Prints out customer's stores
        ArrayList<Appointment> approvedApp = customer.getAppApts();
        String[] appOptionsArray = new String[approvedApp.size()];
        for (int i = 0; i < approvedApp.size(); i++) {
            String message = (i + 1) + ". " + approvedApp.get(i).toString();
            //String[] lines = message.split("\n");
            content.add(new JLabel(message));
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
                ArrayList<String> appApptTitle = new ArrayList<String>();
                for(int i = 0; i < customer.getAppApts().size(); i ++){
                    appApptTitle.add(customer.getAppApts().get(i).getTitle());
                }
                String indexAndStore = (currentApptIndex + 1) + ". " + appApptTitle.get(currentApptIndex);
                int enterApptSelection = JOptionPane.showInternalConfirmDialog(null,
                        indexAndStore + "\nWhat you like to cancel this appointment?", "Customer", JOptionPane.YES_NO_OPTION);
                if (enterApptSelection == JOptionPane.YES_OPTION) {
                    customer.getAppApts().get(currentApptIndex).delete();
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! Your Appointment is Cancelled", "Customer", JOptionPane.INFORMATION_MESSAGE);
                }else if(enterApptSelection == JOptionPane.NO_OPTION){
                    JOptionPane.showMessageDialog(null,
                            indexAndStore + "Ok! Your Appointment is not Cancelled", "Customer", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (e.getSource() == back) {
                viewAppApptFrame.dispose();
            }
        }

    };
}
