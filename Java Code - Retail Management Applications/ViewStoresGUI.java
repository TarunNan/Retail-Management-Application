import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ViewStoresGUI extends JComponent implements Runnable {

    // Private variables
    private Customer customer;
    private String[] enterCalendarOptions = {"Make an appointment", "Go back"};
    private int currentStoreIndex;
    private int currentCalendarIndex;
    private Appointment appointment;

    JFrame viewEscapeRoomFrame;
    Container content;


    JButton enterStore;
    JButton back;
    JComboBox<String> storeOptionsComboBox;

    JButton enterCalendar;
    JComboBox<String> calendarOptionsComboBox;


    GridLayout gridLayout;
    JLayeredPane layeredPaneRight;
    Socket socket;
    BufferedReader bfr;
    PrintWriter pw;

    public ViewStoresGUI(Customer customer, Socket socket) throws IOException {
        this.customer = customer;
        this.socket = socket;
        this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(socket.getOutputStream());
    }

    public synchronized void run() {
        viewEscapeRoomFrame = new JFrame("Customer");
        viewEscapeRoomFrame.setSize(900, 650);
        viewEscapeRoomFrame.setLocationRelativeTo(null);
        viewEscapeRoomFrame.setVisible(true);
        viewEscapeRoomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridLayout = new GridLayout(0, 2);
        content = viewEscapeRoomFrame.getContentPane();
        content.setLayout(gridLayout);

        JLayeredPane layeredPaneLeft = new JLayeredPane();
        layeredPaneLeft.setPreferredSize(new Dimension(450, 600));
        layeredPaneLeft.setBorder(BorderFactory.createTitledBorder("List of all stores:"));

        //Prints stores
        ArrayList<Store> allStores = customer.getStores();
        String[] storeOptionsArray = new String[allStores.size()];
        for (int i = 0; i < allStores.size(); i++) {
            String message = (i + 1) + ". " + allStores.get(i).toString();
            String[] lines = message.split("\n");
            int j = 0;
            for (String line : lines) {
                JLabel temp = new JLabel(line);
                temp.setBounds(5, (i * 5) * 15 + j + 20, 400, 15);
                layeredPaneLeft.add(temp);
                j += 15;
            }

            storeOptionsArray[i] = i + 1 + "";
        }

        // Put buttons and combo box store list side
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
        panel.setBounds(5, 550, 400, 40);
        layeredPaneLeft.add(panel);

        content.add(layeredPaneLeft, BorderLayout.WEST);

        // Calendar stuff
        layeredPaneRight = new JLayeredPane();
        //layeredPaneRight.setLocation(450, 0);
        layeredPaneRight.setPreferredSize(new Dimension(450, 600));
        layeredPaneRight.setBorder(BorderFactory.createTitledBorder("Calendars for [view calendars for an escape room]"));

        content.add(layeredPaneRight);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterStore) {
                currentStoreIndex = storeOptionsComboBox.getSelectedIndex();
                ArrayList<String> sellerStores = customer.getCustomerStoreNames();
                String indexAndStore = (currentStoreIndex + 1) + ". " + sellerStores.get(currentStoreIndex);
                /*
                String enterStoreSelection = (String) JOptionPane.showInputDialog(null,
                        indexAndStore + "\nWhat would you like to do?", "Customer", JOptionPane.PLAIN_MESSAGE,
                        null, enterStoreOptions, null);

                if (enterStoreSelection != null) {
                    if (enterStoreSelection.equals(enterStoreOptions[0])) {     // Create a calendar
                        SwingUtilities.invokeLater(new CreateAppointmentGUI(customer.getName(), customer.getPassword(),
                                customer.getEmail(), customer.getCustomerStoreNames().get(currentStoreIndex)));

                        boolean calendarCreated = false;
                    }
*/
                layeredPaneRight.setBorder(BorderFactory.createTitledBorder("Calendars for " +
                        sellerStores.get(currentStoreIndex)));
                ArrayList<Store> sellerStoresInfo = customer.getStores();
                Store selectedStore = sellerStoresInfo.get(currentStoreIndex);
                ArrayList<Calendar> storeCalendars = selectedStore.getCalendars();

                String[] calendarOptionsArray = new String[storeCalendars.size()];
                for (int i = 0; i < storeCalendars.size(); i++) {
                    String message = (i + 1) + ". " + storeCalendars.get(i).toString();
                    String[] lines = message.split("\n");
                    int j = 0;
                    for (String line : lines) {
                        JLabel temp = new JLabel(line);
                        temp.setBounds(5, (i * 4) * 15 + j + 20, 400, 15);
                        layeredPaneRight.add(temp);
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

                panel.setBounds(5, 550, 400, 40);
                layeredPaneRight.add(panel);
            }
            if (e.getSource() == back) {
                viewEscapeRoomFrame.dispose();
            }

            if (e.getSource() == enterCalendar) {
                currentCalendarIndex = calendarOptionsComboBox.getSelectedIndex();
                ArrayList<Store> customerStoreInfo = customer.getStores();
                Store selectedStore = customerStoreInfo.get(currentStoreIndex);
                ArrayList<Calendar> storeCalendars = selectedStore.getCalendars();
                Calendar currentCalendar = storeCalendars.get(currentCalendarIndex);
                String enterCalendarSelection = (String) JOptionPane.showInputDialog(null,
                        (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                "\nWhat would you like to do?", "Customer", JOptionPane.PLAIN_MESSAGE,
                        null, enterCalendarOptions, null);

                if (enterCalendarSelection != null) {

                    if (enterCalendarSelection.equals(enterCalendarOptions[0])) {
                        ArrayList<AppointmentWindow> aWin = new ArrayList<>();
                        currentCalendar = storeCalendars.get(currentCalendarIndex);

                        int day = Integer.parseInt(JOptionPane.showInputDialog(null,
                                (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                        "\nWhat Day do you Want an Appointment On? Days 1-5", "Customer", JOptionPane.QUESTION_MESSAGE));
                        // System.out.println(day);
                        currentCalendar.getAppointmentWindow().size();
                        for(int i = 0; i < currentCalendar.getAppointmentWindow().size(); i++){
                            if((currentCalendar.getAppointmentWindow().get(i).getApprovedAppointments()).isEmpty() &&
                                    currentCalendar.getAppointmentWindow().get(i).getDate() == day) {
                                aWin.add(currentCalendar.getAppointmentWindow().get(i));
                            }
                        }
                        if (aWin.size() != 0) {
                            String[] aWinOptions = new String[aWin.size()];
                            for(int i = 0; i < aWinOptions.length; i++){
                                String temp = (i + 1) + ". Start: " + aWin.get(i).getStart() + " End: " + aWin.get(i).getEnd();
                                aWinOptions[i] = temp;
                            }
                            String apptWinSelect = (String) JOptionPane.showInputDialog(null,
                                    (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                            "\nWhich Appointment Window would you like to make an Appointment in?", "Customer", JOptionPane.PLAIN_MESSAGE,
                                    null, aWinOptions, null);
                            int awindex = 0;
                            if (apptWinSelect != null) {
                                for(int i = 0; i < aWinOptions.length; i++){
                                    if(apptWinSelect.equals(aWinOptions[i])){
                                        awindex = i;
                                        break;
                                    }
                                }
                                AppointmentWindow aWinS = aWin.get(awindex);
                                int[] numAttendees = new int[aWinS.getMaxNumAttend()];
                                for(int i = 0; i < aWinS.getMaxNumAttend(); i++){
                                    numAttendees[i] = i+1;
                                }
                                int numPeople = Integer.parseInt(JOptionPane.showInputDialog(null,
                                        (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                                "\nHow Many People? Max Attendees" + aWinS.getMaxNumAttend(), "Customer", JOptionPane.QUESTION_MESSAGE));
                                String titleVal = (String) JOptionPane.showInputDialog(null,
                                        (currentCalendarIndex + 1) + ". " + currentCalendar.getTitle() +
                                                "\nWhat is the name of the Appointment?", "Customer", JOptionPane.QUESTION_MESSAGE);
                                Appointment app = new Appointment(titleVal, selectedStore.getTitle(), aWinS.getStart(), aWinS.getEnd(), aWinS.getDate(), numPeople, "requested", customer.getEmail(), currentCalendar.getTitle(), aWinS.getTitle());
                                pw.println("requestAppointment");
                                pw.println(String.format("%s,%s,%s,%d,%d,%d,%s,%d,%s,%s,%s", app.getTitle(), app.getApptWindowTitle(),
                                        app.getCalendarTitle(), app.getStartTime(), app.getEndTime(), app.getDate(), app.getStoreTitle(), app.getPartySize(),
                                        app.getStatus(), app.getRequestedBy(), app.getLastModified()));
                                pw.flush();
                                ArrayList<Appointment> pAppointments = customer.getPendApts();
                                pAppointments.add(app);
                                customer.setPendApts(pAppointments);
                                ArrayList<Appointment> cAppointments = currentCalendar.getAppointments();
                                cAppointments.add(app);
                                currentCalendar.setAppointments(cAppointments);
                                JOptionPane.showMessageDialog(null, "Appointment successfully created",
                                         "Customer", JOptionPane.PLAIN_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "No appointment windows for this day",
                                    "Customer", JOptionPane.PLAIN_MESSAGE);
                        }

                    }

                }
                if (enterCalendarSelection.equals(enterCalendarOptions[1])) {
                    viewEscapeRoomFrame.dispose();
                }
            }
        };
    };
}