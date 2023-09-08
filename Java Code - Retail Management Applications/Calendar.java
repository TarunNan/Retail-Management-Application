import java.io.*;
import java.util.ArrayList;

/**
 *
 * A java class used to create Calendar objects that are modified with user
 * input in the main method. **DRAFT**
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Group 04
 * @version April 8, 2023
 */
public class Calendar {
    // Fields
    private Day[] days;
    private String title;
    private String description;
    private String store;
    private String seller;
    private ArrayList<AppointmentWindow> appointmentWindows;
    private ArrayList<Appointment> appointments;

    // Constructor
    public Calendar(String title, String description, String store, String sellerEmail) {

        this.title = title;
        this.description = description;
        this.store = store;
        this.seller = sellerEmail;

        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        ArrayList<AppointmentWindow> appointmentWindows = new ArrayList<AppointmentWindow>();
        try {
            BufferedReader bfrApp = new BufferedReader(new FileReader("Appointment.csv"));
            BufferedReader bfrAppWin = new BufferedReader(new FileReader("AppointmentWindow.csv"));
            String line = bfrApp.readLine();
            while (line != null) {
                String[] appointment = line.split(",");
                if (appointment[2].equals(this.title) && appointment[6].equals(this.store)) {
                    Appointment app = new Appointment(appointment[0], appointment[6], Integer.parseInt(appointment[3]),
                            Integer.parseInt(appointment[4]), Integer.parseInt(appointment[5]),
                            Integer.parseInt(appointment[7]),
                            appointment[8], appointment[9], this.title, appointment[1]);
                    appointments.add(app);
                }
                line = bfrApp.readLine();
            }
            String line2 = bfrAppWin.readLine();
            while (line2 != null) {
                System.out.println(line2);
                String[] appointmentWindow = line2.split(",");
                ArrayList<Appointment> approvedAppointments = new ArrayList<>();
                if (appointmentWindow[1].equals(this.title)) {
                    for (int i = 0; i < appointments.size(); i++) {
                        if (appointments.get(i).getApptWindowTitle().equals(appointmentWindow[0]) &&
                                appointments.get(i).getStatus().equals("approved")) {
                            approvedAppointments.add(appointments.get(i));
                            System.out.println("pls work3");
                        }
                    }
                    System.out.println(appointmentWindow[0] + appointmentWindow[2] +
                            Integer.parseInt(appointmentWindow[3]) + "" + Integer.parseInt(appointmentWindow[4]) + "" +
                            Integer.parseInt(appointmentWindow[5]) + appointmentWindow[6] +
                            Integer.parseInt(appointmentWindow[7]) + "" +
                            Integer.parseInt(appointmentWindow[8]) + appointmentWindow[1] + approvedAppointments);
                    AppointmentWindow appWin = new AppointmentWindow(appointmentWindow[0], appointmentWindow[2],
                            Integer.parseInt(appointmentWindow[3]), Integer.parseInt(appointmentWindow[4]),
                            Integer.parseInt(appointmentWindow[5]), appointmentWindow[6],
                            Integer.parseInt(appointmentWindow[7]),
                            Integer.parseInt(appointmentWindow[8]), appointmentWindow[1], approvedAppointments);
                    appointmentWindows.add(appWin);
                    System.out.println("hello");
                    // System.out.println(appWin.toString());
                }
                line2 = bfrAppWin.readLine();
            }
            bfrApp.close();
            bfrAppWin.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        this.appointmentWindows = appointmentWindows;
        this.appointments = appointments;
        /*
        this.days = new Day[5];
        for (int i = 1; i <= 5; i++) {
            ArrayList<Appointment> dayAppointments = new ArrayList<Appointment>();
            for (Appointment a : appointments) {
                if (a.getDate() == i) {
                    dayAppointments.add(a);
                }
            }
            ArrayList<AppointmentWindow> dayAppointmentWindows = new ArrayList<AppointmentWindow>();
            for (AppointmentWindow a : appointmentWindows) {
                if (a.getDate() == i) {
                    dayAppointmentWindows.add(a);
                }
            }
            this.days[i - 1] = new Day(i - 1, dayAppointments, dayAppointmentWindows);
        }

         */
        System.out.println("Finished calendar");
    }

    public Calendar(String title, String description, String store, String sellerEmail,
                    ArrayList<AppointmentWindow> appointmentWindows) {
        this.title = title;
        this.description = description;
        this.store = store;
        this.seller = sellerEmail;
        this.days = new Day[5];
        for (int i = 1; i <= 5; i++) {
            ArrayList<AppointmentWindow> dayAppointmentWindows = new ArrayList<AppointmentWindow>();
            for (AppointmentWindow a : appointmentWindows) {
                if (a.getDate() == i) {
                    dayAppointmentWindows.add(a);
                }
            }
            ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
            this.days[i - 1] = new Day(i - 1, appointmentArrayList, dayAppointmentWindows);
        }
    }

    // Methods
    public Day[] getDays() {
        return this.days;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStore() {
        return this.store;
    }

    public ArrayList<String> getAppointmentWindows() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("AppointmentWindow.csv"));
            ArrayList<String> lines = new ArrayList<>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            return lines;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<AppointmentWindow> getAppointmentWindow() {
        return appointmentWindows;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setAppointmentWindows(ArrayList<AppointmentWindow> appointmentWindows) {
        this.appointmentWindows = appointmentWindows;
    }

    public void setDays(Day[] days) {
        this.days = days;
        updateCSVFile();
    }

    public void setTitle(String title) {
        this.title = title;
        updateCSVFile();
    }

    public void setDescription(String description) {
        this.description = description;
        updateCSVFile();
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void delete() {
        // basic if-else logic that can be changed based on other classes
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            for (String s : lines) {
                if ((s.split(",")[0].equals(this.title)) && (s.split(",")[1].equals(this.store))) {
                    lines.remove(s);
                    break;
                }
            }
            PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv", false));
            for (String s : lines) {
                pw.println(s);
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCSVFile() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            ArrayList<String> newFile = new ArrayList<>();
            for (String s : lines) {
                if ((s.split(",")[0].equals(this.title)) && (s.split(",")[1].equals(this.store))) {
                    String newVal = String.format("%s,%s,%s,%s", this.title, this.store, this.description, this.seller);
                    newFile.add(newVal);
                } else {
                    newFile.add(s);
                }
            }
            PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv", false));
            for (int i = 0; i < newFile.size(); i++) {
                pw.println(newFile.get(i));
            }
            pw.close();
            bfr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void writeCSVFile() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            //ArrayList<String> newFile = new ArrayList<>();
            PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv", false));
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }
            String newVal = String.format("%s,%s,%s,%s", this.title, this.store, this.seller, this.description);
            pw.println(newVal);
            pw.close();
            bfr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public String toString() {
        // modify accordingly
        String format = "Title: %s%n   Description: %s%n   Store: %s%n";
        return String.format(format, this.title, this.description, this.store);
    }
}
