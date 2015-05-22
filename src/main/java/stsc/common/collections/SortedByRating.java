package stsc.common.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

/**
 * Sort <T> by double key (from high to low [ 45, 18, 9 ]). Depending on your
 * comparator <C> that you will provide to constructor we will delete last
 * element.
 */
public class SortedByRating<T> {

	private final TreeMultimap<Double, T> storageByRating;

	public SortedByRating(final Comparator<T> comparatorForValue) {
		this.storageByRating = TreeMultimap.create(Ordering.natural().reverse(), comparatorForValue);
	}

	/**
	 * returns true if element was added
	 */
	public boolean addElement(Double rating, T value) {
		return storageByRating.put(rating, value);
	}

	/**
	 * returns true if element was deleted
	 */
	public boolean removeElement(Double rating, T value) {
		return storageByRating.remove(rating, value);
	}

	/**
	 * If there is possible to delete last element we will return it, otherwise
	 * we will return Optional.empty()
	 */
	public Optional<T> deleteLast() {
		if (storageByRating.isEmpty()) {
			return Optional.empty();
		}
		final Double keyToDelete = storageByRating.asMap().lastKey();
		final T elementToDelete = storageByRating.get(keyToDelete).last();

		if (storageByRating.remove(keyToDelete, elementToDelete)) {
			return Optional.ofNullable(elementToDelete);
		}
		return Optional.empty();
	}

	/**
	 * amount of elements
	 */
	public int size() {
		return storageByRating.size();
	}

	public SortedMap<Double, Collection<T>> getValues() {
		return storageByRating.asMap();
	}

	public List<T> getValuesAsList() {
		final List<T> result = new ArrayList<>();
		for (Entry<Double, Collection<T>> e : getValues().entrySet()) {
			result.addAll(e.getValue());
		}
		return result;
	}

	@Override
	public String toString() {
		return storageByRating.toString();
	}

}
