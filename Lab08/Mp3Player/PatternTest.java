package Lab08.Mp3Player;

import java.util.ArrayList;
import java.util.List;

interface State {
    void pressPlay();

    void pressStop();

    void pressFWD();

    void pressREW();
}

class PlaySong implements State {
    MP3Player mp3Player;
    int song;

    public PlaySong(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
        song = 0;
    }

    @Override
    public void pressPlay() {
        if (song != this.mp3Player.getCurrentSong()) {
            System.out.println(String.format("Song %d is playing", this.mp3Player.getCurrentSong()));
            mp3Player.setState(this.mp3Player.getStatePlay());
        } else {
            System.out.println(String.format("Song is already playing", this.mp3Player.getCurrentSong()));
            this.mp3Player.setState(this.mp3Player.getStatePlay());
        }
    }

    @Override
    public void pressStop() {
        System.out.println(String.format("Song %d is paused", this.mp3Player.getCurrentSong()));
        mp3Player.setState(this.mp3Player.getStatePause());
        this.mp3Player.resetPlaylist();
    }

    @Override
    public void pressFWD() {
        this.mp3Player.setCurrentSongFWD();
        this.mp3Player.setState(this.mp3Player.getStatePause());
    }

    @Override
    public void pressREW() {
        this.mp3Player.setCurrentSongREW();
        this.mp3Player.setState(this.mp3Player.getStatePause());
    }
}

class StopSong implements State {

    private MP3Player mp3Player;

    public StopSong(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing", this.mp3Player.getCurrentSong()));
        this.mp3Player.setState(this.mp3Player.getStatePlay());
    }

    @Override
    public void pressStop() {
        System.out.println(String.format("Songs are already stopped", this.mp3Player.getCurrentSong()));
        this.mp3Player.setState(this.mp3Player.getStateStop());
    }

    @Override
    public void pressFWD() {
        this.mp3Player.setCurrentSongFWD();
        this.mp3Player.setState(this.mp3Player.getStatePause());
    }

    @Override
    public void pressREW() {
        this.mp3Player.setCurrentSongREW();
        this.mp3Player.setState(this.mp3Player.getStatePause());
    }
}

class PausedSong implements State {

    private MP3Player mp3Player;

    public PausedSong(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }

    @Override
    public void pressPlay() {
        System.out.println(String.format("Song %d is playing", this.mp3Player.getCurrentSong()));
        this.mp3Player.setState(this.mp3Player.getStatePlay());
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        this.mp3Player.resetPlaylist();
        this.mp3Player.setState(this.mp3Player.getStateStop());

    }

    @Override
    public void pressFWD() {
        this.mp3Player.setCurrentSongFWD();
        this.mp3Player.setState(this.mp3Player.getStatePause());
    }

    @Override
    public void pressREW() {
        this.mp3Player.setCurrentSongREW();
        this.mp3Player.setState(this.mp3Player.getStatePause());

    }
}

class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}

class MP3Player {
    private List<Song> songs;
    private int currentSong;
    private State statePlay;
    private State stateStop;
    private State statePause;
    private State state;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentSong = 0;
        this.statePlay = new PlaySong(this);
        this.stateStop = new StopSong(this);
        this.statePause = new PausedSong(this);
        this.state = stateStop;
    }

    public void pressPlay() {
        state.pressPlay();
    }

    public void pressStop() {
        state.pressStop();
    }

    public void pressFWD() {
        System.out.println("Forward...");
        state.pressFWD();
    }

    public void pressREW() {
        System.out.println("Reward...");
        state.pressREW();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void printCurrentSong() {
        System.out.println(songs.get(currentSong));
    }

    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSongFWD() {
        if (currentSong == songs.size() - 1) {
            currentSong = 0;
        } else {
            ++currentSong;
        }
    }

    public void setCurrentSongREW() {
        if (currentSong == 0) {
            currentSong = songs.size() - 1;
        } else {
            --currentSong;
        }
    }

    public void resetPlaylist() {
        this.currentSong = 0;
    }

    public State getStatePlay() {
        return statePlay;
    }

    public State getStateStop() {
        return stateStop;
    }

    public State getStatePause() {
        return statePause;
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentSong +
                "songList = " + songs +
                '}';
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde