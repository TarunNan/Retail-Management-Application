import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * A java class used to create Appointment objects that are modified with user
 * input in the main method.
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Raelee Lance
 * @version April 9, 2023
 */
public class Appointment {
    // Fields
    private String title;
    private String storeTitle;
    private int startTime;
    private int endTime;
    private int date;
    private int partySize;
    private String status;
    private String requestedBy;
    private String lastModified;
    private String calendarTitle;
    private String apptWindowTitle;

    // Constructor
    public Appointment(String title, String storeTitle, int startTime, int endTime, int date, int partySize,
                       String status, String requestedBy, String calendarTitle, String lastModified, String apptWindowTitle) {
        this.title = title;
        this.storeTitle = storeTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.partySize = partySize;
        this.status = status;
        this.calendarTitle = calendarTitle;
        this.requestedBy = requestedBy;
        this.apptWindowTitle = apptWindowTitle;
        // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
        // this.lastModified = formatter.format(new Date());
        this.lastModified = lastModified;

        System.out.println("Finished appointment");

    }

    public Appointment(String title, String storeTitle, int startTime, int endTime, int date, int partySize,
                       String status, String requestedBy, String calendarTitle, String apptWindowTitle) {
        this.title = title;
        this.storeTitle = storeTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.partySize = partySize;
        this.status = status;
        this.calendarTitle = calendarTitle;
        this.requestedBy = requestedBy;
        this.apptWindowTitle = apptWindowTitle;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
        this.lastModified = formatter.format(new Date());

        System.out.println("Finished appointment");

    }

    // Methods
    public String getTitle() {
        return this.title;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public int getDate() {
        return this.date;
    }

    public int getPartySize() {
        return this.partySize;
    }

    public String getStatus() {
        return this.status;
    }

    public String getRequestedBy() {
        return this.requestedBy;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public String getStoreTitle() {
        return this.storeTitle;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setTitle(String title) {
        this.title = title;
        updateCSVFile();
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        updateCSVFile();
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        updateCSVFile();
    }

    public void setDate(int date) {
        this.date = date;
        updateCSVFile();
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
        updateCSVFile();
    }

    public void setStatus(String status) {
        this.status = status;
        updateCSVFile();
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        updateCSVFile();
    }

    public void setLastModified() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
        this.lastModified = formatter.format(new Date());
        updateCSVFile();
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
        updateCSVFile();
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
        updateCSVFile();
    }

    public String getApptWindowTitle() {
        return apptWindowTitle;
    }

    public void setApptWindowTitle(String apptWindowTitle) {
        this.apptWindowTitle = apptWindowTitle;
        updateCSVFile();
    }

    @Override
    public String toString() {
        return String.format(
                "A new appointment named %s has been %s for %s (%s) on %s from %d to %d. Last modified: %s",
                this.getTitle(),
                this.getStatus(), this.getStoreTitle(), this.getRequestedBy(), this.getDate(),
                this.getStartTime(), this.getEndTime(), this.getLastModified());
    }

    public void writeCSVFile() {
        // FIX
        try {
            ArrayList<String> fileStuff = new ArrayList<>();
            try { // First goes through the buyer file in case the user is a buyer
                BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                String line = bfr.readLine();
                while (line != null) {
                    fileStuff.add(line);
                    line = bfr.readLine();
                }
                bfr.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("An error occurred");
            }
            FileWriter outputfile = new FileWriter("Appointment.csv");
            BufferedWriter writer = new BufferedWriter(outputfile);

            for (int i = 0; i < fileStuff.size(); i++) {
                writer.write(fileStuff.get(i) + "\n");
            }
            writer.write(String.format("%s,%s,%s,%d,%d,%d,%s,%d,%s,%s,%s", this.title, this.apptWindowTitle,
                    this.calendarTitle, this.startTime, this.endTime, this.date, getStoreTitle(), this.partySize,
                    this.status, this.requestedBy, this.lastModified));
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCSVFile(){
        try{
            BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            ArrayList<String> newFile = new ArrayList<>();
            for (String s : lines) {
                if ((s.split(",")[0].equals(this.title)) && (s.split(",")[1].equals(this.apptWindowTitle)) && (Integer.parseInt(s.split(",")[5]) == this.date)) {
                    String newVal = String.format("%s,%s,%s,%d,%d,%d,%s,%d,%s,%s,%s", this.title, this.apptWindowTitle, this.calendarTitle, this.startTime, this.endTime, this.date, getStoreTitle(), this.partySize, this.status, this.requestedBy, this.lastModified);
                    newFile.add(newVal);
                }else{
                    newFile.add(s);
                }
            }
            PrintWriter pw = new PrintWriter(new FileWriter("Appointment.csv", false));
            for(int i = 0; i < newFile.size(); i++){
                pw.println(newFile.get(i));
            }
            pw.close();
            bfr.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void delete() {
        // basic if-else logic that can be changed based on other classes
        ArrayList<String> fileStuff = new ArrayList<>();
        try { // First goes through the buyer file in case the user is a buyer
            BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            BufferedWriter bfw = new BufferedWriter(new FileWriter("Appointment.csv", false));
            for (int i = 0; i < fileStuff.size(); i++) {
                if (!title.equals(fileStuff.get(i).substring(0, fileStuff.get(i).indexOf(",")))) {
                    bfw.write(fileStuff.get(i));
                }
            }
            bfw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("An error occurred");
        }

        setTitle(null);
        setDate(0);
        setStartTime(0);
        setEndTime(0);
        setCalendarTitle(null);
        setPartySize(0);
        setStatus(null);
        setRequestedBy(null);
        this.lastModified = null;
    }
}
