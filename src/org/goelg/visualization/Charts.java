package org.goelg.visualization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javafx.scene.chart.XYChart;

public class Charts {
	private List<XYChart.Series<String, Number>> dataSeries;

	public Charts() {
		dataSeries = new ArrayList<XYChart.Series<String, Number>>();
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public List<XYChart.Series<String, Number>> generateSeries(
			List<TreeMap<LocalDateTime, Integer>> chartValues, AggregationLevel level) {
		switch (level) {
		case MINUTES:
			generateMinuteSeries(chartValues);
			break;
		case HOUR:
			generateHourSeries(chartValues);
			break;
		case DAY:
			generateDaySeries(chartValues);
			break;
		case WEEK:
			generateDaySeries(chartValues);
			break;
		case MONTH:
			generateMonthSeries(chartValues);
			break;
		case YEAR:
			generateYearSeries(chartValues);
			break;

		default:
			break;
		}
		return dataSeries;

	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateMinuteSeries(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		
		for(TreeMap<LocalDateTime, Integer> chartValue:chartValues)
		{	
			XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
			for (HashMap.Entry<LocalDateTime, Integer> entry : chartValue.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();

			data.getData().add(
					new XYChart.Data<String, Number>(time.getHour() + " Hrs"
							+ time.getMinute() + " Mins", value));
		}
		this.dataSeries.add(data);
	}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateHourSeries(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		
		for(TreeMap<LocalDateTime, Integer> chartValue:chartValues)
		{	
			XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
			for (HashMap.Entry<LocalDateTime, Integer> entry : chartValue.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			
			data.getData().add(
					new XYChart.Data<String, Number>(time.getDayOfMonth()+ " "
							+ time.getHour()+ " HRS", value));
		}
			this.dataSeries.add(data);
	}
	}
	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateDaySeries(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		
		for(TreeMap<LocalDateTime, Integer> chartValue:chartValues)
		{	
			XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
			for (HashMap.Entry<LocalDateTime, Integer> entry : chartValue.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			
			data.getData().add(
					new XYChart.Data<String, Number>(time.getMonth().toString()+"/" + time.getDayOfMonth(), value));
		}
			this.dataSeries.add(data);
		}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateMonthSeries(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		
		for(TreeMap<LocalDateTime, Integer> chartValue:chartValues)
		{	
			XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
			for (HashMap.Entry<LocalDateTime, Integer> entry : chartValue.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			
			data.getData().add(
					new XYChart.Data<String, Number>(time.getMonth().toString()+"," + time.getYear(), value));
		}
			this.dataSeries.add(data);
		}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateYearSeries(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		
		for(TreeMap<LocalDateTime, Integer> chartValue:chartValues)
		{	
			XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
			for (HashMap.Entry<LocalDateTime, Integer> entry : chartValue.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			data.getData().add(
					new XYChart.Data<String, Number>(time.getYear()+"", value));
		}
		this.dataSeries.add(data);
	}

	}

}
