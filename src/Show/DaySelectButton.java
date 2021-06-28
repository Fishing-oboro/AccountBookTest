package Show;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DaySelectButton extends JButton {
    private int day;

    public DaySelectButton(int day,CalenderPanel calender){
        this.setFont(new Font(null,Font.BOLD,20));
        this.day=day;
        setText(String.valueOf(this.day));
        setVerticalAlignment(TOP);
        setHorizontalAlignment(LEFT);

        if(day==calender.getLocalDate().getDayOfMonth())
            setBackground(Color.yellow);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //選択日付の変更・パネル更新
                LocalDate localDate=calender.getLocalDate();
                calender.setLocalDate(localDate.getYear(),localDate.getMonth(),day);
                calender.reloadPanel();
            }
        });
    }
}
