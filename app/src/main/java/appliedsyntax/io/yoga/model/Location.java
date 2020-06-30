package appliedsyntax.io.yoga.model;

import java.io.Serializable;

public class Location implements Serializable {
    private String name;
    private String address;
    private String docID;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public Location()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
