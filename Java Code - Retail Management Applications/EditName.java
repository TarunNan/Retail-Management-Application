import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
/**
 *
 * A java class used to edit a user's name
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 * @author Tarun Nandamudi
 * @version May 1, 2023
 */
public class EditName extends JComponent implements Runnable {
    Socket socket;
    BufferedReader bfr;
    PrintWriter pw;
    private JButton enterButton;
    private JTextField newname;
    private JPasswordField cpassword;
    private String name;
    private String password;
    private JLabel titleLabel;
    private JFrame enameframe;
    private JLabel currentstuff;
    private String email;
    private Seller seller;
    private Customer customer;
    private boolean isSeller;

    public static void main(String[] args) {
    }

    @Override
    public synchronized void run() {
        enameframe = new JFrame("Escape Room Scheduler Application");
        Container enamecontent = enameframe.getContentPane();
        enamecontent.setLayout(new GridLayout(3,1));
        enameframe.setSize(800, 300);
        enameframe.setLocationRelativeTo(null);
        enameframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        enameframe.setVisible(true);
        titleLabel = new JLabel("Settings: Edit Name for User: " + this.email);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enamecontent.add(titleLabel);
        JPanel currentinfo = new JPanel();
        currentstuff = new JLabel("Current Name: " + this.name);
        currentstuff.setFont(new Font("Tahoma", Font.BOLD, 14));
        currentstuff.setVerticalAlignment(SwingConstants.CENTER);
        currentstuff.setHorizontalAlignment(SwingConstants.CENTER);
        currentinfo.add(currentstuff);
        enamecontent.add(currentinfo, BorderLayout.CENTER);
        JPanel namepanel = new JPanel();
        namepanel.add(new JLabel("Enter New Name:"));
        newname = new JTextField(10);
        namepanel.add(newname);
        namepanel.add(new JLabel("Confirm Password:"));
        cpassword = new JPasswordField(10);
        namepanel.add(cpassword);
        enterButton = new JButton("Enter");
        namepanel.add(enterButton);
        enamecontent.add(namepanel,BorderLayout.CENTER);
        enterButton.addActionListener(actionListener);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if(e.getSource() == enterButton){
                String nname = (String) newname.getText();
                char[] passArray = cpassword.getPassword();
                String currpass = "";
                for(int i = 0; i < passArray.length;i++){
                    currpass += passArray[i];
                }
                boolean canchange = false;
                try {
                    canchange = changeStuff(currpass, nname);
                } catch (IOException ex) {
                }
                if(isSeller){
                    if(canchange){
                        name = nname;
                        currentstuff.setText("Current Name: " + name);
                        enameframe.dispose();
                    }
                }else{
                    if(canchange){
                        name = nname;
                        currentstuff.setText("Current Name: " + name);
                        enameframe.dispose();
                    }
                }
            }
        }
    };

    public boolean changeStuff(String currpass, String nname) throws IOException {

        if(currpass.equals(password)) {
            if(isSeller) {
                pw.println("updateAccount");
                pw.println(seller.getEmail() + "," + nname + "," + seller.getPassword());
                String s = "";
                if(bfr.ready()) {
                    s = bfr.readLine();
                    if (s.equals("success")) {
                        JOptionPane.showMessageDialog(null, "Succesfully Changed Name!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "An Error has Occurred!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
            }else{
                pw.println("updateAccount");
                pw.println(customer.getEmail() + "," + nname + "," + customer.getPassword());
                pw.flush();
                String s = "";
                if(bfr.ready()) {
                    s = bfr.readLine();
                    if (s.equals("success")) {
                        JOptionPane.showMessageDialog(null, "Succesfully Changed Name!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "An Error has Occurred!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
            }
            return false;
        }else{
            JOptionPane.showMessageDialog(null, "Incorrect Password!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    public EditName(Seller s, Socket socket) throws IOException {
        this.name = s.getName();
        this.password = s.getPassword();
        this.email = s.getEmail();
        this.seller = s;
        this.isSeller = true;
        this.socket = socket;
        this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(socket.getOutputStream());
    }
    public EditName(Customer customer, Socket socket) throws IOException {
        this.name = customer.getName();
        this.password = customer.getPassword();
        this.email = customer.getEmail();
        this.customer = customer;
        this.isSeller = false;
        this.socket = socket;
        this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(socket.getOutputStream());
    }

    public Seller getSeller() {
        return seller;
    }

    public Customer getCustomer() {
        return customer;
    }

}
