package com.shantanu.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
        @Id
        private ObjectId id;

        @Indexed(unique = true)
        @NonNull
        private String userName;

        @JsonProperty("email")
        private String email;

        @JsonProperty("sentimentAnalysis")
        private boolean sentimentAnalysis;

        @NonNull
        private String password;

        @DBRef
        private List<JournalEntry> journalEntryList = new ArrayList<>();

        private List<String> roles;
}
