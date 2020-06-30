package appliedsyntax.io.yoga.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class FeePament implements Serializable {
    private Date dueDate;
    private Date paidDate;
    private int amount;
    private String studentID;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    FeePament()
    {

    }

    public FeePament(Timestamp dueDate, Timestamp paidDate, int amount, String studentID) {
        this.dueDate = dueDate;
        this.paidDate = paidDate;
        this.amount = amount;
        this.studentID = studentID;
    }

    public Date getDueDate() {
        return  dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
