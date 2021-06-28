package Main;

import Show.CalenderPanel;
import Show.GraphPanel;
import Show.StartPanel;
import Process.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private StartPanel startPanel;
    private CalenderPanel calenderPanel;
    private GraphPanel graphPanel;
    private JPanel nowPanel;
    private CardLayout cardLayout;
    private User user;

    public MainFrame(){
        Font font=new Font(null,Font.BOLD,18);
        UIManager.put("OptionPane.buttonFont", new Font(null,Font.BOLD,15));
        UIManager.put("OptionPane.font", font);
        UIManager.put("OptionPane.messageFont", font);
        user=new User();
        setNowPanel();

        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setBounds(0,0,1200,1000);
    }

    public static void main(String[] args) {
        MainFrame frame =new MainFrame();
        frame.setTitle("家計簿アプリ");
        //frame.pack();
        frame.setVisible(true);
    }

    public void setNowPanel(){
        this.nowPanel=new JPanel();
        this.cardLayout=new CardLayout();
        startPanel=new StartPanel(this);
        calenderPanel=new CalenderPanel(this);
        graphPanel=new GraphPanel(this);
        nowPanel.setLayout(cardLayout);
        nowPanel.add(startPanel,"start");
        nowPanel.add(calenderPanel,"calender");
        nowPanel.add(graphPanel,"graph");
        add(nowPanel);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getNowPanel() {
        return nowPanel;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public CalenderPanel getCalenderPanel() {
        return calenderPanel;
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }
}
