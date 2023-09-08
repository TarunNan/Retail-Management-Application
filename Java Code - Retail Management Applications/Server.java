import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * A java class used to run the server
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 * @author Aaron Parihar
 * @version May 1, 2023
 */
public class Server implements Runnable{
    Socket socket;

    public synchronized static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(4242);
        ArrayList<Server> serverthreads = new ArrayList<>();
        while(true){
            Socket socket = server.accept();
            Server servers = new Server(socket);
            serverthreads.add(servers);
            Thread mythread = new Thread(servers);
            mythread.start();
        }
        // // System.out.println("Server started at port 4242. Waiting for connection...");

        // // System.out.println("Client connected.");
    }

    public Server(Socket socket){
        this.socket = socket;
    }


    @Override
    public synchronized void run() {
        BufferedReader serverIn = null;
        try {
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter serverOut = null;
        try {
            serverOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String lineval = "";
        while (true) {
            try {
                if (!((lineval = serverIn.readLine()) !=null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String instruction = lineval;
            // // System.out.println("Instruction received: " + instruction);

            String data = null;
            try {
                data = serverIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // // System.out.println("Data received: " + data);

            switch (instruction) {
                case "requestAppointment":
                    // // System.out.println(
                    // "Expected data format: title,winTitle,calTitle,start,end,date,store,party,status,email,timestamp");
                    String[] requestData = data.split(",");
                    if (requestData.length != 11) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
                        synchronized (this) {
                            BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                            String line = "";
                            ArrayList<String> arr = new ArrayList<>();
                            if ((line = bfr.readLine()) != null) {
                                arr.add(line);
                            }
                            PrintWriter pw = new PrintWriter(new FileWriter("Appointment.csv", false));
                            for (int i = 0; i < arr.size(); i++) {
                                pw.println(arr.get(i));
                            }
                            pw.println(data);
                            pw.close();
                        }
                        // // System.out.println("done");
                        // serverOut.println("success");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "signup":
                    // // System.out.println("Expected data format: email,name,password,role");

                    String[] signupData = data.split(",");
                    if (signupData.length != 4) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }

                    String email = signupData[0];
                    String password = signupData[1];
                    String name = signupData[2];
                    String role = signupData[3];
                    // // System.out.println("Email: " + email);
                    // // System.out.println("Name: " + name);
                    // // System.out.println("Password: " + password);
                    // // System.out.println("Role: " + role);

                    // Check if username already exists
                    try {
                        synchronized(this) {
                            File f = new File("User.csv");
                            BufferedReader bfr = new BufferedReader(new FileReader(f));
                            String line = bfr.readLine();
                            ArrayList<String> signUpEmails = new ArrayList<>();
                            while (line != null) {
                                String[] user = line.split(",");
                                signUpEmails.add(user[0]);
                                line = bfr.readLine();
                            }
                            bfr.close();
                            if (signUpEmails.contains(email)) {
                                // // System.out.println("failed - email already exists");
                                serverOut.println("failed");
                                serverOut.flush();
                                break;
                            } else {
                                FileOutputStream fos = new FileOutputStream(f, true);
                                PrintWriter pw = new PrintWriter(fos);
                                pw.println(email + "," + password + "," + name + "," + role);
                                pw.flush();
                                pw.close();
                                // // System.out.println("Signup Successful");
                                serverOut.println("success");
                                serverOut.flush();
                            }
                        }
                    } catch (Exception e) {
                        // // System.out.println("Signup Unsuccessful " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "login":
                    // // System.out.println("Expected data format: email,password");

                    String[] loginData = data.split(",");
                    if (loginData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }

                    String loginEmail = loginData[0];
                    String loginPassword = loginData[1];
                    // // System.out.println("Email: " + loginEmail);
                    // // System.out.println("Password: " + loginPassword);

                    // Read users.csv
                    try {
                        ArrayList<String> users;
                        synchronized(this) {
                            users = new ArrayList<>();
                            BufferedReader bfr1 = new BufferedReader(new FileReader("User.csv"));
                            String line1 = bfr1.readLine();
                            while (line1 != null) {
                                users.add(line1);
                                line1 = bfr1.readLine();
                            }
                            bfr1.close();
                        }
                        boolean login = false;
                        for (String s : users) {
                            String[] user = s.split(",");
                            if (user[0].equals(loginEmail) && user[1].equals(loginPassword)) {
                                // // System.out.println("Login Successful");
                                serverOut.println("success");
                                login = true;
                                serverOut.println(s);
                                serverOut.flush();
                                break;
                            }
                        }
                        if(!login){
                            // // System.out.println("Login Unsuccessful - no match");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("Login Unsuccessful " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "updateAccount":
                    // // System.out.println("Expected data format: email,name,password");
                    String[] accountInfo = data.split(",");
                    if (accountInfo.length != 3) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
//                        synchronized (this) {
                            ArrayList<String> file = new ArrayList<>();
                            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
                            String line = bfr.readLine();
                            while (line != null) {
                                file.add(line);
                                line = bfr.readLine();
                            }
                            bfr.close();
                            PrintWriter pw = new PrintWriter(new FileWriter("User.csv", false));
                            //pw.println("email,password,role,name");
                            for (String s : file) {
                                String[] arr = s.split(",");
                                if (arr[0].equals(accountInfo[0])) {
                                    pw.println(accountInfo[0] + "," + accountInfo[2] + "," + arr[2] + "," + accountInfo[1]);
                                } else {
                                    pw.println(s);
                                }
                            }
                            pw.close();
                            // // System.out.println("Account Updated");
                            serverOut.println("success");
                            serverOut.flush();
//                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "deleteAccount":
                    // // System.out.println("Expected data format: email");
                    String deleteEmail = data;
                    ArrayList<String> users = new ArrayList<>();
                    try {
                        BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            users.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();

                        BufferedWriter bfw = new BufferedWriter(new FileWriter("User.csv", false));
                        bfw.write("email,password,role,name");
                        bfw.newLine();
                        boolean foundDelete = false;
                        for (int i = 0; i < users.size(); i++) {
                            String[] user = users.get(i).split(",");
                            if (user[0].equals(deleteEmail)) {
                                foundDelete = true;
                            } else {
                                bfw.write(users.get(i));
                                bfw.newLine();
                            }
                        }
                        if (foundDelete) {
                            // // System.out.println("Account deleted");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Account not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                        bfw.close();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "createStore":
                    // // System.out.println("Expected data format: email,storeName");
                    String[] createStoreData = data.split(",");
                    if (createStoreData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String createStoreEmail = createStoreData[0];
                    String createStoreName = createStoreData[1];
                    try {
                        PrintWriter pw = new PrintWriter(new FileWriter("Store.csv", true));
                        pw.println(createStoreEmail + "," + createStoreName);
                        pw.close();
                        // // System.out.println("Store created");
                        serverOut.println("success");
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewSellerStores":
                    // // System.out.println("Expected data format: email");
                    String sellerEmail = data;
                    try {
                        ArrayList<String> stores = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            stores.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> sellerStores = new ArrayList<>();
                        for (String s : stores) {
                            String[] store = s.split(",");
                            if (store[0].equals(sellerEmail)) {
                                sellerStores.add(store[1]);
                            }
                        }
                        // // System.out.println("Stores found");
                        serverOut.println("success");
                        serverOut.flush();
                        for (String s : sellerStores) {
                            serverOut.println(s);
                            serverOut.flush();
                        }
                        serverOut.println("end");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewStore":
                    // // System.out.println("Expected data format: email,storeName");
                    String[] viewStoreData = data.split(",");
                    if (viewStoreData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String viewStoreEmail = viewStoreData[0];
                    String viewStoreName = viewStoreData[1];
                    try {
                        ArrayList<String> stores = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            stores.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundStore = false;
                        String store = null;
                        for (String s : stores) {
                            String[] stores2 = s.split(",");
                            if (stores2[0].equals(viewStoreEmail) && stores2[1].equals(viewStoreName)) {
                                foundStore = true;
                                store = s;
                            }
                        }
                        if (foundStore) {
                            // // System.out.println("Store found");
                            serverOut.println("success");
                            serverOut.flush();
                            serverOut.println(store);
                            serverOut.flush();
                        } else {
                            // // System.out.println("Store not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewPendingAppointments":
                    // // System.out.println("Expected data format: store");
                    String store = data;
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> pendingAppointments = new ArrayList<>();
                        for (String a : appointments) {
                            String[] appointment = a.split(",");
                            if (appointment[6].equals(store) && appointment[8].equals("pending")) {
                                pendingAppointments.add(a);
                            }
                        }
                        // // System.out.println("Appointments found");
                        serverOut.println("success");
                        serverOut.flush();
                        for (String a : pendingAppointments) {
                            serverOut.println(a);
                            serverOut.flush();
                        }
                        serverOut.println("end");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }

                    break;

                case "approveDenyAppointment":
                    // // System.out.println("Expected data format: appointmentTitle,storeTitle,status,timeStamp");
                    String[] approveDenyData = data.split(",");
                    if (approveDenyData.length != 4) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String appointmentTitle = approveDenyData[0];
                    String storeTitle = approveDenyData[1];
                    String status = approveDenyData[2];
                    String timeStamp = approveDenyData[3];
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundAppointment = false;
                        String appointment = null;
                        for (String a : appointments) {
                            String[] appointments2 = a.split(",");
                            if (appointments2[0].equals(appointmentTitle) && appointments2[6].equals(storeTitle)) {
                                foundAppointment = true;
                                appointment = a;
                            }
                        }
                        if (foundAppointment) {
                            String[] appointment2 = appointment.split(",");
                            appointment2[8] = status;
                            String modApp = appointment2[0] + "," + appointment2[1] + "," + appointment2[2] + ","
                                    + appointment2[3] + "," + appointment2[4] + "," + appointment2[5] + ","
                                    + appointment2[6] + "," + appointment2[7] + "," + appointment2[8] + appointment2[9]
                                    + "," + timeStamp;
                            PrintWriter pw = new PrintWriter(new FileWriter("Appointment.csv"));
                            for (String a : appointments) {
                                if (a.equals(appointment)) {
                                    pw.println(modApp);
                                } else {
                                    pw.println(a);
                                }
                            }
                            pw.close();
                            // // System.out.println("Appointment updated");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Appointment not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "importCalendar":
                    // // System.out.println("Expected data format: fileName");
                    String fileName = data;
                    try {
                        ArrayList<String> cals = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                        String line = bfr.readLine();
                        while (line != null) {
                            cals.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv", true));
                        for (String c : cals) {
                            pw.println(c);
                        }
                        pw.close();
                        // // System.out.println("Calendar imported");
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "createCalendar":
                    // // System.out.println("Expected data format: email,store,title,description");
                    String[] createCalendarData = data.split(",");
                    if (createCalendarData.length != 4) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
                        PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv", true));
                        pw.println(data);
                        pw.close();
                        // // System.out.println("Calendar created");
                        serverOut.println("success");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "addAppointmentWindow":
                    // // System.out.println("Expected data format: email,calendar,title,day,start,end,maxAttend");
                    String[] addAppointmentWindowData = data.split(",");
                    if (addAppointmentWindowData.length != 7) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
                        PrintWriter pw = new PrintWriter(new FileWriter("AppointmentWindow.csv", true));
                        pw.println(data);
                        pw.close();
                        // // System.out.println("Appointment window created");
                        serverOut.println("success");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }

                    break;

                case "deleteAppointmentWindow":
                    // // System.out.println("Expected data format: email,calendar,title");
                    String[] deleteAppointmentWindowData = data.split(",");
                    if (deleteAppointmentWindowData.length != 3) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
                        ArrayList<String> appointmentWindows = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("AppointmentWindow.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointmentWindows.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundAppointmentWindow = false;
                        String appointmentWindow = null;
                        for (String a : appointmentWindows) {
                            String[] appointmentWindows2 = a.split(",");
                            if (appointmentWindows2[0].equals(deleteAppointmentWindowData[0])
                                    && appointmentWindows2[1].equals(deleteAppointmentWindowData[1])
                                    && appointmentWindows2[2].equals(deleteAppointmentWindowData[2])) {
                                foundAppointmentWindow = true;
                                appointmentWindow = a;
                            }
                        }
                        if (foundAppointmentWindow) {
                            PrintWriter pw = new PrintWriter(new FileWriter("AppointmentWindow.csv"));
                            for (String a : appointmentWindows) {
                                if (!a.equals(appointmentWindow)) {
                                    pw.println(a);
                                }
                            }
                            pw.close();
                            // // System.out.println("Appointment window deleted");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Appointment window not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }

                case "viewCalendars":
                    // // System.out.println("Expected data format: store");
                    String storeView = data;
                    try {
                        ArrayList<String> calendars = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            calendars.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> storeCalendars = new ArrayList<>();
                        for (String c : calendars) {
                            String[] calendar = c.split(",");
                            if (calendar[1].equals(storeView)) {
                                storeCalendars.add(c);
                            }
                        }
                        // // System.out.println("Calendars found");
                        serverOut.println("success");
                        serverOut.flush();
                        for (String c : storeCalendars) {
                            serverOut.println(c);
                            serverOut.flush();
                        }
                        serverOut.println("end");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCalendar":
                    // // System.out.println("Expected data format: email,store,title");
                    String[] viewCalendarData = data.split(",");
                    if (viewCalendarData.length != 3) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    try {
                        ArrayList<String> calendars = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            calendars.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundCalendar = false;
                        String calendar = null;
                        for (String c : calendars) {
                            String[] calendars2 = c.split(",");
                            if (calendars2[0].equals(viewCalendarData[0])
                                    && calendars2[1].equals(viewCalendarData[1])
                                    && calendars2[2].equals(viewCalendarData[2])) {
                                foundCalendar = true;
                                calendar = c;
                            }
                        }
                        if (foundCalendar) {
                            // // System.out.println("Calendar found");
                            serverOut.println("success");
                            serverOut.flush();
                            serverOut.println(calendar);
                            serverOut.flush();
                            serverOut.println("end");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Calendar not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "deleteCalendar":
                    // // System.out.println("Expected data format: calendarTitle,storeTitle");
                    String[] deleteCalendarData = data.split(",");
                    if (deleteCalendarData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String calendarTitle = deleteCalendarData[0];
                    String storeDeleteTitle = deleteCalendarData[1];
                    try {
                        ArrayList<String> calendars = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Calendar.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            calendars.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundCalendar = false;
                        String calendar = null;
                        for (String c : calendars) {
                            String[] calendars2 = c.split(",");
                            if (calendars2[0].equals(calendarTitle) && calendars2[1].equals(storeDeleteTitle)) {
                                foundCalendar = true;
                                calendar = c;
                            }
                        }
                        if (foundCalendar) {
                            PrintWriter pw = new PrintWriter(new FileWriter("Calendar.csv"));
                            for (String c : calendars) {
                                if (!c.equals(calendar)) {
                                    pw.println(c);
                                }
                            }
                            pw.close();
                            // // System.out.println("Calendar deleted");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Calendar not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "searchCustomer":
                    // // System.out.println("Expected data format: customerEmail");
                    String customerEmail = data;
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> customerAppointments = new ArrayList<>();
                        for (String a : appointments) {
                            String[] appointment = a.split(",");
                            if (appointment[9].equals(customerEmail)) {
                                customerAppointments.add(a);
                            }
                        }
                        if (customerAppointments.size() > 0) {
                            // // System.out.println("Appointments found");
                            serverOut.println("success");
                            serverOut.flush();
                            for (String a : customerAppointments) {
                                serverOut.println(a);
                                serverOut.flush();
                            }
                            serverOut.println("end");
                            serverOut.flush();
                        } else {
                            // // System.out.println("No appointments found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerStores":
                    try {
                        ArrayList<String> stores = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            stores.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        for (String s : stores) {
                            serverOut.println(s);
                            serverOut.flush();
                        }
                        serverOut.println("end");
                        serverOut.flush();
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerStore":
                    // // System.out.println("Expected data format: storeTitle");
                    String storeTitle2 = data;
                    try {
                        ArrayList<String> stores = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Store.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            stores.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundStore = false;
                        String store2 = null;
                        for (String s : stores) {
                            String[] store3 = s.split(",");
                            if (store3[0].equals(storeTitle2)) {
                                foundStore = true;
                                store = s;
                            }
                        }
                        if (foundStore) {
                            serverOut.println("success");
                            serverOut.flush();
                            serverOut.println(store2);
                            serverOut.flush();
                        } else {
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerConfirmedAppointments":
                    // // System.out.println("Expected data format: customerEmail");
                    String customerEmail2 = data;
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> customerAppointments = new ArrayList<>();
                        for (String a : appointments) {
                            String[] appointment = a.split(",");
                            if (appointment[9].equals(customerEmail2) && appointment[8].equals("confirmed")) {
                                customerAppointments.add(a);
                            }
                        }
                        if (customerAppointments.size() > 0) {
                            // // System.out.println("Appointments found");
                            serverOut.println("success");
                            serverOut.flush();
                            for (String a : customerAppointments) {
                                serverOut.println(a);
                                serverOut.flush();
                            }
                            serverOut.println("end");
                            serverOut.flush();
                        } else {
                            // // System.out.println("No appointments found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerConfirmedAppointment":
                    // // System.out.println("Expected data format: appointmentTitle,email");
                    String[] viewData = data.split(",");
                    if (viewData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String appointmentTitle3 = viewData[0];
                    String email3 = viewData[1];
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundAppointment = false;
                        String appointment = null;
                        for (String a : appointments) {
                            String[] appointment2 = a.split(",");
                            if (appointment2[0].equals(appointmentTitle3) && appointment2[9].equals(email3)
                                    && appointment2[8].equals("confirmed")) {
                                foundAppointment = true;
                                appointment = a;
                            }
                        }
                        if (foundAppointment) {
                            serverOut.println("success");
                            serverOut.flush();
                            serverOut.println(appointment);
                            serverOut.flush();
                        } else {
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "cancelAppointment":
                    // // System.out.println("Expected data format: appointmentTitle,storeTitle,timeStamp");
                    String[] cancelData = data.split(",");
                    if (cancelData.length != 3) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String appointmentTitle1 = cancelData[0];
                    String storeTitle1 = cancelData[1];
                    String timeStamp1 = cancelData[2];
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundAppointment = false;
                        String appointment = null;
                        for (String a : appointments) {
                            String[] appointments2 = a.split(",");
                            if (appointments2[0].equals(appointmentTitle1) && appointments2[6].equals(storeTitle1)) {
                                foundAppointment = true;
                                appointment = a;
                            }
                        }
                        if (foundAppointment) {
                            String[] appointment2 = appointment.split(",");
                            appointment2[8] = "cancelled";
                            String modApp = appointment2[0] + "," + appointment2[1] + "," + appointment2[2] + ","
                                    + appointment2[3] + "," + appointment2[4] + "," + appointment2[5] + ","
                                    + appointment2[6] + "," + appointment2[7] + "," + appointment2[8] + appointment2[9]
                                    + "," + timeStamp1;
                            PrintWriter pw = new PrintWriter(new FileWriter("Appointment.csv"));
                            for (String a : appointments) {
                                if (a.equals(appointment)) {
                                    pw.println(modApp);
                                } else {
                                    pw.println(a);
                                }
                            }
                            pw.close();
                            // // System.out.println("Appointment cancelled");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Appointment not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerPendingAppointments":
                    // // System.out.println("Expected data format: email");
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> pendingAppointments = new ArrayList<>();
                        for (String a : appointments) {
                            String[] appointments2 = a.split(",");
                            if (appointments2[9].equals(data) && appointments2[8].equals("pending")) {
                                pendingAppointments.add(a);
                            }
                        }
                        if (pendingAppointments.size() > 0) {
                            for (String a : pendingAppointments) {
                                serverOut.println(a);
                                serverOut.flush();
                            }
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("No pending appointments found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;

                case "viewCustomerPendingAppointment":
                    // // System.out.println("Expected data format: email,title");
                    String[] pendingData = data.split(",");
                    if (pendingData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String email1 = pendingData[0];
                    String title1 = pendingData[1];
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        boolean foundAppointment = false;
                        String appointment = null;
                        for (String a : appointments) {
                            String[] appointments2 = a.split(",");
                            if (appointments2[0].equals(title1) && appointments2[9].equals(email1)
                                    && appointments2[8].equals("pending")) {
                                foundAppointment = true;
                                appointment = a;
                            }
                        }
                        if (foundAppointment) {
                            serverOut.println(appointment);
                            serverOut.flush();
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("Appointment not found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;




                case "exportAppointments":
                    // // System.out.println("Expected data format: email,fileName");
                    String[] exportData = data.split(",");
                    if (exportData.length != 2) {
                        // // System.out.println("failed - invalid length");
                        serverOut.println("failed");
                        serverOut.flush();
                        break;
                    }
                    String expEmail = exportData[0];
                    String expFile = exportData[1];
                    try {
                        ArrayList<String> appointments = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader("Appointment.csv"));
                        String line = bfr.readLine();
                        while (line != null) {
                            appointments.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();
                        ArrayList<String> customerAppointments = new ArrayList<>();
                        for (String a : appointments) {
                            String[] appointments2 = a.split(",");
                            if (appointments2[9].equals(expEmail)) {
                                customerAppointments.add(a);
                            }
                        }
                        if (customerAppointments.size() > 0) {
                            PrintWriter pw = new PrintWriter(new FileWriter(expFile));
                            for (String a : customerAppointments) {
                                pw.println(a);
                            }
                            pw.close();
                            // // System.out.println("Appointments exported");
                            serverOut.println("success");
                            serverOut.flush();
                        } else {
                            // // System.out.println("No appointments found");
                            serverOut.println("failed");
                            serverOut.flush();
                        }
                    } catch (Exception e) {
                        // // System.out.println("An error occurred: " + e.getMessage());
                        serverOut.println("failed");
                        serverOut.flush();
                    }
                    break;
            }
        }
        try {
            serverIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverOut.close();
    }
}
