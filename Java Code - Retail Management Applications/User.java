import java.io.*;
import java.util.ArrayList;
/**
 * A User class that stores the information of the user's account details, creates account,
 * logs in, and handles deleting accounts. Gets inherited by Customer and Seller.
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Alexa Lau
 * @version April 9, 2023
 */
public class User {

    private String email;
    private String password;
    private String name;
    private final String role;

    public User(String name, String password, String email, String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;

    }

    public void delete() {      // Deletes the user's account
        ArrayList<String> fileStuff = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
            String line = bfr.readLine();
            while (line != null) {
                fileStuff.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            BufferedWriter bfw = new BufferedWriter(new FileWriter("User.csv", false));
            bfw.newLine();
            for (int i = 0; i < fileStuff.size(); i++) {
                if (!this.email.equals(fileStuff.get(i).substring(0, fileStuff.get(i).indexOf(",")))) {
                    bfw.write(fileStuff.get(i));
                    bfw.newLine();
                }
            }
            bfw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("An error occurred");
        }

        this.name = null;
        this.email = null;
        this.password = null;
    }

    public void setEmail(String email) {
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
                String email1 = s.substring(0, s.indexOf(","));
                if (email1.equals(this.email)) {
                    pw.println(email + "," + this.password + "," + this.role + "," + this.name);
                } else {
                    pw.println(s);
                }
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        this.email = email;
    }
    public void setPassword(String password) {
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
                if (arr[1].equals(this.password)) {
                    pw.println(this.email + "," + password + "," + this.role + "," + this.name);
                } else {
                    pw.println(s);
                }
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        this.password = password;
    }
    public void setName(String name) {
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
                if (arr[3].equals(this.name)) {
                    pw.println(this.email + "," + this.password + "," + this.role + "," + name);
                } else {
                    pw.println(s);
                }
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public void writeCSVFile(boolean isSeller){
        try { // Add user to user.csv
            ArrayList<String> file = new ArrayList<String>();
            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
            String line = bfr.readLine();
            while (line != null) {
                file.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            PrintWriter pw = new PrintWriter(new FileWriter("User.csv", false));
            for (String s : file) {
                pw.println(s);
            }
            pw.println(this.email + "," + this.password + "," + this.role + "," + this.name);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
