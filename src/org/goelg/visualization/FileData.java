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

public class FileData {
	private TreeMap<LocalDateTime, Integer> chartValues;
	private String fileName;
	private List<File> files;
	private AggregationLevel level;
	

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public TreeMap<LocalDateTime, Integer> getChartValues() {
		return chartValues;
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void setChartValues(TreeMap<LocalDateTime, Integer> chartValues) {
		this.chartValues = chartValues;
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
		chartValues = new TreeMap<LocalDateTime, Integer>();
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
			} 
			else
			{
				value = 0;
			}

			LocalTime time = LocalTime.MIDNIGHT;
			time = time.withHour(hourOfDay);
			time = time.withMinute(minutes);
			chartValues.put(date.atTime(time), value);
		}
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void aggHourData(LocalDate date, String[] points) {
		int hourOfDay = 0;
		int minutes = 0;
		int value = 0;

		for (int i = 1; i < 24 * 60; i++, minutes++) {
			if (minutes == 60) {
				hourOfDay++;
				LocalTime time = LocalTime.MIDNIGHT;
				time = time.withHour(hourOfDay);
				chartValues.put(date.atTime(time), value);
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

	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void aggDayData(LocalDate date, String[] points) {
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		chartValues.put(date.atStartOfDay(), value);
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void aggWeekData(LocalDate date, String[] points) {
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
		if(chartValues.containsKey(time))
		{
			chartValues.put(time, chartValues.get(time)+value);
		}
		else
		{
			chartValues.put(time, value);
		}
	}
	
	
	
	
	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void aggMonthData(LocalDate date, String[] points) {
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}
		}
		LocalDateTime time = date.withDayOfMonth(1).atStartOfDay();
		if(chartValues.containsKey(time))
		{
			chartValues.put(time, chartValues.get(time)+value);
		}
		else
		{
			chartValues.put(time, value);
		}
	}


	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	public void aggYearData(LocalDate date, String[] points) {
		int value = 0;
		for (int i = 1; i < points.length; i++) {
			try {
				value += Integer.parseInt(points[i]);
			} catch (NumberFormatException e) {
				value += 0;
			}

		}
		
		LocalDateTime time = date.withDayOfYear(1).atStartOfDay();
		if(chartValues.containsKey(time))
		{
			chartValues.put(time, chartValues.get(time)+value);
		}
		else
		{
			chartValues.put(time, value);
		}
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
					aggMinuteData(date, points);
					break;
				case HOUR:
					aggHourData(date, points);
					break;
				case DAY:
					aggDayData(date, points);
					break;
				case WEEK:
					aggWeekData(date, points);
					break;
				case MONTH:
					aggMonthData(date, points);
					break;
				case YEAR:
					aggYearData(date, points);
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
		}
	}

}