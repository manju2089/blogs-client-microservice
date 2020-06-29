package blogs.microservice.model;

/**
 * Created by manju on 28-06-2020.
 */
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "blogs")

public class Blog {

    private long id;

    @Column(name = "blogName", nullable = false)
    private String blogName;


    public Blog(){

    }

    public Blog(String name) {
        this.blogName = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId(){
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getBlogName() {
        return blogName;
    }
    public void setBlogName(String name) {
        this.blogName = name;
    }

}
