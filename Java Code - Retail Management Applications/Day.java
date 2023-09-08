import java.util.ArrayList;

/**
 * A java class that allows the program to create Day objects that each have
 * their own
 * available windows for customers to schedule appointments.
 *
 * Purdue University -- CS18000 -- Spring 2023 -- Project 04
 *
 * @author Aaron Parihar & Raelee Lance
 * @version April 9, 2023
 */
public class Day {
    private int number;
    private ArrayList<Appointment> appointments;
    private ArrayList<AppointmentWindow> appointmentWindows;

    public Day(int number, ArrayList<Appointment> appointments, ArrayList<AppointmentWindow> appointmentWindows) {
        this.number = number;
        this.appointments = new ArrayList<Appointment>();
        if (appointments != null) {
            for (int i = 0; i < appointments.size(); i++) {
                System.out.println(appointments.get(i));
            }
        }
        this.appointmentWindows = appointmentWindows;
    }

    public String toString() {
        String format = "Day: %d, Appointments: %s, Appointment Windows: %s";
        String appointment = "";
        for (int i = 0; i < appointments.size(); i++) {
            appointment += appointments.get(i).toString();
            System.out.println(appointment);
        }
        String appointmentWindow = "";
        for (int i = 0; i < appointmentWindows.size(); i++) {
            appointmentWindow += appointmentWindows.get(i).toString();
        }
        return String.format(format, number, appointment, appointmentWindow);
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<AppointmentWindow> getAppointmentWindows() {
        return appointmentWindows;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setAppointmentWindows(ArrayList<AppointmentWindow> appointmentWindows) {
        this.appointmentWindows = appointmentWindows;
    }
}
