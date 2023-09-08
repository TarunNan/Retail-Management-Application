import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * A java class used to create a Seller object for seller users. **DRAFT**
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Group 4
 * @version April 8, 2023
 */
public class Seller extends User {
    private ArrayList<Store> stores;

    public Seller(String name, String password, String email) {
        super(name, password, email, "seller");
        this.stores = new ArrayList<Store>();
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
                // check if seller is the owner of the store
                if (store[1].equals(email)) {
                    // create a new store object
                    Store s = new Store(email, store[0]);
                    // add the store to the seller's list of stores
                    this.stores.add(s);
                }
            }
            bfr.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
        /*
        try {
            ArrayList<String> file = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
            String line = bfr.readLine();
            while (line != null) {
                file.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            PrintWriter pw = new PrintWriter(new FileWriter("User.csv", false));
            for (String s : file) {
                String[] arr = s.split(",");
                if (arr[0].equals(getEmail())) {
                    String answer = getEmail() + "," + getPassword() + "," + getRole() + "," + getName();
                    for(int i = 0; i < stores.size(); i++){
                        if(i == 0){
                            answer+= ", ";
                        }
                        answer += stores.get(i);
                        if(i != stores.size()-1){
                            answer+= ";";
                        }
                    }
                    pw.println(answer);
                } else {
                    pw.println(s);
                }
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } */
    }

    public ArrayList<Appointment> getApptReqs() {
        ArrayList<Appointment> requests = new ArrayList<Appointment>();
        for (int z = 0; z < stores.size(); z++) {
            for (int i = 0; i < stores.get(z).getCalendars().size(); i++) {
               for (int k = 0; k < stores.get(z).getCalendars().get(i).getAppointments().size(); k++) {
                    Appointment a = stores.get(z).getCalendars().get(i).getAppointments().get(k);
                    if (a.getStatus().equals("requested")) {
                        requests.add(a);
                    }
                }
            }
        }
        return requests;
    }

    public void rewriteEmail(String oldEmail){
        try{
            String[] arr = new String[3];
            arr[0] = "Store.csv";
            arr[1] = "Calendar.csv";
            arr[2] = "AppointmentWindow.csv";
            for(int i = 0; i < arr.length; i++){
                BufferedReader bfr = new BufferedReader(new FileReader(arr[i]));
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
                BufferedWriter bfw = new BufferedWriter(new FileWriter(arr[i]));
                for(int j = 0;j < fileStuff.size();j++){
                    bfw.write(fileStuff.get(j) + "\n");
                }
                bfw.close();
                bfr.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<String> getStoreStat(){
        ArrayList<String> stats = new ArrayList<>();
        for(int i = 0; i < stores.size(); i ++){
            stats.add(stores.get(i).storeStats());
        }
        return stats;
    }

    public ArrayList<String> getSortedStoreStat(){
        ArrayList<String> stats = getStoreStat();
        Collections.sort(stats);
        return stats;
    }

    public ArrayList<String> getSellerStoreNames() {
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
                if (info[1].equals(this.getEmail())) {
                    sellerStoreNames.add(info[0]);
                }
            }
            return sellerStoreNames;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

       /*      Stuff for rescheduling
    public boolean moveAppt(Appointment appointment, int moveTime) {
        for (int z = 0; z < stores.size(); z++) {
            for (int i = 0; i < stores.get(z).getCalendars().size(); i++) {
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < stores.get(z).getCalendars().get(i).getDays[j].getAppointments.size(); k++) {
                        Appointment a = stores.get(z).getCalendars().get(i).getDays[j].getAppointments.get(k);
                        if (appointment.equals(a)) {
                            AppointmentWindow current = appointment.getApptWindow();
                            if (!current.isFull()) {
                                int stime = a.getStartTime() + moveTime;
                                int etime = a.getEndTime() + moveTime;
                                Appointment[] array = current.getApptArray();
                                if (array[stime - current.getStartTime()] == false) {
                                    array[a.getStart() - current.getStart()] = false;
                                    array[stime - current.getStart()] = true;
                                    a.setStartTime() = stime;
                                    a.setEndTime() = etime;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    */

}
