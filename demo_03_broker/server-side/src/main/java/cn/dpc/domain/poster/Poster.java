package cn.dpc.domain.poster;

import lombok.*;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Poster {
    private PosterId id;

    private String title;

    private String content;

    @Data
    @AllArgsConstructor
    @ToString(of = "id")
    public static class PosterId {
        @NonNull
        String id;
    }
}
