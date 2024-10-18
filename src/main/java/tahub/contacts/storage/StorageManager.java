package tahub.contacts.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import tahub.contacts.commons.core.LogsCenter;
import tahub.contacts.commons.exceptions.DataLoadingException;
import tahub.contacts.model.ReadOnlyAddressBook;
import tahub.contacts.model.ReadOnlyUserPrefs;
import tahub.contacts.model.UserPrefs;
import tahub.contacts.model.course.UniqueCourseList;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private JsonUniqueCourseListStorage courseListStorage;
    private JsonUniqueCourseListStorage uniqueCourseListStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AddressBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage,
                          JsonUniqueCourseListStorage courseListStorage) {
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.courseListStorage = courseListStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    //===== course list methods =====
    @Override
    public Path getCourseListFilePath() {
        return uniqueCourseListStorage.getCourseListFilePath();
    }

    @Override
    public Optional<UniqueCourseList> readCourseList() throws DataLoadingException {
        return readCourseList(courseListStorage.getCourseListFilePath());
    }

    @Override
    public Optional<UniqueCourseList> readCourseList(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return uniqueCourseListStorage.readCourseList(filePath);
    }

    @Override
    public void saveCourseList(UniqueCourseList courseList) throws IOException {
        saveCourseList(courseList, courseListStorage.getCourseListFilePath());
    }

    @Override
    public void saveCourseList(UniqueCourseList courseList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        courseListStorage.saveCourseList(courseList, filePath);
    }
}
