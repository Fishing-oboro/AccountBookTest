package Show;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;

import Process.Product;
import Process.ProductProcess;
import Process.EditProcess;
import Process.Category;

public class OneDayEditPanel extends JPanel{
    private CalenderPanel calender;
    private JScrollPane scrollPane;
    private JComboBox<String>subCategoryBox;
    private JPanel control;
    private JPanel inputForm;
    private JTable table;
    private ArrayList<Integer>pidList;
    private ArrayList<Integer>cidList;
    private ArrayList<Integer>sidList;
    private Product product;

    public OneDayEditPanel(CalenderPanel calender){
        this.calender=calender;

        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setScrollPane();
        setControl();
    }

    public void setScrollPane(){
        this.scrollPane=new JScrollPane();
        scrollPane.getViewport().setBackground(Color.lightGray);
        DefaultTableModel model =new DefaultTableModel(){
            public Class<?> getColumnClass(int column){
                switch(column){
                    case 0:
                        return Boolean.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;
                    case 5:
                        return String.class;
                    default:
                        return String.class;
                }
            }
        };

        this.table=new JTable();
        table.setRowHeight(30);
        table.setFont(new Font(null,Font.PLAIN,20));
        table.getTableHeader().setFont(new Font(null,Font.BOLD,20));
        this.scrollPane.setViewportView(this.table);
        this.table.setModel(model);

        model.addColumn("選択");
        model.addColumn("日付");
        model.addColumn("大分類");
        model.addColumn("小分類");
        model.addColumn("費用");
        model.addColumn("備考");

        ArrayList<Product>products=ProductProcess.fetchProduct(calender.getMf().getUser().getId(),Date.valueOf(calender.getLocalDate()));
        this.pidList=new ArrayList<>();
        int i=0;
        //index: table=product=pidListであるため、それを参照する
        for(Product product:products){
            model.addRow(new Object[0]);
            model.setValueAt(false,i,0);
            model.setValueAt(product.getBuyDate(),i,1);
            model.setValueAt(product.getCategoryName(),i,2);
            model.setValueAt(product.getSubcategoryName(),i,3);
            model.setValueAt("￥"+product.getCost(),i,4);
            model.setValueAt(product.getMessage(),i,5);
            this.pidList.add(product.getId());
            i++;
        }
        add(this.scrollPane);
    }

