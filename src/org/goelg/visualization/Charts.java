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

	public XYChart.Series<String, Number> generateSeries(
			TreeMap<LocalDateTime, Integer> chartValues, AgreegationLevel level) {
		
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

		default:
			break;
		}
		return dataSeries;

	}

	public void generateMinuteSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();

			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getHour() + " Hrs"
							+ time.getMinute() + " Mins", value));
		}
	}

	public void generateHourSeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getDayOfMonth()+ " "
							+ time.getHour(), value));
		}

	}

	public void generateDaySeries(TreeMap<LocalDateTime, Integer> chartValues) {
		for (HashMap.Entry<LocalDateTime, Integer> entry : chartValues.entrySet()) {
			LocalDateTime time = entry.getKey();			
			int value = entry.getValue();
			dataSeries.getData().add(
					new XYChart.Data<String, Number>(time.getMonth().toString()+" " + time.getDayOfMonth(), value));
		}

	}

}
