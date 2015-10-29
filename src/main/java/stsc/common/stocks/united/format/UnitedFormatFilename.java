package stsc.common.stocks.united.format;

/**
 * Class used to store united format file system names. <br/>
 * Could be created only by {@link UnitedFormatHelper}.
 */
public final class UnitedFormatFilename implements Comparable<UnitedFormatFilename> {

	private final String filename;

	UnitedFormatFilename(final String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	public int compareTo(final UnitedFormatFilename other) {
		return this.getFilename().compareTo(other.getFilename());
	}

}
