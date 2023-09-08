import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
/**
 *
 * A java class used to allow a user to sign up or log in
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 * @author Tarun Nandamudi
 * @version May 1, 2023
 */
public class MainLogin extends JComponent implements Runnable {
    Thread t;
    private Seller seller;
    private Customer customer;
    JButton signupbButton;
    JFrame begin;
    JFrame loginstuff;
    String userValues[];
    JLabel mainloginLabel;

    JButton loginButton;
    JButton signupButton;
    JTextField userField;
    JPasswordField passField;
    private static PrintWriter pw;
    private static BufferedReader bfr;
    private boolean finished;
    static Socket socket;
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new MainLogin());
    }


    @Override
    public synchronized void run() {
        try {
            client();
        } catch (IOException e) {
        }
    }

    public void client() throws IOException {
        socket = new Socket("localhost",4242);
        bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream());
        begin = new JFrame("Escape Room Scheduler Application");
        Container begincontent = begin.getContentPane();
        begincontent.setLayout(new GridLayout(4,1));
        begin.setSize(600, 400);
        begin.setLocationRelativeTo(null);
        begin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        begin.setVisible(true);
        mainloginLabel = new JLabel("Main Login Page");
        mainloginLabel.setFont(new Font("Serif", Font.BOLD, 36));
        mainloginLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainloginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        begincontent.add(mainloginLabel);

        mainloginLabel = new JLabel("Click Login to log in with your user info. To create an account, enter your user info and press Sign-Up.");
        mainloginLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        mainloginLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainloginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        begincontent.add(mainloginLabel);

        JPanel signinPanel = new JPanel();
        signinPanel.add(new JLabel("Enter Username (Email):"));
        userField = new JTextField(10);
        signinPanel.add(userField);
        signinPanel.add(new JLabel("Enter Password:"));
        passField = new JPasswordField(10);
        signinPanel.add(passField);
        userField.addActionListener(actionListener);
        passField.addActionListener(actionListener);
        begincontent.add(signinPanel,BorderLayout.CENTER);

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        JPanel beginpanel = new JPanel();
        beginpanel.add(loginButton);
        beginpanel.add(signupButton);
        loginButton.addActionListener(actionListener);
        signupButton.addActionListener(actionListener);
        begincontent.add(beginpanel,BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            finished = false;
            if(e.getSource() == loginButton){
                String email = userField.getText();
                char[] passArray = passField.getPassword();
                String password = "";
                for(int i = 0; i < passArray.length;i++){
                    password += passArray[i];
                }

                String[] temp = new String[0];
                try {
                    temp = findUser(email, password);
                } catch (IOException ex) {
                }
                if(finished) {
                    userValues = temp;
                    if(userValues[2].equals("seller")){
                        seller = new Seller(userValues[3], userValues[1], userValues[0]);
                        SwingUtilities.invokeLater(new SellerFrame(seller, socket));
                        userField.setText("");
                        passField.setText("");
                        // begin.dispose();
                    }else if(userValues[2].equals("customer")) {
                        customer = new Customer(userValues[3], userValues[1], userValues[0]);
                        //System.out.println("creating customer gui");
                        SwingUtilities.invokeLater((new CustomerGUI(customer, socket)));
                        //System.out.println("created customer gui");
                        userField.setText("");
                        passField.setText("");
                        // begin.dispose();
                    }
                }
            } else if(e.getSource() == signupButton){
                finished = false;
                userValues = new String[4];
                userValues[0] = userField.getText();
                char[] passArray = passField.getPassword();
                String password = "";
                for(int i = 0; i < passArray.length;i++){
                    password += passArray[i];
                }
                userValues[1] = password;
                userValues[3] = (String) JOptionPane.showInputDialog(null,
                        "What would you like your user to be named?",
                        "Escape Room Scheduler: Sign Up Window", JOptionPane.QUESTION_MESSAGE);
                String[] options = {"seller", "customer"};
                userValues[2] = (String) JOptionPane.showInputDialog(null,
                        "What role would you like your user to have?",
                        "Escape Room Scheduler: Sign Up Window", JOptionPane.QUESTION_MESSAGE, null,
                        options, null);
                try {
                    checkUser(userValues);
                } catch (IOException ex) {
                }
                if(finished){
                    if (userValues[2].equalsIgnoreCase("seller")) {
                        seller = new Seller(userValues[3], userValues[1], userValues[0]);
                        // seller.writeCSVFile(true);
                        SwingUtilities.invokeLater(new SellerFrame(seller, socket));
                        userField.setText("");
                        passField.setText("");
                        // begin.dispose();
                    } else if (userValues[2].equalsIgnoreCase("customer")) {
                        customer = new Customer(userValues[3], userValues[1], userValues[0]);
                        // customer.writeCSVFile(false);
                        SwingUtilities.invokeLater((new CustomerGUI(customer, socket)));
                        userField.setText("");
                        passField.setText("");
                        // begin.dispose();
                    }
                }
            }
        }
    };

    public void checkUser(String[] email) throws IOException {
        boolean exists = false;
        /*
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
            String line = bfr.readLine();
            ArrayList<String> userInfo = new ArrayList<>();
            while (line != null) {
                userInfo.add(line);
                line = bfr.readLine();
            }
            for (int i = 0; i < userInfo.size(); i++) {
                String[] temp = userInfo.get(i).split(",");
                if (temp[0].equals(email)) {
                    exists = true;
                }
            }
            bfr.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error logging in." +
                    " Please try again.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        */
        pw.println("signup");
        pw.println(email[0]+"," + email[1] + "," + email[2] + "," + email[3]);
        pw.flush();
        String s = bfr.readLine();
        if(!s.equals("success")){
            exists = true;
        }
        if (exists){
            JOptionPane.showMessageDialog(null, "An account with that email has already been created." +
                    " Please try again.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        } else {
//            user.setName(userValues[3]);
//            user.setEmail(userValues[0]);
//            user.setPassword(userValues[1]);
            JOptionPane.showMessageDialog(null, "Succesfully Signed Up!", "Sign Up Window", JOptionPane.INFORMATION_MESSAGE);
            finished = true;
        }
    }

    public String[] findUser(String email, String password) throws IOException {

        String[] userVal = null;
        /*
        boolean login = false;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("User.csv"));
            String line = bfr.readLine();
            ArrayList<String> userInfo = new ArrayList<>();
            while (line != null) {
                userInfo.add(line);
                line = bfr.readLine();
            }
            for (int i = 0; i < userInfo.size(); i++) {
                String[] temp = userInfo.get(i).split(",");
                if (temp[0].equals(email) && temp[1].equals(password)) {
                    userVal = temp;
                    login = true;
                }
            }
            bfr.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error logging in." +
                    " Please try again.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        */
        boolean login;
        pw.println("login");
        pw.println(email + "," + password);
        pw.flush();
        String s = bfr.readLine();
        // System.out.println(s);
        if(s.equals("success")){
            login = true;
            s = bfr.readLine();
            // System.out.println(s);
            userVal = s.split(",");
        }else{
            login = false;
        }
        if (!login){
            JOptionPane.showMessageDialog(null, "Invalid email or password." +
                    "Please try again.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Succesfully Logged In!", "Log-in Window", JOptionPane.INFORMATION_MESSAGE);
            finished = true;
        }
        return userVal;
    }

    public MainLogin(){
        finished = false;
    }
}
