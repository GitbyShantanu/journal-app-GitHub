package com.shantanu.journalApp.entity;

import lombok.*;
import com.shantanu.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(collection = "journal_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor //bcz lombok only contains ArgsConstructor, sometimes we need noArgs constructor, otherwise gives error
@Builder
public class JournalEntry {
    @Id
    private ObjectId id;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;

    private Sentiment sentiment;
}
