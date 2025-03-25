package constants;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {
    public static final List<String> CSS_FILES = List.of(
            "/views/base.css",
            "/views/task.css",
            "/views/buttons.css",
            "/views/checkbox.css",
            "/views/dialog.css",
            "/views/filters.css"
    );
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter NAVIGATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");
    public static final int MENU_ANIMATION_DURATION = 250;
    public static final String ALL_TASKS_LABEL = "Wszystkie zadania";
}
