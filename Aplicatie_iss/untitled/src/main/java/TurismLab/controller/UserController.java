package TurismLab.controller;

import TurismLab.domain.Book;
import TurismLab.domain.Borrow;
import TurismLab.domain.User;
import TurismLab.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    private Service service;
    private User currentUser;

    @FXML
    private TableView<Book> availableBooksTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    @FXML
    private TableView<Book> searchResultsTableView;
    @FXML
    private TableColumn<Book, String> searchTitleColumn;
    @FXML
    private TableColumn<Book, String> searchAuthorColumn;
    @FXML
    private TableColumn<Book, String> searchGenreColumn;
    @FXML
    private TableColumn<Book, Integer> searchQuantityColumn;

    @FXML
    private TableView<BorrowDTO> borrowedBooksTableView;
    @FXML
    private TableColumn<BorrowDTO, String> borrowTitleColumn;
    @FXML
    private TableColumn<BorrowDTO, String> borrowAuthorColumn;
    @FXML
    private TableColumn<BorrowDTO, String> borrowGenreColumn;
    @FXML
    private TableColumn<BorrowDTO, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowDTO, String> returnDateColumn;

    @FXML
    private TextField titluTextField;
    @FXML
    private TextField autorTextField;
    @FXML
    private ComboBox<String> genComboBox;
    @FXML
    private Label searchLabel;

    @FXML
    private TextField titluTextField1;
    @FXML
    private TextField autorTextField1;
    @FXML
    private TextField genTextField1;
    @FXML
    private Button borrowButton;

    private ObservableList<Book> availableBooksModel = FXCollections.observableArrayList();
    private ObservableList<Book> searchResultsModel = FXCollections.observableArrayList();
    private ObservableList<BorrowDTO> borrowedBooksModel = FXCollections.observableArrayList();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public UserController() {

    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserBorrows();
    }

    public void setService(Service service) {
        this.service = service;
        // Initialize available books table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("gen"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        availableBooksTableView.setItems(availableBooksModel);

        // Initialize search results table
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        searchAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        searchGenreColumn.setCellValueFactory(new PropertyValueFactory<>("gen"));
        searchQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        searchResultsTableView.setItems(searchResultsModel);

        // Initialize borrowed books table
        borrowTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        borrowGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        borrowedBooksTableView.setItems(borrowedBooksModel);

        // Initialize genre combobox
        List<String> genres = Arrays.asList("Fiction", "Science Fiction", "Fantasy", "Mystery", "Romance",
                "Thriller", "Horror", "Biography", "History", "Science",
                "Technology", "Philosophy", "Psychology", "Self-help");
        genComboBox.setItems(FXCollections.observableArrayList(genres));

        // Load all available books
        loadAvailableBooks();

        // Set up selection listener for available books table
        availableBooksTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titluTextField1.setText(newVal.getNume());
                autorTextField1.setText(newVal.getAutor());
                genTextField1.setText(newVal.getGen());
            }
        });

        // Set up selection listener for search results table
        searchResultsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titluTextField1.setText(newVal.getNume());
                autorTextField1.setText(newVal.getAutor());
                genTextField1.setText(newVal.getGen());
            }
        });

        // Set borrow button text
        borrowButton.setText("Imprumuta Cartea");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadAvailableBooks() {
        availableBooksModel.clear();
        List<Book> books = service.getAllBooks();
        for (Book book : books) {
            if (book.getCantitate() > 0) {
                availableBooksModel.add(book);
            }
        }
    }

    private void loadUserBorrows() {
        if (currentUser != null) {
            borrowedBooksModel.clear();
            List<Borrow> borrows = service.getAllBorrowsForUser(currentUser.getId());

            for (Borrow borrow : borrows) {
                BorrowDTO dto = new BorrowDTO(
                        borrow.getBook().getNume(),
                        borrow.getBook().getAutor(),
                        borrow.getBook().getGen(),
                        borrow.getDataImprumut().format(formatter),
                        borrow.getDataRestituire().format(formatter),
                        borrow.getId()
                );
                borrowedBooksModel.add(dto);
            }
        }
    }

    @FXML
    public void handleCautaBook(ActionEvent event) {
        String title = titluTextField.getText().trim();
        String author = autorTextField.getText().trim();
        String genre = genComboBox.getValue();

        searchResultsModel.clear();

        if (title.isEmpty() && author.isEmpty() && genre == null) {
            searchLabel.setText("Introduceti cel putin un criteriu de cautare");
            return;
        }

        List<Book> results = service.searchByFilter(title.isEmpty() ? null : title,
                author.isEmpty() ? null : author,
                genre);

        if (results.isEmpty()) {
            searchLabel.setText("Nu s-au gasit carti pentru criteriile specificate");
        } else {
            searchResultsModel.addAll(results);
            searchLabel.setText("S-au gasit " + results.size() + " rezultate");
        }
    }

    @FXML
    public void handleBorrow(ActionEvent event) {
        if (currentUser == null) {
            showAlert("Error", "No user logged in", Alert.AlertType.ERROR);
            return;
        }

        Book selectedBook = null;

        // Check if a book is selected in the available books table
        Book availableBook = availableBooksTableView.getSelectionModel().getSelectedItem();
        if (availableBook != null) {
            selectedBook = availableBook;
        } else {
            // Check if a book is selected in the search results table
            Book searchBook = searchResultsTableView.getSelectionModel().getSelectedItem();
            if (searchBook != null) {
                selectedBook = searchBook;
            }
        }

        if (selectedBook == null) {
            showAlert("Error", "No book selected", Alert.AlertType.ERROR);
            return;
        }

        if (selectedBook.getCantitate() <= 0) {
            showAlert("Error", "No copies available", Alert.AlertType.ERROR);
            return;
        }

        try {
            service.borrowBook(currentUser, selectedBook);
            showAlert("Success", "Book borrowed successfully", Alert.AlertType.INFORMATION);

            // Refresh the tables
            loadAvailableBooks();
            loadUserBorrows();

            // Clear selection fields
            titluTextField1.clear();
            autorTextField1.clear();
            genTextField1.clear();
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleReturnBook(ActionEvent event) {
        BorrowDTO selectedBorrow = borrowedBooksTableView.getSelectionModel().getSelectedItem();
        if (selectedBorrow == null) {
            showAlert("Error", "No borrowed book selected", Alert.AlertType.ERROR);
            return;
        }

        try {
            Borrow borrow = service.findBorrowById(selectedBorrow.getBorrowId());
            if (borrow != null) {
                service.returnBook(borrow);
                showAlert("Success", "Book returned successfully", Alert.AlertType.INFORMATION);

                // Refresh the tables
                loadAvailableBooks();
                loadUserBorrows();
            } else {
                showAlert("Error", "Borrow record not found", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // DTO class for displaying borrows in the table
    public static class BorrowDTO {
        private String title;
        private String author;
        private String genre;
        private String borrowDate;
        private String returnDate;
        private Long borrowId;

        public BorrowDTO(String title, String author, String genre, String borrowDate, String returnDate, Long borrowId) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
            this.borrowId = borrowId;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getGenre() {
            return genre;
        }

        public String getBorrowDate() {
            return borrowDate;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public Long getBorrowId() {
            return borrowId;
        }
    }
}