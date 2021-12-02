package Exam.StopCorona;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}
class User{
    private String name;
    private String id;
    private List<ILocation> locations;
    public User(String name,String id){
        this.name=name;
        this.id=id;
        this.locations=new ArrayList<>();
    }
    public void addLocation(ILocation location){
        this.locations.add(location);
    }
    public void setILocations(List<ILocation> locations){
        this.locations=locations;
    }

}
class UserIdAlreadyExistException extends Exception{
    public UserIdAlreadyExistException(String id)
    {
        super(String.format("User with id %s already exist",id));
    }
}
class StopCoronaApp{
    private List<User> users;
    private Map<String,User> usersById;
    public StopCoronaApp(){
        this.users=new ArrayList<>();
        this.usersById=new HashMap<>();
    }

    public void addUser(String name,String id) throws UserIdAlreadyExistException {
        if(this.usersById.containsKey(id)){
            throw new UserIdAlreadyExistException(id);
        }
        this.usersById.putIfAbsent(id,new User(name,id));
    }
    public void addLocations(String id,List<ILocation> iLocations){
        this.usersById.get(id).setILocations(iLocations);
    }

}
public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
