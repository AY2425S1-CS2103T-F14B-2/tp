package tahub.contacts.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tahub.contacts.commons.exceptions.IllegalValueException;
import tahub.contacts.model.course.Course;
import tahub.contacts.model.person.Person;
import tahub.contacts.model.studentcourseassociation.StudentCourseAssociation;
import tahub.contacts.model.tutorial.Tutorial;

/**
 * Jackson-friendly version of {@link StudentCourseAssociation}.
 */
class JsonAdaptedStudentCourseAssociation {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "StudentCourseAssociation's %s field is missing!";
    private final JsonAdaptedPerson student;
    private final JsonAdaptedCourse course;
    private final JsonAdaptedTutorial tutorial;
    //private final JsonSerializableGradingSystem grades;

    /**
     * Constructs a {@code JsonAdaptedStudentCourseAssociation} with the given {@code StudentClassAssociation}.
     */
    @JsonCreator
    public JsonAdaptedStudentCourseAssociation(
            @JsonProperty("student") JsonAdaptedPerson student,
            @JsonProperty("course") JsonAdaptedCourse course,
            @JsonProperty("tutorial") JsonAdaptedTutorial tutorial) {
        this.student = student;
        this.course = course;
        this.tutorial = tutorial;
        //this.grades = grades;
    }


    /**
     * Converts a given {@code StudentCourseAssociation} into this class for Jackson use.
     */
    public JsonAdaptedStudentCourseAssociation(StudentCourseAssociation source) {
        this.student = new JsonAdaptedPerson(source.getStudent());
        this.course = new JsonAdaptedCourse(source.getCourse());
        this.tutorial = new JsonAdaptedTutorial(source.getTutorial());
        //this.grades = new JsonSerializableGradingSystem(source.getGrades());
    }

    /**
     * Converts the JsonAdaptedStudentCourseAssociation object into a StudentCourseAssociation object.
     * Validates the student, course, and tutorial fields before creating a new StudentCourseAssociation.
     * If the student/course/tutorials are invalid, the error is handled in their respective adapted classes.
     *
     * @return a new StudentCourseAssociation object representing the JSON data
     * @throws IllegalValueException if the student, course, or tutorial fields are invalid
     */
    public StudentCourseAssociation toModelType() throws IllegalValueException {

        // Checks if the student is valid
        if (this.student == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JsonAdaptedPerson.class.getSimpleName()));
        }
        final Person studentModel = this.student.toModelType();

        // Checks if the course is valid
        if (this.course == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JsonAdaptedCourse.class.getSimpleName()));
        }
        final Course courseModel = this.course.toModelType();

        // Checks if the tutorial is valid
        if (this.tutorial == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JsonAdaptedTutorial.class.getSimpleName()));
        }
        final Tutorial tutorialModel = this.tutorial.toModelType();

        return new StudentCourseAssociation(studentModel, courseModel, tutorialModel, null);
    }
}