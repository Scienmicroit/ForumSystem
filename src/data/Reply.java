package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Reply implements Serializable {
    @Serial
    private static final long serialVersionUID =1L;
    // 回复的内容
    private String content;
    private final String author;
    private final LocalDate date;
    
    /**
     * 构造一个新的回复对象
     * 初始化回复内容、作者以及发布时间
     *
     * @param content 回复的内容
     * @param author  回复的作者
     */

    public Reply(String content, String author) {
        this.content = content;
        this.author = author;
        this.date = LocalDate.now();
    }
    //Setter
    public void setContent(String content){
        this.content = content;
    }
    /**
     * 获取回复内容
     *
     * @return 回复的内容
     */
    public String getContent() {
        return content;
    }
    /**
     * 获取回复的作者
     *
     * @return 回复的作者用户名
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 获取回复的发布日期
     *
     * @return 回复的发布日期（LocalDate类型）
     */
    public LocalDate getDate() {
        return date;
    }


    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
