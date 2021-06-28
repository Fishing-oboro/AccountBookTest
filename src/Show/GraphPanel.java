package Show;

import Main.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import Process.Product;
import Process.Category;
import Process.ProductProcess;
import Process.CalculateProcess;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class GraphPanel extends JPanel {
    private MainFrame mf;
    private JTabbedPane tab;
    private JPanel tab1;
    private JPanel tab2;
    private JPanel tab3;
    private ArrayList<Integer>idList;
    private GridBagLayout layout;
    private GridBagConstraints gbc;

    public GraphPanel(MainFrame mf){
        this.mf=mf;
        layout=new GridBagLayout();
        setLayout(layout);
        gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        setControlPanel();
        setTab();
    }

    public void setTab(){
        tab=new JTabbedPane();
        tab.setFont(new Font(null,Font.BOLD,15));
        this.tab1=new JPanel();
        tab1.setLayout(new BoxLayout(tab1,BoxLayout.PAGE_AXIS));
        this.tab2=new JPanel();
        this.tab3=new JPanel();
        setMonthGraph();
        setScrollPane();
        setBarPanel();
        tab.addTab("大分類グラフ",tab1);
        tab.addTab("小分類グラフ",tab2);
        tab.addTab("月比較グラフ",tab3);
        setGbcXy(2,1);
        setGbcSize(1,1);
        setGbcWeight(0,0.1);
        gbc.anchor=GridBagConstraints.CENTER;
        layout.setConstraints(tab,gbc);
        add(tab);
    }

    public void setMonthGraph(){
        int sum=0;
        ChartPanel monthPanel;
        this.idList=new ArrayList<>();
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        DefaultPieDataset data=new DefaultPieDataset();
        ArrayList<Category>categoryList=CalculateProcess.fetchCategoryCostMonth(mf.getUser().getId(),mf.getCalenderPanel().getLocalDate());
        for(Category category:categoryList){
            sum+=category.getCost();
        }
        for(Category category:categoryList){
            data.setValue(category.getName(),new Double(category.getCost()*100/sum));
            this.idList.add(category.getId());
        }

        JFreeChart monthGraph= ChartFactory.createPieChart(
                mf.getCalenderPanel().getLocalDate().getMonthValue()+"月の大分類別円グラフ",
                data,
                true,
                true,
                false
                );
        monthPanel=new ChartPanel(monthGraph);
        monthGraph.getLegend().setItemFont(new Font(null,Font.PLAIN,20));
        PiePlot piePlot=(PiePlot)monthGraph.getPlot();
        piePlot.setLabelFont(new Font(null,Font.PLAIN,20));
        monthPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
                int i = Integer.valueOf(chartMouseEvent.getEntity().toString().substring(15, 16));
                tab2.removeAll();
                setSubMonthGraph(idList.get(i));
                tab.setSelectedIndex(1);
                revalidate();
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            }
        });
        JPanel graph=new JPanel();
        graph.add(monthPanel);
        setDataPanel(categoryList,graph);
        tab1.add(graph);
    }

    public void setControlPanel(){
        JPanel controlPanel=new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.PAGE_AXIS));

        LocalDate localDate=mf.getCalenderPanel().getLocalDate();
        JComboBox<String>monthBox=new JComboBox<>();
        monthBox.setFont(new Font(null,Font.BOLD,25));
        for(int i=1;i<=12;i++){
            monthBox.addItem(i+"月");
            if(localDate.getMonthValue()==i)
                monthBox.setSelectedIndex(i-1);
        }
        monthBox.setMaximumSize(monthBox.getMinimumSize());
        monthBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.getCalenderPanel().setLocalDate(localDate.getYear(), Month.of(monthBox.getSelectedIndex()+1),localDate.getDayOfMonth());
                mf.getCalenderPanel().setMonth(Month.of(monthBox.getSelectedIndex()+1));
                reloadPanel();
            }
        });

        JComboBox<String>yearBox=new JComboBox<>();
        yearBox.setFont(new Font(null,Font.BOLD,25));
        for(int i=-3;i<=3;i++){
            yearBox.addItem(localDate.getYear()+i+"年");
        }
        yearBox.setMaximumSize(yearBox.getMinimumSize());
        yearBox.setSelectedIndex(3);
        yearBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.getCalenderPanel().setLocalDate(localDate.getYear()+yearBox.getSelectedIndex()-3,localDate.getMonth(),localDate.getDayOfMonth());
                mf.getCalenderPanel().setYear(localDate.getYear()+yearBox.getSelectedIndex()-3);
                reloadPanel();
            }
        });

        JButton returnButton=new JButton("戻る");
        returnButton.setFont(new Font(null,Font.BOLD,20));
        returnButton.setAlignmentX(LEFT_ALIGNMENT);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.getCalenderPanel().reloadPanel();
                mf.getCardLayout().show(mf.getNowPanel(),"calender");
            }
        });

        JPanel date=new JPanel();
        controlPanel.add(Box.createRigidArea(new Dimension(0,50)));
        date.add(yearBox);
        date.add(monthBox);
        controlPanel.add(date);
        controlPanel.add(Box.createRigidArea(new Dimension(0,100)));
        controlPanel.add(returnButton);
        setGbcXy(1,1);
        setGbcSize(1,2);
        setGbcWeight(0.1,0);
        gbc.anchor=GridBagConstraints.NORTHWEST;
        layout.setConstraints(controlPanel,gbc);
        add(controlPanel);
    }

    public void setDataPanel(ArrayList<Category>dataList,JPanel tab){
        JPanel dataPanel=new JPanel();
        int check=2;
        int sum=0;
        dataPanel.setLayout(new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS));
        for(Category data:dataList){
            dataPanel.add(new JLabel(data.getName()+":￥"+data.getCost()));
            check++;
            sum+=data.getCost();
        }
        dataPanel.add(new JLabel("---------------------"));
        dataPanel.add(new JLabel("総支出:￥"+sum));
        for(int i=0;i<check;i++) {
            dataPanel.getComponent(i).setFont(new Font(null, Font.BOLD, 15));
        }
        tab.add(dataPanel);
    }

    public void setScrollPane(){
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.getViewport().setBackground(Color.lightGray);
        DefaultTableModel model =new DefaultTableModel(){
            public Class<?> getColumnClass(int column){
                switch(column){
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;
                    default:
                        return String.class;
                }
            }
        };
        JTable table=new JTable();
        table.setRowHeight(30);
        table.setFont(new Font(null,Font.PLAIN,20));
        table.getTableHeader().setFont(new Font(null,Font.BOLD,20));
        scrollPane.setViewportView(table);
        table.setModel(model);

        model.addColumn("日付");
        model.addColumn("大分類");
        model.addColumn("小分類");
        model.addColumn("費用");
        model.addColumn("備考");

        ArrayList<Product> products= ProductProcess.fetchMonthProduct(mf.getUser().getId(), mf.getCalenderPanel().getLocalDate());
        int i=0;
        //index: table=product=pidListであるため、それを参照する
        for(Product product:products){
            model.addRow(new Object[0]);
            model.setValueAt(product.getBuyDate(),i,0);
            model.setValueAt(product.getCategoryName(),i,1);
            model.setValueAt(product.getSubcategoryName(),i,2);
            model.setValueAt("￥"+product.getCost(),i,3);
            model.setValueAt(product.getMessage(),i,4);
            i++;
        }
        setGbcXy(2,2);
        setGbcSize(1,1);
        setGbcWeight(1,0.1);
        gbc.anchor=GridBagConstraints.NORTH;
        layout.setConstraints(scrollPane,gbc);
        add(scrollPane);

    }

    public void setSubMonthGraph(int cId){
        int sum=0;
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        DefaultPieDataset data=new DefaultPieDataset();
        ArrayList<Category>categoryList=CalculateProcess.fetchSubCategoryCostMonth(mf.getUser().getId(),mf.getCalenderPanel().getLocalDate(),cId);
        for(Category category:categoryList){
            sum+=category.getCost();
        }
        for(Category category:categoryList){
            data.setValue(category.getName(),new Double(category.getCost()*100/sum));
        }

        JFreeChart monthGraph= ChartFactory.createPieChart(
                mf.getCalenderPanel().getLocalDate().getMonthValue()+"月の小分類別円グラフ",
                data,
                true,
                true,
                false
        );
        ChartPanel subPanel=new ChartPanel(monthGraph);
        monthGraph.getLegend().setItemFont(new Font(null,Font.PLAIN,20));
        PiePlot piePlot=(PiePlot)monthGraph.getPlot();
        piePlot.setLabelFont(new Font(null,Font.PLAIN,20));
        tab2.add(subPanel);
        setDataPanel(categoryList,tab2);
    }

    public void setBarPanel(){
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        DefaultCategoryDataset data=new DefaultCategoryDataset();
        ArrayList<Category>BarData;
        JPanel result=new JPanel();
        result.setLayout(new BoxLayout(result,BoxLayout.PAGE_AXIS));
        int sum;
        int sum2=0;
        LocalDate date=mf.getCalenderPanel().getLocalDate();
        for(int i=1;i<=12;i++) {
            sum=0;
            BarData = CalculateProcess.fetchCategoryCostMonth(mf.getUser().getId(), date.withMonth(i));
            for (Category category : BarData) {
                data.addValue(category.getCost(), category.getName(), i + "月");
                sum+=category.getCost();
            }
            sum2+=sum;
            result.add(new JLabel(i+"月の総支出: ￥"+sum));
            if(BarData.isEmpty())
                data.addValue(0,"no data",i+"月");
        }
        result.add(new JLabel("-----------------------------"));
        result.add(new JLabel("年間総支出: ￥"+sum2));
        for(int i=0;i<14;i++){
            result.getComponent(i).setFont(new Font(null,Font.BOLD,15));
        }
        JFreeChart chart=ChartFactory.createStackedBarChart(
                mf.getCalenderPanel().getLocalDate().getYear()+"年",
                "月",
                "支出（円）",
                data,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        ChartPanel BarPanel=new ChartPanel(chart);
        chart.getLegend().setItemFont(new Font(null,Font.PLAIN,20));
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(new Font(null,Font.BOLD,15));
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(new Font(null,Font.BOLD,15));

        tab3.add(BarPanel);
        tab3.add(result);
    }

    public void reloadPanel(){
        this.removeAll();
        this.setControlPanel();
        this.setTab();
        this.revalidate();
        //mf.pack();
        mf.getCardLayout().show(mf.getNowPanel(),"graph");
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

}
