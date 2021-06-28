package Show;

import Main.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import Process.EditProcess;
import Process.Category;
import Process.BudgetProcess;
import Process.CalculateProcess;

public class CalenderPanel extends JPanel{
    private MainFrame mf;
    private LocalDate localDate;
    private JPanel dayOfMonthPanel;
    private JPanel PanelControl;
    private OneDayEditPanel editPanel;
    private Month month;
    private int year;
    private JComboBox<String>subCategoryBox;
    private ArrayList<Integer>cidList;
    private ArrayList<Integer>sidList;
    private GridBagLayout layout;
    private GridBagConstraints gbc;

    class WeekLabel extends JButton{
        public WeekLabel(String name){
            this.setText(name);
            this.setFont(new Font(null,Font.BOLD,20));
        }
    }

    public CalenderPanel(MainFrame mf){
        //////カレンダー部分の作成////
        this.mf=mf;
        layout=new GridBagLayout();
        setLayout(layout);
        gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        this.localDate=LocalDate.now();
        this.month=this.localDate.getMonth();
        this.year=this.localDate.getYear();

        setPanelControl();
        setCalender();
        setEditPanel();

        add(this.PanelControl);
        add(this.dayOfMonthPanel);
        add(this.editPanel);
    }



    public void setPanelControl(){
        PanelControl=new JPanel();
        PanelControl.setLayout(new BoxLayout(PanelControl,BoxLayout.PAGE_AXIS));

        JLabel userName =new JLabel("ユーザ名:"+mf.getUser().getName());
        userName.setFont(new Font(null,Font.BOLD,20));
        userName.setAlignmentX(CENTER_ALIGNMENT);

        JPanel datePanel=new JPanel();
        JComboBox<String>yearBox=new JComboBox<>();
        yearBox.setFont(new Font(null,Font.BOLD,25));
        for(int i=-3;i<=3;i++){
            yearBox.addItem(year+i+"年");
        }
        yearBox.setMaximumSize(yearBox.getMinimumSize());
        yearBox.setSelectedIndex(3);
        yearBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocalDate(year+yearBox.getSelectedIndex()-3,month,localDate.getDayOfMonth());
                year= localDate.getYear();
                reloadPanel();
            }
        });
        datePanel.add(yearBox);

        JComboBox<String>monthBox=new JComboBox<>();
        monthBox.setFont(new Font(null,Font.BOLD,25));
        for(int i=1;i<=12;i++){
            monthBox.addItem(i+"月");
            if(month.getValue()==i)
                monthBox.setSelectedIndex(i-1);
        }
        monthBox.setMaximumSize(monthBox.getMinimumSize());
        monthBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocalDate(year,Month.of(monthBox.getSelectedIndex()+1),localDate.getDayOfMonth());
                month=localDate.getMonth();
                reloadPanel();
            }
        });
        datePanel.add(monthBox);
////
        JPanel calc=new JPanel();
        calc.setAlignmentX(CENTER_ALIGNMENT);
        calc.setBackground(Color.WHITE);
        calc.setBorder(new LineBorder(Color.BLACK,1,true));
        calc.setLayout(new BoxLayout(calc,BoxLayout.PAGE_AXIS));
        int budgetn=BudgetProcess.fetchBudget(mf.getUser().getId(),localDate);
        int costn=CalculateProcess.fetchMonthCost(mf.getUser().getId(),localDate);
        int resultn=budgetn-costn;
        JLabel budget =new JLabel("   　予算:  ￥"+budgetn);
        JLabel cost=new JLabel("ー   支出:  ￥"+costn);
        JLabel result=new JLabel("   　差額:  ￥"+resultn);
        if(resultn<0)result.setForeground(new Color(220,20,60));//crimson
        calc.add(budget);
        calc.add(cost);
        calc.add(new JLabel("------------------------"));
        calc.add(result);
        for(int i=0;i<calc.getComponentCount();i++){
            calc.getComponent(i).setFont(new Font(null,Font.BOLD,20));
        }

        JButton BudgetSet =new JButton("予算設定");
        BudgetSet.setFont(new Font(null,Font.BOLD,20));
        BudgetSet.setAlignmentX(CENTER_ALIGNMENT);
        BudgetSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int money=0;
                String number=JOptionPane.showInputDialog(
                        mf,
                        "予算を入力してください"
                );
                try{
                    money=Integer.parseInt(number);
                }catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(
                            mf,
                            "予算設定に失敗しました。"
                    );
                    return;
                }
                if(BudgetProcess.checkBudget(localDate))
                    BudgetProcess.updateBudget(mf.getUser().getId(),money,localDate);
                else BudgetProcess.addBudget(mf.getUser().getId(),money,localDate);
                reloadPanel();
            }
        });
