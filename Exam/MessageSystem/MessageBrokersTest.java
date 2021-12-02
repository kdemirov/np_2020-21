package Exam.MessageSystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PartitionAssigner {
    public static Integer assignPartition (Message message, int partitionsCount) {
        return (Math.abs(message.key.hashCode())  % partitionsCount) + 1;
    }
}
class Message implements Comparable<Message>{
    private LocalDateTime timestamp;
    private String content;
    private Integer partition;
    public String key;

    public Message(LocalDateTime timestamp, String content, Integer partition, String key) {
        this.timestamp = timestamp;
        this.content = content;
        this.partition = partition;
        this.key = key;
    }

    public Message(LocalDateTime timestamp, String content, String key) {
        this.timestamp = timestamp;
        this.content = content;
        this.key = key;
        this.partition=null;
    }

    public Integer getPartition() {
        return partition;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", message='" + content + '\'' +
                '}';
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Message o) {
     return this.timestamp.compareTo(o.getTimestamp());
    }
}
class PartitionDoesNotExistException extends Exception{
    public PartitionDoesNotExistException(String topicName,Integer partition){
        super(String.format("The topic %s does not have a partition with number %d",topicName,partition));
    }
}
class UnsupportedOperationException extends Exception{
    public UnsupportedOperationException(String message){
        super(message);
    }
}
class Topic{
    private String topicName;
    private int partitionsCount;
    private Map<Integer,TreeSet<Message>> messagesByPartitions;

    public Topic(String topicName,int partitionsCount){
        this.topicName=topicName;
        this.partitionsCount=partitionsCount;
        this.messagesByPartitions=new TreeMap<>();
        IntStream.rangeClosed(1,partitionsCount).forEach(i->this.messagesByPartitions.putIfAbsent(i,new TreeSet<>()));
    }
    public void addMessage(Message message) throws PartitionDoesNotExistException, UnsupportedOperationException {

        Integer partition=message.getPartition();
        if(partition==null){
            partition=PartitionAssigner.assignPartition(message,this.partitionsCount);
        }
        if(!this.messagesByPartitions.containsKey(partition)){
            throw new PartitionDoesNotExistException(this.topicName,partition);

        }
        this.messagesByPartitions.computeIfPresent(partition,(k,v)->{
            if(v.size()==MessageBroker.capacitityPerTopic){
                v.remove(v.first());
            }
            v.add(message);
            return v;
        });



    }
    public void changeNumberOfPartitions(int newPartitionsNumber) throws UnsupportedOperationException {
        if(newPartitionsNumber<partitionsCount){
            throw new UnsupportedOperationException("Partitions number cannot be decreased!");
        }
        int size=this.messagesByPartitions.size();
        int difference=newPartitionsNumber-this.partitionsCount;
        IntStream.rangeClosed(1,difference)
                .forEach(i->this.messagesByPartitions.putIfAbsent(size+i,new TreeSet<>()));
        this.partitionsCount=newPartitionsNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(String.format("Topic: %10s Partitions: %5d\n",topicName
        ,this.partitionsCount));
        this.messagesByPartitions.entrySet()
                .stream()
                .forEach(e->sb.append(String.format("%2d : Count of messages: %5d\n%s\n",
                        e.getKey(),e.getValue().size(),
                        String.format("Messages:\n%s",
                                e.getValue()
                        .stream()
                        .map(m->m.toString())
                        .collect(Collectors.joining("\n")))
                )));
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(topicName, topic.topicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName);
    }

    public String getTopicName() {
        return topicName;
    }
}
class MessageBroker{
    public static LocalDateTime minimumDate;
    public static Integer capacitityPerTopic;
    private Map<String,Topic> topicMap;

    public MessageBroker(LocalDateTime minimumDate,Integer capacitityPerTopic){
        MessageBroker.minimumDate=minimumDate;
        MessageBroker.capacitityPerTopic=capacitityPerTopic;
        this.topicMap=new TreeMap<>();
    }
    public void addTopic(String topic,int partitionCount){
        this.topicMap.put(topic,new Topic(topic,partitionCount));

    }
    public void addMessage(String topic,Message message) throws PartitionDoesNotExistException, UnsupportedOperationException {
       if(message.getTimestamp().isBefore(minimumDate)) return;
       this.topicMap.get(topic).addMessage(message);
    }
    public void changeTopicSettings(String topic,int capacitityPerTopic) throws UnsupportedOperationException {
        this.topicMap.get(topic).changeNumberOfPartitions(capacitityPerTopic);
    }

    @Override
    public String toString() {

        return String.format("Broker with  %d topics:\n%s",
        this.topicMap.size(),
        this.topicMap.values()
        .stream()
        .map(Topic::toString)
        .collect(Collectors.joining("\n")));
    }
}
public class MessageBrokersTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String date = sc.nextLine();
        LocalDateTime localDateTime =LocalDateTime.parse(date);
        Integer partitionsLimit = Integer.parseInt(sc.nextLine());
        MessageBroker broker = new MessageBroker(localDateTime, partitionsLimit);
        int topicsCount = Integer.parseInt(sc.nextLine());

        //Adding topics
        for (int i=0;i<topicsCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            int partitionsCount = Integer.parseInt(parts[1]);
            broker.addTopic(topicName, partitionsCount);
        }

        //Reading messages
        int messagesCount = Integer.parseInt(sc.nextLine());

        System.out.println("===ADDING MESSAGES TO TOPICS===");
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER ADDITION OF MESSAGES===");
        System.out.println(broker);

        System.out.println("===CHANGE OF TOPICS CONFIGURATION===");
        //topics changes
        int changesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<changesCount;i++){
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            Integer partitions = Integer.parseInt(parts[1]);
            try {
                broker.changeTopicSettings(topicName, partitions);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("===ADDING NEW MESSAGES TO TOPICS===");
        messagesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER CONFIGURATION CHANGE===");
        System.out.println(broker);


    }
}

