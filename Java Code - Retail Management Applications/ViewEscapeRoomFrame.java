import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewEscapeRoomFrame extends JComponent implements Runnable {

    // Private variables
    private Seller seller;
    private String[] enterStoreOptions = {"Create a calender", "View calendars", "Delete", "Go back"};
    private String[] enterCalendarOptions = {"Create an appointment window", "View appointment windows", "Delete", "Go back"};
    private String[] enterAppointmentWindowOptions = {"Edit appointment window", "Delete", "Go back"};
    private int currentStoreIndex;
    private int currentCalendarIndex;
    private int currentAppointmentWindowIndex;
    ArrayList<Store> sellerStores;
    ArrayList<String> appointmentWindowsString;

    // Frame and content
    JFrame viewEscapeRoomFrame;
    Container content;

    // Buttons and combo box
    JButton enterStore;
    JButton back;
    JComboBox<String> storeOptionsComboBox;

    JButton enterCalendar;
    JComboBox<String> calendarOptionsComboBox;
    JButton enterAppointmentWindow;
    JComboBox<String> appointmentWindowComboBox;

    // Grid layout and layered panes
    GridLayout gridLayout;
    JLayeredPane layeredPaneMiddle;
    JLayeredPane layeredPaneRight;

    public ViewEscapeRoomFrame(Seller seller) {
        this.seller = seller;
    }

    public synchronized void run() {
        // Setting up frame
        viewEscapeRoomFrame = new JFrame("Seller");
        viewEscapeRoomFrame.setSize(900, 650);
        viewEscapeRoomFrame.setLocationRelativeTo(null);
        viewEscapeRoomFrame.setVisible(true);
        viewEscapeRoomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 3);
        content = viewEscapeRoomFrame.getContentPane();
        content.setLayout(gridLayout);

        JLayeredPane layeredPaneLeft = new JLayeredPane();
        layeredPaneLeft.setPreferredSize(new Dimension(300, 600));
        layeredPaneLeft.setBorder(BorderFactory.createTitledBorder("Your escape rooms"));

        // Prints out seller's stores
        sellerStores = seller.getStores();
        String[] storeOptionsArray = new String[sellerStores.size()];
        for (int i = 0; i < sellerStores.size(); i++) {
            String message = (i + 1) + ". " + sellerStores.get(i).toString();
            String[] lines = message.split("\n");
            int j = 0;
            for (String line : lines) {
                JLabel temp = new JLabel(line);
                temp.setBounds(5, (i * 5) * 15 + j + 20, 280, 15);
                layeredPaneLeft.add(temp);
                j += 15;
            }

            storeOptionsArray[i] = i + 1 + "";
        }

        // Put buttons and combo box on seller's store side
        JPanel panel = new JPanel();
        storeOptionsComboBox = new JComboBox<>(storeOptionsArray);
        storeOptionsComboBox.setVisible(true);
        panel.add(storeOptionsComboBox);
        enterStore = new JButton("Enter Store");
        enterStore.addActionListener(actionListener);
        panel.add(enterStore);
        back = new JButton("Back");
        back.addActionListener(actionListener);
        panel.add(back);
        panel.setBounds(5, 550, 300, 40);
        layeredPaneLeft.add(panel);

        content.add(layeredPaneLeft, BorderLayout.WEST);


        // Calendar stuff
        layeredPaneMiddle = new JLayeredPane();
        layeredPaneMiddle.setPreferredSize(new Dimension(300, 600));
        layeredPaneMiddle.setBorder(BorderFactory.createTitledBorder("Calendars for [view calendars]"));

        content.add(layeredPaneMiddle);


        // Appointment window stuff
        layeredPaneRight = new JLayeredPane();
        layeredPaneRight.setPreferredSize(new Dimension(300, 600));
        layeredPaneRight.setBorder(BorderFactory.createTitledBorder("Appointment windows for [view appt windows]"));

        content.add(layeredPaneRight);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterStore) {
                currentStoreIndex = storeOptionsComboBox.getSelectedIndex();
                ArrayList<String> sellerStoreNames = seller.getSellerStoreNames();
                String indexAndStore = (currentStoreIndex + 1) + ". " + sellerStoreNames.get(currentStoreIndex);
                String enterStoreSelection = (String) JOptionPane.showInputDialog(null,
                        indexAndStore + "\nWhat would you like to do?", "Seller", JOptionPane.PLAIN_MESSAGE,
                        null, enterStoreOptions, null);


                if (enterStoreSelection != null) {

                    if (enterStoreSelection.equals(enterStoreOptions[0])) {     // Create a calendar
                        //SwingUtilities.invokeLater(new EditCalendarFrame(seller.getName(), seller.getPassword(),
                        //        seller.getEmail(), seller.getSellerStoreNames().get(currentStoreIndex)));

                        boolean calendarCreated = false;
                        while (!calendarCreated) {
                            String calendarName = JOptionPane.showInputDialog(null,
                                    "Enter a title for the calendar", "Seller", JOptionPane.QUESTION_MESSAGE);
                            String calendarDescription = JOptionPane.showInputDialog(null,
                                    "Enter a description for the calendar", "Seller", JOptionPane.QUESTION_MESSAGE);

                            if (calendarName == null || calendarDescription == null ||
                                    calendarName.equals("") || calendarDescription.equals("")) {
                                int tryAgain = JOptionPane.showConfirmDialog(null,
                                        "A name and/or description was not entered.\nWould you like to try again?",
                                        "Seller", JOptionPane.YES_NO_OPTION);
                                if (tryAgain == 1) {
                                    calendarCreated = true;
                                }
                            } else {
                                // Add the calendar to files
                                synchronized(this) {
                                    Calendar newCalendar = new Calendar(calendarName, calendarDescription,
                                            sellerStoreNames.get(currentStoreIndex), seller.getEmail());
                                    ArrayList<Calendar> tempCalendars = sellerStores.get(currentStoreIndex).getCalendars();
                                    tempCalendars.add(newCalendar);
                                    newCalendar.writeCSVFile();
                                    Store currentStore = sellerStores.get(currentStoreIndex);
                                    currentStore.setCalendars(tempCalendars);
                                    calendarCreated = true;
                                }
                                JOptionPane.showMessageDialog(null,
                                        "Calendar created successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                    }

                    if (enterStoreSelection.equals(enterStoreOptions[1])) {     // View calendars
                        // Printing calendars to the frame
                        layeredPaneMiddle.setBorder(BorderFactory.createTitledBorder("Calendars for " +
                                sellerStoreNames.get(currentStoreIndex)));
                        ArrayList<Store> sellerStoresInfo = seller.getStores();
                        Store selectedStore = sellerStoresInfo.get(currentStoreIndex);
                        ArrayList<Calendar> storeCalendars = selectedStore.getCalendars();

                        layeredPaneMiddle.removeAll();
                        String[] calendarOptionsArray = new String[storeCalendars.size()];
                        for (int i = 0; i < storeCalendars.size(); i++) {
                            String message = (i + 1) + ". " + storeCalendars.get(i).toString();
                            String[] lines = message.split("\n");
                            int j = 0;
                            for (String line : lines) {
                                JLabel temp = new JLabel(line);
                                temp.setBounds(5, (i * 4) * 15 + j + 20, 280, 15);
                                layeredPaneMiddle.add(temp);
                                j += 15;
                            }

                            calendarOptionsArray[i] = i + 1 + "";
                        }

                        // Adding buttons and stuff
                        JPanel panel = new JPanel();
                        calendarOptionsComboBox = new JComboBox<>(calendarOptionsArray);
                        calendarOptionsComboBox.setVisible(true);
                        panel.add(calendarOptionsComboBox);

                        enterCalendar = new JButton("Enter Calendar");
                        enterCalendar.addActionListener(actionListener);
                        panel.add(enterCalendar);

                        panel.setBounds(5, 550, 280, 40);
                        layeredPaneMiddle.add(panel);

                        // Resetting appointment window section
                        layeredPaneRight.removeAll();
                        layeredPaneRight.setBorder(BorderFactory.createTitledBorder("Appointment windows for [view appt windows]"));
                    }

                    if (enterStoreSelection.equals(enterStoreOptions[2])) {     // Delete store
                        Store currentStore = seller.getStores().get(currentStoreIndex);
                        currentStore.delete();

                        JOptionPane.showMessageDialog(null,
                                "Escape room deleted successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }

            if (e.getSource() == back) {
                viewEscapeRoomFrame.dispose();
            }

            if (e.getSource() == enterCalendar) {
                currentCalendarIndex = calendarOptionsComboBox.getSelectedIndex();

                ArrayList<Store> sellerStoresInfo = seller.getStores();
                Store selectedStore = sellerStoresInfo.get(currentStoreIndex);
                ArrayList<Calendar> storeCalendars = selectedStore.getCalendars();
                Calendar currentCalendar = storeCalendars.get(currentCalendarIndex);

                String enterCalendarSelection = (String) JOptionPane.showInputDialog(null,
                        (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                "\nWhat would you like to do?", "Seller", JOptionPane.PLAIN_MESSAGE,
                        null, enterCalendarOptions, null);

                if (enterCalendarSelection != null) {
                    if (enterCalendarSelection.equals(enterCalendarOptions[0])) {   // Create appointment window
                        SwingUtilities.invokeLater(new EditCalendarFrame(seller, seller.getSellerStoreNames().get(currentStoreIndex),
                                currentCalendar.getTitle()));
                    }
                    if (enterCalendarSelection.equals(enterCalendarOptions[1])) {   // View appointment windows

                        layeredPaneRight.removeAll();
                        layeredPaneRight.setBorder(BorderFactory.createTitledBorder("Appointment windows for " + currentCalendar.getTitle()));

                        ArrayList<String> appointmentWindows = seller.getStores().get(currentStoreIndex).
                                getCalendars().get(currentCalendarIndex).getAppointmentWindows();
                        int amountOfWindows = 0;
                        appointmentWindowsString = new ArrayList<>();

                        for (int i = 0; i < appointmentWindows.size(); i++) {
                            String[] split = appointmentWindows.get(i).split(",");
                            // Title,Monday,Ship,7,8,4,alexa@email.com,4,0,
                            //   0     1     2   3 4 5       6         7 8
                            if (split[6].equals(seller.getEmail()) && split[1].equals(currentCalendar.getTitle())) {
                                JLabel appointmentWindowTitle = new JLabel(
                                        (amountOfWindows + 1) + ". Title of appt window: " + split[0]);
                                JLabel calendarTitle = new JLabel("   Calendar title: " + split[1]);
                                JLabel storeTitle = new JLabel("   Escape room name: " + split[2]);
                                JLabel day = new JLabel("   Day: " + split[5]);
                                JLabel startAndEndTime = new JLabel(
                                        "   Start time: " + split[3] + ", End time: " + split[4]);
                                JLabel maxNumAttend = new JLabel("   Max number of attendees: " + split[7]);
                                appointmentWindowTitle.setBounds(5, (amountOfWindows * 7) * 15 + 20, 280, 15);
                                layeredPaneRight.add(appointmentWindowTitle);
                                calendarTitle.setBounds(5, (amountOfWindows * 7) * 15 + 35, 280, 15);
                                layeredPaneRight.add(calendarTitle);
                                storeTitle.setBounds(5, (amountOfWindows * 7) * 15 + 50, 280, 15);
                                layeredPaneRight.add(storeTitle);
                                day.setBounds(5, (amountOfWindows * 7) * 15 + 65, 280, 15);
                                layeredPaneRight.add(day);
                                startAndEndTime.setBounds(5, (amountOfWindows * 7) * 15 + 80, 280, 15);
                                layeredPaneRight.add(startAndEndTime);
                                maxNumAttend.setBounds(5, (amountOfWindows * 7) * 15 + 95, 280, 15);
                                layeredPaneRight.add(maxNumAttend);

                                amountOfWindows++;
                                appointmentWindowsString.add(appointmentWindows.get(i));

                            }
                        }

                        JPanel panel = new JPanel();
                        String[] appointmentWindowsOptionsArray = new String[amountOfWindows];
                        for (int i = 0; i < appointmentWindowsOptionsArray.length; i++) {
                            appointmentWindowsOptionsArray[i] = i + 1 + "";
                        }
                        appointmentWindowComboBox = new JComboBox<>(appointmentWindowsOptionsArray);
                        appointmentWindowComboBox.setVisible(true);
                        panel.add(appointmentWindowComboBox);

                        enterAppointmentWindow = new JButton("Enter Appointment Window");
                        enterAppointmentWindow.addActionListener(actionListener);
                        enterAppointmentWindow.setVisible(true);
                        panel.add(enterAppointmentWindow);

                        panel.setBounds(5, 550, 280, 40);
                        layeredPaneRight.add(panel);

                    }
                    if (enterCalendarSelection.equals(enterCalendarOptions[2])) {   // Delete calendar
                        ArrayList<Calendar> currentCalendars = sellerStores.get(currentStoreIndex).getCalendars();
                        currentCalendars.get(currentCalendarIndex).delete();

                        JOptionPane.showMessageDialog(null,
                                "Calendar deleted successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }

            if (e.getSource() == enterAppointmentWindow) {
                currentAppointmentWindowIndex = appointmentWindowComboBox.getSelectedIndex();
                String currentAppointmentWindow = appointmentWindowsString.get(currentAppointmentWindowIndex);
                String enterAppointmentWindowSelection = (String) JOptionPane.showInputDialog(null,
                        "You are editing: " + (currentAppointmentWindowIndex + 1) + ". " + currentAppointmentWindow,
                        "Seller", JOptionPane.PLAIN_MESSAGE, null, enterAppointmentWindowOptions, null);
                if (enterAppointmentWindowSelection != null) {
                    // Title,Monday,Ship,7,8,4,alexa@email.com,4,0,
                    //   0     1     2   3 4 5       6         7 8
                    String[] split = currentAppointmentWindow.split(",");
                    String windowTitle = split[0];
                    String calendarTitle = split[1];
                    String storeTitle = split[2];
                    int startTime = Integer.parseInt(split[3]);
                    int endTime = Integer.parseInt(split[4]);
                    int day = Integer.parseInt(split[5]);
                    int maxAttendees = Integer.parseInt(split[7]);

                    AppointmentWindow appointmentWindow = new AppointmentWindow(windowTitle, storeTitle, startTime,
                            endTime, day, seller.getEmail(), maxAttendees, 0, calendarTitle, null);     // TODO
                    // public EditCalendarFrame(String name, String password, String email, String enteredStore, String enteredCalendar, AppointmentWindow enteredAppointmentWindow) {
                    if (enterAppointmentWindowSelection.equals(enterAppointmentWindowOptions[0])) {
                        // Edit appointment window
                        // TODO
                        SwingUtilities.invokeLater(new EditCalendarFrame(seller, storeTitle, calendarTitle, appointmentWindow));
                    }
                    if (enterAppointmentWindowSelection.equals(enterAppointmentWindowOptions[1])) {
                        // Delete
                        appointmentWindow.delete();
                        JOptionPane.showMessageDialog(null,
                                "Appointment window deleted successfully", "Seller", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        }
    };
}
