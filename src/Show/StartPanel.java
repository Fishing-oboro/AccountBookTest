package Show;

import Main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Process.StartProcess;

import static Process.StartProcess.checkUser;
import static Process.StartProcess.makeUser;

public class StartPanel extends JPanel {
    private MainFrame mf;
    private JLabel title;
    private JTextField name;
    private JPasswordField password;
    private GridBagLayout layout;
    private GridBagConstraints gbc;

    public StartPanel(MainFrame mf){
        this.mf=mf;
        layout=new GridBagLayout();
        setLayout(layout);
        gbc=new GridBagConstraints();

        title=new JLabel("家計簿");
        title.setFont(new Font(null,Font.PLAIN,80));
        setGbcXy(1,1);
        setGbcSize(2,1);
        setGbcWeight(0,0.4);
        layout.setConstraints(title,gbc);
        add(title);

        setName();
        setPassword();
        setOkButton();
        setNewAccount();
    }

    public void setName(){
        JPanel namePanel=new JPanel();
        name=new JTextField();
        name.setColumns(12);
        name.setFont(new Font(null,Font.PLAIN,20));
        JLabel nameLabel=new JLabel("　ユーザ名:");
        nameLabel.setFont(new Font(null,Font.BOLD,20));
        namePanel.add(nameLabel);
        namePanel.add(name);
        setGbcXy(1,2);
        setGbcSize(2,1);
        setGbcWeight(0,0.01);
        gbc.anchor=GridBagConstraints.NORTH;
        layout.setConstraints(namePanel,gbc);
        add(namePanel);
    }

    public void setPassword(){
        JPanel passwordPanel=new JPanel();
        this.password=new JPasswordField();
        this.password.setColumns(12);
        this.password.setFont(new Font(null,Font.PLAIN,20));
        JLabel passwordLabel=new JLabel("パスワード:");
        passwordLabel.setFont(new Font(null,Font.BOLD,20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);
        setGbcXy(1,3);
        setGbcSize(2,1);
        setGbcWeight(0,0.03);
        layout.setConstraints(passwordPanel,gbc);
        add(passwordPanel);
    }

    public void setOkButton() {
        JButton okButton=new JButton("OK");
        okButton.setFont(new Font(null,Font.BOLD,15));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkUser(getUserName(),getPassword())){
                    //ユーザを設定・カレンダーパネルの読み込み
                    mf.setUser(StartProcess.fetchUser(getUserName(),getPassword()));
                    mf.getCalenderPanel().reloadPanel();
                }else{
                    //エラーメッセージ
                    JLabel message=new JLabel("入力情報が正しくありません。");
                    message.setFont(new Font(null,Font.PLAIN,15));
                    JOptionPane.showMessageDialog(
                            mf,
                            message,
                            "message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        setGbcXy(1,4);
        setGbcSize(1,1);
        setGbcWeight(0,0.15);
        gbc.anchor=GridBagConstraints.NORTHEAST;
        layout.setConstraints(okButton,gbc);
        add(okButton);
    }

    public void setNewAccount(){
        JButton newAccount=new JButton("新規作成");
        newAccount.setFont(new Font(null,Font.BOLD,15));
        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inputForm=new JPanel();
                JPanel userPanel=new JPanel();
                JPanel passPanel=new JPanel();
                JPanel repassPanel=new JPanel();
                JTextField user=new JTextField("新規ユーザ名");
                user.setColumns(12);
                user.setFont(new Font(null,Font.PLAIN,20));

                JPasswordField pass=new JPasswordField();
                pass.setColumns(12);
                pass.setFont(new Font(null,Font.PLAIN,20));

                JPasswordField repass=new JPasswordField();
                repass.setColumns(12);
                repass.setFont(new Font(null,Font.PLAIN,20));

                userPanel.add(new JLabel("　　　ユーザ名:"));
                userPanel.add(user);
                passPanel.add(new JLabel("　　パスワード:"));
                passPanel.add(pass);
                repassPanel.add(new JLabel("パスワード確認:"));
                repassPanel.add(repass);
                inputForm.add(userPanel);
                inputForm.add(passPanel);
                inputForm.add(repassPanel);
                inputForm.setLayout(new BoxLayout(inputForm,BoxLayout.PAGE_AXIS));

                int result=JOptionPane.showConfirmDialog(
                    mf,
                    inputForm,
                    "新規ユーザ登録画面",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if(result==JOptionPane.OK_OPTION){

                    if(checkUser(user.getText(),String.valueOf(pass.getPassword()))){
                        JOptionPane.showMessageDialog(
                                mf,
                                "既にそのユーザーは存在します。",
                                "message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        if(!String.valueOf(pass.getPassword()).equals(String.valueOf(repass.getPassword()))){
                            JOptionPane.showMessageDialog(
                                    mf,
                                    "パスワードが異なっています。",
                                    "message",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            actionPerformed(e);
                            return;
                        }
                        //作成
                        makeUser(user.getText(),String.valueOf(pass.getPassword()));
                        JOptionPane.showMessageDialog(
                                mf,
                                "ユーザーを作成しました。",
                                "message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        setGbcXy(2,4);
        setGbcSize(1,1);
        layout.setConstraints(newAccount,gbc);
        add(newAccount);
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

    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    public String getUserName() {
        return name.getText();
    }
}

