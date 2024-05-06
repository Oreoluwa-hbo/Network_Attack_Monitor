import java.util.Date;
import java.util.List;

public class Attack {

    private String type;
    private Date dateTime;

    Attack(String type, Date date) {
        this.type = type;
        this.dateTime = date;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return dateTime;
    }


    int getAttacksWithinTimeFrame(List<Attack> previousAttacks, int timeTreshold){
        // compares the date/time of all previous attacks with the current attack
        int count = 0;

        for(Attack a: previousAttacks){
            long timeDifference =this.getDate().getTime() - a.getDate().getTime();
            if(Math.abs(timeDifference/60000.0) <= timeTreshold){
                count++;
            }
        }

        // count = number of previous attacks within the timeThreshold of the current attack
        // the +1 implies that the current attack needs to be included in the final count
        return count+1;
    }

}
