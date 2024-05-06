import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Virus {

    private final String attackNode;
    private final String attackType;
    private final String attackDate;
    private final Date attackTime;

    public Virus(String attackNode, String attackType, String attackDate, String attackTime) {
        Date attackTime1;
        this.attackNode = attackNode;
        this.attackType = attackType;
        this.attackDate = attackDate;
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        try {
            attackTime1 = dateFormat.parse(attackTime);
        } catch(ParseException e) {
            System.out.println(e);
            attackTime1 = new Date();
        }
        this.attackTime = attackTime1;
    }

    public String getAttackNode() {
        return attackNode;
    }

    public String getAttackType() {
        return attackType;
    }

    public String getAttackDate() {
        return attackDate;
    }

    public Date getAttackTime() {
        return attackTime;
    }
}