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

public class AdminController implements Initializable {
    private Service service;

    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    @FXML
    private TextField titluTextField;
    @FXML
    private TextField autorTextField;
    @FXML
    private ComboBox<String> genComboBox;
    @FXML
    private TextField cantitateField;

    @FXML
    private TextField idUnicUserTextField;

    @FXML
    private TableView<BorrowDTO> borrowsTableView;
    @FXML
    private TableColumn<BorrowDTO, String> borrowTitleColumn;
    @FXML
    private TableColumn<BorrowDTO, String> borrowAuthorColumn;
    @FXML
    private TableColumn<BorrowDTO, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowDTO, String> returnDateColumn;

    private ObservableList<Book> booksModel = FXCollections.observableArrayList();
    private ObservableList<BorrowDTO> borrowsModel = FXCollections.observableArrayList();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AdminController() {
    }

    public void setService(Service service) {
        this.service = service;
        // Initialize book table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("gen"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        booksTableView.setItems(booksModel);

        // Initialize borrows table columns
        borrowTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        borrowsTableView.setItems(borrowsModel);

        // Initialize genre combobox
        List<String> genres = Arrays.asList("Fiction", "Science Fiction", "Fantasy", "Mystery", "Romance",
                "Thriller", "Horror", "Biography", "History", "Science",
                "Technology", "Philosophy", "Psychology", "Self-help");
        genComboBox.setItems(FXCollections.observableArrayList(genres));

        // Load all books
        loadAllBooks();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadAllBooks() {
        booksModel.clear();
        List<Book> books = service.getAllBooks();
        booksModel.addAll(books);
    }

    @FXML
    public void handleAddBook(ActionEvent event) {
        try {
            String title = titluTextField.getText();
            String author = autorTextField.getText();
            String genre = genComboBox.getValue();
            int quantity = Integer.parseInt(cantitateField.getText());

            if (title.isEmpty() || author.isEmpty() || genre == null || cantitateField.getText().isEmpty()) {
                showAlert("Error", "All fields must be filled", Alert.AlertType.ERROR);
                return;
            }

            Book book = service.addBook(title, author, genre, quantity);
            booksModel.add(book);
            clearBookFields();
            showAlert("Success", "Book added successfully", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Quantity must be a number", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleDeleteBook() {
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Error", "No book selected", Alert.AlertType.ERROR);
            return;
        }

        // Here you would add logic to delete a book
        // Currently there's no delete method in the service, so you'd need to implement it

        // For now, we'll just reload the books
        loadAllBooks();
    }

    @FXML
    public void handleCautaCarti() {
        try {
            String userIdText = idUnicUserTextField.getText();
            if (userIdText.isEmpty()) {
                showAlert("Error", "Please enter user ID", Alert.AlertType.ERROR);
                return;
            }

            Long userId = Long.parseLong(userIdText);
            User user = service.findUserById(userId);

            if (user == null) {
                showAlert("Error", "User not found", Alert.AlertType.ERROR);
                return;
            }

            // Load borrows for this user
            List<Borrow> borrows = service.getAllBorrowsForUser(userId);
            displayBorrows(borrows);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid user ID format", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayBorrows(List<Borrow> borrows) {
        borrowsModel.clear();

        for (Borrow borrow : borrows) {
            BorrowDTO dto = new BorrowDTO(
                    borrow.getBook().getNume(),
                    borrow.getBook().getAutor(),
                    borrow.getDataImprumut().format(formatter),
                    borrow.getDataRestituire().format(formatter)
            );
            borrowsModel.add(dto);
        }
    }

    private void clearBookFields() {
        titluTextField.clear();
        autorTextField.clear();
        genComboBox.getSelectionModel().clearSelection();
        cantitateField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleRestituie(ActionEvent actionEvent) {
        try {
            // Obține selecția cărții din tabela de împrumuturi
            BorrowDTO selectedBorrowDTO = borrowsTableView.getSelectionModel().getSelectedItem();

            if (selectedBorrowDTO == null) {
                showAlert("Error", "Please select a borrow to return", Alert.AlertType.ERROR);
                return;
            }

            // Căutăm împrumutul pe baza titlului cărții
            String bookTitle = selectedBorrowDTO.getTitle();
            String userIdText = idUnicUserTextField.getText();

            if (userIdText.isEmpty()) {
                showAlert("Error", "Please enter user ID", Alert.AlertType.ERROR);
                return;
            }

            Long userId = Long.parseLong(userIdText);
            User user = service.findUserById(userId);

            if (user == null) {
                showAlert("Error", "User not found", Alert.AlertType.ERROR);
                return;
            }

            // Găsește împrumutul corespunzător
            Borrow borrowToReturn = service.getAllBorrowsForUser(userId).stream()
                    .filter(borrow -> borrow.getBook().getNume().equals(bookTitle))
                    .findFirst()
                    .orElse(null);

            if (borrowToReturn == null) {
                showAlert("Error", "No borrow found for this book and user", Alert.AlertType.ERROR);
                return;
            }

            // Returnează cartea folosind serviciul
            service.returnBook(borrowToReturn);

            // Actualizează tabelul cu împrumuturi
            List<Borrow> borrows = service.getAllBorrowsForUser(userId);
            displayBorrows(borrows);

            showAlert("Success", "Book returned successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // DTO class for displaying borrows in the table
    public static class BorrowDTO {
        private String title;
        private String author;
        private String borrowDate;
        private String returnDate;

        public BorrowDTO(String title, String author, String borrowDate, String returnDate) {
            this.title = title;
            this.author = author;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getBorrowDate() {
            return borrowDate;
        }

        public String getReturnDate() {
            return returnDate;
        }
    }
}