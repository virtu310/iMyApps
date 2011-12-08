package net.imyapps.client;

import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.ByteArrayInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.restlet.data.Reference;
import org.restlet.resource.ClientResource;

import com.dd.plist.PropertyListParser;

import net.imyapps.common.Account;
import net.imyapps.common.AppItem;
import net.imyapps.common.AppItemResource;
import net.imyapps.common.AppItems;
import net.imyapps.common.Login;
import net.imyapps.common.LoginResource;
import net.imyapps.utils.FileOperator;
import net.imyapps.utils.IpaXMLParser;

public class IpaChecker {
	static String VERSION = "0.4";
	static boolean devel = false;
	static String default_url = "http://imyapps.appspot.com";

	static ResourceBundle msg;
	static {
		msg = ResourceBundle.getBundle("net.imyapps.client.IpaChecker", Locale.TAIWAN);
		
		if (devel) {
			default_url = "http://localhost:8888";
		}
	}

	class Record {
		String filename;
		long length;
		long lastModified;
	}
	
	ClientResource cr;
	Set<String> buyerFilterSet;
	Map<String, Record> mapRecord = new HashMap<String, Record>();
	String loginname;
	String password;
	SystemTray systemTray;
	long loginTime = 0;
	
	public static void println(String msg) {
		System.out.println(msg);
	}

	//static String s_serials = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=";
	static String s_serials = "HI5y6J}|SD;':\",K9vO0Tl47im+N-={.3oQLYZ" +
					 "a!@*b]\\cBdejk[Af#$&gPChGp<?q^sVnWUXr%1Et>F8uMw()_2/xRz";
	static int s_max = 234;
	static Random s_random = new Random();
	public static String encrypt(String str) {
		try {
			char a[] = str.toCharArray();
			StringBuilder sb = new StringBuilder();
			for (char c : a) {
				int n = s_serials.indexOf(c);
				if (n == -1)
					return str;
				n = n + s_max * Math.abs(s_random.nextInt() % 4);
				if (n < 10)
					sb.append("00").append(n);
				else if (n < 100)
					sb.append('0').append(n);
				else
					sb.append(n);
			}
			return sb.toString();
		} catch (Exception e) {
			return str;
		}
	}
	
