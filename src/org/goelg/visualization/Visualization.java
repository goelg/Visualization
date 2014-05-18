package org.goelg.visualization;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

/**
* The Visualization program implements an application that
* simply displays charts to the standard output.
*
* @author  Garima Goel
* @version 1.0
* @since   2014-03-31 
*/
public class Visualization extends Application {
	XYChart.Series<String, Number> dataSeries;
	private ListView<String> listFile;
	private Label errLabel;
	private Button button;

	private RadioButton rb1,rb2,rb3,rb4,rb5;
	private DatePicker fromDate,endDate;

	private Dimension screenSize;
	
	private FileData d;
	private List<File> inputFile;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	private LineChart<String, Number> lineChart;

	/*@param
	 *@return
	 *@throws
	 *
	 * 
	 */
	
	public Visualization()
	{
		 dataSeries = null;
		listFile = new ListView<String>();
		errLabel = new Label();
		button = new Button("Draw");

		rb1 = new RadioButton("Day");
		rb2 = new RadioButton("Minute");
		rb3 = new RadioButton("Hour");
		rb4 = new RadioButton("Month");
		rb5 = new RadioButton("Year");
		fromDate = new DatePicker();
		endDate = new DatePicker();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		d = new FileData();
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
		splitPane1.setPrefSize(screenSize.getWidth(),screenSize.getHeight());
		stage.setTitle("Visualization");
		Scene scene = new Scene(new Group(), screenSize.getWidth(),screenSize.getHeight());
		final FileChooser fileChooser = new FileChooser();
		final Button chooseFile = new Button("Browse file");
		final ToggleGroup group = new ToggleGroup();
		chooseFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				 configureFileChooser(fileChooser);
				
				inputFile = fileChooser.showOpenMultipleDialog(stage);
				  fileNames.clear();
					for (int i = 0; i < inputFile.size(); i++) {
			            fileNames.add(inputFile.get(i).getName());
			        }
				
				if (inputFile != null) {
					
					listFile.setItems(fileNames);

				}
			}
		});
		listFile.setPrefSize(100, 100);
		
		final GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		final GridPane grid1 = new GridPane();
		grid1.setVgap(12);
		grid1.setHgap(20);
		grid1.setPadding(new Insets(7, 6, 4, 8));
		grid.add(new Label("Files Select "), 0, 0);
		grid.add(listFile, 0, 1);
		grid.add(chooseFile, 1, 0);
		grid.add(new Label("Aggregation Level "), 0, 2);
		rb1.setToggleGroup(group);
		rb1.setSelected(true);
		rb2.setToggleGroup(group);
		rb3.setToggleGroup(group);
		rb4.setToggleGroup(group);
		rb5.setToggleGroup(group);

		grid.add(rb1, 1, 3);
		grid.add(rb2, 1, 4);
		grid.add(rb3, 1, 5);
		grid.add(rb4, 1, 6);
		grid.add(rb5, 1, 7);
		grid.add(new Label("From Date "), 0, 8);
		grid.add(fromDate, 1, 9);
		grid.add(new Label("End Date"), 0, 9);
		grid.add(endDate, 1, 10);
		grid.add(button, 0, 11);
		grid.add(errLabel, 0, 13);
		splitPane1.getItems().addAll(grid);
		final Group root = (Group) scene.getRoot();
		root.getChildren().add(splitPane1);

		stage.setScene(scene);
		stage.show();
		group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {

					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle t, Toggle t1) {

					}
				});
		group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle t, Toggle t1) {

		
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
				splitPane1.setDividerPositions(0.3f);
				RadioButton chk = (RadioButton) rb1.getToggleGroup()
						.getSelectedToggle(); // Cast object to radio button
		
				
				LocalDate fromDateVal = fromDate.getValue();
				LocalDate endDateVal = endDate.getValue();
				
				
				if (chk.getText().equalsIgnoreCase("Day"))
				{
					d.setLevel(AgreegationLevel.DAY);
					xAxis.setLabel("DAY");
				}
				if (chk.getText().equalsIgnoreCase("Minute"))
				{
					d.setLevel(AgreegationLevel.MINUTES);
					xAxis.setLabel("MINUTES");
				}
				if (chk.getText().equalsIgnoreCase("Month"))
				{
					fromDateVal = fromDateVal.withDayOfMonth(1);
					endDateVal = endDateVal.plusMonths(1).withDayOfMonth(1).minusDays(1);
					d.setLevel(AgreegationLevel.MONTH);
					xAxis.setLabel("MONTH");
				}

				if (chk.getText().equalsIgnoreCase("Year"))
				{
					fromDateVal = fromDateVal.withDayOfYear(1);
					endDateVal = endDateVal.plusYears(1).withDayOfYear(1).minusDays(1);
				
					d.setLevel(AgreegationLevel.YEAR);
					xAxis.setLabel("YEAR");
				}

				if (chk.getText().equalsIgnoreCase("Hour"))
				{
					d.setLevel(AgreegationLevel.HOUR);
					xAxis.setLabel("HOUR");
				}

				d.setFiles(inputFile);

				if (validateData()) {

					Charts test = new Charts();
					d.collectData(fromDateVal, endDateVal);

					dataSeries = test.generateSeries(d.getChartValues(),d.getLevel());
					stage.setTitle("Line Chart");
					// defining the axes
					
					
					// creating the chart
					lineChart = new LineChart<String, Number>(xAxis, yAxis);

					lineChart.setTitle("Line Chart");
					//lineChart.setCreateSymbols(false);

					lineChart.setPrefSize(700, 500);
					lineChart.getData().add(dataSeries);
					grid1.add(lineChart, 4, 4);

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

				RadioButton chk = (RadioButton) rb1.getToggleGroup()
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

}
