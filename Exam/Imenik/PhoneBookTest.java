package Exam.Imenik;

import java.util.*;

class Contact implements Comparable<Contact>{
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
      return String.format("%s %s",name,number);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int compareTo(Contact o) {
        Comparator<Contact> comparator= Comparator.comparing(Contact::getName).
                thenComparing(Contact::getNumber);
        return comparator.compare(this,o);
    }
}
class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String number){
        super(String.format("Duplicate number: %s",number));
    }
}
class PhoneBook{
    private Set<Contact> allNumbers;
    private Map<String,Set<Contact>> contactsByNames;
    private Map<String,Set<Contact>> contactsBySubNumbers;

    public PhoneBook() {
        this.allNumbers=new HashSet<>();
        this.contactsByNames=new HashMap<>();
        this.contactsBySubNumbers=new HashMap<>();
    }
    public void addContact(String name,String number) throws DuplicateNumberException {
        Contact contact=new Contact(name,number);
        if(this.allNumbers.contains(contact)){
            throw new DuplicateNumberException(number);
        }
        this.allNumbers.add(contact);
        this.contactsByNames.putIfAbsent(name,new TreeSet<>());
        this.contactsByNames.get(name).add(contact);
        fillSubNumbers(contact) ;
    }
    public void contactsByNumber(String number){
        if(!this.contactsBySubNumbers.containsKey(number)){
            System.out.println("NOT FOUND");
        }else {
            this.contactsBySubNumbers.get(number)
                    .stream()
                    .forEach(System.out::println);
        }
    }
    public void contactsByName(String name){
        if(!this.contactsByNames.containsKey(name)){
            System.out.println("NOT FOUND");
        }else{
        this.contactsByNames.get(name)
                .stream()
                .forEach(System.out::println);}
    }

    private void fillSubNumbers(Contact contact) {

        for(int i=3;i<=contact.getNumber().length();i++){
            for(int j=0;j<=contact.getNumber().length()-i;j++){
                String subNumber=contact.getNumber().substring(j,j+i);
                this.contactsBySubNumbers.putIfAbsent(subNumber,new TreeSet<>());
                this.contactsBySubNumbers.get(subNumber).add(contact);
            }
        }
    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

