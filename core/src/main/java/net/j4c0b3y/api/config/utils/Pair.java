package net.j4c0b3y.api.config.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 7/02/2025
 */
@Getter
@RequiredArgsConstructor
public class Pair<L, R> {
    private final L left;
    private final R right;
}
