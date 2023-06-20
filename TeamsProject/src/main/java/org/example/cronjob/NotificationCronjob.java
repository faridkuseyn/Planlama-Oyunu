package org.example.cronjob;

import org.example.model.Event;
import org.example.repository.EventRepository;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationCronjob {
    private static Event eventNotification;

    public void main() {

        // Toplantı saatine kalan süre (milisaniye cinsinden)
        long timeRemaining = calculateTimeRemaining();

        // Timer oluştur
        Timer timer = new Timer();

        // TimerTask oluştur
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendReminder();
//                timer.cancel(); // Timer'ı iptal et
            }
        };

        timer.schedule(task, timeRemaining);
    }

    private static long calculateTimeRemaining() {
        // Şu anki zamanı al
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        EventRepository eventRepository = new EventRepository();


        try {
            for (Event event : eventRepository.getAllEvents()) {
                LocalTime time = LocalTime.parse(event.getStartTime()); // Saat ve dakika bilgisi
                LocalDate date = LocalDate.now(); // Bugünün tarihi

                LocalDateTime dateTime = LocalDateTime.of(date, time); // LocalDateTime nesnesini oluştur


                // Toplantı saatine kalan süreyi hesapla
                Duration duration = Duration.between(currentTime, dateTime);
                long timeRemaining = duration.toMillis();

                // Toplantı saatine 10 dakika kala bildirim göndermek için
                // zamanı 10 dakika (600,000 milisaniye) azalt
                timeRemaining -= 600_000;


                NotificationCronjob notificationCronjob = new NotificationCronjob();

                if (timeRemaining < 1 )
                    notificationCronjob.setEventNotification(event);
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 9999999;

    }

    private static void sendReminder() {
        NotificationCronjob notificationCronjob = new NotificationCronjob();

        JOptionPane.showMessageDialog(null, "Toplantınız var! Toplantı bilgileri: " +
                "\n"+"Başlangıç:"+notificationCronjob.getEventNotification().getStartTime() + "\n"+
                "Bitiş:"+notificationCronjob.getEventNotification().getEndTime() + "\n"+
                "Açıklama:"+notificationCronjob.getEventNotification().getEventDescription());
    }

    public Event getEventNotification() {
        return eventNotification;
    }

    public void setEventNotification(Event eventNotification) {
        this.eventNotification = eventNotification;
    }
}
