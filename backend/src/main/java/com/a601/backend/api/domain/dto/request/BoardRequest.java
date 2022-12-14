package com.a601.backend.api.domain.dto.request;


import com.a601.backend.api.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardRequest {
    String email;
    String title;
    String contents;
    String gu;
    String dong;

    @Getter
    @Setter
    @Builder
    public static class modify{
        String email;
        String title;
        String contents;
    }
}
