package cn.dpc.domain.poster;

import cn.dpc.domain.RecordNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PostersImpl implements Posters {
    private static Map<Poster.PosterId, Poster> records = new ConcurrentHashMap<>();

    @Override
    public Mono<Poster> add(Poster poster) {
        if(null == poster.getId()) {
            poster.setId(new Poster.PosterId(UUID.randomUUID().toString()));
        }
        records.put(poster.getId(), poster);
        return Mono.just(poster);
    }

    @Override
    public Mono<Void> update(Poster.PosterId id, Poster poster) {
        if(!records.containsKey(id)) {
            return Mono.error(new RecordNotFoundException("Poster." + id));
        }
        poster.setId(id);
        records.put(id, poster);
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Poster.PosterId id) {
         records.remove(id);
         return Mono.empty();
    }

    @Override
    public Mono<Poster> getById(Poster.PosterId id) {
        if(!records.containsKey(id)) {
            return Mono.error(new RecordNotFoundException("Poster." + id));
        }

        return Mono.just(records.get(id));
    }

    @Override
    public Flux<Poster> listAll() {
        return Flux.fromIterable(records.values());
    }
}
