package P1;
import java.util.Scanner;
import java.io.FileWriter; 
import java.io.IOException; 
class Node {
    String name;
    Node next;

    public Node(String n) {
        name = n;
    }
}
class Ticket {
    int TSeats, CC = 0;
    int seatNumbers[];
    Node head = null, CNode, DelNode;
    String[] waitingList = new String[3];
    int front = -1, rear = -1;
    String[] cancelTicket = new String[3];
    int top = -1;

    public Ticket(int S) {
        TSeats = S;
        seatNumbers = new int[S];
        for (int i = 0; i < S; i++) {
            seatNumbers[i] = i + 1;
        }
    }
    public void updateFile() {
        try (FileWriter writer = new FileWriter("BusTickets.txt")) {
            writer.write("--- BUS TICKET RECORDS (Bus No: 101) ---\n");
            Node temp = head;
            int i = 0;
            while (temp != null) {
                
                writer.write("Bus: 101 | Passenger: " + temp.name + " | Seat: " + seatNumbers[i] + "\n");
                temp = temp.next;
                i++;
            }
            System.out.println("File updated successfully.");
        } catch (IOException e) {
            System.out.println("Error while writing to file: " + e.getMessage());
        }
    }

    public void addPassenger(String name) {
        Node NewNode = new Node(name);
        if (head == null) {
            head = NewNode;
        } else {
            CNode = head;
            while (CNode.next != null) {
                CNode = CNode.next;
            }
            CNode.next = NewNode;
        }
        CC++;
    }

    public void removePassenger(String name) {
        if (head == null) {
            System.out.println("Not found");
        }
        if (head.name.equals(name)) {
            DelNode = head;
            head = head.next;
            CC--;
        } else {
            CNode = head;
            while (CNode.next != null) {
                if (CNode.next.name.equals(name)) {
                    DelNode = CNode.next;
                    CNode.next = CNode.next.next;
                    CC--;
                    break;
                }
                CNode = CNode.next;
            }
        }
        if (DelNode != null) {
            DelNode.next = null;
            DelNode = null;
        }

    }

    void enqueue(String name) {
        if (front == -1) {
            front = 0;
        }
        waitingList[++rear] = name;
    }

    String dequeue() {
        if (front == -1 || front > rear) {
            return null;
        }
        String name = waitingList[front];
        front++;
        return name;
    }

    void push(String name) {
        if (top == cancelTicket.length - 1) {
            System.out.println("Cancellation stack full");
            return;
        }

        cancelTicket[++top] = name;
    }

    String pop() {
        if (top == -1) {
            return null;
        }
        String name = cancelTicket[top];
        top--;
        return name;
    }

    public void BookTicket(String name) {
        if (CC < TSeats) {
            addPassenger(name);
            System.out.println("Ticket booked for " + name +
                    " Seat No: " + seatNumbers[CC - 1]);
            updateFile(); 
        } else {
            enqueue(name);
            System.out.println("Seats full. Added to waiting list: " + name);
        }
    }

    public void cancelticket(String name) {
        Node CNode = head;
        boolean found = false;
        while (CNode != null) {
            if (CNode.name.equals(name)) {
                found = true;
                break;
            }
            CNode = CNode.next;
        }
        if (!found) {
            System.out.println("Passenger not found");

        } else {
            removePassenger(name);
            push(name);
            System.out.println("Ticket cancelled for " + name);
            String next = dequeue();
            if (next != null) {
                addPassenger(next);
                System.out.println(next + " moved from waiting list to confirmed");
            }
            updateFile(); 
        }
    }

    public void confirmedtickets() {
        if (head == null) {
            System.out.println("No confirmed tickets");

        } else {
            CNode = head;
            int i = 0;
            while (CNode != null) {
                System.out.println("Seat " + seatNumbers[i] + " - " + CNode.name);
                CNode = CNode.next;
                i++;
            }
        }
    }

    public void waitlist() {
        if (front == -1 || front > rear) {
            System.out.println("Waiting list empty");

        } else {
            for (int i = front; i <= rear; i++) {

                System.out.println(waitingList[i]);
            }
        }
    }

    public void undocancellation() {
        String name = pop();
        if (name == null) {
            System.out.println("No cancellation to undo");

        } else if (CC < TSeats) {
            addPassenger(name);
            System.out.println("Undo successful. Ticket restored for " + name);
            updateFile(); 
        } else {
            enqueue(name);
            System.out.println("Seats full. Passenger moved to waiting list");
        }
    }

    public void searchPassenger(String name) {
        Node CNode = head;
        int i = 0;
        while (CNode != null) {
            if (CNode.name.equals(name)) {
                System.out.println("Passenger found at Seat No: " +
                        seatNumbers[i]);
                return;
            }
            CNode = CNode.next;
            i++;
        }
        System.out.println("Passenger not found");
    }

    public void sortPassengers() {
        if (head == null) {
            System.out.println("list is empty");
        } else {
            Node CNode = head, j;
            while (CNode != null) {
                j = CNode.next;
                while (j != null) {
                    if (CNode.name.compareTo(j.name) > 0) {
                        String temp = CNode.name;
                        CNode.name = j.name;
                        j.name = temp;
                    }
                    j = j.next;
                }
                CNode = CNode.next;
            }
            System.out.println("Passengers sorted alphabetically");
            updateFile(); 
        }
    }
}

public class Ticketsystem {

    public static void main(String[] args) {
        Ticket T = new Ticket(3);
        Scanner SC = new Scanner(System.in);

        char choice;
        do {
            System.out.println("----BUS BOOKING SYSTEM-----");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Confirmed Tickets");
            System.out.println("4. View  Waiting List Tickets");
            System.out.println("5. Undo Cancellation");
            System.out.println("6.Search Passenger ");
            System.out.println("7. Sort Passengers");
            System.out.println("8. Exit");
            System.out.println("Enter your option: ");

            
            try {
                int opt = Integer.parseInt(SC.next());
                switch (opt) {
                    case 1:
                        System.out.println("Enter passenger name");
                        String N = SC.next();
                        T.BookTicket(N);
                        break;
                    case 2:
                        System.out.println("enter Passenger name to cancel");
                        String pname = SC.next();
                        T.cancelticket(pname);
                        break;
                    case 3:
                        T.confirmedtickets();
                        break;
                    case 4:
                        T.waitlist();
                        break;
                    case 5:
                        T.undocancellation();
                        break;
                    case 6:
                        System.out.print("Enter passenger name to search: ");
                        String sname = SC.next();
                        T.searchPassenger(sname);
                        break;
                    case 7:
                        T.sortPassengers();
                        T.confirmedtickets();
                        break;
                    case 8:
                        System.out.println("Exit");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric option.");
            }

            System.out.println("enter y/Y to continue");
            choice = SC.next().charAt(0);
        } while (choice == 'y' || choice == 'Y');
        SC.close();
    }
}