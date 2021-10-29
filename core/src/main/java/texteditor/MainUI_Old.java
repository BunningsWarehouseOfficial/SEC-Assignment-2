package texteditor;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Controls the main window, displaying the bus timetable contents, a toolbar (with 'load', 'save'
 * and 'add' buttons and a search field) and a status bar that displays the current time.
 *
 * The table actually displays a filtered subset of the timetable entries, according to which 
 * entries match the search field.
 */
public class MainUI_Old
{
    private static final int SPACING = 8;
    
    private Stage stage;
    private ObservableList<TimetableEntry_Old> entries;
    private LoadSaveUI_Old loadSaveUI;
    private AddUI_Old addUI;
    private TextField statusBar = new TextField();
    private ResourceBundle bundle;
    
    public MainUI_Old(Stage stage, ObservableList<TimetableEntry_Old> entries, LoadSaveUI_Old loadSaveUI, AddUI_Old addUI,
                      ResourceBundle bundle)
    {
        this.stage = stage;
        this.entries = entries;
        this.loadSaveUI = loadSaveUI;
        this.addUI = addUI;
        this.bundle = bundle;
    }
    
    public void display()
    {
        stage.setTitle(bundle.getString("main_title"));
        stage.setMinWidth(1000);
                
        // Create toolbar and button event handlers
        var loadBtn = new Button(bundle.getString("load_btn"));
        var saveBtn = new Button(bundle.getString("save_btn"));
        var addBtn = new Button(bundle.getString("add_btn"));
        var filterText = new TextField();        
        filterText.setPromptText(bundle.getString("search_prompt"));
        ToolBar toolBar = new ToolBar(
            loadBtn, saveBtn, addBtn, new Separator(), filterText);            
        loadBtn.setOnAction(event -> loadSaveUI.load());
        saveBtn.setOnAction(event -> loadSaveUI.save());
        addBtn.setOnAction(event -> addUI.addEntry());
        
        // Table and table data
        var entryTable = new TableView<TimetableEntry_Old>();
        var filteredEntries = new FilteredList<>(entries);
        entryTable.setItems(filteredEntries);
        
        filterText.textProperty().addListener(
            (field, oldVal, newVal) ->
                filteredEntries.setPredicate(entry -> matches(entry, newVal))
        );
        
        // Table columns
        TableColumn<TimetableEntry_Old,String> routeIdCol       = new TableColumn<>(bundle.getString("route_tab"));
        TableColumn<TimetableEntry_Old,String> fromCol          = new TableColumn<>(bundle.getString("from_tab"));
        TableColumn<TimetableEntry_Old,String> destinationCol   = new TableColumn<>(bundle.getString("destination_tab"));
        TableColumn<TimetableEntry_Old,String> departureTimeCol = new TableColumn<>(bundle.getString("departure_tab"));
        TableColumn<TimetableEntry_Old,String> arrivalTimeCol   = new TableColumn<>(bundle.getString("arrival_tab"));
        TableColumn<TimetableEntry_Old,String> durationCol      = new TableColumn<>(bundle.getString("duration_tab"));
        
        entryTable.getColumns().setAll(List.of(routeIdCol, fromCol, destinationCol, departureTimeCol, arrivalTimeCol, durationCol));
        
        // Formatting table column values
        routeIdCol.setCellValueFactory( 
            (cell) -> new SimpleStringProperty(cell.getValue().getRouteId()) );
            
        fromCol.setCellValueFactory( 
            (cell) -> new SimpleStringProperty(cell.getValue().getFrom()) );
        
        destinationCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(cell.getValue().getDestination()) );
            
        departureTimeCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(getDepartureTimeString(cell.getValue())));
                        
        arrivalTimeCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(getArrivalTimeString(cell.getValue())));
            
        durationCol.setCellValueFactory(
            (cell) -> new SimpleStringProperty(String.valueOf(cell.getValue().getDuration().toMinutes())));
          
        // Table column widths.
        routeIdCol      .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        fromCol         .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.30));
        destinationCol  .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.30));
        departureTimeCol.prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        arrivalTimeCol  .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));
        durationCol     .prefWidthProperty().bind(entryTable.widthProperty().multiply(0.10));            
        
        // Status bar
        var exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(
            () -> Platform.runLater(this::updateStatus), 
            0L, 1L, TimeUnit.SECONDS);
        stage.setOnHiding(event -> exec.shutdown());        
        
        // Add the main parts of the UI to the window.
        var mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(entryTable);
        mainBox.setBottom(statusBar);
        Scene scene = new Scene(mainBox);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();        
    }
    
    
    private boolean matches(TimetableEntry_Old entry, String searchTerm)
    {
        // FIXME: the string matching here isn't properly internationalised.
        
        return entry.getFrom().toLowerCase().contains(searchTerm.toLowerCase()) || 
               entry.getDestination().toLowerCase().contains(searchTerm.toLowerCase());
    }

    private String getDepartureTimeString(TimetableEntry_Old entry)
    {
        // FIXME: the time formatting here is not properly internationalised.
        
        return entry.getDepartureTime().toString();
    }
    
    private String getArrivalTimeString(TimetableEntry_Old entry)
    {
        // FIXME: need to add 'duration' to 'departureTime' to get the arrival time, and then also 
        // format it in an appropriately internationalised fashion.
        
        return "";
    }
    
    private void updateStatus()
    {
        // FIXME: the date displayed here is not internationalised properly.
        
        statusBar.setText(LocalDateTime.now().toString());
    }
}
