package com.falette.backend.domain.loading;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "loading_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String text;

    public static LoadingMessage create(String text) {
        LoadingMessage m = new LoadingMessage();
        m.text = text;
        return m;
    }
}