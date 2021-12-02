package Lab07.AdministrationChatSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class ChatRoom {
    private String roomName;
    Set<String> users;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        users = new TreeSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomName + "\n");
        if (users.size() > 0) {
            users.forEach(user -> sb.append(user + "\n"));
        } else {
            sb.append("EMPTY" + "\n");
        }
        return sb.toString();
    }
}

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super(String.format("Room with name %s does not exist", roomName));
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String userName) {
        super(String.format("Username %s does not exist", userName));
    }
}

class ChatSystem {
    private Map<String, ChatRoom> chatRooms;
    private Set<String> users;

    public ChatSystem() {
        chatRooms = new TreeMap<>();
        users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        chatRooms.putIfAbsent(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        chatRooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        return chatRooms.get(roomName);
    }

    public void register(String userName) {
        users.add(userName);
        Optional<ChatRoom> chatRoom = chatRooms.entrySet().stream().map(t -> t.getValue()).min(Comparator.comparing(ChatRoom::numUsers));
        if (chatRoom.isPresent()) {
            chatRooms.get(chatRoom.get().getRoomName()).users.add(userName);
        }
    }

    public void registerAndJoin(String userName, String roomName) {
        users.add(userName);
        chatRooms.get(roomName).users.add(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        chatRooms.get(roomName).users.add(userName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        chatRooms.get(roomName).removeUser(userName);
    }

    public void followFriend(String username, String friendUsername) throws NoSuchUserException {
        if (!users.contains(friendUsername)) {
            throw new NoSuchUserException(friendUsername);
        }
        for (Map.Entry<String, ChatRoom> entry : chatRooms.entrySet()) {
            if (entry.getValue().hasUser(friendUsername)) {
                entry.getValue().addUser(username);
            }
        }

    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}
