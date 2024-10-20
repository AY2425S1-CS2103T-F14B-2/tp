package tahub.contacts.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import tahub.contacts.commons.exceptions.DataLoadingException;
import tahub.contacts.model.ReadOnlyAddressBook;
import tahub.contacts.model.ReadOnlyUserPrefs;
import tahub.contacts.model.UserPrefs;
import tahub.contacts.model.course.UniqueCourseList;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    Path getCourseListFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException;


    Optional<UniqueCourseList> readCourseList() throws DataLoadingException;

    Optional<UniqueCourseList> readCourseList(Path filePath) throws DataLoadingException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    void saveCourseList(UniqueCourseList courseList) throws IOException;

    void saveCourseList(UniqueCourseList courseList, Path filePath) throws IOException;
}
