package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Reply implements Serializable {
    @Serial
    private static final long serialVersionUID =1L;
    private String content;
    private final String author;
    private final LocalDate date;

    public Reply(String content, String author) {
        this.content = content;
        this.author = author;
        this.date = LocalDate.now();
    }
    //Setter
    public void setContent(String content){
        this.content = content;
    }
    // Getters
    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
