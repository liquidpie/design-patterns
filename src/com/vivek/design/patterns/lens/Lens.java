package com.vivek.design.patterns.lens;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class Lens<A, B> {

    private final Function<A, B> function;
    private final BiFunction<A, B, A> biFunction;

    public Lens(final Function<A, B> function, final BiFunction<A, B, A> biFunction) {
        this.function = function;
        this.biFunction = biFunction;
    }

    public B get(final A a) {
        return function.apply(a);
    }

    public A set(final A a, final B b) {
        return biFunction.apply(a, b);
    }

    public A mod(final A a, final UnaryOperator<B> unaryOperator) {
        return set(a, unaryOperator.apply(get(a)));
    }

    public <C> Lens<C, B> compose(final Lens<C, A> that) {
        return new Lens<>(
                c -> get(that.get(c)),
                (c, b) -> that.mod(c, a -> set(a, b))
        );
    }

    public <C> Lens<A, C> andThen(final Lens<B, C> that) {
        return that.compose(this);
    }

}