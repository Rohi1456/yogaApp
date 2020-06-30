package appliedsyntax.io.yoga.model;

import java.io.Serializable;

public class Students implements Serializable {
    private String name;
    private String Sbatch;
    private String Slocation;
    private String SmobileNumber;
    private boolean  Sfees_pay_status;
    private boolean  Sactive;
    private int Sage;
    private String Sprofilepic;
    private String DocumentID;

    public Students()
    {

    }
    public Students(String name, String sbatch, String slocation, String smobileNumber, boolean sfees_pay_status, boolean sactive, int sage, String sprofilepic) {
        this.name = name;
        Sbatch = sbatch;
        Slocation = slocation;
        SmobileNumber = smobileNumber;
        Sfees_pay_status = sfees_pay_status;
        Sactive = sactive;
        Sage = sage;
        Sprofilepic = sprofilepic;
    }

    public String getSname() {
        return name;
    }

    public void setSname(String sname) {
        name = sname;
    }

    public String getSbatch() {
        return Sbatch;
    }

    public void setSbatch(String sbatch) {
        Sbatch = sbatch;
    }

    public String getSlocation() {
        return Slocation;
    }

    public void setSlocation(String slocation) {
        Slocation = slocation;
    }

    public String getSmobileNumber() {
        return SmobileNumber;
    }

    public void setSmobileNumber(String smobileNumber) {
        SmobileNumber = smobileNumber;
    }

    public boolean isSfees_pay_status() {
        return Sfees_pay_status;
    }

    public void setSfees_pay_status(boolean sfees_pay_status) {
        Sfees_pay_status = sfees_pay_status;
    }

    public boolean isSactive() {
        return Sactive;
    }

    public void setSactive(boolean sactive) {
        Sactive = sactive;
    }

    public int getSage() {
        return Sage;
    }

    public void setSage(int sage) {
        Sage = sage;
    }

    public String getSprofilepic() {
        return Sprofilepic;
    }

    public void setSprofilepic(String sprofilepic) {
        Sprofilepic = sprofilepic;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

}

