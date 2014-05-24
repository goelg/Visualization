package org.goelg.visualization;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

import javafx.scene.chart.XYChart;

public class Charts {
	private XYChart.Series<String, Number> dataSeries;

	public Charts() {
		dataSeries = new XYChart.Series<String, Number>();
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public XYChart.Series<String, Number> generateSeries(
			TreeMap<LocalDateTime, Integer> chartValues, AggregationLevel level) {
		
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
	public void generateMinuteSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();

			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getHour() + " Hrs"
							+ time.getMinute() + " Mins", value));
		}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateHourSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getDayOfMonth()+ " "
							+ time.getHour()+ " HRS", value));
		}

	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateDaySeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();			
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getMonth().toString()+"/" + time.getDayOfMonth(), value));
		}

	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateMonthSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();			
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getMonth().toString()+"," + time.getYear(), value));
		}

	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void generateYearSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();			
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getYear()+"", value));
		}

	}

}
