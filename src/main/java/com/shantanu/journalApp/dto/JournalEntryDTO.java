package com.shantanu.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shantanu.journalApp.enums.Sentiment;

import javax.validation.constraints.NotNull;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalEntryDTO {
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Sentiment sentiment;
}
