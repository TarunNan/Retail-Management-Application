import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A Customer class that inherits User. Handles appointment requests and
 * appointments from the customer side.
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Alexa Lau
 * @version April 10, 2023
 */
public class Customer extends User {
    private ArrayList<Appointment> appApts = new ArrayList<>();
    private ArrayList<Appointment> pendApts = new ArrayList<>();
    private ArrayList<Store> stores = new ArrayList<>();

    public Customer(String name, String password, String email) {
        super(name, password, email, "customer");
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
            String line = bfr.readLine();
            ArrayList<String> storeInfo = new ArrayList<String>();
            // read in all the lines of the file
            while (line != null) {
                storeInfo.add(line);
                line = bfr.readLine();
            }
            // loop through the lines and create a store object for each line, starting at 1
            // because csv file has a header
            for (int i = 0; i < storeInfo.size(); i++) {
                String[] store = storeInfo.get(i).split(",");
                // create a new store object
                Store s = new Store(store[1], store[0]);
                s.setOpened(Boolean.parseBoolean(store[2]));
                if (s.isOpened() == true) {
                    // add the store to the customers's available list of stores
                    this.stores.add(s);
                }
            }
            bfr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            ArrayList<String> appointmentCSV = new ArrayList<>();
            BufferedReader bfw = new BufferedReader(new FileReader("Appointment.csv"));
            String line = bfw.readLine();
            while (line != null) {
                appointmentCSV.add(line);
                line = bfw.readLine();
            }
            bfw.close();
            for (int i = 0; i < appointmentCSV.size(); i++) {
                String[] appointment = appointmentCSV.get(i).split(",");
                // parts[6] = status
                // parts[7] = requestedBy
                if (email.equals(appointment[9])) {
                    System.out.println("checking");
                    if (appointment[8].equals("approved")) {
                        appApts.add(new Appointment(appointment[0], appointment[6], Integer.parseInt(appointment[3]),
                                Integer.parseInt(appointment[4]), Integer.parseInt(appointment[5]),
                                Integer.parseInt(appointment[7]),
                                appointment[8], appointment[9], appointment[2], appointment[1]));
                        System.out.println("accept");
                    } else if (appointment[8].equals("requested")) {
                        pendApts.add(new Appointment(appointment[0], appointment[6], Integer.parseInt(appointment[3]),
                                Integer.parseInt(appointment[4]), Integer.parseInt(appointment[5]),
                                Integer.parseInt(appointment[7]),
                                appointment[8], appointment[9], appointment[2], appointment[1]));
                        System.out.println("pending");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
        System.out.println("Finished customer");

    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public boolean exportApts() {
        try {
            String filename = "ExportApts_"+getEmail()+".csv";
            File f = new File(filename);
            f.createNewFile();
            BufferedWriter bfw = new BufferedWriter(new FileWriter(f, false));
            for (int i = 0; i < appApts.size(); i++) {
                bfw.write(super.getEmail() + "," + appApts.get(i));
            }
            bfw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        } catch (IOException e) {
            System.out.println("Error printing to file");
            return false;
        }
        // true if exported approved appointments to exportAppointments.csv
        return true;

    }

    public ArrayList<Appointment> getAppApts() {
        return appApts;
    }

    public ArrayList<Appointment> getPendApts() {
        return pendApts;
    }

    public void setAppApts(ArrayList<Appointment> appApts) {
        this.appApts = appApts;
    }

    public void setPendApts(ArrayList<Appointment> pendApts) {
        this.pendApts = pendApts;
    }

    public ArrayList<String> stats() {
        ArrayList<String> stats = new ArrayList<>();
        for (int i = 0; i < this.stores.size(); i++) {
            String temp = "";
            temp += ("Store " + (i + 1) + ": " + this.stores.get(i).getTitle() + ". ");
            AppointmentWindow aw = null;
            for (int j = 0; j < this.stores.get(i).getCalendars().size(); j++) {
                int popularity = 0;
                for (int k = 0; k < this.stores.get(i).getCalendars().get(j).getAppointmentWindow().size(); k++) {
                    AppointmentWindow tempAW = this.stores.get(i).getCalendars().get(j).getAppointmentWindow().get(k);
                    if (tempAW.getApprovedAppointments().size() > popularity) {
                        popularity = tempAW.getApprovedAppointments().size();
                        aw = tempAW;
                    }
                }
            }
            if (aw != null) {
                temp += ("Most popular appointment window: " + aw);
            } else {
                temp += ("No appointments have been made yet");
            }
            stats.add(temp);
        }
        return stats;
    }

    public ArrayList<String> sortedStats() {
        ArrayList<String> sstats = stats();
        Collections.sort(sstats);
        return sstats;
    }

    public void rewriteEmail(String oldEmail){
        try{
            BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
            ArrayList<String> fileStuff = new ArrayList<String>();
            String line = bfr.readLine();
            if(line != null){
                String[] temp = line.split(",");
                boolean matches = false;
                int index = 0;
                for(int j = 0; j < temp.length; j++){
                    if(temp[j].equals(oldEmail)){
                        matches = true;
                        index = j;
                    }
                }
                if(matches){
                    temp[index] = getEmail();
                    String val = "";
                    for(int k = 0;k < temp.length;k++){
                        val+=temp[k];
                        if(k != temp.length-1){
                            val+=",";
                        }
                    }
                    fileStuff.add(val);
                } else {
                    fileStuff.add(line);
                }
                line = bfr.readLine();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Appointment.csv", false));
            for(int i = 0;i < fileStuff.size(); i++){
                bfw.write(fileStuff.get(i) + "\n");
            }
            bfr.close();
            bfw.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public ArrayList<String> getCustomerStoreNames() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
            String line = bfr.readLine();
            ArrayList<String> storeInfo = new ArrayList<>();
            // read in all the lines of the file
            while (line != null) {
                storeInfo.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            ArrayList<String> sellerStoreNames = new ArrayList<>();
            for (int i = 0; i < storeInfo.size(); i++) {
                String[] info = storeInfo.get(i).split(",");
                // info[0] = store name
                // info[1] = seller email
                //if (info[1].equals(this.getEmail())) {
                sellerStoreNames.add(info[0]);
                //}
            }
            return sellerStoreNames;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
