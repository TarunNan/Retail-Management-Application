import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class SignUp extends JComponent implements Runnable {
    private JButton signupbutton;
    private JTextField nameField;
    private JTextField userField;
    private JPasswordField passField;
    private JFrame signupstuff;
    private String[] userValues;
    private JLabel titleLabel;
    private boolean finished;
    private JComboBox<String> roleBox;

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(new SignUp());
    }

    @Override
    public void run() {
        signupstuff = new JFrame("Escape Room Scheduler Application");
        Container logincontent = signupstuff.getContentPane();
        logincontent.setLayout(new GridLayout(6, 1));
        signupstuff.setSize(400, 400);
        signupstuff.setLocationRelativeTo(null);
        signupstuff.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signupstuff.setVisible(true);
        titleLabel = new JLabel("Sign Up Page");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logincontent.add(titleLabel);
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Enter Name:"));
        nameField = new JTextField(10);
        namePanel.add(nameField);
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Enter Username (Email):"));
        userField = new JTextField(10);
        usernamePanel.add(userField);
        JPanel rolePanel = new JPanel();
        rolePanel.add(new JLabel("Enter Role:"));
        String[] choices = { "Seller", "Customer" };
        roleBox = new JComboBox<String>(choices);
        rolePanel.add(roleBox);
        JPanel passPanel = new JPanel();
        passPanel.add(new JLabel("Enter Password:"));
        passField = new JPasswordField(10);
        passPanel.add(passField);
        JPanel enterButtonPanel = new JPanel();
        signupbutton = new JButton("Enter");
        enterButtonPanel.add(signupbutton);
        userField.addActionListener(actionListener);
        passField.addActionListener(actionListener);
        signupbutton.addActionListener(actionListener);
        logincontent.add(namePanel, BorderLayout.CENTER);
        logincontent.add(usernamePanel, BorderLayout.CENTER);
        logincontent.add(rolePanel, BorderLayout.CENTER);
        logincontent.add(passPanel, BorderLayout.CENTER);
        logincontent.add(enterButtonPanel, BorderLayout.CENTER);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signupbutton) {
                checkUser(userField.getText());
                if (finished) {
                    userValues = new String[4];
                    userValues[0] = userField.getText();
                    char[] passArray = passField.getPassword();
                    String password = "";
                    for (int i = 0; i < passArray.length; i++) {
                        password += passArray[i];
                    }
                    userValues[1] = password;
                    userValues[2] = (String) roleBox.getSelectedItem();
                    userValues[3] = nameField.getText();
                    signupstuff.dispose();
                }
            }
        }
    };

    public String[] getUserValues() {
        return userValues;
    }

    public void checkUser(String email) {
        boolean exists = false;
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
        if (exists) {
            JOptionPane.showMessageDialog(null, "An account with that email has already been created." +
                    " Please try again.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Succesfully Signed Up!", "Sign Up Window",
                    JOptionPane.INFORMATION_MESSAGE);
            finished = true;
        }
    }

    public SignUp() {
        userValues = null;
        finished = false;
    }

}
