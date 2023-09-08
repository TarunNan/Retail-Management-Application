import java.io.*;
import java.util.ArrayList;

/**
 * A java class that will be used to create different store objects for each of
 * the stores/escape rooms
 * that the seller has. Will also write the data to a CSV file for reading and
 * use later on.
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 * 
 *
 * @author Tarun Nandamudi
 * @version April 8, 2023
 */
public class Store {

    private String title;
    private ArrayList<Calendar> calendars;
    private String email;
    private boolean opened;

    public Store(String email, String title) {
        this.title = title;
        this.email = email;
        this.opened = true;
        ArrayList<Calendar> calRead = new ArrayList<>();
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
                if (data[1].equals(title) && data[2].equals(email)) {
                    calRead.add(new Calendar(data[0], data[3], data[1], data[2]));
                }
                line = reader.readLine();
            }
            reader.close();



        } catch (FileNotFoundException e) {
            System.out.println("Calendar.csv file not found");
        } catch (IOException e) {
            System.out.println("An error occurred when trying to create the Calendars for this Store");
        }
        this.calendars = calRead;

        System.out.println("Finished store");

    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setCalendars(ArrayList<Calendar> calendars) {
        this.calendars = calendars;
        updateCSVFile();
    }

    public void setEmail(String email) {
        this.email = email;
        updateCSVFile();
    }

    public void setTitle(String title) {
        this.title = title;
        updateCSVFile();
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
        updateCSVFile();
    }

    public void updateCSVFile() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }
            ArrayList<String> newFile = new ArrayList<>();
            for (String s : lines) {
                if ((s.split(",")[0].equals(this.title)) && (s.split(",")[1].equals(this.email))) {
                    String newVal = String.format("%s,%s,%b,", this.title, this.email, isOpened());
                    for (int i = 0; i < calendars.size(); i++) {
                        newVal += calendars.get(i).getTitle();
                        if (i != calendars.size() - 1) {
                            newVal += "; ";
                        }
                    }
                    newFile.add(newVal);
                } else {
                    newFile.add(s);
                }
            }
            PrintWriter pw = new PrintWriter(new FileWriter("Store.csv", false));
            for (int i = 0; i < newFile.size(); i++) {
                pw.println(newFile.get(i));
            }
            pw.close();
            bfr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public String toString() {
        String answer = String.format(
                "Store name: %s\n   Seller Email: %s\n   Is the store open for business? %b\n   Calendar Names: ", this.title,
                this.email, this.opened);
        for (int i = 0; i < this.calendars.size(); i++) {
            answer += calendars.get(i).getTitle();
            if (i != this.calendars.size() - 1) {
                answer += ";";
            }
        }
        return answer;
    }

    public void delete() {
        // basic if-else logic that can be changed based on other classes
        ArrayList<String> fileStuff = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            BufferedWriter bfw = new BufferedWriter(new FileWriter("Store.csv", false));
            for (int i = 0; i < fileStuff.size(); i++) {
                String temp = fileStuff.get(i);
                String[] data = temp.split(",");
                if ((!this.title.equals(data[0])) || (!this.email.equals(data[1]))) {
                    bfw.write(fileStuff.get(i) + "\n");
                }
            }
            bfw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("An error occurred");
        }

        setTitle(null);
        setCalendars(null);
        setEmail(null);
        setOpened(false);
    }

    public String storeStats() {
        String answer = "";
        answer += "Customer and their Approved Appointments:\n";
        ArrayList<ArrayList<String>> custAppt = new ArrayList<>();
        for (int i = 0; i < calendars.size(); i++) {
            for (int k = 0; k < calendars.get(i).getAppointments().size(); k++) {
                    Appointment a = calendars.get(i).getAppointments().get(k);
                    if (a.getStatus().equals("approved")) {
                        boolean custFound = false;
                        for (int l = 0; l < custAppt.size(); l++) {
                            if ((a.getRequestedBy()).equals(custAppt.get(l).get(i))) {
                                custFound = true;
                                custAppt.get(l).add(a.toString());
                            }
                            if (!custFound) {
                                custAppt.get(l).add(a.getRequestedBy());
                                custAppt.get(l).add(a.toString());
                            }
                        }
                    }
                }
        }
        for (int i = 0; i < custAppt.size(); i++) {
            for (int j = 0; j < custAppt.get(i).size(); j++) {
                if (j == 0) {
                    answer += custAppt.get(i).get(j);
                } else {
                    answer += ", " + custAppt.get(i).get(j);
                }
            }
            answer += "\n";
        }
        answer += "Top Appointment Window for Each Calendar\n";
        for (int f = 0; f < calendars.size(); f++) {
            AppointmentWindow mostWind = null;
            answer += calendars.get(f).getTitle() + ": ";
            int most = 0;
            for (int i = 0; i < calendars.get(f).getAppointmentWindow().size(); i++) {
                    AppointmentWindow a = calendars.get(f).getAppointmentWindow().get(i);
                    if (a.getApprovedAppointments().size() > most) {
                        most = a.getApprovedAppointments().size();
                        mostWind = a;
                    }
                }
            if (mostWind != null) {
                answer += mostWind.getTitle() + "\n";
            } else {
                answer += "no appointment windows";
            }

        }
        return answer;
    }

    public ArrayList<Appointment> searchCustomer(Customer customer) {
        ArrayList<Appointment> custAppt = new ArrayList<Appointment>();
        for (int i = 0; i < calendars.size(); i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < calendars.get(i).getDays()[j].getAppointments().size(); k++) {
                    Appointment a = calendars.get(i).getDays()[j].getAppointments().get(k);
                    if ((a.getRequestedBy()).equals(customer.getName())) {
                        custAppt.add(a);
                    }
                }
            }
        }
        return custAppt;
    }

    public void writeCSVFile() {
        ArrayList<String> fileStuff = new ArrayList<>();
        try { // First goes through the buyer file in case the user is a buyer
            BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            FileWriter outputfile = new FileWriter("Store.csv");
            BufferedWriter writer = new BufferedWriter(outputfile);
            for (int i = 0; i < fileStuff.size(); i++) {
                writer.write(fileStuff.get(i));
                writer.write("\n");
            }
            String newVal = String.format("%s,%s,%b,", title, email, isOpened());
            for (int i = 0; i < calendars.size(); i++) {
                newVal += calendars.get(i).getTitle();
                if (i != calendars.size() - 1) {
                    newVal += "; ";
                }
            }
            writer.write(newVal);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Store.csv file not found");
        } catch (IOException e) {
            System.out.println("An error has occurred when trying to write to the Store.csv file");
        }
    }
}
