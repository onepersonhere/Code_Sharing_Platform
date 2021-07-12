package platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//not using lombok cause of test
@Entity(name = "Code")
@Table
public class Code {
    @Column
    private String code;
    @Column
    private String date;
    @Column
    private long time = 0;
    @Column
    private int views = 0;
    @Column
    private boolean isRestricted = false;
    @Id
    private String id;

    private long OriginalTime;
    private int OriginalViews;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getOriginalTime() {
        return OriginalTime;
    }

    public void setOriginalTime(long originalTime) {
        time = originalTime;
        OriginalTime = originalTime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getOriginalViews() {
        return OriginalViews;
    }

    public void setOriginalViews(int originalViews) {
        views = originalViews;
        OriginalViews = originalViews;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

    public void setRestriction(boolean restricted) {
        isRestricted = restricted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isExpired(){
        //if original time = 0?
        //if original view = 0?
        if(isRestricted){
            if(OriginalTime == 0 && views < 0) return true;
            if(OriginalViews == 0 && time <= 0) return true;
            if((OriginalTime != 0 && time <= 0)
                    || (OriginalViews != 0 && views < 0)) return true;
        }
        return false;
    }
}
