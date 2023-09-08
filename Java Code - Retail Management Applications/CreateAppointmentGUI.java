import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * A java class that is used for creating appointments
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 * 
 *
 * @author Tarun Nandamudi & Raelee Lance
 * @version May 1, 2023
 */
public class CreateAppointmentGUI extends JComponent implements Runnable {

    // Private variables
    private Customer customer;
    private Appointment appointment;
    private String enteredStore;

    // Frame
    JFrame createApptFrame;

    // Grid layout
    GridLayout gridLayout;

    // Text fields and text areas
    JTextField calendarNameField = new JTextField(10);
    JTextArea calendarDescriptionArea = new JTextArea(0, 20);
    JTextField calendarTitleField = new JTextField(10);       // TODO ask about this
    JTextField calendarMaxAttendeesField = new JTextField(5);

    // Drop down box
    JComboBox<Integer> calenderDayBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
    JComboBox<Integer> startTimeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});
    JComboBox<Integer> endTimeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});

    // Buttons
    JButton enterButton;
    JButton backButton;

    public CreateAppointmentGUI(String name, String password, String email, String enteredStore) {
        // TODO
        // What should be in parameters:
        // Stuff to initialize seller
        // Whatever store user is creating a calender in
        customer = new Customer(name, password, email);
        this.enteredStore = enteredStore;
    }

    //public static void main(String[] args) {
    //   SwingUtilities.invokeLater(new EditCalendarFrame("Alexa",
    //            "password2", "alexa@email.com", "store"));
    //}

    public synchronized void run() {
        createApptFrame = new JFrame("Seller");
        createApptFrame.setSize(900, 650);
        createApptFrame.setLocationRelativeTo(null);
        createApptFrame.setVisible(true);
        createApptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        Container content = createApptFrame.getContentPane();
        content.setLayout(gridLayout);

        JPanel panel1 = new JPanel();
        JLabel heading = new JLabel("Store: " + enteredStore);
        appointment.setStoreTitle(enteredStore);
        heading.setFont(new Font("Serif", Font.PLAIN, 30));
        panel1.add(heading);
        content.add(panel1);

        content.add(new JPanel());      // Hopefully leave a gap between panel1 and panel2

        JPanel panel2 = new JPanel();   // Appointment title
        panel2.add(new JLabel("Please enter an appointment title:"));
        calendarNameField.setVisible(true);
        panel2.add(calendarNameField);
        appointment.setTitle(calendarNameField.toString());
        content.add(panel2);

        //TODO: STOPPED EDITING HERE
        // need start & end time, date, party size, status, requested by, calendar title, last modified, appt window title
        // for appt constructor


        JPanel panel3 = new JPanel();   // Calendar description
        panel3.add(new JLabel(
                "Please enter the description of the calendar:"));
        calendarDescriptionArea.setVisible(true);
        panel3.add(calendarDescriptionArea);
        content.add(panel3);

        JPanel panel4 = new JPanel();   // Calendar day
        panel4.add(new JLabel("Please select a day:"));
        calenderDayBox.setVisible(true);
        panel4.add(calenderDayBox);
        content.add(panel4);

        JPanel panel5 = new JPanel();   // Calendar title
        // TODO

        JPanel panel6 = new JPanel();   // Appointment window
        panel6.add(new JLabel("Please select an available appointment window:"));
        startTimeBox.setVisible(true);
        panel6.add(startTimeBox);
        content.add(panel6);

        JPanel panel7 = new JPanel();   // Max attendees
        panel7.add(new JLabel("Please enter the max number of attendees:"));
        calendarMaxAttendeesField.setVisible(true);
        panel7.add(calendarMaxAttendeesField);
        content.add(panel7);

        JPanel panelBottom = new JPanel();  // Enter and back button
        enterButton = new JButton("Enter");
        enterButton.setVisible(true);
        enterButton.addActionListener(actionListener);
        panelBottom.add(enterButton);
        backButton = new JButton("Back");
        backButton.setVisible(true);
        backButton.addActionListener(actionListener);
        panelBottom.add(backButton);
        content.add(panelBottom, BorderLayout.SOUTH);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterButton) {

            }
            if (e.getSource() == backButton) {
                createApptFrame.dispose();
            }
        }
    };
}
