package ru.flproject.operator.process;

import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import ru.flproject.operator.function.SerializablePredicate;

import java.util.Map;

@RequiredArgsConstructor
public class StreamSpliterator<I> extends ProcessFunction<I, Object> {
    private static long serialVersionUID = 1L;

    private final Map<OutputTag<I>, SerializablePredicate<I>> predicatesByTags;
    private final OutputTag<I> defaultTag;

    @Override
    public void processElement(I value, ProcessFunction<I, Object>.Context ctx, Collector<Object> out){
        for(Map.Entry<OutputTag<I>, SerializablePredicate<I>> entry : predicatesByTags.entrySet()){
            final var predicate = entry.getValue();
            if(predicate.test(value)){
                final var tag = entry.getKey();
                ctx.output(tag, value);
                return;
            }
        }
        ctx.output(defaultTag, value);
    }
}
