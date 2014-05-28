package org.goelg.visualization;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
* The Visualization program implements an application that
* simply displays charts to the standard output.
*
* @author  Garima Goel
* @version 1.0
* @since   2014-05-18 
*/
public class Visualization extends Application {
	List<Series<String, Number>> dataSeries;
	private ListView<String> listFile;
	private Label errLabel;
	private Button button;
	private CheckBox multiChart;
	private RadioButton rbDay,rbMin,rbHour,rbMonth,rbYear,rbWeek;
	private RadioButton rbLine,rbBar;
	private DatePicker fromDate,endDate;
	private ToggleGroup aggLevelGroup, chartTypeGroup;

	
	private Dimension screenSize;
	
	private FileData data;
	private List<File> inputFile;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	private LocalDate minDate,maxDate;
//	private LineChart<String, Number> lineChart;
	//private BarChart<String, Number> lineChart;
	private XYChart<String, Number> lineChart;
	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	
	public Visualization()
	{
		 dataSeries = new ArrayList<XYChart.Series<String, Number>>();
		listFile = new ListView<String>();
		errLabel = new Label();
		button = new Button("Draw");

		rbDay = new RadioButton("Day");
		rbMin = new RadioButton("Minute");
		rbHour = new RadioButton("Hour");
		rbMonth = new RadioButton("Month");
		rbWeek = new RadioButton("Week");
		rbYear = new RadioButton("Year");
		rbLine = new RadioButton("Line");
		rbBar = new RadioButton("Bar");
		
		fromDate = new DatePicker();
		endDate = new DatePicker();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		multiChart = new CheckBox("Multi Line Enabled");
		data = new FileData();
		minDate = null;
		maxDate = null;

		aggLevelGroup = new ToggleGroup();
		chartTypeGroup = new ToggleGroup();

		rbLine.setToggleGroup(chartTypeGroup);
		rbBar.setToggleGroup(chartTypeGroup);
		rbLine.setSelected(true);
		rbMin.setToggleGroup(aggLevelGroup);
		rbHour.setToggleGroup(aggLevelGroup);
		rbMonth.setToggleGroup(aggLevelGroup);
		rbYear.setToggleGroup(aggLevelGroup);
		rbWeek.setToggleGroup(aggLevelGroup);
		rbDay.setToggleGroup(aggLevelGroup);
		rbDay.setSelected(true);
		
		
	}
	 /**
	   * This is the main method .
	   * @param args Unused.
	   * @return Nothing.
	   * @exception IOException On input error.
	   * @see IOException
	   */
	public static void main(String[] args) {
		launch(args);
	}
	ObservableList<String> fileNames = 
	        FXCollections.observableArrayList();



	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	
	@Override
	public void start(final Stage stage) {
		final SplitPane splitPane1 = new SplitPane();
		splitPane1.setOrientation(Orientation.HORIZONTAL);
		splitPane1.setPrefSize(screenSize.getWidth(),screenSize.getHeight()-50);
		stage.setTitle("Visualization");
		Scene scene = new Scene(new Group());
		stage.setMaximized(true);
		
		final FileChooser fileChooser = new FileChooser();
		final Button chooseFile = new Button("Browse file");
		chooseFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent event) {
				 configureFileChooser(fileChooser);
				
				inputFile = fileChooser.showOpenMultipleDialog(stage);
				  fileNames.clear();
					for (int i = 0; i < inputFile.size(); i++) {
			            fileNames.add(inputFile.get(i).getName());
			        }
				setDate();
				fromDate.setValue(minDate);
				endDate.setValue(maxDate);
				if (inputFile != null) {
					listFile.setItems(fileNames);
				}
				
			}
		});
		listFile.setPrefSize(100, 100);
		int yPos = 0;
		final GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setManaged(true);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		final GridPane grid1 = new GridPane();
		grid1.setVgap(12);
		grid1.setHgap(20);
		grid1.setPadding(new Insets(7, 6, 4, 8));
		grid.add(new Label("Files Select "), 0, yPos);
		grid.add(chooseFile, 1, yPos++);
		grid.add(multiChart, 1, yPos);
		grid.add(listFile, 0, yPos++);
		
		grid.add(new Label("Chart Type "), 0, yPos++);
		grid.add(rbLine, 1, yPos++);
		grid.add(rbBar, 1, yPos++);
		
		grid.add(new Label("Aggregation Level "), 0, yPos++);
		
		grid.add(rbMin, 1, yPos++);
		grid.add(rbHour, 1, yPos++);
		grid.add(rbDay, 1, yPos++);
		grid.add(rbWeek, 1, yPos++);
		grid.add(rbMonth, 1, yPos++);
		grid.add(rbYear, 1, yPos++);
		grid.add(new Label("From Date "), 0, yPos);
		grid.add(fromDate, 1, yPos++);
		grid.add(new Label("End Date"), 0, yPos);
		grid.add(endDate, 1, yPos++);
		grid.add(button, 0, yPos++);
		grid.add(errLabel, 0, yPos++);
		splitPane1.getItems().addAll(grid);
		final Group root = (Group) scene.getRoot();
		root.getChildren().add(splitPane1);

		stage.setScene(scene);
		stage.show();
		aggLevelGroup.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {

					public void changed(ObservableValue<? extends Toggle> obVal,
							Toggle tog, Toggle tog1) {

					}
				});

		button.setOnAction(new EventHandler<ActionEvent>() {

			/*@param
			 *@return
			 *@throws
			 *
			 * 
			 */
			public void handle(ActionEvent event) {
				xAxis = new CategoryAxis();
				yAxis = new NumberAxis();
				if (lineChart != null)
					grid1.getChildren().remove(lineChart);
				splitPane1.getItems().remove(grid1);
				grid1.setManaged(true);
				splitPane1.setDividerPositions(0.3f);
				String aggLevelVal =((RadioButton)aggLevelGroup.getSelectedToggle()).getText(); 
				String chartTypeVal =((RadioButton)chartTypeGroup.getSelectedToggle()).getText(); 
				LocalDate fromDateVal = fromDate.getValue();
				LocalDate endDateVal = endDate.getValue();				
				
				if (aggLevelVal.equalsIgnoreCase("Day"))
				{
					data.setLevel(AggregationLevel.DAY);
					xAxis.setLabel("DAY");
				}
				if (aggLevelVal.equalsIgnoreCase("Minute"))
				{
					data.setLevel(AggregationLevel.MINUTES);
					xAxis.setLabel("MINUTES");
				}
				if (aggLevelVal.equalsIgnoreCase("Month"))
				{
					
					fromDateVal = fromDateVal.withDayOfMonth(1);
					endDateVal = endDateVal.plusMonths(1).withDayOfMonth(1).minusDays(1);
					data.setLevel(AggregationLevel.MONTH);
					xAxis.setLabel("MONTH");
				}

				if (aggLevelVal.equalsIgnoreCase("Week"))
				{
					int dayWeek = fromDateVal.getDayOfWeek().getValue();
					fromDateVal = fromDateVal.minusDays(dayWeek-1);
					dayWeek = endDateVal.getDayOfWeek().getValue();
					endDateVal = endDateVal.plusDays(7-dayWeek);
					data.setLevel(AggregationLevel.WEEK);
					xAxis.setLabel("WEEK");
				}
				
				
				if (aggLevelVal.equalsIgnoreCase("Year"))
				{
					fromDateVal = fromDateVal.withDayOfYear(1);
					endDateVal = endDateVal.plusYears(1).withDayOfYear(1).minusDays(1);		
					data.setLevel(AggregationLevel.YEAR);
					xAxis.setLabel("YEAR");
				}

				if (aggLevelVal.equalsIgnoreCase("Hour"))
				{
					data.setLevel(AggregationLevel.HOUR);
					xAxis.setLabel("HOUR");
				}

				data.setFiles(inputFile);

				if (validateData()) {

					Charts test = new Charts();
					data.collectData(fromDateVal, endDateVal,multiChart.isSelected());
					dataSeries = test.generateSeries(data.getChartValues(),data.getLevel());
					stage.setTitle("Line Chart");
					// defining the axes
					
					
					// creating the chart
					if(chartTypeVal.equalsIgnoreCase("Bar"))
					{
						lineChart = new BarChart<String, Number>(xAxis, yAxis);
						lineChart.setTitle("Bar Chart");
					}
						
					else
					{
						lineChart = new LineChart<String, Number>(xAxis, yAxis);
						lineChart.setTitle("Line Chart");
					}
					
					
					//lineChart.setCreateSymbols(false);

					lineChart.setPrefSize(splitPane1.getWidth(),splitPane1.getHeight()-100);
					
					for(Series<String, Number> data:dataSeries)
						lineChart.getData().add(data);
					grid1.add(lineChart, 0, 0);

					splitPane1.getItems().addAll(grid1);
					stage.show();
					 for (XYChart.Series<String, Number> s : lineChart.getData()) {
				            for (XYChart.Data<String, Number> d : s.getData()) {
				                Tooltip.install(d.getNode(), new Tooltip(
				                        String.format("%s = %d", 
				                                d.getXValue(), 
				                                d.getYValue())));
				            }
				        }
				}
			}

			/*@param
			 *@return
			 *@throws
			 *
			 * 
			 */
			private boolean validateData() {

				RadioButton chk = (RadioButton) rbDay.getToggleGroup()
						.getSelectedToggle(); // Cast object to radio button
				LocalDate fromDateVal = fromDate.getValue();
				LocalDate endDateVal = endDate.getValue();

				Period difference = Period.between(fromDateVal, endDateVal);
				errLabel.setText("");
				if (chk.getText().equalsIgnoreCase("Day")) {
					if (difference.getMonths() > 6) {
						errLabel.setText("Please select Days less than 6 months");
						return false;
					}
					if (difference.getYears() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}
				}
				if (chk.getText().equalsIgnoreCase("Minute")) {
					if (difference.getMonths() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}
					if (difference.getYears() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}
					

					if (difference.getDays() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}

				}
				if (chk.getText().equalsIgnoreCase("Month")) {
					if (difference.getYears() > 20) {
						errLabel.setText("Please select lesser duration");
						return false;
					}
				}

				if (chk.getText().equalsIgnoreCase("Hour")) {
					if (difference.getMonths() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}
					if (difference.getYears() > 0) {
						errLabel.setText("Please select other Aggregation period");
						return false;
					}
				}

				return true;
			}

		});
	}

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("File");

		fileChooser.setInitialDirectory(new File(System
				.getProperty("user.home")));
	}

	
	public void setDate() {
		FileInputStream finStream = null;
		BufferedReader buffReader = null;
		for(File file:inputFile)
		{
		try {
			finStream = new FileInputStream(file);
			String line;
			String cvsSplitBy = ",";
			String prevLine = new String();
			buffReader = new BufferedReader(new InputStreamReader(finStream));
			String[] points ;
			if((line = buffReader.readLine()) != null) {
				prevLine = line;
				points = line.split(cvsSplitBy);
				LocalDate date;
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
				date = LocalDate.parse(points[0],dtf);
				if(minDate==null)
					minDate = date;
				if(Period.between(minDate, date).isNegative())
					minDate = date;
				
			}
			
			while ((line = buffReader.readLine()) != null) {
				prevLine = line;	
			}
			
			if(prevLine.length()>0)
			{
				points = prevLine.split(cvsSplitBy);
				LocalDate date;
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
				date = LocalDate.parse(points[0],dtf);
				if(maxDate==null)
					maxDate = date;
				if(Period.between(date,maxDate).isNegative())
					maxDate = date;
				
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