/////
        JButton returnButton=new JButton("TOPに戻る");
        returnButton.setFont(new Font(null,Font.BOLD,20));
        returnButton.setAlignmentX(CENTER_ALIGNMENT);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.getCardLayout().show(mf.getNowPanel(),"start");
            }
        });

        JButton graphButton =new JButton("詳細情報");
        graphButton.setAlignmentX(CENTER_ALIGNMENT);
        graphButton.setFont(new Font(null,Font.BOLD,20));
        graphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.getGraphPanel().reloadPanel();
            }
        });

        JButton addButton=new JButton("分類追加");
        addButton.setAlignmentX(CENTER_ALIGNMENT);
        addButton.setFont(new Font(null,Font.BOLD,20));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inputForm=new JPanel();
                ButtonGroup selects=new ButtonGroup();
                JRadioButton select1=new JRadioButton("大分類");
                JRadioButton select2=new JRadioButton("小分類");

                selects.add(select1);
                selects.add(select2);
                JLabel info=new JLabel("分類名:");
                JTextField text=new JTextField();
                text.setColumns(12);
                inputForm.add(select1);
                inputForm.add(select2);
                inputForm.add(info);
                inputForm.add(text);
                for(int i=0;i<inputForm.getComponentCount();i++) {
                    inputForm.getComponent(i).setFont(new Font(null,Font.BOLD,18));
                }
                JComboBox<String>categoryBox=new JComboBox<>();
                int result=JOptionPane.showConfirmDialog(
                        mf,
                        inputForm,
                        "分類追加",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                switch (result){
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        return;
                    case JOptionPane.OK_OPTION:
                        String tableName;
                        if(select1.isSelected())
                            tableName = "categoryList";
                        else {
                            tableName = "subCategoryList";
                            JPanel panel=new JPanel();
                            JLabel categoryLabel=new JLabel("大分類:");
                            categoryBox=new JComboBox<>();
                            cidList=new ArrayList<>();
                            ArrayList<Category>categories=EditProcess.fetchCategoryList(mf.getUser().getId());
                            for(Category category:categories){
                                categoryBox.addItem(category.getName());
                                cidList.add(category.getId());
                            }
                            panel.add(categoryLabel);
                            panel.add(categoryBox);
                            for(int i=0;i<panel.getComponentCount();i++) {
                                panel.getComponent(i).setFont(new Font(null,Font.BOLD,18));
                            }
                            int r=JOptionPane.showConfirmDialog(
                                mf,
                                panel,
                                "大分類選択",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                            );
                            switch (r) {
                                case JOptionPane.CLOSED_OPTION:
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    return;
                                case JOptionPane.OK_OPTION:
                            }
                        }
                        if(tableName.equals("categoryList")) {
                            if(EditProcess.checkCategory(text.getText(),mf.getUser().getId())){
                                JOptionPane.showMessageDialog(
                                        mf,
                                        "その項目は既に存在します。"
                                );
                                return;
                            }
                            EditProcess.addCategory(text.getText(), mf.getUser().getId());

                        }
                        else if(tableName.equals("subCategoryList")) {
                            if(EditProcess.checkSubCategory(text.getText(), mf.getUser().getId(), cidList.get(categoryBox.getSelectedIndex()))){
                                JOptionPane.showMessageDialog(
                                        mf,
                                        "その項目は既に存在します。"
                                );
                                return;
                            }
                            EditProcess.addSubCategory(
                                    text.getText(),
                                    mf.getUser().getId(),
                                    cidList.get(categoryBox.getSelectedIndex())
                            );
                        }
                        JOptionPane.showMessageDialog(
                                mf,
                                "追加に成功しました。"
                        );
                        return;
                }

            }
        });

        JButton deleteButton=new JButton("分類削除");
        deleteButton.setAlignmentX(CENTER_ALIGNMENT);
        deleteButton.setFont(new Font(null,Font.BOLD,20));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inputForm=new JPanel();
                JPanel panel=new JPanel();
                ButtonGroup selects=new ButtonGroup();
                JRadioButton select1=new JRadioButton("大分類");
                select1.setFont(new Font(null,Font.BOLD,18));
                JRadioButton select2=new JRadioButton("小分類");
                select2.setFont(new Font(null,Font.BOLD,18));
                selects.add(select1);
                selects.add(select2);

                inputForm.add(select1);
                inputForm.add(select2);

                JLabel categoryLabel=new JLabel("大分類:");
                categoryLabel.setFont(new Font(null,Font.BOLD,18));
                JComboBox<String>categoryBox=new JComboBox<>();
                categoryBox.setFont(new Font(null,Font.BOLD,18));
                cidList=new ArrayList<>();
                ArrayList<Category>categories=EditProcess.fetchCategoryList(mf.getUser().getId());
                for(Category category:categories){
                    categoryBox.addItem(category.getName());
                    cidList.add(category.getId());
                }
                panel.add(categoryLabel);
                panel.add(categoryBox);
                inputForm.add(panel);

                JPanel subCategoryPanel=new JPanel();
                JLabel subCategoryLabel=new JLabel("小分類:");
                subCategoryLabel.setFont(new Font(null,Font.BOLD,18));
                setSubCategoryBox(cidList.get(categoryBox.getSelectedIndex()));
                categoryBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        inputForm.remove(subCategoryPanel);
                        subCategoryPanel.remove(subCategoryBox);
                        setSubCategoryBox(cidList.get(categoryBox.getSelectedIndex()));
                        subCategoryPanel.add(subCategoryBox);
                        inputForm.add(subCategoryPanel);
                        inputForm.revalidate();
                    }
                });
                subCategoryPanel.add(subCategoryLabel);
                subCategoryPanel.add(subCategoryBox);
                inputForm.add(subCategoryPanel);

                int result=JOptionPane.showConfirmDialog(
                        mf,
                        inputForm,
                        "分類削除画面",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                switch(result){
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        return;
                    case JOptionPane.OK_OPTION:
                        int r=JOptionPane.showConfirmDialog(
                                mf,
                                "分類に含まれるデータも削除されます。本当に削除しますか？",
                                "確認画面",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );
                        switch(r){
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                return;
                            case JOptionPane.OK_OPTION:
                                String tableName="categoryList";
                                int id=cidList.get(categoryBox.getSelectedIndex());
                                if(select2.isSelected()) {
                                    tableName = "subCategoryList";
                                    id=sidList.get(subCategoryBox.getSelectedIndex());
                                }
                                if(!EditProcess.deleteCategory(tableName,id,mf.getUser().getId())) {
                                    JOptionPane.showMessageDialog(
                                            mf,
                                            "その項目は削除できません。"
                                    );
                                    return;
                                }
                                JOptionPane.showMessageDialog(
                                        mf,
                                        "削除が完了しました。"
                                );
                        }
                }
            }
        });

        PanelControl.add(userName);
        PanelControl.add(Box.createRigidArea(new Dimension(0,10)));
        PanelControl.add(datePanel);
        PanelControl.add(Box.createRigidArea(new Dimension(0,180)));
        PanelControl.add(calc);
        PanelControl.add(Box.createRigidArea(new Dimension(0,10)));
        PanelControl.add(BudgetSet);
        PanelControl.add(Box.createRigidArea(new Dimension(0,80)));
        PanelControl.add(graphButton);
        PanelControl.add(Box.createRigidArea(new Dimension(0,40)));
        PanelControl.add(addButton);
        PanelControl.add(deleteButton);
        PanelControl.add(Box.createRigidArea(new Dimension(0,40)));
        PanelControl.add(returnButton);
        PanelControl.add(Box.createRigidArea(new Dimension(0,100)));
        setGbcXy(1,1);
        setGbcSize(1,2);
        setGbcWeight(0.1,0);
        gbc.anchor=GridBagConstraints.WEST;
        layout.setConstraints(PanelControl,gbc);
    }

    public void setSubCategoryBox(int id) {
        subCategoryBox=new JComboBox<>();
        subCategoryBox.setFont(new Font(null,Font.BOLD,18));
        sidList=new ArrayList<>();
        ArrayList<Category>subCategories= EditProcess.fetchSubCategoryList(mf.getUser().getId(),id);
        for(Category category:subCategories){
            subCategoryBox.addItem(category.getName());
            sidList.add(category.getId());
        }
    }

    public void setCalender(){
        this.dayOfMonthPanel=new JPanel();
        String[]weekList={
                "日",
                "月",
                "火",
                "水",
                "木",
                "金",
                "土"
        };

        for(int i=0;i<7;i++){
            dayOfMonthPanel.add(new WeekLabel(weekList[i]));
        }

        //初日の曜日を取得
        DayOfWeek firstDayOfWeek=LocalDate.of(this.year,this.month,1).getDayOfWeek();
        //this.localDateの月末を取得
        int lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        //日付ボタン生成
        ArrayList<DaySelectButton>daySelectButtons=new ArrayList<>();
        for(int i=1;i<=lastDayOfMonth;i++){
            daySelectButtons.add(new DaySelectButton(i,this));
        }
        //初日まで空ボタンを挿入し、月末までそれぞれの日付ボタンを挿入
        int locationDate=setLocationDate(firstDayOfWeek);
        for(int i=0;i<locationDate;i++){
            dayOfMonthPanel.add(new Button());
        }
        for(DaySelectButton daySelectButton:daySelectButtons) {
            dayOfMonthPanel.add(daySelectButton);
        }
        //レイアウト設定
        if((lastDayOfMonth+locationDate)>7*5)
            dayOfMonthPanel.setLayout(new GridLayout(7,7));
        else
            dayOfMonthPanel.setLayout(new GridLayout(6,7));
        setGbcXy(2,1);
        setGbcSize(1,1);
        setGbcWeight(0,0.1);
        gbc.anchor=GridBagConstraints.CENTER;
        layout.setConstraints(dayOfMonthPanel,gbc);
    }

    public void setEditPanel() {
        this.editPanel=new OneDayEditPanel(this);
        setGbcXy(2,2);
        setGbcSize(1,1);
        setGbcWeight(1,0.1);
        gbc.anchor=GridBagConstraints.NORTH;
        layout.setConstraints(editPanel,gbc);
    }

    public void setLocalDate(int year, Month month, int day){
        this.localDate=LocalDate.of(year,month,day);
    }

    public int setLocationDate(DayOfWeek dayOfWeek){
        int locationDate=-1;
        if(dayOfWeek.equals(DayOfWeek.SUNDAY)){
            locationDate=0;
        }else if(dayOfWeek.equals(DayOfWeek.MONDAY)){
            locationDate=1;
        }else if(dayOfWeek.equals(DayOfWeek.TUESDAY)){
            locationDate=2;
        }else if(dayOfWeek.equals(DayOfWeek.WEDNESDAY)){
            locationDate=3;
        }else if(dayOfWeek.equals(DayOfWeek.THURSDAY)){
            locationDate=4;
        }else if(dayOfWeek.equals(DayOfWeek.FRIDAY)){
            locationDate=5;
        }else if(dayOfWeek.equals(DayOfWeek.SATURDAY)){
            locationDate=6;
        }
        return locationDate;
    }

    public void reloadPanel(){
        this.removeAll();
        setPanelControl();
        setCalender();
        setEditPanel();
        this.add(PanelControl);
        this.add(dayOfMonthPanel);
        this.add(editPanel);
        this.revalidate();
        mf.getCardLayout().show(mf.getNowPanel(),"calender");
    }

    public void setGbcXy(int x,int y){
        gbc.gridx=x;
        gbc.gridy=y;
    }

    public void setGbcSize(int width,int height){
        gbc.gridwidth=width;
        gbc.gridheight=height;
    }

    public void setGbcWeight(double x,double y){
        gbc.weightx=x;
        gbc.weighty=y;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public MainFrame getMf() {
        return mf;
    }

    public void setMonth(Month month) {
        this.month = month;
    }
    public void setYear(int year) {
        this.year = year;
    }
}