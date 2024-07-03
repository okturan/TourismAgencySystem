package core;

import com.github.lgooddatepicker.components.DatePicker;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Helper {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Metal".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static Boolean isFieldEmpty(String fieldValue) {
        return fieldValue.trim().isEmpty();
    }

    public static Boolean isFieldListEmpty(String... fieldValues) {
        for (String field : fieldValues) {
            if (isFieldEmpty(field)) {
                return true;
            }
        }
        return false;
    }

    public static void showMessage(String str) {
        String title;
        String message;

        message = switch (str) {
            case "fill" -> {
                title = "Error";
                yield "Please fill all required fields.";
            }
            case "done" -> {
                title = "Result:";
                yield "Success";
            }

            case "notFound" -> {
                title = "Not Found";
                yield "Record Not Found";
            }
            default -> {
                title = "Attention";
                yield str;
            }
        };
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        String message;
        if (str.equals("sure")) {
            message = "Are you sure?";
        } else {
            message = str;
        }
        return JOptionPane.showConfirmDialog(null, message, "Confirm deletion", JOptionPane.YES_NO_OPTION) ==
                JOptionPane.YES_OPTION;
    }


    public static Point getCenteredLocation(int xSize, int ySize) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        Rectangle screenBounds;
        if (screens.length > 1) {
            // Use the second screen if available
            screenBounds = screens[1].getDefaultConfiguration().getBounds();
        } else {
            // Fallback to the primary screen
            screenBounds = screens[0].getDefaultConfiguration().getBounds();
        }

        int x = screenBounds.x + (screenBounds.width - xSize) / 2;
        int y = screenBounds.y + (screenBounds.height - ySize) / 2;
        return new Point(x, y);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void resizeColumnWidth(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(35);
        columnModel.getColumn(0).setMinWidth(25);
        for (int column = 1; column < table.getColumnCount(); column++) {
            int width = 25;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) width = 300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public static void addDateChangeListeners(DatePicker startDatePicker, DatePicker endDatePicker, JPanel container) {
        // Add a date change listener to the start date picker
        startDatePicker.addDateChangeListener(event -> {
            LocalDate startDate = event.getNewDate();
            LocalDate endDate = endDatePicker.getDate();
            // Set the lower bound of the end date picker based on the start date
            if (startDate != null) {
                endDatePicker.getSettings().setDateRangeLimits(startDate.plusDays(1), null);
                // Check if the start date is after the end date
                if (endDate != null && startDate.isAfter(endDate)) {
                    JOptionPane.showMessageDialog(container, "Start date cannot be after the end date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                    startDatePicker.setDate(startDatePicker.getDate());  // Revert to previous date
                } else {
                    System.out.println("Start Date changed to: " + startDate);
                }
            }
        });

        // Add a date change listener to the end date picker
        endDatePicker.addDateChangeListener(event -> {
            LocalDate startDate = startDatePicker.getDate();
            LocalDate endDate = event.getNewDate();
            // No need to set range limits here, only need to check if the end date is valid
            if (startDate != null && endDate != null && endDate.isBefore(startDate.plusDays(1))) {
                JOptionPane.showMessageDialog(container, "End date cannot be before the start date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                endDatePicker.setDate(endDatePicker.getDate());  // Revert to previous date
            } else {
                System.out.println("End Date changed to: " + endDate);
            }
        });
    }

}
