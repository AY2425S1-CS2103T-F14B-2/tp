package tahub.contacts.model.studentcourseassociation;

import static java.util.Objects.requireNonNull;
import static tahub.contacts.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of StudentCourseAssociations that enforces uniqueness between its elements and does not allow nulls.
 * Supports a minimal set of list operations.
 */
public class StudentCourseAssociationList implements Iterable<StudentCourseAssociation> {

    private final ObservableList<StudentCourseAssociation> internalList = FXCollections.observableArrayList();
    private final ObservableList<StudentCourseAssociation> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent SCA as the given argument.
     */
    public boolean contains(StudentCourseAssociation toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Returns true if the list contains an equivalent SCA as the given argument.
     */
    public boolean has(StudentCourseAssociation toCheck) {
        return contains(toCheck);
    }

    /**
     * Adds a SCA to the list.
     * The SCA must not already exist in the list.
     */
    public void add(StudentCourseAssociation toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new RuntimeException("Duplicate SCA detected");
        }
        internalList.add(toAdd);
    }

    /**
     * Returns the SCA list.
     */
    public ObservableList<StudentCourseAssociation> get() {
        return internalList;
    }

    /**
     * Replaces the SCA {@code target} in the list with {@code editedStudentCourseAssociation}.
     * {@code target} must exist in the list.
     * The SCA identity of {@code editedStudentCourseAssociation} must not be the same as another existing SCA in the list.
     */
    public void set(StudentCourseAssociation target, StudentCourseAssociation editedStudentCourseAssociation) {
        requireAllNonNull(target, editedStudentCourseAssociation);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RuntimeException("SCA not found in list");
        }

        if (!target.equals(editedStudentCourseAssociation) && contains(editedStudentCourseAssociation)) {
            throw new RuntimeException("Duplicate SCA detected");
        }

        internalList.set(index, editedStudentCourseAssociation);
    }

    /**
     * Removes the equivalent SCA from the list.
     * The SCA must exist in the list.
     */
    public void remove(StudentCourseAssociation toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RuntimeException("SCA not found in list");
        }
    }

    public void set(StudentCourseAssociationList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code scaList}.
     * {@code scaList} must not contain duplicate scaList.
     */
    public void set(List<StudentCourseAssociation> scaList) {
        requireAllNonNull(scaList);
        if (!scaAre(scaList)) {
            throw new RuntimeException("Duplicate SCA detected.");
        }

        internalList.setAll(scaList);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<StudentCourseAssociation> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<StudentCourseAssociation> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentCourseAssociationList otherStudentCourseAssociationList)) {
            return false;
        }

        return internalList.equals(otherStudentCourseAssociationList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code sca} contains only unique sca.
     */
    private boolean scaAre(List<StudentCourseAssociation> sca) {
        for (int i = 0; i < sca.size() - 1; i++) {
            for (int j = i + 1; j < sca.size(); j++) {
                if (sca.get(i).equals(sca.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
