import javax.swing.*;

public class GUI extends JFrame {

    private Map map;

    public GUI() {
        this(new Map());
    }

    public GUI(Map m) {
        map = m;

    }
}
