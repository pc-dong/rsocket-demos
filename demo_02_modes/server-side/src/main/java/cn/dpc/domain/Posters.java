package cn.dpc.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Posters {
    Mono<Poster> add(Poster poster);

    Mono<Void> update(Poster.PosterId id, Poster poster);

    Mono<Void> delete(Poster.PosterId id);

    Mono<Poster> getById(Poster.PosterId id);

    Flux<Poster> listAll();
}