	public static String decrypt(String str) {
		int len = str.length();
		if (len % 3 != 0)
			return str;
		
		try {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<len; i+=3) {
				String s = str.substring(i, i+3);
				int n = Integer.parseInt(s) % s_max;
				sb.append(s_serials.charAt(n));
			}
			
			return sb.toString();
		} catch (Exception e) {
			return str;
		}
	}

	public IpaChecker() {
		try {
			if (SystemTray.isSupported()) {
				systemTray = SystemTray.getSystemTray();
				systemTray.add(new TrayIcon(Toolkit.getDefaultToolkit().getImage("icon16.jpg")));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getLoginName() {
		return loginname;
	}

	public void setLoginName(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addBuyerFilter(String buyer) {
		if (buyerFilterSet == null)
			buyerFilterSet = new HashSet<String>();
		
		buyerFilterSet.add(buyer);
	}

	public void loadRecords(File file) throws IOException {
		if (file.exists() == false)
			return;
		
		Properties props = new Properties();
		InputStream in = new FileInputStream(file);
		try {
			props.loadFromXML(in);
		}
		finally {
			in.close();
		}
		
		Iterator<Object> iter = props.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String values[] = props.getProperty(key).split(",");
			if (values.length != 2)
				continue;
			Record record = new Record();
			record.filename = key;
			record.length = Long.parseLong(values[0]);
			record.lastModified = Long.parseLong(values[1]);
			mapRecord.put(key, record);
		}
	}
	
	public void saveRecords(File file) throws IOException {
		if (file.exists())
			file.delete();
		
		Properties props = new Properties();
		Iterator<Record> iter = mapRecord.values().iterator();
		while (iter.hasNext()) {
			Record record = iter.next();
			props.setProperty(record.filename, 
					String.valueOf(record.length) + "," +
					String.valueOf(record.lastModified));
		}
		
		OutputStream out = new FileOutputStream(file);
		try {
			props.storeToXML(out, "iMyApps Records");
		}
		finally {
			out.close();
		}
	}

	public void copyInputStream(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int len;

		while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}
	
	public int sendItem(List<AppItem> appItems) {
		if (appItems.size() == 0)
			return 0;
		
		int count = 0;
		if (doLogin() == false) {
			println("Login fail!");
			System.exit(1);
		}
		
		cr.setReference(new Reference(default_url + "/API/appitem"));
		AppItemResource resource = cr.wrap(AppItemResource.class);
		
		for (int i=0; i<appItems.size();) {
			int sendCount = 
				appItems.size() - i >= 20 ? 20 : appItems.size() - i;
			
			ArrayList<AppItem> tmpItems = new ArrayList<AppItem>();
			for (int j=0; j<sendCount; j++) {
				tmpItems.add(appItems.get(i++));
				count++;
			}
			AppItems sendItems = new AppItems();
			sendItems.setAppItems(tmpItems.toArray(new AppItem[tmpItems.size()]));
			String respString[] = resource.add(sendItems);
			
			for (String resp : respString) {
				println(resp);
			}
		}
		
		cr.release();
		
		return count;
	}

	public boolean doLogin() {
		if (cr != null &&
			(loginTime != 0 &&
			System.currentTimeMillis() - loginTime < 300000))
			return true;
		
		cr = new ClientResource(default_url + "/API/login");
		LoginResource loginResource = cr.wrap(LoginResource.class);
		
		Account account;
		if (true) {
			Login login = new Login();
			login.setLoginName(getLoginName());
			login.setPassword(getPassword());
			account = loginResource.login2(login);
		}
		else {
			cr.setReference(new Reference(default_url + "/API/login?l=" + 
									  getLoginName() + "&p=" +
									  getPassword()));
			account = loginResource.login();
		}
		
		if (account == null) {
			println(getLoginName() + " login failed!");
			return false;
		}
		
		println(account.getLoginName() + " logined!");
		loginTime = System.currentTimeMillis();
		
		return true;
	}
	
	public int startUpdate(File folder, String storeFolder) {
		String files[] = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".ipa");
			}
		});
		TrayIcon ti = null;
		
		long starttm = System.currentTimeMillis();
		if (systemTray != null) {
			ti = systemTray.getTrayIcons()[0];
			ti.displayMessage(msg.getString("title"), msg.getString("loadipa"), TrayIcon.MessageType.INFO);
			ti.setToolTip(msg.getString("loadipa"));
		}
		
		List<AppItem> appItems = new ArrayList<AppItem>();
		ArrayList<File> failList = new ArrayList<File>();
		for (String name : files) {
			File file = new File(folder.getAbsolutePath() + File.separatorChar + name);
			if (file.isFile() == false) {
				continue;
			}
			
if (devel && file.getName().startsWith("A"))
	continue;
			
			/* check for already updated */
			Record record = mapRecord.get(name);
			if (record != null) {
				if (file.length() == record.length &&
					file.lastModified() == record.lastModified) {
					continue;
				}
			}
			
			ZipEntry entry = null;
			boolean successful = true;
			ZipFile zipFile = null;
			try {
				println("Scan " + file.getAbsolutePath());
				AppItem appItem = null;
				
				if (name.endsWith(".plist")) {
					IpaXMLParser parser = new IpaXMLParser();
					try {
						parser.parse(new FileInputStream(file));
						appItem = parser.getItems().get(0);
					}
					catch (Exception e) {
						appItem = getFromBinaryPlist(parser, new FileInputStream(file));
					}
				}
				else {
					zipFile = new ZipFile(file);
					Enumeration entries = zipFile.entries();
					
					while (entries.hasMoreElements()) {
						entry = (ZipEntry)entries.nextElement();
						
						if (entry.isDirectory()) {
							continue;
						}
						
						if (entry.getName().equals("iTunesMetadata.plist")) {
							IpaXMLParser parser = new IpaXMLParser();
							try {
								parser.parse(zipFile.getInputStream(entry));
								appItem = parser.getItems().get(0);
							}
							catch (Exception e) {
								appItem = getFromBinaryPlist(parser, 
											zipFile.getInputStream(entry));
							}
							
							break;
						}
					}
				}
				
				if (appItem != null) {
					if (buyerFilterSet == null || buyerFilterSet.size() == 0 ||
							buyerFilterSet.contains(appItem.getBuyerId())) {
							appItems.add(appItem);
						}
if (devel && appItem.getPlaylistName().indexOf("c") > 0)					
	appItem.setNote("This is " + appItem.getPlaylistName());
					/* update record */
					if (record == null) {
						record = new Record();
						record.filename = name;
						mapRecord.put(name, record);
					}
					record.length = file.length();
					record.lastModified = file.lastModified();
					
					if (storeFolder != null) {
						File storeFile = new File(storeFolder + 
												  File.separatorChar + 
												  FileOperator.getFilename(file.getName()) +
												  '.' + "plist");
						if (storeFile.exists())
							storeFile.delete();
						long size = FileOperator.copy(zipFile.getInputStream(entry), 
										  new FileOutputStream(storeFile));
						if (size != entry.getSize()) {
							println("Store " + storeFile.getAbsolutePath() + " failed!");
						}
					}
				}
				
			} catch (ZipException e) {
				e.printStackTrace();
				println("Parse " + file.getAbsolutePath() + " failed!");
				successful = false;
			} catch (IOException e) {
				e.printStackTrace();
				println("Parse " + file.getAbsolutePath() + " failed!");
				successful = false;
			} catch (Exception e) {
				e.printStackTrace();
				println("Parse " + file.getAbsolutePath() + " failed!");
				successful = false;
			}
			finally {
				if (zipFile != null) {
					try {
						zipFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			if (successful == false)
				failList.add(file);
		}
		
		if (ti != null) {
			String toolTip = msg.getString("updating") + " (" + appItems.size() + ")";
			ti.displayMessage(msg.getString("title"), 
					toolTip, 
					TrayIcon.MessageType.INFO);
			ti.setToolTip(toolTip);
		}
		
		int count = sendItem(appItems);
		
		println("Total: " + files.length);
		println("Updated: " + count);
		
		for (File f : failList) {
			println("Fail: " + f.getAbsolutePath());
		}
		
		if (ti != null) {
			String toolTip = msg.getString("finished") + " (" + count + "/" + appItems.size() + ")";
			ti.displayMessage(msg.getString("title"), 
					toolTip, 
					TrayIcon.MessageType.INFO);
			ti.setToolTip(toolTip);
			
			long t = System.currentTimeMillis() - starttm;
			if (t < 2500) {
				try {
					Thread.sleep(2500 - t);
				} catch (InterruptedException e) {
				}
			}
		}
		
		return count;
	}
	
	private AppItem getFromBinaryPlist(IpaXMLParser parser, InputStream input) throws Exception {
		/* convert binary_plist to xml_plist */
		File tempBin = File.createTempFile("binary_", ".plist");
		File tempXml = File.createTempFile("xml_", ".plist");
		
		try {
			copyInputStream(input, new FileOutputStream(tempBin));
			
			PropertyListParser.convertToXml(tempBin, tempXml);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			copyInputStream(new FileInputStream(tempXml), out);
			ByteArrayInputStream in = new ByteArrayInputStream(
					out.toString("UTF-8").replaceAll("&", "&amp;").
					getBytes("UTF-8"));
			
			parser.parse(in);
			return parser.getItems().get(0);
		}
		finally {
			tempBin.delete();
			tempXml.delete();
		}
	}

	public void saveUserPass(File passFile) {
		try {
			FileWriter writer = new FileWriter(passFile);
			writer.append(encrypt(getLoginName())).append("\n").
				   append(encrypt(getPassword())).append("\n");
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean loadUserPass(File passFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(passFile));
			String loginname = reader.readLine();
			String password = reader.readLine();
			reader.close();
			setLoginName(decrypt(loginname));
			setPassword(decrypt(password));
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * popup loginname & password input dialog
	 * @return
	 */
	public boolean askUserPass() {
		LoginDialog loginDialog = new LoginDialog();
		loginDialog.init();
		if (loginDialog.login()) {
			setLoginName(loginDialog.getName());
			setPassword(loginDialog.getPassword());
			if (doLogin() == false) {
			    showMessage(msg.getString("loginfail"));
			    return false;
			}
			
			return true;
		}
		else {
			return false;
		}
	}

	static public void showMessage(String message) {
		JOptionPane pane = new JOptionPane(message);
		JDialog dialog = pane.createDialog(new JFrame(), msg.getString("message"));
		dialog.setModal(true);
		dialog.setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
		IpaChecker ipa = new IpaChecker();
		int count;
		String apppath = null;
		String configpath = null;
		String storePath = null;
		boolean skipReport = false;
		boolean forceUpdate = false;
		long delay = -1;
		boolean skipLoginDialog = false;
		
		for (int i=0; i<args.length; i++) {
			String arg = args[i].toLowerCase();
			if (arg.equals("-b") || arg.equals("--buyer")) {
				String buyerStr = args[++i];
				String buyers[] = buyerStr.split(",");
				for (String buyer : buyers) {
					ipa.addBuyerFilter(buyer.trim());
				}
			}
			else if (arg.equals("-a") || arg.equals("--apppath")) {
				apppath = args[++i];
			}
			else if (arg.equals("-d") || arg.equals("--delay")) {
				delay = Integer.parseInt(args[++i]);
			}
			else if (arg.equals("-c") || arg.equals("--configpath")) {
				configpath = args[++i];
			}
			else if (arg.equals("-s") || arg.equals("--storepath")) {
				storePath = args[++i];
			}
			else if (arg.equals("-u") || arg.equals("--forceupdate")) {
				forceUpdate = true;
			}
			else if (arg.equals("-k") || arg.equals("--skipreport")) {
				skipReport = true;
			}
			else if (arg.equals("-l") || arg.equals("--loginname")) {
				ipa.setLoginName(args[++i]);
			}
			else if (arg.equals("-p") || arg.equals("--password")) {
				ipa.setPassword(args[++i]);
			}
			else if (arg.equals("-o") || arg.equals("--skiplogindialog")) {
				skipLoginDialog = true;
			}
			else if (arg.equals("-?") || arg.equals("-h") || arg.equals("--help")) {
				StringBuilder sb = new StringBuilder();
				sb.append("iMyAppsClient v").append(VERSION);
				sb.append("    (c)2011 iMyApps\n");
				sb.append("Usage: iMyAppsClient -o -s D:\\iPhoneApps\n");
				sb.append("    --buyer [buyer]                 (-b)\n");
				sb.append("    --apppath [app path]            (-a)\n");
				sb.append("    --delay [delay time]            (-d)\n");
				sb.append("    --configpath [configure path]   (-c)\n");
				sb.append("    --storepath [store path]        (-s)\n");
				sb.append("    --forceupdate [force update]    (-u)\n");
				sb.append("    --skipreport [skip report]      (-k)\n");
				sb.append("    --loginname [loginname]         (-l)\n");
				sb.append("    --password [password]           (-p)\n");
				sb.append("    --skiplogindialog               (-o)\n");
				sb.append("    --help                          (-?,-h)\n");
				showMessage(sb.toString());
				println(sb.toString());
				System.exit(0);
			}
		}
		
		try {
			if (configpath == null) {
				String localpath = System.getenv("LOCALAPPDATA");
				if (localpath == null || localpath.length() == 0) {
					localpath = System.getProperty("user.home");
					if (localpath == null || localpath.length() == 0) {
						localpath = System.getProperty("user.dir");
					}
				}
				if (localpath == null || localpath.length() == 0) {
					configpath = "iMyApps";
				}
				else {
					configpath = localpath + File.separatorChar + "iMyApps";
				}
			}
			
			// 載入 user/pass
			File userPassFile = new File(configpath + File.separatorChar + "login.dat");
			if (ipa.getLoginName() == null || ipa.getPassword() == null) {
				if (skipLoginDialog == true && userPassFile.isFile()) {
					if (ipa.loadUserPass(userPassFile) == false) {
						println("Please specified loginname and password.");
						showMessage("Please specified loginname and password.");
						System.exit(1);
					}
				}
				else {
					if (ipa.askUserPass() == false) {
						System.exit(1);
					}
					ipa.saveUserPass(userPassFile);
				}
			}
			
			// 載入設定檔
			File configFile = new File(configpath + File.separatorChar + "config.xml");
			if (configFile.isFile()) {
				Properties props = new Properties();
				props.loadFromXML(new FileInputStream(configFile));
				if (apppath == null && props.containsKey("apppath"))
					apppath = props.getProperty("apppath");
				
				if (delay == -1 && props.containsKey("delay"))
					delay = Integer.parseInt(props.getProperty("delay"));
				
				if (storePath == null && props.containsKey("storePath"))
					storePath = props.getProperty("storePath");
				
				if (props.containsKey("buyer")) {
					String buyerStr = props.getProperty("buyer");
					String buyers[] = buyerStr.split(",");
					for (String buyer : buyers) {
						ipa.addBuyerFilter(buyer.trim());
					}
				}
			}
			
			// 確認 user/pass
			if (ipa.getLoginName() == null || ipa.getLoginName().length() == 0 ||
				ipa.getPassword() == null || ipa.getPassword().length() == 0) {
				println("Please specified loginname and password.");
				showMessage("Please specified loginname and password.");
				System.exit(1);
			}
			
			/* setup default apppath if not set */
			if (apppath == null) {
				String userhome = System.getenv("USERPROFILE");
				apppath = userhome + File.separatorChar + "Music" +
						File.separatorChar + "iTunes" + 
						File.separatorChar + "iTunes Media" + 
						File.separatorChar + "Mobile Applications";
			}
			
			File configPathFile = new File(configpath);
			if (configPathFile.isDirectory() == false) {
				if (configPathFile.mkdirs() == false) {
					println("Create \"" + configpath + "\" folder failed!");
					showMessage("Create \"" + configpath + "\" folder failed!");
					System.exit(1);
				}
			}
			
			File appPathFile = new File(apppath);
			if (appPathFile.isDirectory() == false) {
				// 讓用戶選擇 iTunes Media 資料夾
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle(msg.getString("selectfolder"));
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	appPathFile = chooser.getSelectedFile();
			    	if (appPathFile.isDirectory() == false) {
						println("\"" + apppath + "\" is not a directory!");
						showMessage("\"" + apppath + "\" is not a directory!");
						System.exit(1);
			    	}
			    	
			    	// 儲存設定檔
			    	Properties props = new Properties();
			    	if (configFile.isFile())
			    		props.loadFromXML(new FileInputStream(configFile));
					props.setProperty("apppath", appPathFile.getAbsolutePath());
					props.storeToXML(new FileOutputStream(configFile), "iMyApps Config");
			    }
			}
			
			/* force update? */
			File recordsFile = new File(configpath + File.separatorChar + "records-" + ipa.getLoginName() + ".dat");
			if (forceUpdate && recordsFile.exists()) {
				if (recordsFile.delete() == false) {
					println("Delete \"" + recordsFile.getAbsolutePath() + "\" failed!");
					showMessage("Delete \"" + recordsFile.getAbsolutePath() + "\" failed!");
					System.exit(1);
				}
			}
			
			if (delay > 0) {
				Thread.sleep(delay*1000);
			}
			
			/* load records */
			ipa.loadRecords(recordsFile);
			
			/* start to update records */
			count = ipa.startUpdate(appPathFile, storePath);
			if (count > 0) {
				/* store update_rec */
				File tempFile = new File(configpath + File.separatorChar + "records.dat.tmp");
				ipa.saveRecords(tempFile);
				recordsFile.delete();
				tempFile.renameTo(recordsFile);
			}
			
			if (skipReport == false) {
				showMessage(msg.getString("report") + count + msg.getString("record"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			showMessage(e.getMessage());
		}
		
		System.exit(0);
	}
}
