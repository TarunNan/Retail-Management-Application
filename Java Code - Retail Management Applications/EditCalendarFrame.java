import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * A java class that allows sellers to make calendars and
 * appointment windows.
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 *
 * @author Raelee Lance
 * @version May 1, 2023
 */
public class EditCalendarFrame extends JComponent implements Runnable {

    // Private variables
    private Seller seller;
    private String enteredStore;
    private String enteredCalendar;
    private AppointmentWindow enteredAppointmentWindow;

    // Frame
    JFrame editCalendarFrame;

    // Grid layout
    GridLayout gridLayout;

    // Text fields and text areas
    JTextField appointmentWindowNameField = new JTextField(10);
    JTextField appointmentWindowMaxAttendeesField = new JTextField(5);

    // Drop down box
    JComboBox<Integer> calenderDayBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
    JComboBox<Integer> startTimeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});
    JComboBox<Integer> endTimeBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});

    // Buttons
    JButton enterButton;
    JButton backButton;

    public EditCalendarFrame(Seller seller, String enteredStore, String enteredCalendar) {
        // TODO
        // What should be in parameters:
        // Stuff to initialize seller
        // Whatever store user is creating a calender in
        this.seller = seller;
        this.enteredStore = enteredStore;
        this.enteredCalendar = enteredCalendar;
    }

    public EditCalendarFrame(Seller seller, String enteredStore, String enteredCalendar, AppointmentWindow enteredAppointmentWindow) {
        this.seller = seller;
        this.enteredStore = enteredStore;
        this.enteredCalendar = enteredCalendar;
        this.enteredAppointmentWindow = enteredAppointmentWindow;
    }


    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(new EditCalendarFrame("Alexa",
    //            "password2", "alexa@email.com", "store"));
    //}


    public synchronized void run() {
        editCalendarFrame = new JFrame("Seller");
        editCalendarFrame.setSize(900, 650);
        editCalendarFrame.setLocationRelativeTo(null);
        editCalendarFrame.setVisible(true);
        editCalendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 1);
        Container content = editCalendarFrame.getContentPane();
        content.setLayout(gridLayout);

        JPanel panel1 = new JPanel();
        JLabel heading = new JLabel("Store: " + enteredStore + ", Calender: " + enteredCalendar);
        if (enteredAppointmentWindow != null) {
            heading.setText("Store: " + enteredStore + ", Calender: " + enteredCalendar +
                    ", Appt Window: " + enteredAppointmentWindow.getTitle());
        }
        heading.setFont(new Font("Serif", Font.PLAIN, 30));
        panel1.add(heading);
        content.add(panel1);

        content.add(new JPanel());      // Hopefully leave a gap between panel1 and panel2

        JPanel panel2 = new JPanel();   // Calendar name
        panel2.add(new JLabel(
                "Please enter the title of the appointment window you would like to create:"));
        if (enteredAppointmentWindow != null) {
            appointmentWindowNameField.setText(enteredAppointmentWindow.getTitle());
        }
        appointmentWindowNameField.setVisible(true);
        panel2.add(appointmentWindowNameField);
        content.add(panel2);

        JPanel panel4 = new JPanel();   // Calendar day
        panel4.add(new JLabel("Please select a day:"));
        if (enteredAppointmentWindow != null) {
            calenderDayBox.setSelectedIndex(enteredAppointmentWindow.getDate() - 1);
        }
        calenderDayBox.setVisible(true);
        panel4.add(calenderDayBox);
        content.add(panel4);

        JPanel panel6 = new JPanel();   // Start time
        panel6.add(new JLabel("Please select a start time:"));
        if (enteredAppointmentWindow != null) {
            startTimeBox.setSelectedIndex(enteredAppointmentWindow.getStart() - 1);
        }
        startTimeBox.setVisible(true);
        panel6.add(startTimeBox);
        content.add(panel6);

        JPanel panel7 = new JPanel();   // End time
        panel7.add(new JLabel("Please select an end time:"));
        if (enteredAppointmentWindow != null) {
            endTimeBox.setSelectedIndex(enteredAppointmentWindow.getEnd() - 1);
        }
        endTimeBox.setVisible(true);
        panel7.add(endTimeBox);
        content.add(panel7);

        JPanel panel8 = new JPanel();   // Max attendees
        panel8.add(new JLabel("Please enter the max number of attendees:"));
        if (enteredAppointmentWindow != null) {
            appointmentWindowMaxAttendeesField.setText(enteredAppointmentWindow.getMaxNumAttend() + "");
        }
        appointmentWindowMaxAttendeesField.setVisible(true);
        panel8.add(appointmentWindowMaxAttendeesField);
        content.add(panel8);

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
                boolean everythingCorrect = true;
                if (appointmentWindowNameField.getText() == null || appointmentWindowNameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a title", "Seller", JOptionPane.ERROR_MESSAGE);
                    everythingCorrect = false;
                }
                if (startTimeBox.getSelectedIndex() >= endTimeBox.getSelectedIndex()) {
                    JOptionPane.showMessageDialog(null, "Please check start and end times",
                            "Seller", JOptionPane.ERROR_MESSAGE);
                    everythingCorrect = false;
                }
                try {
                    if (Integer.parseInt(appointmentWindowMaxAttendeesField.getText()) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a value greater than 0",
                                "Seller", JOptionPane.ERROR_MESSAGE);
                        everythingCorrect = false;
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a number whose value is greater than 0",
                            "Seller", JOptionPane.ERROR_MESSAGE);
                    everythingCorrect = false;
                }
                if (everythingCorrect) {
                    if (enteredAppointmentWindow == null) {
                        // TODO: numBooking and approvedAppointments
                        AppointmentWindow newAppointmentWindow = new AppointmentWindow(
                                appointmentWindowNameField.getText(), enteredStore,
                                startTimeBox.getSelectedIndex() + 1, endTimeBox.getSelectedIndex() + 1,
                                calenderDayBox.getSelectedIndex() + 1, seller.getEmail(),
                                Integer.parseInt(appointmentWindowMaxAttendeesField.getText()), 0,
                                enteredCalendar, null);
                        newAppointmentWindow.writeCSVFile();

                        Calendar currentCalendar = null;
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader("Calendar.csv"));
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
                                if (data[0].equals(enteredCalendar) && data[2].equals(seller.getEmail())) {
                                    currentCalendar = new Calendar(data[0], data[3], data[1], data[2]);
                                    break;
                                }
                                line = reader.readLine();
                            }
                            reader.close();



                        } catch (FileNotFoundException exception) {
                            // System.out.println("Calendar.csv file not found");
                        } catch (IOException exception) {
                            // System.out.println("An error occurred when trying to create the Calendars for this Store");
                        }
                        //BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"))
                        ArrayList<AppointmentWindow> cAppointments = currentCalendar.getAppointmentWindow();
                        cAppointments.add(newAppointmentWindow);
                        currentCalendar.setAppointmentWindows(cAppointments);
                        JOptionPane.showMessageDialog(null,
                                "Appointment window created successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                        editCalendarFrame.dispose();
                    } else {
                        // TODO
                        if (!enteredAppointmentWindow.getTitle().equals(appointmentWindowNameField.getText())) {
                            JOptionPane.showMessageDialog(null, "You cannot change an appoint window name\n" +
                                    "Name set back to " + enteredAppointmentWindow.getTitle());
                            if (enteredAppointmentWindow != null) {
                                appointmentWindowNameField.setText(enteredAppointmentWindow.getTitle());
                            }
                        }
                        enteredAppointmentWindow.setDate(calenderDayBox.getSelectedIndex() + 1);
                        enteredAppointmentWindow.setStart(startTimeBox.getSelectedIndex() + 1);
                        enteredAppointmentWindow.setEnd(endTimeBox.getSelectedIndex() + 1);
                        enteredAppointmentWindow.setMaxNumAttend(Integer.parseInt(appointmentWindowMaxAttendeesField.getText()));
                        JOptionPane.showMessageDialog(null,
                                "Appointment window edited successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                        editCalendarFrame.dispose();
                    }
                }
            }
            if (e.getSource() == backButton) {
                editCalendarFrame.dispose();
            }
        }
    };
}
