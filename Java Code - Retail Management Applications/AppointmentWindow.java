import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * A java class used to create AppointmentWindow objects that are modified with
 * user input in the main method.
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Raelee Lance & Tarun Nandamudi
 * @version April 9, 2023
 */
public class AppointmentWindow {
    // Fields
    private String title;
    private String store;
    private int start;
    private int end;
    private int date;
    private String seller;
    private int maxNumAttend;
    private int numBooking;
    private ArrayList<Appointment> approvedAppointments;
    private Timestamp timestamp;
    private String calendarTitle;

    // Constructor
    public AppointmentWindow(String title, String store, int start, int end, int date, String seller, int mapNumAttend,
                             int numBooking, String calendarTitle, ArrayList<Appointment> approvedAppointments) {
        this.title = title;
        this.store = store;
        this.start = start;
        this.end = end;
        this.date = date;
        this.seller = seller;
        this.maxNumAttend = mapNumAttend;
        this.numBooking = numBooking;
        this.calendarTitle = calendarTitle;
        this.approvedAppointments = approvedAppointments;

        System.out.println("Finished appt window");

    }

    public String getTitle() {
        return title;
    }

    public String getStore() {
        return store;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getDate() {
        return date;
    }

    public String getSeller() {
        return seller;
    }

    public int getMaxNumAttend() {
        return maxNumAttend;
    }

    public int getNumBooking() {
        return numBooking;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
        updateCSVFile();
    }

    public void setStore(String store) {
        this.store = store;
        updateCSVFile();
    }

    public void setStart(int start) {
        this.start = start;
        updateCSVFile();
    }

    public void setEnd(int end) {
        this.end = end;
        updateCSVFile();
    }

    public void setDate(int date) {
        this.date = date;
        updateCSVFile();
    }

    public void setSeller(String seller) {
        this.seller = seller;
        updateCSVFile();
    }

    public void setMaxNumAttend(int maxNumAttend) {
        this.maxNumAttend = maxNumAttend;
        updateCSVFile();
    }

    public void setNumBooking(int numBooking) {
        this.numBooking = numBooking;
        updateCSVFile();
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        updateCSVFile();
    }

    public void setApprovedAppointments(ArrayList<Appointment> approvedAppointments) {
        this.approvedAppointments = approvedAppointments;
        updateCSVFile();
    }

    public ArrayList<Appointment> getApprovedAppointments() {
        return this.approvedAppointments;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
        updateCSVFile();
    }

    public String toString() {
        if (approvedAppointments == null) {
            return String.format("Title: %s\n" +
                            "Maximum number of attendees: %d\n" +
                            "Current number of approved bookings: %d\n" +
                            "Start time: %d\n" +
                            "End time: %d\n" +
                            "%s",
                    this.title, maxNumAttend, 0, start, end, timestamp.getTime());
        }
        return String.format("Title: %s\n" +
                        "Maximum number of attendees: %d\n" +
                        "Current number of approved bookings: %d\n" +
                        "Start time: %d\n" +
                        "End time: %d\n" +
                        "%s",
                this.title, maxNumAttend, approvedAppointments.size(), start, end, timestamp.getTime());
    }

    public void updateCSVFile() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("AppointmentWindow.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            ArrayList<String> newFile = new ArrayList<>();
            for (String s : lines) {
                if ((s.split(",")[0].equals(this.title)) && (s.split(",")[2].equals(this.store))
                        && (s.split(",")[1].equals(this.calendarTitle))) {
                    String newVal = String.format("%s,%s,%s,%d,%d,%d,%s,%d,%d", this.title, this.calendarTitle,
                            this.store, this.start, this.end, this.date, this.seller, this.maxNumAttend,
                            this.numBooking);
                    if (approvedAppointments != null) {
                        for (int i = 0; i < approvedAppointments.size(); i++) {
                            newVal += approvedAppointments.get(i).getTitle();
                            if (i != approvedAppointments.size() - 1) {
                                newVal += ";";
                            }
                        }
                    }
                    newFile.add(newVal);
                } else {
                    newFile.add(s);
                }
            }
            PrintWriter pw = new PrintWriter(new FileWriter("AppointmentWindow.csv", false));
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
            BufferedReader bfr = new BufferedReader(new FileReader("AppointmentWindow.csv"));
            ArrayList<String> fileStuff = new ArrayList<>();
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            FileWriter outputfile = new FileWriter("AppointmentWindow.csv");
            BufferedWriter writer = new BufferedWriter(outputfile);
            for (int i = 0; i < fileStuff.size(); i++) {
                writer.write(fileStuff.get(i));
                writer.write("\n");
            }
            String s = String.format("%s,%s,%s,%d,%d,%d,%s,%d,%d,", this.title, this.calendarTitle, this.store,
                    this.start, this.end, this.date, this.seller, this.maxNumAttend, this.numBooking);
            writer.write(s);
            if (approvedAppointments != null) {
                for (int i = 0; i < approvedAppointments.size(); i++) {
                    writer.write(approvedAppointments.get(i).getTitle());
                    if (i != approvedAppointments.size() - 1) {
                        writer.write(";");
                    }
                }
            }
            writer.write("\n");
            bfr.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() { // Deletes the available window
        ArrayList<String> fileStuff = new ArrayList<>();
        try { // First goes through the buyer file in case the user is a buyer
            BufferedReader bfr = new BufferedReader(new FileReader("AppointmentWindow.csv"));
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            ArrayList<String> arr = new ArrayList<>();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("AppointmentWindow.csv", false));
            for (int i = 0; i < fileStuff.size(); i++) {
                String[] temp = fileStuff.get(i).split(",");
                // Title,Monday,Ship,7,8,4,alexa@email.com,4,0,
                //   0     1     2   3 4 5       6         7 8
                if (temp[0].equals(this.title) && temp[6].equals(this.seller) && temp[2].equals(this.store)) {
                    String[] apptNames = temp[8].split(";");
                    for (int j = 0; j < apptNames.length; j++) {
                        arr.add(apptNames[j]);
                    }
                } else {
                    bfw.write(fileStuff.get((i)));
                    bfw.write("\n");
                }
            }
            BufferedReader bfar = new BufferedReader(new FileReader("Appointment.csv"));
            ArrayList<String> appt = new ArrayList<>();
            line = bfar.readLine();
            while (line != null) {
                appt.add(line);
                line = bfar.readLine();
            }
            bfar.close();
            BufferedWriter bfaw = new BufferedWriter(new FileWriter("Appointment.csv", false));
            for (int i = 0; i < appt.size(); i++) {
                String[] temp = appt.get(i).split(",");
                for (int j = 0; j < arr.size(); j++) {
                    if (!temp[0].equals(arr.get(j)) && !temp[1].equals(this.calendarTitle)) {
                        bfaw.write(appt.get((i)));
                    }
                }
            }
            bfw.close();
            bfaw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }
}
