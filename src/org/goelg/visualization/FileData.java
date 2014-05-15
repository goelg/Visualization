package org.goelg.visualization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class FileData {
	TreeMap<LocalDateTime, Integer> chartValues;

	public TreeMap<LocalDateTime, Integer> getChartValues() {
		return chartValues;
	}

	public void setChartValues(TreeMap<LocalDateTime, Integer> chartValues) {
		this.chartValues = chartValues;
	}

	AgreegationLevel level;

	public AgreegationLevel getLevel() {
		return level;
	}

	public void setLevel(AgreegationLevel level) {
		this.level = level;
	}

	private XYChart.Series<String, Number> dataSeries;
	private String fileName;
	private List<File> files;

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileData() {
		this.dataSeries = new Series<String, Number>();
		chartValues = new TreeMap<LocalDateTime, Integer>();
	}

	public XYChart.Series<String, Number> getSeries() {
		return dataSeries;
	}

	public void setSeries(XYChart.Series<String, Number> series) {
		this.dataSeries = series;
	}

	public String getString(int i) {
		String smallStr[] = { "00", "01", "02", "03", "04", "05", "06", "07",
				"08", "09" };
		if (i < 10)
			return smallStr[i];
		else
			return "" + i;
	}


	public void aggMinuteData(LocalDate date, String[] points) {
		int hourOfDay = 0;
		int minutes = 0;
		for (int i = 1; i < 60 * 24; i++, minutes++) {
			if (minutes == 60) {
				hourOfDay++;
				minutes = 0;
			}
			int value;
			if (i < points.length) {
				try

				{

					value = Integer.parseInt(points[i]);
				} catch (NumberFormatException e) {
					value = 0;
				}
			} else
				value = 0;

			LocalTime time = LocalTime.MIDNIGHT;
			time = time.withHour(hourOfDay);
			time = time.withMinute(minutes);
			LocalDateTime dateTime = date.atTime(time);
			System.out.println(dateTime.toString());
			chartValues.put(dateTime, value);

		}
	}

	public void aggHourData(LocalDate date, String[] points) {
		int hourOfDay = 0;
		int minutes = 0;
		int value = 0;

		for (int i = 1; i < 24 * 60; i++, minutes++) {

			if (minutes == 60) {

				hourOfDay++;
				LocalTime time = LocalTime.MIDNIGHT;
				time = time.withHour(hourOfDay);
				//time.withMinute(minutes);
				LocalDateTime dateTime = date.atTime(time);
				chartValues.put(dateTime, value);
				minutes = 0;
				value = 0;

			}
			if (i < points.length) {
				try {

					value += Integer.parseInt(points[i]);
				} catch (NumberFormatException e) {
					value += 0;
				}
			} else
				value += 0;
		}

	}

	public void aggDayData(LocalDate date, String[] points) {
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		LocalDateTime time = date.atStartOfDay();
		chartValues.put(time, value);
	}

	public void aggMonthData(LocalDate date, String[] points) {
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		date = date.withDayOfMonth(1);
		LocalDateTime time = date.atStartOfDay();
		if(chartValues.containsKey(time))
				chartValues.put(time, chartValues.get(time)+value);
		else
			chartValues.put(time, value);
	}
	
	public void collectData(LocalDate fromDate, LocalDate endDate) {
		FileInputStream fin = null;
		BufferedReader br = null;
		chartValues.clear();
		for(File file:files)
		{
		try {
			fin = new FileInputStream(file);
			String line;
			String cvsSplitBy = ",";

			br = new BufferedReader(new InputStreamReader(fin));

			while ((line = br.readLine()) != null) {

				String[] points = line.split(cvsSplitBy);
				LocalDate date;
				
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
				date = LocalDate.parse(points[0],dtf);
				Period difference = Period.between(fromDate, date);
				
				if(difference.isNegative())
					continue;

				difference = Period.between(date, endDate);
				if(difference.isNegative())
					continue;
				
				switch (level) {
				case MINUTES:
					aggMinuteData(date, points);
					break;
				case HOUR:
					aggHourData(date, points);
					break;
				case DAY:
					aggDayData(date, points);
					break;
				case MONTH:
					aggMonthData(date, points);
					break;
				default:
					break;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		}
	}

}