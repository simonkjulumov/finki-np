import java.time.LocalDateTime;
import java.util.*;

class PartitionAssigner {
    public static Integer assignPartition (Message message, int partitionsCount) {
        return (Math.abs(message.getKey().hashCode())  % partitionsCount) + 1;
    }
}

class Message implements Comparable<Message> {
    private LocalDateTime date;
    private String content;
    private Integer partition;
    private String key;

    public Message(LocalDateTime date, String content, String key) {
        this.date = date;
        this.content = content;
        this.partition = null;
        this.key = key;
    }

    public Message(LocalDateTime date, String content, Integer partition, String key) {
        this.date = date;
        this.content = content;
        this.partition = partition;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Message{timestamp=%s, message='%s'}", date.toString(), content);
    }

    @Override
    public int compareTo(Message message) {
        return this.getDate().compareTo(message.getDate());
    }
}

class Topic {
    private String topicName;
    private int partitionsCount;
    private Map<Integer, ArrayList<Message>> partitionAndMessages;
    private int messageCapacity;
    private LocalDateTime minimumDate;

    public Topic(String topicName, int partitionsCount, int messageCapacity, LocalDateTime minimumDate) {
        this.topicName = topicName;
        this.partitionsCount = partitionsCount;
        this.messageCapacity = messageCapacity;
        this.minimumDate = minimumDate;

        partitionAndMessages = new HashMap<Integer, ArrayList<Message>>();
        for(int i = 1; i <= partitionsCount; i++) {
            partitionAndMessages.put(i, new ArrayList<Message>());
        }

    }

    public void addMessage(Message message) throws PartitionDoesNotExistException {
        if(message.getPartition() == null) {
            message.setPartition(PartitionAssigner.assignPartition(message, partitionsCount));
        }

        ArrayList<Message> msgs = partitionAndMessages.getOrDefault(message.getPartition(), null);
        if(msgs == null){
            throw new PartitionDoesNotExistException(topicName, message.getPartition().toString());
        }
        Collections.sort(msgs);
        if(messageCapacity == msgs.size()) {
            msgs.remove(0);
        }
        msgs.add(message);
    }

    void changeNumberOfPartitions (int newPartitionsNumber) throws UnsupportedOperationException {
        if(newPartitionsNumber < partitionsCount) {
            throw new UnsupportedOperationException();
        }

        for(int i = partitionsCount + 1; i <= newPartitionsNumber; i++) {
            partitionAndMessages.put(i, new ArrayList<Message>());
        }

        partitionsCount = newPartitionsNumber;
    }

    @Override
    public String toString() {
        String info = String.format("Topic: %10s Partitions: %5d\n", topicName, partitionsCount);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(info);
        for(Map.Entry<Integer, ArrayList<Message>> pm : partitionAndMessages.entrySet()) {
            ArrayList<Message> messages = pm.getValue();
            Collections.sort(messages);
            messages.removeIf(x -> x.getDate().isBefore(minimumDate));
            stringBuilder.append(String.format(" %s : Count of messages:      %s\n", pm.getKey().toString(), String.valueOf(messages.size())));
            stringBuilder.append("Messages:\n");
            for(Message message : pm.getValue()) {
                stringBuilder.append(message.toString() + "\n");
            }
        }
        return stringBuilder.toString();
    }
        }

class PartitionDoesNotExistException extends Exception {
    private String topicName;
    private String partitionNumber;

    public PartitionDoesNotExistException(String topicName, String partitionNumber) {
        this.topicName = topicName;
        this.partitionNumber = partitionNumber;
    }

    @Override
    public String getMessage() {
        return String.format("The topic %s does not have a partition with number %s", topicName, partitionNumber);
    }
}

class MessageBroker {
    private LocalDateTime minimumDate;
    private Integer capacityPerTopic;
    private TreeMap<String, Topic> topics;

    public MessageBroker(LocalDateTime minimumDate, Integer capacityPerTopic) {
        this.minimumDate = minimumDate;
        this.capacityPerTopic = capacityPerTopic;
        topics = new TreeMap<String, Topic>();
    }

    void addTopic (String topic, int partitionsCount) {
        Topic t = new Topic(topic, partitionsCount, capacityPerTopic, minimumDate);
        topics.put(topic, t);
    }

    void addMessage (String topic, Message message) throws PartitionDoesNotExistException {
        topics.get(topic).addMessage(message);
    }

    void changeTopicSettings (String topic, int partitionsCount) {
        topics.get(topic).changeNumberOfPartitions(partitionsCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Broker with  %d topics:\n", topics.size()));
        for(Topic topic : topics.values()){
            sb.append(topic.toString());
        }
        return sb.toString();
    }
}

public class MessageBrokersTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String date = sc.nextLine();
        LocalDateTime localDateTime = LocalDateTime.parse(date);
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
