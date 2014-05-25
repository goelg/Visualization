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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FileData {
	private List<TreeMap<LocalDateTime, Integer>> chartValues;
	private String fileName;
	private List<File> files;
	private AggregationLevel level;
	

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public List<TreeMap<LocalDateTime, Integer>> getChartValues() {
		return chartValues;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void setChartValues(List<TreeMap<LocalDateTime, Integer>> chartValues) {
		this.chartValues = chartValues;
	}


	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void addChartValues(int index, TreeMap<LocalDateTime, Integer>chartValue) {
		
		if(index<chartValues.size())
		{
			TreeMap<LocalDateTime, Integer> tempValue;
			tempValue = chartValues.get(index);
			chartValue.putAll(tempValue);	
		}
		chartValues.add(index,chartValue);
		
	}

	

	/**
	 * @return the level
	 */
	public AggregationLevel getLevel() {
		return level;
	}

	/**
	 * @param level object  the level object  to set
	 */
	public void setLevel(AggregationLevel level) {
		this.level = level;
	}


	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * @return the File Name 
	 */
	public String getFileName() {
		return fileName;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public FileData() {
		chartValues = new ArrayList<TreeMap<LocalDateTime, Integer>>();//TreeMap<LocalDateTime, Integer>();
	}

	public String getString(int number) {
		String smallStr[] = { "00", "01", "02", "03", "04", "05", "06", "07",
				"08", "09" };
		if (number < 10)
		{
			return smallStr[number];
		}
		else
		{
			return String.valueOf(number);
		}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */

	public TreeMap<LocalDateTime, Integer> aggMinuteData(LocalDate date, String[] points) {
		int hourOfDay = 0;
		int minutes = 0;
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		
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
			} 
			else
			{
				value = 0;
			}

			LocalTime time = LocalTime.MIDNIGHT;
			time = time.withHour(hourOfDay);
			time = time.withMinute(minutes);			
				chartValue.put(date.atTime(time), value);
			
		}
		return chartValue;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer>  aggHourData(LocalDate date, String[] points) {
		int hourOfDay = 0;
		int minutes = 0;
		int value = 0;
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		for (int i = 1; i < 24 * 60; i++, minutes++) {
			if (minutes == 60) {
				hourOfDay++;
				LocalTime time = LocalTime.MIDNIGHT;
				time = time.withHour(hourOfDay);
				chartValue.put(date.atTime(time), value);
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
			{
				value += 0;
			}
		}
		return chartValue;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer> aggDayData(LocalDate date, String[] points) {
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		chartValue.put(date.atStartOfDay(), value);
		return chartValue;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer> aggWeekData(LocalDate date, String[] points) {
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		int dayWeek = date.getDayOfWeek().getValue();

		LocalDateTime time = date.plusDays(8-dayWeek).atStartOfDay();
		if(chartValue.containsKey(time))
		{
			chartValue.put(time, chartValue.get(time)+value);
		}
		else
		{
			chartValue.put(time, value);
		}
		return chartValue;
	}
	
	
	
	
	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer> aggMonthData(LocalDate date, String[] points) {
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}
		}
		LocalDateTime time = date.withDayOfMonth(1).atStartOfDay();
		if(chartValue.containsKey(time))
		{
			chartValue.put(time, chartValue.get(time)+value);
		}
		else
		{
			chartValue.put(time, value);
		}
		return chartValue;
	}


	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer> aggYearData(LocalDate date, String[] points) {
		TreeMap<LocalDateTime, Integer> chartValue = new TreeMap<LocalDateTime, Integer>();
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		
		LocalDateTime time = date.withDayOfYear(1).atStartOfDay();
		if(chartValue.containsKey(time))
		{
			chartValue.put(time, chartValue.get(time)+value);
		}
		else
		{
			chartValue.put(time, value);
		}
		return chartValue;
	}
	
	
	

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void collectData(LocalDate fromDate, LocalDate endDate) {
		FileInputStream finStream = null;
		BufferedReader buffReader = null;
		chartValues.clear();
		int i = 0;
		for(File file:files)
		{
		try {
			finStream = new FileInputStream(file);
			String line;
			String cvsSplitBy = ",";

			buffReader = new BufferedReader(new InputStreamReader(finStream));

			while ((line = buffReader.readLine()) != null) {

				String[] points = line.split(cvsSplitBy);
				LocalDate date;
				
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
				date = LocalDate.parse(points[0],dtf);
				Period difference = Period.between(fromDate, date);
				
				if(difference.isNegative())
				{
					continue;
				}

				difference = Period.between(date, endDate);
				if(difference.isNegative())
				{
					continue;
				}
				
				switch (level) {
				case MINUTES:
					addChartValues(i, aggMinuteData(date, points));
					break;
				case HOUR:
					addChartValues(i,aggHourData(date, points));
					break;
				case DAY:
					addChartValues(i,aggDayData(date, points));
					break;
				case WEEK:
					addChartValues(i,aggWeekData(date, points));
					break;
				case MONTH:
					addChartValues(i,aggMonthData(date, points));
					break;
				case YEAR:
					addChartValues(i,aggYearData(date, points));
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
				finStream.close();
				buffReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		i++;
		}
	}

}