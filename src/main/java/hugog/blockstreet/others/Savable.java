package hugog.blockstreet.others;

public interface Savable<T> {

    void save();

    T load();

}
