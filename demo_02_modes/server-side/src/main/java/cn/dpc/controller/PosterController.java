package cn.dpc.controller;

import cn.dpc.config.annotation.DeleteMessageMapping;
import cn.dpc.config.annotation.GetMessageMapping;
import cn.dpc.config.annotation.PostMessageMapping;
import cn.dpc.config.annotation.PutMessageMapping;
import cn.dpc.domain.Poster;
import cn.dpc.domain.Posters;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("posters")
public class PosterController {
    private final Posters posters;

    @PostMessageMapping()
    public Mono<Poster> addNew(Poster poster) {
        return posters.add(poster);
    }

    @GetMessageMapping("{id}")
    public Mono<Poster> getById(@DestinationVariable("id") String id) {
        return posters.getById(new Poster.PosterId(id));
    }

    @PutMessageMapping("{id}")
    public Mono<Void> update(@DestinationVariable("id") String id, @Payload Poster poster) {
        return posters.update(new Poster.PosterId(id), poster);
    }

    @DeleteMessageMapping("{id}")
    public Mono<Void> update(@DestinationVariable("id") String id) {
        return posters.delete(new Poster.PosterId(id));
    }

    @GetMessageMapping()
    public Flux<Poster> listAll() {
        return posters.listAll();
    }
}
