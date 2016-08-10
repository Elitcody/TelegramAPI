package me.Elitcody;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;

public class Gui {
	public void addTxt(final JTextArea txt){
		new Thread(new Runnable() {
			
			public void run() {
				while(true)
				{
					for(int x = 0; x < cmdhandler.getLastMessages().size();x++){
						if(!txt.getText().contains(cmdhandler.getLastMessages().get(x))){
							txt.append("\nUtente: "+cmdhandler.getLastMessages().get(x));
							txt.update(txt.getGraphics());
						}						
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	
		}).start();
	}
	
	public void runApplet(){
		JFrame frame = new JFrame("Chat");
		
		JPanel panel = new JPanel();
		
		panel.setLayout(null);

		JLabel label = new JLabel("");

		final JTextArea txt = new JTextArea();
		
		txt.setBounds(40,20,640,350);
		
		txt.setFont(new Font("Serif", Font.BOLD, 15));
	
		txt.setForeground(Color.GREEN);
		
		txt.setBackground(Color.BLACK);
        
		final JTextField sendMSG = new JTextField(50);
		
		sendMSG.addActionListener(new ActionListener(){

			public void actionPerformed(java.awt.event.ActionEvent e) {
				final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
				int chatId = CHATID;
				
				main.sendMsg(bot, chatId, sendMSG.getText());
				txt.append("\nTu: "+sendMSG.getText());
				
				sendMSG.setText("");
			}});
		
		addTxt(txt);
		sendMSG.setBounds(40,370,640,30);
		txt.setEditable(false);
		
		JScrollPane jp = new JScrollPane (txt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 

		txt.setLineWrap(true);
		
		jp.setBounds(685, 20, 20, 350);
		panel.add(txt);
		panel.add(label);
		panel.add(sendMSG);
		

		frame.getContentPane().add(jp);
		frame.add(panel);
		frame.setSize(720, 480);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	
	
	
	}
}
