package Exam.Prevodi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InnerTime {
    private int hours;
    private int minutes;
    private int seconds;
    private int miliseconds;

    public InnerTime(int hours, int minutes, int seconds, int miliseconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.miliseconds = miliseconds;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, miliseconds);
    }

    public void shift(int mls) {
        if (mls < 0) {
            shiftLeft(mls);

        } else {
            shiftRight(mls);
        }
    }

    private void shiftLeft(int mls) {
       this.miliseconds = this.miliseconds+mls;
        if (this.miliseconds < 0) {
            this.seconds = this.seconds - 1;
            this.miliseconds = 1000 + this.miliseconds;
        }
        if (this.seconds < 0) {
            this.minutes = this.minutes - 1;
            this.seconds = 60 + this.seconds;
        }
        if (this.minutes < 0) {
            this.hours = this.hours - 1;
            this.minutes = 60 + this.minutes;
        }
    }

    private void shiftRight(int mls) {
       this.miliseconds= this.miliseconds + mls;
        if (this.miliseconds >= 1000) {
            this.seconds = this.seconds + 1;
            this.miliseconds = this.miliseconds - 1000;
        }
        if (this.seconds >= 60) {
            this.minutes = this.minutes + 1;
            this.seconds = this.seconds - 60;
        }
        if (this.minutes >= 60) {
            this.hours = this.hours + 1;
            this.minutes = this.minutes - 60;
        }

    }

    public static InnerTime getTimeFromString(String line) {
        String[] lines = line.split(":");
        int hours = Integer.parseInt(lines[0]);
        int minutes = Integer.parseInt(lines[1]);
        String[] minAndSec = lines[2].split(",");
        int seconds = Integer.parseInt(minAndSec[0]);
        int miliseconds = Integer.parseInt(minAndSec[1]);
        return new InnerTime(hours, minutes, seconds, miliseconds);
    }

}

class Subtitle {
    private int id;
    private List<String> subtitles;
    private InnerTime begin;
    private InnerTime end;

    public Subtitle(int id, InnerTime begin, InnerTime end, List<String> subtitles) {
        this.id = id;
        this.begin = begin;
        this.subtitles = subtitles;
        this.end = end;
    }

    public void shift(int ms) {
        this.begin.shift(ms);
        this.end.shift(ms);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", id));
        sb.append(String.format("%s --> %s", begin, end)).append("\n");
        this.subtitles.forEach(s -> sb.append(s + "\n"));
        return sb.toString();
    }
    public static Subtitle getSubtitle(List<String> lines){
        int id=Integer.parseInt(lines.get(0));
        String[] timeParts=lines.get(1).split("\\s+");
        InnerTime begin=InnerTime.getTimeFromString(timeParts[0]);
        InnerTime end=InnerTime.getTimeFromString(timeParts[2]);
        List<String> subtitles= IntStream.range(2,lines.size()).
                mapToObj(i->lines.get(i))
                .collect(Collectors.toList());
        return new Subtitle(id,begin,end,subtitles);
    }

}

class Subtitles {
    private List<Subtitle> subtitles;

    public Subtitles() {
        this.subtitles = new ArrayList<>();
    }

   private void fillSubtitles(List<String> lines){

        String line=null;
       List<String> tmp=new ArrayList<>();
        for(int i=0;i<lines.size();i++){
            line=lines.get(i);
            if(line.isEmpty()){
                this.subtitles.add(Subtitle.getSubtitle(tmp));
                tmp=new ArrayList<>();
                continue;
            }else{
                tmp.add(line);
            }
            if(i==lines.size()-1){

                this.subtitles.add(Subtitle.getSubtitle(tmp));
            }
        }

   }

    public int loadSubtitles(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines=br.lines().collect(Collectors.toList());
        fillSubtitles(lines);


        return this.subtitles.size();
    }

    public void print() {
        this.subtitles.forEach(System.out::println);
    }

    public void shift(int ms) {
        this.subtitles.forEach(s -> s.shift(ms));
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}
