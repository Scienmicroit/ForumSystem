package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Post implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 静态计数器，用于生成唯一帖子ID
    private static int nextId = 1;
    private String title;
    private String content;
    private final int id;
    private final String author;
    private final LocalDate date;
    private final List<Reply> replies;
    private boolean isSticky;
    private int likes;


    /**
     * 构造函数，初始化帖子基本信息
     *
     * @param title   帖子标题
     * @param content 帖子内容
     * @param author  帖子作者
     */
    public Post(String title, String content, String author) {
        this.id = nextId++;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = LocalDate.now();
        //设置帖子的日期为当前日期
        this.replies = new ArrayList<>();
        // 设置帖子默认不置顶
        this.isSticky = false;
        this.likes = 0;
    }
    // --------------------- Getters ---------------------

    public int getId() {
        return id;
    }
    /**
    * 获取帖子的标题
    * 
    * @return 帖子的标题
    */
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Reply> getReplies() {
        return replies;
    }
    /**
     * 设置帖子的置顶状态
     * 
    * @param sticky 帖子的新置顶状态
    */

    public boolean isSticky() {
        return isSticky;
    }
    
    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public int getLikes() {
        return likes;
    }
    public void addLike() {
        likes++;
    }


    // --------------------- Setters ---------------------

    /**
     * 修改帖子标题
     *
     * 新标题
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }
    /**
     * 修改帖子内容
     *
     * 新内容
     */
    public void setContent(String newContent) {
        this.content = newContent;
    }

    // --------------------- 功能方法 ---------------------
    /**
     * 添加回复到帖子
     *
     * @param reply 回复对象
     */
    public void addReply(Reply reply) {
        replies.add(reply);
    }

    /**
     * 获取格式化的日期字符串（yyyy-MM-dd）
     *
     * @return 格式化后的日期字符串
     */
    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
