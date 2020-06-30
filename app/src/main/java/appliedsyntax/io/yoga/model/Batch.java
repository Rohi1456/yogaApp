package appliedsyntax.io.yoga.model;

import java.io.Serializable;

public class Batch implements Serializable {
    private String name;
    private String location;
    private String docID;

    public Batch(String name, String location) {
        this.name = name;
        this.location = location;
    }
    public Batch()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
