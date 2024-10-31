package tahub.contacts.logic.parser;

import static tahub.contacts.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tahub.contacts.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tahub.contacts.commons.core.LogsCenter;
import tahub.contacts.logic.commands.AddCommand;
import tahub.contacts.logic.commands.ClearCommand;
import tahub.contacts.logic.commands.Command;
import tahub.contacts.logic.commands.DeleteCommand;
import tahub.contacts.logic.commands.EditCommand;
import tahub.contacts.logic.commands.EnrollCommand;
import tahub.contacts.logic.commands.ExitCommand;
import tahub.contacts.logic.commands.FindCommand;
import tahub.contacts.logic.commands.HelpCommand;
import tahub.contacts.logic.commands.ListCommand;
import tahub.contacts.logic.commands.attend.AttendAbsentCommand;
import tahub.contacts.logic.commands.attend.AttendClearCommand;
import tahub.contacts.logic.commands.attend.AttendPresentCommand;
import tahub.contacts.logic.commands.attend.AttendRemoveCommand;
import tahub.contacts.logic.commands.course.CourseAddCommand;
import tahub.contacts.logic.commands.course.CourseDeleteCommand;
import tahub.contacts.logic.commands.course.CourseEditCommand;
import tahub.contacts.logic.parser.attend.AttendAbsentCommandParser;
import tahub.contacts.logic.parser.attend.AttendClearCommandParser;
import tahub.contacts.logic.parser.attend.AttendPresentCommandParser;
import tahub.contacts.logic.parser.attend.AttendRemoveCommandParser;
import tahub.contacts.logic.parser.course.CourseAddCommandParser;
import tahub.contacts.logic.parser.course.CourseDeleteCommandParser;
import tahub.contacts.logic.parser.course.CourseEditCommandParser;
import tahub.contacts.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case CourseAddCommand.COMMAND_WORD:
            return new CourseAddCommandParser().parse(arguments);

        case CourseEditCommand.COMMAND_WORD:
            return new CourseEditCommandParser().parse(arguments);

        case CourseDeleteCommand.COMMAND_WORD:
            return new CourseDeleteCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case AttendPresentCommand.COMMAND_WORD:
            return new AttendPresentCommandParser().parse(arguments);

        case AttendAbsentCommand.COMMAND_WORD:
            return new AttendAbsentCommandParser().parse(arguments);

        case AttendRemoveCommand.COMMAND_WORD:
            return new AttendRemoveCommandParser().parse(arguments);

        case AttendClearCommand.COMMAND_WORD:
            return new AttendClearCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case EnrollCommand.COMMAND_WORD:
            return new EnrollCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