    public void setControl(){
        this.control=new JPanel();
        control.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel message=new JLabel("データ関連項目:");
        message.setFont(new Font(null,Font.BOLD,20));
        control.add(message);
        JButton add=new JButton("追加");
        add.setFont(new Font(null,Font.BOLD,20));
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputFormPanel(0,new JLabel("データを入力してください"))){
                    ProductProcess.addProduct(calender.getMf().getUser().getId(),product);
                    calender.reloadPanel();
                }
            }
        });

        JButton delete=new JButton("削除");
        delete.setFont(new Font(null,Font.BOLD,20));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer>delete=new ArrayList<>();
                for(int i=0;i<table.getRowCount();i++){
                    Boolean check=Boolean.valueOf(table.getValueAt(i,0).toString());
                    if(check) delete.add(i);
                }
                //ここで削除するか確認
                int result=JOptionPane.showConfirmDialog(
                        calender.getMf(),
                        "指定されたデータを削除します。",
                        "確認",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                switch(result){
                    case JOptionPane.CANCEL_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.OK_OPTION:
                        for(int index:delete){
                            ProductProcess.deleteProduct(pidList.get(index));
                        }
                        break;
                }
                calender.reloadPanel();
            }
        });

        JButton update=new JButton("編集");
        update.setFont(new Font(null,Font.BOLD,20));
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer>update=new ArrayList<>();
                JLabel label;
                int j=1;
                for(int i=0;i<table.getRowCount();i++){
                    Boolean check=Boolean.valueOf(table.getValueAt(i,0).toString());
                    if(check) update.add(i);//index
                }
                for(int index:update){
                    label=new JLabel(j+"個目のデータの入力");
                    if(inputFormPanel(pidList.get(index),label)){
                        ProductProcess.updateProduct(product);
                    }
                    j++;
                }
                calender.reloadPanel();
            }
        });

        this.control.add(add);
        this.control.add(delete);
        this.control.add(update);
        add(this.control);
    }

    public boolean inputFormPanel(int id,JLabel mainMessage){
        inputForm=new JPanel();
        JPanel input =new JPanel();
        input.setLayout(new BoxLayout(input,BoxLayout.PAGE_AXIS));
        inputForm.setLayout(new BoxLayout(inputForm,BoxLayout.PAGE_AXIS));

        JPanel categoryPanel=new JPanel();
        JLabel categoryLabel=new JLabel("大分類:");
        categoryLabel.setFont(new Font(null,Font.BOLD,18));
        JComboBox<String>categoryBox=new JComboBox<>();
        categoryBox.setFont(new Font(null,Font.BOLD,18));
        this.cidList=new ArrayList<>();
        ArrayList<Category>categories=EditProcess.fetchCategoryList(calender.getMf().getUser().getId());
        for(Category category:categories){
            categoryBox.addItem(category.getName());
            this.cidList.add(category.getId());
        }
        JPanel subCategoryPanel=new JPanel();
        JLabel subCategoryLabel=new JLabel("小分類:");
        subCategoryLabel.setFont(new Font(null,Font.BOLD,18));
        categoryBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.remove(subCategoryPanel);
                subCategoryPanel.remove(subCategoryBox);
                setSubCategoryBox(cidList.get(categoryBox.getSelectedIndex()));
                subCategoryPanel.add(subCategoryBox);
                input.add(subCategoryPanel,1);
                reloadInputForm(input,mainMessage);
            }
        });

        setSubCategoryBox(cidList.get(categoryBox.getSelectedIndex()));

        JPanel taxPanel=new JPanel();
        JLabel taxMenu=new JLabel("税設定:");
        taxMenu.setFont(new Font(null,Font.BOLD,18));
        JComboBox<String>taxMenuBox=new JComboBox<>();
        taxMenuBox.setFont(new Font(null,Font.BOLD,18));
        taxMenuBox.addItem("税込価格");
        taxMenuBox.addItem("税率10％");
        taxMenuBox.addItem("税率8％");

        JPanel costPanel=new JPanel();
        JLabel costLabel=new JLabel("費用:");
        costLabel.setFont(new Font(null,Font.BOLD,18));
        JTextField costForm=new JTextField("");
        costForm.setColumns(12);
        costForm.setFont(new Font(null,Font.PLAIN,18));

        JPanel messagePanel=new JPanel();
        JLabel messageLabel=new JLabel("備考:");
        messageLabel.setFont(new Font(null,Font.BOLD,18));
        JTextField messageForm=new JTextField("");
        messageForm.setColumns(12);
        messageForm.setFont(new Font(null,Font.PLAIN,18));

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryBox);
        input.add(categoryPanel);
        subCategoryPanel.add(subCategoryLabel);
        subCategoryPanel.add(subCategoryBox);
        input.add(subCategoryPanel);
        taxPanel.add(taxMenu);
        taxPanel.add(taxMenuBox);
        input.add(taxPanel);
        costPanel.add(costLabel);
        costPanel.add(costForm);
        input.add(costPanel);
        messagePanel.add(messageLabel);
        messagePanel.add(messageForm);
        input.add(messagePanel);
        mainMessage.setFont(new Font(null,Font.BOLD,15));
        mainMessage.setAlignmentX(CENTER_ALIGNMENT);
        inputForm.add(mainMessage);
        inputForm.add(Box.createRigidArea(new Dimension(0,20)));
        inputForm.add(input);

        int result=JOptionPane.showConfirmDialog(
                calender.getMf(),
                inputForm,
                "入力フォーム",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        switch(result){
            case JOptionPane.CANCEL_OPTION:
                return false;
            case JOptionPane.CLOSED_OPTION:
                return false;
            case JOptionPane.OK_OPTION:
                if(subCategoryBox.getSelectedIndex()==-1) {
                    JOptionPane.showMessageDialog(
                            calender.getMf(),
                            "小分類が存在しません。"
                    );
                    return false;
                }
                this.product=new Product(
                        id,
                        this.cidList.get(categoryBox.getSelectedIndex()),
                        this.sidList.get(subCategoryBox.getSelectedIndex()),
                        calculate(taxMenuBox.getSelectedIndex(),Integer.parseInt(costForm.getText())),
                        Date.valueOf(calender.getLocalDate()),
                        messageForm.getText()
                );
        }
        return true;
    }

    private int calculate(int index,int cost){
        switch(index){
            case 0:
                return cost;
            case 1:
                return (int)Math.floor(cost*1.1);
            case 2:
                return (int)Math.floor(cost*1.08);
        }
        return cost;
    }

    public void setSubCategoryBox(int id) {
        subCategoryBox=new JComboBox<>();
        subCategoryBox.setFont(new Font(null,Font.BOLD,18));
        sidList=new ArrayList<>();
        ArrayList<Category>subCategories= EditProcess.fetchSubCategoryList(calender.getMf().getUser().getId(),id);
        for(Category category:subCategories){
            subCategoryBox.addItem(category.getName());
            sidList.add(category.getId());
        }
    }

    public void reloadInputForm(JPanel input,JLabel mainMessage){
        this.inputForm.removeAll();
        this.inputForm.add(mainMessage);
        this.inputForm.add(input);
        this.inputForm.validate();
    }


}