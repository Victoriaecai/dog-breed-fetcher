package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher innerFetcher;
    private final HashMap<String, List<String>> cache;
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.innerFetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (!cache.containsKey(breed)) {
            callsMade++;
            try {
                cache.put(breed, innerFetcher.getSubBreeds(breed));
            } catch (BreedNotFoundException e) {
                throw e;
            }
        }
        return cache.get(breed);
    }


    public int getCallsMade() {
        return callsMade;
    }
}