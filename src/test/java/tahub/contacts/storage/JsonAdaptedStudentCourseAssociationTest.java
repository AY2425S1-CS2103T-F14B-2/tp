package tahub.contacts.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tahub.contacts.testutil.TypicalPersons.BENSON;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tahub.contacts.commons.exceptions.IllegalValueException;
import tahub.contacts.model.studentcourseassociation.StudentCourseAssociation;


/**
 * Tests 'JsonAdaptedStudentCourseAssociation' class.
 */
public class JsonAdaptedStudentCourseAssociationTest {

    private static final String INVALID_MATRICULATION_NUMBER = "A00";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_MATRICULATION_NUMBER = BENSON.getMatricNumber().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final String VALID_COURSE_CODE = "CS3233";
    private static final String VALID_COURSE_NAME = "Competitive Programming";
    private static final String INVALID_COURSE_CODE = "PPPPP";
    private static final String INVALID_COURSE_NAME = "Fam|ly G^Y";

    private static final String VALID_TUTORIAL_ID = "T22";
    private static final String INVALID_TUTORIAL_ID = "XMAS";

    @Test
    void toModelType_validStudentCourseAssociation_noExceptionThrown() {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse validCourse = new JsonAdaptedCourse(VALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial validTutorial = new JsonAdaptedTutorial(VALID_TUTORIAL_ID, validCourse);

        JsonAdaptedStudentCourseAssociation adapter = new JsonAdaptedStudentCourseAssociation(validPerson,
                validCourse, validTutorial);
        assertDoesNotThrow(() -> adapter.toModelType());
    }

    @Test
    void toModelType_invalidStudent_throwsIllegalValueException() {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse validCourse = new JsonAdaptedCourse(VALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial invalidTutorial = new JsonAdaptedTutorial(INVALID_TUTORIAL_ID, validCourse);

        JsonAdaptedStudentCourseAssociation adapter = new JsonAdaptedStudentCourseAssociation(validPerson,
                validCourse, invalidTutorial);
        assertThrows(IllegalValueException.class, adapter::toModelType);
    }

    @Test
    void toModelType_invalidCourse_throwsIllegalValueException() {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse invalidCourse = new JsonAdaptedCourse(INVALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial validTutorial = new JsonAdaptedTutorial(VALID_TUTORIAL_ID, invalidCourse);

        JsonAdaptedStudentCourseAssociation adapter = new JsonAdaptedStudentCourseAssociation(validPerson,
                invalidCourse, validTutorial);
        assertThrows(IllegalValueException.class, adapter::toModelType);
    }

    @Test
    void toModelType_invalidTutorial_throwsIllegalValueException() {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse valid = new JsonAdaptedCourse(VALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial invalidTutorial = new JsonAdaptedTutorial(INVALID_TUTORIAL_ID, valid);

        JsonAdaptedStudentCourseAssociation adapter = new JsonAdaptedStudentCourseAssociation(validPerson,
                valid, invalidTutorial);
        assertThrows(IllegalValueException.class, adapter::toModelType);
    }

    @Test
    void testConstructor() throws IllegalValueException {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse validCourse = new JsonAdaptedCourse(VALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial validTutorial = new JsonAdaptedTutorial(VALID_TUTORIAL_ID, validCourse);

        StudentCourseAssociation sca = new StudentCourseAssociation(validPerson.toModelType(),
                validCourse.toModelType(), validTutorial.toModelType());
        JsonAdaptedStudentCourseAssociation adaptedSca = new JsonAdaptedStudentCourseAssociation(sca);
        assertEquals(sca, adaptedSca.toModelType());
    }

    @Test
    void toModelType_nullInputs_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () -> {
            new JsonAdaptedStudentCourseAssociation(null, null, null).toModelType();
        });
    }

    @Test
    void toModelType_validInputs_returnStudentCourseAssociation() {
        JsonAdaptedPerson validPerson = new JsonAdaptedPerson(VALID_MATRICULATION_NUMBER, VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        JsonAdaptedCourse validCourse = new JsonAdaptedCourse(VALID_COURSE_CODE, VALID_COURSE_NAME);
        JsonAdaptedTutorial validTutorial = new JsonAdaptedTutorial(VALID_TUTORIAL_ID, validCourse);
        // valid inputs so should not throw exception
        JsonAdaptedStudentCourseAssociation adapter = new JsonAdaptedStudentCourseAssociation(validPerson,
                validCourse, validTutorial);
        assertDoesNotThrow(() -> {
            StudentCourseAssociation sca = adapter.toModelType();
            assertEquals(VALID_MATRICULATION_NUMBER, sca.getStudent().getMatricNumber().toString());
            assertEquals(VALID_COURSE_CODE, sca.getCourse().courseCode);
            assertEquals(VALID_TUTORIAL_ID, sca.getTutorial().getTutorialId());
        });
    }
}
