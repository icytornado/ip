import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class parser makes sense of the user commands
 * @author Zhang Peng
 * @version 28 Jan 2021
 */
public class Parser {
    public Parser() {
    }

    /**
     * This is the the method for making sense of the user commands
     * @param arrayList takes in the arrayList
     * @param path specifies the path to the file
     * @param input takes in the a scanner to scan
     * @return Nothing.
     */
    public String makingSenseOfUserCommand(ArrayList<Task> arrayList, String path, String input) {
        String holder;
        assert path != null;
        assert input != null;
        if (input.contains("bye")) {
            holder = "Bye. Hope to see you again soon!" + "\n";
        } else if (!(input.contains("todo") || input.contains("event") || input.contains("deadline")
                        || input.contains("list") || input.contains("done") || input.contains("delete")
                        || input.contains("find"))) {
            holder = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(" + "\n";
        } else if (input.equals("todo") || input.equals("event") || input.equals("deadline")
                        || input.equals("done") || input.equals("delete") || input.equals("find")) {
            holder = "☹ OOPS!!! The description of a todo cannot be empty." + "\n";
        } else if (input.equals("list")) {
            holder = "Here are the tasks in your list:" + "\n";
            for (Task m : arrayList) {
                holder = holder.concat(m.index + ". " + m);
                holder = holder.concat("\n");
            }
        } else if (input.contains("done ")) {
            int x = Integer.parseInt(input.substring(5));
            try {
                if (x > arrayList.size()) {
                    throw new DukeException("☹ OOPS!!! Sorry item no found :-(\n");
                } else {
                    holder = "Nice! I've marked this task as done:" + "\n";
                    holder = holder.concat(arrayList.get(x - 1).markAsDone().getStatusIcon() + " "
                                + arrayList.get(x - 1).description);
                    arrayList.set(x - 1, arrayList.get(x - 1).markAsDone());
                }
            } catch (DukeException e) {
                holder = e.getMessage();
            }
        } else if (input.contains("todo ")) {
            holder = "Got it. I've added this task: " + "\n";
            Task task = new Todo(input.substring(5));
            holder = holder + task + "\n";
            new TaskList().addToList(arrayList, task);
            holder = holder.concat("Now you have " + arrayList.size() + " task(s) in the list");
        } else if (input.contains("deadline")) {
            holder = "Got it. I've added this task: " + "\n";
            String[] parts = input.split("/", 2);
            String part1 = parts[0];
            String part2 = parts[1];
            //e.g. deadline return book /by 02/12/2019
            if (part2.contains("/")) {
                String dateString = part2.substring(3);
                String temp = dateString.substring(6) + "-" + dateString.substring(3, 5) + "-"
                        + dateString.substring(0, 2);
                LocalDate xx = LocalDate.parse(temp);
                String f = xx.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
                Task task = new Deadline(part1.substring(9), f);
                holder = holder + task + "\n";
                new TaskList().addToList(arrayList, task);
            } else {
                Task task = new Deadline(part1.substring(9), part2.substring(3));
                holder = holder + task + "\n";
                new TaskList().addToList(arrayList, task);
            }
            holder = holder.concat("Now you have " + arrayList.size() + " task(s) in the list");
        } else if (input.contains("event ")) {
            holder = "Got it. I've added this task: " + "\n";
            String[] parts = input.split("/", 2);
            String part1 = parts[0];
            String part2 = parts[1];

            // e.g. event project meeting /at 02/12/2019 2-4pm
            if (part2.contains("/")) {
                String dateString = part2.substring(3, 13);
                String timeString = part2.substring(13);
                String temp = dateString.substring(6) + "-" + dateString.substring(3, 5) + "-"
                        + dateString.substring(0, 2);
                LocalDate xx = LocalDate.parse(temp);
                String f = xx.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + timeString;
                Task task = new Event(part1.substring(6), f);
                holder = holder + task + "\n";
                new TaskList().addToList(arrayList, task);
            } else {
                Task task = new Event(part1.substring(6), part2.substring(3));
                holder = holder + task + "\n";
                new TaskList().addToList(arrayList, task);
            }
            holder = holder.concat("Now you have " + arrayList.size() + " task(s) in the list");
        } else if (input.contains("delete ")) {
            int deletedNumber = Integer.parseInt(input.substring(7));
            try {
                if (deletedNumber > arrayList.size()) {
                    throw new DukeException("☹ OOPS!!! Sorry item no found :-(");
                } else {
                    holder = "Noted. I've removed this task: " + "\n";
                    new TaskList().deleteFromList(arrayList, deletedNumber);
                    holder = holder.concat("Now you have " + arrayList.size() + " task(s) in the list");
                }
            } catch (DukeException e) {
                return e.getMessage();
            }
        } else if (input.contains("find ")) {
            holder = "Here are the matching tasks in your list: \n";
            ArrayList<Task> tt = new ArrayList<>();
            int counterOne = 1;
            assert arrayList.size() != 0;
            for (Task ttt :arrayList) {
                if (ttt.description.contains(input.substring(5))) {
                    ttt.index = counterOne;
                    tt.add(ttt);
                    counterOne++;
                }
            }
            assert tt.size() != 0;
            for (Task m : tt) {
                holder = holder.concat(m.index + ". " + m);
                holder = holder.concat("\n");
            }
        } else {
            holder = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(" + "\n";
        }
        int counterTwo = 1;
        assert arrayList.size() != 0;
        for (Task s : arrayList) {
            s.index = counterTwo;
            counterTwo++;
        }
        assert path != null;
        assert arrayList.size() != 0;
        new Storage().savingFile(arrayList, path);
        return holder + "\n";
    }
}


