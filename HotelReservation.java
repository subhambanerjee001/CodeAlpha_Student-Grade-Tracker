import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class HotelReservation {

    static class Room {
        int number;
        String category;
        double price;
        int capacity;
        boolean isBooked;

        public Room(int number, String category, double price, int capacity) {
            this.number = number;
            this.category = category;
            this.price = price;
            this.capacity = capacity;
            this.isBooked = false;
        }

        public String toString() {
            String type = (capacity == 1) ? "Single Bed" : "Double Bed";
            return "Room " + number + " (" + category + ", " + type + ") - ₹" + price + " - " + (isBooked ? "Booked" : "Available");
        }
    }

    
    static class Guest {
        String name;
        String phone;

        public Guest(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

    
    static class Booking {
        Room room;
        Guest guest;
        int numberOfGuests;

        public Booking(Room room, Guest guest, int numberOfGuests) {
            this.room = room;
            this.guest = guest;
            this.numberOfGuests = numberOfGuests;
            this.room.isBooked = true;
        }

        public String toString() {
            return "Room " + room.number + " booked by " + guest.name + " (" + guest.phone + ") - Guests: " + numberOfGuests;
        }
    }

   
    static class Hotel {
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File("bookings.txt");

        public Hotel() {
            rooms.add(new Room(101, "Standard", 1500, 1));
            rooms.add(new Room(114, "Standard", 2100, 2));
            rooms.add(new Room(121, "Standard", 1500, 1));
            rooms.add(new Room(102, "Deluxe", 2500, 2));
            rooms.add(new Room(140, "Deluxe", 2500, 2));
            rooms.add(new Room(701, "Suite", 4000, 2));
            rooms.add(new Room(706, "Suite", 4000, 2));
            rooms.add(new Room(104, "Standard", 1500, 1));
            rooms.add(new Room(105, "Deluxe", 2500, 2));
            loadBookingsFromFile();
        }

        public ArrayList<Room> searchRooms(String category) {
            ArrayList<Room> result = new ArrayList<>();
            for (Room r : rooms) {
                if (r.category.equalsIgnoreCase(category) && !r.isBooked) {
                    result.add(r);
                }
            }
            return result;
        }

        public Booking bookRoom(int roomNumber, String name, String phone, int numberOfGuests) {
            for (Room r : rooms) {
                if (r.number == roomNumber && !r.isBooked) {
                    if (numberOfGuests > r.capacity) {
                        JOptionPane.showMessageDialog(null, "❌ Room capacity is only " + r.capacity + " guest(s).", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    Guest g = new Guest(name, phone);
                    Booking b = new Booking(r, g, numberOfGuests);
                    bookings.add(b);
                    saveAllBookingsToFile();
                    return b;
                }
            }
            return null;
        }

        public boolean cancelBooking(int roomNumber, String name) {
            Iterator<Booking> it = bookings.iterator();
            while (it.hasNext()) {
                Booking b = it.next();
                if (b.room.number == roomNumber && b.guest.name.equalsIgnoreCase(name)) {
                    b.room.isBooked = false;
                    it.remove();
                    saveAllBookingsToFile();
                    return true;
                }
            }
            return false;
        }

        public void resetBookings() {
            for (Room r : rooms) {
                r.isBooked = false;
            }
            bookings.clear();
            file.delete();
        }

        public ArrayList<Booking> getAllBookings() {
            return bookings;
        }

        private void saveAllBookingsToFile() {
            try (FileWriter fw = new FileWriter(file)) {
                for (Booking b : bookings) {
                    fw.write(b.room.number + "," + b.room.category + "," + b.room.price + "," + b.room.capacity + "," + b.guest.name + "," + b.guest.phone + "," + b.numberOfGuests + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadBookingsFromFile() {
            if (!file.exists()) return;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 7) continue;
                    int roomNum = Integer.parseInt(parts[0]);
                    String cat = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int capacity = Integer.parseInt(parts[3]);
                    String name = parts[4];
                    String phone = parts[5];
                    int numberOfGuests = Integer.parseInt(parts[6]);

                    Room room = getRoomByNumber(roomNum);
                    if (room != null && !room.isBooked) {
                        room.capacity = capacity;
                        Guest guest = new Guest(name, phone);
                        Booking booking = new Booking(room, guest, numberOfGuests);
                        bookings.add(booking);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        private Room getRoomByNumber(int number) {
            for (Room r : rooms) {
                if (r.number == number) return r;
            }
            return null;
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservation().createGUI());
    }

    Hotel hotel = new Hotel();

    public void createGUI() {
        JFrame frame = new JFrame("🏨 Hotel Reservation System");
        frame.setSize(750, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);
        frame.add(scroll, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField roomNumField = new JTextField();
        JTextField guestCountField = new JTextField();
        JTextField cancelNameField = new JTextField();
        JTextField cancelRoomField = new JTextField();

        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});

        JButton searchBtn = new JButton("🔍 Search Rooms");
        JButton bookBtn = new JButton("✅ Book Room");
        JButton cancelBtn = new JButton("❌ Cancel Booking");
        JButton viewBtn = new JButton("📄 View Bookings");
        JButton resetBtn = new JButton("🔄 Reset Bookings");

        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(searchBtn);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomNumField);
        panel.add(new JLabel("No. of Guests:"));
        panel.add(guestCountField);
        panel.add(bookBtn);

        panel.add(new JLabel("Cancel Name:"));
        panel.add(cancelNameField);
        panel.add(new JLabel("Cancel Room #:"));
        panel.add(cancelRoomField);
        panel.add(cancelBtn);

        panel.add(viewBtn);
        panel.add(resetBtn);

        frame.add(panel, BorderLayout.NORTH);

        searchBtn.addActionListener(e -> {
            String cat = (String) categoryBox.getSelectedItem();
            ArrayList<Room> result = hotel.searchRooms(cat);
            output.setText("Available Rooms:\n");
            for (Room r : result) output.append(r + "\n");
        });

        bookBtn.addActionListener(e -> {
            try {
                int roomNum = Integer.parseInt(roomNumField.getText());
                String name = nameField.getText();
                String phone = phoneField.getText();
                int guests = Integer.parseInt(guestCountField.getText());
                Booking b = hotel.bookRoom(roomNum, name, phone, guests);
                if (b != null) output.setText("✅ Booking Successful!\n" + b);
                else output.setText("❌ Booking Failed.");
            } catch (Exception ex) {
                output.setText("⚠️ Invalid input.");
            }
        });

        cancelBtn.addActionListener(e -> {
            try {
                int roomNum = Integer.parseInt(cancelRoomField.getText());
                String name = cancelNameField.getText();
                boolean cancelled = hotel.cancelBooking(roomNum, name);
                output.setText(cancelled ? "✅ Booking Cancelled!" : "❌ Cancellation Failed.");
            } catch (Exception ex) {
                output.setText("⚠️ Invalid cancellation details.");
            }
        });

        viewBtn.addActionListener(e -> {
            output.setText("📋 All Bookings:\n");
            for (Booking b : hotel.getAllBookings()) output.append(b + "\n");
        });

        resetBtn.addActionListener(e -> {
            hotel.resetBookings();
            output.setText("❌ All bookings cleared.");
        });

        frame.setVisible(true);
    }
}
