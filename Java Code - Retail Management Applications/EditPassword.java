import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 *
 * A java class used to edit a user's password
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 05
 *
 * @author Tarun Nandamudi
 * @version May 1, 2023
 */
public class EditPassword extends JComponent implements Runnable {
    private JButton enterButton;
    private JPasswordField npassword;
    private JPasswordField cpassword;
    private JPasswordField oldpassword;
    private String name;
    private String password;
    private JLabel titleLabel;
    private JFrame epassframe;
    private JLabel currentstuff;
    private String email;
    private Seller seller;
    private Customer customer;
    private boolean isSeller;
    Socket socket;
    BufferedReader bfr;
    PrintWriter pw;

    public static void main(String[] args) {
    }

    @Override
    public void run() {
        epassframe = new JFrame("Escape Room Scheduler Application");
        Container epasscontent = epassframe.getContentPane();
        epasscontent.setLayout(new GridLayout(6,1));
        epassframe.setSize(800, 400);
        epassframe.setLocationRelativeTo(null);
        epassframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        epassframe.setVisible(true);
        titleLabel = new JLabel("Settings: Edit Password for User: " + this.name);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        epasscontent.add(titleLabel);

        JPanel currentinfo = new JPanel();
        currentstuff = new JLabel("Changing Current Password...");
        currentstuff.setFont(new Font("Tahoma", Font.BOLD, 14));
        currentstuff.setVerticalAlignment(SwingConstants.CENTER);
        currentstuff.setHorizontalAlignment(SwingConstants.CENTER);
        currentinfo.add(currentstuff);
        epassframe.add(currentinfo, BorderLayout.CENTER);

        JPanel npasspanel = new JPanel();
        npasspanel.add(new JLabel("Enter New Password:"));
        npassword = new JPasswordField(10);
        npasspanel.add(npassword);

        JPanel cpasspanel = new JPanel();
        cpasspanel.add(new JLabel("Reenter New Password:"));
        cpassword = new JPasswordField(10);
        cpasspanel.add(cpassword);

        JPanel opasspanel = new JPanel();
        opasspanel.add(new JLabel("Confirm with Old Password:"));
        oldpassword = new JPasswordField(10);
        opasspanel.add(oldpassword);

        JPanel buttons = new JPanel();
        enterButton = new JButton("Enter");
        buttons.add(enterButton);
        epasscontent.add(npasspanel, BorderLayout.CENTER);
        epasscontent.add(cpasspanel, BorderLayout.CENTER);
        epasscontent.add(opasspanel, BorderLayout.CENTER);
        epasscontent.add(buttons, BorderLayout.CENTER);
        enterButton.addActionListener(actionListener);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == enterButton){
                char[] newpassArray = npassword.getPassword();
                String newpass = "";
                for(int i = 0; i < newpassArray.length;i++){
                    newpass += newpassArray[i];
                }
                char[] cpassArray = cpassword.getPassword();
                String cnewpass = "";
                for(int i = 0; i < cpassArray.length;i++){
                    cnewpass += cpassArray[i];
                }
                char[] oldpassarray = oldpassword.getPassword();
                String oldpass = "";
                for(int i = 0; i < oldpassarray.length;i++){
                    oldpass += oldpassarray[i];
                }
                boolean canchange = false;
                try {
                    canchange = changeStuff(newpass, cnewpass, oldpass);
                } catch (IOException ex) {
                }
                if(isSeller){
                    if(canchange){
                        seller.setPassword(newpass);
                        password = newpass;
                        epassframe.dispose();
                    }
                }else {
                    if(canchange){
                        customer.setPassword(newpass);
                        password = newpass;
                        epassframe.dispose();
                    }
                }
            }
        }
    };

    public boolean changeStuff(String newpass, String cnewpass, String oldpass) throws IOException {
        if(oldpass.equals(this.password) && cnewpass.equals(newpass)) {
            if(isSeller){
                pw.println("updateAccount");
                pw.println(seller.getEmail() + "," + seller.getName() + "," + newpass);
                String s = "";
                if(bfr.ready()) {
                    s = bfr.readLine();
                    if (s.equals("success")) {
                        JOptionPane.showMessageDialog(null, "Succesfully Changed Password!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "An Error has Occurred!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
            } else{
                pw.println("updateAccount");
                pw.println(customer.getEmail() + "," + customer.getName() + "," + newpass);
                String s = "";
                if(bfr.ready()) {
                    s = bfr.readLine();
                    if (s.equals("success")) {
                        JOptionPane.showMessageDialog(null, "Succesfully Changed Password!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "An Error has Occurred!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
            }
            return false;
        } else{
            if(oldpass.equals(this.password)) {
                JOptionPane.showMessageDialog(null, "New Passwords Did Not Match!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Incorrect Password!", "Escape Room Scheduler: Settings", JOptionPane.INFORMATION_MESSAGE);
            }
            return false;
        }
    }

    public EditPassword(Seller s, Socket socket) throws IOException {
        this.name = s.getName();
        this.password = s.getPassword();
        this.email = s.getEmail();
        this.seller = s;
        this.isSeller = true;
        this.socket = socket;
        this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(socket.getOutputStream());
    }
    public EditPassword(Customer customer, Socket socket) throws IOException {
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
