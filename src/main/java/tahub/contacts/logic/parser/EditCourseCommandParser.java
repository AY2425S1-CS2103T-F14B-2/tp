package tahub.contacts.logic.parser;

import tahub.contacts.logic.commands.CourseCommand;
import tahub.contacts.logic.commands.EditCourseCommand;
import tahub.contacts.logic.parser.exceptions.ParseException;
import tahub.contacts.model.course.Course;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static tahub.contacts.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tahub.contacts.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tahub.contacts.logic.parser.CliSyntax.PREFIX_CODE;
import static tahub.contacts.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tahub.contacts.logic.parser.CliSyntax.PREFIX_NAME;
import static tahub.contacts.logic.parser.CliSyntax.PREFIX_PHONE;
import static tahub.contacts.model.course.Course.COURSE_CODE_MESSAGE_CONSTRAINTS;

import tahub.contacts.logic.commands.EditCourseCommand.EditCourseDescriptor;
import tahub.contacts.model.course.CourseCode;
import tahub.contacts.model.course.CourseName;

public class EditCourseCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCourseCommand
     * and returns an EditCourseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CourseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CODE, PREFIX_NAME);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CODE, PREFIX_NAME);
        
        if (!arePrefixesPresent(argMultimap, PREFIX_CODE, PREFIX_NAME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCourseCommand.MESSAGE_USAGE));
        }
        
        CourseCode courseCodeToEdit;
        EditCourseDescriptor editCourseDescriptor = new EditCourseDescriptor();
        
        if (argMultimap.getValue(PREFIX_CODE).isPresent()) {
            courseCodeToEdit = new CourseCode(argMultimap.getValue(PREFIX_CODE).get());
        }
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editCourseDescriptor.setCourseName(ParserUtil.parseCourseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        
        if (!editCourseDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCourseCommand.MESSAGE_COURSE_NOT_EDITED);
        }
        
        return new EditCourseCommand(courseCodeToEdit, editCourseDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
