package knk.ee.neverland.pojos;

import java.time.LocalDateTime;

public class Quest {
    private int id;
    private String name;
    private String description;
    private User creator;
    private LocalDateTime timeCreated;
    private int groupID;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
