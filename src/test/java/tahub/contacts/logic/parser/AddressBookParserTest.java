package tahub.contacts.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tahub.contacts.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tahub.contacts.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tahub.contacts.logic.commands.CommandTestUtil.COURSE_NAME_DESC;
import static tahub.contacts.logic.commands.CommandTestUtil.VALID_COURSE_CODE;
import static tahub.contacts.logic.commands.CommandTestUtil.VALID_COURSE_NAME;
import static tahub.contacts.testutil.Assert.assertThrows;
import static tahub.contacts.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tahub.contacts.logic.commands.AddCommand;
import tahub.contacts.logic.commands.ClearCommand;
import tahub.contacts.logic.commands.DeleteCommand;
import tahub.contacts.logic.commands.EditCommand;
import tahub.contacts.logic.commands.EditCommand.EditPersonDescriptor;
import tahub.contacts.logic.commands.ExitCommand;
import tahub.contacts.logic.commands.FindCommand;
import tahub.contacts.logic.commands.HelpCommand;
import tahub.contacts.logic.commands.ListCommand;
import tahub.contacts.logic.commands.course.CourseDeleteCommand;
import tahub.contacts.logic.commands.course.CourseEditCommand;
import tahub.contacts.logic.parser.exceptions.ParseException;
import tahub.contacts.model.course.CourseCode;
import tahub.contacts.model.person.NameContainsKeywordsPredicate;
import tahub.contacts.model.person.Person;
import tahub.contacts.testutil.EditCourseDescriptorBuilder;
import tahub.contacts.testutil.EditPersonDescriptorBuilder;
import tahub.contacts.testutil.PersonBuilder;
import tahub.contacts.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editCourse() throws Exception {
        CourseCode courseCode = new CourseCode(VALID_COURSE_CODE);
        CourseEditCommand.EditCourseDescriptor descriptor = new EditCourseDescriptorBuilder()
                .withCourseName(VALID_COURSE_NAME).build();
        CourseEditCommand command = (CourseEditCommand) parser
                .parseCommand(CourseEditCommand.COMMAND_WORD + " c/" + courseCode + COURSE_NAME_DESC);
        assertEquals(new CourseEditCommand(courseCode, descriptor), command);
    }

    @Test
    public void parseCommand_deleteCourse() throws Exception {
        CourseCode courseCode = new CourseCode(VALID_COURSE_CODE);
        CourseDeleteCommand command = (CourseDeleteCommand) parser.parseCommand(CourseDeleteCommand
                .COMMAND_WORD + " c/" + courseCode);
        assertEquals(new CourseDeleteCommand(courseCode), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
