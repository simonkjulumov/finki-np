import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

class ChatRoom implements Comparable<ChatRoom>{
    private String name;
    private LinkedList<String> users;

    public ChatRoom(String name) {
        this.name = name;
        this.users = new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("\n");
        if(numUsers() == 0){
            sb.append("EMPTY");
            sb.append("\n");
        }
        else{
            Collections.sort(users);
            for(String user : users) {
                sb.append(user);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void addUser(String username) {
        if(!hasUser(username)){
            users.add(username);
        }
    }

    public void removeUser(String username) {
        if(hasUser(username)){
            users.remove(username);
        }
    }

    public boolean hasUser(String username) {
        return users.stream().filter(x -> x.equals(username)).findFirst().orElse(null) != null;
    }

    public int numUsers() {
        return users.size();
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ChatRoom chatRoom) {
        return name.compareTo(chatRoom.getName());
    }
}

class NoSuchRoomException extends Exception {
    private String roomName;

    public NoSuchRoomException(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String getMessage() {
        return this.roomName;
    }
}

class NoSuchUserException extends Exception {
    private String userName;

    public NoSuchUserException(String userName) {
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        return this.userName;
    }
}

class ChatSystem {
    private TreeMap<String, ChatRoom> chatRooms;
    private List<String> registeredUsers;

    public ChatSystem() {
        chatRooms = new TreeMap<>();
        registeredUsers = new ArrayList<>();
    }

    public void addRoom(String roomName) {
        chatRooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        chatRooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        ChatRoom room = chatRooms.getOrDefault(roomName, null);
        if(room == null){
            throw new NoSuchRoomException(roomName);
        }
        return room;
    }

    public void register(String userName) {
        registeredUsers.add(userName);
        List<ChatRoom> roomsWithLeastUsers = chatRooms.values().stream().collect(Collectors.toList());
        Collections.sort(roomsWithLeastUsers, (Comparator.comparingInt(x -> x.numUsers())));

        if(roomsWithLeastUsers.size() == 1){
            roomsWithLeastUsers.get(0).addUser(userName);
        }
        else if (roomsWithLeastUsers.size() > 1){
            int minNumberOfUsers = roomsWithLeastUsers.get(0).numUsers();
            roomsWithLeastUsers = roomsWithLeastUsers.stream().filter(x -> x.numUsers() == minNumberOfUsers).collect(Collectors.toList());
            if(roomsWithLeastUsers.size() > 1){
                Collections.sort(roomsWithLeastUsers);
            }
            roomsWithLeastUsers.get(0).addUser(userName);
        }
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        registeredUsers.add(userName);
        joinRoom(userName, roomName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        ChatRoom room = getRoom(roomName);
        String user = getUser(userName);
        //chatRooms.get(roomName).addUser(userName);
        room.addUser(user);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        String user = getUser(username);
        ChatRoom chatRoom = getRoom(roomName);
        chatRoom.removeUser(user);
    }

    public void followFriend(String userName, String friendUserName) throws NoSuchUserException, NoSuchRoomException {
        String user = getUser(userName);
        String friend = getUser(friendUserName);

        List<ChatRoom> chatRoomsWhereFriendIs = chatRooms.values().stream().filter(x -> x.hasUser(friend)).collect(Collectors.toList());
        for(ChatRoom chatRoom : chatRoomsWhereFriendIs) {
            joinRoom(user, chatRoom.getName());
        }
    }

    private String getUser(String userName) throws NoSuchUserException {
        String user = registeredUsers.stream().filter(x -> x.equals(userName)).findFirst().orElse(null);
        if(user == null) {
            throw new NoSuchUserException(userName);
        }
        return user;
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}
