package stsc.common.stocks;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class used for create correct and multi-operation-system accepted file
 * system storage for datafeeds.<br/>
 * For example windows has problem with storing files with names like 'aux' (and
 * it especially become a problem on java - because when you try to read such
 * file your thread get stuck).
 */
public final class UnitedFormatHelper {

	private final static String PREFIX = "_";
	private final static String EXTENSION = ".uf";

	private static final class Regexps {
		public static final Pattern notSymbolPrefix = Pattern.compile("^([\\^$#\\.])(.+)$");
	}

	private UnitedFormatHelper() {
	}

	/**
	 * returns '_'
	 */
	public final static String getPrefix() {
		return PREFIX;
	}

	/**
	 * returns '.uf'
	 */
	public final static String getExtension() {
		return EXTENSION;
	}

	/**
	 * TODO think how to get rid of this method <br/>
	 * _094FTSE.uf -> _094FTSE.uf
	 */
	public final static UnitedFormatFilename filesystemToFilesystem(final String filesystemName) {
		return new UnitedFormatFilename(filesystemName);
	}

	/**
	 * ^FTSE -> _094FTSE.uf
	 */
	public final static UnitedFormatFilename toFilesystem(final String stockName) {
		final Matcher matcher = Regexps.notSymbolPrefix.matcher(stockName);
		if (matcher.matches()) {
			final String code = matcher.group(1);
			final String postfix = matcher.group(2);
			final int c = (int) (code.charAt(0));
			final String prefix = String.format("_%03d", c);
			return new UnitedFormatFilename(prefix + postfix + getExtension());
		}
		return new UnitedFormatFilename(getPrefix() + stockName + getExtension());
	}

	public static String generatePath(final String folderPath, final UnitedFormatFilename fileName) {
		final Path filePath = FileSystems.getDefault().getPath(folderPath).resolve(fileName.getFilename());
		return filePath.toString();
	}

}
