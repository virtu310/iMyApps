package net.imyapps.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {
	private static final long serialVersionUID = -4943620971337875818L;
	
	JLabel lbName;
	JTextField txName;
	JLabel lbPass;
	JPasswordField pwPass;
	JButton bnOk;
	JButton bnCancel;
	JLabel lbRegister;
	boolean bLogin = false;
	
	public void init() {
		setResizable(false);
		setSize(400, 200);
		setTitle(IpaChecker.msg.getString("title"));
		setLocation(300, 300);
		
		/*
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				dispose();
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		*/
		
		lbPass = new JLabel(IpaChecker.msg.getString("password"));
		lbName = new JLabel(IpaChecker.msg.getString("name"));
		txName = new JTextField("", 24);
		pwPass = new JPasswordField("", 24);
		bnOk = new JButton(IpaChecker.msg.getString("ok"));
		bnCancel = new JButton(IpaChecker.msg.getString("cancel"));
		lbRegister = new JLabel(IpaChecker.msg.getString("register"));
		lbRegister.setForeground(Color.BLUE);
		
		bnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		bnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txName.getText().length() > 0 && pwPass.getPassword().length > 0) {
					bLogin = true;
					setVisible(false);
					dispose();
				}
			}
		});
		
		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txName.getText().length() > 0 && pwPass.getPassword().length > 0) {
						bLogin = true;
						setVisible(false);
						dispose();
					}
				}
			}
		};
		txName.addKeyListener(keyListener);
		pwPass.addKeyListener(keyListener);
		
		Container c = getContentPane();
        c.setLayout(new GridLayout(5, 1));
        
        JPanel pnName = new JPanel();
        pnName.add(lbName);
        pnName.add(txName);
	    JPanel pnPass  = new JPanel();
	    pnPass.add(lbPass);
	    pnPass.add(pwPass);
	    
	    JPanel pnButton = new JPanel();
	    pnButton.add(bnOk);
	    pnButton.add(bnCancel);
	    
	    JPanel pnRegister = new JPanel();
	    pnRegister.add(lbRegister);
	    lbRegister.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				String url = "http://imyapps.appspot.com?page=signup";
				Desktop desktop = java.awt.Desktop.getDesktop();
				if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
					IpaChecker.showMessage(url);
				}
				else {
					java.net.URI uri;
					try {
						uri = new java.net.URI(url);
		                desktop.browse(uri);
					} catch (Exception e1) {
						IpaChecker.showMessage(url);
					}
				}
			}
		});
	    
	    c.add(new JLabel(IpaChecker.msg.getString("login")));
	    c.add(pnName);
	    c.add(pnPass);
	    c.add(pnButton);
	    c.add(pnRegister);
	}

	@Override
	public void setLocale(Locale locale){
		IpaChecker.msg = ResourceBundle.getBundle("net.imyapps.client.IpaChecker", locale);
	}
	
	public boolean login() {
		setModal(true);
		setVisible(true);
		return bLogin;
	}
	
	public String getName() {
		return txName.getText();
	}
	
	public String getPassword() {
		return new String(pwPass.getPassword());
	}
}
