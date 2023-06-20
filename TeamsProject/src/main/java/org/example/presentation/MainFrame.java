package org.example.presentation;

import org.example.model.Event;
import org.example.repository.EventRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class MainFrame extends JFrame {
    private YearMonth currentYearMonth;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private EventRepository eventRepository;


    public void CalendarApp() {
        setTitle("Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        currentYearMonth = YearMonth.now();

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton previousButton = new JButton("<");
        previousButton.addActionListener(e -> previousMonth());
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> nextMonth());
        monthLabel = new JLabel("", JLabel.CENTER);

        headerPanel.add(previousButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(7, 7));
        updateCalendar();

        add(calendarPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            calendarPanel.add(dayLabel);
        }

        LocalDate startDate = currentYearMonth.atDay(1);
        int offset = startDate.getDayOfWeek().getValue() % 7;

        for (int i = 1; i <= offset; i++) {
            calendarPanel.add(new JLabel(""));
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(Integer.toString(day));
            int finalDay = day;
            dayButton.addActionListener(e -> showEventDialog(finalDay, currentYearMonth));
            calendarPanel.add(dayButton);
        }

        revalidate();
        monthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());
    }

    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void showEventDialog(int day, YearMonth currentYearMonth) {
        // Dialog penceresini oluştur
        JDialog dialog = new JDialog(this, "Events for Day " + day, true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout());

        // Etkinlikleri tutmak için bir tablo modeli oluştur
        String[] columnNames = {"Processing Time","Start Time", "End Time", "Event Description", "Event Type"};
        DefaultTableModel eventTableModel = new DefaultTableModel(columnNames, 0);
        JTable eventTable = new JTable(eventTableModel);
        eventTable.setEnabled(true);
        eventTable.getTableHeader().setReorderingAllowed(false);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane eventScrollPane = new JScrollPane(eventTable);

        List<Event> events = null;

        if (listEvents(Date.valueOf(currentYearMonth.toString() + "-" + day + "")) != null) {
            events = listEvents(Date.valueOf(currentYearMonth.toString() + "-" + day + ""));
            for (Event event : Objects.requireNonNull(listEvents(Date.valueOf(currentYearMonth.toString() + "-" + day + "")))) {
                eventTableModel.addRow(new Object[]{event.getProcessingTime(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getEventType(),
                        event.getEventDescription()});
            }

        }
        dialog.add(eventScrollPane, BorderLayout.CENTER);


        // Düzenle düğmesini oluştur
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = eventTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Seçili etkinliği düzenle
                    String processingTime = eventTableModel.getValueAt(selectedRow, 0).toString();
                    String startTime = eventTableModel.getValueAt(selectedRow, 1).toString();
                    String endTime = (String) eventTableModel.getValueAt(selectedRow, 2);
                    String type = (String) eventTableModel.getValueAt(selectedRow, 3);
                    String description = (String) eventTableModel.getValueAt(selectedRow, 4);

                    eventRepository = new EventRepository();
                    eventRepository.deleteEvent(description, LocalDate.parse(processingTime),startTime,endTime);

                    JTextField startTimeField = new JTextField(startTime, 10);
                    JTextField endTimeField = new JTextField(endTime, 10);
                    JTextField typeField = new JTextField(type, 20);
                    JTextField descriptionField = new JTextField(description, 20);

                    JPanel inputPanel = new JPanel();
                    inputPanel.add(new JLabel("Start Time:"));
                    inputPanel.add(startTimeField);
                    inputPanel.add(new JLabel("End Time:"));
                    inputPanel.add(endTimeField);
                    inputPanel.add(new JLabel("Event Type:"));
                    inputPanel.add(typeField);
                    inputPanel.add(new JLabel("Event Description:"));
                    inputPanel.add(descriptionField);

                    int result = JOptionPane.showConfirmDialog(dialog, inputPanel, "Edit Event", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Etkinliği güncelle
                        LocalDate processingTimeNew = Date.valueOf(currentYearMonth.toString() + "-" + day + "").toLocalDate();
                        Event event = new Event(processingTimeNew, startTimeField.getText(), endTimeField.getText(), typeField.getText(), descriptionField.getText());

                        eventRepository = new EventRepository();
                        eventRepository.addEvent(event);

                    }
                    dispose();
                    showEventDialog(day, currentYearMonth);
                }
            }
        });

        // Sil düğmesini oluştur
        JButton deleteButton = new JButton("Delete");
        List<Event> finalEvents = events;
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                int selectedRow = eventTable.getSelectedRow();
                Event selectedEvent = finalEvents.get(selectedRow);
                if (selectedRow != -1) {
                    eventRepository = new EventRepository();
                    eventRepository.deleteEvent(selectedEvent.getEventDescription(),selectedEvent.getProcessingTime(),selectedEvent.getStartTime(),selectedEvent.getEndTime());
                }
                dispose();
                showEventDialog(day, currentYearMonth);
            }
        });

        // Ekle düğmesini oluştur
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Yeni bir etkinlik eklemek için giriş dialogu oluştur
                JTextField startTimeField = new JTextField(10);
                JTextField endTimeField = new JTextField(10);
                JTextField descriptionField = new JTextField(20);
                JTextField eventType = new JTextField(20);

                JPanel inputPanel = new JPanel();
                inputPanel.add(new JLabel("Start Time:"));
                inputPanel.add(startTimeField);
                inputPanel.add(new JLabel("End Time:"));
                inputPanel.add(endTimeField);
                inputPanel.add(new JLabel("Event Type:"));
                inputPanel.add(eventType);
                inputPanel.add(new JLabel("Event Description:"));
                inputPanel.add(descriptionField);


                int result = JOptionPane.showConfirmDialog(dialog, inputPanel, "Add New Event", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String startTime = startTimeField.getText();
                    String endTime = endTimeField.getText();
                    String type = eventType.getText();
                    String description = descriptionField.getText();
                    LocalDate processingTime = Date.valueOf(currentYearMonth.toString() + "-" + day + "").toLocalDate();

                    Event event = new Event(processingTime, startTime, endTime, type, description);

                    eventRepository = new EventRepository();
                    eventRepository.addEvent(event);
                    dispose();
                    showEventDialog(day, currentYearMonth);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Dialog penceresini görüntüle
        dialog.setVisible(true);


    }

    private List<Event> listEvents(Date date) {
        eventRepository = new EventRepository();
        java.util.List<Event> events = eventRepository.getEventsByDate(date);
        if (events.isEmpty()) {
            return null;
        } else {
            return events;
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }




}
