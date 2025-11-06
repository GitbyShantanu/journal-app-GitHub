package com.shantanu.journalApp.scheduler;

import com.shantanu.journalApp.entity.JournalEntry;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.enums.Sentiment;
import com.shantanu.journalApp.repository.UserRepositoryImpl;
import com.shantanu.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository; // Db se SentimentAnalysis via Email opted users fetch karne ke liye custom repo


//    @Scheduled(cron = "0 * * * * *") //every minute

    @Scheduled(cron = "0 0 9 ? * SUN") // every sunday 9AM
    public void fetchUserAndSendSaMail() {
        List<User> usersForSA = userRepository.getUserForSA(); // DB se sentiment analysis enabled users ki list

        for(User user : usersForSA) { // Har user ke liye
            List<JournalEntry> journalEntryList = user.getJournalEntryList(); // User ki saari journal entries

            // Pichle 7 din ki entries ka content nikalo
            List<Sentiment> sentimentsList = journalEntryList.stream()
                    // Filter: Sirf recent entries rakho (pichle 7 din ki)

                    .filter(x -> x.getDate()
                            //Kya entry last 7 days ke baad ki hai? true/false
                            .isAfter(LocalDateTime.now()  // Aaj ka date-time lo Usme se 7 din minus karo
                                    .minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()) // journal se Sentiment enum nikalo (HAPPY, SAD, etc.)
                    .collect(Collectors.toList()); // List banao(wrap up)

            // Har sentiment ka count nikalo
            Map<Sentiment, Integer> sentimentCountsMap = new HashMap<>();
            for(Sentiment sentiment : sentimentsList) {
                if(sentiment != null) {
                    sentimentCountsMap.put(sentiment, sentimentCountsMap.getOrDefault(sentiment, 0)+1);
                }
            }

            // Sabse zyada count wala sentiment dhundho
            Sentiment mostFreqSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCountsMap.entrySet()) {
                if(entry.getValue() > maxCount) { // Current cnt > max?
                    maxCount = entry.getValue(); // Update New maxCount
                    mostFreqSentiment = entry.getKey(); // Winner sentiment having max count
                }
            }

            // Winner sentiment email karo
            if(mostFreqSentiment != null) {
                emailService.sendEmail(
                    user.getEmail(),  // To: User ka email address
                    "Sentiment for last 7 days",  // Subject: Email ka subject
                    "dear "+user.getUserName()+" Your mood in last week was "+ mostFreqSentiment.toString() // Body: "Your mood was: HAPPY, SAD, etc."
                );
            }
        }
    }
}
