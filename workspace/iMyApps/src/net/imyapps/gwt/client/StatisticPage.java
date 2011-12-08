package net.imyapps.gwt.client;

import net.imyapps.common.Statistic;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatisticPage {
	static StatisticPage instance;

	Panel mainPanel;
	
	public static void destroy() {
		if (instance != null && instance.mainPanel != null)
			instance.mainPanel.clear();
		
		instance = null;
	}
	
	public static void switchTo(Panels panels) {
		if (instance == null) {
			instance = new StatisticPage();
		}
		
		panels.getMain().clear();
		panels.getMain().add(instance.mainPanel);
		instance.renew();
	}

	public StatisticPage() {
		initPage();
	}
	
	public void initPage() {
		mainPanel = new VerticalPanel();
		
		IMyApps.statisticResource.getClientResource().setReference(API.statisticAppItem(0, -1));
		IMyApps.statisticResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
				Preference<MediaType>(MediaType.APPLICATION_JSON));
		
		renew();
	}

	private void renew() {
		mainPanel.clear();
		mainPanel.add(new HTML(IMyApps.msgs.msg_loading()));
		final FlexTable flexTable1 = new FlexTable();
		flexTable1.setStyleName("statisticTable");
		final FlexTable flexTable2 = new FlexTable();
		flexTable2.setStyleName("statisticTable");
		
		IMyApps.statisticResource.retrieve(new Result<Statistic>() {

			public void onFailure(Throwable caught) {
				// Handle the error
				Window.alert(caught.getMessage());
			}

			private String persent(int v1, int v2) {
				return String.valueOf((float) ((int) ((double) v1 / v2 * 10000)) / 100);
			}
			
			private String encode(String msg) {
				try {
					return URL.encode(msg.replace("&", "%26"));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return msg;
			}
			
			public void onSuccess(Statistic result) {
				Image image1, image2;
				String url1, url2="";
				
				url1 = "http://chart.apis.google.com/chart" +
					   "?chs=400x250" +
					   "&chxs=0,676767,20" +
					   "&cht=p";
					   //"&chdlp=b" +
					   //"&chtt=" + encode(IMyApps.msgs.lb_totalcount());
				
				url2 = "http://chart.apis.google.com/chart" +
				   "?chs=400x250" +
				   "&chxs=0,676767,20" +
				   "&cht=p";
				   //"&chdlp=b" +
				   //"&chtt=" + encode(IMyApps.msgs.lb_totalprice());
				
				int size = result.getByGenre().size();
				int compCount = size > 10 ? 10 : size-1;
				if (result.getByGenre() != null && size > 0) {
					// count
					String d="&chd=t:";
					String t="&chl=";
					//String dl="&chdl=";
					
					// 只列出前幾名
					int values[] = new int[size];
					String keys[] = new String[size];
					int other = 0;
					int cnt=0;
					for (String key : result.getByGenre().keySet()) {
						int value = result.getByGenre().get(key).getCount();
						int i=0;
						for (; i<cnt; i++) {
							if (value >= values[i])
								break;
						}
						for (int j=cnt; j>i; j--) {
							values[j] = values[j-1];
							keys[j] = keys[j-1];
						}
						values[i] = value;
						keys[i] = key;
						cnt++;
					}
					
					int total = 0;
					for (int i=0; i<compCount; i++) {
						//dl = dl + encode(keys[i]) + "(" + values[i] + ")" + "|";
						String v = persent(values[i], result.getTotal().getCount());
						d = d + v + ",";
						t = t + encode(keys[i] + " " + v + "%|");
						total += values[i];
					}
					other = result.getTotal().getCount() - total;
					//dl = dl + encode("others") + "(" + other + ")";
					String v = persent(other, result.getTotal().getCount());
					d = d + v;
					t = t + encode("others " + v + "%");
					
					cnt = 0;
					for (String key : result.getByGenre().keySet()) {
						int value = result.getByGenre().get(key).getCount();
						int row = (cnt/4)*2 + 2;
						int col = cnt%4;
						flexTable1.setText(row, col, key);
						flexTable1.setText(row+1, col, " " + value + " (" +
								persent(value, result.getTotal().getCount()) + "%)");
						flexTable1.getCellFormatter().setStyleName(row, col, "statisticTable-EvenRow");
						flexTable1.getCellFormatter().setStyleName(row+1, col, "statisticTable-OddRow");
						cnt++;
					}
					
					url1 = url1 + d + t;
					
					// amount
					d="&chd=t:";
					t="&chl=";
					//dl = "&chdl=";
					
					// 只列出前幾名
					values = new int[size];
					keys = new String[size];
					other = 0;
					cnt = 0;
					for (String key : result.getByGenre().keySet()) {
						int value = result.getByGenre().get(key).getAmount();
						int i=0;
						for (; i<cnt; i++) {
							if (value >= values[i])
								break;
						}
						for (int j=cnt; j>i; j--) {
							values[j] = values[j-1];
							keys[j] = keys[j-1];
						}
						values[i] = value;
						keys[i] = key;
						cnt++;
					}
					
					total=0;
					for (int i=0; i<compCount; i++) {
						float fvalue = (float)values[i] / 1000;
						//dl = dl + encode(keys[i] + "($" + fvalue + ")") + "|";
						v = persent(values[i], result.getTotal().getAmount());
						d = d + v + ",";
						t = t + encode(keys[i] + " " + v + "%|");
						total += values[i];
					}
					other = result.getTotal().getAmount() - total;
					//dl = dl + encode("others") + "(" + ((float)other/1000) + ")";
					v = persent(other, result.getTotal().getAmount());
					d = d + v;
					t = t + encode("others " + v + "%");
					
					cnt = 0;
					for (String key : result.getByGenre().keySet()) {
						int value = result.getByGenre().get(key).getAmount();
						float fvalue = (float)value / 1000;
						int row = (cnt/4)*2 + 2;
						int col = (cnt%4);
						flexTable2.setText(row, col, key);
						flexTable2.setText(row+1, col, 
								"$" + fvalue + " (" + 
								persent(value, result.getTotal().getAmount()) + "%)");
						flexTable2.getCellFormatter().setStyleName(row, col, "statisticTable-EvenRow");
						flexTable2.getCellFormatter().setStyleName(row+1, col, "statisticTable-OddRow");
						cnt++;
					}
					
					url2 = url2 + d + t;
				}

				image1 = new Image(url1);
				image2 = new Image(url2);

				HTML html1 = new HTML(IMyApps.msgs.lb_totalCount() + ": " + 
									result.getTotal().getCount());
				HTML html2 = new HTML(IMyApps.msgs.lb_paidCount() + ": "+
						 			result.getPaid().getCount());
				HTML html3 = new HTML(IMyApps.msgs.lb_totalPrice() + ": $"+
			 			(float) result.getTotal().getAmount() / 1000);
				
				mainPanel.clear();
				mainPanel.add(html1);
				mainPanel.add(html2);
				mainPanel.add(html3);
				mainPanel.add(new HTML("<br /><br />"));
				//mainPanel.add(image1);
				flexTable1.getFlexCellFormatter().setColSpan(0, 0, 4);
				flexTable1.setWidget(0, 0, new HTML(IMyApps.msgs.lb_totalCount()));
				flexTable1.getFlexCellFormatter().setColSpan(1, 0, 4);
				flexTable1.setWidget(1, 0, image1);
				mainPanel.add(flexTable1);
				mainPanel.add(new HTML("<br /><br />"));
				//mainPanel.add(image2);
				flexTable2.getFlexCellFormatter().setColSpan(0, 0, 4);
				flexTable2.setWidget(0, 0, new HTML(IMyApps.msgs.lb_totalPrice()));
				flexTable2.getFlexCellFormatter().setColSpan(1, 0, 4);
				flexTable2.setWidget(1, 0, image2);
				mainPanel.add(flexTable2);
			}
			
		});
	}
}
